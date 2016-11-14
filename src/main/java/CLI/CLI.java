package CLI;

import Command.Command;
import Utils.Processor;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The main class running the whole process.
 */
public class CLI {
    private static Processor processor = Processor.getInstance();

    /**
     * The main running function.
     *
     * @param input the stream of subsequent commands to CLI
     */
    public static void run(BufferedReader input) {
        try {
            String line = input.readLine();
            boolean running = true;
            while (running) {
                try {
                    running = processor.process(line);
                } catch (Command.CommandFailedException e) {
                    System.out.println(e.getMessage());
                    running = true;
                }
                line = input.readLine();
            }
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
