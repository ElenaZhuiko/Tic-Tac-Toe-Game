package main.java.dao;

public class MissingClassException extends RuntimeException {
    public MissingClassException(String s, Throwable e) {
        super(s, e);
    }
}
