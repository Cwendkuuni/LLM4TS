package net.sf.sugar.fspath.cli; // replace if necessary

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {

    private static final String TEST_ROOT_DIR = new File(".").getAbsolutePath();

    private FSPathExplorer explorer;
    private String query;

    @Before
    public void setup() {
        explorer = new FSPathExplorer();
        query = "";
    }

    @After
    public void teardown() throws IOException {
        if (explorer != null) {
            explorer.fsPath.close();
            explorer = null;
        }
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertEquals(System.out, explorer.out);
        assertEquals(System.err, explorer.err);
        assertNull(explorer.rootPath);
        assertEquals("", explorer.fsPathQuery);
        assertNull(explorer.fsPath);
        assertNull(explorer.prompt);
    }

    /**
     * Test main method
     */
    @Test
    public void testMain() {
        String[] args = {};
        FSPathExplorer.main(args);
        // Since JUnit can't call System.exit, we can't directly test the main method
        // But, you can add assertions to test the end result of the program (output, etc.)
    }

    /**
     * Test configureExplorer method
     */
    @Test
    public void testConfigureExplorer() throws IOException {
        String[] args = {"-path", "./", "-find", "test.txt"};
        explorer.configureExplorer(args);
        assertEquals(new File("."), explorer.rootPath);
        assertEquals("test.txt", explorer.fsPathQuery);
        assertEquals(null, explorer.fsPath);
        assertNotNull(explorer.prompt);
    }

    /**
     * Test createFSPath method
     */
    @Test
    public void testCreateFSPath() throws IOException {
        File rootPath = new File(TEST_ROOT_DIR);
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        assertEquals(rootPath, explorer.fsPath.getRootDirectory());
    }

    /**
     * Test start method with no input
     */
    @Test
    public void testStartEmptyInput() throws IOException {
        String input = "";
        String[] output = explorer.start(input);
        assertEquals(0, output.length);
    }

    /**
     * Test start method with help command
     */
    @Test
    public void testStartHelpCommand() throws IOException {
        String input = Commands.HELP.getCommandText();
        String[] output = explorer.start(input);
        // You can add assertions to check the output
        // Or test the end result of the program (output, etc.)
    }

    /**
     * Test start method with pwd command
     */
    @Test
    public void testStartPwdCommand() throws IOException {
        String input = Commands.PWD.getCommandText();
        String[] output = explorer.start(input);
        assertEquals(1, output.length);
        assertEquals(TEST_ROOT_DIR, output[0]);
    }

    /**
     * Test start method with cd command
     */
    @Test
    public void testStartCdCommand() throws IOException {
        File newDir = new File("temp");
        newDir.mkdir();
        String input = "cd temp";
        String[] output = explorer.start(input);
        assertEquals(1, output.length);
        assertTrue(newDir.getAbsolutePath().equals(output[0]));
        newDir.delete();
    }

    /**
     * Test start method with invalid cd command
     */
    @Test(expected = IOException.class)
    public void testStartInvalidCdCommand() throws IOException {
        String input = "cd invalid";
        explorer.start(input);
    }

    /**
     * Test start method with query command
     */
    @Test
    public void testStartQueryCommand() throws IOException {
        query = "//dir[@name = 'test']";
        String input = query;
        String[] output = explorer.start(input);
        // You can add assertions to check the output
        // Or test the end result of the program (output, etc.)
    }
}