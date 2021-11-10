package main.java.domain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GameProgramAnnotation extends Game{
    private String title = "";
    public GameProgramAnnotation(int gameId, User user) {
        super(gameId, user);
    }

    @OnCreate
    public void beforePosted() {
        try {
        HttpRequest request = HttpRequest.newBuilder(new URI("https://github.com")).GET().build();
        HttpResponse<String> response = null;
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            title = "Github is  " + response.body().length() + " charts long";
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public void onDisplay() {

    }
}
