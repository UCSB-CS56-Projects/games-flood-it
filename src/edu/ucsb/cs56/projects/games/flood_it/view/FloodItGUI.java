package edu.ucsb.cs56.projects.games.flood_it.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/**
 * Class for the Flood it game JFrame
 *
 * @author Daniel Ben-Naim
 * @author Dylan Hanson
 * @author Sophia Mao
 * @author Kai Jann
 * @author Chris Luo
 * @author Kevin Briggs
 * @author Gustav Schoelin
 * @author Karl Wang
 */
public class FloodItGUI extends JFrame {

    //private variables for all the GUI components

    private FloodItController controller;
    private JFrame frame;
    private JPanel textContainer;
    private FloodItGrid gridBoard;
    private FloodItInstructGUI instructions;
    private JTextArea messageArea;
    private JPanel buttonPanel;
    private JTextField countdown;
    private JLabel movesLeft;
    private final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
            Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.BLACK};
    private String[] colorNames = {"Red", "Blue", "Green", "Yellow", "Magenta", "Cyan", "Orange", "Black"};
    private boolean newGame;

    /**
     * FloodItGUI constructor creates an instance of the game
     *
     * @param controller
     */
    public FloodItGUI(FloodItController controller) {
        this.controller = controller;
    }

    /**
     * init initializes the game and draws the board.
     */
    public void init() {
        //set JFrame properties
        frame = new JFrame("Flood It! by SM and KJ and KB and CL and DH and DBN and GS and KW");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        gridBoard = new FloodItGrid(this, controller.getGrid(), colors);
        //set JTextArea properties for the big message returning box
        messageArea = new JTextArea(40, 20);
        messageArea.setEditable(false);
        JScrollPane messageScroller = new JScrollPane(messageArea);
        messageScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        messageScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        //set JTextField properties for countdown box
        countdown = new JTextField(controller.getMovesLeft().toString(), 2);
        countdown.setEditable(false);
        //JLabel for the countdown JTextArea
        movesLeft = new JLabel("Moves left:");
        //Panel that holds the color buttons and countdown
        buttonPanel = new JPanel();
        //add Countdown components to buttonPanel
        buttonPanel.add(movesLeft);
        buttonPanel.add(countdown);
        //ALL button properties
        for (int i = 0; i < controller.getNumColors(); i++) {
            final int k = i;
            JButton currentButton = new JButton(colorNames[i]);
            currentButton.setBackground(colors[i]);
            buttonPanel.add(currentButton);
            currentButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    colorClick(k);
                }
            });
        }
        //add buttonPanel to South component in BorderLayout of JFrame
        frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
        //Container for text and instructions button
        textContainer = new JPanel();
        textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.PAGE_AXIS));
        //add Components to textContainer
        //textContainer.add(messageScroller);
        textContainer.add(messageScroller);
		JPanel buttonContainer = new JPanel();
        textContainer.add(buttonContainer);
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.PAGE_AXIS));
        JButton buttonInstruction = new JButton("Instructions");
        buttonInstruction.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonContainer.add(buttonInstruction);
        buttonInstruction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                instructions = new FloodItInstructGUI();
                messageArea.append("You have clicked the instructions\n");
            }
        });
        JButton newGameButton = new JButton("New Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonContainer.add(newGameButton);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newGame = true;
                frame.setVisible(false);
                frame.dispose();
            }
        });
        JButton resetGameButton = new JButton("Reset Game");
        resetGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonContainer.add(resetGameButton);
        resetGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.reset();
                gridBoard.redrawLabel(controller.getGrid(), colors);
                countdown.setText(controller.getMovesLeft().toString());
                messageArea.append("Game reset\n");
            }
        });
        //add textContainer to JFrame
        frame.getContentPane().add(BorderLayout.EAST, textContainer);
        frame.getContentPane().add(BorderLayout.CENTER, gridBoard);
        frame.setVisible(true);
    }

    /**
     * decrementAMove decrements a move when a user makes a move
     *
     * @return numMoves an integer representing the number of moves
     */
    public Integer decrementAMove() {
        if (controller.getMovesLeft() <= 0) {
            messageArea.append("Out of moves!\n");
            return 0;
        } else {
            int numMoves = controller.getMovesLeft() - 1;
            controller.setMovesLeft(numMoves);
            return numMoves;
        }
    }


    public boolean isNewGame() {
        return newGame;
    }

    public void colorClick(int k){
        if (controller.getMovesLeft() != 0 && !controller.checkWin() && controller.getGrid()[0][0] != k) {
            countdown.setText(decrementAMove().toString());
            messageArea.append(colorNames[k] + "\n");
            controller.floodIt(0, 0, k, controller.getGrid()[0][0]);
            gridBoard.redrawLabel(controller.getGrid(), colors);
            if (controller.checkWin())
                messageArea.append("You Win :D\n");
            else if (controller.getMovesLeft() == 0)
                messageArea.append("You Lose :(\n");
        } else if (controller.getMovesLeft() != 0 && !controller.checkWin())
            messageArea.append("Invalid move\n");
    }

}
