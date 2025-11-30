package com.example;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.Icon;
//A class to store the icon's corresponding to the players of the game
public class PlayerIcon implements Icon{
    private Tile tile;
    private int size;

    public PlayerIcon(Tile tile,int size){
        this.tile = tile;
        this.size = size;
    }
    //Painting the icon of the player onto the butotns as triangles
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
                    case 0 -> { cx = x+ triSize;cy = y+ triSize; }           
                    case 1 -> { cx = x+ size-triSize; cy = y +triSize; }           
                    case 2 -> { cx = x+triSize;cy = y + size- triSize; }    
                    default -> { cx = x+size- triSize;cy = y +size - triSize; }   
                }

                tri.addPoint(cx,cy - triSize / 2);
                tri.addPoint(cx - triSize/2,cy + triSize / 2);
                tri.addPoint(cx + triSize/2,cy + triSize / 2);

                g2.fill(tri);
                g2.setColor(Color.BLACK);
                g2.draw(tri);
            }
        } finally {
            g2.dispose();
        }
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }
    
}
