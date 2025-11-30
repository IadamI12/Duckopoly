package com.example;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable{
    private Random random = new Random();
    //Imitating a dice roll
    public int roll(){
        int result = random.nextInt(6)+1;
        return result;
    }
}
