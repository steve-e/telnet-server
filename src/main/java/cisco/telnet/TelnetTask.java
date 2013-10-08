package cisco.telnet;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TelnetTask implements Runnable {

    private final Socket socket;
    private File currentDirectory = new File(".");

    public TelnetTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                replyTo(command());
            }
        } catch (IOException e) {
            // Connection reset, exit
        }
    }

    private TelnetCommand command() throws IOException {
        return commandFor(receive());
    }

    private TelnetCommand commandFor(final String command) {
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

    private String receive() throws IOException {
        if (socket.isClosed() || !socket.isConnected() || socket.isInputShutdown()) {
            return "exit";
        }
        return StringUtils.defaultString(new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine(), "exit");
    }

    private void replyTo(TelnetCommand cmd) throws IOException {
        CommandResult commandResult = cmd.executeFrom(currentDirectory);
        currentDirectory = commandResult.currentDirectory();
        new PrintWriter(socket.getOutputStream(), true).println(commandResult.output());
        if (cmd instanceof ExitCommand) {
            Thread.currentThread().interrupt();
            socket.close();
        }
    }
}
