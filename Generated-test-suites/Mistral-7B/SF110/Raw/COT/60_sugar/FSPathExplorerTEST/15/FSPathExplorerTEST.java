package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for {@link FSPathExplorer} class
 */
public class FSPathExplorerTest {

    private FSPathExplorer fsPathExplorer;
    private FSPath fsPath;

    @Before
    public void setUp() {
        fsPathExplorer = new FSPathExplorer();
        fsPath = FSPathFactory.newFSPath(new File("."));
        fsPathExplorer.createFSPath();
        fsPathExplorer.setFSPath(fsPath);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fsPathExplorer);
        assertEquals("", fsPathExplorer.getFsPathQuery());
        assertNull(fsPathExplorer.getRootPath());
        assertSame(System.out, fsPathExplorer.getOut());
        assertSame(System.err, fsPathExplorer.getErr());
    }

    @Test
    public void testConfigureExplorerWithValidArguments() throws IOException {
        String[] args = {"-p", "/path/to/root", "-f", "query"};
        fsPathExplorer.configureExplorer(args);
        assertEquals("/path/to/root", fsPathExplorer.getRootPath().getAbsolutePath());
        assertEquals("query", fsPathExplorer.getFsPathQuery());
        assertNull(fsPathExplorer.getOut());
        assertNull(fsPathExplorer.getErr());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testConfigureExplorerWithInvalidArguments() {
        String[] args = {"-p", "/path/to/root", "-f", "query"};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.configureExplorer(new String[0]);
    }

    @Test
    public void testCreateFSPathWithNullRootPath() {
        fsPathExplorer.createFSPath();
        assertNull(fsPathExplorer.getFSPath());
    }

    @Test
    public void testCreateFSPathWithValidRootPath() {
        File rootPath = new File("/path/to/root");
        fsPathExplorer.createFSPath(rootPath);
        assertSame(rootPath, fsPathExplorer.getRootPath());
        assertSame(FSPathFactory.newFSPath(rootPath), fsPathExplorer.getFSPath());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFSPathWithNullFile() {
        fsPathExplorer.createFSPath(null);
    }

    @Test
    public void testStartWithNoCommands() {
        fsPathExplorer.start();
        assertTrue(fsPathExplorer.isRunning());
        assertFalse(fsPathExplorer.prompt.isClosed());
    }

    @Test
    public void testStartWithValidCommands() throws IOException {
        fsPathExplorer.start();
        String command = "pwd";
        String expectedOutput = System.getProperty("user.dir");
        String output = fsPathExplorer.prompt.readLine();
        assertEquals(command, output);
        assertEquals(expectedOutput, fsPathExplorer.getFSPath().getRootDirectory().getAbsolutePath());

        command = "cd ./test";
        File testDirectory = new File("./test");
        fsPathExplorer.configureExplorer(new String[]{"-p", ".", "-f", command});
        output = fsPathExplorer.prompt.readLine();
        assertEquals(command, output);
        assertTrue(testDirectory.exists() && testDirectory.isDirectory() && testDirectory.canRead());
        assertSame(testDirectory, fsPathExplorer.getFSPath().getRootDirectory());

        command = "help";
        fsPathExplorer.prompt.readLine();
        output = fsPathExplorer.prompt.readLine();
        assertEquals(Commands.HELP.getCommandText(), output);
        assertEquals(18, output.split("\n").length);

        command = "exit";
        fsPathExplorer.prompt.readLine();
        fsPathExplorer.prompt.readLine();
        assertFalse(fsPathExplorer.isRunning());
        assertTrue(fsPathExplorer.prompt.isClosed());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testStartWithInvalidCommand() {
        fsPathExplorer.start();
        String command = "invalidCommand";
        fsPathExplorer.prompt.readLine();
        fsPathExplorer.prompt.readLine();
    }

    @Test
    public void testQueryWithEmptyFSPathQuery() throws IOException {
        String query = "";
        FSPathResultList results = fsPath.query(query);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryWithValidFSPathQuery() throws IOException {
        String query = "//dir[@name = 'test']";
        FSPathResultList results = fsPath.query(query);
        assertTrue(!results.isEmpty());
        for (FSPathResult result : results) {
            assertTrue(result.getFile().isDirectory());
            assertTrue(result.getFile().getName().equalsIgnoreCase("test"));
        }
    }

    @Test
    public void testQueryWithInvalidFSPathQuery() throws IOException {
        String query = "//file[@name = 'test']//dir";
        FSPathResultList results = fsPath.query(query);
        assertTrue(results.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryWithNullFSPathQuery() {
        String query = null;
        fsPath.query(query);
    }

    @Test(expected = IOException.class)
    public void testQueryWithInvalidPath() throws IOException {
        String query = "//dir[@name = 'test']/../..//file[name = 'test.txt']";
        fsPath = FSPathFactory.newFSPath(new File("/root"));
        FSPathResultList results = fsPath.query(query);
        assertFalse(results.isEmpty());
        for (FSPathResult result : results) {
            assertFalse(result.getFile().exists());
        }
    }
}