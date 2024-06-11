package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.FSPathExplorer;

/**
 * Test class for FSPathExplorer.
 */
public class FSPathExplorerTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    /**
     * Test the main method.
     *
     * @throws IOException
     */
    @Test
    public void testMain() throws IOException {
        String[] args = new String[] { "-path", "/tmp" };
        FSPathExplorer.main(args);
    }

    /**
     * Test the showWelcome method.
     */
    @Test
    public void testShowWelcome() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showWelcome();
    }

    /**
     * Test the configureExplorer method.
     *
     * @throws IOException
     */
    @Test
    public void testConfigureExplorer() throws IOException {
        String[] args = new String[] { "-path", "/tmp", "-find", "*.txt" };
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertNotNull(explorer.getFsPath());
        assertEquals("/tmp", explorer.getFsPath().getRootDirectory().getAbsolutePath());
    }

    /**
     * Test the createFSPath method.
     *
     * @throws IOException
     */
    @Test
    public void testCreateFSPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.getFsPath());
    }

    /**
     * Test the start method.
     *
     * @throws IOException
     */
    @Test
    public void testStart() throws IOException {
        String[] args = new String[] { "-path", "/tmp", "-find", "*.txt" };
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();

        FSPath fsPath = explorer.getFsPath();
        FSPathResultList results = fsPath.query("*.txt");

        for (FSPathResult result : results) {
            assertNotNull(result.getFile());
            assertEquals("file", result.getType().name());
            assertEquals(FSPathAttributes.TYPE_FILE, result.getFile().getType());
        }
    }

    /**
     * Test invalid command.
     */
    @Test(expected = AssertionError.class)
    public void testInvalidCommand() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        explorer.prompt.readLine(); // consume initial prompt
        explorer.prompt.readLine("invalidCommand");
    }

    /**
     * Test empty query.
     */
    @Test
    public void testEmptyQuery() throws IOException {
        String[] args = new String[] { "-path", "/tmp" };
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.readLine(); // consume initial prompt
        explorer.prompt.readLine(); // consume empty query
        fail("Expected an error message for empty query");
    }

    /**
     * Test query with no results.
     *
     * @throws IOException
     */
    @Test
    public void testQueryNoResults() throws IOException {
        String[] args = new String[] { "-path", "/tmp", "-find", "nonexistentFile.txt" };
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.readLine(); // consume initial prompt
        explorer.prompt.readLine(); // consume query
        FSPathResultList results = explorer.getFsPath().query("nonexistentFile.txt");
        assertEquals(0, results.size());
    }

    /**
     * Test query with results.
     *
     * @throws IOException
     */
    @Test
    public void testQueryResults() throws IOException {
        String[] args = new String[] { "-path", "/tmp", "-find", "*.txt" };
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.readLine(); // consume initial prompt
        explorer.prompt.readLine(); // consume query
        FSPathResultList results = explorer.getFsPath().query("*.txt");
        assertEquals(1, results.size());
        FSPathResult result = results.get(0);
        File file = result.getFile();
        assertNotNull(file);
        assertEquals("file", result.getType().name());
        assertEquals(FSPathAttributes.TYPE_FILE, file.getType());
        assertEquals(".txt", file.getName().substring(file.getName().lastIndexOf(".")));
    }
}