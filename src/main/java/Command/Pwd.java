package Command;

import java.util.List;

public class Pwd implements Command {
    @Override
    public String execute(List<String> args) throws CommandFailedException {
        String workingDir = System.getProperty("user.dir");
        System.out.println(workingDir);
        return workingDir;
    }

    @Override
    public CommandType getType() {
        return CommandType.pwd;
    }
}
