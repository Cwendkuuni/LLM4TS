package net.sf.sugar.fspath.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private File testDir;
    private File testFile1;
    private File testFile2;

    @Before
    public void setUp() throws Exception {
        testDir = new File("target/test-classes/fsPathExplorerTest");
        Files.createDirectories(testDir.toPath());
        testFile1 = new File(testDir, "testFile1.txt");
        testFile2 = new File(testDir, "testFile2.txt");
        explorer = new FSPathExplorer();
    }

    @After
    public void tearDown() throws Exception {
        if (testDir.exists()) {
            Files.deleteRecursively(testDir.toPath());
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(explorer);
    }

    @Test
    public void testMain() {
        String[] args = {"--path", testDir.getAbsolutePath()};
        explorer.main(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRootPath() {
        String[] args = {};
        explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
    }

    @Test
    public void testSetRootPath() {
        String[] args = {"--path", testDir.getAbsolutePath()};
        explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertNotNull(explorer.getRootPath());
        assertEquals(testDir, explorer.getRootPath());
    }

    @Test
    public void testQueryEmpty() throws IOException {
        String query = "";
        List<FSPathResult> results = explorer.query(query);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQuerySingleFile() throws IOException {
        testFile1.createNewFile();
        String query = "//file[name = 'testFile1.txt']";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(testFile1, results.get(0).getFile());
    }

    @Test
    public void testQueryNonExistingPath() throws IOException {
        String query = "//dir[@name = 'does_not_exist']";
        List<FSPathResult> results = explorer.query(query);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryMultipleFiles() throws IOException {
        testFile1.createNewFile();
        testFile2.createNewFile();
        String query = "//file[name = 'testFile1.txt' or name = 'testFile2.txt']";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(2, results.size());
        assertNotNull(results.get(0).getFile());
        assertNotNull(results.get(1).getFile());
        assertEquals(testFile1, results.get(0).getFile());
        assertEquals(testFile2, results.get(1).getFile());
    }

    @Test
    public void testQuerySubDirectory() throws IOException {
        File subDir = new File(testDir, "subDir");
        subDir.mkdir();
        testFile1 = new File(subDir, "testFile1.txt");
        testFile1.createNewFile();
        String query = "//dir[@name = 'subDir']//file[name = 'testFile1.txt']";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(testFile1, results.get(0).getFile());
    }

    @Test
    public void testQueryParentDirectory() throws IOException {
        File parentDir = testDir.getParentFile();
        String query = "..";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(parentDir, results.get(0).getFile());
    }

    @Test(expected = IOException.class)
    public void testQueryInvalidPath() throws Exception {
        String query = "//invalid_path_query";
        explorer.query(query);
    }

    @Test
    public void testQueryEmptyDirectory() throws IOException {
        String query = "//dir[@name = '']";
        List<FSPathResult> results = explorer.query(query);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryRootDirectory() throws IOException {
        String query = "//";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(testDir, results.get(0).getFile());
    }

    @Test
    public void testQueryAttributes() throws IOException {
        testFile1.createNewFile();
        testFile1.setExecutable(true, false);
        String query = "//file[name = 'testFile1.txt' and isExecutable() = 'true']";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(testFile1, results.get(0).getFile());
    }

    @Test
    public void testQueryAttributesMultipleFiles() throws IOException {
        testFile1.createNewFile();
        testFile1.setExecutable(true, false);
        testFile2.createNewFile();
        String query = "//file[name = 'testFile1.txt' or name = 'testFile2.txt' and isExecutable() = 'true']";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(2, results.size());
        assertNotNull(results.get(0).getFile());
        assertNotNull(results.get(1).getFile());
        assertEquals(testFile1, results.get(0).getFile());
        assertEquals(testFile2, results.get(1).getFile());
    }

    @Test
    public void testQueryAttributesNonExistingPath() throws IOException {
        String query = "//dir[@name = 'does_not_exist']/file[isExecutable() = 'true']";
        List<FSPathResult> results = explorer.query(query);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryAttributesFileAttributes() throws IOException {
        testFile1.createNewFile();
        testFile1.setLastModified(System.currentTimeMillis() - 3600000L);
        String query = "//file[name = 'testFile1.txt' and size() > 1024 and lastModified() < -1h]";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(testFile1, results.get(0).getFile());
    }

    @Test
    public void testQueryAttributesDirectoryAttributes() throws IOException {
        testDir.mkdir();
        String query = "//dir[@name = 'testDir'] and isDirectory() = 'true'";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(testDir, results.get(0).getFile());
    }

    @Test
    public void testQueryAttributesMultipleAttributes() throws IOException {
        testFile1.createNewFile();
        testFile1.setLastModified(System.currentTimeMillis() - 3600000L);
        testFile1.setExecutable(true, false);
        String query = "//file[name = 'testFile1.txt' and isExecutable() = 'true' and size() > 1024 and lastModified() < -1h]";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(testFile1, results.get(0).getFile());
    }

    @Test
    public void testQueryAttributesWithParentheses() throws IOException {
        testFile1.createNewFile();
        testFile1.setLastModified(System.currentTimeMillis() - 3600000L);
        String query = "//file[(name = 'testFile1.txt') and (size() > 1024 and lastModified() < -1h)]";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(testFile1, results.get(0).getFile());
    }

    @Test
    public void testQueryAttributesWithQuotes() throws IOException {
        testFile1.createNewFile();
        String query = "//file[name = 'testFile1.txt' and size() > 1024 and lastModified() < -1h]";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(testFile1, results.get(0).getFile());
    }

    @Test
    public void testQueryAttributesWithSpaces() throws IOException {
        testFile1.createNewFile();
        String query = "//file[name = 'test File1.txt' and size() > 1024 and lastModified() < -1h]";
        List<FSPathResult> results = explorer.query(query);
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getFile());
        assertEquals(testFile1, results.get(0).getFile());
    }
}