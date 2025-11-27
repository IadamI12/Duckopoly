package com.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bonus implements Serializable{
    private String bonus;
    private int money;
    private int profitType;
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
