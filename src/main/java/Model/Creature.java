package Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * All the moving and living beasts on the field.
 */

public class Creature {
    private static final int CRYSTALS_NEEDED = 4;

    // Where we live.
    private final World world;
    // Attributes.
    private final List<Stuff> loot = new ArrayList<>();
    private final char glyph;
    private final Color color;
    private final int power;
    private int force;
    private int kyberCrystals;
    // What we do.
    private CreatureAI ai;
    // Coordinates.
    private int x;
    private int y;

    public Creature(World world, char glyph, Color color, int force, int power) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.force = force;
        this.power = power;
    }

    public void setAi(CreatureAI ai) {
        this.ai = ai;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // A lot of getters.
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getGlyph() {
        return glyph;
    }

    public Color getColor() {
        return color;
    }

    public int getForce() {
        return force;
    }

    public int getPower() {
        return power;
    }

    public World getWorld() {
        return world;
    }

    /// Make a step.
    public void moveBy(int mx, int my) {
        Creature inhabitant = world.getCreatureAt(x + mx, y + my);
        if (inhabitant != null) {
            attack(inhabitant);
        } else {
            Stuff item = world.getItemAt(x + mx, y + my);
            if (item != null) {
                pick(item);
            } else {
                ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
            }
        }

    }

    // Active actions.

    /// An action taken when this creature meets someone else.
    public void attack(Creature enemy) {
        ai.onAttack(enemy);
        enemy.attacked();
    }

    /// An action taken when the creature loses all the Force it had.
    public void die() {
        world.remove(this);
        ai.onDeath();
    }

    /**
     * It's time to change something in our boring life.
     * Or not. Let the ai decide.
     */
    public void update() {
        ai.onUpdate();
    }

    /// We found a piece of stuff.
    public void pick(Stuff item) {
        ai.onPick(item);
        loot.add(item);
        kyberCrystals += item.getWinPoints();
    }

    // Passive actions.

    /// Other creature attacks us.
    public void attacked() {
        ai.onAttacked();
    }

    /// The way to lose/gain the Force interacting with the outer world.
    public void modifyForce(int delta) {
        force += delta;
        if (isDead()) {
            die();
        }
    }

    // Checks.

    /// The funny thing is that for all the mobsters (aka troopers) it would return true :3
    public boolean isDead() {
        return force < 0;
    }

    /// Maybe we have already won?
    public boolean isWinner() {
        return kyberCrystals >= CRYSTALS_NEEDED;
    }
}