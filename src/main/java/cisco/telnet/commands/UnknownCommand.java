package cisco.telnet.commands;

import cisco.telnet.CommandResult;

import java.io.File;

public class UnknownCommand implements TelnetCommand {
    @Override
    public CommandResult executeFrom(File currentDirectory) {
        return new CommandResult("Unrecognised command", currentDirectory);
    }
}
