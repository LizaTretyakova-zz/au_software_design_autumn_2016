package Command;

import java.util.List;

/**
 * Outputs the current work path to the console.
 */
public class Pwd implements Command {
    @Override
    public String execute(List<String> args) throws CommandFailedException {
        String workingDir = System.getProperty("user.dir");
        System.out.println(workingDir);
        return workingDir;
    }

}
