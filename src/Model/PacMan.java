package Model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacMan extends Entity {

    private boolean powered;
    private LocalTime startBlue;
    private int poweredDuration, pacgum, lives;
    private static Ghost[] ghosts;
    
    
    public PacMan() {
        super();
        powered = false;
        startBlue = LocalTime.now();
    }

    @Override
    public void action() {
        if(!wallCollition()) {
            eats();
            move();
        }
    }

    private void eats()
    {
        if(isSuperPacGum(x,y)) {
            if( !powered ) { timer -= 100 ; }
            powered = true;
            startBlue = LocalTime.now();
            poweredDuration = ((Hall)map[x][y]).getBlueGhostDuration();
            setGhostsBlue(true);
        }
        if(isPacGum(x,y)) { 
            pacgum--;
        }
        score += ((Hall)map[x][y]).getPoints();
        map[x][y] = new Hall();
        
    }
    
    public int getLives() {
        return lives;
    }
    
    
    public boolean superTimePassed()
    {
        int duration = (int) Duration.between(startBlue,LocalTime.now()).getSeconds();
        if( powered && duration >= poweredDuration){
            timer += 100;
            powered = false;
            setGhostsBlue(false);
            return true;
        }
        return false;
    }

    public void setGhostsBlue(boolean b)
    {
        for (int i = 0  ; i <4 ; i++)
            ghosts[i].setBlue(b);
    }
    
    public boolean isPowered() {
        return powered;
    }

    public int getPacGum() {
        return pacgum;
    }

    public static void setGhosts(Ghost[] ghosts) {
        PacMan.ghosts = ghosts;
    }

    public void setPowered(boolean b) {
        this.powered = b;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setPacgum(int pacgum) {
        this.pacgum = pacgum;
    }
    
    @Override
    public void killed() {
        this.lives--;
        initPosition();
        if(lives == 0 ) {
            this.alive = false;
            for (int i =  0; i< 4 ; i++)
                ghosts[i].setAlive(false);
        }
        else
        {
            try {
                sleepAll();
            } catch (InterruptedException ex) {
                Logger.getLogger(PacMan.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public void sleepAll() throws InterruptedException
    {
        int time = 500;
        for (int i =  0; i< 4 ; i++) {
            ghosts[i].initPosition();
        }
        for (int i =  0; i< 4 ; i++) {
            ghosts[i].sleepEntity(time);
        }        
        sleepEntity(time);
        
    }

}
