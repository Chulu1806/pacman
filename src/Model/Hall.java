package Model;


public class Hall extends Case{ 
    
    protected int points;
    protected int blueGhostDuration; // in seconds
        
    public Hall(){
        super();
        this.points = 0;
        this.blueGhostDuration = 0;
    }
        
    public int getPoints()
    {
        return this.points;
    } 

    public int getBlueGhostDuration() {
        return blueGhostDuration;
    }
    
}
