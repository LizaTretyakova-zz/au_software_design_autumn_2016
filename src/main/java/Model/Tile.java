package Model;

import asciiPanel.AsciiPanel;

import java.awt.*;

/**
 * A cell of the field.
 */
public enum Tile {
    FLOOR((char) 250, AsciiPanel.white, 0, 0), // ground
    WALL((char) 219, AsciiPanel.cyan, 1, 0), // wall
    BOUNDS('x', AsciiPanel.brightBlack, Integer.MAX_VALUE, 0), // out of the world
    MIDI_CHLORIAN((char) 7, AsciiPanel.brightWhite, 0, 1); // a healing ground

    private char glyph;
    private int sharpness;
    private int force;
    private Color color;

    Tile(char glyph, Color color, int sharpness, int force) {
        this.glyph = glyph;
        this.color = color;
        this.sharpness = sharpness;
        this.force = force;
    }

    // Getters.

    public char getGlyph() {
        return glyph;
    }

    public Color getColor() {
        return color;
    }

    public int getSharpness() {
        return sharpness;
    }

    public int getForce() {
        return force;
    }

    // Checks.

    public boolean isDiggable() {
        return this == WALL;
    }

    public boolean isGround() {
        return this == FLOOR || this == MIDI_CHLORIAN;
    }

    public boolean isFree() {
        return this == FLOOR || this == WALL;
    }

    public boolean isInteractive() {
        return this == MIDI_CHLORIAN;
    }
}
