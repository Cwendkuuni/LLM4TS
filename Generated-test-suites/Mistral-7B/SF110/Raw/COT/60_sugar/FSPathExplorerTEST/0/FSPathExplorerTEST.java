package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for FSPathExplorer.
 */
public class FSPathExplorerTest {
    // 

    private FSPathExplorer fsPathExplorer;
    private File rootPath;

    @Before
    public void setUp() {
        fsPathExplorer = new FSPathExplorer();
        rootPath = new File("."); // use current directory as root path
        fsPathExplorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
        fsPathExplorer.createFSPath();
    }

    /**
     * Test method for {@link FSPathExplorer#FSPathExplorer()}.
     */
    @Test
    public void testFSPathExplorerConstructor() {
        // no action required
    }

    /**
     * Test method for {@link FSPathExplorer#configureExplorer(String[])}.
     */
    @Test
    public void testConfigureExplorer() {
        File rootPath = new File("./src/test/resources"); // setup a test directory
        String[] args = {"-p", rootPath.getAbsolutePath()};

        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.configureExplorer(args);
        assertEquals(rootPath, fsPathExplorer.rootPath);
    }

    /**
     * Test method for {@link FSPathExplorer#createFSPath()}.
     */
    @Test
    public void testCreateFSPath() {
        FSPath fsPath = fsPathExplorer.fsPath;
        assertEquals(rootPath, fsPath.getRootDirectory());
    }

    /**
     * Test method for {@link FSPathExplorer#start()}.
     */
    @Test
    public void testStart() throws IOException {
        String query = "//dir[@name = 'test']";
        fsPathExplorer.fsPathQuery = query;

        FSPathResultList resultList = fsPathExplorer.fsPath.query(query);

        assertTrue(resultList.size() > 0);

        for (FSPathResult fsPathResult : resultList) {
            assertEquals(new File(fsPathResult.getFile().getAbsolutePath()), new File("test"));
        }
    }

    /**
     * Test method for {@link FSPathExplorer#start()} with empty query.
     */
    @Test(expected = IOException.class)
    public void testStartEmptyQuery() throws IOException {
        fsPathExplorer.fsPathQuery = "";
        fsPathExplorer.start();
    }

    /**
     * Test method for {@link FSPathExplorer#start()} with null query.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStartNullQuery() {
        fsPathExplorer.fsPathQuery = null;
        fsPathExplorer.start();
    }

    /**
     * Test method for {@link FSPathExplorer#start()} with invalid query.
     */
    @Test(expected = IOException.class)
    public void testStartInvalidQuery() throws IOException {
        fsPathExplorer.fsPathQuery = "//invalid";
        fsPathExplorer.start();
    }

    /**
     * Test method for {@link FSPathExplorer#start()} with empty directory query.
     */
    @Test(expected = IOException.class)
    public void testStartEmptyDirectoryQuery() throws IOException {
        String query = "//dir[@name = '']";
        fsPathExplorer.fsPathQuery = query;
        fsPathExplorer.start();
    }

    /**
     * Test method for {@link FSPathExplorer#start()} with non-existent directory query.
     */
    @Test(expected = IOException.class)
    public void testStartNonExistentDirectoryQuery() throws IOException {
        String query = "//dir[@name = 'nonExistent']";
        fsPathExplorer.fsPathQuery = query;
        fsPathExplorer.start();
    }

    // 
}