package cisco.telnet;

import java.io.File;

public class CdCommand implements TelnetCommand {
    private final String command;

    public CdCommand(String command) {
        this.command = command;
    }

    @Override
    public CommandResult executeFrom(File currentDirectory) {
        File newDirectory = new File(command.substring("cd".length()).trim());
        return new CommandResult(newDirectory.getAbsolutePath(), newDirectory);
    }
}
