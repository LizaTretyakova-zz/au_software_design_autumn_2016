package Model;

import java.awt.*;

/**
 * Created by liza on 25.12.16.
 * <p>
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

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    // Setters.

    public void setY(int y) {
        this.y = y;
    }

    public int getWinPoints() {
        return winPoints;
    }
}
