package main.java.dao;

import main.java.domain.Game;

import java.nio.file.Path;

public class GameRepository extends AbstractRepository<Integer, Game>{
    public GameRepository() {
        super(Path.of("games.bin"));
    }

    @Override
    public Integer getId(Game element) {
        return element.getGameId();
    }
}
