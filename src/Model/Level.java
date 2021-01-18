package Model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

public class Level {

    private int level_number;
    private char[][] labyrinth;
    private int rows, columns, numPG, iPM, jPM, iG, jG, iF, jF, timer;
    private char fruit1, fruit2;
    
    public Level() {
        this.level_number = 1;
        this.numPG = 0;
        try {
            fillLabyrinth("labyrinth"+level_number+".txt");
//            fillLabyrinth("you_win.txt");
        } catch (IOException ex) {
            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public void fillLabyrinth(String filename) throws FileNotFoundException, IOException
    {
        ReaderWriter rw = new ReaderWriter();
        String[] rc_line = rw.readLabyrinth(filename);
        this.labyrinth = rw.getLabyrinth();
        this.rows    = Integer.parseInt(rc_line[0]);
        this.columns = Integer.parseInt(rc_line[1]);
        this.iPM     = Integer.parseInt(rc_line[2]);
        this.jPM     = Integer.parseInt(rc_line[3]);
        this.iG      = Integer.parseInt(rc_line[4]);
        this.jG      = Integer.parseInt(rc_line[5]);
        this.iF      = Integer.parseInt(rc_line[6]);
        this.jF      = Integer.parseInt(rc_line[7]);
        this.fruit1  = rc_line[8].charAt(0);
        this.fruit2  = rc_line[9].charAt(0);
        
    }
    
    
    
    public Case[][] initGrid()
    {
        Case game_board[][] = new Case[rows][columns];
        numPG = 0;
        for(int i = 0 ; i < rows ; i++)
        {
            for(int j = 0 ; j < columns ; j++)
            {
                switch(labyrinth[i][j]){
                    case '-' : game_board[i][j] = new Wall();            break;
                    case '/' : game_board[i][j] = new Hall();            break;
                    case '=' : game_board[i][j] = new Tunnel();          break;
                    case '+' : game_board[i][j] = new PacGum(); numPG++; break;
                    case '*' : game_board[i][j] = new SuperPacGum();     break;
                }
            }
        }

        System.out.println("number of PacGums "+numPG);
        return game_board;
    }

    public int getLevel_number() {
        return level_number;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getNumPG() {
        return numPG;
    }

    public int getiPM() {
        return iPM;
    }

    public int getjPM() {
        return jPM;
    }

    public int getiG() {
        return iG;
    }

    public int getjG() {
        return jG;
    }

    public int getiF() {
        return iF;
    }

    public int getjF() {
        return jF;
    }

    public char getFruit1() {
        return fruit1;
    }

    public char getFruit2() {
        return fruit2;
    }

    public int getTimer() {
        return timer;
    }
    
    public fruits getFruit(int i) {
        char test = (i == 1) ? fruit1 : fruit2;
        switch (test) {
            case 'c' : return fruits.CHERRY;
            case 's' : return fruits.STRAWBERRY;
            case 'o' : return fruits.ORANGE;
            case 'a' : return fruits.APPLE;
            case 'm' : return fruits.MELON;
            case 'g' : return fruits.GALAXIAN;
            case 'b' : return fruits.BELL;
            case 'k' : return fruits.KEY;
            default  : return fruits.CHERRY;
        }
    }
    
    
    public void leveUp() { level_number++; }    
    
}
