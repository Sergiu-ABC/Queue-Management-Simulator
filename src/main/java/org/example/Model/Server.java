package org.example.Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;

    public Server() {
        this.clients = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
    }

    public void addClient(Client newClient) {
        clients.add(newClient);
        waitingPeriod.addAndGet(newClient.getServiceTime());
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public Client[] getClients() {
        Client[] currentClients = new Client[clients.size()];
        clients.toArray(currentClients);
        return currentClients;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Client currentClient = clients.take();



                waitingPeriod.addAndGet(-currentClient.getServiceTime());

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}