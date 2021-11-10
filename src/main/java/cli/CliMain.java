package main.java.cli;

import main.java.domain.Game;
import main.java.domain.User;
import main.java.service.ElementExistException;
import main.java.service.GameService;
import main.java.service.UserService;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Consumer;


public class CliMain {
    private UserService userService;
    private GameService gameService;

    public static void main(String[] args) {
        CliMain cliMain = new CliMain();
        String username = "a";
        int gameId = 0;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-u" -> username = args[i + 1];
                case "-au" -> cliMain.addUser(username);
                case "-al" -> cliMain.addLike(gameId, username);
                case "-af" -> cliMain.addGame(username, gameId);
                case "-lu" -> cliMain.printGamesByUser(username);
            }
        }
    }


    private void printGamesByUser(String username) {
        try {
            Collection<Game> games = gameService.readAll();
            games.stream().filter(p -> p.getPlayer().getUsername().equals(username)).forEach(System.out::println); // stream nad kolekcima na funkcionalni interface ktery ma methodu accept
                                                                        // (game -> { System.out.println(game);}) - lambda vyraz
        }catch( NoSuchElementException e){

        }
    }

    private void addGame(String username, Integer gameId) {
        try {
            gameService.create(username, gameId);
        } catch (ElementExistException e) {
            System.err.println("Game exists. This should not happen");
        }catch (IllegalArgumentException e){
            System.err.println("invalid username");
        }
    }

    private void addLike(Integer gameId, String username) {
        try {
            gameService.addLike(gameId, username);
        } catch (IllegalArgumentException e) {
            System.err.println("Game and user must exist");
        }
    }

    private void addUser(String username) {
        try {
            userService.create(new User(username));
        } catch (ElementExistException e) {
            System.err.println("User " + username + " already exist");
        }
    }
}
