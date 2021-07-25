package com.tjv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Model {
    class Coordinate{
        int indexRow;
        int indexColumn;
        Coordinate(int x, int y){
            indexRow = x;
            indexColumn = y;
        }
    }
    static char board [][] =  { {' ', ' ', ' '},
                                {' ', ' ', ' '},
                                {' ', ' ', ' '}};
    private static char  yourSign = 'X';
    private static char  oppositeSign = '0';
    boolean endGame = false;
    Coordinate bestCoordinate;

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

    int score(){
        if(win() == 1) return 10;
        else if(win() == 0){ return -10; }
        return 0;
    }

    ArrayList<Coordinate> getStep(){
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

    void  printBoard(){
        System.out.println("-----------------");
        for (int i = 0; i < 3; i++){
            System.out.print("| ");
            for(int j = 0; j < 3; j++){
                System.out.print( board[i][j] + " | ");
            }
            System.out.println();
        }
        System.out.println("-----------------");
    }

    Coordinate readInput(){
        Scanner sc = new Scanner(System.in);
        int indexLine, indexColumn;
        while (true){
            indexLine = sc.nextInt();
            indexColumn = sc.nextInt();
            if (indexLine < 0 || indexColumn < 0 || indexLine > 2|| indexColumn > 2){
                System.out.println("This position is off the bounds of the board! Try again.");
            }else if (board[indexLine][indexColumn] != ' '){
                System.out.println("Someone has already made a move at this position! Try again.");
            }else { break; }
        }
        return new Coordinate(indexLine, indexColumn);
    }

    void clear(Coordinate coordinate){
        board[coordinate.indexRow][coordinate.indexColumn] = ' ';
    }

    int minimax(boolean maximizingPlayer, int depth){
        ArrayList<Coordinate> move = getStep();
        int resultGame = score();
        if(resultGame != 0 || move.isEmpty()){
            if (resultGame - depth == 9 || depth == 0) endGame = true;
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
        if (move.size() == 1 && depth == 0) endGame = true;
        return result;
    }

    void start(boolean computer){
        while(!endGame){
            if (computer){
                minimax(true, 0);
            }else {
                bestCoordinate = readInput();
            }
            moving(bestCoordinate,computer);
            printBoard();
            computer = !computer;
        }
        int resultGame = score();
        if (resultGame == 10){ System.out.println("I win"); }
        else if(resultGame == -10)  System.out.println("You win");
        else  System.out.println("Game is over");
    }

    static boolean chooseFirstStep(){
        Scanner sc = new Scanner(System.in);
        while(true){
            String st = sc.nextLine();
            if(st.equals("I")) return false;
            else if(st.equals("you")) return true;
            else { System.out.println("Wrong data"); }
        }
    }
}
