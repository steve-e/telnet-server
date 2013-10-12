package telnet.server.commands;

import telnet.server.CommandResult;

import java.io.File;

public interface TelnetCommand {
    public CommandResult executeFrom(File currentDirectory);
}
