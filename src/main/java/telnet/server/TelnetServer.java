package telnet.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TelnetServer {

    private final int port;
    private final ExecutorService executorService;
    private final CountDownLatch startUp;
    private volatile boolean listening;
    private ServerSocket serverSocket;

    public TelnetServer(int port) {
        this.port = port;
        listening = true;
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        startUp = new CountDownLatch(1);
    }

    public void start() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                startServer();
            }
        });
        try {
            startUp.await(10,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            startUp.countDown();
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
            executorService.shutdown();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
