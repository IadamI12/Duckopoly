package com.example;

import java.io.Serializable;
public class Bonus implements Serializable{
    private String bonus;
    private int money;
    private int profitType;
    //Setting up the bonusTiles, whats the line shown on the screen, whats the amount and who gets it
    public Bonus(String bonus, int money, int profitType){
        this.bonus = bonus;
        this.money = money;
        this.profitType = profitType;
    }
    public String getBonus(){
        return bonus;
    }
    public int getMoney(){
        return money;
    }
    public int getProfitType(){
        return profitType;
    }

    
}
