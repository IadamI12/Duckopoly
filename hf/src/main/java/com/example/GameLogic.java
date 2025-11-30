package com.example;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic {
    private List<Player> players = new ArrayList<>();
    private List<Tile> tiles = new ArrayList<>();
    private List<Bonus> bonusTiles = new ArrayList<>();
    private Random random = new Random();
    private List<Dice> dice = new ArrayList<>();
    private int selectedTilePosition;
    private int boardSize;
    private int turn = 0;
    private Player currentPlayer;

    // Storing the date to the gamelogic
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
    // I wont document them individually, since they're self explenatory
    public int getBoardSize() {
        return boardSize;
    }

    public void selectTile(int i) {
        selectedTilePosition = i;
    }

    public Tile getSelectedTile() {
        return tiles.get(selectedTilePosition);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    // ----------Setters and getters------------------

    // Returning the result of the dicethrow
    public List<Integer> diceThrow() {
        List<Integer> temp = new ArrayList<>();
        int firstDice = dice.get(0).roll();
        int secondDice = dice.get(1).roll();
        temp.add(firstDice);
        temp.add(secondDice);
        return temp;
    }

    // Moving the player based on where he is, and what tile he was on / he stepped
    // on
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

    // Determining the winner of the game
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

    // Starting a new turn, changing the current player
    public void newTurn() {
        turn++;
        nextPlayer();
        // If the player is in jail, increase his jail time
        if (currentPlayer.getJailed()) {
            currentPlayer.setJailTime(currentPlayer.getJailTime() + 1);
        }
    }

    // Determine who's the next player, skipping the lost ones
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

    // Calculate the net worth of the current player
    public int getCurrentPlayerNetWorth() {
        int netWorth = currentPlayer.getMoney();
        for (Tile t : currentPlayer.getTiles()) {
            netWorth += t.getSellValue();
        }
        return netWorth;
    }

    // Checking if the current player has lost
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

    // Saving the game state to a file
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

    // Checking if the current player has all the tiles of a color group
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

    // checking if a tile is tradable (someone owns it and its not the player who's
    // turn it is)
    public boolean isTradable(Tile tile) {
        return ((tile.getOwner() != currentPlayer) && (tile.getOwner().getId() != 100));
    }

    // Handling if the player has bought the tile he's standing on
    public void playerBought() {
        tiles.get(currentPlayer.getPosition()).setPurchasable(false);
        currentPlayer.setMoney(currentPlayer.getMoney() - tiles.get(currentPlayer.getPosition()).getPrice());
        currentPlayer.setTiles(tiles.get(currentPlayer.getPosition()));
        tiles.get(currentPlayer.getPosition()).setOwner(currentPlayer);
    }

    // Determining the cost to buy a house onto the color of a given tile
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

    // Changing the player's balance, since he bought a house
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

    // Handling a player selling his property
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