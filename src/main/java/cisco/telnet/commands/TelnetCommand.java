package cisco.telnet.commands;

import cisco.telnet.CommandResult;

import java.io.File;

public interface TelnetCommand {
    public CommandResult executeFrom(File currentDirectory);
}
