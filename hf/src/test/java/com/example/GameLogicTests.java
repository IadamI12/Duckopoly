package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.data.Bonus;
import com.example.data.Dice;
import com.example.data.Player;
import com.example.data.Tile;
import com.example.logic.GameLogic;

import java.awt. Color;
import java.util.ArrayList;
import java.util.List;
public class GameLogicTests {

    private List<Player> players;
    private List<Tile> tiles;
    private List<Bonus> bonusTiles;
    private List<Dice> dice;
    private GameLogic gameLogic;
    // Setup the board before each of the tests
    @BeforeEach
    void setUp() {
        // Initialize players
        players = new ArrayList<>();
        players.add(new Player(0, "a", 200, 0, null, false, Color. CYAN, false));
        players. add(new Player(1, "b", 200, 0, null, false, Color. ORANGE, false));
        // Initialize tiles
        tiles = new ArrayList<>();
        tiles.add(new Tile("a", 0, 0, 0, 0, "#ffffff", 0, 0));
        tiles.add(new Tile("b", 60, 50, 2, 0, "#676767", 1, 1));
        tiles.add(new Tile("c", 0, 0, 0, 0, "#999999", 0, 0));
        tiles.add(new Tile("d", 60, 50, 4, 0, "#eeeeee", 1, 1));

        // Initialize bonus tiles
        bonusTiles = new ArrayList<>();
        bonusTiles.add(new Bonus("hihi", 50, 0));

        // Initialize dice
        dice = new ArrayList<>();
        dice. add(new Dice());
        dice.add(new Dice());

        // Set up initial player positions
        players. get(0).setPosition(0);
        players.get(1).setPosition(0);
        tiles.get(0). addPlayer(players.get(0));
        tiles.get(0).addPlayer(players.get(1));

        // Create GameLogic
        gameLogic = new GameLogic(players, tiles, bonusTiles, dice, tiles.size(), players.get(0), 0);
    }

//TESTS


    @Test
    void testPlayerMoney() {
        Player player = players.get(0);
        assertEquals(200, player.getMoney(), "Player should start with 200 money");
    }
    @Test
    void testPlayerMoneyTransaction() {
        Player player = players.get(0);
        int playerMoney = player.getMoney();
        player.setMoney(playerMoney-20);
        assertEquals(playerMoney-20, player.getMoney(), "Player money should decrease by 20");
        player.setMoney(player.getMoney() +50);
        assertEquals(playerMoney+30, player.getMoney(), "Player money should inrease by 30");
    }

    @Test
    void testPlayerGetJailed() {
        Player player = players.get(0);
        assertFalse(player.getJailed(), "Shouldnt be jailed instantly");
        player.setJailed(true);
        player.setJailTime(0);
        assertTrue(player.getJailed(), "Player should be jailed");
        assertEquals(0,player.getJailTime(), "Jailtime should be 0");
        player.setJailTime(player.getJailTime() + 1);
        assertEquals(1, player.getJailTime(), "Jail time should be  1");
        player.setJailed(false);
        assertFalse(player.getJailed(), "Player should be released");
    }


    @Test
    void testPlayerSteppingOnTiles() {
        Tile tile = tiles.get(1);
        Player player = players.get(0);
        
        assertTrue(tile.getPlayers().isEmpty(), "Both players are on the 0-th tile so should be empty");
        tile.addPlayer(player);
        assertEquals(1,tile.getPlayers().size(), "Should move to 2nd tile");
        tile.removePlayer(player);
        assertTrue(tile.getPlayers().isEmpty(), "Shouldve removed the player from the tile");
    }

    @Test
    void testTileOwner() {
        Tile tile = tiles. get(1);
        Player player = players.get(0);
        assertEquals(100, tile.getOwner(). getId(), "Noone owns the tile (id = 100 basically means this by specification)");
        tile.setOwner(player);
        assertEquals(player, tile.getOwner(), "Tile owner should be the player");
        assertEquals("a", tile.getOwner().getName(), "Owner name should be a");
    }

    @Test
    void testHouse() {
        Tile tile = tiles.get(1);
        int tax = tile.getTax();
        int houseCost = tile.getHouseCost();
        assertEquals(0, tile.getNumberOfHouses(), "Tiles start with 0 houses");
        tile.setNumberOfHouses(1);
        tile.setTax(tax + houseCost);
        assertEquals(tax + houseCost, tile.getTax(), "The tax should increase by the house cost");
    }

    @Test
    void testDice() {
        Dice dice = new Dice();
        
        for (int i = 0; i < 100; i++) {
            int roll = dice.roll();
            assertTrue(roll >= 1 && roll <= 6, "Dice should give numbers ranging from 1-6, it was:" + roll);
        }
    }

    @Test
    void testPlayerNetWorth() {
        Player player = players.get(0);
        Tile tile = tiles.get(1);
        
        int nw = gameLogic.getCurrentPlayerNetWorth();
        assertEquals(player.getMoney(), nw, "Net worth = money if you have no tiles");
        player.setTiles(tile);
        tile.setOwner(player);
        int nw2 = gameLogic.getCurrentPlayerNetWorth();
        assertEquals(player.getMoney() + tile.getSellValue(), nw2, "Net worth should include your properties");
    }

    @Test
    void testNextPlayer() {
        assertEquals(players.get(0), gameLogic.getCurrentPlayer(), "1st player sshould start");
        gameLogic.nextPlayer();
        assertEquals(players.get(1), gameLogic.getCurrentPlayer(), "2nd player should be after first");
        gameLogic.nextPlayer();
        assertEquals(players.get(0), gameLogic.getCurrentPlayer(), "nextPlayer should loop after last player");
    }

    @Test
    void testLostPlayerSkip() {
        players.get(1).setLost(true);
        assertEquals(players.get(0), gameLogic.getCurrentPlayer(), "1st player is the currentPlayer");
        gameLogic. nextPlayer();
        assertEquals(players.get(0), gameLogic. getCurrentPlayer(), "Shouldve skipped the player that lost");
    }

}
