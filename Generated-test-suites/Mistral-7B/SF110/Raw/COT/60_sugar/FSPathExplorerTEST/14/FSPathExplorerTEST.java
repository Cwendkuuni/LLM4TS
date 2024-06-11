package net.sf.sugar.fspath.cli;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * FSPathExplorerTest.java
 *
 * Created on 04 April 2008, 16:36
 */

public class FSPathExplorerTest {

    

    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        Assert.assertNotNull(explorer);
    }

    @Test
    public void testMain() {
        String[] args = new String[]{"-path", "/path/to/root", "-find", "query"};
        FSPathExplorer.main(args);
    }

    @Test
    public void testShowWelcome() {
        String welcome = FSPathExplorer.showWelcome();
        Assert.assertNotNull(welcome);
        System.out.println(welcome);
    }

    @Test
    public void testCreateFSPathWithNullRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        Assert.assertNotNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithValidRootPath() {
        String rootPath = "/path/to/root";
        File fileRootPath = new File(rootPath);
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = fileRootPath;
        explorer.createFSPath();
        Assert.assertEquals(fileRootPath, explorer.fsPath.getRootDirectory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFSPathWithInvalidRootPath() {
        String rootPath = "invalid_root_path";
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = new File(rootPath);
        explorer.createFSPath();
    }

    @Test
    public void testQueryEmptyFSPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query("");
        Assert.assertEquals(0, results.size());
    }

    @Test
    public void testQueryValidFSPathQuery() throws IOException {
        String rootPath = "/path/to/root";
        String fsPathQuery = "//dir[@name = 'foo']";
        File fileRootPath = new File(rootPath);
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = fileRootPath;
        explorer.fsPathQuery = fsPathQuery;
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(fsPathQuery);
        Assert.assertNotNull(results);
        Assert.assertTrue(results.size() > 0);
    }

    @Test
    public void testQueryInvalidFSPathQuery() throws IOException {
        String rootPath = "/path/to/root";
        String fsPathQuery = "//dir[@name = 'foo']/bar";
        File fileRootPath = new File(rootPath);
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = fileRootPath;
        explorer.fsPathQuery = fsPathQuery;
        explorer.createFSPath();
        try {
            FSPathResultList results = explorer.fsPath.query(fsPathQuery);
            Assert.fail("An IOException should be thrown when querying an invalid FSPath query.");
        } catch (IOException e) {
            // Expected
        }
    }

    
}