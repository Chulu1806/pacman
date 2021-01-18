
package Model;


public class Blinky extends Ghost {

    public Blinky() {
        super();
        this.direction = Direction.WEST;
    }

    @Override
    public void directionChanger() {
        Vec2 pacPos = new Vec2(Ghost.pacman.getX(), Ghost.pacman.getY());
        PathFinder pathFinder = new PathFinder();
        setDirection(pathFinder.findDirection(new Vec2(x, y), pacPos));
    }

  

}
