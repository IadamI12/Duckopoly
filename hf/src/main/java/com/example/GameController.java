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

    public GameController(GameLogic gameLogic, Board board, boolean thrown) {
        this.gameLogic = gameLogic;
        this.board = board;
        this.thrown = thrown;
        this.board.connectButtons(this);
    }
private void handlePlayerLost(Player lostPlayer, Player currentPlayer) {
    board.playerLost(lostPlayer,currentPlayer, gameLogic.getPlayers());
    board.refreshPlayerPanels(gameLogic.getPlayers());
    board.disablePassButton();
}

private void updateButtonStates() throws PlayerWon {
    Player currentPlayer = gameLogic.getCurrentPlayer();
    int netWorth = gameLogic.getCurrentPlayerNetWorth();
    boolean thrown = board.getThrown();
    if (netWorth < 0) {
        board.disableThrowButton();
        board.disablePassButton();
        checkPlayerLost();
        return;
    }
    
    if (currentPlayer.getMoney() < 0) {
        board.disableThrowButton();
        board.disablePassButton(); 
    } else {
            if (!thrown && currentPlayer.getMoney() >= 0) {
                board.enableThrowButton();

            } else {
                board.disableThrowButton();
            }
        if  (currentPlayer.getJailTime() > 0){
            board.disablePassButton();
        }else if (thrown){
            board.enablePassButton();
        }
        } 
    }

private void checkPlayerLost() {
    try {
        Player lostPlayer = gameLogic.currentPlayerLost();
        if (lostPlayer != null) {
            handlePlayerLost(lostPlayer, gameLogic.getCurrentPlayer());
        }
    } catch (PlayerWon pw) {
        board.gameOverScreen();
        board.highlightCurrentPlayer(gameLogic.getCurrentPlayer(), gameLogic. getPlayers());
        board.showMessage(pw.getMessage());
    }
}
    private void handleSaveButton() {
        try {
            gameLogic.saveGame("gamesave.txt",board.getThrown());
            board.showMessage("Game saved successfully!");
        } catch (IOException ex) {
            board.showMessage("Failed to save game!" + ex.getMessage());
        }
    }
    private void handleThrowButton(){
            board.setThrown(true);
            board.disableThrowButton();
            List<Integer> result = gameLogic. diceThrow();
            int total = result.get(0) + result.get(1);
            board.updateDiceResult(result);
            Player currentPlayer = gameLogic.getCurrentPlayer();
            if (!currentPlayer.getJailed())
                movePlayer(total);
            else {
                board.enablePassButton();
                if (result.get(0) == result.get(1) || currentPlayer.getJailTime() == 1) {
                    if (result.get(0) == result.get(1)) {
                        board.showMessage("You rolled: " + result.get(0) + " and " + result.get(1) + ", you are free!");
                    } else {
                        board.showMessage("Jailtime left: 0 rounds!");
                    }
                    currentPlayer.setJailed(false);
                    currentPlayer.setJailTime(0);
                } else {
                    board.showMessage("Jailtime left: " + (2 - currentPlayer.getJailTime()) + " rounds!");
                }
            }
    }
    public void handlePassButton(){
            board.setThrown(false);
            newTurn();
            try{
            updateButtonStates();
            }catch (PlayerWon pw) {
                board.gameOverScreen();
                JOptionPane.showMessageDialog(null, pw.getMessage());
            }
            if (!gameLogic.getCurrentPlayer().getJailed()) {
                board.disablePassButton();
            }
            board.disableHouseButton();
            board.disableSellButton();
    }
    public void handleTileButton(int tileIndex){
                gameLogic.selectTile(tileIndex);
                Tile temp = gameLogic.getTiles().get(tileIndex);
                String info = String.format(
                        "Name: %s%nPrice: %d%nHouse cost: %d%nTax: %d%nHouses: %d%nOwner: %s%nSell value: %d",
                        temp.getName(), temp.getPrice(), temp.getHouseCost(), temp.getTax(),
                        temp.getNumberOfHouses(), temp.getOwner().getName(), temp.getSellValue());

                board.hightLightTile(info,temp.getTileColor());
                Player currentPlayer = gameLogic.getCurrentPlayer();
                Tile selectedTile = gameLogic.getTiles().get(tileIndex);
                if (currentPlayer.getMoney() < 0 && currentPlayer.getTiles().contains(selectedTile)) {
                    board.enableSellButton();
                }
                if (board.getThrown()) {
                    if (tileIndex == currentPlayer.getPosition()) {

                        if (selectedTile.getPurchasable() == true && currentPlayer.getMoney() >= selectedTile.getPrice()) {
                            board.enableBuyButton();
                        } else {
                            board.disableBuyButton();
                        }
                        if (selectedTile.getHousePurchasable() == true &&
                                currentPlayer.getMoney() >= gameLogic.getHouseCostForAllHouses(selectedTile) &&
                                selectedTile.getNumberOfHouses() < 5 &&
                                currentPlayer.getTiles().contains(selectedTile) &&
                                gameLogic.currentPlayerHasAllTilesOfColorGroup(
                                        gameLogic.getTiles().get(currentPlayer.getPosition()).getTileColor())){
                            board.enableHouseButton();
                        } 
                        else {
                            board.disableHouseButton();
                        }
                    } else {
                        board.disableBuyButton();;
                        board.disableHouseButton();
                    }
                    if (currentPlayer.getTiles().contains(selectedTile)) {
                        board.enableSellButton();
                    } else {
                        board.disableSellButton();
                    }
                }
                if (gameLogic.isTradable(selectedTile)) {
                    board.enableTradeButton();
                } else {
                    board.disableTradeButton();
                }
    }
private void handleBuyButton(){
    Player currentPlayer = gameLogic.getCurrentPlayer();
    Tile currentTile = gameLogic.getTiles().get(currentPlayer.getPosition());
    gameLogic.playerBought();
    board.disableBuyButton();
    board.enableSellButton();
            if (currentTile.getHousePurchasable() == true &&
                    currentPlayer.getMoney() >= gameLogic.getHouseCostForAllHouses(currentTile) &&
                    currentTile.getNumberOfHouses() < 5 &&
                    currentPlayer.getTiles().contains(currentTile) &&
                    gameLogic.currentPlayerHasAllTilesOfColorGroup(currentTile.getTileColor())){
                board.enableHouseButton();
            }
            else {
                board.disableHouseButton();
            }
            board.highlightTileBorder(board.getTileButtons().get(currentPlayer.getPosition()),currentPlayer.getPieceColor(),true);
            board.refreshHouseCount(currentTile);
            board.refreshPlayerPanels(gameLogic.getPlayers());
            try{
            updateButtonStates();
            }catch (PlayerWon pw) {
                board.gameOverScreen();
                JOptionPane.showMessageDialog(null, pw.getMessage());
            }
}
private void handleHouseButton(){
        Player currentPlayer = gameLogic.getCurrentPlayer();
        Tile currentTile = gameLogic.getTiles().get(currentPlayer.getPosition());
            gameLogic.playerBoughtHouse();

            if (currentTile.getHousePurchasable() == true &&
                    currentPlayer.getMoney() >= gameLogic.getHouseCostForAllHouses(currentTile) &&
                    currentTile.getNumberOfHouses() < 5 &&
                    currentPlayer.getTiles().contains(currentTile)) {
                board.enableHouseButton();
            } 
            else {
                board.disableHouseButton();
            }
            board.refreshHouseCount(currentTile);
            board.refreshPlayerPanels(gameLogic.getPlayers());
            try{
            updateButtonStates();
            }catch (PlayerWon pw) {
                board.gameOverScreen();
                JOptionPane.showMessageDialog(null, pw.getMessage());
            }
}
private void handleSellButton(){
            gameLogic.playerSold();
            board.disableHouseButton();
            board.disableSellButton();
            board.refreshHouseCount(gameLogic.getSelectedTile());
            board.refreshPlayerPanels(gameLogic.getPlayers());
            try{
            updateButtonStates();
            }catch (PlayerWon pw) {
                board.gameOverScreen();
                JOptionPane.showMessageDialog(null, pw.getMessage());
            }
            int indexOfSelectedTile = gameLogic.getTiles().indexOf(gameLogic.getSelectedTile());
            board.highlightTileBorder(board.getTileButtons().get(indexOfSelectedTile),null,false);
}
private void handleTradeButton(){
    TradeWindow tradeWindow = new TradeWindow(board, gameLogic.getCurrentPlayer(), gameLogic.getSelectedTile());
    tradeWindow = (TradeWindow) tradeWindow;
    board.disableTradeButton();
}




public void startGame(){
    board.setThrown(thrown);
    board.repaintAllTiles();
    for (JButton button : board.getTileButtons()) {
        Player owner = gameLogic.getTiles().get(board.getTileButtons().indexOf(button)).getOwner();
        board.highlightTileBorder(button, owner.getPieceColor(), (owner.getId() != 100) ? true : false);
    }
    board.highlightCurrentPlayer(gameLogic.getCurrentPlayer(),gameLogic.getPlayers());
            try{
            updateButtonStates();
            }catch (PlayerWon pw) {
                board.gameOverScreen();
                JOptionPane.showMessageDialog(null, pw.getMessage());
            }
}
public void movePlayer(int numberOfTiles){
    String message = gameLogic.movePlayer(numberOfTiles);
    if (message != null) {
        board.showMessage(message);
    }
    board.refreshPlayerPanels(gameLogic.getPlayers());
     //board.repaintTile(tile);
     board.repaintAllTiles();
    try{
    updateButtonStates();
    }catch (PlayerWon pw) {
        board.gameOverScreen();
        JOptionPane.showMessageDialog(null, pw.getMessage());
    }
}
public void newTurn(){
    gameLogic.newTurn();
    board.highlightCurrentPlayer(gameLogic.getCurrentPlayer(),gameLogic.getPlayers());
    try{
    updateButtonStates();
    }catch (PlayerWon pw) {
        board.gameOverScreen();
        JOptionPane.showMessageDialog(null, pw.getMessage());
        return;
    }
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








@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("SAVE")) {
            handleSaveButton();
        }
        // boolean lost = false;
        if (e.getActionCommand().equals("THROW")){
            handleThrowButton();

        }
        if (e.getActionCommand().equals("PASS")) {
            handlePassButton();
        }
        else if (e.getActionCommand().startsWith("TILE_")) {
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