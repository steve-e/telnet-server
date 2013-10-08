package cisco.telnet.commands;

import cisco.telnet.CommandResult;

import java.io.File;

public class PwdCommand implements TelnetCommand {
    @Override
    public CommandResult executeFrom(File currentDirectory) {
        return new CommandResult(currentDirectory.getAbsolutePath(), currentDirectory);
    }
}
