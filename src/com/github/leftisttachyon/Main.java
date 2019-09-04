package com.github.leftisttachyon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * The main class for this application
 *
 * @author Jed Wang
 */
public class Main {

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
     * The main method; the entry point for this application
     *
     * @param args the command line arguments
     * @throws IOException the standard IOException reasons
     */
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.add(new PromptPanel("sdfsdf"));
        frame.pack();

        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
        File pbFile = new File("src/com/github/leftisttachyon/resources/pb.dat");
        try (PrintWriter out = new PrintWriter(new FileOutputStream(pbFile,
                false))) {
            out.println(pb);
        }

        System.exit(0);
    }
}
