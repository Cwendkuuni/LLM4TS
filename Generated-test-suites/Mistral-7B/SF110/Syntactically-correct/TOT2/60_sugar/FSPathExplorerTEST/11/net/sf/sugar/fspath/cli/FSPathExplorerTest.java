import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;
 private File rootPath;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 rootPath = new File(System.getProperty("user.dir"));
 explorer.setRootPath(rootPath);
 explorer.createFSPath();
 }

 @Test
 public void testPWDCommand() {
 String expected = rootPath.getAbsolutePath();
 String actual = explorer.executeCommand(Commands.PWD);
 assertEquals(expected, actual);
 }

 @Test
 public void testCDCommandSuccess() throws IOException {
 File tempDir = File.createTempFile("junit", null);
 explorer.setRootPath(rootPath);
 explorer.executeCommand(Commands.CD, "..");
 File currentDir = explorer.getFSPath().getRootDirectory();
 explorer.executeCommand(Commands.CD, tempDir.getAbsolutePath());
 explorer.executeCommand(Commands.CD);
 File expected = new File(currentDir.getParentFile().getAbsolutePath());
 File actual = explorer.getFSPath().getRootDirectory();
 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testCDCommandFailure() throws IOException {
 String invalidDir = "/invalid/directory/path";
 explorer.executeCommand(Commands.CD, invalidDir);
 }

 @Test
 public void testFindCommandBasic() throws IOException {
 String query = "//file[@size > 1000]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = (int) Math.ceil(Math.random() * 20);
 int actualSize = results.size();
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testFindCommandWithAttributes() throws IOException {
 String query = "//file[@size > 1000][@hidden = false]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testHelpCommand() {
 String actual = explorer.executeCommand(Commands.HELP);
 String expected = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'";
 assertEquals(expected, actual);
 }

 @Test
 public void testExitCommand() {
 explorer.executeCommand(Commands.EXIT);
 }

 @Test
 public void testInvalidCommand() {
 String invalidCommand = "invalidCommand";
 String actual = explorer.executeCommand(invalidCommand);
 assertNull(actual);
 }

 @Test
 public void testFindCommandEmptyResult() throws IOException {
 String query = "//file[@size = 50000]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 0;
 int actualSize = results.size();
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testFindCommandNoMatches() throws IOException {
 String query = "//file[@size > 1000][@hidden = true]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 0;
 int actualSize = results.size();
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testFindCommandWithQuotes() throws IOException {
 String query = "//file[contains(name, 'test.txt')]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithQuotesAndAttributes() throws IOException {
 String query = "//file[contains(name, 'test.txt')][@hidden = false]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithParentPath() throws IOException {
 String query = "//dir[@name = 'test']//file[@size > 10000]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithComplexPath() throws IOException {
 String query = "//dir[@name = 'test']//file[@size > 10000]//dir[@name = 'sub']//file[@hidden = false]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithDirectoryQuery() throws IOException {
 String query = "//dir[@name = 'test']";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithDirectoryQueryAndAttributes() throws IOException {
 String query = "//dir[@name = 'test'][@hidden = false]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithComplexDirectoryQuery() throws IOException {
 String query = "//dir[@name = 'test1']//dir[@name = 'test2']//dir[@name = 'test3']";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithComplexDirectoryQueryAndAttributes() throws IOException {
 String query = "//dir[@name = 'test1'][@hidden = false]//dir[@name = 'test2']//dir[@name = 'test3'][@hidden = false]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithComplexQuery() throws IOException {
 String query = "//file[@size > 10000][@name =~ /test.*\\.txt/]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithComplexQueryAttributes() throws IOException {
 String query = "//file[@size > 10000][@name =~ /test.*\\.txt/][@hidden = false]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithComplexQueryAndParentPath() throws IOException {
 String query = "//file[@size > 10000]//dir[@name = 'test']//file[@name =~ /test.*\\.txt/]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }

 @Test
 public void testFindCommandWithComplexQueryAndParentPathAttributes() throws IOException {
 String query = "//file[@size > 10000][@hidden = false]//dir[@name = 'test'][@hidden = false]//file[@name =~ /test.*\\.txt/][@hidden = false]";
 FSPathResultList results = explorer.executeQuery(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertTrue(actualSize > 0);
 }
}