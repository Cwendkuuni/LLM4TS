package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * FSPathExplorer Test
 */
public class FSPathExplorerTest {

    private static final String TEST_DIRECTORY = "target/test-files";
    private static final String TEST_FILE_1 = TEST_DIRECTORY + "/testFile1.txt";
    private static final String TEST_FILE_2 = TEST_DIRECTORY + "/testFile2.txt";
    private static final String TEST_EMPTY_DIRECTORY = TEST_DIRECTORY + "/emptyDirectory";

    private File testDirectory;

    @Before
    public void setUp() throws IOException {
        testDirectory = new File(TEST_DIRECTORY);
        testDirectory.mkdirs();

        File testFile1 = new File(TEST_FILE_1);
        testFile1.createNewFile();

        File testFile2 = new File(TEST_FILE_2);
        testFile2.createNewFile();

        File emptyDirectory = new File(TEST_EMPTY_Directory);
        emptyDirectory.mkdir();
    }

    @After
    public void tearDown() throws IOException {
        if (testDirectory != null && testDirectory.exists()) {
            File[] files = testDirectory.listFiles();
            for (File file : files) {
                file.delete();
            }
            testDirectory.delete();
        }
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    /**
     * Test configureExplorer method with valid root path
     */
    @Test
    public void testConfigureExplorerWithValidRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("./");
        explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
        assertEquals(rootPath, explorer.rootPath);
    }

    /**
     * Test configureExplorer method with invalid root path
     */
    @Test(expected = IOException.class)
    public void testConfigureExplorerWithInvalidRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File invalidPath = new File("./invalidPath");
        explorer.configureExplorer(new String[]{"-p", invalidPath.getAbsolutePath()});
    }

    /**
     * Test createFSPath method with valid root path
     */
    @Test
    public void testCreateFSPathWithValidRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    /**
     * Test createFSPath method with null root path
     */
    @Test(expected = NullPointerException.class)
    public void testCreateFSPathWithNullRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath(null);
    }

    /**
     * Test start method with empty query
     */
    @Test
    public void testStartWithEmptyQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();
        assertFalse(explorer.prompt.isClosed());
    }

    /**
     * Test start method with valid query
     */
    @Test
    public void testStartWithValidQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", TEST_DIRECTORY.getAbsolutePath(), "-f", "//file"});
        explorer.createFSPath();
        explorer.start();
        String expectedResult = TEST_FILE_1 + " " + TEST_FILE_2;
        String actualResult = explorer.prompt.readLine();
        assertTrue(actualResult.contains(expectedResult));
    }

    /**
     * Test start method with invalid query
     */
    @Test
    public void testStartWithInvalidQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", TEST_DIRECTORY.getAbsolutePath()});
        explorer.createFSPath();
        explorer.start();
        String currentExpression = explorer.prompt.readLine();
        assertNull(currentExpression);
    }

    /**
     * Test pwd command
     */
    @Test
    public void testPwdCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", TEST_DIRECTORY.getAbsolutePath()});
        explorer.createFSPath();
        explorer.start();
        String expectedResult = TEST_DIRECTORY.getAbsolutePath();
        String actualResult = explorer.prompt.readLine().split(":")[1];
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test cd command with valid directory path
     */
    @Test
    public void testCdCommandWithValidDirectoryPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", TEST_DIRECTORY.getAbsolutePath()});
        explorer.createFSPath();
        explorer.start();
        String currentPath = TEST_DIRECTORY.getAbsolutePath();
        explorer.prompt.writeLine("cd testDirectory");
        String expectedResult = TEST_DIRECTORY.getAbsolutePath() + "/testDirectory";
        String actualResult = explorer.prompt.readLine().split(":")[1];
        assertEquals(expectedResult, actualResult);
        assertTrue(new File(expectedResult, "testFile1.txt").exists());
        assertTrue(new File(expectedResult, "testFile2.txt").exists());
    }

    /**
     * Test cd command with invalid directory path
     */
    @Test(expected = IOException.class)
    public void testCdCommandWithInvalidDirectoryPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", TEST_DIRECTORY.getAbsolutePath()});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine("cd invalidDirectory");
    }

    /**
     * Test help command
     */
    @Test
    public void testHelpCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", TEST_DIRECTORY.getAbsolutePath()});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine("help");
        String expectedResult = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\n";
        String actualResult = explorer.prompt.readLine();
        assertTrue(actualResult.contains(expectedResult));
    }

    /**
     * Test find command with empty query
     */
    @Test
    public void testFindCommandWithEmptyQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", TEST_DIRECTORY.getAbsolutePath()});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine("find //");
        String actualResult = explorer.prompt.readLine();
        assertNull(actualResult);
    }

    /**
     * Test find command with valid query
     */
    @Test
    public void testFindCommandWithValidQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", TEST_DIRECTORY.getAbsolutePath()});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine("find //file");
        List<File> expectedResults = List.of(new File(TEST_FILE_1), new File(TEST_FILE_2));
        List<File> actualResults = explorer.queryFiles();
        assertEquals(expectedResults.size(), actualResults.size());
        for (int i = 0; i < expectedResults.size(); i++) {
            assertEquals(expectedResults.get(i).getAbsolutePath(), actualResults.get(i).getAbsolutePath());
        }
    }

    /**
     * Test find command with invalid query
     */
    @Test
    public void testFindCommandWithInvalidQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", TEST_DIRECTORY.getAbsolutePath()});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine("find //invalidQuery");
        assertFalse(explorer.prompt.isClosed());
    }

    /**
     * Test find command with empty directory
     */
    @Test
    public void testFindCommandWithEmptyDirectory() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", TEST_EMPTY_DIRECTORY});
        explorer.createFSPath();
        explorer.start();
        explorer.prompt.writeLine("find //file");
        assertFalse(explorer.prompt.isClosed());
    }
}