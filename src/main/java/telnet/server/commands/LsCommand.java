package telnet.server.commands;

import org.apache.commons.lang.StringUtils;
import telnet.server.CommandResult;

import java.io.File;

import static java.lang.System.lineSeparator;

public class LsCommand implements TelnetCommand {
    @Override
    public CommandResult executeFrom(File currentDirectory) {
        return new CommandResult(StringUtils.join(currentDirectory.list(), lineSeparator()), currentDirectory);
    }
}
