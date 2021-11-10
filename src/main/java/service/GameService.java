package main.java.service;

import main.java.dao.CrudRepository;
import main.java.dao.GameRepository;
import main.java.domain.Game;
import main.java.domain.OnCreate;
import main.java.domain.ProgrammableContribution;
import main.java.domain.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public class GameService extends AbstractCrudService<Integer, Game>{
    private final UserService userService;
    public GameService(CrudRepository<Integer, Game> repository, UserService userService){
        super(repository);
        this.userService = userService;
    }

    @Override
    public void deleteById(Integer id){
        throw new UnsupportedOperationException("Game cannot be deleted");
    }

    public void addLike(Game game, User player){
        Objects.requireNonNull(game);
        Objects.requireNonNull(player);
        game.addLike(player);
        update(game);
    }
    public void addLike(Integer gameId, String username) throws IllegalArgumentException{
        Optional<Game> gameOptional = readById(gameId);
        if (gameOptional.isEmpty()){
            throw new IllegalArgumentException("Game does not exist");
        }
        Optional<User> userOptional = userService.readById(username);
        if (userOptional.isEmpty()){
            throw new IllegalArgumentException("User does not exist");
        }

        addLike(gameOptional.get(), userOptional.get());
    }

    @Override
    public void create(Game element) throws ElementExistException {
        super.create(element);
//        if (element instanceof ProgrammableContribution prog){
//            prog.onCreate();
//        }
        for (Method  method : element. getClass().getMethods()){
            if (method.isAnnotationPresent(OnCreate.class)){
                try {
                    method.invoke(element);
                }catch (IllegalAccessException | InvocationTargetException e){
                    System.err.println("");
                }
            }
        }
    }
    
    public  void create(String username, Integer gameID) throws ElementExistException {
        Optional<User> optionalUser = userService.readById(username);
        if(optionalUser.isPresent()){
             create(new Game(gameID, optionalUser.get()));
        }else {
             throw new IllegalArgumentException("invalid username");
        }
    }
}
