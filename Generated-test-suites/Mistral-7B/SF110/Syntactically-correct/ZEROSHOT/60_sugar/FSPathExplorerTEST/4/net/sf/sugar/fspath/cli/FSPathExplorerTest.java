package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

/**
 * Unit test for the FSPathExplorer class.
 */
public class FSPathExplorerTest {

    private FSPathExplorer explorer;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }

    @After
    public void tearDown() {
        // Close the input stream to prevent resource leaks
        explorer.prompt.close();
    }

    @Test
    public void testConfigurationWithValidArguments() throws IOException {
        String[] args = { "-p", "/tmp", "-f", "//dir[@name = 'test']/file" };
        explorer.configureExplorer(args);
        File rootPath = explorer.rootPath;
        assertEquals("/tmp", rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'test']/file", explorer.fsPathQuery);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationWithInvalidArgumentCount() {
        String[] args = { "-p", "/tmp" };
        explorer.configureExplorer(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationWithInvalidOption() {
        String[] args = { "-x", "/tmp", "-f", "//dir[@name = 'test']/file" };
        explorer.configureExplorer(args);
    }

    @Test
    public void testCreateFSPathWithNullRootPath() {
        explorer.createFSPath();
        assertNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithValidRootPath() throws IOException {
        String[] args = { "-p", "/tmp" };
        explorer.configureExplorer(args);
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testStartWithValidQuery() throws IOException {
        String[] args = { "-p", "/tmp", "-f", "//dir[@name = 'test']/file" };
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(1, results.size());
        File expectedFile = new File("/tmp/test/file");
        FSPathResult result = results.get(0);
        assertEquals(expectedFile.getPath(), result.getFile().getPath());
    }

    @Test
    public void testStartWithInvalidQuery() throws IOException {
        String[] args = { "-p", "/tmp" };
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(0, results.size());
    }

    @Test
    public void testStartWithEmptyQuery() throws IOException {
        String[] args = { "-p", "/tmp" };
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(0, results.size());
    }

    @Test
    public void testQuitCommand() throws IOException {
        String[] args = { "-p", "/tmp" };
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        String quitCommand = "exit";
        explorer.prompt.writeLine(quitCommand);
        explorer.prompt.readLine();
        // The prompt should have closed
        assertNull(explorer.prompt);
    }
}