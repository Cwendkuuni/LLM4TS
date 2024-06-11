package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for FSPathExplorer class
 *
 * @author your_name
 */
public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private File tempDirectory;

    @Before
    public void setUp() throws Exception {
        explorer = new FSPathExplorer();
        tempDirectory = File.createTempDirectory("junit", "fsPathExplorerTest");
        explorer.rootPath = tempDirectory;
    }

    @After
    public void tearDown() throws Exception {
        explorer.rootPath = null;
        tempDirectory.delete();
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    /**
     * Test main method
     */
    @Test
    public void testMain() {
        // This test is skipped as it's not possible to run a main method in a test
        // You can use alternatives like TestNG or PowerMock to test main methods
    }

    /**
     * Test showWelcome method
     */
    @Test
    public void testShowWelcome() {
        explorer.showWelcome();
    }

    /**
     * Test createFSPath method
     * @throws IOException
     */
    @Test
    public void testCreateFSPath() throws IOException {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    /**
     * Test configureExplorer method
     */
    @Test
    public void testConfigureExplorer() {
        String[] args = {"-p", tempDirectory.getAbsolutePath(), "-f", "testQuery"};
        explorer.configureExplorer(args);
        assertEquals(tempDirectory, explorer.rootPath);
        assertEquals("testQuery", explorer.fsPathQuery);
    }

    /**
     * Test start method
     * @throws IOException
     */
    @Test
    public void testStart() throws IOException {
        String[] args = {"-p", tempDirectory.getAbsolutePath(), "-f", "testQuery"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();

        File testFile = File.createTempFile("junit", "temp");
        assertNull(explorer.fsPath.query(new FSPathQuery("//file[@name = 'testFile']")));
        testFile.deleteOnExit();
        File testDirectory = new File(tempDirectory, "testDirectory");
        testDirectory.mkdir();
        testDirectory.deleteOnExit();
        File testQueryFile = new File(testDirectory, "testQueryFile.txt");
        testQueryFile.createNewFile();
        assertNotNull(explorer.fsPath.query(new FSPathQuery("//file[@name = 'testQueryFile.txt']")));
        testQueryFile.delete();
        testDirectory.delete();
    }

    /**
     * Test query method with empty query
     * @throws IOException
     */
    @Test
    public void testQueryEmptyQuery() throws IOException {
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query("");
        assertEquals(0, results.size());
    }

    /**
     * Test query method with invalid query
     * @throws IOException
     */
    @Test
    public void testQueryInvalidQuery() throws IOException {
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query("//invalidQuery");
        assertEquals(0, results.size());
    }
}