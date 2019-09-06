package com.github.leftisttachyon;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A JPanel that prompts the user to choose yes or no.
 *
 * @author Jed Wang
 */
public final class PromptPanel extends JPanel {

    /**
     * The button that says "yes" on it
     */
    private JButton yesButton;

    /**
     * The button that says "no" on it
     */
    private JButton noButton;

    /**
     * The upper label
     */
    private JLabel label;

    /**
     * The last clicked value
     */
    private boolean lastClicked = false;

    /**
     * Creates a new PromptPanel
     *
     * @param question the question to prompt the user
     */
    public PromptPanel(String question) {
        super();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        label = new JLabel(question);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 12);
        yesButton = new JButton("Yes");
        yesButton.setFont(buttonFont);
        yesButton.setPreferredSize(new Dimension(75, 50));
        yesButton.addActionListener(this::yesClicked);

        noButton = new JButton("No");
        noButton.setFont(buttonFont);
        noButton.setPreferredSize(new Dimension(75, 50));
        noButton.addActionListener(this::noClicked);

        add(Box.createRigidArea(new Dimension(0, 5)));

        Box labelPanel = Box.createHorizontalBox();
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.add(label);
        labelPanel.add(Box.createHorizontalGlue());
        add(labelPanel);

        add(Box.createRigidArea(new Dimension(0, 10)));

        Box buttonPanel = Box.createHorizontalBox();
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(yesButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(noButton);
        buttonPanel.add(Box.createHorizontalGlue());

        add(buttonPanel);
        add(Box.createRigidArea(new Dimension(0, 5)));
    }

    /**
     * Called when the yes button is clicked
     *
     * @param ae the ActionEvent generated
     */
    private void yesClicked(ActionEvent ae) {
        synchronized (this) {
            lastClicked = true;
            notifyAll();
        }
    }

    /**
     * Called when the no button is clicked
     *
     * @param ae the ActionEvent generated
     */
    private void noClicked(ActionEvent ae) {
        synchronized (this) {
            lastClicked = false;
            notifyAll();
        }
    }

    /**
     * Waits and returns the value of the next click.
     *
     * @return the next value of the button clicked
     * @throws InterruptedException if the wait() method is interrupted
     */
    public boolean getNextClick() throws InterruptedException {
        synchronized (this) {
            wait();
        }

        return lastClicked;
    }

    /**
     * Sets the text inside the upper label.
     *
     * @param question the text to display at the top
     */
    public void setQuestion(String question) {
        label.setText(question);
    }
}
