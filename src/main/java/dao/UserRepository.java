package main.java.dao;

import main.java.domain.User;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Path;

public class UserRepository extends AbstractRepository<String, User>{
    public UserRepository(@Value("${SpringConfig.pathDbForUsers}")Path fileDb) {
        super(fileDb);
    }


    @Override
    public String getId(User element) {
        return element.getUsername();
    }
}
