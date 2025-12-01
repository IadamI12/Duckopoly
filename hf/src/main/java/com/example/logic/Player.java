package com.example.logic;

import java.util.List;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class to store the players of the game
 * 
 * @author Pap Ádám
 */
public class Player implements Serializable {
    /**
     * The id of the player
     */
    private int id;
    /**
     * The name of the player
     */
    private String name;
    /**
     * The amount of money the player has
     */
    private int money;
    /**
     * The position of the player on the board
     */
    private int position;
    /**
     * The tiles owned by the player
     */
    private List<Tile> tiles;
    /**
     * Whether the player is in jail
     */
    private boolean jailed;
    /**
     * The jail time of the player
     */
    private int jailTime;
    /**
     * The color of the player's piece
     */
    private Color pieceColor;
    /**
     * Whether the player has lost the game
     */
    private boolean lost;

    /**
     * Constructor for the Player class
     * 
     * @param id         - the id of the player
     * @param name       - the name of the player
     * @param money      - the amount of money the player has
     * @param position   - the position of the player on the board
     * @param tiles      - the tiles owned by the player
     * @param jailed     - whether the player is in jail
     * @param pieceColor - the color of the player's piece
     * @param lost       - whether the player has lost the game
     */
    public Player(int id, String name, int money, int position, List<Tile> tiles, boolean jailed, Color pieceColor,
            boolean lost) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.position = position;
        this.tiles = new ArrayList<>();
        this.jailed = jailed;
        this.pieceColor = pieceColor;
        this.lost = lost;
    }

    /**
     * Getter for the player's id
     * 
     * @return int - the id of the player
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the player's name
     * 
     * @return String - the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the player's position on the board
     * 
     * @return int - the position of the player
     */
    public int getPosition() {
        return position;
    }

    /**
     * Getter for the player's money
     * 
     * @return int - the amount of money the player has
     */
    public int getMoney() {
        return money;
    }

    /**
     * Setter for the player's money
     * 
     * @param money - the new amount of money the player has
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Setter for the player's position on the board
     * 
     * @param position - the new position of the player
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Getter for the tiles owned by the player
     * 
     * @return {@code List<Tile>} - the tiles owned by the player
     */
    public List<Tile> getTiles() {
        return tiles;
    }

    /**
     * Setter for the tiles owned by the player
     * 
     * @param t - the tile to add to the player's tiles
     */
    public void setTiles(Tile t) {
        this.tiles.add(t);
    }

    /**
     * Setter for whether the player is in jail
     * 
     * @param jailed - true if the player is in jail, false otherwise
     */
    public void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    /**
     * Getter for whether the player is in jail
     * 
     * @return boolean - true if the player is in jail, false otherwise
     */
    public boolean getJailed() {
        return jailed;
    }

    /**
     * Setter for the jail time of the player
     * 
     * @param jailTime - the new jail time of the player
     */
    public void setJailTime(int jailTime) {
        this.jailTime = jailTime;
    }

    /**
     * Getter for the jail time of the player
     * 
     * @return int - the jail time of the player
     */
    public int getJailTime() {
        return jailTime;
    }

    /**
     * Getter for the color of the player's piece
     * 
     * @return Color - the color of the player's piece
     */
    public Color getPieceColor() {
        return pieceColor;
    }

    /**
     * Setter for the color of the player's piece
     * 
     * @param pieceColor - the new color of the player's piece
     */
    public void setPieceColor(Color pieceColor) {
        this.pieceColor = pieceColor;
    }

    /**
     * Getter for whether the player has lost the game
     * 
     * @return boolean - true if the player has lost, false otherwise
     */
    public boolean getLost() {
        return lost;
    }

    /**
     * Setter for whether the player has lost the game
     * 
     * @param lost - true if the player has lost, false otherwise
     */
    public void setLost(boolean lost) {
        this.lost = lost;
    }
}