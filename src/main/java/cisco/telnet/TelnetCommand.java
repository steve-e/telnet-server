package cisco.telnet;

import java.io.File;

public interface TelnetCommand {
    public CommandResult executeFrom(File currentDirectory);
}
