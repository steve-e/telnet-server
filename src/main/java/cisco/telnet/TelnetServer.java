package cisco.telnet;

import java.io.IOException;
import java.net.ServerSocket;

public class TelnetServer {

    private final int port;
    private volatile boolean listening;
    private ServerSocket serverSocket;

    public TelnetServer(int port) {
        this.port = port;
        listening = true;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            while (listening) {
                new Thread(new TelnetTask(serverSocket.accept())).start();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void close() {
        try {
            listening = false;
            serverSocket.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
