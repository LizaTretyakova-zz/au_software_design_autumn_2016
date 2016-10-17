package Command;

import java.util.List;
import java.util.Objects;

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

    enum CommandType {
        cat,
        echo,
        wc,
        pwd,
        exit
    }
    String execute(List<String> args) throws CommandFailedException;
    CommandType getType();

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

    static boolean needsArg(String src) {
        switch(src) {
            case CAT:
            case ECHO:
            case WC:
                return true;
            case EXIT:
            case PWD:
            default:
                return false;
        }
    }
}
