package cisco.telnet;

public class TelnetRunner {
    public static void main(String[] args) {
        new TelnetServer(6666).start();
    }
}
