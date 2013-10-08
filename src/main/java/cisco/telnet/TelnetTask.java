package cisco.telnet;

import cisco.telnet.commands.ExitCommand;
import cisco.telnet.commands.TelnetCommand;
import cisco.telnet.commands.TelnetCommands;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class TelnetTask implements Runnable {

    private final Socket socket;
    private final TelnetCommands telnetCommands;
    private final SocketCommunications socketCommunications;
    private File currentDirectory = new File(".");

    public TelnetTask(Socket socket) {
        this.socket = socket;
        telnetCommands = new TelnetCommands();
        socketCommunications = new SocketCommunications(socket);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                respondTo(command());
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
        return StringUtils.defaultString(socketCommunications.clientInstruction(), "exit");
    }

    private void respondTo(TelnetCommand command) throws IOException {
        CommandResult commandResult = command.executeFrom(currentDirectory);
        updateCurrentDirectory(commandResult);
        replyWith(commandResult);
        postProcess(command);
    }

    private void postProcess(TelnetCommand command) throws IOException {
        if(command instanceof ExitCommand) {
            exit();
        }
    }

    public void exit() throws IOException {
        Thread.currentThread().interrupt();
        socket.close();
    }

    private void updateCurrentDirectory(CommandResult commandResult) {
        currentDirectory = commandResult.currentDirectory();
    }

    private void replyWith(CommandResult commandResult) throws IOException {
        socketCommunications.reply(commandResult.output());
    }
}
