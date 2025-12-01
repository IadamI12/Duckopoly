package com.example.logic;

import java.io.Serializable;

/**
 * A class to store the bonus tiles of the game
 * 
 * @author Pap Ádám
 */
public class Bonus implements Serializable {
    /**
     * The line shown on the screen
     */
    private String bonus;
    /**
     * The amount of money involved
     */
    private int money;
    /**
     * The type of profit (0 = gain, 1 = loss, 2 = getfromall, 3 = payall)
     */
    private int profitType;

    /**
     * Constructor for the Bonus class
     * 
     * @param bonus      - the line shown on the screen
     * @param money      - the amount of money involved
     * @param profitType - the type of profit
     */
    public Bonus(String bonus, int money, int profitType) {
        this.bonus = bonus;
        this.money = money;
        this.profitType = profitType;
    }

    /**
     * Getter for the bonus line
     * 
     * @return String - the bonus line
     */
    public String getBonus() {
        return bonus;
    }

    /**
     * Getter for the amount of money involved
     * 
     * @return int - the amount of money involved
     */
    public int getMoney() {
        return money;
    }

    /**
     * Getter for the type of profit
     * 
     * @return int - the type of profit
     */
    public int getProfitType() {
        return profitType;
    }

}
