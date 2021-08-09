package com.tjv.view;

import com.tjv.Wrapper;
import com.tjv.controller.Controller;
import com.tjv.model.Coordinate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.*;

import static com.tjv.controller.BotState.*;

@Component
public class VisualizationImpl  extends TelegramLongPollingBot implements Visualization{
    private Controller controller;
    private Map<String, Wrapper> clients;

    static { ApiContextInitializer.init(); }

    public void run(Controller controller){
        this.controller = controller;
        clients = new HashMap<>();
        TelegramBotsApi BotApi = new TelegramBotsApi();
        try {
            BotApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        System.out.println("Bot is running");
    }

    public void printBoard(String text, String id){
        final InlineKeyboardMarkup replyKeyboardMarkup = makeKeyBoardForGame(id);
        sendMessage(text, id, replyKeyboardMarkup);
    }

    InlineKeyboardMarkup  makeKeyBoardForGame(String id){
        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        char [][] board = clients.get(id).getBoard();
        rowInline1.add(new InlineKeyboardButton("| " + board[0][0] + " |").setCallbackData("0_0"));
        rowInline1.add(new InlineKeyboardButton("| " + board[0][1] + " |").setCallbackData("0_1"));
        rowInline1.add(new InlineKeyboardButton("| " + board[0][2] + " |").setCallbackData("0_2"));

        rowInline2.add(new InlineKeyboardButton("| " + board[1][0] + " |").setCallbackData("1_0"));
        rowInline2.add(new InlineKeyboardButton("| " + board[1][1] + " |").setCallbackData("1_1"));
        rowInline2.add(new InlineKeyboardButton("| " + board[1][2] + " |").setCallbackData("1_2"));

        rowInline3.add(new InlineKeyboardButton("| " + board[2][0] + " |").setCallbackData("2_0"));
        rowInline3.add(new InlineKeyboardButton("| " + board[2][1] + " |").setCallbackData("2_1"));
        rowInline3.add(new InlineKeyboardButton("| " + board[2][2] + " |").setCallbackData("2_2"));

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        replyKeyboardMarkup.setKeyboard(rowsInline);
        return  replyKeyboardMarkup;
    }

    InlineKeyboardMarkup makeKeyBoardForChoiceFirstStep(){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("I choose X").setCallbackData("user_step_first"));
        rowInline.add(new InlineKeyboardButton().setText("I choose 0").setCallbackData("computer_step_first"));

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    InlineKeyboardMarkup makeKeyBoardForNewGame(){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Yes").setCallbackData("new_game"));
        rowInline.add(new InlineKeyboardButton().setText("No").setCallbackData("end_game"));

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public void chooseForChoice(String id){
        InlineKeyboardMarkup markupInline = makeKeyBoardForChoiceFirstStep();
        sendMessage( "Game starts! Choose choice",id, markupInline);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String message_text = update.getMessage().getText();
            if (message_text.equals("/start")) {
                clients.put(chatId, new Wrapper(chatId));
                controller.analysisAnswer(clients.get(chatId));
            } else {
                sendMessage( "Nothing. Try again. '/start'", chatId);
            }

        } else if (update.hasCallbackQuery()) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String call_data = update.getCallbackQuery().getData();
            Wrapper answer = clients.get(chatId);
            if (answer == null){
                sendMessage("Wrong", chatId );
            }else {
                if (call_data.equals("user_step_first") && answer.state.equals(CHOOSE_SIGH)) {
                    sendMessage("You are first", chatId);
                    answer.setFirstStepUser(true);
                    answer.state = USER_STEP;
                } else if (call_data.equals("computer_step_first") && answer.state.equals(CHOOSE_SIGH)) {
                    sendMessage("Computer is first", chatId);
                    answer.setFirstStepUser(false);
                    answer.state = COMPUTER_STEP;
                } else if (call_data.matches(".*\\d.*") && answer.state.equals(USER_STEP)) {
                    Coordinate coordinate = new Coordinate(Integer.parseInt(call_data.split("_")[0]),
                            Integer.parseInt(call_data.split("_")[1]));
                    if (answer.changeBoard(coordinate, true)) {
                        printBoard("Your step", chatId);
                        answer.state = COMPUTER_STEP;
                    } else {
                        sendMessage("Try again", chatId);
                    }
                } else if (call_data.equals("new_game")) {
                    answer.state = START_GAME;
                } else if (call_data.equals("end_game")) {
                    answer.state = WAIT;
                }
                controller.analysisAnswer(answer);
            }
        }
    }


    public void sendMessage(String text, String id){
        try {
            execute(new SendMessage(id, text));
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void sendMessage(String text, String id, InlineKeyboardMarkup keyboard){
        try {
            execute(new SendMessage(id, text).setReplyMarkup(keyboard));
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void sendMessage(String text, String id, ReplyKeyboardMarkup keyboard){
        try {
            execute(new SendMessage(id, text).setReplyMarkup(keyboard));
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void endGame(int result, String id){
        if(result == 10){
            sendMessage("I win", id);
        }else if(result == -10){
            sendMessage("You win", id);
        }else {
            sendMessage("Game is over", id);
        }
        InlineKeyboardMarkup newGameAsk = makeKeyBoardForNewGame();
        sendMessage("Do you want a new game?",id, newGameAsk);
        clients.get(id).cleanData();
    }
    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

}
