package Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cat implements Command {
    @Override
    public String execute(List<String> args) throws CommandFailedException {
        if (args.size() < 1) {
            throw new CommandFailedException("Cat: not enough arguments.");
        }

        String path = args.get(0);
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            String result = stream.collect(Collectors.joining("\n"));
            System.out.println(result);
            return result;
        } catch (IOException e) {
            throw new CommandFailedException("Incorrect input file name!");
        }
    }
}
