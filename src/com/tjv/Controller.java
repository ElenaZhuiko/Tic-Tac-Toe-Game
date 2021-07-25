package com.tjv;
import com.tjv.view.*;

public class Controller {
   private Model model;
    private VisualizationImpl view;

    Controller(Model model, VisualizationImpl view){
        this.model = model;
        this.view = view;
    }
}
