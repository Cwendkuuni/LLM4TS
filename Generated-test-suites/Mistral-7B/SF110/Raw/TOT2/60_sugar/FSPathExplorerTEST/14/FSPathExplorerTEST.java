import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathFactory;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private File tempRootPath;

 @Before
 public void setUp() throws Exception {
 tempRootPath = File.createTempFile("junit", null);
 tempRootPath.deleteOnExit();
 File tempDir = new File(tempRootPath, "testDir");
 tempDir.mkdirs();
 explorer = new FSPathExplorer();
 explorer.rootPath = tempRootPath;
 }

 @Test
 public void testCreateFSPath() {
 FSPath fsPath = explorer.createFSPath();
 assertEquals("expected rootPath to be set", tempRootPath, fsPath.getRootDirectory());
 }

 @Test
 public void testSimpleQuery() {
 String query = "//file[name = 'testDir/testFile.txt']";
 FSPathResultList results = explorer.fsPath.query(query);
 List<String> expected = Arrays.asList(new String[]{"testRootPath/testDir/testFile.txt"});
 verifyResults(results, expected);
 }

 @Test
 public void testQueryWithWildcard() {
 String query = "//file[name = 'testDir/test*']";
 FSPathResultList results = explorer.fsPath.query(query);
 List<String> expected = Arrays.asList(new String[]{"testRootPath/testDir/testFile.txt", "testRootPath/testDir/testFile2.txt"});
 verifyResults(results, expected);
 }

 @Test
 public void testQueryWithMultipleConditions() {
 String query = "//file[@size > 10][@name = 'testFile.txt']";
 FSPathResultList results = explorer.fsPath.query(query);
 List<String> expected = Arrays.asList(new String[]{"testRootPath/testDir/testFile2.txt"}); //assuming testFile2.txt has a size > 10
 verifyResults(results, expected);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidQuery() {
 String query = "//dir[@name = 'test/']"; //test for forward slashes
 FSPathResultList results = explorer.fsPath.query(query);
 fail("expected IllegalArgumentException");
 }

 @Test(expected = IOException.class)
 public void testQueryThrowsIOException() throws IOException {
 explorer.fsPath.query("//notAValidPath");
 }

 @Test
 public void testCDCommand() {
 String query = "cd testDir";
 explorer.prompt.readLineAndConsume("cd testDir");
 File current = new File(explorer.rootPath, "testDir");
 assertTrue(current.exists() && current.isDirectory());
 }

 @Test(expected = IOException.class)
 public void testCDCommandToNonExistingDirectoryThrowsIOException() throws IOException {
 String query = "cd testNonExistentDir";
 explorer.prompt.readLineAndConsume("cd testNonExistentDir");
 fail("expected IOException");
 }

 @Test
 public void testPWDCommand() {
 String query = "pwd";
 String expected = tempRootPath.getAbsolutePath();
 explorer.prompt.readLineAndConsume(expected);
 }

 @Test
 public void testExitCommand() {
 String query = "exit";
 explorer.prompt.readLineAndConsume("exit");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCommand() {
 String query = "invalidCommand";
 explorer.prompt.readLineAndConsume("invalidCommand");
 fail("expected IllegalArgumentException");
 }

 @Test(expected = IOException.class)
 public void testHelpCommandThrowsIOException() throws IOException {
 String query = "help";
 explorer.prompt.readLineAndConsume("help"); //help should print to system out
 FSPathResultList results = explorer.fsPath.query("help");
 fail("expected IOException");
 }

 @After
 public void tearDown() throws Exception {
 tempRootPath.delete();
 }

 private void verifyResults(FSPathResultList results, List<String> expected) {
 assertEquals(expected.size(), results.size());
 for (int i = 0; i < results.size(); i++) {
 assertEquals(expected.get(i), results.get(i).getPath());
 }
 }

}