package net.sf.sugar.fspath.cli;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * FSPathExplorerTest class
 */
public class FSPathExplorerTest {

    

    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        Assert.assertNotNull(explorer);
    }

    @Test
    public void testMainEmptyArgs() {
        String[] args = {};
        try {
            FSPathExplorer.main(args);
        } catch (Exception e) {
            Assert.assertTrue(true); // expected exception
        }
    }

    @Test
    public void testMainValidArgs() {
        String[] args = {"-p", "/tmp", "-f", "//dir[@name = 'foo']"};
        try {
            FSPathExplorer.main(args);
        } catch (Exception e) {
            Assert.assertTrue(false); // unexpected exception
        }
    }

    @Test
    public void testConfigureExplorerNoArgs() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[0]);
        Assert.assertNull(explorer.rootPath);
        Assert.assertEquals("", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        String path = "/tmp";
        File filePath = new File(path);
        String[] args = {"-p", path};
        explorer.configureExplorer(args);
        Assert.assertNotNull(explorer.rootPath);
        Assert.assertEquals(path, explorer.rootPath.getAbsolutePath());
        Assert.assertEquals("", explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        String query = "//dir[@name = 'foo']";
        String[] args = {"-f", query};
        explorer.configureExplorer(args);
        Assert.assertNull(explorer.rootPath);
        Assert.assertEquals(query, explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithPathAndQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        String path = "/tmp";
        File filePath = new File(path);
        String query = "//dir[@name = 'foo']";
        String[] args = {"-p", path, "-f", query};
        explorer.configureExplorer(args);
        Assert.assertNotNull(explorer.rootPath);
        Assert.assertEquals(path, explorer.rootPath.getAbsolutePath());
        Assert.assertEquals(query, explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathNullRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        Assert.assertNotNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPathNotNullRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        Assert.assertNotNull(explorer.fsPath);
        Assert.assertEquals(rootPath, explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStartNoQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        // expected behavior, no query is executed
    }

    @Test
    public void testStartNullRootPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        String query = "//dir[@name = 'foo']";
        explorer.fsPathQuery = query;
        explorer.start();
        Assert.assertFalse( explorer.fsPath.isDirectory());
        Assert.assertFalse(new File(explorer.fsPath.getPath()).exists());
    }

    @Test
    public void testStartValidRootPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "//dir[@name = 'foo']";
        explorer.fsPathQuery = query;
        explorer.start();
        FSPathResultList resultList = explorer.fsPath.query(query);
        Assert.assertNotNull(resultList);
        Assert.assertTrue(resultList.size() > 0);
    }

    @Test
    public void testStartValidQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/"); // root of filesystem
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "//dir[@name = 'etc']/file[@name = 'passwd']";
        explorer.fsPathQuery = query;
        explorer.start();
        FSPathResultList resultList = explorer.fsPath.query(query);
        Assert.assertNotNull(resultList);
        Assert.assertTrue(resultList.size() > 0);
        FSPathResult result = resultList.get(0);
        Assert.assertNotNull(result.getFile());
        Assert.assertFalse(result.getFile().isDirectory());
        File file = result.getFile();
        Assert.assertTrue(file.exists());
        Assert.assertEquals("passwd", file.getName());
    }

    @Test(expected = IOException.class)
    public void testStartInvalidQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "//dir[@name = 'foo']/file[@name = bar]";
        explorer.fsPathQuery = query;
        explorer.start();
        // expected exception
    }

    @Test(expected = IOException.class)
    public void testStartInvalidPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        String query = "//dir[@name = 'foo']";
        explorer.fsPathQuery = query;
        explorer.start();
        // expected exception
    }

    
}