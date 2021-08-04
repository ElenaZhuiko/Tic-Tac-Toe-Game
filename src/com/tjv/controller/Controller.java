package com.tjv.controller;
import com.tjv.Wrapper;
import com.tjv.model.Coordinate;
import com.tjv.model.Model;
import com.tjv.view.VisualizationImpl;
import java.util.ArrayList;

import static com.tjv.controller.BotState.*;

public class Controller {
    private final Model model;
    private final VisualizationImpl view;

    public Controller(Model model, VisualizationImpl view){
        this.model = model;
        this.view = view;
    }

    public void startGame(){
        System.out.println("Game is running");
        view.run(this);
    }

    public void analysisAnswer(Wrapper answer){
        int gameResult = checkBoard(answer.getBoard());
        if (gameResult != -1 ){
            answer.state = END_GAME;
        }
        if(answer.state.equals(START_GAME)){
            view.chooseForChoice();
        }else if (answer.state.equals(USER_STEP)){
            view.printBoard("your turn");
        }else if (answer.state.equals(COMPUTER_STEP)){
            answer.changeBoard(model.step(answer), false);
            answer.state = USER_STEP;
            view.printBoard("computers step");
            gameResult = checkBoard(answer.getBoard());
            if (gameResult != -1){
                answer.state = END_GAME;
                view.endGame(gameResult);
            }
        }else if(answer.state.equals(END_GAME)){
            view.endGame(gameResult);
        }
    }

    public int checkBoard(char [][] board){
        int result = model.score(board);
        if(result == 0) {
            ArrayList<Coordinate> steps = model.getStep();
            if (steps.size() == 0)
                return 0;
            else{
                return -1;
            }
        }
        return result;
    }

}
