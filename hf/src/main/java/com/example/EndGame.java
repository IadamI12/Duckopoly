package com.example;

public class EndGame  extends Exception{
    //Throwing it at the end of the game, if a player hasn't won
    public EndGame(String message){
        super(message);
    }
}
