package ViewController;

import Model.Direction;
import Model.GameModel;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
//import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Game extends Application {

  
    int X, Y;                               
    GameModel game = new GameModel();
    ImageView[][] game_grid;       
    ImageView[] hearts = { new ImageView(new Image("Heart.png")),
                           new ImageView(new Image("Heart.png")),
                           new ImageView(new Image("Heart.png"))};
    
    Image imPM = new Image("Pacman.png");
    Image[] imG = { new Image("Pinky.png"),
                    new Image("Blinky.png"),
                    new Image("Inky.png"),
                    new Image("Clyde.png")};
    Image imH = new Image("Vide.png");
    Image imW = new Image("Wall.png");
    Image imPG   = new Image("PacGum.png");
    Image imSPG  = new Image("SuperPacGum.png");
    Image imF = new Image("Heart.png");
    Image imBlue = new Image("BlueGhost.png");
    Text highscore = new Text();
    Text score = new Text();
    Text lives = new Text();

    GridPane grid = new GridPane();
    BorderPane root;
    Pane control;
    
    @Override
    public void start(Stage primaryStage) {
        double w = imPM.getWidth(), h = imPM.getHeight();
        initGameScenario();
        
        Scene scene = new Scene(root, Y * w, (X * h) + (h * 5));

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        Observer o = new Observer() { 
            @Override
            public void update(Observable o, Object arg) {
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        score.setText("SCORE : "+game.getScore());
                        
                        switch(game.getLives())
                        {
                            case 2 : hearts[2].setImage(imH); break;
                            case 1 : hearts[1].setImage(imH); break;
                            case 0 : hearts[0].setImage(imH); break;

                        }
                        try {
                            if(game.pacmanIsAlive())
                            {
                                if(game.ateAllPacgum())
                                    initGameGrid();
                                showUpdatedGrid();
                                showPacman();
                                showGhosts();
                            }
                            else
                            {
                                System.out.println("GAME OVER");
                                initGameGrid();
                                showUpdatedGrid();
                            }
                        } catch (InterruptedException | IOException ex) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        };

        game.start(o);
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override            
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        game.changeDirection(Direction.NORTH);
                        break;
                    case DOWN:
                        game.changeDirection(Direction.SOUTH);
                        break;
                    case LEFT:
                        game.changeDirection(Direction.WEST);
                        break;
                    case RIGHT:
                        game.changeDirection(Direction.EAST);
                        break;
                }

            }
        });


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                game.end();
                Platform.exit();
                System.exit(1);
            }
        });
        
    }
    
    public void showUpdatedGrid()
    {
        for(int i = 0 ; i < X ; i++ ) {
            for(int j = 0 ; j < Y ; j++ ) {
                if(game.isHall(i, j)) {
                    game_grid[i][j].setImage(imH);
                    if(game.isPacGum(i, j)) {
                        game_grid[i][j].setImage(imPG);
                    }
                    if(game.isSuperPacGum(i, j)) {
                        game_grid[i][j].setImage(imSPG);
                    }
                    if(game.isFruit(i,j)) {
                        //switchFruit();
                        game_grid[i][j].setImage(imF);
                    }
                }                
            }
        }
    }

    public void switchFruit() {
        switch(game.getFruit()) {
            case 'c' : imF = new Image("cherry.png");     break;
            case 's' : imF = new Image("strawberry.png"); break;
            case 'o' : imF = new Image("orange.png");     break;
            case 'a' : imF = new Image("apple.png");      break;
            case 'm' : imF = new Image("melon.png");      break;
            case 'g' : imF = new Image("galaxian.png");   break;
            case 'b' : imF = new Image("bell.png");       break;
            case 'k' : imF = new Image("key.png");        break;
        }
    }
    
    
    public void showPacman() 
    {
        game_grid[game.enX(0)][game.enY(0)].setImage(imPM);
    }
    
    public void showGhosts()
    {   
        int x = 0, y = 0;
        for(int i = 0 ; i < 4 ; i++) {
            x = game.enX(i+1);
            y = game.enY(i+1);
            if(game.ghostIsBlue(i)) {
                game_grid[x][y].setImage(imBlue);
            }
            else
            {
                switch (i) {
                    case 0: game_grid[x][y].setImage(imG[0]); break;
                    case 1: game_grid[x][y].setImage(imG[1]); break;
                    case 2: game_grid[x][y].setImage(imG[2]); break;
                    case 3: game_grid[x][y].setImage(imG[3]); break;    
                }
            }
        }
    }
    
    public void initGameGrid() {
        X = game.getX();
        Y = game.getY();
        game_grid = new ImageView[X][Y];
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
              
                if (game.isPacGum(i, j)) {
                    game_grid[i][j] = new ImageView(imPG);

                } else if (game.isSuperPacGum(i, j)) {
                    game_grid[i][j] = new ImageView(imSPG);

                } else if (game.isWall(i, j)) {
                    game_grid[i][j] = new ImageView(imW);

                } else if (game.isTunnel(i, j)) {
                    game_grid[i][j] = new ImageView(imH);

                } else if (game.isHall(i, j)) {
                    game_grid[i][j] = new ImageView(imH);

                }
                
                grid.add(game_grid[i][j], j, i);
            }
        }
        grid.setAlignment(Pos.CENTER);
        root.setCenter(grid);
    }
    
    public void initGameScores() {
        double w = imPM.getWidth(), h = imPM.getHeight();
        root = new BorderPane();
        
        Font f = Font.loadFont(Game.class.getResource("arcade.TTF").toExternalForm(), h*0.7);
        
        highscore.setLayoutX(w);
        highscore.setLayoutY(h + (h*0.7));
        highscore.setText("HIGHSCORE : "+ game.getBestScore());
        highscore.setFont(f);
        highscore.setFill(Color.WHITE);
        
        score.setLayoutX(w);
        score.setLayoutY((3*h) + (h*0.7));
        score.setFont(f);
        score.setFill(Color.WHITE);
        
        
        lives.setText("LIVES ");
        lives.setLayoutX(13.5 * w);
        lives.setLayoutY(h + (h*0.7));
        lives.setFont(f);
        lives.setFill(Color.WHITE);
        
        hearts[0].setLayoutX(17*w);
        hearts[0].setLayoutY(h);
        hearts[1].setLayoutX(18*w);
        hearts[1].setLayoutY(h);
        hearts[2].setLayoutX(19*w);
        hearts[2].setLayoutY(h);
        
        control = new Pane();
        
        control.getChildren().addAll(highscore,score,lives,hearts[0], hearts[1], hearts[2]);
        
        root.setTop(control);
        
    }
    
    public void initGameScenario() {
        initGameScores();
        initGameGrid();
        root.setStyle("-fx-background-color: #000000;");
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
/*
    int x = game.enX(0), y = game.enY(0);
    Rotate rotate = new Rotate();
    switch(game.getPacmanDirection()) {
        case NORTH : rotate.setAngle(90); break;
        case SOUTH : rotate.setAngle(270); break;
        case EAST  : rotate.setAngle(0); break;
        case WEST  : rotate.setAngle(180); break;
    }

    game_grid[x][y].getTransforms().add(rotate);

*/