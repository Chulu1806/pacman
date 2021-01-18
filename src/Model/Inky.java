
package Model;


public class Inky extends Ghost {

    public Inky() {
        super();
        this.direction = Direction.NORTH;
    }

    @Override
    public void directionChanger() {
        setDirection(randomDirection());
    }

}
