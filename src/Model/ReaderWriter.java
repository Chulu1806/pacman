package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.shape.Shape;

public class ReaderWriter {

    //String URL = "C:\\Users\\Michupi\\Documents\\Francia\\LYON 1\\POO\\pac-man\\PacMan\\src\\";
    String URL = "/home/michelle/Desktop/POO/TPs/pac-man/PacMan/src/";
    private char[][] labyrinth;
    
    public ReaderWriter() { }

    public String[] readLabyrinth(String filename) throws FileNotFoundException, IOException
    {
        String[] rc_line = null;
        String url = System.getProperty("user.dir").concat("/src/"+filename);

        try (BufferedReader br = new BufferedReader( new FileReader(url))) {

            String st;
            st = br.readLine();
            rc_line = st.trim().split(" ");
            int rows = Integer.parseInt(rc_line[0]);
            int columns = Integer.parseInt(rc_line[1]);
            int i = 0;
            labyrinth = new char[rows][columns];
            while((st = br.readLine()) != null){
                for(int j=0 ; j<columns ; j++){
                    labyrinth[i][j] = st.charAt(j);
                }
                i++;
            }


        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return rc_line;
    }
        
    public int[] readBestScores() throws FileNotFoundException, IOException
    {

        String url = System.getProperty("user.dir").concat("/src/bestScores.txt");
        File file = new File(url);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        st = br.readLine();
        String[] rc_line = st.trim().split(" ");
        int[] bestScores = new int[3];
        bestScores[0] = Integer.parseInt(rc_line[0]);
        bestScores[1] = Integer.parseInt(rc_line[1]);
        bestScores[2] = Integer.parseInt(rc_line[2]);
        
        return bestScores;
    }
    
    public void writeBestScore(int[] scores) throws IOException {
        String url = System.getProperty("user.dir").concat("/src/bestScores.txt");
        FileWriter writer = new FileWriter(url);
        BufferedWriter bw = new BufferedWriter(writer);
        String s = "";
        for(int i = 0 ; i < 3 ; i++)
            s += scores[i] + " ";
        System.out.println("    "+s);
        bw.write(s);
        bw.close();
    }
    
    public char[][] getLabyrinth() {
        return labyrinth;
    }

}
