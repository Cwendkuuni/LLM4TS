package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private File workingDirectory;

    @Before
    public void setUp() throws Exception {
        workingDirectory = new File(System.getProperty("user.dir"));
        explorer = new FSPathExplorer();
        explorer.setWorkingDirectory(workingDirectory);
    }

    @After
    public void tearDown() throws Exception {
        explorer = null;
    }

    @Test
    public void testEmptyQuery() {
        explorer.createFSPath();
        FSPathResultList resultList = explorer.queryFSPath("");
        assertEquals(0, resultList.size());
    }

    @Test
    public void testSimpleQuery() {
        explorer.createFSPath();
        File testFile = new File(workingDirectory, "test.txt");
        testFile.createNewFile();
        FSPathResultList resultList = explorer.queryFSPath("//file[@name = 'test.txt']");
        assertEquals(1, resultList.size());
        FSPathResult result = resultList.get(0);
        assertEquals(testFile.getAbsolutePath(), result.getPath().toString());
        testFile.delete();
    }

    @Test
    public void testQueryWithDirectory() {
        explorer.createFSPath();
        File testDir = new File(workingDirectory, "testDir");
        testDir.mkdir();
        File testFile = new File(testDir, "test.txt");
        testFile.createNewFile();
        FSPathResultList resultList = explorer.queryFSPath("//dir[@name = 'testDir']//file[@name = 'test.txt']");
        assertEquals(1, resultList.size());
        FSPathResult result = resultList.get(0);
        assertEquals(testFile.getAbsolutePath(), result.getPath().toString());
        testDir.delete();
        testFile.delete();
    }

    @Test
    public void testQueryWithAttribute() {
        explorer.createFSPath();
        File testFile = new File(workingDirectory, "test.txt");
        testFile.createNewFile();
        FSPathResultList resultList = explorer.queryFSPath("//file[fs:size() > 1]");
        assertEquals(1, resultList.size());
        FSPathResult result = resultList.get(0);
        assertEquals(testFile.getAbsolutePath(), result.getPath().toString());
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testInvalidQuery() throws IOException {
        explorer.createFSPath();
        FSPathResultList resultList = explorer.queryFSPath("//dir[@name = 'testDir']//file[@name = 'test.txt'");
        fail("Expected IOException but got " + resultList.size() + " results.");
    }

    @Test
    public void testHelpCommand() {
        explorer.createFSPath();
        String helpOutput = explorer.executeCommand("help");
        String expectedHelpOutput = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\nAttributes : (see javadoc for java.io.File for more info)\n";
        assertEquals(expectedHelpOutput, helpOutput);
    }

    @Test
    public void testPwdCommand() {
        explorer.createFSPath();
        String pwdOutput = explorer.executeCommand("pwd");
        String expectedPwdOutput = workingDirectory.getAbsolutePath();
        assertEquals(expectedPwdOutput, pwdOutput);
    }

    @Test
    public void testCdCommand() {
        explorer.createFSPath();
        File testDir = new File(workingDirectory, "testDir");
        testDir.mkdir();
        explorer.changeWorkingDirectory("testDir");
        String pwdOutput = explorer.executeCommand("pwd");
        String expectedPwdOutput = testDir.getAbsolutePath();
        assertEquals(expectedPwdOutput, pwdOutput);
        testDir.delete();
    }

    @Test(expected = IOException.class)
    public void testInvalidCdCommand() throws IOException {
        explorer.createFSPath();
        explorer.changeWorkingDirectory("testDirX");
        fail("Expected IOException but got null for pwd command.");
    }

    @Test
    public void testExitCommand() {
        explorer.createFSPath();
        explorer.executeCommand("exit");
        assertEquals(0, explorer.getOutputStream().size());
    }
}