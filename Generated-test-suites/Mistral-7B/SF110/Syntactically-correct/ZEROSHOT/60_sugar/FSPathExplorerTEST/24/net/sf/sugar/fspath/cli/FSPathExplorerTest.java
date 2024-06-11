package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

/**
 * Test class for FSPathExplorer.
 */
public class FSPathExplorerTest {

    private static FSPathExplorer explorer;

    @BeforeClass
    public static void setUpBeforeClass() {
        explorer = new FSPathExplorer();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        explorer = null;
    }

    /**
     * Test of main method, of class FSPathExplorer.
     */
    @Test
    public void testMain() {
        String[] args = {};
        FSPathExplorer.main(args);
        // add your test code here
        fail("The test case is empty.");
    }

    /**
     * Test of configureExplorer method, of class FSPathExplorer.
     */
    @Test
    public void testConfigureExplorer() {
        String[] args = {"-path", "/tmp"};
        explorer.configureExplorer(args);
        assertEquals(new File("/tmp"), explorer.rootPath);
        args = new String[]{"-find", "test.txt"};
        explorer.fsPathQuery = "";
        explorer.configureExplorer(args);
        assertEquals("test.txt", explorer.fsPathQuery);
    }

    /**
     * Test of createFSPath method, of class FSPathExplorer.
     */
    @Test
    public void testCreateFSPath() {
        explorer.rootPath = null;
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);

        explorer.rootPath = new File("/tmp");
        explorer.createFSPath();
        assertEquals(explorer.rootPath, explorer.fsPath.getRootDirectory());
    }

    /**
     * Test of start method, of class FSPathExplorer.
     */
    @Test
    public void testStart() throws IOException {
        explorer.rootPath = new File("/tmp");
        explorer.createFSPath();
        explorer.start();

        String command = "pwd";
        explorer.prompt.sendInput(command);
        String actual = explorer.prompt.readLine();
        assertEquals("/tmp", actual);

        command = "cd test";
        explorer.prompt.sendInput(command);
        File testDir = new File("/tmp/test");
        if (!testDir.exists()) {
            testDir.mkdir();
        }
        actual = explorer.prompt.readLine();
        assertTrue(actual.contains("changed to : " + testDir.getAbsolutePath()));

        command = "cd ..";
        explorer.prompt.sendInput(command);
        actual = explorer.prompt.readLine();
        assertTrue(actual.contains("changed to : " + explorer.rootPath.getAbsolutePath()));

        command = "help";
        explorer.prompt.sendInput(command);
        String helpText = explorer.prompt.readAll();
        assertTrue(helpText.contains("FSPathExplorer help :"));

        command = "exit";
        explorer.prompt.sendInput(command);
        explorer.prompt.close();
    }

    /**
     * Test of query method, of class FSPath.
     */
    @Test
    public void testQuery() throws IOException {
        explorer.rootPath = new File("/tmp");
        explorer.createFSPath();
        File testFile = new File("/tmp/testFile.txt");
        if (!testFile.exists()) {
            testFile.createNewFile();
        }
        String query = "//file[@name = 'testFile.txt']";
        FSPathResultList results = explorer.fsPath.query(query);
        assertEquals(1, results.size());
        assertTrue(results.get(0).getFile().getPath().equals(testFile.getPath()));
    }

    /**
     * Test of findFirst method, of class FSPath.
     */
    @Test
    public void testFindFirst() throws IOException {
        explorer.rootPath = new File("/tmp");
        explorer.createFSPath();
        File testFile = new File("/tmp/testFile.txt");
        if (!testFile.exists()) {
            testFile.createNewFile();
        }
        String query = "//file[@name = 'testFile.txt']";
        FSPathResult result = explorer.fsPath.findFirst(query);
        assertNotNull(result);
        assertEquals(testFile.getPath(), result.getFile().getPath());

        query = "//file[@name = 'testFile.notExist']";
        result = explorer.fsPath.findFirst(query);
        assertNull(result);
    }
}