package Command;

import java.util.List;

public class Exit implements Command {
    @Override
    public String execute(List<String> args) throws CommandFailedException {
        System.out.println("Good bye!");
        return null;
    }

    @Override
    public CommandType getType() {
        return CommandType.exit;
    }
}
