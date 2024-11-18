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

import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;

import java.awt.*;
import java.awt.event.*;

public class ViewGUI2048 extends JFrame {
    private Board board;
    private JFrame frame;
    
    private JLabel scoreLabel;
    private static final JLabel welcome = new JLabel("2048"); 
    private JPanel panel, dataPanel, gamePanel; //panel will be the main panel on which everything takes place

    public ViewGUI2048(){
        setUp();        
    }

    private void setUp(){
        gamePanel = new JPanel();
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(8000, 8000);
        dataPanel = new JPanel();
        dataPanel.add(welcome);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(dataPanel);
        this.add(panel);

        this.addWindowFocusListener(new WindowAdapter(){
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
          
    }
    private void start(){
        gamePanel.add(new JLabel("Pick a gride size:"));
        JButton fourGrid = new JButton("Four by four");
        fourGrid.setActionCommand("four");
        fourGrid.addActionListener(new ButtonClickListener());
        JButton sixGrid = new JButton("Six by six");
        fourGrid.setActionCommand("six");
        fourGrid.addActionListener(new ButtonClickListener());
        JButton eightGrid = new JButton("Eight by eight");
        eightGrid.setActionCommand("eight");
        eightGrid.addActionListener(new ButtonClickListener());
        gamePanel.add(fourGrid);
        gamePanel.add(sixGrid);
        gamePanel.add(eightGrid);
        panel.add(gamePanel);

    }

    private class ButtonClickListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String command = e.getActionCommand();
            if(e.equals("four")){
                clear();
                board = new Board(BoardSize.FOUR);
                scoreLabel = new JLabel("Current score: " + board.getScore());
                dataPanel.add(scoreLabel);
                frame.add(dataPanel);   
                runGame();
            }else if(e.equals("six")){
                clear();
                board = new Board(BoardSize.SIX);
                scoreLabel = new JLabel("Current score: " + board.getScore());
                dataPanel.add(scoreLabel);
                frame.add(dataPanel);   
                runGame();
            }else{
                clear();
                board = new Board(BoardSize.EIGHT);
                scoreLabel = new JLabel("Current score: " + board.getScore());
                dataPanel.add(scoreLabel);
                frame.add(dataPanel);  
                runGame();
            }
        }
    }
    private class Grid extends JPanel{
        private int size;
        private final int TILE_SIZE = 10;
        public Grid(BoardSize size){
            this.size = 4 + 2 * size.ordinal();
        }
        /**
         * @override
         */
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Optional<Tile>[][] currBoard = board.getBoard();
            for(int i = 0; i < currBoard.length; i++){
                for(int j = 0; j < currBoard.length; j++){
                    String tileValue;
                    if(currBoard[i][j].isEmpty()){
                        g.setColor(Color.GRAY);
                        tileValue = "";
                    }
                    
                    else{
                        g.setColor(Color.RED);
                        Integer val = currBoard[i][j].get().getValue();
                        tileValue = val.toString();
                    }
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
    
    private void clear(){
        frame.remove(gamePanel);
        frame.revalidate();
        frame.repaint();
    }

    private void runGame(){
        do{
        } while(board.isGameOver() != HasWon.LOST);
    }
    private void drawCurrGrid(Graphics g){
        super.paintComponents(g);
        int width = board.getBoard().length * 40;
        int height  = board.getBoard().length * 40;
        int cellWidth = width / 40;
        int cellHeight = height / 40;
        g.setColor(Color.GRAY);
        for(int i  = 0; i <= board.getBoard().length; i++){
            //Draw vertical lines
            g.drawLine(i * cellWidth, 0, 9 * cellWidth, height);
            //Draw horizontal lines
            g.drawLine(0, i*cellHeight, width, i * cellHeight);

        }


    }
    public static void main(String[] args){
        ViewGUI2048 game = new ViewGUI2048();
        game.start();
        game.setVisible(true);

    }

    

}