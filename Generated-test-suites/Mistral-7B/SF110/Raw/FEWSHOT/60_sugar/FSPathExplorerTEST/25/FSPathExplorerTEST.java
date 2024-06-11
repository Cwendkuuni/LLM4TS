package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testConfigureExplorer() {
        String[] args = new String[] { "-p", "/tmp", "-find" };
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        File expectedRootPath = new File("/tmp");
        assertEquals(expectedRootPath, explorer.rootPath);
        assertNull(explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithEmptyPathQuery() {
        String[] args = new String[] { "-path", "/tmp", "-find" };
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        File expectedRootPath = new File("/tmp");
        assertEquals(expectedRootPath, explorer.rootPath);
        assertNull(explorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithNonEmptyPathQuery() {
        String[] args = new String[] { "-path", "/tmp", "-find", "test" };
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        File expectedRootPath = new File("/tmp");
        assertEquals(expectedRootPath, explorer.rootPath);
        assertEquals("test", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPathWithNullRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithRootPath() {
        File rootPath = new File("/tmp");
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        assertEquals(rootPath, explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStartWithEmptyQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        String expectedPromptText = "fspath>";
        assertEquals(expectedPromptText, explorer.prompt.getPromptText());
        String expectedResult = "/";
        assertEquals(expectedResult, explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testStartWithQueryThatMatchesFilesAndDirectories() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "//file[@size > 100]";
        explorer.fsPathQuery = query;
        explorer.start();
        List<FSPathResult> results = explorer.fsPath.query(query);
        int expectedSize = 1 + (int) Math.floor(Math.random() * 10);
        assertEquals(expectedSize, results.size());
    }

    @Test
    public void testStartWithQueryThatMatchesOnlyDirectories() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "//dir[fs:matches(@name, 'test')]";
        explorer.fsPathQuery = query;
        explorer.start();
        List<FSPathResult> results = explorer.fsPath.query(query);
        int expectedSize = 1 + (int) Math.floor(Math.random() * 10);
        assertEquals(expectedSize, results.size());
        File testDir = new File(Paths.get("/tmp", "test").toFile().getPath());
        for (FSPathResult result : results) {
            File file = result.getFile();
            assertTrue(file.isDirectory());
            assertEquals(testDir.getAbsolutePath(), file.getAbsolutePath());
        }
    }

    @Test
    public void testStartWithQueryThatMatchesOnlyFiles() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "//file[contains(@name, '.txt')]";
        explorer.fsPathQuery = query;
        explorer.start();
        List<FSPathResult> results = explorer.fsPath.query(query);
        int expectedSize = 1 + (int) Math.floor(Math.random() * 10);
        assertEquals(expectedSize, results.size());
        for (FSPathResult result : results) {
            File file = result.getFile();
            assertTrue(file.isFile());
            assertEquals(file.getName().endsWith(".txt"), true);
        }
    }

    @Test
    public void testStartWithQueryThatDoesNotMatchAnything() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "//file[@size = 12345]";
        explorer.fsPathQuery = query;
        explorer.start();
        List<FSPathResult> results = explorer.fsPath.query(query);
        assertEquals(0, results.size());
    }

    @Test
    public void testStartWithQueryThatMatchesFilesAndDirectoriesAndNothingElse() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "//dir[@name = 'test']//file[contains(@name, '.txt')]";
        explorer.fsPathQuery = query;
        explorer.start();
        List<FSPathResult> results = explorer.fsPath.query(query);
        int expectedSize = 1 + (int) Math.floor(Math.random() * 10);
        assertEquals(expectedSize, results.size());
        for (FSPathResult result : results) {
            File file = result.getFile();
            assertTrue(file.isFile());
            assertEquals(file.getName().endsWith(".txt"), true);
            File parent = file.getParentFile();
            File testDir = new File(parent.getAbsolutePath() + "/test");
            assertTrue(testDir.exists());
            assertTrue(testDir.isDirectory());
        }
    }

    @Test
    public void testStartWithQueryThatMatchesDirectoriesAndFilesAndNothingElse() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "//dir[@name = 'test']//file[@size > 1000]";
        explorer.fsPathQuery = query;
        explorer.start();
        List<FSPathResult> results = explorer.fsPath.query(query);
        int expectedSize = 1 + (int) Math.floor(Math.random() * 10);
        assertEquals(expectedSize, results.size());
        File testDir = new File(Paths.get("/tmp", "test").toFile().getPath());
        for (FSPathResult result : results) {
            File file = result.getFile();
            assertTrue(file.isFile());
            assertEquals(file.getName().endsWith(".txt"), false);
            assertEquals(file.getParentFile().getAbsolutePath(), testDir.getAbsolutePath());
            File parent = file.getParentFile();
            File testDir = new File(parent.getAbsolutePath());
            assertTrue(testDir.exists());
            assertTrue(testDir.isDirectory());
        }
    }
}