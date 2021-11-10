package main.java.dao;

import main.java.domain.User;

import java.nio.file.Path;

public class UserRepository extends AbstractRepository<String, User>{
    public UserRepository() {
        super(Path.of("users.bin"));
    }


    @Override
    public String getId(User element) {
        return element.getUsername();
    }
}
