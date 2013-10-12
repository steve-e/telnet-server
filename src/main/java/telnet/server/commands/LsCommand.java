package telnet.server.commands;

import org.apache.commons.lang.StringUtils;
import telnet.server.CommandResult;

import java.io.File;

public class LsCommand implements TelnetCommand {
    @Override
    public CommandResult executeFrom(File currentDirectory) {
        return new CommandResult(StringUtils.join(currentDirectory.list(), "\n"), currentDirectory);
    }
}
