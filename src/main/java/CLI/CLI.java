package CLI;

import Command.Command;
import Utils.Processor;

import java.io.BufferedReader;
import java.io.IOException;

public class CLI {
    private static Processor processor = Processor.getInstance();

    public static void run(BufferedReader input) {
        try {
            String line = input.readLine();
            while(processor.process(line)) {
                line = input.readLine();
            }
        } catch (IOException | Command.CommandFailedException | RuntimeException e) {
                System.out.println(e.getMessage());
        }
    }
}
