package telnet.server;

import java.io.File;

public class CommandResult {
    private final String output;
    private final File currentDirectory;

    public CommandResult(String output, File currentDirectory) {
        this.output = output;
        this.currentDirectory = currentDirectory;
    }

    public String output() {
        return output;
    }

    public File currentDirectory() {
        return currentDirectory;
    }
}
