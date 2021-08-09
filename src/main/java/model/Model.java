package main.java.model;

import main.java.Wrapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class Model {
    private char board [][];
    private char  yourSign;
    private char  oppositeSign;
    private Coordinate bestCoordinate;

    public void setSign(boolean choice){
        if (!choice){
            yourSign = 'X';
            oppositeSign = '0';
            return;
        }
        yourSign = '0';
        oppositeSign = 'X';
    }

    char checkRowAndColumn(int dir){
        char repeatChar;
        Set<Character> set = new HashSet<>();
        for(int i = 0; i < 3; i++) {
            if (dir == 0) {
                repeatChar = board[i][0];
                set.add(board[i][0]);
                set.add(board[i][1]);
                set.add(board[i][2]);
            } else {
                repeatChar = board[0][i];
                set.add(board[0][i]);
                set.add(board[1][i]);
                set.add(board[2][i]);
            }
            if (set.size() == 1 && !set.contains(' ')){
                return repeatChar;
            }
            set.clear();
        }
        return '.';
    }

    char checkDiagonal(){
        Set<Character> set = new HashSet<>();
        for(int i = 0; i < 3; i++){
            set.add(board[i][i]);
        }
        if (set.size() == 1 && !set.contains(' '))
            return board[0][0];
        set.clear();
        for(int i = 0; i < 3; i++){
            set.add(board[i][2-i]);
        }
        if (set.size() == 1 && !set.contains(' '))
            return board[0][2];
        return '.';
    }
    int win(){
        char winChar = checkRowAndColumn(0), winChar2 = checkRowAndColumn(1), winChar3 = checkDiagonal();
        if (winChar == yourSign || winChar2== yourSign || winChar3==yourSign) return 1;
        if (winChar == oppositeSign || winChar2 == oppositeSign || winChar3 == oppositeSign) return 0;
        return -1;
    }

    public int score(char [][] boardGame){
        board = boardGame;
        if(win() == 1) return 10;
        else if(win() == 0){ return -10; }
        return 0;
    }

    public ArrayList<Coordinate> getStep(){
        ArrayList<Coordinate> step = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[i][j] == ' '){
                    step.add(new Coordinate(i, j));
                }
            }
        }
        return step;
    }

    void moving(Coordinate coordinate, boolean computer){
        if (computer){
            board[coordinate.indexRow][coordinate.indexColumn] = yourSign;
            return;
        }
        board[coordinate.indexRow][coordinate.indexColumn] = oppositeSign;
    }

    void clear(Coordinate coordinate){
        board[coordinate.indexRow][coordinate.indexColumn] = ' ';
    }

    int minimax(boolean maximizingPlayer, int depth){
        ArrayList<Coordinate> move = getStep();
        int resultGame = score(board);
        if(resultGame != 0 || move.isEmpty()){
            return resultGame - depth;
        }

        int result;
        if (maximizingPlayer){
            result = -1000;
            for (Coordinate value : move) {
                moving(value, true);
                int newResult = minimax(false, depth + 1);
                if (newResult > result){
                    result = newResult;
                    if (depth == 0)
                        bestCoordinate = value;
                }
                clear(value);
            }
        }else {
            result = 1000;
            for (Coordinate value : move) {
                moving(value, false);
                int newResult = minimax(true, depth + 1);
                if (newResult < result){
                    result = newResult;
                    if (depth == 0)
                        bestCoordinate = value;
                }
                clear(value);
            }
        }
        return result;
    }

    public Coordinate step(Wrapper answer){
        board = answer.getBoard();
        setSign(answer.isFirstStepUser());
        minimax(true, 0);
        return bestCoordinate;
    }

}
