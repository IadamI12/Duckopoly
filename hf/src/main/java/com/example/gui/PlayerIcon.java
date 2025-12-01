package com.example.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.Icon;

import com.example.logic.Player;
import com.example.logic.Tile;

/**
 * A class to store the icon's corresponding to the players of the game
 * 
 * @author Pap Ádám
 */
public class PlayerIcon implements Icon {
    /**
     * The tile of the icon
     */
    private Tile tile;
    /**
     * The size of the icon
     */
    private int size;

    /**
     * Constructor for the PlayerIcon class
     * 
     * @param tile - the tile of the icon
     * @param size - the size of the icon
     */
    public PlayerIcon(Tile tile, int size) {
        this.tile = tile;
        this.size = size;
    }

    /**
     * Painting the icon of the player onto the buttons as triangles
     * 
     * @param c - the component to be painted on
     * @param g - the graphics object to be used for painting
     * @param x - the x coordinate of the icon
     * @param y - the y coordinate of the icon
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(tile.getTileColor());
            g2.fillRect(x, y, size, size);
            List<Player> players = tile.getPlayers();
            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                Color pieceColor = switch (p.getId()) {
                    case 0 -> Color.CYAN;
                    case 1 -> Color.ORANGE;
                    case 2 -> Color.GREEN;
                    case 3 -> Color.MAGENTA;
                    default -> Color.BLACK;
                };
                g2.setColor(pieceColor);

                Polygon tri = new Polygon();
                int cx, cy;
                int triSize = size / 3;
                switch (i) {
                    case 0 -> {
                        cx = x + triSize;
                        cy = y + triSize;
                    }
                    case 1 -> {
                        cx = x + size - triSize;
                        cy = y + triSize;
                    }
                    case 2 -> {
                        cx = x + triSize;
                        cy = y + size - triSize;
                    }
                    default -> {
                        cx = x + size - triSize;
                        cy = y + size - triSize;
                    }
                }

                tri.addPoint(cx, cy - triSize / 2);
                tri.addPoint(cx - triSize / 2, cy + triSize / 2);
                tri.addPoint(cx + triSize / 2, cy + triSize / 2);

                g2.fill(tri);
                g2.setColor(Color.BLACK);
                g2.draw(tri);
            }
        } finally {
            g2.dispose();
        }
    }

    /**
     * Getter for the width of the icon
     * 
     * @return int - the width of the icon
     */
    @Override
    public int getIconWidth() {
        return size;
    }

    /**
     * Getter for the height of the icon
     * 
     * @return int - the height of the icon
     */
    @Override
    public int getIconHeight() {
        return size;
    }

}
