package com.example;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The brain of the game, handling all the logic
 * 
 * @author Pap Ádám
 */
public class GameLogic {
    /**
     * the players of the game
     */
    private List<Player> players = new ArrayList<>();

    /**
     * the tiles of the game
     */
    private List<Tile> tiles = new ArrayList<>();
    /**
     * the bonus tiles of the game
     */
    private List<Bonus> bonusTiles = new ArrayList<>();
    /**
     * Random number generator
     */
    private Random random = new Random();
    /**
     * the dices of the game
     */
    private List<Dice> dice = new ArrayList<>();
    /**
     * the currently selected tile
     */
    private int selectedTilePosition;
    /**
     * the size of the board
     */
    private int boardSize;
    /**
     * the current turn
     */
    private int turn = 0;
    /**
     * the current player
     */
    private Player currentPlayer;

    /**
     * Storing the data to the gamelogic
     * 
     * @param players       - list of players
     * @param tiles         - list of tiles
     * @param bonusTiles    - list of bonus tiles
     * @param dice          - list of dices
     * @param boardSize     - size of the board
     * @param currentPlayer - the current player
     * @param turn          - the current turn
     */
    public GameLogic(List<Player> players, List<Tile> tiles, List<Bonus> bonusTiles, List<Dice> dice, int boardSize,
            Player currentPlayer, int turn) {
        this.players = players;
        this.tiles = tiles;
        this.bonusTiles = bonusTiles;
        this.dice = dice;
        this.boardSize = boardSize;
        this.currentPlayer = currentPlayer;
        this.turn = turn;

    }

    // ----------Setters and getters------------------
    /**
     * Getting the size of the board
     * 
     * @return int - the size of the board
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Selecting a tile by its index
     * 
     * @param i - the index of the tile to select
     */
    public void selectTile(int i) {
        selectedTilePosition = i;
    }

    /**
     * Getting the selected tile
     * 
     * @return Tile - the selected tile
     */
    public Tile getSelectedTile() {
        return tiles.get(selectedTilePosition);
    }

    /**
     * Getting the list of players
     * 
     * @return {@code List<Player>} - the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Getting the list of tiles
     * 
     * @return {@code List<Tile>} - the list of tiles
     */
    public List<Tile> getTiles() {
        return tiles;
    }

    /**
     * Getting the current player
     * 
     * @return Player - the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // ----------Setters and getters------------------
    /**
     * Returning the result of the dicethrow
     * 
     * @return {@code List<Integer>} - the result of the dicethrow
     */
    public List<Integer> diceThrow() {
        List<Integer> temp = new ArrayList<>();
        int firstDice = dice.get(0).roll();
        int secondDice = dice.get(1).roll();
        temp.add(firstDice);
        temp.add(secondDice);
        return temp;
    }

    /**
     * Moving the player based on where he is, and what tile he was on / he stepped
     * on
     * 
     * @param numberOfTiles - the number of tiles to move forward
     * @return String - message to write out based on what happened
     */
    public String movePlayer(int numberOfTiles) {
        String whatToWriteOut = null;
        // Store the tile index
        Tile jailTile = tiles.get(boardSize / 4);
        // Where he went from
        int oldPosition = currentPlayer.getPosition();
        Tile oldTile = tiles.get(currentPlayer.getPosition());
        oldTile.removePlayer(currentPlayer);
        // Where he landed
        int newPosition = (currentPlayer.getPosition() + numberOfTiles) % tiles.size();
        currentPlayer.setPosition(newPosition);
        Tile newTile = tiles.get(newPosition);
        newTile.addPlayer(currentPlayer);
        // Being sent to jail
        if (newTile.getName().equals("Go To Jail")) {
            currentPlayer.setPosition(boardSize / 4);
            newTile.removePlayer(currentPlayer);
            jailTile.addPlayer(currentPlayer);
            currentPlayer.setJailed(true);
            currentPlayer.setJailTime(0);
            whatToWriteOut = "You went to jail!";
        }
        // If the went through the Start tile give him 100 money
        if (oldPosition > newPosition) {
            currentPlayer.setMoney(currentPlayer.getMoney() + 100);
        }
        // If the player stepped on a Community Chest or Chance tile, give him a random
        // bonus card
        if (newTile.getName().equals("Community Chest") || newTile.getName().equals("Chance")) {
            if (bonusTiles.size() > 0) {
                int randIndex = random.nextInt(bonusTiles.size());
                Bonus bonusCard = bonusTiles.get(randIndex);
                whatToWriteOut = bonusCard.getBonus();
                switch (bonusCard.getProfitType()) {
                    // get money
                    case 0:
                        currentPlayer.setMoney(currentPlayer.getMoney() + bonusCard.getMoney());
                        break;
                    // lose money
                    case 1:
                        currentPlayer.setMoney(currentPlayer.getMoney() - bonusCard.getMoney());
                        break;
                    // get money from others
                    case 2:
                        currentPlayer
                                .setMoney(currentPlayer.getMoney() + (bonusCard.getMoney() * (players.size() - 1)));
                        for (Player p : players) {
                            if (p != currentPlayer && !p.getLost()) {
                                p.setMoney(p.getMoney() - bonusCard.getMoney());
                            }
                        }
                        break;
                    // give money to others
                    case 3:
                        currentPlayer
                                .setMoney(currentPlayer.getMoney() - (bonusCard.getMoney() * (players.size() - 1)));
                        for (Player p : players) {
                            if (p != currentPlayer && !p.getLost()) {
                                p.setMoney(p.getMoney() + bonusCard.getMoney());
                            }
                        }
                        break;
                }
            }
        }
        // If the player stepped on a tile owned by another player, pay tax to him
        for (Player p : players) {
            if (p.getTiles().contains(newTile)) {
                currentPlayer.setMoney(currentPlayer.getMoney() - newTile.getTax());
                p.setMoney(p.getMoney() + newTile.getTax());
            }
        }
        return whatToWriteOut;
    }

    /**
     * Determining the winner of the game
     * 
     * @return Player - the winner of the game
     */
    public Player determineWinner() {
        Player winner = null;
        for (Player p : players) {
            if (!p.getLost()) {
                if (winner == null || p.getMoney() > winner.getMoney()) {
                    winner = p;
                }
            }
        }
        return winner;
    }

    /**
     * Starting a new turn, changing the current player
     */
    public void newTurn() {
        turn++;
        nextPlayer();
        // If the player is in jail, increase his jail time
        if (currentPlayer.getJailed()) {
            currentPlayer.setJailTime(currentPlayer.getJailTime() + 1);
        }
    }

    /**
     * Determine who's the next player, skipping the lost ones
     */
    public void nextPlayer() {
        int index = players.indexOf(currentPlayer);
        for (int i = 0; i < players.size(); i++) {
            index = (index + 1) % players.size();
            Player p = players.get(index);
            if (!p.getLost()) {
                currentPlayer = p;
                return;
            }
        }
    }

    /**
     * Calculating the net worth of the current player
     * 
     * @return int - the net worth of the current player
     */
    public int getCurrentPlayerNetWorth() {
        int netWorth = currentPlayer.getMoney();
        for (Tile t : currentPlayer.getTiles()) {
            netWorth += t.getSellValue();
        }
        return netWorth;
    }

    /**
     * Checking if the current player has lost
     * 
     * @return Player - the player who lost, or null if no one lost
     * @throws PlayerWon - if a player has won the game
     */
    public Player currentPlayerLost() throws PlayerWon {
        int currentPlayerMoney = getCurrentPlayerNetWorth();
        if (currentPlayerMoney < 0) {
            Player lostPlayer = currentPlayer;
            // delete his ownership over all the tiles he's owned
            for (Tile t : tiles) {
                if (t.getOwner() == lostPlayer) {
                    t.setOwner(new Player(100, "-", 0, 0, null, false, Color.BLACK, false));
                    t.setPurchasable(true);
                    t.setSellValue(t.getPrice() / 2);
                }
                t.removePlayer(lostPlayer);
            }
            lostPlayer.setLost(true);
            // Count how many players have lost
            int numberOfLost = 0;
            for (Player p : players) {
                if (p.getLost())
                    numberOfLost++;
            }
            // If only one player hasn't lost, the game is over
            if (players.size() - numberOfLost == 1) {
                Player winner = determineWinner();

                throw new PlayerWon(winner.getName() + " has won the game!");
            } else {
                // If more than one player hasn't lost continue the game
                nextPlayer();
            }
            return lostPlayer;
        }
        return null;
    }

    /**
     * Saving the game state to a file
     * 
     * @param filename - the name of the file to save to
     * @param thrown   - whether the current player has thrown or not
     * @throws IOException - if theres an io exception
     */
    public void saveGame(String filename, boolean thrown) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
        oos.writeObject(players);
        oos.writeObject(tiles);
        oos.writeObject(bonusTiles);
        oos.writeInt(turn);
        oos.writeInt(players.indexOf(currentPlayer));
        if (thrown) {
            oos.writeInt(1);
        } else {
            oos.writeInt(0);
        }
        oos.close();
    }

    /**
     * Checking if the current player has all the tiles of a color group
     * 
     * @param color - the color to check
     * @return boolean - true if the current player has all the tiles of the color
     *         group, false otherwise
     */
    public boolean currentPlayerHasAllTilesOfColorGroup(Color color) {
        List<Tile> playerTiles = currentPlayer.getTiles();
        int sum1 = 0;
        int sum2 = 0;
        // how many he has
        for (Tile tile : playerTiles) {
            if (tile.getTileColor().equals(color)) {
                sum1++;
            }
        }
        // how many there are
        for (Tile tile : getTiles()) {
            if (tile.getTileColor().equals(color)) {
                sum2++;
            }
        }
        return sum1 == sum2;
    }

    /**
     * Checking if a tile is tradable (someone owns it and its not the player who's
     * turn it is)
     * 
     * @param tile - the tile to check
     * @return boolean - true if the tile is tradable, false otherwise
     */
    public boolean isTradable(Tile tile) {
        return ((tile.getOwner() != currentPlayer) && (tile.getOwner().getId() != 100));
    }

    /**
     * Handling if the player has bought the tile he's standing on
     */
    public void playerBought() {
        tiles.get(currentPlayer.getPosition()).setPurchasable(false);
        currentPlayer.setMoney(currentPlayer.getMoney() - tiles.get(currentPlayer.getPosition()).getPrice());
        currentPlayer.setTiles(tiles.get(currentPlayer.getPosition()));
        tiles.get(currentPlayer.getPosition()).setOwner(currentPlayer);
    }

    /**
     * Determining the cost to buy a house onto the color of a given tile
     * 
     * @param tile - tile to check the color of
     * @return int - the total cost of buying a house on all tiles of the color
     */
    public int getHouseCostForAllHouses(Tile tile) {
        int sum = 0;
        Color selectedColor = tile.getTileColor();
        for (Tile t : tiles) {
            if (t.getTileColor().equals(selectedColor)) {
                sum += t.getHouseCost();
            }
        }
        return sum;
    }

    /**
     * Changing the player's balance, since he bought a house
     */
    public void playerBoughtHouse() {
        Tile currentTile = tiles.get(currentPlayer.getPosition());
        for (Tile tile : tiles) {
            if (tile.getTileColor().equals(currentTile.getTileColor())) {
                tile.setNumberOfHouses(tile.getNumberOfHouses() + 1);
                tile.setTax(tile.getTax() + tile.getHouseCost());
            }
        }
        currentPlayer.setMoney(currentPlayer.getMoney() - getHouseCostForAllHouses(currentTile));
        currentTile.setSellValue(currentTile.getSellValue() + currentTile.getHouseCost() / 2);
    }

    /**
     * Handling a player selling his property
     */
    public void playerSold() {
        // player's money, and tiles being changed
        currentPlayer.setMoney(currentPlayer.getMoney() + getSelectedTile().getSellValue());
        currentPlayer.getTiles().remove(getSelectedTile());
        // ownership of the tile being set to noone, and taking away the houses from it
        getSelectedTile().setOwner(new Player(100, "-", 0, 0, null, false, Color.BLACK, false));
        getSelectedTile().setPurchasable(true);
        getSelectedTile().setSellValue(getSelectedTile().getPrice() / 2);
        getSelectedTile().setNumberOfHouses(0);
        getSelectedTile().setTax(getSelectedTile().getPrice() / 2);
    }
}