package Model;

import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Ghost extends Entity{
    
    SecureRandom random = new SecureRandom();
    protected static PacMan pacman;
    private boolean blue;
    
    public Ghost() {
        super();
        blue = false;
    } 
    
    @Override
    public void action() {
        foundPacman();
        if(wallCollition()) // If there will be a collition if ghost moves
            directionChanger();
        else
            move();
    }
    
    
    public void foundPacman()
    {
        boolean canEatG = !pacman.superTimePassed() && this.blue;
        if(pacman.getX() == x && pacman.getY() == y)
        {
            if(canEatG){
                this.killed();
            }else{
                pacman.killed();
            }
        }
    }
    
    public abstract void directionChanger();
    
    public Direction randomDirection(){
        Direction[] directions = Direction.values();
        
        
        int i = random.nextInt(directions.length);
        int counter = 0;
        
        while( !super.changeDirection(directions[i]) && 
            directions[i].getOppositeDirection() == this.direction ) {

            i++;
            counter++;
            if (counter > directions.length)
                return this.direction.getOppositeDirection();

            if (i >= directions.length)
                i = 0;
        }

        return directions[i]; 
    }
    
    public boolean isBlue() {
        return blue;
    }

    public static void setPacMan(PacMan pacman) {
      Ghost.pacman = pacman;
    }

    public void setBlue(boolean Blue) {
        this.blue = Blue;
    }

    public void setOppDirection() {
        this.direction = this.direction.getOppositeDirection();
    }
    
    @Override
    public void killed() {
        try {
            sleepEntity(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(Ghost.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.blue = false;
        initPosition();
        
    }

}