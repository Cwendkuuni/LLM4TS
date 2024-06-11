package net.sf.sugar.fspath.cli; // Make sure to import necessary classes if needed

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.FSPathExplorer;

import org.junit.Before;
import org.junit.Test;

/**
 * FSPathExplorerTest.java
 *
 * Created on 04 April 2023, 16:36
 */
public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private FSPath fsPath;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        fsPath = FSPathFactory.newFSPath();
    }

    // Test for constructor
    @Test
    public void testFSPathExplorerConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    // Test for configureExplorer method
    @Test
    public void testConfigureExplorer() {
        String[] args = {"-path", "/tmp", "-find", "test.txt"};
        explorer.configureExplorer(args);
        assertEquals(new File("/tmp"), explorer.rootPath);
        assertEquals("test.txt", explorer.fsPathQuery);
    }

    // Test for createFSPath method
    @Test
    public void testCreateFSPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    // Test for start method with empty query
    @Test
    public void testStartEmptyQuery() {
        try {
            explorer.start();
            fail("Expected IOException but no exception thrown.");
        } catch (IOException e) {
            // Expected
        }
    }

    // Test for start method with valid query
    @Test
    public void testStartValidQuery() throws IOException {
        String query = "//dir[@name = 'test']";
        explorer.fsPathQuery = query;
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(query);
        assertEquals(1, results.size());
    }

    // Test for start method with query containing invalid expression
    @Test
    public void testStartInvalidQuery() {
        String query = "//dir[@name = 'test' @name = 'invalid'";
        explorer.fsPathQuery = query;
        try {
            explorer.start();
            fail("Expected IOException but no exception thrown.");
        } catch (IOException e) {
            // Expected
        }
    }

    // Test for start method with query containing file with non-existent path
    @Test
    public void testStartNonExistentPath() throws IOException {
        String query = "//file[@path = '/nonExistentPath/test.txt']";
        explorer.fsPathQuery = query;
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(query);
        assertEquals(0, results.size());
    }

    // Test for start method with query containing file with read-only permission
    @Test
    public void testStartReadOnlyFile() throws IOException {
        String query = "//file[@path = '/tmp/test.txt']";
        File testFile = new File("/tmp/test.txt");
        testFile.setReadable(false);
        explorer.rootPath = new File("/tmp");
        explorer.fsPathQuery = query;
        explorer.createFSPath();
        try {
            explorer.start();
            fail("Expected IOException but no exception thrown.");
        } catch (IOException e) {
            // Expected
            assertTrue(e.getMessage().contains("Permission denied"));
        }
    }

    // Test for start method with query containing file with write-only permission
    @Test
    public void testStartWriteOnlyFile() throws IOException {
        String query = "//file[@path = '/tmp/test.txt']";
        File testFile = new File("/tmp/test.txt");
        testFile.setWritable(false);
        explorer.rootPath = new File("/tmp");
        explorer.fsPathQuery = query;
        explorer.createFSPath();
        try {
            explorer.start();
            fail("Expected IOException but no exception thrown.");
        } catch (IOException e) {
            // Expected
            assertTrue(e.getMessage().contains("Permission denied"));
        }
    }

    // Test for start method with query containing file with execute-only permission
    @Test
    public void testStartExecuteOnlyFile() throws IOException {
        String query = "//file[@path = '/tmp/test.txt']";
        File testFile = new File("/tmp/test.txt");
        testFile.setExecutable(false);
        explorer.rootPath = new File("/tmp");
        explorer.fsPathQuery = query;
        explorer.createFSPath();
        try {
            explorer.start();
            fail("Expected IOException but no exception thrown.");
        } catch (IOException e) {
            // Expected
            assertTrue(e.getMessage().contains("Permission denied"));
        }
    }

    // Test for start method with query containing hidden file
    @Test
    public void testStartHiddenFile() throws IOException {
        String query = "//file[@path = '.', '@hidden = true']";
        explorer.fsPathQuery = query;
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(query);
        assertEquals(1, results.size());
    }

    // Test for start method with query containing directory
    @Test
    public void testStartDirectory() throws IOException {
        String query = "//dir[@path = '/tmp']";
        explorer.fsPathQuery = query;
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(query);
        assertEquals(1, results.size());
    }

    // Test for start method with query containing file with specified size
    @Test
    public void testStartFileWithSize() throws IOException {
        String query = "//file[@path = '/tmp/test.txt', '@size = 10']";
        explorer.fsPathQuery = query;
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(query);
        if (results.size() > 0) {
            File file = results.get(0).getFile();
            assertEquals(10, file.length());
        } else {
            fail("Expected at least one result.");
        }
    }

    // Test for start method with query containing file with specified attribute
    @Test
    public void testStartFileWithAttribute() throws IOException {
        String query = "//file[@path = '/tmp/test.txt', '@isDirectory = false', '@isFile = true']";
        explorer.fsPathQuery = query;
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(query);
        if (results.size() > 0) {
            File file = results.get(0).getFile();
            assertFalse(file.isDirectory());
            assertTrue(file.isFile());
        } else {
            fail("Expected at least one result.");
        }
    }

    // Test for start method with query containing file with specified name
    @Test
    public void testStartFileWithName() throws IOException {
        String query = "//file[@name = 'test.txt']";
        explorer.fsPathQuery = query;
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(query);
        if (results.size() > 0) {
            File file = results.get(0).getFile();
            assertEquals("test.txt", file.getName());
        } else {
            fail("Expected at least one result.");
        }
    }
}