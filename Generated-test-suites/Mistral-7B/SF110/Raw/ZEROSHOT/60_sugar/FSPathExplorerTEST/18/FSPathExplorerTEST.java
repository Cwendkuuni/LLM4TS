package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

/**
 * Tests for FSPathExplorer class
 */
public class FSPathExplorerTest {

    private FSPathExplorer explorer;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        assertNotNull(explorer);
        assertNull(explorer.getRootPath());
        assertNull(explorer.getFsPath());
        assertNull(explorer.getPrompt());
        assertNull(explorer.getOut());
        assertNull(explorer.getErr());
    }

    /**
     * Test main method
     */
    @Test
    public void testMain() throws IOException, InterruptedException {
        String[] args = { "-path", "/tmp" };
        FSPathExplorer.main(args);
        assertNotNull(explorer.getRootPath());
        File rootPath = explorer.getRootPath();
        assertEquals("/tmp", rootPath.getAbsolutePath());
        assertNotNull(explorer.getFsPath());
        FSPath fsPath = explorer.getFsPath();
        assertTrue(fsPath instanceof FSPath);
        assertNotNull(explorer.getPrompt());
        assertNotNull(explorer.getOut());
        assertNotNull(explorer.getErr());
    }

    /**
     * Test configureExplorer method
     */
    @Test
    public void testConfigureExplorer() {
        String[] args = { "-path", "/tmp" };
        explorer.configureExplorer(args);
        File rootPath = explorer.getRootPath();
        assertEquals("/tmp", rootPath.getAbsolutePath());
        String query = "//file[name='test.txt']";
        explorer.configureExplorer(new String[]{"-find", query});
        assertEquals(query, explorer.getFsPathQuery());
    }

    /**
     * Test createFSPath method
     */
    @Test
    public void testCreateFSPath() throws IOException {
        String[] args = { "-path", "/tmp" };
        explorer.configureExplorer(args);
        explorer.createFSPath();
        FSPath fsPath = explorer.getFsPath();
        assertTrue(fsPath instanceof FSPath);
    }

    /**
     * Test start method
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testStart() throws IOException, InterruptedException {
        String[] args = { "-path", "/tmp" };
        explorer.configureExplorer(args);
        explorer.createFSPath();
        String query = "//file[name='test.txt']";
        explorer.configureExplorer(new String[]{"-find", query});
        explorer.start();
        List<FSPathResult> results = explorer.getFsPath().getQueryResults();
        assertEquals(1, results.size());
        FSPathResult result = results.get(0);
        File file = result.getFile();
        assertTrue(file.exists() && file.isFile());
        assertEquals("test.txt", file.getName());
        String pwd = explorer.getPrompt().readLine();
        assertEquals(Commands.PWD.getCommandText(), pwd);
        explorer.getPrompt().readLine(); //read empty line
        explorer.getPrompt().readLine(); //read 'exit' command
    }

    /**
     * Test invalid query
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testInvalidQuery() throws IOException, InterruptedException {
        String[] args = { "-path", "/tmp" };
        explorer.configureExplorer(args);
        explorer.createFSPath();
        String query = "//file[name='test.txt']//";
        explorer.configureExplorer(new String[]{"-find", query});
        explorer.start();
        List<FSPathResult> results = explorer.getFsPath().getQueryResults();
        assertEquals(0, results.size());
    }

    /**
     * Test empty query
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testEmptyQuery() throws IOException, InterruptedException {
        String[] args = { "-path", "/tmp" };
        explorer.configureExplorer(args);
        explorer.createFSPath();
        String query = "";
        explorer.configureExplorer(new String[]{"-find", query});
        explorer.start();
        List<FSPathResult> results = explorer.getFsPath().getQueryResults();
        assertEquals(0, results.size());
    }

    /**
     * Test invalid option
     */
    @Test
    public void testInvalidOption() {
        String[] args = { "-invalidOption", "/tmp" };
        try {
            explorer.configureExplorer(args);
            fail("Invalid option should throw an exception");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test no rootPath option
     */
    @Test
    public void testNoRootPathOption() {
        String[] args = { "-find", "//file[name='test.txt']" };
        try {
            explorer.configureExplorer(args);
            fail("No rootPath option should throw an exception");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test no find option
     */
    @Test
    public void testNoFindOption() {
        String[] args = { "-path", "/tmp" };
        try {
            explorer.configureExplorer(args);
            fail("No find option should throw an exception");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test null rootPath
     */
    @Test
    public void testNullRootPath() {
        String[] args = { "-path", null };
        try {
            explorer.configureExplorer(args);
            fail("Null rootPath should throw an exception");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    /**
     * Test null find query
     */
    @Test
    public void testNullFindQuery() {
        String[] args = { "-find", null };
        try {
            explorer.configureExplorer(args);
            fail("Null find query should throw an exception");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }
}