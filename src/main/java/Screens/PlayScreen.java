package Screens;

import Model.*;
import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * The game happens here.
 */
public class PlayScreen implements Screen {
    private static final int SCREEN_WIDTH = 80;
    private static final int SCREEN_HEIGHT = 21;
    private static final int WORLD_WIDTH = 90;
    private static final int WORLD_HEIGHT = 32;

    private World world;
    private Creature player;
    private int screenWidth;
    private int screenHeight;

    public PlayScreen() {
        // Set screen width and height.
        screenWidth = SCREEN_WIDTH;
        screenHeight = SCREEN_HEIGHT;

        // Set and populate the world.
        createWorld();
        createCreatures(new CreatureFactory(world)); // can be easily mocked
        createItems(new StuffFactory(world));
    }

    private void createWorld() {
        world = new WorldBuilder(WORLD_WIDTH, WORLD_HEIGHT)
                .makeCaves()
                .makeMidiChlorians()
                .build();
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        // Create the player.
        player = creatureFactory.newPlayer();
        //  Create some mobs.
        for (int i = 0; i < 8; i++) {
            creatureFactory.newMobster();
        }
    }

    private void createItems(StuffFactory stuffFactory) {
        stuffFactory.newKyberCrystalBlue();
        stuffFactory.newKyberCrystalGreen();
        stuffFactory.newKyberCrystalRed();
        stuffFactory.newKyberCrystalYellow();
    }

    private int getScrollX() {
        return Math.max(0, Math.min(player.getX() - screenWidth / 2, world.getWidth() - screenWidth));
    }

    private int getScrollY() {
        return Math.max(0, Math.min(player.getY() - screenHeight / 2, world.getHeight() - screenHeight));
    }

    /// Show the current situation to the user.
    @Override
    public void displayOutput(AsciiPanel terminal) {

        int left = getScrollX();
        int top = getScrollY();

        displayTiles(terminal, left, top);

        terminal.write(player.getGlyph(), player.getX() - left, player.getY() - top);
        terminal.write("Force: " + Integer.toString(player.getForce()), 1, 23);
        terminal.writeCenter("-- collect all kyber crystals to win --", 22);
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;

                terminal.write(world.getGlyphAt(wx, wy), x, y, world.getColorAt(wx, wy));
            }
        }

        world.getCreatures().stream().filter(c -> c.getX() > left && c.getX() < left + screenWidth
                && c.getY() > top && c.getY() < top + screenHeight).forEach(c -> {
            terminal.write(c.getGlyph(), c.getX() - left, c.getY() - top, c.getColor());
        });

        world.getItems().stream().filter(i -> i.getX() > left && i.getX() < left + screenWidth
                && i.getY() > top && i.getY() < top + screenHeight).forEach(i -> {
            terminal.write(i.getGlyph(), i.getX() - left, i.getY() - top, i.getColor());
        });
    }

    /**
     * Some standard moves: with arrows or 'hjkl'-style.
     * 'Escape' to end the game and lose.
     * <p>
     * Also checks if the game has finished and returns a right screen to be shown to the user.
     *
     * @param key
     * @return
     */
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                return new LoseScreen();
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_H:
                player.moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_L:
                player.moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_K:
                player.moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_J:
                player.moveBy(0, 1);
                break;
            case KeyEvent.VK_Y:
                player.moveBy(-1, -1);
                break;
            case KeyEvent.VK_U:
                player.moveBy(1, -1);
                break;
            case KeyEvent.VK_B:
                player.moveBy(-1, 1);
                break;
            case KeyEvent.VK_N:
                player.moveBy(1, 1);
                break;
        }

        world.update();

        Screen result = this;
        if (player.isDead()) {
            result = new LoseScreen();
        } else if (player.isWinner()) {
            result = new WinScreen();
        }
        return result;
    }
}
