package com.example;

import java.util.ArrayList;
import java.util.List;

public class Main {        
        static List <Player> players = new ArrayList<>();
        static List <Tile> tiles = new ArrayList<>();
        static List <Bonus> bonusTiles = new ArrayList<>();
        static List <Dice> dice = new ArrayList<>();
        static  int boardSize;
        static Player currentPlayer;
        static int turn;
        static boolean thrown;

    public static void main(String[] args){
        setup();
        GameLogic game = new GameLogic(players,tiles,bonusTiles,dice,boardSize,currentPlayer,turn);
        Board board = new Board(players,tiles);    
        GameController controller = new GameController(game,board,thrown);
        controller.startGame();
    }
    public static void setup(){
        Setup st = new Setup();
        int[] savedState = null;
        try {
            savedState = st.load(players, tiles, bonusTiles);
        } catch (EndGame e) {
            System.out.println(e.getMessage());
            return;
        }
        int savedThrown = 0;
        if (savedState != null){
          turn = savedState[0];
            currentPlayer = players.get(savedState[1]);
            savedThrown = savedState[2]; 
        }
        else{
            currentPlayer = players.get(0);
        for (Player p : players) {
            p.setPosition(0);
            tiles.get(0).addPlayer(p);
        }
        }
        dice.add(new Dice());
        dice.add(new Dice());
        boardSize = tiles.size();
     if (savedState != null) {
        thrown = savedThrown == 1;     
    }
}
}
