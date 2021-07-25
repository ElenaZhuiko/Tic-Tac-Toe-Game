package com.tjv;

import com.tjv.view.VisualizationImpl;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
//        solution.start( solution.chooseFirstStep());
        VisualizationImpl view = new VisualizationImpl();
        Controller controller = new Controller(model, view);
    }
}
