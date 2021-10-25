package main.java.view;


import main.java.controller.Controller;

public interface Visualization {
    void run(Controller controller);
    void printBoard(String text, String chatId);
    void chooseForChoice(String id);
    void endGame(int result, String id);
}
