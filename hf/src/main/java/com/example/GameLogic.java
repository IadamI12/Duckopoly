package com.example;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JOptionPane;

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
    public GameLogic(List<Player> players, List<Tile> tiles, List<Bonus> bonusTiles, List<Dice> dice, int boardSize, Player currentPlayer, int turn){
        this.players = players;
        this.tiles = tiles;
        this.bonusTiles = bonusTiles;
        this.dice = dice;
        this.boardSize = boardSize;
        this.currentPlayer = currentPlayer;
        this.turn = turn;

    }
    // public int startGame() {
    //     Setup st = new Setup();
    //     int[] savedState = null;
    //     try {
    //         savedState = st.load(players, tiles, bonusTiles);
    //     } catch (EndGame e) {
    //         System.out.println(e.getMessage());
    //         return 0;
    //     }
    //     int savedThrown = 0;
    //     if (savedState != null){
    //       this.turn = savedState[0];
    //         this.currentPlayer = players.get(savedState[1]);
    //         savedThrown = savedState[2]; 
    //     }
    //     else{
    //         currentPlayer = players.get(0);
    //     for (Player p : players) {
    //         p.setPosition(0);
    //         tiles.get(0).addPlayer(p);
    //     }
    //     }
    //     dice.add(new Dice());
    //     dice.add(new Dice());
    //     boardSize = tiles.size();
    //     return savedThrown;
    // }

    public List<Integer> diceThrow() {
        List<Integer> temp = new ArrayList<>();
        int firstDice = dice.get(0).roll();
        int secondDice = dice.get(1).roll();
        temp.add(firstDice);
        temp.add(secondDice);
        return temp;
    }

    public void movePlayer(int numberOfTiles) {
        Tile jailTile = tiles.get(7);
        int oldPosition = currentPlayer.getPosition();
        Tile oldTile = tiles.get(currentPlayer.getPosition());
        oldTile.removePlayer(currentPlayer);
        int newPosition = (currentPlayer.getPosition() + numberOfTiles) % tiles.size();
        currentPlayer.setPosition(newPosition);
        Tile newTile = tiles.get(newPosition);
        newTile.addPlayer(currentPlayer);

        if (newTile.getName().equals("Go To Jail")) {
            currentPlayer.setPosition(7);
            newTile.removePlayer(currentPlayer);
            jailTile.addPlayer(currentPlayer);
            currentPlayer.setJailed(true);
            currentPlayer.setJailTime(0);
            JOptionPane.showMessageDialog(null, "You went to jail!");
        }
        if (oldPosition > newPosition) {
            currentPlayer.setMoney(currentPlayer.getMoney() + 200);
        }
        if (newTile.getName().equals("Community Chest") || newTile.getName().equals("Chance")) {
            if (bonusTiles.size() > 0) {
                int randIndex = random.nextInt(bonusTiles.size());
                Bonus bonusCard = bonusTiles.get(randIndex);
                JOptionPane.showMessageDialog(null, bonusCard.getBonus());
                switch (bonusCard.getProfitType()) {
                    // kapsz pénzt
                    case 0:
                        currentPlayer.setMoney(currentPlayer.getMoney() + bonusCard.getMoney());
                        break;
                    // vesztesz pénzt
                    case 1:
                        currentPlayer.setMoney(currentPlayer.getMoney() - bonusCard.getMoney());
                        break;
                    // kapsz másoktól pénzt
                    case 2:
                        currentPlayer
                                .setMoney(currentPlayer.getMoney() + (bonusCard.getMoney() * (players.size() - 1)));
                        for (Player p : players) {
                            if (p != currentPlayer && !p.getLost()) {
                                p.setMoney(p.getMoney() - bonusCard.getMoney());
                            }
                        }
                        break;
                    // adsz másoknak pénzt
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
        for (Player p : players){
        if (p.getTiles().contains(newTile)) {
            currentPlayer.setMoney(currentPlayer.getMoney() - newTile.getTax());
            p.setMoney(p.getMoney() + newTile.getTax());
        }
        }

    }
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
public void newTurn() {
    turn++;
    nextPlayer();
    if (currentPlayer.getJailed()){
        currentPlayer.setJailTime(currentPlayer.getJailTime() +1);
    }
    // Player lostPlayer = null;
    // int netWorth = getCurrentPlayerNetWorth();
    // if (netWorth <= 0) {
    //     try {
    //         lostPlayer = currentPlayerLost();
    //     } catch (PlayerWon pw) {
    //         board.gameOverScreen();
    //         JOptionPane.showMessageDialog(null, pw.getMessage());
    //         return;
    //     }
    // }

    // if (lostPlayer != null) {
    //     board.playerLost(lostPlayer);
    // }
}
public void nextPlayer(){
 int index = players.indexOf(currentPlayer);
                for (int i = 0; i < players.size();i++){
                    index = (index+1)%players.size();
                    Player p = players.get(index);
                    if (!p.getLost()){
                        currentPlayer = p;
                        return;
                    }
                }
}
public int getCurrentPlayerNetWorth() {
    int netWorth = currentPlayer.getMoney();
    for (Tile t : currentPlayer.getTiles()) {
        netWorth += t.getSellValue();
    }
    return netWorth;
}
    public Player currentPlayerLost() throws PlayerWon {
    int currentPlayerMoney = getCurrentPlayerNetWorth();
    if (currentPlayerMoney < 0) {
        Player lostPlayer = currentPlayer;

        for (Tile t : tiles) {
            if (t.getOwner() == lostPlayer) {
                t.setOwner(new Player(100, "-", 0, 0, null, false, Color.BLACK, false));
                t.setPurchasable(true);
                t.setSellValue(t.getPrice() / 2);
            }
            t.removePlayer(lostPlayer);
        }
        lostPlayer.setLost(true);

        int numberOfLost = 0;
        for (Player p : players) {
            if (p.getLost())
                numberOfLost++;
        }
    
        if (players.size() - numberOfLost == 1) {
            //itt kéne highlight elv???
            //highlightplayer
            Player winner = determineWinner();
            
            throw new PlayerWon(winner.getName() + " has won the game!");
        } else {
            nextPlayer();
        }
        return lostPlayer;
    }
    return null;
}
public void saveGame(String filename,boolean thrown) throws IOException{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
        oos.writeObject(players);
        oos.writeObject(tiles);
        oos.writeObject(bonusTiles);
        oos.writeInt(turn);
        oos.writeInt(players.indexOf(currentPlayer));
        if (thrown){
            oos.writeInt(1);
        }
        else{
            oos.writeInt(0);
        }
        oos.close();
}
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


    public boolean currentPlayerHasAllTilesOfColorGroup(Color color) {
        List<Tile> playerTiles = currentPlayer.getTiles();
        int sum1 = 0;
        int sum2 = 0;
        for (Tile tile : playerTiles) {
            if (tile.getTileColor().equals(color)) {
                sum1++;
            }
        }
        for (Tile tile : getTiles()) {
            if (tile.getTileColor().equals(color)) {
                sum2++;
            }
        }
        return sum1 == sum2;
    }


    public boolean isTradable(Tile tile) {
        return ((tile.getOwner() != currentPlayer) && (tile.getOwner().getId() != 100));
    }
    public void playerBought(){
        tiles.get(currentPlayer.getPosition()).setPurchasable(false);
            currentPlayer.setMoney(currentPlayer.getMoney() - tiles.get(currentPlayer.getPosition()).getPrice());
            currentPlayer.setTiles(tiles.get(currentPlayer.getPosition()));
            tiles.get(currentPlayer.getPosition()).setOwner(currentPlayer);
    }
public int getHouseCostForAllHouses(Tile tile){
    int sum = 0;
    Color selectedColor = tile.getTileColor();
    for (Tile t : tiles){
        if (t.getTileColor().equals(selectedColor)){
            sum += t.getHouseCost();
        }
    }
    return sum;
}
    public void playerBoughtHouse(){
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
    public void playerSold(){
currentPlayer.setMoney(currentPlayer.getMoney() + getSelectedTile().getSellValue());
            currentPlayer.getTiles().remove(getSelectedTile());
            getSelectedTile().setOwner(new Player(100, "-", 0, 0, null, false, Color.BLACK, false));
            getSelectedTile().setPurchasable(true);
            getSelectedTile().setSellValue(getSelectedTile().getPrice() / 2);
            getSelectedTile().setNumberOfHouses(0);
            getSelectedTile().setTax(getSelectedTile().getPrice()/2);
    }
}
//ha még nem dobott de meg tudja venniissue:(
//sell house !!!! -> ha egyik mezot eladom amin van ház az összeshöz lemenjen?


//több file kimentés is?


//RELATIVE PATH KÉPEKKEL KIPROBALNI PLS PLS
