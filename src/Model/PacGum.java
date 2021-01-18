package Model;

public class PacGum extends Hall{
    
    public PacGum(){
        super();
        this.points = 10;
        this.blueGhostDuration = 0;
          
    }
    
  @Override  
    public int getPoints()
    {
         return this.points; 
    }
    
}
