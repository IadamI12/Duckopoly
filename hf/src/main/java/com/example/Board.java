package com.example;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Board extends JFrame {
    private List<Player> players = new ArrayList<>();
    private List<JButton> tileButtons = new ArrayList<>();
    private List<Tile> tiles = new ArrayList<>();

    private JButton throwButton;
    private JTextField diceResult;
    private JButton passButton;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem save;
    private ImageIcon gameIcon;
    private JButton buyButton;
    private JButton housButton;
    private JButton sellButton;
    private JButton tradeButton;

    private JPanel playersPanel;
    private List<JTextArea> playerAreas = new ArrayList<>();

    private JPanel infos;
    private JTextArea tileInfo;

    private boolean thrown = false;

    // ----------Setters and getters------------------
    // I wont document them individually, since they're self explenatory
    public boolean isThrown() {
        return thrown;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public void setThrown(boolean thrown) {
        this.thrown = thrown;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<JButton> getTileButtons() {
        return tileButtons;
    }

    public boolean getThrown() {
        return thrown;
    }
    // -------------Setters and getters------------------

    // Enabling-disabling buttons (used by the game controller)
    // I wont be documenting them individually, as they do exactly like their name
    // says
    public void disableThrowButton() {
        throwButton.setEnabled(false);
    }

    public void enableThrowButton() {
        throwButton.setEnabled(true);
    }

    public void disablePassButton() {
        passButton.setEnabled(false);
    }

    public void enablePassButton() {
        passButton.setEnabled(true);
    }

    public void disableBuyButton() {
        buyButton.setEnabled(false);
    }

    public void enableBuyButton() {
        buyButton.setEnabled(true);
    }

    public void disableHouseButton() {
        housButton.setEnabled(false);
    }

    public void enableHouseButton() {
        housButton.setEnabled(true);
    }

    public void disableSellButton() {
        sellButton.setEnabled(false);
    }

    public void enableSellButton() {
        sellButton.setEnabled(true);
    }

    public void disableTradeButton() {
        tradeButton.setEnabled(false);
    }

    public void enableTradeButton() {
        tradeButton.setEnabled(true);
    }

    // Setting up the GUI, creating the areas of the board, the menu and the players
    // Also setting up the Title, the icon and the Menubar, as the game can be
    // saved.
    public Board(List<Player> players, List<Tile> tiles) {
        this.players = players;
        this.tiles = tiles;
        this.setTitle("Duckopoly");
        this.setLayout(new BorderLayout());

        JPanel mainArea = new JPanel(new BorderLayout());
        JPanel boardInfo = createBoard();
        JPanel menuInfo = createMenu();
        JPanel playersInfo = createPlayers();
        mainArea.add(boardInfo, BorderLayout.CENTER);
        mainArea.add(menuInfo, BorderLayout.EAST);
        this.add(mainArea, BorderLayout.CENTER);
        this.add(playersInfo, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
        this.setTitle("Duckopoly");
        gameIcon = new ImageIcon("hf\\src\\main\\resources\\duck.png");
        this.setIconImage(gameIcon.getImage());
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        save = new JMenuItem("Save");

        // Making the hotkeys to give easier access to the buttons
        // Setting up the ActionCommends to correspond to the buttons when called (in
        // gamelogic)
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        save.setActionCommand("SAVE");
        buyButton.setMnemonic(KeyEvent.VK_B);
        buyButton.setActionCommand("BUY");
        housButton.setMnemonic(KeyEvent.VK_H);
        housButton.setActionCommand("HOUSE");
        sellButton.setMnemonic(KeyEvent.VK_S);
        sellButton.setActionCommand("SELL");
        throwButton.setMnemonic(KeyEvent.VK_T);
        throwButton.setActionCommand("THROW");
        passButton.setMnemonic(KeyEvent.VK_P);
        passButton.setActionCommand("PASS");
        tradeButton.setMnemonic(KeyEvent.VK_R);
        tradeButton.setActionCommand("TRADE");

        menu.add(save);
        menuBar.add(menu);
        // Updating the border of the tiles, depending on who owns them (if anyone does
        // of course)
        for (JButton tile : tileButtons) {
            highlightTileBorder(tile, null, false);
        }
        this.setJMenuBar(menuBar);
        this.setVisible(true);

    }

    // Setting up the board with tiles in a gridbaglayout, and setting up the middle
    // part with the image of the duck
    private JPanel createBoard() {
        JPanel board = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        int cnt = 0;
        for (int i = 0; i < 8; i++) {
            constraints.gridx = i;
            constraints.gridy = 0;
            board.add(createTiles("Kacsa" + i, cnt), constraints);
            cnt++;
        }
        for (int i = 1; i < 7; i++) {
            constraints.gridx = 7;
            constraints.gridy = i;
            board.add(createTiles("Kacsa" + i, cnt), constraints);
            cnt++;
        }
        for (int i = 7; i >= 0; i--) {
            constraints.gridx = i;
            constraints.gridy = 7;
            board.add(createTiles("Kacsa" + i, cnt), constraints);
            cnt++;
        }
        for (int i = 6; i >= 1; i--) {
            constraints.gridx = 0;
            constraints.gridy = i;
            board.add(createTiles("Kacsa" + i, cnt), constraints);
            cnt++;
        }
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 8;
        constraints.gridheight = 8;
        constraints.fill = GridBagConstraints.BOTH;
        JPanel middle = new JPanel();
        ImageIcon duckIcon = new ImageIcon("hf\\src\\main\\resources\\duck2.png");
        middle.setLayout(new BorderLayout());
        middle.add(new JLabel(duckIcon), BorderLayout.CENTER);
        middle.setBackground(new Color(149, 248, 244));
        board.add(middle, constraints);
        return board;
    }

    // Creating the buttons for the tiles, also setting up their command
    private JButton createTiles(String name, int cnt) {
        JButton button = new JButton();
        button.setActionCommand("TILE_" + cnt);
        button.setBackground(tiles.get(cnt).getTileColor());
        // Putting the players onto the buttons
        int iconSize = 80;
        button.setIcon(new PlayerIcon(tiles.get(cnt), iconSize));
        button.setPreferredSize(new Dimension(iconSize, iconSize));
        tileButtons.add(button);
        return button;
    }

    // Repainting all the tiles after the updates that couldve occured to them
    public void repaintAllTiles() {
        for (JButton button : tileButtons) {
            button.revalidate();
            button.repaint();
        }
    }

    // Creating the right side of the screen, the whole menu
    private JPanel createMenu() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Info of the selected tile
        infos = new JPanel(new BorderLayout());
        infos.setPreferredSize(new Dimension(200, 150));
        infos.setBackground(Color.gray);
        tileInfo = new JTextArea();
        tileInfo.setEditable(false);
        tileInfo.setLineWrap(true);
        tileInfo.setWrapStyleWord(true);
        tileInfo.setFont(new Font("Times New Roman", Font.BOLD, 16));
        tileInfo.setPreferredSize(new Dimension(200, 140));
        infos.add(tileInfo, BorderLayout.SOUTH);
        menu.add(infos);
        // Spacing for better look
        menu.add(Box.createRigidArea(new Dimension(0, 20)));

        menu.setBackground(new Color(149, 248, 244));
        // Setting up the buttons for the tiles
        buyButton = new JButton("Buy");
        housButton = new JButton("House");
        // Making them centered
        buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        housButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyButton.setEnabled(false);
        housButton.setEnabled(false);
        sellButton = new JButton("Sell");
        sellButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellButton.setEnabled(false);
        tradeButton = new JButton("Trade");
        tradeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        tradeButton.setEnabled(false);

        menu.add(buyButton);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(housButton);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(sellButton);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(tradeButton);
        menu.add(Box.createRigidArea(new Dimension(0, 50)));
        // Making it so the other information starts at the bottom
        menu.add(Box.createVerticalGlue());
        // Showing the result of the dicethrow
        JPanel dicePanel = new JPanel();
        dicePanel.setLayout(new BoxLayout(dicePanel, BoxLayout.Y_AXIS));
        JLabel eredmeny = new JLabel("Result:");
        eredmeny.setAlignmentX(Component.CENTER_ALIGNMENT);
        eredmeny.setFont(new Font("Times New Roman", Font.BOLD, 22));
        dicePanel.add(eredmeny);
        diceResult = new JTextField();
        diceResult.setFont(new Font("Times New Roman", Font.BOLD, 22));
        diceResult.setEnabled(false);
        diceResult.setMaximumSize(new Dimension(50, 30));
        diceResult.setAlignmentX(Component.CENTER_ALIGNMENT);
        dicePanel.add(diceResult);
        dicePanel.setBackground(new Color(149, 248, 244));
        dicePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Setting up the buttons for the gameplay, like throw pass and throw
        throwButton = new JButton("Throw");
        throwButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dicePanel.add(throwButton);
        dicePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        passButton = new JButton("Pass");
        passButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        passButton.setEnabled(false);
        dicePanel.add(passButton);
        dicePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(dicePanel);
        return menu;
    }

    // Creating the panels for the players based on how many are them at the bottom
    // of the screen
    private JPanel createPlayers() {
        playersPanel = new JPanel(new GridLayout(1, 4));
        // Just in case they were updated
        playerAreas.clear();
        for (int i = 0; i < players.size(); i++) {
            JTextArea area = createPlayer(players.get(i).getName(), players.get(i).getMoney(),
                    players.get(i).getPieceColor());
            playerAreas.add(area);
            playersPanel.add(area);
        }
        return playersPanel;
    }

    // Creating a singular panel for the player based on his stats
    private JTextArea createPlayer(String name, int money, Color pieceColor) {
        JTextArea player = new JTextArea();
        player.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Formatting his information into multiple lines to look cleaner
        // %s for string, %d for integer etc and the %n puts a new line
        String info = String.format(
                " Name: %s%n Money: %d%n",
                name, money);
        player.setBackground(pieceColor);
        player.setOpaque(true);
        player.setFont(new Font("Times New Roman", Font.BOLD, 30));
        player.setEditable(false);
        player.setLineWrap(true);
        player.setWrapStyleWord(true);
        player.setEditable(false);
        player.setPreferredSize(new Dimension(150, 150));
        player.setText(info);
        return player;
    }

    // Updateing the playerpanels to show their new stats
    public void refreshPlayerPanels(List<Player> players) {
        for (int i = 0; i < players.size() && i < playerAreas.size(); i++) {
            Player p = players.get(i);
            JTextArea area = playerAreas.get(i);
            String info = String.format(
                    " Name: %s%n Money: %d%n",
                    p.getName(), p.getMoney());
            area.setText(info);
        }
    }

    // Updating the information about the tiles to show their new stats
    public void refreshHouseCount(Tile tile) {
        String info = String.format(
                "Name: %s%nPrice: %d%nHouse cost: %d%nTax: %d%nHouses: %d%nOwner: %s%nSell value: %d",
                tile.getName(),
                tile.getPrice(),
                tile.getHouseCost(),
                tile.getTax(),
                tile.getNumberOfHouses(),
                tile.getOwner().getName(),
                tile.getSellValue());
        tileInfo.setText(info);
    }

    // Changing the highlighting of the players, based on who's turn it is
    public void highlightCurrentPlayer(Player currentPlayer, List<Player> players) {
        for (int i = 0; i < players.size() && i < playerAreas.size(); i++) {
            Player p = players.get(i);
            JTextArea area = playerAreas.get(i);

            Color base = p.getPieceColor();
            // color of the player who's waiting for his game
            Color normal = base.darker().darker();
            // color of the player who's turn it is
            Color selected = base.brighter().brighter();

            area.setOpaque(true);
            // if the player has lost gray him out
            if (p.getLost()) {
                area.setBackground(Color.LIGHT_GRAY);
                area.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
            } else if (p == currentPlayer) {
                area.setBackground(selected);
                area.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            } else {
                area.setBackground(normal);
                area.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
        }
    }

    // If the game ended, disable all the buttons
    public void gameOverScreen() {
        throwButton.setEnabled(false);
        passButton.setEnabled(false);
        housButton.setEnabled(false);
        buyButton.setEnabled(false);
        sellButton.setEnabled(false);
        tradeButton.setEnabled(false);
    }

    // Handling a player losing
    public void playerLost(Player lostPlayer, Player currentPlayer, List<Player> players) {
        // show a message of the player who has lost
        JOptionPane.showMessageDialog(null, lostPlayer.getName() + " lost!");
        // delete his piece
        for (JButton tileButton : tileButtons)
            tileButton.repaint();
        // gray out his panel
        highlightCurrentPlayer(currentPlayer, players);
        throwButton.setEnabled(true);
        passButton.setEnabled(true);
    }

    // Connecting the buttons to their actionlisteners
    public void connectButtons(ActionListener e) {
        save.addActionListener(e);
        throwButton.addActionListener(e);
        passButton.addActionListener(e);
        buyButton.addActionListener(e);
        housButton.addActionListener(e);
        sellButton.addActionListener(e);
        tradeButton.addActionListener(e);
        for (JButton tileButton : tileButtons) {
            tileButton.addActionListener(e);
        }
    }

    // Setting the label of the diceresult
    public void updateDiceResult(List<Integer> result) {
        diceResult.setText("" + (result.get(0) + result.get(1)));
    }

    // Putting a message on screen
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    // Highlighting the selected tile (top right of the screen)
    public void hightLightTile(String info, Color color) {
        tileInfo.setText(info);
        infos.setBackground(color);
    }

    // Highlighting the border of a tile, based on the owner
    public void highlightTileBorder(JButton tile, Color playerColor, boolean bought) {
        if (bought)
            tile.setBorder(BorderFactory.createLineBorder(playerColor, 4));
        else
            // if the tile has no owner
            tile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }
}