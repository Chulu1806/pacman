package Model;

public final class Pinky extends Ghost{


    public Pinky() {
        super();
        this.direction = Direction.EAST;
    }


    
    @Override
    public void directionChanger() {
        setDirection(randomDirection());
    }

            
   
}
