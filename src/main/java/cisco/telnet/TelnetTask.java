package cisco.telnet;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.Socket;

public class TelnetTask implements Runnable {

    private final Socket socket;
    private final TelnetCommands telnetCommands;
    private File currentDirectory = new File(".");

    public TelnetTask(Socket socket) {
        this.socket = socket;
        telnetCommands = new TelnetCommands();
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
        return telnetCommands.commandFor(receive());
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
