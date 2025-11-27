package com.example;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
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
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board extends JFrame implements Serializable, ActionListener {
    private GameLogic game = new GameLogic();

    // private List<Player> players = new ArrayList<>();
    private List<JButton> tiles = new ArrayList<>();
    private List<Dice> dices = new ArrayList<>();

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

    public boolean isThrown() {
        return thrown;
    }

    public void setThrown(boolean thrown) {
        this.thrown = thrown;
    }

    public Board(GameLogic game, List<Player> players) {
        this.game = game;
        // this.players = players;
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
        // ne statikus legyen a link?
        gameIcon = new ImageIcon("hf\\src\\main\\java\\com\\example\\duck.png");
        this.setIconImage(gameIcon.getImage());
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        save = new JMenuItem("Save");
        save.addActionListener(this);

        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        buyButton.setMnemonic(KeyEvent.VK_B);
        housButton.setMnemonic(KeyEvent.VK_H);
        sellButton.setMnemonic(KeyEvent.VK_S);
        throwButton.setMnemonic(KeyEvent.VK_T);
        passButton.setMnemonic(KeyEvent.VK_P);

        menu.add(save);
        menuBar.add(menu);
        tileBorderUpdate();
        this.setJMenuBar(menuBar);
        this.setVisible(true);
        
    }

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
        ImageIcon duckIcon = new ImageIcon("hf\\src\\main\\java\\com\\example\\duck2.png");
        middle.setLayout(new BorderLayout());
        middle.add(new JLabel(duckIcon), BorderLayout.CENTER);
        middle.setBackground(new Color(149, 248, 244));
        board.add(middle, constraints);
        return board;
    }

    private JButton createTiles(String name, int cnt) {
        JButton button = new JButton();
        button.setBackground(game.getTiles().get(cnt).getTileColor());
        //button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        button.addActionListener(this);
        // ezzz??
        int iconSize = 80;
        button.setIcon(new PlayerIcon(game.getTiles().get(cnt), iconSize));
        button.setPreferredSize(new Dimension(iconSize, iconSize));
        tiles.add(button);
        return button;
    }

    public void repaintTile(Tile tile) {
        int idx = game.getTiles().indexOf(tile);
        if (idx >= 0 && idx < tiles.size()) {
            tiles.get(idx).repaint();
        }
    }

    private JPanel createMenu() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createLineBorder(Color.BLACK));

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

        menu.add(Box.createRigidArea(new Dimension(0, 20)));
        menu.setBackground(new Color(149, 248, 244));
        buyButton = new JButton("Buy");
        housButton = new JButton("House");
        buyButton.addActionListener(this);
        housButton.addActionListener(this);
        buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        housButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyButton.setEnabled(false);
        housButton.setEnabled(false);
        sellButton = new JButton("Sell");
        sellButton.addActionListener(this);
        sellButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellButton.setEnabled(false);
        tradeButton = new JButton("Trade");
        tradeButton.addActionListener(this);
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
        menu.add(Box.createVerticalGlue());
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
        throwButton = new JButton("Throw");
        throwButton.addActionListener(this);
        throwButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dicePanel.add(throwButton);
        dicePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        passButton = new JButton("Pass");
        passButton.addActionListener(this);
        passButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        passButton.setEnabled(false);
        dicePanel.add(passButton);
        dicePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(dicePanel);
        return menu;
    }

    private JPanel createPlayers() {
        playersPanel = new JPanel(new GridLayout(1, 4));
        playerAreas.clear();
        List<Player> players = game.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            JTextArea area = createPlayer(players.get(i).getName(), players.get(i).getMoney(),
                    players.get(i).getPieceColor());
            playerAreas.add(area);
            playersPanel.add(area);
        }
        return playersPanel;
    }

    private JTextArea createPlayer(String name, int money, Color pieceColor) {
        JTextArea player = new JTextArea();
        player.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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

    public void refreshPlayerPanels() {
        List<Player> players = game.getPlayers();
        for (int i = 0; i < players.size() && i < playerAreas.size(); i++) {
            Player p = players.get(i);
            JTextArea area = playerAreas.get(i);
            String info = String.format(
                    " Name: %s%n Money: %d%n",
                    p.getName(), p.getMoney());
            area.setText(info);
        }
    }

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

    public void highlightCurrentPlayer(Player currentPlayer) {
        List<Player> players = game.getPlayers();
        for (int i = 0; i < players.size() && i < playerAreas.size(); i++) {
            Player p = players.get(i);
            JTextArea area = playerAreas.get(i);
            Color base = p.getPieceColor();
            Color normal = base.darker().darker();
            Color selected = base.brighter().brighter();

            area.setOpaque(true);
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
    // rolldice segedfgv akkorviszont a jobb panelnek privatnak kene lennie? ezt a
    // runbol hivogatom
    // run és konstruktor ketteszedese gui+program?
    // grafika megjelenitese

    // while(true){
    // koronkent valtozas + itt checkolni hogy ment-e / vége + kiesett jatekos?
    // kéne gombok class?
    // }

    // az nagol magyar keverés weird xd
    public void updatePassButtonState() {
        Player p = game.getCurrentPlayer();
        int netWorth = game.getCurrentPlayerNetWorth();
        if (netWorth < 0) {
            passButton.setEnabled(false);
            throwButton.setEnabled(false);
            try {
                Player lostPlayer = game.currentPlayerLost();
                if (lostPlayer != null) {
                    playerLost(lostPlayer);
                }
            } catch (PlayerWon pw) {
                gameOverScreen();
                JOptionPane.showMessageDialog(null, pw.getMessage());
            }
            return;
        }

        if (p.getMoney() < 0) {
            throwButton.setEnabled(false);
            passButton.setEnabled(false);
        } else {
            if (!thrown && p.getMoney() >= 0) {
                throwButton.setEnabled(true);

            } else {
                throwButton.setEnabled(false);
            }
        if  (p.getJailTime() > 0){
            passButton.setEnabled(false);
        }else if (thrown){
            passButton.setEnabled(true);
        }
        }
    }

    public void gameOverScreen() {
        throwButton.setEnabled(false);
        passButton.setEnabled(false);
        housButton.setEnabled(false);
        buyButton.setEnabled(false);
        sellButton.setEnabled(false);
        tradeButton.setEnabled(false);
    }

    public void playerLost(Player lostPlayer) {
        JOptionPane.showMessageDialog(null, lostPlayer.getName() + " lost!");
        refreshPlayerPanels();
        for (JButton tileButton : tiles)
            tileButton.repaint();
        highlightCurrentPlayer(game.getCurrentPlayer());
        throwButton.setEnabled(true);
        passButton.setEnabled(true);
    }

    private boolean isTradable(Tile tile) {
        return ((tile.getOwner() != game.getCurrentPlayer()) && (tile.getOwner().getId() != 100));
    }

    private boolean currentPlayerHasAllTilesOfColorGroup(Color color) {
        List<Tile> playerTiles = game.getCurrentPlayer().getTiles();
        int sum1 = 0;
        int sum2 = 0;
        for (Tile tile : playerTiles) {
            if (tile.getTileColor().equals(color)) {
                sum1++;
            }
        }
        for (Tile tile : game.getTiles()) {
            if (tile.getTileColor().equals(color)) {
                sum2++;
            }
        }
        return sum1 == sum2;
    }

    // public void tileBorderUpdate() {
    //     for (int i = 0; i < tiles.size(); i++) {
    //         if (game.getTiles().get(i).getOwner().equals(game.getCurrentPlayer())) {
    //             tiles.get(i).setBorder(BorderFactory.createLineBorder(game.getCurrentPlayer().getPieceColor(), 4));
    //         }
    //         if (!game.getTiles().get(i).getOwner().equals(game.getCurrentPlayer())) {
    //             tiles.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    //         }
    //     }
    // }
   public void tileBorderUpdate() {
        for (int i = 0; i < tiles.size(); i++) {
            if (!game.getTiles().get(i).getOwner().getName().equals("-")) {
                tiles.get(i).setBorder(BorderFactory.createLineBorder(game.getTiles().get(i).getOwner().getPieceColor(), 4));
            }
            else {
                tiles.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            try {
                game.saveGame("gamesave.txt");
                JOptionPane.showMessageDialog(null, "Game saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to save game!" + ex.getMessage());
            }
        }
        // boolean lost = false;
        if (e.getSource() == throwButton) {

            thrown = true;
            throwButton.setEnabled(false);
            List<Integer> result = game.diceThrow();
            diceResult.setText("" + (result.get(0) + result.get(1)));
            if (!game.getCurrentPlayer().getJailed())
                game.movePlayer(result.get(0) + result.get(1));
            else {
                passButton.setEnabled(true);
                if (result.get(0) == result.get(1) || game.getCurrentPlayer().getJailTime() == 1) {
                    if (result.get(0) == result.get(1)) {
                        JOptionPane.showMessageDialog(null,
                                "You rolled: " + result.get(0) + " and " + result.get(1) + ", you are free!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Jailtime left: 0 rounds!");
                    }
                    game.getCurrentPlayer().setJailed(false);
                    game.getCurrentPlayer().setJailTime(0);
                } else {
                   // game.getCurrentPlayer().setJailTime(game.getCurrentPlayer().getJailTime() + 1);
                    JOptionPane.showMessageDialog(null,
                            "Jailtime left: " + (2 - game.getCurrentPlayer().getJailTime()) + " rounds!");
                }
            }

        }
        if (e.getSource() == passButton) {
            thrown = false;
            game.newTurn();
            updatePassButtonState();
            if (!game.getCurrentPlayer().getJailed()) {
                passButton.setEnabled(false);
            }
            housButton.setEnabled(false);
            sellButton.setEnabled(false);
        }
        for (int i = 0; i < tiles.size(); i++) {
            if (e.getSource() == tiles.get(i)) {
                game.selectTile(i);
                Tile temp = game.getTiles().get(i);
                String info = String.format(
                        "Name: %s%nPrice: %d%nHouse cost: %d%nTax: %d%nHouses: %d%nOwner: %s%nSell value: %d",
                        temp.getName(), temp.getPrice(), temp.getHouseCost(), temp.getTax(),
                        temp.getNumberOfHouses(), temp.getOwner().getName(), temp.getSellValue());
                tileInfo.setText(info);
                infos.setBackground(temp.getTileColor());
                if (game.getCurrentPlayer().getMoney() < 0 && game.getCurrentPlayer().getTiles().contains(
                        (game.getTiles().get(i)))) {
                    sellButton.setEnabled(true);
                }
                if (thrown) {
                    if (i == game.getCurrentPlayer().getPosition()) {

                        if (game.getTiles().get(i).getPurchasable() == true &&
                                game.getCurrentPlayer().getMoney() >= game.getTiles().get(i).getPrice()) {
                            buyButton.setEnabled(true);
                        } else {
                            buyButton.setEnabled(false);
                        }
                        if (game.getTiles().get(i).getHousePurchasable() == true &&
                                game.getCurrentPlayer().getMoney() >= game.getTiles().get(i).getHouseCost() &&
                                game.getTiles().get(i).getNumberOfHouses() < 5 &&
                                game.getCurrentPlayer().getTiles().contains(game.getTiles().get(i)) &&
                                currentPlayerHasAllTilesOfColorGroup(
                                        game.getTiles().get(game.getCurrentPlayer().getPosition()).getTileColor())) {
                            housButton.setEnabled(true);
                        } else {
                            housButton.setEnabled(false);
                        }
                    } else {
                        buyButton.setEnabled(false);
                        housButton.setEnabled(false);
                    }
                    if (game.getCurrentPlayer().getTiles().contains(game.getTiles().get(i))) {
                        sellButton.setEnabled(true);
                    } else {
                        sellButton.setEnabled(false);
                    }
                }
                if (isTradable(game.getTiles().get(i))) {
                    tradeButton.setEnabled(true);
                } else {
                    tradeButton.setEnabled(false);
                }
            }
        }
        // splitelni kéne a GUI-t szerintem itt ezeket egy nagy changegamecucc
        if (e.getSource() == buyButton) {

            game.getTiles().get(game.getCurrentPlayer().getPosition()).setPurchasable(false);
            game.getCurrentPlayer().setMoney(game.getCurrentPlayer().getMoney()
                    - game.getTiles().get(game.getCurrentPlayer().getPosition()).getPrice());
            game.getCurrentPlayer().setTiles(game.getTiles().get(game.getCurrentPlayer().getPosition()));
            buyButton.setEnabled(false);
            sellButton.setEnabled(true);
            if (game.getTiles().get(game.getCurrentPlayer().getPosition()).getHousePurchasable() == true &&
                    game.getCurrentPlayer().getMoney() >= game.getTiles().get(game.getCurrentPlayer().getPosition())
                            .getHouseCost()
                    &&
                    game.getTiles().get(game.getCurrentPlayer().getPosition()).getNumberOfHouses() < 5 &&
                    game.getCurrentPlayer().getTiles()
                            .contains(game.getTiles().get(game.getCurrentPlayer().getPosition()))
                    &&
                    currentPlayerHasAllTilesOfColorGroup(
                            game.getTiles().get(game.getCurrentPlayer().getPosition()).getTileColor())) {
                housButton.setEnabled(true);
            } else {
                housButton.setEnabled(false);
            }
            game.getTiles().get(game.getCurrentPlayer().getPosition()).setOwner(game.getCurrentPlayer());
            tiles.get(game.getCurrentPlayer().getPosition())
                    .setBorder(BorderFactory.createLineBorder(game.getCurrentPlayer().getPieceColor(), 4));
            refreshHouseCount(game.getTiles().get(game.getCurrentPlayer().getPosition()));
            refreshPlayerPanels();
            updatePassButtonState();
        }
        if (e.getSource() == housButton) {

            for (Tile tile : game.getTiles()) {
                if (tile.getTileColor()
                        .equals(game.getTiles().get(game.getCurrentPlayer().getPosition()).getTileColor())) {
                    tile.setNumberOfHouses(tile.getNumberOfHouses() + 1);
                    tile.setTax(tile.getTax() + tile.getHouseCost());
                }
            }
            game.getCurrentPlayer().setMoney(game.getCurrentPlayer().getMoney()
                    - game.getTiles().get(game.getCurrentPlayer().getPosition()).getHouseCost());

            // game.getCurrentPlayer().getTiles().get(game.getCurrentPlayer().getTiles().indexOf(game.getTiles().get(game.getCurrentPlayer().getPosition()))).setNumberOfHouses(
            // game.getCurrentPlayer().getTiles().get(game.getCurrentPlayer().getTiles().indexOf(game.getTiles().get(game.getCurrentPlayer().getPosition()))).getNumberOfHouses()
            // + 1);
            // game.getTiles().get(game.getCurrentPlayer().getPosition()).setTax(game.getTiles().get(game.getCurrentPlayer().getPosition()).getTax()+
            // game.getTiles().get(game.getCurrentPlayer().getPosition()).getHouseCost());
            if (game.getTiles().get(game.getCurrentPlayer().getPosition()).getHousePurchasable() == true &&
                    game.getCurrentPlayer().getMoney() >= game.getTiles().get(game.getCurrentPlayer().getPosition())
                            .getHouseCost()
                    &&
                    game.getTiles().get(game.getCurrentPlayer().getPosition()).getNumberOfHouses() < 5 &&
                    game.getCurrentPlayer().getTiles()
                            .contains(game.getTiles().get(game.getCurrentPlayer().getPosition()))) {
                housButton.setEnabled(true);
            } else {
                housButton.setEnabled(false);
            }
            game.getTiles().get(game.getCurrentPlayer().getPosition())
                    .setSellValue(game.getTiles().get(game.getCurrentPlayer().getPosition()).getSellValue() +
                            game.getTiles().get(game.getCurrentPlayer().getPosition()).getHouseCost() / 2);

            refreshHouseCount(game.getTiles().get(game.getCurrentPlayer().getPosition()));
            refreshPlayerPanels();
            updatePassButtonState();
        }
        if (e.getSource() == sellButton) {
            game.getCurrentPlayer()
                    .setMoney(game.getCurrentPlayer().getMoney() + game.getSelectedTile().getSellValue());
            game.getCurrentPlayer().getTiles().remove(game.getSelectedTile());
            game.getSelectedTile().setOwner(new Player(100, "-", 0, 0, null, false, Color.BLACK, false));
            game.getSelectedTile().setPurchasable(true);
            game.getSelectedTile().setSellValue(game.getSelectedTile().getPrice() / 2);
            game.getSelectedTile().setNumberOfHouses(0);
            sellButton.setEnabled(false);
            housButton.setEnabled(false);
            refreshHouseCount(game.getSelectedTile());
            refreshPlayerPanels();
            updatePassButtonState();
            tileBorderUpdate();
        }
        if (e.getSource() == tradeButton) {
            TradeWindow tradeWindow = new TradeWindow(this, game.getCurrentPlayer(), game.getSelectedTile());
            tradeButton.setEnabled(false);

        }

    }

}