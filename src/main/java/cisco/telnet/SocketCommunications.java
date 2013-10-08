package cisco.telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketCommunications {

    private final Socket socket;

    public SocketCommunications(Socket socket) {
        this.socket = socket;
    }

    public void reply(String output) throws IOException {
        new PrintWriter(socket.getOutputStream(), true).println(output);
    }

    public String clientInstruction() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
    }
}
