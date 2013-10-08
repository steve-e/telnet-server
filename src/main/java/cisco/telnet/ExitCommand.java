package cisco.telnet;

import java.io.IOException;
import java.net.Socket;

public class ExitCommand implements TelnetCommand {
    private final Socket socket;

    public ExitCommand(Socket socket) {
        this.socket = socket;
    }

    @Override
    public String execute() {
        try {
            return "Exiting";
        } catch (RuntimeException e) {
            throw new IllegalStateException(e);
        }
    }
}
