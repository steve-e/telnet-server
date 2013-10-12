package telnet.server;

public class TelnetRunner {
    public static void main(String[] args) {
        int port = 6666;
        if(args.length == 1)    {
            port = Integer.valueOf(args[0]);
        }
        new TelnetServer(port).start();
    }
}
