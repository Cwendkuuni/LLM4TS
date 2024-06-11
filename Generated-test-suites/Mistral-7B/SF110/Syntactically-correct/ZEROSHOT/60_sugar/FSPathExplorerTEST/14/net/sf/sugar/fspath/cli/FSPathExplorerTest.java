package net.sf.sugar.fspath.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private String rootPath;
    private String fsPathQuery;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        rootPath = new File(".").getAbsolutePath();
        explorer.configureExplorer(new String[]{"-p", rootPath});
        explorer.createFSPath();
    }

    @Test
    public void testCreateFSPath() {
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testQueryEmptyFSPath() {
        explorer.fsPathQuery = "";
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(0, results.size());
    }

    @Test
    public void testQuerySingleFile() {
        File fileToTest = new File("testFile.txt");
        explorer.fsPathQuery = "file[@name = 'testFile.txt']";
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(1, results.size());
        FSPathResult expectedFile = results.get(0);
        assertEquals(fileToTest.getPath(), expectedFile.getFile().getPath());
    }

    @Test
    public void testQueryDirectory() {
        File directoryToTest = new File(".");
        explorer.fsPathQuery = "dir[@name = '.]";
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(1, results.size());
        FSPathResult expectedDirectory = results.get(0);
        assertEquals(directoryToTest.getPath(), expectedDirectory.getFile().getPath());
    }

    @Test
    public void testQueryMultipleFiles() {
        File file1ToTest = new File("file1.txt");
        File file2ToTest = new File("file2.txt");
        explorer.fsPathQuery = "file[@name = 'file1.txt'] | file[@name = 'file2.txt']";
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(2, results.size());
        FSPathResult expectedFile1 = results.get(0);
        assertEquals(file1ToTest.getPath(), expectedFile1.getFile().getPath());
        FSPathResult expectedFile2 = results.get(1);
        assertEquals(file2ToTest.getPath(), expectedFile2.getFile().getPath());
    }

    @Test
    public void testQueryNestedDirectory() {
        File nestedDirectory = new File("nestedDirectory");
        File fileInsideNestedDirectory = new File(nestedDirectory.getAbsolutePath() + "/testFile.txt");
        explorer.fsPathQuery = "dir[@name = 'nestedDirectory']/file[@name = 'testFile.txt']";
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(1, results.size());
        FSPathResult expectedFile = results.get(0);
        assertEquals(fileInsideNestedDirectory.getPath(), expectedFile.getFile().getPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryInvalidFSPath() {
        explorer.fsPathQuery = "file[@name = 'testFile.txt']/dir[@name = 'testDirectory']";
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        // An IllegalArgumentException should be thrown since a file cannot contain a directory
    }

    @After
    public void tearDown() {
        explorer = null;
    }
}