package cisco.telnet;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelnetServer {

    private final int port;
    private final ExecutorService executorService;
    private volatile boolean listening;
    private ServerSocket serverSocket;

    public TelnetServer(int port) {
        this.port = port;
        listening = true;
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            while (listening) {
                executorService.submit(new TelnetTask(serverSocket.accept()));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void close() {
        try {
            listening = false;
            serverSocket.close();
            executorService.shutdownNow();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
