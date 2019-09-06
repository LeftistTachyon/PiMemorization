package com.github.leftisttachyon;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

/**
 * A JPanel which I draw in
 *
 * @author Jed Wang
 */
public final class DrawPanel extends JPanel {

    /**
     * A service
     */
    private ScheduledExecutorService service = null;

    /**
     * Creates a new DrawPanel.
     */
    public DrawPanel() {
        super();

        setPreferredSize(new Dimension(500, 500));
    }

    /**
     * Sets whether this panel is drawing or not
     *
     * @param isDrawing whether this is drawing or not
     */
    public void setDrawing(boolean isDrawing) {
        if (isDrawing) {
            service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(this::repaint,
                    0, 150, TimeUnit.MILLISECONDS);
        } else {
            service.shutdown();
            service = null;
        }
    }

    @Override
    public void paint(Graphics g) {
        try {
            super.paint(g);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The height of CHAR_FONT (?)
     */
    private static final int CHAR_FONT_HEIGHT = 60;

    /**
     * The font to use for drawing characters
     */
    private static final Font CHAR_FONT = new Font("Segoe UI", CHAR_FONT_HEIGHT, Font.PLAIN);

    /**
     * A character that I can draw
     */
    private class DrawableCharacter {

        /**
         * The character to draw
         */
        private final char character;

        /**
         * The coordinates of the character
         */
        private int x, y;

        /**
         * x and y velocities
         */
        private final int dx, dy;

        /**
         * The amount of opacity
         */
        private int opacity;

        /**
         * Creates a new DrawableCharacter given the parameters.
         *
         * @param c the character to draw
         * @param x the x-coordinate to start at
         * @param y the y-coordinate to start at
         */
        public DrawableCharacter(char c, int x, int y) {
            character = c;
            this.x = x;
            this.y = y;

            dx = (int) (Math.random() * 4 - 2);
            dy = (int) (Math.random() * 4 - 2);

            opacity = 255;
        }

        /**
         * Creates a new DrawableCharacter at the center of the screen.
         *
         * @param c the character to draw
         */
        public DrawableCharacter(char c) {
            this(c, getWidth() / 2, getHeight() / 2);
        }

        /**
         * Paints this object
         *
         * @param g2D the Graphics2D object to use
         */
        public void paint(Graphics2D g2D) {
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
            
        }
    }
}
