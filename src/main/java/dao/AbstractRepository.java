package main.java.dao;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class AbstractRepository<K, E>  {
    private final Path fileDatabasePath;
    private final Map<K, E> inMemoryDb = new HashMap<>();


    protected AbstractRepository(Path path) {
        this.fileDatabasePath = path;
        if (!Files.exists(fileDatabasePath)) {
            try {
                Files.createFile(fileDatabasePath);
            } catch (IOException e) {
                throw new CreatingDbException(e);
            }
        }
    }

    private void loadFromFile() {
        inMemoryDb.clear();

        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(fileDatabasePath))) {
            Object readIn;
            if ((readIn = inputStream.readObject()) != null) {
                if (readIn instanceof Map mapFromFile) {
                    inMemoryDb.putAll(mapFromFile);
                }
            }
        } catch (EOFException e) { //This exception is thrown when inputStream.readObject() tries to read past the end
            // of the input file. This is neither an error nor bug.
            System.err.println("Finished reading from " + fileDatabasePath);
        } catch (IOException e) {
            throw new FileDbIoException(e);
        } catch (ClassNotFoundException e) {
            throw new MissingClassException("Cannot load data from " + fileDatabasePath + ". Some class is missing or has a version mismatch.", e);
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(fileDatabasePath))) {
            outputStream.writeObject(inMemoryDb);
        } catch (IOException e) {
            System.err.println("Element does not save");;
        }
    }

    public abstract K getId(E element);
    public void createOrUpdate(E entity) {
        loadFromFile();
        inMemoryDb.put(getId(entity), Objects.requireNonNull(entity));
        saveToFile();
    }

    public Optional<E> readById(K id) {
        loadFromFile();
        return Optional.ofNullable(inMemoryDb.get(id)); // method get returns the value associated with the key id or null if the key does not exist
    }

    public Collection<E> readAll() {
        loadFromFile();
        return inMemoryDb.values();
    }

    public void deleteById(K id) {
        loadFromFile();
        inMemoryDb.remove(id);
        saveToFile();
    }

    public boolean exists(E entity) {
        loadFromFile();
        return inMemoryDb.containsKey(getId(entity));
    }

}
