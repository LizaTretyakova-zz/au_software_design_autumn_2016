package Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * Counts the statistics of the provided file: the number of words, the number of lines and the number of bytes.
 */
public class Wc implements Command {
    @Override
    public String execute(List<String> args) throws CommandFailedException {
        if (args.size() < 1) {
            throw new CommandFailedException("Wc: not enough arguments.");
        }

        Stats stats = new Stats();
        String path = args.get(0);

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(line -> {
                stats.lines++;
                stats.words += line.trim().isEmpty() ? 0 : line.trim().split("\\s+").length;
            });
        } catch (IOException e) {
            throw new CommandFailedException(e.getMessage());
        }
        try {
            stats.bytes = Files.size(Paths.get(path));
        } catch (IOException e) {
            throw new CommandFailedException(e.getMessage());
        }

        String result = "File stats: " + Integer.toString(stats.words) + " words, "
                + Integer.toString(stats.lines) + " lines and "
                + Long.toString(stats.bytes) + " bytes.";
        System.out.println(result);
        return result;
    }

    private static class Stats {
        public int words = 0;
        public int lines = 0;
        public long bytes = 0;
    }
}
