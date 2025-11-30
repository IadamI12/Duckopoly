package com.example;

import java.util.List;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
//A class to store the players of the game
public class Player implements Serializable{
    private int id;
    private String name;
    private int money;
    private int position;
    private List<Tile> tiles;
    private boolean jailed;
    private int jailTime;
    private Color pieceColor;
    private boolean lost;
    public Player(int id, String name, int money, int position, List<Tile> tiles, boolean jailed, Color pieceColor, boolean lost){
        this.id = id;
        this.name = name;
        this.money = money;
        this.position = position;
        this.tiles = new ArrayList<>();
        this.jailed = jailed;
        this.pieceColor = pieceColor;
        this.lost = lost;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getPosition(){
        return position;
    }
    public int getMoney(){
        return money;
    }
    public void setMoney(int money){
        this.money = money;
    }
    public void setPosition(int position){
        this.position = position;
    }
    public List<Tile> getTiles(){
        return tiles;
    }
    public void setTiles(Tile t){
        this.tiles.add(t);
    }
    public void setJailed(boolean jailed){
        this.jailed = jailed;
    }
    public boolean getJailed(){
        return jailed;
    }
    public void setJailTime(int jailTime){
        this.jailTime = jailTime;
    }
    public int getJailTime(){
        return jailTime;
    }
    public Color getPieceColor(){
        return pieceColor;
    }
    public void setPieceColor(Color pieceColor){
        this.pieceColor = pieceColor;
    }
    public boolean getLost(){
        return lost;
    }
    public void setLost(boolean lost){
        this.lost = lost;
    }
}