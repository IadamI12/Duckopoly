package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class Setup{
    public int[] load(List<Player> players, List<Tile> tiles,List<Bonus> bonusTiles) throws EndGame{
  try{
    loadTiles(tiles,"hf\\src\\main\\java\\com\\example\\tiles.txt");
} catch (IOException e){
    throw new EndGame("Failed to load the tiles from file");
}
try{
    loadBonusTiles(bonusTiles,"hf\\src\\main\\java\\com\\example\\bonustiles.txt");
} catch (IOException e){
    throw new EndGame("Failed to load the bonustiles from file");
}


        String[] responses = {"New game","Load old game","Cancel"};
        JLabel message = new JLabel("Choose an option to start:");
        message.setFont(new Font("Times New Roman",Font.BOLD,22));
        ImageIcon icon = new ImageIcon("hf\\src\\main\\java\\com\\example\\duck.png");
        int response = JOptionPane.showOptionDialog(null, message, "Duckopoly", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon, responses, 0);
        if (response == -1 || response == 2){
            throw new EndGame("Game start has been cancelled.");
        }
        else if (response == 0){
            newGame(players,tiles,bonusTiles);
            return null;
        }
        else{
            try{
            return loadFromFile(players,tiles,bonusTiles);

            }  catch (IOException e) {
        e.printStackTrace();
        throw new EndGame("Failed to load game (IO): " + e.getClass().getName());
    } catch (ClassNotFoundException er) {
        er.printStackTrace();
        throw new EndGame("Failed to load game (Class): " + er.getClass().getName());
    } catch (Exception ex) {
        ex.printStackTrace();
        throw new EndGame("Failed to load game: " + ex.getClass().getName() + " - " + ex.getMessage());
    }

            //return;
        }
    }
    public void newGame(List<Player> players, List<Tile> tiles,List<Bonus> bonusTiles) throws EndGame{
        JSlider slider = new JSlider(JSlider.HORIZONTAL,2,4,2);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setFont(new Font("Times New Roman",Font.BOLD,14));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel label = new JLabel("Select the number of players:");
        label.setFont(new Font("Times New Roman", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);
        panel.add(slider, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(null,panel,"Number of players: ",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
        int playerCount = slider.getValue();
        if (result == JOptionPane.OK_OPTION) {
        
            JOptionPane.showMessageDialog(null, "The game will start with " + playerCount + " players!");
        } else {
            throw new EndGame("Game start has been cancelled.");
        }
        List <Color> playerColors = new ArrayList<>();
        playerColors.add(Color.CYAN);
        playerColors.add(Color.ORANGE);
        playerColors.add(Color.GREEN);
        playerColors.add(Color.MAGENTA);
        for (int i = 0; i < playerCount; i++){
            //esetleg itt a jatekosok ikonjait mellé lehetne rakni?
           String name = JOptionPane.showInputDialog(null,"Enter the name for Player" + (i + 1) + ": ","Player Name",JOptionPane.PLAIN_MESSAGE);
           if (name == null || name.length() <= 0){
            throw new EndGame("Game start has been cancelled.");
           }
           Player newPlayer = new Player(i, name.trim(), 200,0, null,false, playerColors.get(i),false);
           players.add(newPlayer);
        }
    }
    public int[] loadFromFile(List<Player> players, List<Tile> tiles, List<Bonus> bonusTiles) throws ClassNotFoundException, IOException{
        String filename = "gamesave.txt";
            java.io.File f = new java.io.File(filename);
System.out.println("Looking for save file at: " + f.getAbsolutePath());
    System.out.println("File exists: " + f.exists());
    
    if (!f.exists()) {
        throw new IOException("Save file not found at: " + f.getAbsolutePath());
    }
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
                List<Player> loadedPlayers = (List<Player>) ois.readObject();
        List<Tile> loadedTiles = (List<Tile>) ois.readObject();
        List<Bonus> loadedBonusTiles = (List<Bonus>) ois.readObject();
        int savedTurn = ois.readInt();
        int savedCurrentPlayerIndex = ois.readInt();
        int savedCurrentPlayerThrown = ois.readInt();
        players.clear();
        players.addAll(loadedPlayers);
        
        tiles.clear();
        tiles.addAll(loadedTiles);
        
        bonusTiles.clear();
        bonusTiles.addAll(loadedBonusTiles);
        ois.close();
        return new int[]{savedTurn,savedCurrentPlayerIndex, savedCurrentPlayerThrown};
    }   


    private void loadTiles(List<Tile> tiles, String filename) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(filename));
            while (true){
                String line1 = br.readLine();
                if (line1 == null) break;
                String[] parts = line1.split(";");
                String name = parts[0];
                int price = Integer.parseInt(parts[1]);
                int houseCost = Integer.parseInt(parts[2]);
                int income = Integer.parseInt(parts[3]);
                int numberOfHouses = Integer.parseInt(parts[4]);
                String tileColor = parts[5];
                int purchasable = Integer.parseInt(parts[6]);
                int housePurchasable = Integer.parseInt(parts[7]);
                tiles.add(new Tile(name, price, houseCost, income, numberOfHouses,tileColor,purchasable, housePurchasable)); 
            }
        br.close();
    }
    private void loadBonusTiles(List<Bonus> bonusTiles, String filename) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while(true){
            String line2 = br.readLine();
            if (line2 == null) break;
            String[] parts = line2.split(";");
            String story = parts[0];
            int money = Integer.parseInt(parts[1]);
            int profit = Integer.parseInt(parts[2]);
            bonusTiles.add(new Bonus(story, money, profit));
        }
        br.close();
    }

}
