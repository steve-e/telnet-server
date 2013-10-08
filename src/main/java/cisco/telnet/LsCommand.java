package cisco.telnet;

import org.apache.commons.lang.StringUtils;

import java.io.File;

public class LsCommand implements TelnetCommand {
    @Override
    public CommandResult executeFrom(File currentDirectory) {
        return new CommandResult(StringUtils.join(currentDirectory.list(), "\n"), currentDirectory);
    }
}
