package com.example.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.example.logic.Player;
import com.example.logic.Tile;
/**
 * A class handling the trading
 * 
 * @author Pap Ádám
 */
public class TradeWindow implements ActionListener {
    /**
     * The player who wants to trade
     */
    private Player currentPlayer;
    /**
     * The tile involved in the trade
     */
    private Tile tile;
    /**
     * The game board
     */
    private Board board;
    /**
     * The frame of the trade window
     */
    private JFrame frame;
    /**
     * The text field for entering the offer amount
     */
    private JTextField area;
    /**
     * The confirm button for the trade
     */
    private JButton confirmButton;

    /**
     * Constructor for the TradeWindow class
     * 
     * @param board         - the game board
     * @param currentPlayer - the player who wants to trade
     * @param tile          - the tile involved in the trade
     */
    public TradeWindow(Board board, Player currentPlayer, Tile tile) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.tile = tile;

        // Creating the trade window
        frame = new JFrame("Trade Window");
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        // Setting the location to the middle of the board
        frame.setLocationRelativeTo(board);
        String filename = "hf/src/main/resources/villager.png";
        String filePath = filename.replace("/", File.separator);
        ImageIcon icon = new ImageIcon(filePath);
        frame.setIconImage(icon.getImage());
        // Creating the left panel and filling it with the tile info
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JTextArea tileInfo = new JTextArea();
        // Creating the panel that shows the tile's color
        JPanel tileColorJPanel = new JPanel();
        tileColorJPanel.setBackground(tile.getTileColor());
        tileColorJPanel.setPreferredSize(new Dimension(50, 150));
        leftPanel.add(tileColorJPanel);

        String info = String.format(
                "Name: %s%nPrice: %d%nHouse cost: %d%nTax: %d%nHouses: %d%nOwner: %s%nSell value: %d",
                tile.getName(),
                tile.getPrice(),
                tile.getHouseCost(),
                tile.getTax(),
                tile.getNumberOfHouses(),
                tile.getOwner().getName(),
                tile.getSellValue());
        tileInfo.setEditable(false);
        tileInfo.setLineWrap(true);
        tileInfo.setWrapStyleWord(true);
        tileInfo.setFont(new Font("Times New Roman", Font.BOLD, 16));
        tileInfo.setPreferredSize(new Dimension(200, 140));
        tileInfo.setText(info);

        leftPanel.add(tileInfo);
        frame.add(leftPanel, BorderLayout.WEST);

        // Creating the right panel and filling it with the offer area and confirm
        // button
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JLabel amount = new JLabel("Enter offer amount:");
        amount.setFont(new Font("Times New Roman", Font.BOLD, 16));
        amount.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 75)));
        rightPanel.add(amount);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        area = new JTextField();
        area.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        area.setMaximumSize(new Dimension(100, 30));
        // Adding a document listener to the offer amount text field, so it updates even
        // if the user pastes something in the field
        area.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkConfirmButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkConfirmButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkConfirmButton();
            }
        });

        rightPanel.add(area);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        confirmButton = new JButton("Confirm Trade");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(this);
        confirmButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        rightPanel.add(confirmButton);
        area.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.setBackground(new Color(149, 248, 244));
        frame.add(rightPanel, BorderLayout.CENTER);

    }

    /**
     * Check if the confirm button should be enabled
     */
    private void checkConfirmButton() {
        try {
            // disabling the button if the input is invalid
            if (area.getText().isEmpty() || currentPlayer.getMoney() < Integer.parseInt(area.getText())) {
                confirmButton.setEnabled(false);
            } else {
                confirmButton.setEnabled(true);
            }
        } catch (NumberFormatException e) {
            confirmButton.setEnabled(false);
        }
    }

    /**
     * Handling the confirm button being pressed
     * 
     * @param e - the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == confirmButton) {
            int offer = Integer.parseInt(area.getText());
            Player owner = tile.getOwner();
            // Transferring the money and the tile
            currentPlayer.setMoney(currentPlayer.getMoney() - offer);
            owner.setMoney(owner.getMoney() + offer);
            tile.setOwner(currentPlayer);
            currentPlayer.setTiles(tile);
            owner.getTiles().remove(tile);
            // Refreshing the board to show the changes
            board.refreshHouseCount(tile);
            int tileIndex = board.getTiles().indexOf(tile);
            board.highlightTileBorder(board.getTileButtons().get(tileIndex), currentPlayer.getPieceColor(), true);
            board.refreshPlayerPanels(board.getPlayers());
            // Closing the trade window
            frame.dispose();
        }
    }

}
