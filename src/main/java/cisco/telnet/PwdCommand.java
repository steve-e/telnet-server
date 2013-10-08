package cisco.telnet;

import java.io.File;

public class PwdCommand implements TelnetCommand {
    @Override
    public String execute() {
        return new File(".").getAbsolutePath();
    }
}
