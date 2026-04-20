package org.example.BusinessLogic;

import org.example.Model.Task;
import org.example.Model.Server;
import java.util.List;

public interface Strategy {
    void addTask(List<Server> servers, Task client);
}