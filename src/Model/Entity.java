package Model;

import static java.lang.Thread.sleep;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Entity extends Observable implements Runnable {
    
    protected Direction direction;
    protected int timer;
    protected int x, initx;
    protected int y, inity;
    protected boolean alive;
    protected static Case[][] map;
    protected static int score;
    protected Direction tmp;
    
    public Entity() {
        this.alive = true;
        this.direction = Direction.NORTH;
        //this.timer = 250;
        this.tmp = null;
    }
    
    public void start() {
        new Thread(this).start();
    }
    
    @Override
    public void run()
    {
        while(alive)
        {
            action();
            setChanged();
            notifyObservers();
            try {
                sleep(timer); // 
            } catch (InterruptedException ex) {
                Logger.getLogger(Entity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        setChanged();
//        notifyObservers();
    }

    public abstract void action();
    
    public abstract void killed();
    
    protected boolean isWall(int i , int j) {
        return map[i][j] instanceof Wall;    
    }
    
    protected boolean isPacGum(int i , int j) {
        return map[i][j] instanceof PacGum;    
    }
    
    public boolean isSuperPacGum(int i, int j) {
        return map[i][j] instanceof SuperPacGum;
    }

    public boolean isTunnel(int i , int j) {
        return map[i][j] instanceof Tunnel;    
    }
    
    public boolean wallCollition()
    {
//        if((direction == Direction.NORTH && isWall(x - 1, y)) || // the north case is a wall
//           (direction == Direction.SOUTH && isWall(x + 1, y)) || // the south case is a wall
//           (direction == Direction.EAST  && isWall(x ,y + 1)) || // the east  case is a wall
//           (direction == Direction.WEST  && isWall(x ,y - 1)))   // the west  case is a wall
//            { return true;  }
//        else
//            { return false; }
        
        switch(direction)
        {
            case NORTH: return (map[x - 1][y] instanceof Wall);
            case SOUTH: return (map[x + 1][y] instanceof Wall);
            case EAST : return (map[x][y + 1] instanceof Wall);
            case WEST : return (map[x][y - 1] instanceof Wall);
                
            default : return false;
        }
    }
    
    public void move() 
    {
        correctDirection();
        switch(this.direction)
        {   
            case NORTH : this.x--; break;
            case SOUTH : this.x++; break;
            case EAST  : this.y++; break;
            case WEST  : this.y--; break; 

        }

        if(isTunnel(x,y)) {
            int w = map[0].length;
            this.y = (y == 0) ? w - 2 : 1;
        }
    }

    public void correctDirection() {
        if(this.tmp != null)
        {
            if(changeDirection(tmp))
            {
                this.direction = tmp;
                this.tmp = null;
            }
        }
        
    }    
    
    public boolean changeDirection(Direction dir)
    {       
        switch(dir)
        {
            case NORTH: return !(map[x - 1][y] instanceof Wall);
            case SOUTH: return !(map[x + 1][y] instanceof Wall);
            case EAST : return !(map[x][y + 1] instanceof Wall);
            case WEST : return !(map[x][y - 1] instanceof Wall);
                
            default : return false;
        }
    }
    
    public void initPosition(){
        this.x = this.initx; this.y = this.inity;
    }    
    
    public int getX() { return x; }

    public int getY() { return y; }

    public boolean isAlive() { return alive; }

    public Direction getDirection() { return direction; }
    
    public static int getScore() { return score; }

    public void setInitPosition(int i, int j) { this.initx = i; this.inity = j; }
    
    public static void setMap(Case[][] game_board) { Entity.map = game_board; }

    public static void setScore(int score) { Entity.score = score; }

    public void setTimer(int timer) { this.timer = timer; }

    public void setAlive(boolean alive) { this.alive = alive; }
    
    public void sleepEntity(int time) throws InterruptedException { Thread.currentThread().sleep(time); }

    public void setDirection(Direction direction) {
        if(changeDirection(direction))
            this.direction = direction;
        else
            this.tmp = direction;
    }
    
}
