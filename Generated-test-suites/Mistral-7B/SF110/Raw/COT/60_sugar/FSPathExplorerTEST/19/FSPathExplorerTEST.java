package net.sf.sugar.fspath.cli;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

/**
 * Tests for {@link FSPathExplorer}.
 */
public class FSPathExplorerTest {
    // 

    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    @Test
    public void testMain() throws IOException, InterruptedException {
        String[] args = {"-p", "/tmp"};
        FSPathExplorer.main(args);
        // Add more assertions based on expected output
    }

    @Test
    public void testShowWelcome() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showWelcome();
        String expected = "+-------------------------------------------------------+\n" +
                "|                                                       |\n" +
                "| FSPathExplorer                                        |\n" +
                "|                                                       |\n" +
                "+-------------------------------------------------------+\n" +
                "Type 'help' for commands...\n" +
                "Scanning filesystem metadata...\n" +
                "+-------------------------------------------------------+\n" +
                "PLEASE NOTE running FSPathExplorer from the top level   |\n" +
                "of your filesystem is not advised                        |\n" +
                "+-------------------------------------------------------+\n";
        System.out.flush();
        String actual = System.out.getBytes()[0] >= 0 ? System.out.getOutputStream().toString() : new String(System.out.getErrStream().readAllBytes());
        assertEquals(expected, actual);
        System.out.setOut(System.out.origin);
        System.out.setErr(System.err);
    }

    @Test
    public void testShowHelp() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showHelp();
        // Add more assertions based on expected output
    }

    @Test
    public void testConfigureExplorer_NoArgs() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[0]);
        assertNull(explorer.getRootPath());
        assertNull(explorer.getFsPathQuery());
    }

    @Test
    public void testConfigureExplorer_ValidArgs() {
        String[] args = {"-p", "/tmp", "-f", "//dir[@name = 'test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        File expectedRootPath = new File("/tmp");
        String expectedFsPathQuery = "//dir[@name = 'test']";
        assertEquals(expectedRootPath, explorer.getRootPath());
        assertEquals(expectedFsPathQuery, explorer.getFsPathQuery());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorer_MissingPathArg() {
        String[] args = {"-f", "//dir[@name = 'test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorer_MissingFindArg() {
        String[] args = {"-p", "/tmp"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.getFsPath());
    }

    @Test
    public void testCreateFSPath_NoRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(null);
        explorer.createFSPath();
        assertNull(explorer.getFsPath());
    }

    @Test
    public void testStart_PWDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/tmp"));
        explorer.createFSPath();
        String expected = "Current directory : /tmp";
        String output = "";
        try {
            explorer.start();
            output = explorer.getOutput();
        } finally {
            explorer.setOutput("");
        }
        assertEquals(expected, output);
    }

    @Test
    public void testStart_CDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.setRootPath(rootPath);
        explorer.createFSPath();
        String expected = "changed to : ./foo";
        String output = "";
        try {
            explorer.start();
            String command = "cd ./foo";
            explorer.executeCommand(command);
            output = explorer.getOutput();
        } finally {
            explorer.setOutput("");
            explorer.setCurrentDirectory(rootPath);
        }
        assertEquals(expected, output);
        assertEquals(new File("/tmp/foo"), explorer.getCurrentDirectory());
    }

    @Test
    public void testStart_InvalidCDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.setRootPath(rootPath);
        explorer.createFSPath();
        String expectedError = "unable to change to " + Commands.CD.getArgument() + " please make sure that the directory exists and is readable";
        String output = "";
        try {
            explorer.start();
            String command = "cd abc";
            explorer.executeCommand(command);
            output = explorer.getOutput();
        } finally {
            explorer.setOutput("");
        }
        assertTrue(output.contains(expectedError));
    }

    @Test
    public void testStart_InvalidFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.setRootPath(rootPath);
        explorer.createFSPath();
        String expectedError = "Unsupported query : ";
        String output = "";
        try {
            explorer.start();
            String command = "//dir[@name = 'abc']/file[contains(name, 'xyz')]/attrib[@name = 'xyz']";
            explorer.executeCommand(Commands.QUERY.getCommandText() + " " + command);
            output = explorer.getOutput();
        } finally {
            explorer.setOutput("");
        }
        assertTrue(output.contains(expectedError));
    }

    @Test
    public void testStart_ValidFSPathQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.setRootPath(rootPath);
        explorer.createFSPath();
        String expected = "//dir[@name = 'lib']/file[contains(name, '.class')]";
        String output = "";
        try {
            explorer.start();
            explorer.executeCommand(Commands.QUERY.getCommandText() + " " + expected);
            List<FSPathResult> results = explorer.getResults();
            for (FSPathResult result : results) {
                output += result.getFile().getPath() + "\n";
            }
        } finally {
            explorer.setOutput("");
        }
        String expectedOutput = "/tmp/lib/foo/bar/com/example/MyClass.class\n";
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStart_QUITCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        String command = Commands.QUIT.getCommandText();
        explorer.executeCommand(command);
        assertTrue(explorer.isRunning() == false);
    }

    // 

    private FSPathExplorer explorer;
    private String output;

    private void setOutput(String output) {
        this.output = output;
        System.setOut(new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void print(String s) {
                this.out.write(s.getBytes());
            }
        });
    }

    private String getOutput() {
        String out = new String(System.out.getBytes());
        System.setOut(System.out);
        return out;
    }

    private File getCurrentDirectory() {
        return this.explorer.getCurrentDirectory();
    }

    private void setCurrentDirectory(File currentDirectory) {
        this.explorer.setCurrentDirectory(currentDirectory);
    }

    private boolean isRunning() {
        return this.explorer.isRunning();
    }
}