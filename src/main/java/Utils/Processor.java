package Utils;

import Command.Command;
import Command.Cat;
import Command.Echo;
import Command.Exit;
import Command.Pwd;
import Command.Wc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Processor {
    private static final HashMap<String, String> environment = new HashMap<>();
    private static Processor instance = null;
    private static Parser parser = Parser.getInstance();

    private Processor() {}

    public static Processor getInstance() {
        if(instance == null) {
            instance = new Processor();
        }
        return instance;
    }

    private String processUnit(List<String> input) throws IOException, Command.CommandFailedException {
        if(Command.isCommand(input.get(0))) {
            switch(input.get(0)) {
                case Command.CAT:
                    return new Cat().execute(input.subList(1, input.size()));
                case Command.ECHO:
                    return new Echo().execute(input.subList(1, input.size()));
                case Command.WC:
                    return new Wc().execute(input.subList(1, input.size()));
                case Command.PWD:
                    return new Pwd().execute(null);
                case Command.EXIT:
                    return new Exit().execute(null);
                default:
                    throw new RuntimeException("Unknown command.");
            }
        } else if(Objects.equals(input.get(1), Command.ASSIGN)) {
            environment.put(input.get(0), input.get(2));
            return null;
        } else {
            throw new IOException("Unknown command.");
        }
    }

    // Returns false if got an exit command.
    public boolean process(String input) throws IOException, Command.CommandFailedException {
        List<String> parsed = parser.parse(input, environment);

        if(Objects.equals(parsed.get(parsed.size() - 1), Command.PIPE)) {
            throw new IOException("Incorrect input: passing value to an empty command.");
        }

        List<String> buf = new ArrayList<>();
        String output = null;
        for(int i = 0; i < parsed.size(); i++) {
            String token = parsed.get(i);

            if(Objects.equals(token, Command.PIPE) || i == parsed.size() - 1) {
                if(i == parsed.size() - 1) {
                    buf.add(token);
                }

                // If we met a token, we can execute what we have in the buffer,
                // probably having to add some previous result.
                if(output != null) {
                    buf.add(output);
                }

                output = processUnit(buf);
                if(Objects.equals(buf.get(0), Command.EXIT)) {
                    return false;
                } else if(i == parsed.size() - 1) {
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
