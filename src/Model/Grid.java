package Model;


public class Grid {
    
    private static int  number_gum;
    protected final Case grid[][];
    private static int width;
    private static int length;
    
    public Grid(int x, int y, int n){
        Grid.number_gum= n;
        Grid.width=x;
        Grid.length=y;
       this.grid = new Case[x][y];
        
    }
    
     
    
}
