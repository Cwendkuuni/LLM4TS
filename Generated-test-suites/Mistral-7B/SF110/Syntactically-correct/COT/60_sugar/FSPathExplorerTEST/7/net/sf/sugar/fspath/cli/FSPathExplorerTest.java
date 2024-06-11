package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for FSPathExplorer.
 */
public class FSPathExplorerTest {

  private FSPathExplorer explorer;
  private File tempDirectory;

  @Before
  public void setUp() throws IOException {
    tempDirectory = File.createTempDirectory("FSPathExplorerTest", "");
    explorer = new FSPathExplorer();
    explorer.rootPath = tempDirectory;
  }

  @Test
  public void testConstructor() {
    assertNotNull(explorer);
  }

  @Test
  public void testMain() {
    // This test method is not comprehensive, as it is difficult to test the command line arguments
    // in a JUnit test case. However, it can be used to test the basic functionality of the main method.
    FSPathExplorer.main(new String[]{"-path", tempDirectory.getAbsolutePath()});
  }

  @Test
  public void testShowWelcome() {
    FSPathExplorer explorer = new FSPathExplorer();
    String welcomeMessage = explorer.showWelcome();
    assertNotNull(welcomeMessage);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testQueryEmptyFSPath() {
    explorer.createFSPath();
    explorer.start();
    assertNull(explorer.fsPathQuery);
    explorer.queryFSPath();
  }

  @Test
  public void testQueryValidFSPath() throws IOException {
    explorer.createFSPath();
    File testFile = File.createTempFile("test", null);
    testFile.createNewFile();
    File testDirectory = new File(tempDirectory, "testDirectory");
    testDirectory.mkdir();

    explorer.fsPathQuery = "//file[@name = 'test']";
    explorer.queryFSPath();
    assertEquals(testFile.getPath(), explorer.getLastResult());

    explorer.fsPathQuery = "//dir[@name = 'testDirectory']";
    explorer.queryFSPath();
    assertEquals(testDirectory.getAbsolutePath(), explorer.getLastResult());

    File testDirectory2 = new File(tempDirectory, "testDirectory2");
    testDirectory2.mkdir();
    explorer.fsPathQuery = "//dir[@name = 'testDirectory']/file[@name = 'testFile']";
    explorer.queryFSPath();
    File resultFile = new File(testDirectory2, "testFile.txt");
    resultFile.createNewFile();
    assertEquals(resultFile.getPath(), explorer.getLastResult());

    explorer.fsPathQuery = "//dir[@name = 'testDirectory']/file[contains(name, '.txt')]";
    FSPathResultList results = explorer.queryFSPathList();
    assertFalse(results.isEmpty());
  }

  @Test(expected = NullPointerException.class)
  public void testQueryNullFSPath() {
    explorer.queryFSPath();
  }

  @Test(expected = IOException.class)
  public void testQueryInvalidFSPath() throws IOException {
    explorer.createFSPath();
    explorer.queryFSPath("//nonExistentPath");
  }

  @Test
  public void testHelpCommand() {
    explorer.createFSPath();
    explorer.start();
    explorer.helpCommand();
  }

  @Test
  public void testCdCommand() throws IOException {
    File tempDirectory2 = new File(tempDirectory, "tempDirectory2");
    tempDirectory2.mkdir();

    explorer.createFSPath();
    explorer.start();
    explorer.cdCommand(".");
    assertEquals(tempDirectory.getAbsolutePath(), explorer.getLastResult());

    explorer.cdCommand(tempDirectory2.getAbsolutePath());
    assertEquals(tempDirectory2.getAbsolutePath(), explorer.getLastResult());

    explorer.cdCommand("../");
    assertEquals(tempDirectory.getAbsolutePath(), explorer.getLastResult());

    explorer.cdCommand("testDirectory/testDirectory2/nonExistentDirectory");
    assertEquals(tempDirectory.getAbsolutePath(), explorer.getLastResult());
  }

  @Test(expected = IOException.class)
  public void testCdCommandInvalidDirectory() throws IOException {
    explorer.createFSPath();
    explorer.start();
    explorer.cdCommand("nonExistentDirectory");
  }

  @Test
  public void testPwdCommand() {
    explorer.createFSPath();
    explorer.start();
    explorer.pwdCommand();
    assertEquals(tempDirectory.getAbsolutePath(), explorer.getLastResult());
  }

  @Test(expected = IOException.class)
  public void testPwdCommandInvalidPath() throws IOException {
    explorer.createFSPath();
    explorer.start();
    explorer.pwdCommand("nonExistentPath");
  }

  @Test
  public void testExitCommand() {
    explorer.createFSPath();
    explorer.start();
    explorer.exitCommand();
    assertTrue(explorer.isClosed());
  }

  @After
  public void tearDown() throws IOException {
    tempDirectory.delete();
    explorer = null;
  }

  private String getLastResult() {
    return explorer.getLastResult();
  }

  private boolean isClosed() {
    return explorer.isClosed();
  }
}