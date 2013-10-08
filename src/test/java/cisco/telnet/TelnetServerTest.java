package cisco.telnet;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TelnetServerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private TelnetServer telnetServer;
    private Socket socket;
    private ExecutorService executorService;
    private File root;

    @Before
    public void setUp() throws Exception {
        root = temporaryFolder.getRoot();
        executorService = Executors.newFixedThreadPool(1);
        int port = new Random().nextInt(1000) + 5000;
        telnetServer = new TelnetServer(port);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                telnetServer.start();
            }
        });

        socket = new Socket("localhost", port);
    }

    @After
    public void tearDown() throws Exception {
        socket.close();
        telnetServer.close();
        executorService.shutdownNow();
    }

    @Test
    public void canConnect() throws Exception {
        whenTheClientSends("pwd");
        assertThat(socket.isConnected(), is(true));
        assertThat(socket.isBound(), is(true));
        assertThat(socket.isClosed(), is(false));
    }

    @Test
    public void canPwd() throws Exception {
        String expectedPath = new File(".").getAbsolutePath();
        whenTheClientSends("pwd");
        assertThat(theServerResponse(), is(expectedPath));
    }

    @Test
    public void canCd() throws Exception {
        String expectedPath = root.getAbsolutePath();
        whenTheClientSends("cd " + expectedPath);
        assertThat(theServerResponse(), is(expectedPath));
    }

    @Test
    public void canDisconnect() throws Exception {
        whenTheClientSends("exit");
        assertThat(theServerResponse(), is("Exiting"));
        //assertThat(socket.isClosed(), is(true));
    }

    private String theServerResponse() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
    }

    private void whenTheClientSends(String command) throws IOException {
        new PrintWriter(socket.getOutputStream(), true).println(command);
    }

}
