package cisco.telnet.commands;

import cisco.telnet.CommandResult;
import org.apache.commons.lang.StringUtils;
import java.io.File;
import static java.lang.System.lineSeparator;

public class LsCommand implements TelnetCommand {
    @Override
    public CommandResult executeFrom(File currentDirectory) {
        return new CommandResult(StringUtils.join(currentDirectory.list(), lineSeparator()), currentDirectory);
    }
}
