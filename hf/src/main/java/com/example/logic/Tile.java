package com.example.logic;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class responsible for storing the tiles
 * 
 * @author Pap Ádám
 */
public class Tile implements Serializable {
    /**
     * The name of the tile
     */
    private String name;
    /**
     * The price of the tile
     */
    private int price;
    /**
     * The cost of a house on the tile
     */
    private int houseCost;
    /**
     * The tax of the tile
     */
    private int tax;
    /**
     * The number of houses on the tile
     */
    private int numberOfHouses;
    /**
     * The owner of the tile
     */
    private Player owner;
    /**
     * The color of the tile
     */
    private Color tileColor;
    /**
     * Whether the tile is purchasable
     */
    private boolean purchasable;
    /**
     * Whether houses can be purchased on the tile
     */
    private boolean housePurchasable;
    /**
     * The sell value of the tile
     */
    private int sell;
    /**
     * The players currently on the tile
     */
    private List<Player> playersOnTile;

    /**
     * Constructor for the Tile class
     * 
     * @param name             - namem of the tile
     * @param price            - price of the tile
     * @param houseCost        - cost of a house on the tile
     * @param tax              - tax of the tile
     * @param numberOfHouses   - number of houses on the tile
     * @param color            - color of the tile
     * @param purchasable      - whether the tile is purchasable
     * @param housePurchasable - whether houses can be purchased on the tile
     */
    public Tile(String name, int price, int houseCost, int tax, int numberOfHouses, String color, int purchasable,
            int housePurchasable) {
        this.name = name;
        this.price = price;
        this.houseCost = houseCost;
        this.tax = tax;
        this.numberOfHouses = numberOfHouses;
        // the owner being set to a basic one
        this.owner = new Player(100, "-", 0, 0, null, false, Color.BLACK, false);
        color = color.substring(1);
        int rgb = Integer.parseInt(color, 16);
        this.tileColor = new Color(rgb);
        if (purchasable == 0) {
            this.purchasable = false;
        } else {
            this.purchasable = true;
        }
        if (housePurchasable == 0) {
            this.housePurchasable = false;
        } else {
            this.housePurchasable = true;
        }
        this.playersOnTile = new ArrayList<>();
        this.sell = price / 2 + (numberOfHouses * houseCost) / 2;
    }

    /**
     * Getter for the name of the tile
     * 
     * @return String - the name of the tile
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the price of the tile
     * 
     * @return int - the price of the tile
     */
    public int getPrice() {
        return price;
    }

    /**
     * Getter for the house cost of the tile
     * 
     * @return int - the cost of a house on the tile
     */
    public int getHouseCost() {
        return houseCost;
    }

    /**
     * Getter for the tax of the tile
     * 
     * @return int - the tax of the tile
     */
    public int getTax() {
        return tax;
    }

    /**
     * Setter for the tax of the tile
     * 
     * @param tax - the new tax of the tile
     */
    public void setTax(int tax) {
        this.tax = tax;
    }

    /**
     * Getter for the number of houses on the tile
     * 
     * @return int - the number of houses on the tile
     */
    public int getNumberOfHouses() {
        return numberOfHouses;
    }

    /**
     * Setter for the number of houses on the tile
     * 
     * @param numberOfHouses - the new number of houses on the tile
     */
    public void setNumberOfHouses(int numberOfHouses) {
        this.numberOfHouses = numberOfHouses;
    }

    /**
     * Getter for the color of the tile
     * 
     * @return Color - the color of the tile
     */
    public Color getTileColor() {
        return tileColor;
    }

    /**
     * Getter for whether the tile is purchasable
     * 
     * @return boolean - whether the tile is purchasable
     */
    public boolean getPurchasable() {
        return purchasable;
    }

    /**
     * Setter for whether the tile is purchasable
     * 
     * @param result - the new purchasable status of the tile
     */
    public void setPurchasable(boolean result) {
        this.purchasable = result;
    }

    /**
     * Getter for whether houses can be purchased on the tile
     * 
     * @return boolean - whether houses can be purchased on the tile
     */
    public boolean getHousePurchasable() {
        return housePurchasable;
    }

    /**
     * Setter for whether houses can be purchased on the tile
     * 
     * @param result - the new house purchasable status of the tile
     */
    public void setHousePurchasable(boolean result) {
        this.housePurchasable = result;
    }

    /**
     * Getter for the owner of the tile
     * 
     * @return Player - the owner of the tile
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Setter for the owner of the tile
     * 
     * @param owner - the new owner of the tile
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Adding a player to the tile
     * 
     * @param player - the player to add
     */
    public void addPlayer(Player player) {
        if (!playersOnTile.contains(player)) {
            playersOnTile.add(player);
        }
    }

    /**
     * Removing a player from the tile
     * 
     * @param player - the player to remove
     */
    public void removePlayer(Player player) {
        playersOnTile.remove(player);
    }

    /**
     * Getter for the players on the tile
     * 
     * @return {@code List<Player>} - the players on the tile
     */
    public List<Player> getPlayers() {
        return playersOnTile;
    }

    /**
     * Getter for the sell value of the tile
     * 
     * @return int - the sell value of the tile
     */
    public int getSellValue() {
        return sell;
    }

    /**
     * Setter for the sell value of the tile
     * 
     * @param sell - the new sell value of the tile
     */
    public void setSellValue(int sell) {
        this.sell = sell;
    }
}
