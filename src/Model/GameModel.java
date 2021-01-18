package Model;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public final class GameModel extends Observable{
    private Level   level;
    private PacMan  pacman;
    protected Ghost ghosts[];
    private Case    game_board[][];
    private final int score;
    private LocalTime start;
    private char fruit;
    
    public GameModel() {
        this.score = 0;
        this.level = new Level();
        this.pacman = new PacMan();
        this.ghosts = new Ghost[4];
        this.start = LocalTime.now();
        initGame();
    }
    
    //Initialises the game
    public void initGame()
    {
        int iPM = level.getiPM(), jPM = level.getjPM();
        int iG  = level.getiG() , jG  = level.getjG();
        this.fruit = level.getFruit1();
        this.pacman.setInitPosition(iPM, jPM);
        this.pacman.initPosition();
        this.pacman.setTimer(250);
        this.pacman.setLives(3);
        initGhosts();
        for(int i = 0 ; i<4 ; i++) {
            this.ghosts[i].setInitPosition(iG, jG);
            this.ghosts[i].initPosition();
            this.ghosts[i].setTimer(255);
        }
        this.game_board = level.initGrid();
        this.pacman.setPacgum(level.getNumPG());
        Entity.setMap(game_board);
        Entity.setScore(score);
        PacMan.setGhosts(ghosts);
        Ghost.setPacMan(pacman);
    }
    
    protected void initGhosts() {
        ghosts[0]= new Pinky(); 
        ghosts[1]= new Blinky(); 
        ghosts[2]= new Inky(); 
        ghosts[3]= new Clyde();
    }
    
    public void start(Observer o) {
        pacman.addObserver(o);
        pacman.start();
        for(int i = 0 ; i<4 ; i++) {
            ghosts[i].addObserver(o);
            ghosts[i].start();
        }
    }
    
    public boolean isHall(int i, int j) {
        return game_board[i][j] instanceof Hall;
    }
    
    public boolean isPacGum(int i, int j) {
        return game_board[i][j] instanceof PacGum;
    }
    
    public boolean isSuperPacGum(int i, int j) {
        return game_board[i][j] instanceof SuperPacGum;
    }
    
    public boolean isWall(int i , int j) {
        return game_board[i][j] instanceof Wall;    
    }

    public boolean isTunnel(int i , int j) {
        return game_board[i][j] instanceof Tunnel;    
    }
    
    public boolean isFruit(int i , int j) {
        return game_board[i][j] instanceof Fruits;    
    }
    
    public int enX(int code) {
        switch(code)
        {
            case 0: return pacman.getX();
            case 1: return ghosts[0].getX();
            case 2: return ghosts[1].getX();
            case 3: return ghosts[2].getX();
            case 4: return ghosts[3].getX();
            default: return 0;
        }
    }
   
    public int enY(int code) {
        switch(code)
        {
            case 0: return pacman.getY();
            case 1: return ghosts[0].getY();
            case 2: return ghosts[1].getY();
            case 3: return ghosts[2].getY();
            case 4: return ghosts[3].getY();
            default: return 0;
        }
    }
    
    public boolean isPacmanInCase(int i, int j) {
        return pacman.getX() == i && pacman.getY() == j;
    }
    
    public boolean isGhostInCase(int code,int i, int j) {
        return ghosts[code].getX() == i && ghosts[code].getY() == j;
    }
    
    
    public void changeDirection(Direction direction) {
        pacman.setDirection(direction);
    }

    public int getScore() {
        return Entity.getScore();
    }

    public int getX() {
        return level.getRows();
    }
    
    public int getY() {
        return level.getColumns();
    }

    public int getLives() {
        return pacman.getLives();
    }

    public char getFruit() {
        return fruit;
    }
    
    public Direction getPacmanDirection() {
        return pacman.getDirection();
    }

    
    public void GameOver() throws IOException {  
    
        level.fillLabyrinth("game_over.txt");
        game_board = level.initGrid();
        BestScore();
    }
    
    private void YouWin() throws IOException {
        level.fillLabyrinth("you_win.txt");
        game_board = level.initGrid();
        BestScore();
    }
    
    public void BestScore() throws IOException {
        ReaderWriter rw = new ReaderWriter();
        int[] bs = rw.readBestScores();
        int score = Entity.getScore();
        
        if( score > bs[2] && score < bs[1]) {
            bs[2] = score;
        }else if( score > bs[1] && score < bs[0]){
            bs[1] = score;
        }else if( score > bs[0]){
            bs[0] = score;
        }
        
        rw.writeBestScore(bs);
    }
    
    public int getBestScore() {
        ReaderWriter rw = new ReaderWriter();
        int[] bs = {0,0,0};
        try {
            bs = rw.readBestScores();
        } catch (IOException ex) {
            Logger.getLogger(GameModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return bs[0];
    }

    public void LevelPassed() throws IOException {
        level.leveUp();
        int level_number = level.getLevel_number();
        if(level_number <= 4)
        {
            level.fillLabyrinth("labyrinth"+level_number+".txt");
            game_board = level.initGrid();
            initGame();
        }
        else
        {
            YouWin();
        }
    }
    
    public boolean collitionWithGhost(int i) {
        return pacman.getX() == ghosts[i].getX() && pacman.getY() == ghosts[i].getY();
    }
    
    public boolean ateAllPacgum() throws IOException {
        if(pacman.getPacGum() == 0)
        {
            LevelPassed();
            return true;
        }
        return false;
    }
    
    
    public boolean pacmanIsAlive() throws InterruptedException, IOException {
        if(pacman.isAlive())
        {   
            FruitTest();
            return true;
        }
        else
        {
            GameOver();
            return false;
        }
    }
    
    public void FruitTest()
    {
        int fruitTime = (int) Duration.between(start,LocalTime.now()).getSeconds();
        int i = level.getiF(), j = level.getjF();

        if(fruitTime == 10) {
            this.game_board[i][j] = new Fruits(level.getFruit(1));
            this.fruit = level.getFruit1();
        }
        if(fruitTime == 20) {
            this.game_board[i][j] = new Fruits(level.getFruit(2));
            this.fruit = level.getFruit2();
        }
    }
    
    public boolean ghostIsBlue(int i) {
        return ghosts[i].isBlue();
    }
    
    public void end()
    {
        pacman.setAlive(false);
        ghosts[0].setAlive(false);
        ghosts[1].setAlive(false);
        ghosts[2].setAlive(false);
        ghosts[3].setAlive(false);
    }

        
    public void sleepEntity(int code) throws InterruptedException
    {
        switch(code)
        {
            case 0: pacman.sleepEntity(level.getTimer());
            case 1: ghosts[0].sleepEntity(level.getTimer());
            case 2: ghosts[0].sleepEntity(level.getTimer());
            case 3: ghosts[0].sleepEntity(level.getTimer());
            case 4: ghosts[0].sleepEntity(level.getTimer());
        }
    }


}