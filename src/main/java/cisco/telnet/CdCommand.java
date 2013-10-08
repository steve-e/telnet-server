package cisco.telnet;

import java.io.File;

public class CdCommand implements TelnetCommand {
    private final String command;

    public CdCommand(String command) {
        this.command = command;
    }

    @Override
    public String execute() {
        return new File(command.substring("cd".length()).trim()).getAbsolutePath();
    }
}
