/*
File: ViewGUI2048.java
Authors: N/A (list all names? just person who wrote it?)
Course: CSC 335
Purpose: Implements the GUI application, which will be used to provide a graphical 
interface via which the user can interact with our 2048 game.
 */

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
    private void clear(){
        frame.remove(gamePanel);
        frame.revalidate();
        frame.repaint();
    }

    private static void runGame(){

    }
    public static void main(String[] args){
        ViewGUI2048 game = new ViewGUI2048();
        game.start();
        game.setVisible(true);

    }

    

}