package Command;

import java.util.List;

public interface Command {
    class CommandFailedException extends Exception {
        public CommandFailedException(String msg) {
            super(msg);
        }
    }

    String CAT = "cat";
    String ECHO = "echo";
    String EXIT = "exit";
    String PWD = "pwd";
    String WC = "wc";
    String PIPE = "|";
    String ASSIGN = "=";

    String execute(List<String> args) throws CommandFailedException;

    static boolean isCommand(String src) {
        switch(src) {
            case CAT:
            case ECHO:
            case EXIT:
            case PWD:
            case WC:
                return true;
            default:
                return false;
        }
    }
}
