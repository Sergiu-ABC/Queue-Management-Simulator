package org.example.Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> clients;
    private AtomicInteger waitingPeriod;
    private volatile boolean isRunning = true;

    public Server() {
        this.clients = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
    }

    public void addClient(Task newClient) {
        clients.add(newClient);
        waitingPeriod.addAndGet(newClient.getServiceTime());
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public Task[] getClients() {
        Task[] currentClients = new Task[clients.size()];
        return clients.toArray(currentClients);
    }

    public void stopServer() {
        this.isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Task currentClient = clients.peek();

                if (currentClient != null) {
                    Thread.sleep(1000);

                    currentClient.setServiceTime(currentClient.getServiceTime() - 1);
                    waitingPeriod.decrementAndGet();

                    if (currentClient.getServiceTime() <= 0) {
                        clients.take();
                    }
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}