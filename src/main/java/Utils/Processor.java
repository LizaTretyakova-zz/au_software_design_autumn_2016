package Utils;

import Command.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Gets the parsed string and executes it.
 */
public class Processor {
    private static final HashMap<String, String> environment = new HashMap<>();

    private static final String CAT = "cat";
    private static final String ECHO = "echo";
    private static final String EXIT = "exit";
    private static final String PWD = "pwd";
    private static final String WC = "wc";
    private static final String PIPE = "|";
    private static final String ASSIGN = "=";
    private static Processor instance = null;
    private static Parser parser = Parser.getInstance();
    private Processor() {
    }

    private static boolean isCommand(String src) {
        switch (src) {
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

    /**
     * The singleton implementation method.
     *
     * @return the instance of the processor
     */
    public static Processor getInstance() {
        if (instance == null) {
            instance = new Processor();
        }
        return instance;
    }

    private String processUnit(List<String> input) throws IOException, Command.CommandFailedException {
        if (isCommand(input.get(0))) {
            switch (input.get(0)) {
                case CAT:
                    return new Cat().execute(input.subList(1, input.size()));
                case ECHO:
                    return new Echo().execute(input.subList(1, input.size()));
                case WC:
                    return new Wc().execute(input.subList(1, input.size()));
                case PWD:
                    return new Pwd().execute(null);
                case EXIT:
                    return new Exit().execute(null);
                default:
                    throw new RuntimeException("Unknown command.");
            }
        } else if (Objects.equals(input.get(1), ASSIGN)) {
            environment.put(input.get(0), input.get(2));
            return null;
        } else {
            throw new IOException("Unknown command.");
        }
    }

    /**
     * Returns false if got an exit command.
     *
     * @param input parsed line from user
     * @return true if succeeded
     * @throws IOException
     * @throws Command.CommandFailedException
     */
    public boolean process(String input) throws IOException, Command.CommandFailedException {
        List<String> parsed = parser.parse(input, environment);

        if (Objects.equals(parsed.get(parsed.size() - 1), PIPE)) {
            throw new IOException("Incorrect input: passing value to an empty command.");
        }

        List<String> buf = new ArrayList<>();
        String output = null;
        for (int i = 0; i < parsed.size(); i++) {
            String token = parsed.get(i);

            if (Objects.equals(token, PIPE) || i == parsed.size() - 1) {
                if (i == parsed.size() - 1) {
                    buf.add(token);
                }

                // If we met a token, we can execute what we have in the buffer,
                // probably having to add some previous result.
                if (output != null) {
                    buf.add(output);
                }

                output = processUnit(buf);
                if (Objects.equals(buf.get(0), EXIT)) {
                    return false;
                } else if (i == parsed.size() - 1) {
                    return true;
                } else {
                    buf.clear();
                }
            } else {
                buf.add(token);
            }
        }

        throw new RuntimeException("Reached the end of the line.");
    }
}
