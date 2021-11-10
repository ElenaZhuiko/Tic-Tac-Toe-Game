package main.java.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Game implements Serializable {
    private final int gameId;
    private final LocalDateTime created = LocalDateTime.now();
    private final User player;
    private Set<User> usersLiked = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return getGameId() == game.getGameId();
    }

    @Override
    public int hashCode() {
        return getGameId();
    }

    public Game(int gameId, User user) {
        this.gameId = gameId;
        this.player = user;
    }

    public void addLike(User user){
        usersLiked.add(user);
    }

    public int getGameId() {
        return gameId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public User getPlayer() {
        return player;
    }
}
