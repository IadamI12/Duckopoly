package com.example.logic;

import java.io.Serializable;
import java.util.Random;

/**
 * A class to imitate a dice roll
 * 
 * @author Pap Ádám
 */
public class Dice implements Serializable {
    /**
     * Default constructor for the Dice class
     */
    public Dice() {
    }

    /**
     * Random number generator
     */
    private Random random = new Random();

    /**
     * Imitating a dice roll
     * 
     * @return int - the result of the dice roll (1-6)
     */
    public int roll() {
        int result = random.nextInt(6) + 1;
        return result;
    }
}
