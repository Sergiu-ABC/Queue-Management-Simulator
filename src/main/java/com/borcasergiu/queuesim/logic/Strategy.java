package com.borcasergiu.queuesim.logic;

import com.borcasergiu.queuesim.model.Task;
import com.borcasergiu.queuesim.model.Server;
import java.util.List;

public interface Strategy {
    void addTask(List<Server> servers, Task client);
}