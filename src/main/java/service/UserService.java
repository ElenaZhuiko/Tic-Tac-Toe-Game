package main.java.service;

import main.java.dao.CrudRepository;
import main.java.dao.UserRepository;
import main.java.domain.User;

public class UserService extends AbstractCrudService<String, User>{
    public UserService(CrudRepository<String, User> repository){
        super(repository);
    }
}
