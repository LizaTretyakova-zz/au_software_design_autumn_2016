package Model;

/**
 * Created by liza on 24.12.16.
 * <p>
 * Produce creatures.
 */

import asciiPanel.AsciiPanel;

public class CreatureFactory {
    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    /// Craft a new player.
    public Creature newPlayer() {
        Creature player = new Creature(world, '@', AsciiPanel.brightWhite, 10, 0);
        world.addTo(0, 0, player);
        new PlayerAI(player);
        return player;
    }

    /// Craft a new trooper.
    public Creature newMobster() {
        Creature mobster = new Creature(world, 'T', AsciiPanel.white, 0, 1);
        world.addAtEmptyLocation(mobster);
        new MobsterAI(mobster);
        return mobster;
    }
}