/*
File: ViewGUI2048.java
Authors: Lane Molsbee
Course: CSC 335
Purpose: Implements the GUI application, which will be used to provide a graphical 
interface via which the user can interact with our 2048 game.
 */

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.util.Optional;
import java.awt.*;
import java.awt.event.*;

import javax.sound.sampled.*;
import java.io.*;

public class ViewGUI2048 extends JFrame {
    private Board board;
    private JLabel scoreLabel;
    private Grid gameBoard;
    private Clip musicRunner;
    private static final JLabel WELCOME = new JLabel("2048"); 
    private static final JLabel GAMEOVER = new JLabel("Game Over");
    private JPanel panel, dataPanel, gamePanel; //panel will be the main panel on which everything takes place
    /**
     * This is the class constructor. 
     */
    public ViewGUI2048(){
        setUp();        
    }
    /**
     * This method does the setup work for the GUI. It creates the size of the frame as well as initializes
     * the private instance variables, and completex the functionality to close the window. 
     */
    private void setUp(){  
        this.setSize(800, 800);
        panel = new JPanel();
        scoreLabel = new JLabel("");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dataPanel = new JPanel();
        gamePanel = new JPanel();
        panel.add(dataPanel);
        panel.add(gamePanel);          
        dataPanel.add(WELCOME);   
        dataPanel.add(scoreLabel);    
        this.add(panel);

        this.addWindowFocusListener(new WindowAdapter(){
            public void windowClosing(WindowEvent windowEvent){
                stopMusic();
                System.exit(0);
            }
        });
                  
    }
    /**
     * This method starts up the GUI. It begins by building the game panel using 3 buttons, 
     * which will allow the user to select the size of the grid that they want to use. 
     * This will later be overridden to put the grid, current score, and buttons to
     * do the shifting on the screen. 
     */
    private void start(){
        gamePanel.add(new JLabel("Pick a grid size:"));
        //Set up the button to choose a 4x4 grid
        JButton fourGrid = new JButton("Four by four");
        fourGrid.setActionCommand("four");
        fourGrid.addActionListener(new ButtonClickListener());
        //Set up the button to choose a 6x6 grid
        JButton sixGrid = new JButton("Six by six");
        sixGrid.setActionCommand("six");
        sixGrid.addActionListener(new ButtonClickListener());
        //Set up the button to choose an 8x8 grid. 
        JButton eightGrid = new JButton("Eight by eight");
        eightGrid.setActionCommand("eight");
        eightGrid.addActionListener(new ButtonClickListener());
        gamePanel.add(fourGrid);
        gamePanel.add(sixGrid);
        gamePanel.add(eightGrid);
    }
    /**
     * This private class handles the buttons to choose a size for the game
     * board. It is independent of the functionality to do the shifting,
     * which will be in its own separate private class. 
     */
    private class ButtonClickListener implements ActionListener{
        /**
         * This method determines what actions to perform based on the action command of
         * the ActionEvent input e.
         * @param e is an ActionEvent giving the action event to be used to execute the code
         */
        public void actionPerformed(ActionEvent e){
            String command = e.getActionCommand();
            if(command.equals("four")){
                completeSetupAndRun(BoardSize.FOUR);
            }else if(command.equals("six")){
                completeSetupAndRun(BoardSize.SIX);
            }else{
                completeSetupAndRun(BoardSize.EIGHT);
            }
        }
        /**
         * This  method will handle the final setup tasks and the beginning of running the game. 
         * It will clear the buttons to choose a grid, initialize the board and game grid, place
         * the starting tiles, and run the game
         * @param size is a BoardSize representing the size of the board to be used in the game
         */
        private void completeSetupAndRun(BoardSize size){
            try{
                clear();
                board = new Board(size);
                gameBoard = new Grid(board.getBoard().length);
                board.placeTilesStartGame();
                runGame();
            }catch(Exception e){
                gamePanel.add(new JLabel("Some kind of error occurred"));
            }
        }
            
    }
    /**
     * This class represents the game grid and will draw the grid as the board
     * updates using its current state. 
     */
    private class Grid extends JPanel{
        private int size;
        private final int TILE_SIZE = 60;
        /**
         * This is the class constructor. 
         * @param size is an int giving the size of the board. 
         */
        public Grid(int size){
            this.size = size;
            //This sets the size of the Grid to 60 times the tile size, ensuring that the grid is the correct nxn size
            int gridSize = size * TILE_SIZE;
            //This method will set the dimentions of the grid. 
            this.setPreferredSize(new Dimension(gridSize, gridSize));
        }
        /**
         * This method paints the grid using the values of the tiles in the grid. 
         * @override
         * @param g is a Graphics object that will handle the graphics of the grid. 
         */
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Optional<Tile>[][] currBoard = board.getBoard();
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    String tileValue;
                    //Set the default tile color to gray if there is no tile present in that space in the grid
                    if(currBoard[i][j].isEmpty()){
                        g.setColor(Color.GRAY);
                        tileValue = "";
                    }
                    //Set the tile color based on what is in a non-empty space in the board. 
                    else{
                        Tile currTile = currBoard[i][j].get();
                        /*
                        if(currTile.getTileColor() == TileColor.LIGHT_BLUE){
                            g.setColor(Color.LIGHT_BLUE);
                        }
                        else if(currTile..getTileColor() == TileColor.DARK_BLUE){
                            g.setColor(Color.DARK_BLUE);
                        }
                        else if(currTile.getTileColor() == TileColor.PURPLE){
                            g.setColor(Color.PURPLE);
                        }
                        else{
                            g.setColor(Color.GRAY);
                        }
                            */
                        g.setColor(Color.RED);
                        Integer val = currTile.getValue();
                        tileValue = val.toString();
                    }
                    /*
                     * This next section of code draws the grid. It first creates the rectangles using the pre-defiend tile size,
                     * then sets the color of the border and draws it. It then fills each tile with the value of the tile associated
                     * with that space in the grid. 
                     */
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    FontMetrics metrics = g.getFontMetrics();
                    int textWidth = metrics.stringWidth(tileValue);
                    int textHeight = metrics.getHeight();
                    int x = j * TILE_SIZE + (TILE_SIZE - textWidth) / 2;
                    int y = i * TILE_SIZE + (TILE_SIZE + textHeight) / 2 - metrics.getDescent();
                    g.setColor(Color.BLACK);
                    g.drawString(tileValue, x, y);                        
                }
            }
        }
    }
    /**
     * This class handles the shifting of the board and updates the panels of the game
     * accordingly with each time the board is shifted. 
     */
    private class ShiftHandler implements ActionListener{
        /**
         * This method handles the action performed and uses the action command value
         * of the input action to determine how to manipulate the board. 
         * @param e is an ActionEvent object giving the action that occurred
         */
        public void actionPerformed(ActionEvent e){
            String command = e.getActionCommand();
            //Handle the up shifting
            if(command.equals("shiftUp")){
                int moves = board.shiftUp();
                if(moves > 0){
                    if (board.getBoard().length == 4) {
                        board.placeRandomTile(moves);
                    } else if (board.getBoard().length == 6) {
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                    } else {
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                    }
                }
                scoreLabel.setText("Current Score: " + board.getScore());
                gameBoard.repaint();
                gamePanel.repaint();
                panel.repaint();
            //Handle the left shifting
            }else if(command.equals("shiftLeft")){
                int moves = board.shiftLeft();
                if(moves > 0){
                    if (board.getBoard().length == 4) {
                        board.placeRandomTile(moves);
                    } else if (board.getBoard().length == 6) {
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                    } else {
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                    }
                }
                scoreLabel.setText("Current Score: " + board.getScore());
                gameBoard.repaint();
                gamePanel.repaint();
                panel.repaint();
            //Handle the right shifting
            }else if(command.equals("shiftRight")){
                int moves = board.shiftRight();
                if(moves > 0){
                    if (board.getBoard().length == 4) {
                        board.placeRandomTile(moves);
                    } else if (board.getBoard().length == 6) {
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                    } else {
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                    }
                }
                scoreLabel.setText("Current Score: " + board.getScore());
                gameBoard.repaint();
                gamePanel.repaint();
                panel.repaint();
            //Handle the down shifting
            }else if(command.equals("shiftDown")){
                int moves = board.shiftDown();
                if(moves > 0){
                    if (board.getBoard().length == 4) {
                        board.placeRandomTile(moves);
                    } else if (board.getBoard().length == 6) {
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                    } else {
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                        board.placeRandomTile(moves);
                    }
                }
                scoreLabel.setText("Current Score: " + board.getScore());
                gameBoard.repaint();
                gamePanel.repaint();
                panel.repaint();
            }
        }
    }
    /**
     * This method gets rid of the grid choosing buttons to make way for the rest
     * of the game to run. It removes the game panel, makes a new one, and repaints the 
     * original panel. 
     */
    private void clear(){
        panel.remove(gamePanel);
        gamePanel = new JPanel();                    
        panel.revalidate();
        panel.repaint();
    }
    private void playMusic(String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        try{
            File audioFile = new File(fileName);
            AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);
            musicRunner = AudioSystem.getClip();
            musicRunner.open(stream);
            musicRunner.loop(Clip.LOOP_CONTINUOUSLY);
            musicRunner.start();
        }catch(UnsupportedAudioFileException e){
            gamePanel.add(new JLabel("Doesn't support this audio file type"));
        }catch(IOException e){
            gamePanel.add(new JLabel("ERROR: FILE I/O"));
        }catch(LineUnavailableException e){
            gamePanel.add(new JLabel("Can't play this"));
        }
    }
    private void stopMusic(){
        if(musicRunner.isRunning()){
            musicRunner.stop();
        }
        musicRunner.close();
    }
    /**
     * This method is responsible for running the game. It clears the board, adds the game panel,
     * and sets up the buttons that handle the shifting of the board and the updating of the score. 
     */
    private void runGame() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        try{
            clear(); 
        File audioFile = new File("Midnight Blast - 68bpm.wav");     
        AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);
        musicRunner = AudioSystem.getClip();
        musicRunner.open(stream);
        musicRunner.loop(Clip.LOOP_CONTINUOUSLY);
        musicRunner.start();
        panel.add(gamePanel);        
        gamePanel.add(gameBoard, BorderLayout.CENTER);
        //Set up the button to handle the up shifting
        JButton upButton = new JButton("↑");
        upButton.setActionCommand("shiftUp");
        upButton.addActionListener(new ShiftHandler());
        //Set up the button to handle the left shifting
        JButton leftButton = new JButton("←");
        leftButton.setActionCommand("shiftLeft");
        leftButton.addActionListener(new ShiftHandler());
        //Set up the button to handle the right shifting
        JButton rightButton = new JButton("→");
        rightButton.setActionCommand("shiftRight");
        rightButton.addActionListener(new ShiftHandler());
        //Set up the button to handle the down shifting
        JButton downButton = new JButton("↓");
        downButton.setActionCommand("shiftDown");
        downButton.addActionListener(new ShiftHandler());
        //Add these buttons and display the initial state of the board. 
        gamePanel.add(upButton);
        gamePanel.add(leftButton);
        gamePanel.add(rightButton);
        gamePanel.add(downButton);
        gamePanel.revalidate();
        gamePanel.repaint();
        scoreLabel.setText("Current score: " + board.getScore());
        gameBoard.repaint();
        }catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
            gamePanel.add(new JLabel("There was some sort of error"));
        }
        
        
    }
    /**
     * This method creates the GUI and runs the entire GUI program. 
     * @param args is String[] that is used to start the program. 
     */
    public static void main(String[] args){
        ViewGUI2048 game = new ViewGUI2048();
        game.start();
        game.setVisible(true);
    }
}