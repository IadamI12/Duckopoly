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
import java.io.File;

/**
 * A class handling the setup of a new game or loading an old one
 * 
 * @author Pap Ádám
 */
public class Setup {
    /**
     * Default constructor for the Setup class
     */
    public Setup() {
    }

    /**
     * Creating a new game or loading an old one
     * 
     * @param players    - the list of players
     * @param tiles      - the list of tiles
     * @param bonusTiles - the list of bonus tiles
     * @return int[] - an array containing extra information about the loaded game
     *         (turn, currentPlayerIndex, currentPlayerThrown)
     * @throws EndGame - if the setup failed or was cancelled
     */
    public int[] load(List<Player> players, List<Tile> tiles, List<Bonus> bonusTiles) throws EndGame {
        try {
            // loading the tiles from tiles.txt
            loadTiles(tiles, "hf\\src\\main\\resources\\tiles.txt");
        } catch (IOException e) {
            throw new EndGame("Failed to load the tiles from file");
        }
        try {
            // loading the bonusTiles from bonustiles.txt
            loadBonusTiles(bonusTiles, "hf\\src\\main\\resources\\bonustiles.txt");
        } catch (IOException e) {
            throw new EndGame("Failed to load the bonustiles from file");
        }
        String[] responses = { "New game", "Load old game", "Cancel" };
        JLabel message = new JLabel("Choose an option to start:");
        message.setFont(new Font("Times New Roman", Font.BOLD, 22));
        String filename = "hf\\src\\main\\resources\\duck.png";
        String filePath = filename.replace("\\", File.separator);
        ImageIcon icon = new ImageIcon(filePath);
        // saving the response the user gives
        int response = JOptionPane.showOptionDialog(null, message, "Duckopoly", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, icon, responses, 0);
        // cancel the operation if he clicked "Cancel" or closed the application
        if (response == -1 || response == 2) {
            throw new EndGame("Game start has been cancelled.");
            // start a new game
        } else if (response == 0) {
            newGame(players, tiles, bonusTiles);
            return null;
        } else {
            try {
                // load in a saved game if possible
                return loadFromFile(players, tiles, bonusTiles);

            } catch (IOException e) {
                e.printStackTrace();
                throw new EndGame("Failed to load game (IO): " + e.getClass().getName());
            } catch (ClassNotFoundException er) {
                er.printStackTrace();
                throw new EndGame("Failed to load game (Class): " + er.getClass().getName());
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EndGame("Failed to load game: " + ex.getClass().getName() + " - " + ex.getMessage());
            }
        }
    }

    /**
     * Creating a new game
     * 
     * @param players    - the list of players
     * @param tiles      - the list of tiles
     * @param bonusTiles - the list of bonustiles
     * @throws EndGame - if the new game setup was cancelled
     */

    public void newGame(List<Player> players, List<Tile> tiles, List<Bonus> bonusTiles) throws EndGame {

        // amking the slider for selecting the number of players (2-4)
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 2, 4, 2);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setFont(new Font("Times New Roman", Font.BOLD, 14));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel label = new JLabel("Select the number of players:");
        label.setFont(new Font("Times New Roman", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);
        panel.add(slider, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(null, panel, "Number of players: ", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        // storing the value
        int playerCount = slider.getValue();
        if (result == JOptionPane.OK_OPTION) {
            // if the game wasn't cancelled
            JOptionPane.showMessageDialog(null, "The game will start with " + playerCount + " players!");
        } else {
            throw new EndGame("Game start has been cancelled.");
        }
        // setting the color of players
        List<Color> playerColors = new ArrayList<>();
        playerColors.add(Color.CYAN);
        playerColors.add(Color.ORANGE);
        playerColors.add(Color.GREEN);
        playerColors.add(Color.MAGENTA);
        for (int i = 0; i < playerCount; i++) {
            // reading the names of the players
            String name = JOptionPane.showInputDialog(null, "Enter the name for Player" + (i + 1) + ": ", "Player Name",
                    JOptionPane.PLAIN_MESSAGE);
            // if the name is invalid end the process
            if (name == null || name.length() <= 0) {
                throw new EndGame("Game start has been cancelled.");
            }
            // create the players
            Player newPlayer = new Player(i, name.trim(), 150, 0, null, false, playerColors.get(i), false);
            players.add(newPlayer);
        }
    }

    /**
     * Loading the game from a saved file (gamesave.txt)
     * 
     * @param players    - the list of players
     * @param tiles      - the list of tiles
     * @param bonusTiles - the list of bonustiles
     * @return int[] - an array containing extra information about the loaded game
     *         (turn, currentPlayerIndex, currentPlayerThrown)
     * @throws ClassNotFoundException - if the class couldnt be found
     * @throws IOException            - if an io exception happens
     */
    public int[] loadFromFile(List<Player> players, List<Tile> tiles, List<Bonus> bonusTiles)

            throws ClassNotFoundException, IOException {
        String filename = "hf\\gamesave.txt";
        File f = new File(filename);
        if (!f.exists()) {
            throw new IOException("Save file not found at: " + f.getAbsolutePath());
        }
        // storing the information that was saved in the file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
        List<Player> loadedPlayers = (List<Player>) ois.readObject();
        List<Tile> loadedTiles = (List<Tile>) ois.readObject();
        List<Bonus> loadedBonusTiles = (List<Bonus>) ois.readObject();
        int savedTurn = ois.readInt();
        int savedCurrentPlayerIndex = ois.readInt();
        int savedCurrentPlayerThrown = ois.readInt();
        // clear in case any dump data was left
        players.clear();
        players.addAll(loadedPlayers);

        tiles.clear();
        tiles.addAll(loadedTiles);

        bonusTiles.clear();
        bonusTiles.addAll(loadedBonusTiles);
        ois.close();
        // returns int, for the values that are extra information, and aren't in the
        // constructor
        return new int[] { savedTurn, savedCurrentPlayerIndex, savedCurrentPlayerThrown };
    }

    /**
     * Loading the tiles from a file
     * 
     * @param tiles    - the list of tiles
     * @param filename - the name of the file
     * @throws IOException - if an io exception happens
     */
    private void loadTiles(List<Tile> tiles, String filename) throws IOException {
        //making it work on linux and windows as well
        String filePath = filename.replace("\\", File.separator).replace("/", File.separator);
        File file = new File(filePath);
        if (!file.exists()) {
        throw new IOException("File not found: " + filePath);
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        while (true) {
            String line1 = br.readLine();
            if (line1 == null)
                break;
            // storing the parts of the line that store a tile's data
            String[] parts = line1.split(";");
            String name = parts[0];
            int price = Integer.parseInt(parts[1]);
            int houseCost = Integer.parseInt(parts[2]);
            int income = Integer.parseInt(parts[3]);
            int numberOfHouses = Integer.parseInt(parts[4]);
            String tileColor = parts[5];
            int purchasable = Integer.parseInt(parts[6]);
            int housePurchasable = Integer.parseInt(parts[7]);
            tiles.add(
                    new Tile(name, price, houseCost, income, numberOfHouses, tileColor, purchasable, housePurchasable));
        }
        br.close();
    }

    /**
     * Loading the bonusTiles from a file
     * 
     * @param bonusTiles - the list of bonustiles
     * @param filename   - the name of the file
     * @throws IOException - if an io exception happens
     */
    private void loadBonusTiles(List<Bonus> bonusTiles, String filename) throws IOException {
        String filePath = filename.replace("\\", File.separator).replace("/", File.separator);
        File file = new File(filePath);
        if (!file.exists()) {
        throw new IOException("File not found: " + filePath);
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        while (true) {
            String line2 = br.readLine();
            if (line2 == null)
                break;
            // storing the parts of the line that store a bonusTile's data
            String[] parts = line2.split(";");
            String story = parts[0];
            int money = Integer.parseInt(parts[1]);
            int profit = Integer.parseInt(parts[2]);
            bonusTiles.add(new Bonus(story, money, profit));
        }
        br.close();
    }

}
