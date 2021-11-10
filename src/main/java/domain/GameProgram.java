package main.java.domain;

import javax.print.URIException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GameProgram extends Game implements  ProgrammableContribution{
    private String title = "";
    public GameProgram(int gameId, User user) {
        super(gameId, user);
    }

    @Override
    public void onCreate() {
        try {
        HttpRequest request = HttpRequest.newBuilder(new URI("https://github.com")).GET().build();
        HttpResponse<String> response = null;
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            title = "Github is  " + response.body().length() + " charts long";
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisplay() {

    }
}
