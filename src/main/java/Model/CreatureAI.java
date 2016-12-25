package Model;

/**
 * Rules the creature.
 */
public abstract class CreatureAI {
    protected Creature creature;

    public CreatureAI(Creature creature) {
        this.creature = creature;
        this.creature.setAi(this);
    }

    /// When we stepped onto the new ground tile.
    public void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
            if (tile.isInteractive()) {
                creature.modifyForce(tile.getForce());
                creature.getWorld().ground(x, y);
            }
        } else if (tile.isDiggable() && canDig(tile)) {
            onDig(tile);
            creature.setX(x);
            creature.setY(y);
            creature.getWorld().ground(x, y);
        }
    }

    /// Checks whether we have enough resources or not.
    public boolean canDig(Tile tile) {
        return false;
    }

    /// Commit digging.
    public void onDig(Tile tile) {
    }

    /// Commit attack.
    public void onAttack(Creature enemy) {
    }

    /// Mb undertake something. Or not.
    public void onAttacked() {
    }

    /// All the necessary actions.
    public void onDeath() {
    }

    /// Change something.
    public void onUpdate() {
    }

    /// We found a piece of stuff.
    public void onPick(Stuff item) {
    }
}