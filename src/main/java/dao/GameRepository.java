package main.java.dao;

import main.java.domain.Game;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Path;

public class GameRepository extends AbstractRepository<Integer, Game>{
    public GameRepository(@Value("${SpringConfig.pathDbForGames}")Path fileDb) {
        super(fileDb);
    }

    @Override
    public Integer getId(Game element) {
        return element.getGameId();
    }
}
