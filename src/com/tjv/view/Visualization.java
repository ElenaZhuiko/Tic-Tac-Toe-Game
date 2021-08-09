package com.tjv.view;

import com.tjv.controller.Controller;

public interface Visualization {
    void run(Controller controller);
    void printBoard(String text, String chatId);
}
