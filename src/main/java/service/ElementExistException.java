package main.java.service;

public class ElementExistException extends RuntimeException{
    public ElementExistException(){ super("Existing element cannot be inserted");}
}
