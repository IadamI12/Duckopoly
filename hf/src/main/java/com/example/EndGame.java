package com.example;

/**
 * A class handling the end of the game when no player has won
 * 
 * @author Pap Ádám
 */
public class EndGame extends Exception {
    /**
     * Constructor for the EndGame exception
     * 
     * @param message - the message to be displayed when the exception is thrown
     */
    public EndGame(String message) {
        super(message);
    }
}
