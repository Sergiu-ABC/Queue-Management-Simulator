package org.example.BusinessLogic;

import org.example.Model.Task;
import org.example.Model.Server;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private List<Thread> threads;
    private Strategy strategy;

    public Scheduler(int maxNoServers) {
        this.servers = new ArrayList<>();
        this.threads = new ArrayList<>();


        this.strategy = new ConcreteStrategyTime();


        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            servers.add(server);
            Thread t = new Thread(server);
            threads.add(t);
            t.start();
        }
    }


    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue()  ;
        } else {
            strategy = new ConcreteStrategyTime();
        }
    }

    public int dispatchClient(Task client) {

        int waitTime = getShortestWaitTimeForStats();


        strategy.addTask(servers, client);

        return waitTime;
    }

    private int getShortestWaitTimeForStats() {
        Server shortestQueue = servers.get(0);
        for (Server server : servers) {
            if (server.getWaitingPeriod() < shortestQueue.getWaitingPeriod()) {
                shortestQueue = server;
            }
        }
        return shortestQueue.getWaitingPeriod();
    }

    public List<Server> getServers() {
        return servers;
    }

    public void stopAllServers() {
        for (Server server : servers) {
            server.stopServer();
        }
        for (Thread t : threads) {
            t.interrupt();
        }
    }
}