package telnet.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TelnetServerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private TelnetServer telnetServer;
    private Socket socket;
    private ExecutorService executorService;
    private File root;
    private File child;
    private File grandChild;
    private CountDownLatch finishedGate;

    @Before
    public void setUp() throws Exception {
        createDirectoryStructure();
        executorService = Executors.newFixedThreadPool(1);
        int port = 55001;
        telnetServer = new TelnetServer(port);
        finishedGate = new CountDownLatch(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                telnetServer.start();
                finishedGate.countDown();
            }
        });

        socket = new Socket();
        socket.connect(new InetSocketAddress(port), 2000);
    }

    private void createDirectoryStructure() throws IOException {
        root = temporaryFolder.getRoot();
        child = temporaryFolder.newFolder();
        grandChild = new File(child, "grandChild");
        grandChild.mkdir();
    }

    @After
    public void tearDown() throws Exception {
        socket.close();
        telnetServer.close();
        executorService.shutdown();
        finishedGate.await(2, TimeUnit.SECONDS);
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
        String expectedPath = new File(".").getCanonicalPath();
        whenTheClientSends("pwd");
        assertThat(theServerResponse(), is(expectedPath));
    }

    @Test
    public void canPwdTwice() throws Exception {
        String expectedPath = new File(".").getCanonicalPath();
        whenTheClientSends("pwd");
        assertThat(theServerResponse(), is(expectedPath));
        whenTheClientSends("pwd");
        assertThat(theServerResponse(), is(expectedPath));
    }

    @Test
    public void canChangeDirectory() throws Exception {
        String expectedPath = root.getAbsolutePath();
        whenTheClientSends("cd " + expectedPath);
        assertThat(theServerResponse(), is(expectedPath));
        whenTheClientSends("pwd");
        assertThat(theServerResponse(), is(expectedPath));
    }

    @Test
    public void cannotChangeToNonExistentDirectory() throws Exception {
        String expectedPath = root.getAbsolutePath();
        whenTheClientSends("cd " + expectedPath);
        assertThat(theServerResponse(), is(expectedPath));
        whenTheClientSends("cd foo");
        assertThat(theServerResponse(), is(expectedPath));
    }

    @Test
    public void canChangeWithRelativeDirectory() throws Exception {
        String expectedPath = root.getAbsolutePath();
        whenTheClientSends("cd " + expectedPath);
        assertThat(theServerResponse(), is(expectedPath));
        whenTheClientSends("cd "+child.getName());
        assertThat(theServerResponse(), is(child.getAbsolutePath()));
        whenTheClientSends("cd "+grandChild.getName());
        assertThat(theServerResponse(), is(grandChild.getAbsolutePath()));
    }

    @Test
    public void canListDirectory() throws Exception {
        String expectedPath = grandChild.getAbsolutePath();
        whenTheClientSends("cd " + expectedPath);
        assertThat(theServerResponse(), is(expectedPath));
        whenTheClientSends("ls");
        assertThat(theServerResponse(), is(""));
    }

    @Test
    public void canDisconnect() throws Exception {
        whenTheClientSends("exit");
        assertThat(theServerResponse(), is("Exiting"));
    }

    @Test
    public void ignoresUnrecognisedCommand() throws Exception {
        whenTheClientSends("foo");
        assertThat(theServerResponse(), is("Unrecognised command"));
    }

    private String theServerResponse() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
    }

    private void whenTheClientSends(String command) throws IOException {
        new PrintWriter(socket.getOutputStream(), true).println(command);
    }
}
