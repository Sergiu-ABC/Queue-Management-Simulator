package org.example.BusinessLogic;

import org.example.Model.Task;
import org.example.Model.Server;
import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task client) {
        Server shortestQueue = servers.get(0);
        for (Server server : servers) {
            if (server.getClients().length < shortestQueue.getClients().length) {
                shortestQueue = server;
            }
        }
        shortestQueue.addClient(client);
    }
}