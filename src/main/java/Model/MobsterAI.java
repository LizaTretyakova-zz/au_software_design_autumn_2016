package Model;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The trooper.
 */
public class MobsterAI extends CreatureAI {

    private static final Logger LOGGER = Logger.getLogger(MobsterAI.class.getName());

    public MobsterAI(Creature creature) {
        super(creature);
    }

    /// Move somewhere, just not to stand as a frozen Han Solo.
    @Override
    public void onUpdate() {
        int dx = ThreadLocalRandom.current().nextInt(-1, 1 + 1);
        int dy = ThreadLocalRandom.current().nextInt(-1, 1 + 1);
        LOGGER.log(Level.INFO, "move a mobster for dx {0} dy {1}\n", new Object[]{dx, dy});
        creature.moveBy(dx, dy);
    }
}
