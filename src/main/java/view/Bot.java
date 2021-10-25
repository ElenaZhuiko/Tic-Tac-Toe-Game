package main.java.view;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

public class Bot extends TelegramLongPollingBot {
    @Value("${VisualizationTelegram.TOKEN}")
    private String TOKEN;
    @Value("${VisualizationTelegram.BOT_NAME}")
    private String BOT_NAME;
    static { ApiContextInitializer.init(); }

    Bot(){
        TelegramBotsApi BotApi = new TelegramBotsApi();
        try {
            BotApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        System.out.println("Bot is running");
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
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String chatId = update.getMessage().getChatId().toString();
//            String message_text = update.getMessage().getText();
//            if (message_text.equals("/start")) {
//                clients.put(chatId, new model.Wrapper(chatId));
//                controller.analysisAnswer(clients.get(chatId));
//            } else {
//                sendMessage( "Nothing. Try again. '/start'", chatId);
//            }
//
//        } else if (update.hasCallbackQuery()) {
//            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
//            String call_data = update.getCallbackQuery().getData();
//            model.Wrapper answer = clients.get(chatId);
//            if (answer == null){
//                sendMessage("Wrong", chatId );
//            }else {
//                if (call_data.equals("user_step_first") && answer.state.equals(CHOOSE_SIGH)) {
//                    sendMessage("You are first", chatId);
//                    answer.setFirstStepUser(true);
//                    answer.state = USER_STEP;
//                } else if (call_data.equals("computer_step_first") && answer.state.equals(CHOOSE_SIGH)) {
//                    sendMessage("Computer is first", chatId);
//                    answer.setFirstStepUser(false);
//                    answer.state = COMPUTER_STEP;
//                } else if (call_data.matches(".*\\d.*") && answer.state.equals(USER_STEP)) {
//                    Coordinate coordinate = new Coordinate(Integer.parseInt(call_data.split("_")[0]),
//                            Integer.parseInt(call_data.split("_")[1]));
//                    if (answer.changeBoard(coordinate, true)) {
//                        printBoard("Your step", chatId);
//                        answer.state = COMPUTER_STEP;
//                    } else {
//                        sendMessage("Try again", chatId);
//                    }
//                } else if (call_data.equals("new_game")) {
//                    answer.state = START_GAME;
//                } else if (call_data.equals("end_game")) {
//                    answer.state = WAIT;
//                }
//                controller.analysisAnswer(answer);
//            }
//        }
//    }

    @Override
    public void onUpdateReceived(Update update) {

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
