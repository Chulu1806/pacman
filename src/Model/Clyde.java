
package Model;


public class Clyde extends Ghost {

    public Clyde() {
        super();
        this.direction = Direction.SOUTH;
    }

    @Override
    public void directionChanger() {
        setDirection(randomDirection());
    }




}
