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
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;

public class ViewGUI2048 extends JFrame {
    private Board board;
    private JLabel scoreLabel;
    private InputMap inputs;
    private ActionMap actions;
    private Grid gameBoard;
    private static final JLabel welcome = new JLabel("2048"); 
    private JPanel panel, dataPanel, gamePanel; //panel will be the main panel on which everything takes place

    public ViewGUI2048(){
        setUp();        
    }

    private void setUp(){  
        this.setSize(800, 800);
        panel = new JPanel();
        scoreLabel = new JLabel("");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dataPanel = new JPanel();
        gamePanel = new JPanel();
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
        inputs = new InputMap();
        actions = new ActionMap();
        inputs.put(KeyStroke.getKeyStroke("LEFT"), "shiftLeft");
        inputs.put(KeyStroke.getKeyStroke("RIGHT"), "shiftRight");
        inputs.put(KeyStroke.getKeyStroke("UP"), "shiftUp");
        inputs.put(KeyStroke.getKeyStroke("DOWN"), "shiftDown");
        actions.put("shiftLeft", new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                board.shiftLeft();
                gameBoard.repaint();
                gamePanel.repaint();
                panel.repaint();
                repaint();
            }
        });
        actions.put("shiftRight", new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                board.shiftRight();
                gameBoard.repaint();
                gamePanel.repaint();
                panel.repaint();
                repaint();
            }
        });
        actions.put("shiftUp", new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                board.shiftUp();
                gameBoard.repaint();
                gamePanel.repaint();
                panel.repaint();
                repaint();
            }
        });
        actions.put("shiftDown", new AbstractAction(){
            public void actionPerformed(ActionEvent e){
                board.shiftDown();
                gameBoard.repaint();
                gamePanel.repaint();
                panel.repaint();
                repaint();
            }
        });
        setFocusable(true);
        requestFocusInWindow();
        
          
    }
    private void start(){
        gamePanel.add(new JLabel("Pick a gride size:"));
        JButton fourGrid = new JButton("Four by four");
        fourGrid.setActionCommand("four");
        fourGrid.addActionListener(new ButtonClickListener());
        JButton sixGrid = new JButton("Six by six");
        sixGrid.setActionCommand("six");
        sixGrid.addActionListener(new ButtonClickListener());
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
                gameBoard = new Grid(board.getBoard().length);
                board.placeTilesStartGame();
                runGame();
            }else if(command.equals("six")){
                clear();
                board = new Board(BoardSize.SIX);
                gameBoard = new Grid(board.getBoard().length);
                board.placeTilesStartGame();
                runGame();
            }else{
                clear();
                board = new Board(BoardSize.EIGHT);
                gameBoard = new Grid(board.getBoard().length);
                board.placeTilesStartGame();
                runGame();
            }
        }
    }
    private class Grid extends JPanel{
        private int size;
        private final int TILE_SIZE = 60;
        public Grid(int size){
            this.size = size;
            int gridSize = size * TILE_SIZE;
            this.setPreferredSize(new Dimension(gridSize, gridSize));
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
        panel.add(gamePanel);        
        gamePanel.add(gameBoard, BorderLayout.CENTER);
        gamePanel.revalidate();
        gamePanel.repaint();
        scoreLabel.setText("Current score: " + board.getScore());
        gameBoard.repaint();
        
        
    }
    public static void main(String[] args){
        ViewGUI2048 game = new ViewGUI2048();
        game.start();
        game.setVisible(true);

    }

    

}