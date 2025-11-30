package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class GameController implements ActionListener {
    private GameLogic gameLogic;
    private Board board;
    private boolean thrown;
    //Connecting the brain of the game with the GUI
    //Also storing thrown in case of a saved game
    public GameController(GameLogic gameLogic, Board board, boolean thrown) {
        this.gameLogic = gameLogic;
        this.board = board;
        this.thrown = thrown;
        //Connecting the buttons to their listeners
        this.board.connectButtons(this);
    }
    //Handling a player losing in the GUI
    private void handlePlayerLost(Player lostPlayer, Player currentPlayer) {
        board.playerLost(lostPlayer, currentPlayer, gameLogic.getPlayers());
        board.refreshPlayerPanels(gameLogic.getPlayers());
        board.disablePassButton();
    }
    //Updating the state of the buttons based on a player's financial situation and criminal record
    private void updateButtonStates() throws PlayerWon {
        Player currentPlayer = gameLogic.getCurrentPlayer();
        int netWorth = gameLogic.getCurrentPlayerNetWorth();
        boolean thrown = board.getThrown();
        //If the player has negative netWorth he lost
        if (netWorth < 0) {
            board.disableThrowButton();
            board.disablePassButton();
            checkPlayerLost();
            return;
        }
        //If the player has the opportunity to sell enough of his valuables to stay in the game he MUST do it to progress forward
        if (currentPlayer.getMoney() < 0) {
            board.disableThrowButton();
            board.disablePassButton();
        } else {
            //if he hasn't thrown he can throw
            if (!thrown && currentPlayer.getMoney() >= 0) {
                board.enableThrowButton();

            }
            //if he has, he can't 
            else {
                board.disableThrowButton();
            }
            //if he's in jail he must throw
            if (currentPlayer.getJailTime() > 0) {
                board.disablePassButton();
            } 
            //if he has thrown, he can pass
            else if (thrown) {
                board.enablePassButton();
            }
        }
    }
    //Catching if a player has lost or not
    //If everyone lost except one, the game is over
    private void checkPlayerLost() {
        try {
            Player lostPlayer = gameLogic.currentPlayerLost();
            //if the lostPlayer isn't null that means the current player lost
            if (lostPlayer != null) {
                handlePlayerLost(lostPlayer, gameLogic.getCurrentPlayer());
            }
        } catch (PlayerWon pw) {
            //disabling buttons, graying out the lost player and showing the message of the unfortunate news
            board.gameOverScreen();
            board.highlightCurrentPlayer(gameLogic.getCurrentPlayer(), gameLogic.getPlayers());
            board.showMessage(pw.getMessage());
        }
    }
    //Saving the game to "gamesave.txt", and also whether the player has thrown
    private void handleSaveButton() {
        try {
            gameLogic.saveGame("hf\\gamesave.txt", board.getThrown());
            board.showMessage("Game saved successfully!");
        } catch (IOException ex) {
            board.showMessage("Failed to save game!" + ex.getMessage());
        }
    }
    //Throw button being pressed
    private void handleThrowButton() {
        board.setThrown(true);
        board.disableThrowButton();
        //Rolling the dice and storing the results
        List<Integer> result = gameLogic.diceThrow();
        int total = result.get(0) + result.get(1);
        board.updateDiceResult(result);
        Player currentPlayer = gameLogic.getCurrentPlayer();
        //If the player isn't jailed, move him
        if (!currentPlayer.getJailed())
            movePlayer(total);
        else {
            //He's in jail
            board.enablePassButton();
            //Got out
            if (result.get(0) == result.get(1) || currentPlayer.getJailTime() == 1) {
                //Rolled the same with both dice
                if (result.get(0) == result.get(1)) {
                    board.showMessage("You rolled: " + result.get(0) + " and " + result.get(1) + ", you are free!");
                } else {
                    board.showMessage("Jailtime left: 0 rounds!");
                }
                currentPlayer.setJailed(false);
                currentPlayer.setJailTime(0);
            } 
            //Still in jail
            else {
                board.showMessage("Jailtime left: " + (2 - currentPlayer.getJailTime()) + " rounds!");
            }
        }
    }
    //Pass button being pressed
    public void handlePassButton() {
        board.setThrown(false);
        newTurn();
        try {
            updateButtonStates();
        } catch (PlayerWon pw) {
            board.gameOverScreen();
            JOptionPane.showMessageDialog(null, pw.getMessage());
        }
        if (!gameLogic.getCurrentPlayer().getJailed()) {
            board.disablePassButton();
        }
        board.disableHouseButton();
        board.disableSellButton();
    }
    //Tile being clicked (tileIndex shows which index was clicked)
    public void handleTileButton(int tileIndex) {
        gameLogic.selectTile(tileIndex);
        Tile temp = gameLogic.getTiles().get(tileIndex);
        //Format the info to look good
        String info = String.format(
                "Name: %s%nPrice: %d%nHouse cost: %d%nTax: %d%nHouses: %d%nOwner: %s%nSell value: %d",
                temp.getName(), temp.getPrice(), temp.getHouseCost(), temp.getTax(),
                temp.getNumberOfHouses(), temp.getOwner().getName(), temp.getSellValue());
        //Show the selected tile on screen
        board.hightLightTile(info, temp.getTileColor());
        Player currentPlayer = gameLogic.getCurrentPlayer();
        Tile selectedTile = gameLogic.getTiles().get(tileIndex);
        if (currentPlayer.getMoney() < 0 && currentPlayer.getTiles().contains(selectedTile)) {
            board.enableSellButton();
        }
        if (board.getThrown()) {
            if (tileIndex == currentPlayer.getPosition()) {
                //If the tile is purchasable and the player who's turn it is has enough money
                if (selectedTile.getPurchasable() == true && currentPlayer.getMoney() >= selectedTile.getPrice()) {
                    board.enableBuyButton();
                } else {
                    board.disableBuyButton();
                }
                //If a house can be purchased to the tile (based on the given specificiaton)
                if (selectedTile.getHousePurchasable() == true &&
                        currentPlayer.getMoney() >= gameLogic.getHouseCostForAllHouses(selectedTile) &&
                        selectedTile.getNumberOfHouses() < 5 &&
                        currentPlayer.getTiles().contains(selectedTile) &&
                        gameLogic.currentPlayerHasAllTilesOfColorGroup(
                                gameLogic.getTiles().get(currentPlayer.getPosition()).getTileColor())) {
                    board.enableHouseButton();
                } else {
                    board.disableHouseButton();
                }
            } else {
                board.disableBuyButton();
                ;
                board.disableHouseButton();
            }
            //If the player already owns the tile, he can sell it
            if (currentPlayer.getTiles().contains(selectedTile)) {
                board.enableSellButton();
            } else {
                board.disableSellButton();
            }
        }
        //If the tile is tradable, enable the trade button
        if (gameLogic.isTradable(selectedTile)) {
            board.enableTradeButton();
        } else {
            board.disableTradeButton();
        }
    }
    //Buy button being pressed
    private void handleBuyButton() {
        Player currentPlayer = gameLogic.getCurrentPlayer();
        Tile currentTile = gameLogic.getTiles().get(currentPlayer.getPosition());
        //Changing the stats of the player and the GUI 
        gameLogic.playerBought();
        board.disableBuyButton();
        board.enableSellButton();

        //If the house is purchasable enable the button
        if (currentTile.getHousePurchasable() == true &&
                currentPlayer.getMoney() >= gameLogic.getHouseCostForAllHouses(currentTile) &&
                currentTile.getNumberOfHouses() < 5 &&
                currentPlayer.getTiles().contains(currentTile) &&
                gameLogic.currentPlayerHasAllTilesOfColorGroup(currentTile.getTileColor())) {
            board.enableHouseButton();
        } else {
            board.disableHouseButton();
        }
        //Refresh the GUI to show the 
        board.highlightTileBorder(board.getTileButtons().get(currentPlayer.getPosition()),
                currentPlayer.getPieceColor(), true);
        board.refreshHouseCount(currentTile);
        board.refreshPlayerPanels(gameLogic.getPlayers());
        //Handling the player winning
        try {
            updateButtonStates();
        } catch (PlayerWon pw) {
            board.gameOverScreen();
            JOptionPane.showMessageDialog(null, pw.getMessage());
        }
    }
    //House button being pressed
    private void handleHouseButton() {
        Player currentPlayer = gameLogic.getCurrentPlayer();
        Tile currentTile = gameLogic.getTiles().get(currentPlayer.getPosition());
        gameLogic.playerBoughtHouse();
        //Enabling house button if purchasable
        if (currentTile.getHousePurchasable() == true &&
                currentPlayer.getMoney() >= gameLogic.getHouseCostForAllHouses(currentTile) &&
                currentTile.getNumberOfHouses() < 5 &&
                currentPlayer.getTiles().contains(currentTile)) {
            board.enableHouseButton();
        } else {
            board.disableHouseButton();
        }
        //Refreshing the GUI
        board.refreshHouseCount(currentTile);
        board.refreshPlayerPanels(gameLogic.getPlayers());
        //Handling a player winning
        try {
            updateButtonStates();
        } catch (PlayerWon pw) {
            board.gameOverScreen();
            JOptionPane.showMessageDialog(null, pw.getMessage());
        }
    }
    //Sell button being pressed
    private void handleSellButton() {
        //Refreshing the GUI and the player's stats
        gameLogic.playerSold();
        board.disableHouseButton();
        board.disableSellButton();
        board.refreshHouseCount(gameLogic.getSelectedTile());
        board.refreshPlayerPanels(gameLogic.getPlayers());
        try {
            updateButtonStates();
        } catch (PlayerWon pw) {
            board.gameOverScreen();
            JOptionPane.showMessageDialog(null, pw.getMessage());
        }
        //Refreshing the border of the sold tile
        int indexOfSelectedTile = gameLogic.getTiles().indexOf(gameLogic.getSelectedTile());
        board.highlightTileBorder(board.getTileButtons().get(indexOfSelectedTile), null, false);
    }
    //Trade button being pressed
    private void handleTradeButton() {
        TradeWindow tradeWindow = new TradeWindow(board, gameLogic.getCurrentPlayer(), gameLogic.getSelectedTile());
        //The compiler was sad
        tradeWindow = (TradeWindow) tradeWindow;
        board.disableTradeButton();
    }
    //Staring the game
    public void startGame() {
        board.setThrown(thrown);
        board.repaintAllTiles();
        //Setting the borders of the buttons
        for (JButton button : board.getTileButtons()) {
            Player owner = gameLogic.getTiles().get(board.getTileButtons().indexOf(button)).getOwner();
            board.highlightTileBorder(button, owner.getPieceColor(), (owner.getId() != 100) ? true : false);
        }
        board.highlightCurrentPlayer(gameLogic.getCurrentPlayer(), gameLogic.getPlayers());
        //Checking if a player has won
        try {
            updateButtonStates();
        } catch (PlayerWon pw) {
            board.gameOverScreen();
            JOptionPane.showMessageDialog(null, pw.getMessage());
        }
    }
    //Handling a player being moved
    public void movePlayer(int numberOfTiles) {
        //Refreshing the player's data to match where he moved
        String message = gameLogic.movePlayer(numberOfTiles);
        if (message != null) {
            board.showMessage(message);
        }
        //Refreshing the GUI based on where the player moved
        board.refreshPlayerPanels(gameLogic.getPlayers());
        board.repaintAllTiles();
        //Catching if a player has won or not
        try {
            updateButtonStates();
        } catch (PlayerWon pw) {
            board.gameOverScreen();
            JOptionPane.showMessageDialog(null, pw.getMessage());
        }
    }
    //Handling new turn
    public void newTurn() {
        gameLogic.newTurn();
        board.highlightCurrentPlayer(gameLogic.getCurrentPlayer(), gameLogic.getPlayers());
        try {
            updateButtonStates();
        } catch (PlayerWon pw) {
            board.gameOverScreen();
            JOptionPane.showMessageDialog(null, pw.getMessage());
            return;
        }
        //Check if the current player has lost or not
        Player lostPlayer = null;
        int netWorth = gameLogic.getCurrentPlayerNetWorth();
        if (netWorth <= 0) {
            try {
                lostPlayer = gameLogic.currentPlayerLost();
            } catch (PlayerWon pw) {
                board.gameOverScreen();
                JOptionPane.showMessageDialog(null, pw.getMessage());
                return;
            }
        }
        if (lostPlayer != null) {
            handlePlayerLost(lostPlayer, gameLogic.getCurrentPlayer());
        }
    }

    //Handling which button was pressed based on the set actioncommand
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("SAVE")) {
            handleSaveButton();
        }
        // boolean lost = false;
        if (e.getActionCommand().equals("THROW")) {
            handleThrowButton();

        }
        if (e.getActionCommand().equals("PASS")) {
            handlePassButton();
        } else if (e.getActionCommand().startsWith("TILE_")) {
            int tileIndex = Integer.parseInt(e.getActionCommand().substring(5));
            handleTileButton(tileIndex);
        }
        if (e.getActionCommand().equals("BUY")) {
            handleBuyButton();
        }
        if (e.getActionCommand().equals("HOUSE")) {
            handleHouseButton();
        }
        if (e.getActionCommand().equals("SELL")) {
            handleSellButton();
        }
        if (e.getActionCommand().equals("TRADE")) {
            handleTradeButton();

        }

    }
}