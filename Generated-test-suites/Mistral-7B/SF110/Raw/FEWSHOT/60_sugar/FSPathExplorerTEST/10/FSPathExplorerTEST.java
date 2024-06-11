package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testCreateFSPathExplorerInstance() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNull(explorer.getRootPath());
        assertNull(explorer.getFsPathQuery());
        assertEquals(System.out, explorer.getOut());
        assertEquals(System.err, explorer.getErr());
        assertEquals(new Prompt(System.in, explorer.getOut(), explorer.getErr()), explorer.getPrompt());
    }

    @Test
    public void testConfigureExplorerWithValidArgs() {
        String[] args = {"-p", "/tmp", "-f", "//dir[@name = 'foo']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        File expectedRootPath = new File("/tmp");
        File actualRootPath = explorer.getRootPath();
        assertEquals(expectedRootPath, actualRootPath);
        assertEquals("//dir[@name = 'foo']", explorer.getFsPathQuery());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithMissingPathArg() {
        String[] args = {"-f", "//dir[@name = 'foo']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertNull(explorer.getRootPath());
        assertNull(explorer.getFsPathQuery());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithMissingFindArg() {
        String[] args = {"-p", "/tmp"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        File expectedRootPath = new File("/tmp");
        File actualRootPath = explorer.getRootPath();
        assertEquals(expectedRootPath, actualRootPath);
        assertNull(explorer.getFsPathQuery());
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.setRootPath(rootPath);
        FSPath fsPath = explorer.createFSPath();
        assertEquals(rootPath, fsPath.getRootDirectory());
    }

    @Test
    public void testQueryEmptyFSPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(null);
        explorer.createFSPath();
        String query = "";
        FSPathResultList results = explorer.query(query);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQuerySimpleFSPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.setRootPath(rootPath);
        explorer.createFSPath();
        String query = "//dir[@name = 'foo']";
        FSPathResultList results = explorer.query(query);
        List<String> expectedResults = new ArrayList<>();
        expectedResults.addAll(Arrays.asList(
                rootPath.getPath() + File.separator + "foo",
                rootPath.getPath() + File.separator + "foo" + File.separator + "subdir1" + File.separator + "file1.txt",
                rootPath.getPath() + File.separator + "foo" + File.separator + "subdir2" + File.separator + "file2.txt"
        ));
        for (FSPathResult result : results) {
            assertEquals(expectedResults.remove(0), result.getFile().getAbsolutePath());
        }
        assertTrue(expectedResults.isEmpty());
    }

    @Test
    public void testQueryNonExistingFSPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/tmp"));
        explorer.createFSPath();
        String query = "//nonexistingdir[@name = 'foo']";
        FSPathResultList results = explorer.query(query);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryWithWildcards() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/var/www/html");
        explorer.setRootPath(rootPath);
        explorer.createFSPath();
        String query = "//file[contains(@name, '.htm') or contains(@name, '.html')]";
        FSPathResultList results = explorer.query(query);
        List<String> expectedResults = new ArrayList<>();
        expectedResults.addAll(Arrays.asList(
                rootPath.getPath() + File.separator + "index.html",
                rootPath.getPath() + File.separator + "about.html",
                rootPath.getPath() + File.separator + "contact.htm",
                rootPath.getPath() + File.separator + "error.html"
        ));
        for (FSPathResult result : results) {
            assertEquals(expectedResults.remove(0), result.getFile().getAbsolutePath());
        }
        assertTrue(expectedResults.isEmpty());
    }

    @Test
    public void testQueryWithDirectory() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/var/www/html");
        explorer.setRootPath(rootPath);
        explorer.createFSPath();
        String query = "//dir[@name = 'css']";
        FSPathResultList results = explorer.query(query);
        assertEquals(1, results.size());
        FSPathResult expectedResult = results.get(0);
        File expectedFile = new File(rootPath.getAbsolutePath(), "css");
        assertEquals(expectedFile, expectedResult.getFile());
    }

    @Test
    public void testQueryWithMultipleMatches() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/var/www/html");
        explorer.setRootPath(rootPath);
        explorer.createFSPath();
        String query = "//file[@name = 'about.*']";
        FSPathResultList results = explorer.query(query);
        List<String> expectedResults = new ArrayList<>();
        expectedResults.addAll(Arrays.asList(
                rootPath.getPath() + File.separator + "about.html",
                rootPath.getPath() + File.separator + "about.htm"
        ));
        for (FSPathResult result : results) {
            assertEquals(expectedResults.remove(0), result.getFile().getAbsolutePath());
        }
        assertTrue(expectedResults.isEmpty());
    }

}