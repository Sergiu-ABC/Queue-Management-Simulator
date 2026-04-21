package org.example;

import org.example.GUI.Controller;
import org.example.GUI.Viewer;

public class  Main {
    public static void main(String[] args) {
        Viewer viewer = new Viewer();
        Controller controller = new Controller(viewer);
    }
}