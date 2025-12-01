package com.example.exception;

/**
 * A class handling a player winning
 * 
 * @author Pap Ádám
 */
public class PlayerWon extends Exception {
    /**
     * Constructor for the PlayerWon exception
     * 
     * @param message - the message displayed when the exception is thrown
     */
    public PlayerWon(String message) {
        super(message);
    }
}