package cisco.telnet.commands;

import cisco.telnet.CommandResult;

import java.io.File;

public class CdCommand implements TelnetCommand {
    private final String command;

    public CdCommand(String command) {
        this.command = command;
    }

    @Override
    public CommandResult executeFrom(File currentDirectory) {
        String path = command.substring("cd".length()).trim();
        File newDirectory = new File(path);
        if (!newDirectory.isAbsolute()) {
            newDirectory = new File(currentDirectory, path);
        }
        return new CommandResult(newDirectory.getAbsolutePath(), newDirectory);
    }
}
