package com.github.leftisttachyon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The main class for this application
 *
 * @author Jed Wang
 */
public class Main {

    /**
     * The main method; the entry point for this application
     *
     * @param args the command line arguments
     * @throws IOException the standard IOException reasons
     */
    public static void main(String[] args) throws IOException {
        File piFile = new File("src/com/github/leftisttachyon/resources/pi.dat");
        StringBuilder builder = new StringBuilder();
        try (BufferedReader piIn = new BufferedReader(new FileReader(piFile))) {
            String line;
            while ((line = piIn.readLine()) != null) {
                builder.append(line);
            }
        }

        String pi = builder.toString();

        int pb;
        File pbFile = new File("src/com/github/leftisttachyon/resources/pb.dat");
        try (BufferedReader pbIn = new BufferedReader(new FileReader(pbFile))) {
            pb = Integer.parseInt(pbIn.readLine());
        } catch (NumberFormatException nfe) {
            pb = 0;
        }

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
