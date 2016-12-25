package Model;

import java.awt.*;

/**
 * Useful things on the field. Some of them can let you win.
 */
public class Stuff {

    private char glyph;
    private Color color;
    private int x;
    private int y;
    private int winPoints;

    Stuff(char glyph, Color color, int winPoints) {
        this.glyph = glyph;
        this.color = color;
        this.winPoints = winPoints;
    }

    // Getters.

    public char getGlyph() {
        return glyph;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWinPoints() {
        return winPoints;
    }

    // Setters.

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
