package com.github.leftisttachyon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

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
     * A list of characters
     */
    private ArrayList<DrawableCharacter> characters = null;

    /**
     * Stores the last key pressed.
     */
    private int lastKeyPressed = -1;

    /**
     * Creates a new DrawPanel.
     */
    public DrawPanel() {
        super();

        setPreferredSize(new Dimension(500, 500));

        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inMap = getInputMap(condition);
        ActionMap actMap = getActionMap();

        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
        actMap.put("Escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastKeyPressed = KeyEvent.VK_ESCAPE;
                synchronized (DrawPanel.this) {
                    DrawPanel.this.notifyAll();
                }
            }
        });
        
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAUSE, 0), "Pause");
        actMap.put("Pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastKeyPressed = KeyEvent.VK_PAUSE;
                synchronized (DrawPanel.this) {
                    DrawPanel.this.notifyAll();
                }
            }
        });

        for (char c = '0'; c <= '9'; c++) {
            String s = String.valueOf(c);
            inMap.put(KeyStroke.getKeyStroke(c), s);

            final char cc = c;
            actMap.put(s, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    characters.add(new DrawableCharacter(cc));
                    lastKeyPressed = cc;
                    synchronized (DrawPanel.this) {
                        DrawPanel.this.notifyAll();
                    }
                }
            });
        }
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
                    0, 15, TimeUnit.MILLISECONDS);

            characters = new ArrayList<>();
        } else {
            service.shutdown();
            service = null;

            characters = null;
        }
    }

    @Override
    public void paint(Graphics g) {
        try {
            if (characters != null) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());

                Graphics2D g2D = (Graphics2D) g;

                for (int i = characters.size() - 1; i >= 0; i--) {
                    DrawableCharacter dChar = characters.get(i);
                    dChar.paint(g2D);

                    if (!dChar.isDrawn()) {
                        characters.remove(i);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the next key press that is listened to
     *
     * @return the next key press that is listened to
     * @throws InterruptedException if the wait() method is interrupted
     */
    public int getNextPress() throws InterruptedException {
        synchronized (this) {
            this.wait();
        }

        return lastKeyPressed;
    }

    /**
     * The font to use for drawing characters
     */
    private static final Font CHAR_FONT = new Font("Segoe UI", Font.PLAIN, 60);

    /**
     * A character that I can draw
     */
    private class DrawableCharacter {

        /**
         * The character to draw
         */
        private final String character;

        /**
         * The coordinates of the character
         */
        private float x, y;

        /**
         * x and y velocities
         */
        private final float dx, dy;

        /**
         * The amount of opacity
         */
        private float opacity;

        /**
         * Creates a new DrawableCharacter given the parameters.
         *
         * @param c the character to draw
         * @param x the x-coordinate to start at
         * @param y the y-coordinate to start at
         */
        public DrawableCharacter(char c, int x, int y) {
            character = String.valueOf(c);
            this.x = x;
            this.y = y;

            dx = (float) (Math.random() * 4 - 2);
            dy = (float) (Math.random() * 4 - 2);

            opacity = 1.0f;
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
            if (opacity <= 0) {
                return;
            }

            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setFont(CHAR_FONT);

            Color color = new Color(0, 0, 0, opacity);
            g2D.setColor(color);

            FontMetrics metrics = g2D.getFontMetrics();
            int height = metrics.getHeight(),
                    width = metrics.stringWidth(character);

            g2D.drawString(character, x - width / 2, y + height / 4);

            // update
            x += dx;
            y += dy;

            opacity -= 0.008f;
        }

        /**
         * Determines whether the character is still being drawn
         *
         * @return whether the character is still being drawn
         */
        public boolean isDrawn() {
            return opacity > 0;
        }

        @Override
        public String toString() {
            return "Drawable " + character + " x=" + x + " y=" + y;
        }
    }
}
