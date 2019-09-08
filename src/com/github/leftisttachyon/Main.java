package com.github.leftisttachyon;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * The main class for this application
 *
 * @author Jed Wang
 */
public final class Main extends JFrame {

    /**
     * The file where the digits of PI are stored
     */
    public static final File PI_FILE
            = new File("src/com/github/leftisttachyon/resources/pi.dat");

    /**
     * The file where my PB is stored
     */
    public static final File PB_FILE
            = new File("src/com/github/leftisttachyon/resources/pb.dat");

    /**
     * The internal PromptPanel
     */
    private final PromptPanel promptPanel;

    /**
     * The internal DrawPanel
     */
    private final DrawPanel drawPanel;
    
    /**
     * The label at the bottom of this window
     */
    private final JLabel bottomLabel;

    /**
     * The digits of pi
     */
    private final String PI;

    /**
     * The user's PB
     */
    private int pb = getPB();

    /**
     * Creates a new Main window.
     */
    public Main() {
        super("Pi Memorization!");

        String tempPi = null;
        try {
            tempPi = getPi();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            PI = tempPi;
        }

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));

        promptPanel = new PromptPanel("Sans");
        contentPane.add(promptPanel);

        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

        drawPanel = new DrawPanel();
        drawPanel.setDrawing(true);
        contentPane.add(drawPanel);
        
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        
        bottomLabel = new JLabel();
        bottomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        contentPane.add(bottomLabel);

        contentPane.add(Box.createRigidArea(new Dimension(0, 5)));

        pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    exit(pb);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * The main method; the entry point for this application
     *
     * @param args the command line arguments
     * @throws IOException the standard IOException reasons
     * @throws InterruptedException random reasons
     */
    public static void main(String[] args) throws IOException,
            InterruptedException {
        Main main = new Main();
        main.setVisible(true);

        main.startGame();

        /*String pi = getPi();

        int pb = getPB();

        piMemorizationClassic(pi, pb);*/
    }

    /**
     * The old, command line way of memorizing Pi. It's just not very good or
     * pleasing to look at.
     *
     * @param pi the digits of pi, without the decimal point
     * @param pb your pb
     * @throws IOException the standard IOException reasons
     */
    private static void piMemorizationClassic(String pi, int pb)
            throws IOException {
        Scanner input = new Scanner(System.in);

        while (true) {
            Boolean exploring = null;
            int cnt = 0;

            System.out.println("Do you want to explore the digits of pi? (Y/N)");
            while (exploring == null) {
                switch (input.nextLine()) {
                    case "Y":
                    case "y":
                        exploring = true;
                        while (true) {
                            System.out.println("From which digit of pi do you want to start exploring?");
                            try {
                                cnt = input.nextInt();
                                break;
                            } catch (InputMismatchException ime) {
                                System.err.println("Please enter a valid number");
                            }
                        }
                        input.nextLine();
                        break;
                    case "N":
                    case "n":
                        exploring = false;
                        break;
                    default:
                        System.err.println("Please enter a valid value.");
                        break;
                }
            }

            System.out.println("Start listing the digits of pi!");

            for (; cnt < pi.length();) {
                char nextDigit = pi.charAt(cnt);
                String nextInput = input.nextLine();
                if ("EXIT".equalsIgnoreCase(nextInput)) {
                    if (cnt > pb) {
                        System.out.println("Congrats! You beat your previous PB of "
                                + pb + " digits!");
                        pb = cnt;
                    }
                    System.out.println("YOUR PB IS: " + pb + " digits");
                    exit(pb);
                } else if ("STOP".equalsIgnoreCase(nextInput)) {
                    System.out.println("Stopping...");
                    break;
                } else if (nextInput.length() != 1
                        || nextInput.charAt(0) != nextDigit) {
                    System.out.println("Oops, digit " + (cnt + 1)
                            + " of pi is supposed to be " + nextDigit);
                    // System.out.println("You typed " + nextInput);
                    if (!exploring) {
                        break;
                    }
                } else {
                    ++cnt;
                }
            }

            if (exploring) {
                System.out.println("\nCongrats! You explored the first " + cnt
                        + " digits of pi!");
            } else {
                System.out.println("\nCongrats! You memorized the first " + cnt
                        + " digits of pi!");
                if (cnt > pb) {
                    System.out.println("Congrats! You beat your previous PB of "
                            + pb + " digits!");
                    pb = cnt;
                }

                System.out.println("YOUR PB IS: " + pb + " digits");
            }

            System.out.println("Do you want to try again? (Y/N)");
            boolean decision = false;
            while (!decision) {
                switch (input.nextLine()) {
                    case "Y":
                    case "y":
                        decision = true;
                        break;
                    case "N":
                    case "n":
                        exit(pb);
                        break;
                    default:
                        System.err.println("Please enter a valid value.");
                        break;
                }
            }
        }
    }

    /**
     * Starts the game
     *
     * @throws InterruptedException the standard reasons
     * @throws IOException the standard IOException reasons
     */
    private void startGame() throws InterruptedException, IOException {
        while (true) {
            boolean exploring;
            int cnt = 0;

            promptPanel.setVisible(true);
            promptPanel.setQuestion("Do you want to explore the digits of pi?");
            if (exploring = promptPanel.getNextClick()) {
                while (true) {
                    String s = JOptionPane.showInputDialog(this,
                            "From which digit of pi do you want to start exploring?",
                            "Explore?",
                            JOptionPane.PLAIN_MESSAGE);
                    try {
                        cnt = Integer.parseInt(s) - 1;
                        if (cnt < 0 || cnt >= PI.length()) {
                            JOptionPane.showMessageDialog(this,
                                    "Please enter a valid number", "Invalid input",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            break;
                        }
                    } catch (InputMismatchException | NumberFormatException e) {
                        JOptionPane.showMessageDialog(this,
                                "Please enter a valid number", "Invalid input",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            promptPanel.setVisible(false);

            for (; cnt < PI.length();) {
                char nextDigit = PI.charAt(cnt);
                int next = drawPanel.getNextPress();
                if (next == KeyEvent.VK_ESCAPE) {
                    if (cnt > pb) {
                        JOptionPane.showMessageDialog(this,
                                "Congrats! You beat your previous PB of "
                                + pb + " digits! Your PB is now "
                                + (pb = cnt) + "digits",
                                "New PB!", JOptionPane.INFORMATION_MESSAGE);
                    }
                    exit(pb);
                } else if (next == KeyEvent.VK_PAUSE) {
                    break;
                } else if (next != nextDigit) {
                    JOptionPane.showMessageDialog(this, "Oops, digit " + (cnt + 1)
                            + " of pi is supposed to be " + nextDigit,
                            "Oops!", JOptionPane.WARNING_MESSAGE);
                    // System.out.println("You typed " + nextInput);
                    if (!exploring) {
                        break;
                    }
                } else {
                    ++cnt;
                }
            }

            if (exploring) {
                JOptionPane.showMessageDialog(this,
                        "Congrats! You explored the first " + cnt
                        + " digits of pi!",
                        "Congrats!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Congrats! You memorized the first " + cnt
                        + " digits of pi!",
                        "Congrats!", JOptionPane.INFORMATION_MESSAGE);
                if (cnt > pb) {
                    JOptionPane.showMessageDialog(this,
                            "Wow! You beat your previous PB of "
                            + pb + " digits! Your new PB is " + (pb = cnt) + " digits",
                            "New PB!", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            promptPanel.setVisible(true);
            promptPanel.setQuestion("Do you want to try again?");
            if (!promptPanel.getNextClick()) {
                exit(pb);
            }
            promptPanel.setVisible(false);
        }
    }

    /**
     * Gets the digits of Pi from the PI file
     *
     * @return the digits of Pi, extracted from the Pi file
     * @throws IOException the standard IOException rules
     */
    public static String getPi() throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader piIn = new BufferedReader(new FileReader(PI_FILE))) {
            String line;
            while ((line = piIn.readLine()) != null) {
                builder.append(line);
            }
        }

        return builder.toString();
    }

    /**
     * Gets your PB from the PB file
     *
     * @return your PB, extracted from the PB file
     */
    public static int getPB() {
        try (BufferedReader pbIn = new BufferedReader(new FileReader(PB_FILE))) {
            return Integer.parseInt(pbIn.readLine());
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Called when the user intends to exit the program.
     *
     * @param pb the current PB
     * @throws IOException the standard IOException reasons
     */
    private static void exit(int pb) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(PB_FILE,
                false))) {
            out.println(pb);
        }

        System.exit(0);
    }
}
