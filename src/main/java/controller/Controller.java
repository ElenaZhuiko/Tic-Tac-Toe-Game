package main.java.controller;
import main.java.model.Coordinate;
import main.java.Wrapper;
import main.java.model.Model;
import main.java.view.VisualizationTelegram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Controller {
    private  Model model;
    private VisualizationTelegram view;

    @Autowired
    public Controller(Model model, VisualizationTelegram view){
        this.model = model;
        this.view = view;
        startGame();
    }

    public void startGame(){
        System.out.println("Game is running");
        view.run(this);
    }

    public void analysisAnswer(Wrapper answer){
        String chatId = answer.getChatId();
        int gameResult = checkBoard(answer.getBoard());
        if (gameResult != -1 ){
            answer.state = BotState.END_GAME;
        }
        if(answer.state.equals(BotState.START_GAME)){
            answer.state = BotState.CHOOSE_SIGH;
            view.chooseForChoice(chatId);
        }else if (answer.state.equals(BotState.USER_STEP)){
            view.printBoard("your turn", chatId);
        }else if (answer.state.equals(BotState.COMPUTER_STEP)){
            answer.changeBoard(model.step(answer), false);
            answer.state = BotState.USER_STEP;
            view.printBoard("computers step", chatId);
            gameResult = checkBoard(answer.getBoard());
            if (gameResult != -1){
                answer.state = BotState.END_GAME;
                view.endGame(gameResult, chatId);
            }
        }else if(answer.state.equals(BotState.END_GAME)){
            view.endGame(gameResult, chatId);
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
