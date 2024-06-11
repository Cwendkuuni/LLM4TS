package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private File rootPath;

    @Before
    public void setUp() {
        rootPath = new File("."); // Set the current directory as the root path
        explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        explorer.createFSPath();
    }

    @Test
    public void testConstructor() {
        assertNotNull(explorer);
    }

    @Test
    public void testMainWithCorrectArguments() {
        // TODO: Implement test case for this method
    }

    @Test
    public void testCheckArgs() {
        // TODO: Implement test case for this method
    }

    @Test
    public void testShowWelcome() {
        // TODO: Implement test case for this method
    }

    @Test
    public void testShowHelp() {
        // TODO: Implement test case for this method
    }

    @Test
    public void testConfigureExplorerWithValidArguments() {
        String[] args = {"-path", "/tmp", "-find", "test.txt"};
        explorer.configureExplorer(args);
        assertEquals("/tmp", explorer.rootPath.getAbsolutePath());
        assertEquals("test.txt", explorer.fsPathQuery);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithMissingPathArgument() {
        String[] args = {"-find", "test.txt"};
        explorer.configureExplorer(args);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testCreateFSPathWithNullRootPath() {
        explorer.rootPath = null;
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithEmptyRootPath() {
        explorer.rootPath = new File("");
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFSPathWithNonExistentRootPath() {
        File nonExistentRootPath = new File("/nonexistent");
        explorer.rootPath = nonExistentRootPath;
        explorer.createFSPath();
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testStartWithEmptyQuery() {
        String output = "";
        String currentExpression = "";
        try {
            explorer.start();
            currentExpression = explorer.prompt.readLine();
            output += currentExpression;
            currentExpression = explorer.prompt.readLine();
            output += currentExpression;
            assertEquals("", output);
        } catch (IOException e) {
            fail("IOException expected");
        }
    }

    @Test
    public void testStartWithValidQuery() {
        String query = "//dir[@name = 'bin']";
        String output = "";
        String currentExpression = "";
        try {
            explorer.fsPathQuery = query;
            explorer.start();
            currentExpression = explorer.prompt.readLine();
            output += currentExpression;
            currentExpression = explorer.prompt.readLine();
            output += currentExpression;
            FSPathResultList expectedResults = fsPathQuery(query, rootPath);
            for (FSPathResult result : expectedResults) {
                assertTrue(output.contains(result.getFile().getPath()));
            }
        } catch (IOException e) {
            fail("IOException expected");
        }
    }

    @Test
    public void testStartWithInvalidQuery() {
        String query = "//dir[@name = 'bin']/file[@length > 1000000000]";
        String output = "";
        String currentExpression = "";
        try {
            explorer.fsPathQuery = query;
            explorer.start();
            currentExpression = explorer.prompt.readLine();
            assertEquals("Invalid query: ", currentExpression.substring(0, 15));
            currentExpression = explorer.prompt.readLine();
            assertEquals("", output);
        } catch (IOException e) {
            fail("IOException expected");
        }
    }

    @Test
    public void testFsPathQueryWithValidQueryAndExistingFile() {
        String query = "//file[@name = 'test.java']";
        FSPathResultList expectedResults = fsPathQuery(query, rootPath);
        FSPathResult result = expectedResults.get(0);
        File testFile = new File(result.getFile().getPath());
        assertTrue(testFile.exists());
    }

    @Test
    public void testFsPathQueryWithValidQueryAndNonExistentFile() {
        String query = "//file[@name = 'nonexistent.java']";
        FSPathResultList expectedResults = fsPathQuery(query, rootPath);
        assertEquals(0, expectedResults.size());
    }

    @Test
    public void testFsPathQueryWithValidQueryAndDirectory() {
        String query = "//dir[@name = 'bin']";
        FSPathResultList expectedResults = fsPathQuery(query, rootPath);
        assertEquals(1, expectedResults.size());
        File binDirectory = new File(expectedResults.get(0).getFile().getPath());
        assertTrue(binDirectory.isDirectory());
    }

    @Test
    public void testFsPathQueryWithValidQueryAndMultipleResults() {
        String query = "//file[@name = 'test*.txt']";
        FSPathResultList expectedResults = fsPathQuery(query, rootPath);
        assertEquals(2, expectedResults.size());
    }

    @Test
    public void testFsPathQueryWithInvalidQuery() {
        String query = "//file[@name = 'test.txt]/file[@name = 'test.txt']";
        FSPathResultList expectedResults = fsPathQuery(query, rootPath);
        assertEquals(0, expectedResults.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFsPathQueryWithNullOrEmptyQuery() {
        String query = null;
        FSPathResultList results = explorer.fsPath.query(query);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IOException.class)
    public void testFsPathQueryWithInvalidPath() {
        String query = "//dir[@name = 'bin']/file[@name = 'test.java']/../..";
        FSPathResultList results = explorer.fsPath.query(query);
        fail("IOException expected");
    }

    private FSPathResultList fsPathQuery(String query, File rootPath) {
        FSPath fsPath = FSPathFactory.newFSPath(rootPath);
        FSPathResultList results = fsPath.query(query);
        return results;
    }
}