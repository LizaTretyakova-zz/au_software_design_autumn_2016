package Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Galaxy. We'll try to rule it :)
 */
public class World {
    private final List<Creature> creatures = new ArrayList<>();
    private final List<Stuff> items = new ArrayList<>();
    // Land and inhabitants.
    private Tile[][] tiles;
    // Parameters.
    private int width;
    private int height;

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
    }

    // A bunch of getters.

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public List<Stuff> getItems() {
        return items;
    }

    // A bit different getters.

    public char getGlyphAt(int x, int y) {
        return tile(x, y).getGlyph();
    }

    public Color getColorAt(int x, int y) {
        return tile(x, y).getColor();
    }

    /// A getter for a specific tile.
    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return Tile.BOUNDS;
        else
            return tiles[x][y];
    }

    /// Make a specified cell ground.
    public void ground(int x, int y) {
        tiles[x][y] = Tile.FLOOR;
    }

    /// Put a creature to a specified cell.
    public void addTo(int x, int y, Creature creature) {
        creature.setX(x);
        creature.setY(y);
        creatures.add(creature);
    }

    /// Put a creature to an arbitrary empty cell.
    public void addAtEmptyLocation(Creature creature) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
        }
        while (!tile(x, y).isGround() || getCreatureAt(x, y) != null);

        addTo(x, y, creature);
    }

    /// Add an item to a completely random cell.
    public void addItemToRandomLocation(Stuff item) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
        }
        while (!tile(x, y).isGround() || getCreatureAt(x, y) != null);

        item.setX(x);
        item.setY(y);
        items.add(item);
    }

    /// Returns the creature in the specified cell if there is any.
    public Creature getCreatureAt(int x, int y) {
        for (Creature c : creatures) {
            if (c.getX() == x && c.getY() == y) {
                return c;
            }
        }
        return null;
    }

    /// Returns the item in the specified cell if there is any.
    public Stuff getItemAt(int x, int y) {
        for (Stuff i : items) {
            if (i.getX() == x && i.getY() == y) {
                return i;
            }
        }
        return null;
    }

    /// Removes the creature from the specified cell if there is any.
    public void remove(Creature inhabitant) {
        creatures.remove(inhabitant);
    }

    /// Removes the item from the specified cell if there is any.
    public void remove(Stuff item) {
        items.remove(item);
    }

    /// The Galaxy lives.
    public void update() {
        for (Creature creature : creatures) {
            creature.update();
        }
    }
}
