package com.tjv;

import com.tjv.controller.BotState;
import com.tjv.model.Coordinate;

public class Wrapper {
    private char board [][] =  { {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}};

    public BotState state;
    private boolean firstStepUser;
    private String chatId;
    private char  yourSign;
    private char  oppositeSign;

    public Wrapper(String chatId){
        state = BotState.START_GAME;
        this.chatId = chatId;
    }
    public void cleanData(){
        state = BotState.START_GAME;
        board = new char[][]{{' ', ' ', ' '},
                        {' ', ' ', ' '},
                        {' ', ' ', ' '}};
    }
    public String getChatId() {
        return chatId;
    }

    public boolean isFirstStepUser() {
        return firstStepUser;
    }

    public void setFirstStepUser(boolean firstStepUser) {
        this.firstStepUser = firstStepUser;
        if (firstStepUser){
            yourSign = '0';
            oppositeSign = 'X';
        }else {
            yourSign = 'X';
            oppositeSign = '0';
        }
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
    public char[][] getBoard() {
        return board;
    }

    public boolean changeBoard(Coordinate coordinate, boolean user){
        if (board[coordinate.indexRow][coordinate.indexColumn] != ' '){
            return false;
        }
        if (user){
            board[coordinate.indexRow][coordinate.indexColumn] = oppositeSign;
        } else {
            board[coordinate.indexRow][coordinate.indexColumn] = yourSign;
        }
        return true;
    }
    public void setBoard(char[][] board) {
        this.board = board;
    }

}
