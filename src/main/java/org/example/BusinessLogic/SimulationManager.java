package org.example.BusinessLogic;

import org.example.Model.Task;
import org.example.Model.Server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int minArrivalTime, maxArrivalTime;
    private int minServiceTime, maxServiceTime;
    private int numberOfServers;
    private int numberOfClients;

    private SelectionPolicy selectionPolicy;
    private Scheduler scheduler;
    private List<Task> generatedClients;
    private SimulationObserver observer;

    private int totalWaitTime = 0;
    private int totalServiceTime = 0;
    private int peakHour = 0;
    private int maxClientsAtPeak = 0;

    public SimulationManager(int n, int q, int tMax, int minArr, int maxArr, int minServ, int maxServ, SelectionPolicy policy) {
        this.numberOfClients = n;
        this.numberOfServers = q;
        this.timeLimit = tMax;
        this.minArrivalTime = minArr;
        this.maxArrivalTime = maxArr;
        this.minServiceTime = minServ;
        this.maxServiceTime = maxServ;
        this.selectionPolicy = policy;

        this.scheduler = new Scheduler(numberOfServers);
        this.scheduler.changeStrategy(selectionPolicy);

        this.generatedClients = new ArrayList<>();
        generateRandomClients();
    }

    public void setObserver(SimulationObserver observer) {
        this.observer = observer;
    }

    private void logMessage(PrintWriter writer, String message) {
        if (observer != null) {
            observer.updateLog(message);
        }
        if (writer != null) {
            writer.println(message);
        }
        System.out.println(message);
    }

    private void generateRandomClients() {
        Random random = new Random();
        for (int i = 1; i <= numberOfClients; i++) {
            int arrTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int servTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;

            Task newClient = new Task(i, arrTime, servTime);
            generatedClients.add(newClient);

            totalServiceTime += servTime;
        }
        Collections.sort(generatedClients);
    }

    @Override
    public void run() {
        int currentTime = 0;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = dtf.format(LocalDateTime.now());
        String fileName = "log_of_events_" + timestamp + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            while (currentTime <= timeLimit) {

                Iterator<Task> iterator = generatedClients.iterator();
                while (iterator.hasNext()) {
                    Task c = iterator.next();
                    if (c.getArrivalTime() <= currentTime) {
                        totalWaitTime += scheduler.dispatchClient(c);
                        iterator.remove();
                    }
                }

                logMessage(writer, "Time " + currentTime);

                StringBuilder waitingStr = new StringBuilder("Waiting clients: ");
                for (Task c : generatedClients) {
                    waitingStr.append(c.toString()).append(" ");
                }
                logMessage(writer, waitingStr.toString());

                boolean allServersEmpty = true;
                int currentTotalClientsInQueues = 0;

                for (int i = 0; i < scheduler.getServers().size(); i++) {
                    Server s = scheduler.getServers().get(i);
                    Task[] clientsInQueue = s.getClients();

                    currentTotalClientsInQueues += clientsInQueue.length;

                    StringBuilder queueStr = new StringBuilder("Queue " + (i + 1) + ": ");
                    if (clientsInQueue.length == 0) {
                        queueStr.append("closed");
                    } else {
                        allServersEmpty = false;
                        for (Task c : clientsInQueue) {
                            queueStr.append(c.toString()).append(" ");
                        }
                    }
                    logMessage(writer, queueStr.toString());
                }

                if (currentTotalClientsInQueues > maxClientsAtPeak) {
                    maxClientsAtPeak = currentTotalClientsInQueues;
                    peakHour = currentTime;
                }

                logMessage(writer, "--------------------------------------------------");

                if (generatedClients.isEmpty() && allServersEmpty) {
                    break;
                }

                currentTime++;
                Thread.sleep(1000);
            }

            double averageWaitTime = (double) totalWaitTime / numberOfClients;
            double averageServiceTime = (double) totalServiceTime / numberOfClients;

            logMessage(writer, "\n--- SIMULATION FINISHED ---");
            logMessage(writer, "Average waiting time: " + String.format("%.2f", averageWaitTime));
            logMessage(writer, "Average service time: " + String.format("%.2f", averageServiceTime));
            logMessage(writer, "Peak hour: " + peakHour + " (with " + maxClientsAtPeak + " clients in queues)");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            scheduler.stopAllServers();
        }
    }
}