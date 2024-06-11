package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testConstructors() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNull(explorer.getRootPath());
        assertNull(explorer.getFsPath());
        assertNull(explorer.getPrompt());
        assertNull(explorer.getOut());
        assertNull(explorer.getErr());

        explorer = new FSPathExplorer();
        assertSame(System.out, explorer.getOut());
        assertSame(System.err, explorer.getErr());
        assertNull(explorer.getRootPath());
        assertNull(explorer.getFsPath());
        assertNull(explorer.getPrompt());
    }

    @Test
    public void testMainWithArguments() throws IOException {
        String[] args = {"testData/input/arg1", "-f", "//file[@name='testFile.txt']"};
        FSPathExplorer.main(args);
    }

    @Test
    public void testMainWithoutArguments() {
        String[] args = {};
        FSPathExplorer.main(args);
        FSPathExplorer.showWelcome();
    }

    @Test
    public void testShowWelcome() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showWelcome();
    }

    @Test
    public void testConfigureExplorer() {
        String[] args = {"testData/input/arg1", "-f", "//file[@name='testFile.txt']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        File expectedRootPath = new File("testData/input/arg1");
        assertEquals(expectedRootPath, explorer.getRootPath());
        String expectedQuery = "//file[@name='testFile.txt']";
        assertEquals(expectedQuery, explorer.getFsPathQuery());
        assertNull(explorer.getFsPath());
        assertNull(explorer.getPrompt());
    }

    @Test
    public void testCreateFSPath() {
        String[] args = {"testData/input/arg1", "-f", "//file[@name='testFile.txt']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        File expectedRootPath = new File("testData/input/arg1");
        File expectedFSPath = new File(expectedRootPath, "testFile.txt").getAbsoluteFile();
        FSPath expectedFSPathInstance = FSPathFactory.newFSPath(expectedRootPath);
        FSPath expectedResult = explorer.getFsPath();
        assertEquals(expectedFSPathInstance, expectedResult);
    }

    @Test
    public void testStart() throws IOException {
        String[] args = {"testData/input/arg1", "-f", "//file[@name='testFile.txt']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        String expectedOutput = "testData/input/arg1/testFile.txt";
        String output = explorer.getOut().toString();
        assertTrue(output.contains(expectedOutput));
    }

    @Test
    public void testInvalidQuery() throws IOException {
        String[] args = {"testData/input/arg1", "-f", "//file[@name='testFile'"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        try {
            explorer.start();
            fail("Expected an IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCdCommand() throws IOException {
        String[] args = {"testData/input/arg1", "-f", "//file[@name='testFile.txt']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        File newDir = new File("testData/input/arg2");
        explorer.prompt.sendLine("cd testData/input/arg2");
        assertEquals(newDir, explorer.getFsPath().getRootDirectory());
    }

    @Test
    public void testPwdCommand() throws IOException {
        String[] args = {"testData/input/arg1", "-f", "//file[@name='testFile.txt']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        File expectedRootPath = new File("testData/input/arg1");
        explorer.prompt.sendLine("pwd");
        String output = explorer.getOut().toString();
        assertEquals(expectedRootPath.getAbsolutePath(), output.split("\n")[0]);
    }

    @Test
    public void testHelpCommand() throws IOException {
        String[] args = {"testData/input/arg1", "-f", "//file[@name='testFile.txt']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.prompt.sendLine("help");
        String output = explorer.getOut().toString();
        //TODO: Add an assertion that the help text is correctly displayed
        // output.contains("FSPathExplorer help :");
    }
}