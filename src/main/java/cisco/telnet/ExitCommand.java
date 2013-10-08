package cisco.telnet;

import java.io.File;

public class ExitCommand implements TelnetCommand {

    @Override
    public CommandResult executeFrom(File currentDirectory) {
        return new CommandResult("Exiting", currentDirectory);
    }
}
