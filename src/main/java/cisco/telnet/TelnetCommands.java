package cisco.telnet;

import java.util.Scanner;

public class TelnetCommands {

    public TelnetCommand commandFor(final String command) {
        Scanner scanner = new Scanner(command);
        final String first = scanner.next();
        if ("pwd".equals(first)) {
            return new PwdCommand();
        } else if ("cd".equals(first)) {
            return new CdCommand(command);
        } else if ("exit".equals(first)) {
            return new ExitCommand();
        } else if ("ls".equals(first)) {
            return new LsCommand();
        } else {
            return new UnknownCommand();
        }
    }
}
