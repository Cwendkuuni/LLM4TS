package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

/**
 * FSPathExplorerTest.java
 *
 * Test class for FSPathExplorer
 *
 * @author your_name
 */
public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private FSPath fsPath;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        fsPath = FSPathFactory.newFSPath();
    }

    @Test
    public void testEmptyConstructor() {
        FSPathExplorer emptyConstructor = new FSPathExplorer();
        assertNull(emptyConstructor.getRootPath());
        assertNull(emptyConstructor.getFsPathQuery());
        assertNull(emptyConstructor.getFsPath());
        assertNull(emptyConstructor.getPrompt());
        assertNull(emptyConstructor.getOut());
        assertNull(emptyConstructor.getErr());
    }

    @Test
    public void testConstructorWithArgs() {
        File rootPath = new File("/");
        String fsPathQuery = "//dir[@name = 'test']";
        FSPathExplorer constructorWithArgs = new FSPathExplorer(rootPath, fsPathQuery);
        assertEquals(rootPath, constructorWithArgs.getRootPath());
        assertEquals(fsPathQuery, constructorWithArgs.getFsPathQuery());
        assertNotNull(constructorWithArgs.getFsPath());
        assertNotNull(constructorWithArgs.getPrompt());
        assertNotNull(constructorWithArgs.getOut());
        assertNotNull(constructorWithArgs.getErr());
    }

    @Test
    public void testCreateFSPath() throws IOException {
        explorer.createFSPath();
        assertTrue(explorer.getFsPath() instanceof FSPath);
    }

    @Test
    public void testConfigureExplorerWithEmptyArgs() {
        String[] args = new String[0];
        explorer.configureExplorer(args);
        assertNull(explorer.getRootPath());
        assertNull(explorer.getFsPathQuery());
    }

    @Test
    public void testConfigureExplorerWithValidArgs() {
        File rootPath = new File("/");
        String fsPathQuery = "//dir[@name = 'test']";
        String[] args = {"-p", rootPath.getAbsolutePath(), "-f", fsPathQuery};
        explorer.configureExplorer(args);
        assertEquals(rootPath, explorer.getRootPath());
        assertEquals(fsPathQuery, explorer.getFsPathQuery());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testConfigureExplorerWithInvalidArgs() {
        String[] args = {"-p", "/test"};
        explorer.configureExplorer(args);
    }

    @Test
    public void testCreateFSPathWithNullRootPath() {
        explorer.setRootPath(null);
        explorer.createFSPath();
        assertNull(explorer.getFsPath());
    }

    @Test
    public void testCreateFSPathWithEmptyRootPath() {
        explorer.setRootPath(new File(""));
        explorer.createFSPath();
        assertNull(explorer.getFsPath());
    }

    @Test
    public void testCreateFSPathWithAbsoluteRootPath() throws IOException {
        File rootPath = new File("/");
        explorer.setRootPath(rootPath);
        explorer.createFSPath();
        assertTrue(explorer.getFsPath() instanceof FSPath);
    }

    @Test
    public void testCreateFSPathWithRelativeRootPath() throws IOException {
        File rootPath = new File(".");
        explorer.setRootPath(rootPath);
        explorer.createFSPath();
        assertTrue(explorer.getFsPath() instanceof FSPath);
    }

    @Test
    public void testStartWithEmptyQuery() {
        explorer.start();
        assertTrue(explorer.isRunning());
    }

    @Test
    public void testStartWithValidQuery() {
        String fsPathQuery = "//dir[@name = 'test']";
        explorer.setFsPathQuery(fsPathQuery);
        explorer.start();
        assertTrue(explorer.isRunning());
    }

    @Test(expected = IOException.class)
    public void testStartWithInvalidQuery() throws IOException {
        String fsPathQuery = "//dir[@name =";
        explorer.setFsPathQuery(fsPathQuery);
        explorer.start();
    }

    @Test
    public void testQueryEmptyDirectory() throws IOException {
        File testDir = new File("./test");
        if (!testDir.exists()) {
            testDir.mkdir();
        }
        explorer.setRootPath(testDir);
        explorer.setFsPathQuery("//");
        FSPathResultList resultList = explorer.queryFSPath();
        assertEquals(0, resultList.size());
    }

    @Test
    public void testQueryFile() throws IOException {
        File testFile = new File("./test/testFile.txt");
        if (!testFile.exists()) {
            testFile.createNewFile();
        }
        explorer.setRootPath(new File("./test"));
        explorer.setFsPathQuery("//file[@name = 'testFile.txt']");
        FSPathResultList resultList = explorer.queryFSPath();
        assertEquals(1, resultList.size());
        FSPathResult result = resultList.get(0);
        assertEquals(testFile.getAbsolutePath(), result.getFile().getAbsolutePath());
    }

    @Test
    public void testQueryDirectory() throws IOException {
        File testDir = new File("./test");
        if (!testDir.exists()) {
            testDir.mkdir();
        }
        explorer.setRootPath(new File("./test"));
        explorer.setFsPathQuery("//dir[@name = 'test']");
        FSPathResultList resultList = explorer.queryFSPath();
        assertEquals(1, resultList.size());
        FSPathResult result = resultList.get(0);
        assertEquals(testDir.getAbsolutePath(), result.getFile().getAbsolutePath());
    }

    @Test
    public void testQueryMultipleMatches() throws IOException {
        File testDir1 = new File("./test1");
        File testDir2 = new File("./test2");
        if (!testDir1.exists()) {
            testDir1.mkdir();
        }
        if (!testDir2.exists()) {
            testDir2.mkdir();
        }
        explorer.setRootPath(new File("./test"));
        explorer.setFsPathQuery("//dir[@name = 'test']");
        FSPathResultList resultList = explorer.queryFSPath();
        assertEquals(2, resultList.size());
        FSPathResult result1 = resultList.get(0);
        assertEquals(testDir1.getAbsolutePath(), result1.getFile().getAbsolutePath());
        FSPathResult result2 = resultList.get(1);
        assertEquals(testDir2.getAbsolutePath(), result2.getFile().getAbsolutePath());
    }

    @Test
    public void testQueryNoMatches() throws IOException {
        String fsPathQuery = "//dir[@name = 'nonexistent']";
        explorer.setRootPath(new File("."));
        explorer.setFsPathQuery(fsPathQuery);
        FSPathResultList resultList = explorer.queryFSPath();
        assertEquals(0, resultList.size());
    }

    @Test
    public void testQueryWithInvalidPath() throws IOException {
        String fsPathQuery = "//@name = 'test'";
        explorer.setRootPath(new File("."));
        explorer.setFsPathQuery(fsPathQuery);
        FSPathResultList resultList = explorer.queryFSPath();
        assertEquals(0, resultList.size());
    }

    @Test
    public void testQueryWithInvalidAttribute() throws IOException {
        String fsPathQuery = "//file[@attribute = 'test']";
        explorer.setRootPath(new File("."));
        explorer.setFsPathQuery(fsPathQuery);
        FSPathResultList resultList = explorer.queryFSPath();
        assertEquals(0, resultList.size());
    }

    @Test
    public void testQueryWithInvalidCombination() throws IOException {
        String fsPathQuery = "//file[@name = 'test']/@length > 10";
        explorer.setRootPath(new File("."));
        explorer.setFsPathQuery(fsPathQuery);
        FSPathResultList resultList = explorer.queryFSPath();
        assertEquals(0, resultList.size());
    }

    @Test
    public void testQueryWithInvalidFileSize() throws IOException {
        String fsPathQuery = "//file[@name = 'test']/@length > 10M";
        explorer.setRootPath(new File("."));
        explorer.setFsPathQuery(fsPathQuery);
        FSPathResultList resultList = explorer.queryFSPath();
        assertEquals(0, resultList.size());
    }

    @Test
    public void testQueryWithInvalidRegularExpression() throws IOException {
        String fsPathQuery = "//file[@name = '^test']";
        explorer.setRootPath(new File("."));
        explorer.setFsPathQuery(fsPathQuery);
        FSPathResultList resultList = explorer.queryFSPath();
        assertEquals(0, resultList.size());
    }

    @Test
    public void testQuitCommand() {
        explorer.start();
        explorer.executeCommand(Commands.QUIT.getCommandText());
        assertFalse(explorer.isRunning());
    }

    @Test
    public void testCdCommand() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File testDir = new File(tempDir, "test");
        File testFile = new File(testDir, "testFile.txt");
        if (!testDir.exists()) {
            testDir.mkdir();
        }
        if (!testFile.exists()) {
            testFile.createNewFile();
        }
        explorer.setRootPath(new File("."));
        explorer.start();
        explorer.executeCommand(Commands.CD.getCommandText() + " " + testDir.getAbsolutePath());
        assertEquals(testDir.getAbsolutePath(), explorer.getCurrentDirectory());
        explorer.executeCommand(Commands.CD.getCommandText() + " ../");
        assertEquals(".", explorer.getCurrentDirectory());
    }

    @Test
    public void testPwdCommand() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File testDir = new File(tempDir, "test");
        File testFile = new File(testDir, "testFile.txt");
        if (!testDir.exists()) {
            testDir.mkdir();
        }
        if (!testFile.exists()) {
            testFile.createNewFile();
        }
        explorer.setRootPath(new File("."));
        explorer.start();
        explorer.executeCommand(Commands.CD.getCommandText() + " " + testDir.getAbsolutePath());
        explorer.executeCommand(Commands.PWD.getCommandText());
        assertEquals(testDir.getAbsolutePath(), explorer.getOutput());
        explorer.executeCommand(Commands.CD.getCommandText() + " .");
        explorer.executeCommand(Commands.PWD.getCommandText());
        assertEquals(".", explorer.getOutput());
    }

    @Test
    public void testHelpCommand() {
        explorer.start();
        explorer.executeCommand(Commands.HELP.getCommandText());
        assertTrue(explorer.getOutput().contains("FSPathExplorer help :"));
    }

}