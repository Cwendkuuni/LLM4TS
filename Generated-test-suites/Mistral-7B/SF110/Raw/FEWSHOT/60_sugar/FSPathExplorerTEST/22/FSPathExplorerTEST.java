package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testCreateFSPathExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    @Test
    public void testConfigureExplorerWithRootPath() {
        String rootPath = "/tmp";
        String[] args = {"-p", rootPath};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals(new File(rootPath), explorer.rootPath);
    }

    @Test
    public void testConfigureExplorerWithFSPathQuery() {
        String fsPathQuery = "//dir[@name = 'foo']";
        String[] args = {"-f", fsPathQuery};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals(fsPathQuery, explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithBothRootPathAndFSPathQuery() {
        String rootPath = "/tmp";
        String fsPathQuery = "//dir[@name = 'foo']";
        String[] args = {"-p", rootPath, "-f", fsPathQuery};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals(new File(rootPath), explorer.rootPath);
        assertEquals(fsPathQuery, explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        FSPath fsPath = explorer.fsPath;
        assertNotNull(fsPath);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        File rootPath = new File("/tmp");
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        FSPath fsPath = explorer.fsPath;
        assertEquals(rootPath, fsPath.getRootDirectory());
    }

    @Test
    public void testStartWithEmptyQueryAndPWDCommand() throws IOException {
        String line = "pwd";
        FSPathExplorer explorer = new FSPathExplorer();
        String currentDirectory = explorer.prompt.readLine(line);
        assertEquals(System.getProperty("user.dir"), currentDirectory);
    }

    @Test
    public void testStartWithEmptyQueryAndEmptyFSPathQuery() throws IOException {
        String line = "//dir[@name = '']";
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPathQuery = line;
        FSPathResultList results = explorer.fsPath.query(line);
        assertNull(results);
    }

    @Test
    public void testStartWithEmptyQueryAndValidFSPathQuery() throws IOException {
        String line = "//dir[@name = 'foo']";
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPathQuery = line;
        FSPathResultList results = explorer.fsPath.query(line);
        assertNotNull(results);
    }

    @Test
    public void testStartWithEmptyQueryAndInvalidFSPathQuery() throws IOException {
        String line = "//dir[@name = 'foo']//file[length = 1000000000]";
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.fsPathQuery = line;
        FSPathResultList results = explorer.fsPath.query(line);
        assertNull(results);
    }

    @Test
    public void testStartWithEmptyQueryAndHelpCommand() throws IOException {
        String line = "help";
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        explorer.prompt.readLine(line);
    }

    @Test
    public void testStartWithEmptyQueryAndCDCommand() throws IOException {
        File newDir = new File("/tmp/test");
        if (!newDir.exists()) {
            newDir.mkdir();
        }
        String line = "cd /tmp/test";
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        explorer.prompt.readLine(line);
        File currentDirectory = explorer.fsPath.getRootDirectory();
        assertEquals(newDir, currentDirectory);
    }

    @Test
    public void testStartWithEmptyQueryAndQuitCommand() throws IOException {
        String line = "exit";
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        explorer.prompt.readLine(line);
    }
}