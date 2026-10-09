package com.borcasergiu.queuesim;

import com.borcasergiu.queuesim.gui.Controller;
import com.borcasergiu.queuesim.gui.Viewer;

public class  Main {
    public static void main(String[] args) {
        Viewer viewer = new Viewer();
        Controller controller = new Controller(viewer);
    }
}