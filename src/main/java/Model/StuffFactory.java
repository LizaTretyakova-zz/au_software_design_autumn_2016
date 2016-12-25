package Model;

import asciiPanel.AsciiPanel;

import java.awt.*;

/**
 * Crystal mine :3
 */
public class StuffFactory {
    private World world;

    public StuffFactory(World world) {
        this.world = world;
    }

    public Stuff newKyberCrystalRed() {
        return newKyberCrystal(AsciiPanel.brightRed);
    }

    public Stuff newKyberCrystalBlue() {
        return newKyberCrystal(AsciiPanel.brightBlue);
    }

    public Stuff newKyberCrystalGreen() {
        return newKyberCrystal(AsciiPanel.brightGreen);
    }

    public Stuff newKyberCrystalYellow() {
        return newKyberCrystal(AsciiPanel.brightYellow);
    }

    /// Produce a crystal of a given colour.
    private Stuff newKyberCrystal(Color color) {
        Stuff kyberCrystal = new Stuff((char) 4, color, 1);
        world.addItemToRandomLocation(kyberCrystal);
        return kyberCrystal;
    }


}
