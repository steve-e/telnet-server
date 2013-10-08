package cisco.telnet;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TelnetTask implements Runnable {

    private final Socket socket;

    public TelnetTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            reply(commandFor(receive()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TelnetCommand commandFor(final String command) {
        Scanner scanner = new Scanner(command);
        final String first = scanner.next();
        if ("pwd".equals(first)) {
            return new PwdCommand();
        } else if ("cd".equals(first)) {
            return new TelnetCommand() {
                @Override
                public String execute() {
                    return new File(command.substring(first.length()).trim()).getAbsolutePath();
                }
            };
        }
        throw new IllegalStateException("no command!");
    }

    private String receive() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
    }

    private void reply(TelnetCommand cmd) throws IOException {
        new PrintWriter(socket.getOutputStream(), true).println(cmd.execute());
    }

}
