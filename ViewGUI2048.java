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

/*
 * For game over:
 * If they won, put two buttons asking if they want to continue. If htey say yes,
 * just get rid of the extra stuff and continue the game as normal. If they say no,
 * close the program. 
 * If they lost, just put a message saying the game is over and what their final score was. 
 */
public class ViewGUI2048 extends JFrame {
    private Board board;
    private JLabel scoreLabel;
    private Grid gameBoard;
    private Clip musicRunner;
    private AudioInputStream stream;
    private static final JLabel WELCOME = new JLabel("<html>2048<br></html>");
    private static final JLabel GAMEOVER = new JLabel("<html>Game Over<br></html>");
    private JPanel panel, dataPanel, gamePanel, gameOverPanel; // panel will be the main panel on which everything takes
                                                               // place

    // Flag used to tell whether the player has already won (used to tell when win conditions should
    // be checked)
    private static boolean hasPlayerWon = false;

    // Flag used to tell whether the game should wait before allowing further player moves
    private static boolean waitForDecision = false;
    /**
     * This is the class constructor.
     */
    public ViewGUI2048() {
        setUp();
    }

    /**
     * This method does the setup work for the GUI. It creates the size of the frame
     * as well as initializes
     * the private instance variables, and completex the functionality to close the
     * window.
     */
    private void setUp() {
        this.setSize(1000, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        scoreLabel = new JLabel("");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dataPanel = new JPanel();
        gamePanel = new JPanel();
        gameOverPanel = new JPanel();
        panel.add(dataPanel);
        panel.add(gamePanel);
        dataPanel.add(WELCOME);
        dataPanel.add(scoreLabel);
        this.add(panel);

        this.addWindowFocusListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                musicRunner.stop();
                musicRunner.close();
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });

    }

    /**
     * This method starts up the GUI. It begins by building the game panel using 3
     * buttons,
     * which will allow the user to select the size of the grid that they want to
     * use.
     * This will later be overridden to put the grid, current score, and buttons to
     * do the shifting on the screen.
     */
    private void start() {
        gamePanel.add(new JLabel("Pick a grid size:"));
        // Set up the button to choose a 4x4 grid
        JButton fourGrid = new JButton("Four by four");
        fourGrid.setActionCommand("four");
        fourGrid.addActionListener(new ButtonClickListener());
        // Set up the button to choose a 6x6 grid
        JButton sixGrid = new JButton("Six by six");
        sixGrid.setActionCommand("six");
        sixGrid.addActionListener(new ButtonClickListener());
        // Set up the button to choose an 8x8 grid.
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
    private class ButtonClickListener implements ActionListener {
        /**
         * This method determines what actions to perform based on the action command of
         * the ActionEvent input e.
         * 
         * @param e is an ActionEvent giving the action event to be used to execute the
         *          code
         */
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("four")) {
                completeSetupAndRun(BoardSize.FOUR);
            } else if (command.equals("six")) {
                completeSetupAndRun(BoardSize.SIX);
            } else {
                completeSetupAndRun(BoardSize.EIGHT);
            }
        }

        /**
         * This method will handle the final setup tasks and the beginning of running
         * the game.
         * It will clear the buttons to choose a grid, initialize the board and game
         * grid, place
         * the starting tiles, and run the game
         * 
         * @param size is a BoardSize representing the size of the board to be used in
         *             the game
         */
        private void completeSetupAndRun(BoardSize size) {
            try {
                clear();
                board = new Board(size);
                gameBoard = new Grid(board.getBoard().length);
                board.placeTilesStartGame();
                runGame();
            } catch (Exception e) {
                gamePanel.add(new JLabel("Some kind of error occurred"));
            }
        }

    }

    /**
     * This class represents the game grid and will draw the grid as the board
     * updates using its current state.
     */
    private class Grid extends JPanel {
        private int size;
        private final int TILE_SIZE = 60;

        /**
         * This is the class constructor.
         * 
         * @param size is an int giving the size of the board.
         */
        public Grid(int size) {
            this.size = size;
            // This sets the size of the Grid to 60 times the tile size, ensuring that the
            // grid is the correct nxn size
            int gridSize = size * TILE_SIZE;
            // This method will set the dimentions of the grid.
            this.setPreferredSize(new Dimension(gridSize, gridSize));
        }

        /**
         * This method paints the grid using the values of the tiles in the grid.
         * 
         * @override
         * @param g is a Graphics object that will handle the graphics of the grid.
         */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Optional<Tile>[][] currBoard = board.getBoard();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    String tileValue;
                    // Set the default tile color to gray if there is no tile present in that space
                    // in the grid
                    if (currBoard[i][j].isEmpty()) {
                        g.setColor(Color.GRAY);
                        tileValue = "";
                    }
                    // Set the tile color based on what is in a non-empty space in the board.
                    else {
                        Tile currTile = currBoard[i][j].get();

                        if (currTile.getTileColor() == TileColor.CYAN) {
                            g.setColor(Color.CYAN);
                        } else if (currTile.getTileColor() == TileColor.GREEN) {
                            g.setColor(Color.GREEN);
                        } else if (currTile.getTileColor() == TileColor.BLUE) {
                            g.setColor(Color.BLUE);
                        } else {
                            g.setColor(Color.YELLOW);
                        }
                        Integer val = currTile.getValue();
                        tileValue = val.toString();
                    }
                    /*
                     * This next section of code draws the grid. It first creates the rectangles
                     * using the pre-defiend tile size,
                     * then sets the color of the border and draws it. It then fills each tile with
                     * the value of the tile associated
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
                    g.setFont(new Font("default", Font.BOLD, 16));
                    g.drawString(tileValue, x, y);
                }
            }
        }
    }

    /**
     * This class handles the shifting of the board and updates the panels of the
     * game
     * accordingly with each time the board is shifted.
     */
    private class ShiftHandler implements ActionListener {
        /**
         * This method handles the action performed and uses the action command value
         * of the input action to determine how to manipulate the board.
         * 
         * @param e is an ActionEvent object giving the action that occurred
         */
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            // Handle the up shifting
            if (command.equals("shiftUp") && waitForDecision == false) {
                int moves = board.shiftUp();
                if (moves > 0) {
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
                // Handle the left shifting
            } else if (command.equals("shiftLeft") && waitForDecision == false) {
                int moves = board.shiftLeft();
                if (moves > 0) {
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
                // Handle the right shifting
            } else if (command.equals("shiftRight") && waitForDecision == false) {
                int moves = board.shiftRight();
                if (moves > 0) {
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
                // Handle the down shifting
            } else if (command.equals("shiftDown") && waitForDecision == false) {
                int moves = board.shiftDown();
                if (moves > 0) {
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

            // Checks to see whether the game is over
            if (board.isGameOver() == HasWon.LOST) {
                panel.add(gameOverPanel);
                gameOverPanel.add(GAMEOVER);
                gameOverPanel.add(new JLabel("Final Score:  " + board.getScore()));
                gameOverPanel.repaint();
                panel.add(gameOverPanel);
            }
            if (board.isGameOver() == HasWon.WON && hasPlayerWon == false) {
                hasPlayerWon = true;
                waitForDecision = true;
                panel.add(gameOverPanel);
                gameOverPanel.add(new JLabel("<html>Congratulations, you won!<br>Current score: " + board.getScore() + "<br>Would you like to continue?</html>"));
                JButton continueGame = new JButton("Yes");
                continueGame.setActionCommand("continue");
                continueGame.addActionListener(new ContinueHandler());
                gameOverPanel.add(continueGame);
                JButton endGame = new JButton("No");
                endGame.setActionCommand("endGame");
                endGame.addActionListener(new ContinueHandler());
                gameOverPanel.add(endGame);
                gameOverPanel.repaint();
                panel.add(gameOverPanel);
            }
        }
    }

    private class ContinueHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("continue")) {
                panel.remove(gameOverPanel);
                gameOverPanel = new JPanel();
                panel.repaint();
                gamePanel.repaint();
                waitForDecision = false;
            } else {
                musicRunner.stop();
                musicRunner.close();
                System.exit(0);
            }
        }

    }

    /**
     * This method gets rid of the grid choosing buttons to make way for the rest
     * of the game to run. It removes the game panel, makes a new one, and repaints
     * the
     * original panel.
     */
    private void clear() {
        panel.remove(gamePanel);
        gamePanel = new JPanel();
        panel.revalidate();
        panel.repaint();
    }

    private void runGame() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        try {
            clear();
            // The following code sets up the background music.
            File audioFile = new File("Midnight Blast - 68bpm.wav");
            stream = AudioSystem.getAudioInputStream(audioFile);
            musicRunner = AudioSystem.getClip();
            musicRunner.open(stream);
            musicRunner.loop(Clip.LOOP_CONTINUOUSLY);
            musicRunner.start();
            panel.add(gamePanel);
            gamePanel.add(gameBoard, BorderLayout.CENTER);

            // Used to ensure that the win conditions work
            // board.setTile(0, 1, 1024);
            // board.setTile(0, 0, 1024);

            // Set up the button to handle the up shifting
            JButton upButton = new JButton("↑");
            upButton.setActionCommand("shiftUp");
            upButton.addActionListener(new ShiftHandler());
            // Set up the button to handle the left shifting
            JButton leftButton = new JButton("←");
            leftButton.setActionCommand("shiftLeft");
            leftButton.addActionListener(new ShiftHandler());
            // Set up the button to handle the right shifting
            JButton rightButton = new JButton("→");
            rightButton.setActionCommand("shiftRight");
            rightButton.addActionListener(new ShiftHandler());
            // Set up the button to handle the down shifting
            JButton downButton = new JButton("↓");
            downButton.setActionCommand("shiftDown");
            downButton.addActionListener(new ShiftHandler());
            // Add these buttons and display the initial state of the board.
            gamePanel.add(upButton);
            gamePanel.add(leftButton);
            gamePanel.add(rightButton);
            gamePanel.add(downButton);
            gamePanel.revalidate();
            gamePanel.repaint();
            scoreLabel.setText("Current score: " + board.getScore());
            gameBoard.repaint();
            gamePanel.revalidate();
            gamePanel.repaint();
            panel.revalidate();
            panel.repaint();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            gamePanel.add(new JLabel("There was some sort of error"));
        }

    }

    /**
     * This method creates the GUI and runs the entire GUI program.
     * 
     * @param args is String[] that is used to start the program.
     */
    public static void main(String[] args) {
        ViewGUI2048 game = new ViewGUI2048();
        game.start();
        game.setVisible(true);
    }
}