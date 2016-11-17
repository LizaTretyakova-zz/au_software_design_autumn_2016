package Command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grep implements Command {

    public static class State {
        @Parameter(names = "-i", description = "Ignore case")
        public boolean caseInsensitive = false;

        @Parameter(names = "-w", description = "Match only the whole word")
        public boolean wholeWord = false;

        @Parameter(names = "-A", description = "Print some lines")
        public int afterLines = 0;

        public int cnt = 0;
    }

    @Override
    public String execute(List<String> args) throws CommandFailedException {
        State state = getSettings(args);
        Pattern pattern = constructRegex(args, state);

        // Process file.
        String path = args.get(args.size() - 1);
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            String result = stream.filter(line -> {
               state.cnt--;
               if(pattern.matcher(line).find()) {
                   state.cnt = state.afterLines;
                   return true;
               } else if(state.cnt >= 0) {
                    return true;
               }
               return false;
            }).collect(Collectors.joining("\n"));
            System.out.println(result);
            return result;
        } catch (IOException e) {
            throw new CommandFailedException("Incorrect input file name!");
        }
    }

    private State getSettings(List<String> args) {
        State state = new State();
        String[] argv = new String[args.size() - 2];
        for(int i = 0; i < args.size() - 2; ++i) {
            argv[i] = args.get(i);
        }
        new JCommander(state, argv);
        return state;
    }

    private Pattern constructRegex(List<String> args, State state) {
        String patternArg = args.get(args.size() - 2);
        if(state.wholeWord) {
            patternArg = "\\b" + patternArg + "\\b";
        }
        Pattern pattern;
        if(state.caseInsensitive) {
            pattern = Pattern.compile(patternArg, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(patternArg);
        }
        return pattern;
    }
}
