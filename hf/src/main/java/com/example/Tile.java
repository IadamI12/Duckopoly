package com.example;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tile implements Serializable{
    private String name;
    private int price;
    private int houseCost;
    private int tax;
    private int numberOfHouses;
    private Player owner;
    private Color tileColor;
    private boolean purchasable;
    private boolean housePurchasable;
    private int sell;

    private List<Player> playersOnTile;
    public Tile(String name,int price,int houseCost, int tax, int numberOfHouses,String color,int purchasable, int housePurchasable){
        this.name = name;
        this.price = price;
        this.houseCost = houseCost;
        this.tax = tax;
        this.numberOfHouses = numberOfHouses;
        this.owner = new Player(100, "-", 0, 0, null,false, Color.BLACK,false);
        color = color.substring(1);
        int rgb = Integer.parseInt(color,16);
        this.tileColor = new Color(rgb);
        if (purchasable == 0){
            this.purchasable = false;
        }
        else{
            this.purchasable = true;
        }
        if (housePurchasable == 0){
            this.housePurchasable = false;
        }
        else{
            this.housePurchasable = true;
        }
        this.playersOnTile = new ArrayList<>();
        this.sell = price / 2 + (numberOfHouses * houseCost) / 2;
    }
    public String getName(){
        return name;
    }
    public int getPrice(){
        return price;
    }
    public int getHouseCost(){
        return houseCost;
    }
    public int getTax(){
        return tax;
    }
    public void setTax(int tax){
        this.tax = tax;
    }
    public int getNumberOfHouses(){
        return numberOfHouses;
    }
    public void setNumberOfHouses(int numberOfHouses){
        this.numberOfHouses = numberOfHouses;
    }
    public Color getTileColor(){
        return tileColor;
    }
    public boolean getPurchasable(){
        return purchasable;
    }
    public void setPurchasable(boolean result){
        this.purchasable = result;
    }
    public boolean getHousePurchasable(){
        return housePurchasable;
    }
    public void setHousePurchasable(boolean result){
        this.housePurchasable = result;
    }    
    public Player getOwner(){
        return owner;
    }
    public void setOwner(Player owner){
        this.owner = owner;
    }
    public void addPlayer(Player player){
        if (!playersOnTile.contains(player)){
            playersOnTile.add(player);
        }
    }
    public void removePlayer(Player player){
        playersOnTile.remove(player);
    }
    public List<Player> getPlayers(){
        return playersOnTile;
    }
    public int getSellValue(){
        return sell;
    }
    public void setSellValue(int sell){
        this.sell = sell;
    }
}
