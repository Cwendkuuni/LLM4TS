package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testFSPathExplorerCreation() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNull(explorer.rootPath);
        assertNull(explorer.fsPath);
        assertNull(explorer.prompt);
        assertNull(explorer.out);
        assertNull(explorer.err);
    }

    @Test
    public void testConfigureExplorerWithValidArgs() {
        String[] args = {"-p", "/home/user/test", "-f", "//dir[@name = 'test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals(new File("/home/user/test"), explorer.rootPath);
        assertEquals("/home/user/test", explorer.fsPath.getRootDirectory().getAbsolutePath());
        assertFalse(explorer.fsPathQuery.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithInvalidArgsNoPath() {
        String[] args = {"-f", "//dir[@name = 'test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertNull(explorer.fsPath);
        assertNull(explorer.prompt);
        assertNull(explorer.out);
        assertNull(explorer.err);
        // the test should fail with an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithInvalidArgsNoQuery() {
        String[] args = {"-p", "/home/user/test"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals(new File("/home/user/test"), explorer.rootPath);
        assertEquals("/home/user/test", explorer.fsPath.getRootDirectory().getAbsolutePath());
        assertFalse(explorer.fsPathQuery.isEmpty());
        // the test should fail with an IllegalArgumentException
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File testFile = new File("/tmp/testFile.txt");
        explorer.createFSPath();
        assertNull(explorer.rootPath);
        assertNull(explorer.fsPath);

        explorer.rootPath = new File("/home/user/test");
        explorer.createFSPath();
        assertEquals("/home/user/test", explorer.fsPath.getRootDirectory().getAbsolutePath());

        explorer.rootPath = null;
        try {
            testFile.createNewFile();
        } catch (IOException e) {
            // ignore
        }
        explorer.createFSPath();
        assertTrue(explorer.fsPath.getFile().exists());
    }

    @Test
    public void testStartWithNoCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();
        assertTrue(explorer.prompt.isOpen());
        String input = "ls";
        String output = explorer.prompt.readLine(input);
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(lines.get(0).contains("Current directory:"));
    }

    @Test
    public void testStartWithPwdCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();
        String input = "pwd";
        String output = explorer.prompt.readLine(input);
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(lines.get(0).contains("Current directory :"));
        assertTrue(lines.get(0).contains(System.getProperty("user.dir")));
    }

    @Test
    public void testStartWithCDCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        File testDir = new File("/tmp/testDir");
        testDir.mkdir();
        explorer.rootPath = new File("/home/user/test");
        explorer.createFSPath();
        explorer.start();
        String input = "cd /tmp/testDir";
        explorer.prompt.readLine("cd /tmp/testDir");
        String output = explorer.prompt.readLine("pwd");
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(lines.get(0).contains("/tmp/testDir"));
        testDir.delete();
    }

    @Test
    public void testStartWithHelpCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();
        String input = "help";
        explorer.prompt.readLine("help");
        String output = explorer.prompt.readLine("");
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 50);
    }

    @Test
    public void testStartWithInvalidCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();
        String input = "xxx";
        String output = explorer.prompt.readLine(input);
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(lines.get(0).contains("Command 'xxx' not found."));
    }

    @Test
    public void testStartWithValidQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        File testFile = new File("/tmp/testFile.txt");
        try {
            testFile.createNewFile();
        } catch (IOException e) {
            // ignore
        }
        explorer.rootPath = new File("/home/user/test");
        explorer.createFSPath();
        explorer.start();
        String input = "//file[@name = 'testFile.txt']";
        explorer.prompt.readLine("pwd");
        explorer.prompt.readLine("cd /tmp");
        String output = explorer.prompt.readLine(input);
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(lines.get(0).contains(testFile.getAbsolutePath()));
        testFile.delete();
    }

    @Test
    public void testStartWithInvalidQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();
        String input = "//dir[@name = 'testFile.txt']";
        String output = explorer.prompt.readLine(input);
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(lines.get(0).contains("No such file or directory: /home/user/test/dir[@name = 'testFile.txt']"));
    }

    @Test
    public void testStartWithInvalidQueryWithDots() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();
        String input = "//dir[@name = './testFile.txt']";
        String output = explorer.prompt.readLine(input);
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(lines.get(0).contains("No such file or directory: /home/user/test/dir[@name = './testFile.txt']"));
    }

    @Test
    public void testStartWithValidQueryWithAttribute() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        File testDir = new File("/tmp/testDir");
        testDir.mkdir();
        File testFile = new File(testDir, "testFile.txt");
        try {
            testFile.createNewFile();
        } catch (IOException e) {
            // ignore
        }
        explorer.rootPath = new File("/home/user/test");
        explorer.createFSPath();
        explorer.start();
        String input = "//file[@name = 'testFile.txt'][size > 10]";
        explorer.prompt.readLine("pwd");
        explorer.prompt.readLine("cd /tmp");
        String output = explorer.prompt.readLine(input);
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(testFile.length() > 10);
        assertTrue(lines.get(0).contains(testFile.getAbsolutePath()));
        testFile.delete();
        testDir.delete();
    }

    @Test
    public void testStartWithInvalidQueryWithAttribute() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();
        String input = "//file[@name = 'testFile.txt'][size < 10]";
        String output = explorer.prompt.readLine(input);
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(lines.get(0).contains("No such file or directory: /home/user/test/file[@name = 'testFile.txt'][size < 10]"));
    }

    @Test
    public void testStartWithValidQueryWithMultipleWildcards() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        File testDir1 = new File("/tmp/testDir1");
        testDir1.mkdir();
        File testFile1 = new File(testDir1, "testFile1.txt");
        try {
            testFile1.createNewFile();
        } catch (IOException e) {
            // ignore
        }
        File testDir2 = new File("/tmp/testDir2");
        testDir2.mkdir();
        File testFile2 = new File(testDir2, "testFile2.txt");
        try {
            testFile2.createNewFile();
        } catch (IOException e) {
            // ignore
        }
        explorer.rootPath = new File("/home/user/test");
        explorer.createFSPath();
        explorer.start();
        String input = "//file[contains(name, 'testFile') and contains(name, '.txt')]";
        explorer.prompt.readLine("pwd");
        explorer.prompt.readLine("cd /tmp");
        String output = explorer.prompt.readLine(input);
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(lines.get(0).contains(testFile1.getAbsolutePath()));
        assertTrue(lines.get(0).contains(testFile2.getAbsolutePath()));
        testFile1.delete();
        testFile2.delete();
        testDir1.delete();
        testDir2.delete();
    }

    @Test
    public void testStartWithInvalidQueryWithMultipleWildcards() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();
        String input = "//file[contains(name, 'testFile') and contains(name, '.txt') and size < 10]";
        String output = explorer.prompt.readLine(input);
        List<String> lines = Arrays.asList(output.split("\n"));
        assertTrue(lines.size() > 0);
        assertTrue(lines.get(0).contains("No such file or directory: /home/user/test/file[contains(name, 'testFile') and contains(name, '.txt') and size < 10]"));
    }
}