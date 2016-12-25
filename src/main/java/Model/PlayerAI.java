package Model;

/**
 * Created by liza on 23.12.16.
 * <p>
 * The player.
 */
public class PlayerAI extends CreatureAI {
    public PlayerAI(Creature creature) {
        super(creature);
    }

    /// Destroy monsters, step into their cell, lose some Force.
    @Override
    public void onAttack(Creature enemy) {
        creature.modifyForce(-enemy.getPower());
        enemy.die();
        creature.setX(enemy.getX());
        creature.setY(enemy.getY());
    }

    /// Checks if we have enough Force. If we can die performing this action, refuse.
    @Override
    public boolean canDig(Tile tile) {
        return creature.getForce() - tile.getSharpness() > 0;
    }

    /// Lose some Force, turn a piece of wall into a piece of ground.
    @Override
    public void onDig(Tile tile) {
        creature.modifyForce(-tile.getSharpness());
    }

    /// Grab an item and step into its cell.
    @Override
    public void onPick(Stuff item) {
        creature.getWorld().remove(item);
        creature.setX(item.getX());
        creature.setY(item.getY());
    }
}
