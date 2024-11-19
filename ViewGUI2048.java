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
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(8000, 8000);      
        panel = new JPanel();
        scoreLabel = new JLabel("");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        gamePanel = new JPanel();
        dataPanel = new JPanel();
        panel.add(dataPanel);
        panel.add(gamePanel);          
        dataPanel.add(welcome);   
        dataPanel.add(scoreLabel);    
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
    }

    private class ButtonClickListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String command = e.getActionCommand();
            if(command.equals("four")){
                clear();
                board = new Board(BoardSize.FOUR);
                runGame();
            }else if(command.equals("six")){
                clear();
                board = new Board(BoardSize.SIX);
                runGame();
            }else{
                clear();
                board = new Board(BoardSize.EIGHT);
                runGame();
            }
        }
    }
    private class Grid extends JPanel{
        private int size;
        private final int TILE_SIZE = 60;
        public Grid(int size){
            this.size = size;
        }
        /**
         * @override
         */
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Optional<Tile>[][] currBoard = board.getBoard();
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
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
        panel.remove(gamePanel);   
        gamePanel = new JPanel(); 
        panel.revalidate();
        panel.repaint();
    }

    private void runGame(){
        clear();
        Grid gameBoard = new Grid(board.getBoard().length);        
        do{       
            panel.add(gamePanel);
            scoreLabel.setText("Current score: " + board.getScore());  
            gameBoard.paintComponent(getGraphics());
            gamePanel.add(gameBoard);
            gamePanel.revalidate();
            gamePanel.repaint();
            panel.revalidate();
            panel.repaint();          
            
        } while(board.isGameOver() != HasWon.LOST);    
    }
    public static void main(String[] args){
        ViewGUI2048 game = new ViewGUI2048();
        game.start();
        game.setVisible(true);

    }

    

}