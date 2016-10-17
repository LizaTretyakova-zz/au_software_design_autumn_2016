package Command;

import java.util.List;

public class Echo implements Command {
    @Override
    public String execute(List<String> args) throws CommandFailedException {
        if (args.size() < 1) {
            throw new CommandFailedException("Echo: not enough arguments!");
        }

        String result = args.get(0);//.calculate();
        System.out.println(result);
        return result;
    }

    @Override
    public CommandType getType() {
        return CommandType.echo;
    }
}
