package net.sf.sugar.fspath.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FSPathExplorerTest {

    private static final String TEST_DATA_DIRECTORY = "target/test-classes/testData";
    private FSPathExplorer fsPathExplorer;

    @Before
    public void setUp() {
        fsPathExplorer = new FSPathExplorer();
    }

    @After
    public void tearDown() {
        fsPathExplorer = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(fsPathExplorer);
    }

    @Test
    public void testMainWithValidArguments() {
        String[] args = {"-p", TEST_DATA_DIRECTORY};
        FSPathExplorer.main(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainWithInvalidArguments() {
        String[] args = {};
        FSPathExplorer.main(args);
    }

    @Test
    public void testShowWelcome() {
        fsPathExplorer.showWelcome();
    }

    @Test
    public void testCreateFSPath() throws IOException {
        fsPathExplorer.createFSPath();
        File currentRoot = fsPathExplorer.getFSPath().getRootDirectory();
        assertNotNull(currentRoot);
        assertEquals(TEST_DATA_DIRECTORY, currentRoot.getAbsolutePath());
    }

    @Test
    public void testQueryEmptyDirectory() throws IOException {
        fsPathExplorer.fsPathQuery = "";
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryFile() throws IOException {
        fsPathExplorer.createFSPath();
        fsPathExplorer.fsPathQuery = "//file[@name = 'test.txt']";
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        assertEquals(1, results.size());
        File expectedFile = new File(TEST_DATA_DIRECTORY + File.separator + "test.txt");
        FSPathResult expectedResult = results.get(0);
        assertEquals(expectedFile.getAbsolutePath(), expectedResult.getFile().getAbsolutePath());
    }

    @Test
    public void testQueryDirectory() throws IOException {
        fsPathExplorer.createFSPath();
        fsPathExplorer.fsPathQuery = "//dir[@name = 'test']";
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        assertEquals(1, results.size());
        File expectedDirectory = new File(TEST_DATA_DIRECTORY + File.separator + "test");
        FSPathResult expectedResult = results.get(0);
        assertEquals(expectedDirectory.getAbsolutePath(), expectedResult.getFile().getAbsolutePath());
    }

    @Test
    public void testQueryFileUnderDirectory() throws IOException {
        fsPathExplorer.createFSPath();
        fsPathExplorer.fsPathQuery = "//dir[@name = 'test']//file[@name = 'testFile.txt']";
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        assertEquals(1, results.size());
        File expectedFile = new File(TEST_DATA_DIRECTORY + File.separator + "test" + File.separator + "testFile.txt");
        FSPathResult expectedResult = results.get(0);
        assertEquals(expectedFile.getAbsolutePath(), expectedResult.getFile().getAbsolutePath());
    }

    @Test
    public void testQueryFileWithNameMatching() throws IOException {
        fsPathExplorer.createFSPath();
        fsPathExplorer.fsPathQuery = "//file[@name = '*test*']";
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        List<File> testFiles = List.of(
                new File(TEST_DATA_DIRECTORY + File.separator + "test.txt"),
                new File(TEST_DATA_DIRECTORY + File.separator + "testDir" + File.separator + "testFile.txt")
        );
        for (File file : testFiles) {
            FSPathResult result = null;
            for (FSPathResult fsPathResult : results) {
                if (file.getAbsolutePath().equals(fsPathResult.getFile().getAbsolutePath())) {
                    result = fsPathResult;
                    break;
                }
            }
            assertNotNull(result);
        }
    }

    @Test
    public void testQueryFileWithSizeMatching() throws IOException {
        fsPathExplorer.createFSPath();
        fsPathExplorer.fsPathQuery = "//file[@size = 10]";
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        File smallFile = new File(TEST_DATA_DIRECTORY + File.separator + "small.txt");
        assertEquals(1, results.size());
        FSPathResult expectedResult = results.get(0);
        assertEquals(smallFile.getAbsolutePath(), expectedResult.getFile().getAbsolutePath());
    }

    @Test
    public void testQueryFileWithCustomAttributeMatching() throws IOException {
        fsPathExplorer.createFSPath();
        fsPathExplorer.fsPathQuery = "//file[@hidden = 'false']";
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        File visibleFile = new File(TEST_DATA_DIRECTORY + File.separator + "visible.txt");
        assertEquals(1, results.size());
        FSPathResult expectedResult = results.get(0);
        assertEquals(visibleFile.getAbsolutePath(), expectedResult.getFile().getAbsolutePath());
    }

    @Test
    public void testQueryDirectoryWithCustomAttributeMatching() throws IOException {
        fsPathExplorer.createFSPath();
        fsPathExplorer.fsPathQuery = "//dir[@readable = 'true']";
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        File readableDirectory = new File(TEST_DATA_DIRECTORY + File.separator + "readable");
        assertEquals(1, results.size());
        FSPathResult expectedResult = results.get(0);
        assertEquals(readableDirectory.getAbsolutePath(), expectedResult.getFile().getAbsolutePath());
    }

    @Test
    public void testQueryEmptyQuery() throws IOException {
        fsPathExplorer.createFSPath();
        fsPathExplorer.fsPathQuery = "";
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryNullQuery() {
        fsPathExplorer.createFSPath();
        fsPathExplorer.fsPathQuery = null;
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryInvalidQuery() throws IOException {
        fsPathExplorer.createFSPath();
        fsPathExplorer.fsPathQuery = "//invalidQuery";
        FSPathResultList results = fsPathExplorer.getFSPath().query(fsPathExplorer.fsPathQuery);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSetRootDirectory() throws IOException {
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        String testDirectoryPath = TEST_DATA_DIRECTORY;
        File testDirectory = new File(testDirectoryPath);
        File tempDirectory = new File(tempDirectoryPath);
        fsPathExplorer.createFSPath();
        fsPathExplorer.setRootDirectory(testDirectory);
        File currentRoot = fsPathExplorer.getFSPath().getRootDirectory();
        assertEquals(testDirectory.getAbsolutePath(), currentRoot.getAbsolutePath());
        fsPathExplorer.setRootDirectory(tempDirectory);
        currentRoot = fsPathExplorer.getFSPath().getRootDirectory();
        assertEquals(tempDirectory.getAbsolutePath(), currentRoot.getAbsolutePath());
    }

    @Test
    public void testSetRootDirectoryWithNullDirectory() {
        fsPathExplorer.createFSPath();
        fsPathExplorer.setRootDirectory(null);
        File currentRoot = fsPathExplorer.getFSPath().getRootDirectory();
        assertNull(currentRoot);
    }

    private FSPath getFSPath() {
        return fsPathExplorer.getFSPath();
    }

}