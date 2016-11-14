package Command;

import java.util.List;

/**
 * An interface all the commands should implement.
 */
public interface Command {
    /**
     *
     * @param args the list of arguments for the given command
     * @return the output to be printed to console (if needed)
     * @throws CommandFailedException
     */
    String execute(List<String> args) throws CommandFailedException;

    class CommandFailedException extends Exception {
        public CommandFailedException(String msg) {
            super(msg);
        }
    }
}
