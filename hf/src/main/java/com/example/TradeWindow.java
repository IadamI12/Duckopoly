package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


public class TradeWindow implements ActionListener{
private Player currentPlayer;
private Tile tile;
private Board board;
private JFrame frame;
    private JTextField area;
    private JButton confirmButton;
    public TradeWindow(Board board,Player currentPlayer,Tile tile){
    this.board = board;
    this.currentPlayer = currentPlayer;
    this.tile = tile;
     frame = new JFrame("Trade Window");
     frame.setLayout(new BorderLayout());
    frame.setSize(400, 300);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
    frame.setResizable(false);
    frame.setLocationRelativeTo(board);
    ImageIcon icon = new ImageIcon("hf/src/main/java/com/example/villager.png");
    frame.setIconImage(icon.getImage());
     JPanel leftPanel = new JPanel();
     leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

     JTextArea tileInfo = new JTextArea();

     JPanel tileColorJPanel = new JPanel();
     tileColorJPanel.setBackground(tile.getTileColor());
     tileColorJPanel.setPreferredSize(new Dimension(50,150));
        leftPanel.add(tileColorJPanel);

         String info = String.format(
        "Name: %s%nPrice: %d%nHouse cost: %d%nTax: %d%nHouses: %d%nOwner: %s%nSell value: %d",
        tile.getName(),
        tile.getPrice(),
        tile.getHouseCost(),
        tile.getTax(),
        tile.getNumberOfHouses(),
        tile.getOwner().getName(),
        tile.getSellValue()
    );
        tileInfo.setEditable(false);
        tileInfo.setLineWrap(true);
        tileInfo.setWrapStyleWord(true);
        tileInfo.setFont(new Font("Times New Roman", Font.BOLD, 16));
        tileInfo.setPreferredSize(new Dimension(200, 140));
        tileInfo.setText(info);

        leftPanel.add(tileInfo);

        frame.add(leftPanel, BorderLayout.WEST);
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

area. getDocument().addDocumentListener(new DocumentListener() {
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
private void checkConfirmButton(){
    try{
    if (area.getText().isEmpty() || currentPlayer.getMoney() < Integer.parseInt(area.getText())){
        confirmButton.setEnabled(false);
    }    
    else{
        confirmButton.setEnabled(true);
    }}catch (NumberFormatException e){
        confirmButton.setEnabled(false);
    }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       
        if (e.getSource() == confirmButton){
            int offer = Integer.parseInt(area.getText());
            Player owner = tile.getOwner();
            currentPlayer.setMoney(currentPlayer.getMoney() - offer);
            owner.setMoney(owner.getMoney() + offer);
            tile.setOwner(currentPlayer);
            currentPlayer.setTiles(tile);
            owner.getTiles().remove(tile);
            board.refreshHouseCount(tile);
            int tileIndex = board.getTiles().indexOf(tile);
            board.highlightTileBorder(board.getTileButtons().get(tileIndex),currentPlayer.getPieceColor(),true);
            board.refreshPlayerPanels(board.getPlayers());
            frame.dispose();
        }
    }
    
}
