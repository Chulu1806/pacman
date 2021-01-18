package Model;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class manages to give te sortest path between to case in the grid
 * 
 * @author sarah
 */
public class PathFinder {

    Case[][] grille = null;



    class Vec2Pair {
        public Vec2 prec, cur;

        public Vec2Pair(Vec2 prec, Vec2 cur) {
            this.prec = prec;
            this.cur = cur;
        }
    }

    protected boolean valide(int x, int y) {
        // the case surely in one of border and doesn't have a neigbhor
        if (x < 0 || y < 0 || x >= grille.length || y >= grille[0].length)
            return false;

        return !(grille[x][y] instanceof Wall);
    }

    private void emplierUnVoisin(LinkedList<Vec2> queue,Vec2 cur, Vec2 prem, ArrayList<Vec2Pair> results) {
        if (valide(cur.x, cur.y)) {
            grille[cur.x][cur.y] = new Wall();
            results.add(new Vec2Pair(prem, cur));
            queue.add(cur);
        }
    }


    private void emplierLesVoisin(Vec2 prem, LinkedList<Vec2> queue, ArrayList<Vec2Pair> resultats) {

        Vec2 current = new Vec2(prem.x + 1, prem.y);
        emplierUnVoisin(queue, current, prem, resultats);

        current = new Vec2(prem.x, prem.y + 1);
        emplierUnVoisin(queue, current, prem, resultats);

        current = new Vec2(prem.x - 1, prem.y);
        emplierUnVoisin(queue, current, prem, resultats);

        current = new Vec2(prem.x, prem.y -1);
        emplierUnVoisin(queue, current, prem, resultats);

    }

    protected int getIndex(ArrayList<Vec2Pair> results, Vec2 head) {

        for (int i = 0; i < results.size(); i++)
            if (results.get(i).cur.equals(head))
                return i;

        return -1;
    }

    protected Vec2 trouverChemin(ArrayList<Vec2Pair> results, Vec2 prem) {

        int index = getIndex(results, prem);

        Vec2Pair result = results.get(index);

        for (int i = index - 1; i >= 0; i--)
            if (result.prec.equals(results.get(i).cur))
                result = results.get(i);


        return result.cur;
    }

    protected Direction getDirectionFromPosition(Vec2 origin, Vec2 to) {

        Vec2 current = new Vec2(origin.x + 1, origin.y);
        if (to.equals(current))
            return Direction.EAST;

        current = new Vec2(origin.x, origin.y + 1);
        if (to.equals(current))
            return Direction.NORTH;

        current = new Vec2(origin.x - 1, origin.y);
        if (to.equals(current))
            return Direction.WEST;

        return Direction.WEST;

    }


    public Direction findDirection(Vec2 from, Vec2 to) {
        this.grille = (new Level()).initGrid();

        LinkedList<Vec2> queue = new LinkedList<Vec2>();
        ArrayList<Vec2Pair> resultats = new ArrayList<Vec2Pair>();

        queue.add(from);

        while (!queue.isEmpty()) {

            Vec2 prem = queue.pollFirst();

            if (prem.equals(to)) break;

            emplierLesVoisin(prem, queue, resultats);

        }

        return getDirectionFromPosition(from, trouverChemin(resultats, to));

    }


    
    
}
