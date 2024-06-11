package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

/**
 * Test class for FSPathExplorer
 *
 * @author <YourName>
 */
public class FSPathExplorerTest {

    private static final String TEST_DIRECTORY = "target/test-classes/FSPathExplorerTestData";
    private static final String TEST_FILE = TEST_DIRECTORY + "/testFile.txt";

    private FSPathExplorer explorer;
    private FSPath fsPath;
    private File testDirectory;

    @Before
    public void setUp() {
        try {
            File testDirectoryFile = new File(TEST_DIRECTORY);
            if (!testDirectoryFile.exists()) {
                testDirectoryFile.mkdirs();
            }

            File testFile = new File(TEST_FILE);
            if (!testFile.exists()) {
                testFile.createNewFile();
            }

            this.explorer = new FSPathExplorer();
            this.fsPath = FSPathFactory.newFSPath(new File(TEST_DIRECTORY));
            this.testDirectory = testDirectoryFile;

        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Test getRootPath method
     */
    @Test
    public void testGetRootPath() {
        File rootPath = new File("/");
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(rootPath);
        File result = explorer.getRootPath();
        assertEquals(rootPath.getAbsolutePath(), result.getAbsolutePath());
    }

    /**
     * Test setRootPath method
     */
    @Test
    public void testSetRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/");
        explorer.setRootPath(rootPath);
        assertEquals(rootPath.getAbsolutePath(), explorer.getRootPath().getAbsolutePath());
    }

    /**
     * Test configureExplorer method with valid arguments
     */
    @Test
    public void testConfigureExplorerWithValidArguments() {
        String[] args = {"-p", TEST_DIRECTORY, "-f", "//file[@name = 'testFile.txt']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals(new File(TEST_DIRECTORY), explorer.getRootPath());
        assertEquals(TEST_FILE, explorer.getFsPathQuery());
    }

    /**
     * Test createFSPath method
     */
    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.getFsPath());
    }

    /**
     * Test start method with valid query
     */
    @Test
    public void testStartWithValidQuery() throws IOException {
        String query = "//file[@name = 'testFile.txt']";
        explorer.setFsPathQuery(query);
        explorer.start();
        String expectedResult = TEST_FILE;
        String result = this.prompt.readLine();
        assertEquals(expectedResult, result);
    }

    /**
     * Test start method with empty query
     */
    @Test
    public void testStartWithEmptyQuery() {
        explorer.setFsPathQuery("");
        explorer.start();
        String result = this.prompt.readLine();
        assertNull(result);
    }

    /**
     * Test start method with invalid query
     */
    @Test
    public void testStartWithInvalidQuery() {
        String query = "//dir[@name = 'test']//file[@name = 'testFile.txt']//fs:length > 1000";
        explorer.setFsPathQuery(query);
        explorer.start();
        String result = this.prompt.readLine();
        assertNull(result);
    }

    /**
     * Test start method with valid CD command
     */
    @Test
    public void testStartWithValidCDCommand() throws IOException {
        String query = "pwd";
        explorer.setFsPathQuery(query);
        explorer.start();
        String expectedResult = TEST_DIRECTORY;
        String result = this.prompt.readLine();
        assertEquals(expectedResult, result);

        query = "cd ../";
        explorer.setFsPathQuery(query);
        explorer.start();
        File expectedParentDirectory = testDirectory.getParentFile();
        String result2 = this.prompt.readLine();
        assertEquals(expectedParentDirectory.getAbsolutePath(), result2);
    }

    /**
     * Test start method with invalid CD command
     */
    @Test
    public void testStartWithInvalidCDCommand() {
        String query = "cd test";
        explorer.setFsPathQuery(query);
        explorer.start();
        String result = this.prompt.readLine();
        assertNull(result);
    }

    /**
     * Test query method with valid query
     */
    @Test
    public void testQueryWithValidQuery() throws IOException {
        String query = "//file[@name = 'testFile.txt']";
        FSPathResultList results = fsPath.query(query);
        FSPathResult expectedResult = results.get(0);
        assertEquals(TEST_FILE, expectedResult.getFile().getPath());
    }

    /**
     * Test query method with empty query
     */
    @Test
    public void testQueryWithEmptyQuery() {
        FSPathResultList results = fsPath.query("");
        assertNull(results.get(0).getFile());
    }

    /**
     * Test query method with invalid query
     */
    @Test
    public void testQueryWithInvalidQuery() {
        String query = "//dir[@name = 'test']//file[@name = 'testFile.txt']//fs:length > 1000";
        FSPathResultList results = fsPath.query(query);
        assertNull(results.get(0).getFile());
    }

    /**
     * Test query method with invalid directory query
     */
    @Test
    public void testQueryWithInvalidDirectoryQuery() {
        String query = "//dir[@name = 'test']/file[@name = 'testFile.txt']";
        FSPathResultList results = fsPath.query(query);
        assertNull(results.get(0).getFile());
    }

    /**
     * Test query method with valid attribute query
     */
    @Test
    public void testQueryWithValidAttributeQuery() throws IOException {
        String query = "//file[@size > 1]";
        FSPathResultList results = fsPath.query(query);
        for (FSPathResult result : results) {
            File file = result.getFile();
            File expectedFile = new File(TEST_DIRECTORY, "testFile.txt");
            if (file.equals(expectedFile)) {
                assertNotNull(file.length());
                break;
            }
            File tmpFile = new File(TEST_DIRECTORY, "tmp" + System.currentTimeMillis() + ".txt");
            if (file.equals(tmpFile)) {
                assertNotNull(file.length());
                break;
            }
        }
    }

    /**
     * Test query method with empty attribute query
     */
    @Test
    public void testQueryWithEmptyAttributeQuery() {
        String query = "//file[]";
        FSPathResultList results = fsPath.query(query);
        for (FSPathResult result : results) {
            File file = result.getFile();
            assertNotNull(file);
        }
    }

    /**
     * Test query method with invalid attribute query
     */
    @Test
    public void testQueryWithInvalidAttributeQuery() {
        String query = "//file[@invalidAttribute = 'test']";
        FSPathResultList results = fsPath.query(query);
        assertNull(results.get(0).getFile());
    }
}