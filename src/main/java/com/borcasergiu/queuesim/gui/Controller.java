package com.borcasergiu.queuesim.gui;
import com.borcasergiu.queuesim.logic.SimulationManager;
import com.borcasergiu.queuesim.logic.SelectionPolicy;

public class Controller {
    private Viewer viewer;

    public Controller(Viewer viewer) {
        this.viewer = viewer;


        this.viewer.getStartButton().addActionListener(e -> startSimulation());
    }

    private void startSimulation() {
        try {
            int n = viewer.getClients();
            int q = viewer.getServers();
            int tMax = viewer.getTimeLimit();
            int minArr = viewer.getMinArr();
            int maxArr = viewer.getMaxArr();
            int minServ = viewer.getMinServ();
            int maxServ = viewer.getMaxServ();

            SelectionPolicy policy = (viewer.getStrategyIndex() == 0) ?
                    SelectionPolicy.SHORTEST_TIME : SelectionPolicy.SHORTEST_QUEUE;


            SimulationManager engine = new SimulationManager(n, q, tMax, minArr, maxArr, minServ, maxServ, policy);


            engine.setObserver(viewer);

            Thread t = new Thread(engine);
            t.start();

            viewer.getStartButton().setEnabled(false);

        } catch (NumberFormatException ex) {
            viewer.updateLog("Error: Please enter valid integers in all fields.");
        }
    }
}