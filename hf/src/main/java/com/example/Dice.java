package com.example;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable{
    //private int value;
    private Random random = new Random();
    public int roll(){
        int result = random.nextInt(6)+1;
       // value = result;
        return result;
    }
    // public int getValue(){
    //     return value;
    // }

}
