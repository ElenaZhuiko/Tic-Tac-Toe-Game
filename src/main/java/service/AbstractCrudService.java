package main.java.service;

import main.java.dao.AbstractRepository;
import main.java.dao.CrudRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Optional;

public abstract class AbstractCrudService<K, E> {
    protected final CrudRepository<K, E> repository;

    protected AbstractCrudService(CrudRepository<K, E> repository) {
        this.repository = repository;
    }

    public void create(E element) throws ElementExistException {
        if (repository.exists(element))
            throw new ElementExistException();
        repository.createOrUpdate(element);
    }
    public void update(E element) throws ElementDoesNotExistException {
        if(repository.exists(element)){
            repository.createOrUpdate(element);
        }else{
            throw new ElementDoesNotExistException();
        }
    }

    public Optional<E> readById(K id){
        return repository.readById(id);
    }

    public Collection<E> readAll(){
        return repository.readAll();
    }
    public void deleteById(K id){
        repository.deleteById(id);
    }
}
