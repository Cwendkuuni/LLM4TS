package net.sf.sugar.fspath.cli;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.sf.sugar.fspath.cli.Commands.*;

/**
 * FSPathExplorerTest.
 */
public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private File tempDir;

    @Before
    public void setUp() {
        try {
            tempDir = File.createTempDirectory("junit", "");
        } catch (IOException e) {
            fail("Failed to create temp directory");
        }

        explorer = new FSPathExplorer();
        explorer.rootPath = tempDir;
    }

    @After
    public void tearDown() {
        if (tempDir != null && tempDir.exists()) {
            FileUtils.deleteDirectory(tempDir);
        }
    }

    @Test
    public void testEmptyQuery() {
        explorer.configureExplorer(new String[]{"", ""});
        explorer.createFSPath();
        explorer.start();
        String expected = "Current directory : " + tempDir.getAbsolutePath() + "\n";
        String actual = getOutput();
        assertEquals(expected, actual);
    }

    @Test
    public void testSimpleQuery() {
        explorer.configureExplorer(new String[]{"-p", tempDir.getAbsolutePath() + File.separator + "sub", "-f", "*.txt"});
        explorer.createFSPath();
        explorer.start();
        String expected = "Current directory : " + new File(tempDir, "sub").getAbsolutePath() + "\n";
        String actual = getOutput();
        assertEquals(expected, actual);
        // Add more assertions to check the output of the query
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidQuery() {
        explorer.configureExplorer(new String[]{"", ""});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine("//dir[@name = 'invalid']");
        // No need to add any assertions here as the test will fail with the expected exception
    }

    @Test
    public void testCDCommand() {
        explorer.configureExplorer(new String[]{"-p", tempDir.getAbsolutePath() + File.separator + "sub", "-f", "*.txt"});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine(CD.getCommandText() + " parent");
        String expected = "changed to : " + new File(tempDir, "..").getAbsolutePath() + "\n";
        String actual = getOutput();
        assertEquals(expected, actual);
    }

    @Test
    public void testPWDCommand() {
        explorer.configureExplorer(new String[]{"-p", tempDir.getAbsolutePath() + File.separator + "sub"});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine(PWD.getCommandText());
        String expected = "Current directory : " + new File(tempDir, "sub").getAbsolutePath() + "\n";
        String actual = getOutput();
        assertEquals(expected, actual);
    }

    @Test
    public void testHelpCommand() {
        explorer.configureExplorer(new String[]{"", ""});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine(HELP.getCommandText());
        // Add assertions to check the output of the help command
    }

    @Test
    public void testExitCommand() {
        explorer.configureExplorer(new String[]{"", ""});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine(EXIT.getCommandText());
        // Check that the program exits after running the exit command
    }

    private String getOutput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        explorer.start();
        System.setOut(System.out);
        return outContent.toString();
    }
}