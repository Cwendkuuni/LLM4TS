import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 explorer = new FSPathExplorer();
 fsPath = FSPathFactory.newFSPath(new File("."));
 }

 @Test
 public void testConstructor() {
 FSPathExplorer explorer = new FSPathExplorer();
 assertNotNull(explorer);
 }

 @Test
 public void testShowWelcome() {
 explorer.showWelcome();
 }

 @Test
 public void testEmptyQuery() {
 FSPathResultList results = explorer.fsPath.query("");
 assertFalse(results.isEmpty());
 }

 @Test
 public void testValidQuery() {
 String query = "//dir[@name = 'src']//file[@length > 10000]";
 FSPathResultList results = explorer.fsPath.query(query);
 assertFalse(results.isEmpty());
 }

 @Test
 public void testInvalidQuery() {
 String query = "//dir[@name = 'foo']//file[@length foo > 10000]";
 FSPathResultList results = explorer.fsPath.query(query);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testValidQueryResult() {
 String query = "//dir[@name = 'src']//file[@length > 10000]";
 FSPathResultList results = explorer.fsPath.query(query);
 FSPathResult firstResult = results.get(0);
 assertEquals(new File("src/someFileWithBigSize.txt").getPath(), firstResult.getFile().getPath());
 }

 @Test
 public void testErrorHandling() {
 assertThrows(IOException.class, () -> explorer.fsPath.query(""));
 }

 @Test
 public void testSetRootPath() {
 File rootPath = new File("path/to/root");
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 assertEquals(rootPath, explorer.rootPath);
 }

 @Test
 public void testSetQuery() {
 explorer.configureExplorer(new String[]{"-f", "//dir[@name = 'foo']"});
 assertEquals("//dir[@name = 'foo']", explorer.fsPathQuery);
 }

 @Test
 public void testInvalidOption() {
 assertThrows(IllegalArgumentException.class, () -> explorer.configureExplorer(new String[]{"-z", "foo"}));
 }

 @Test
 public void testHelpCommand() {
 String helpOutput = explorer.prompt.readLine();
 helpOutput = helpOutput.substring(helpOutput.indexOf("\nExamples :")).trim();
 List<String> examples = List.of(helpOutput.split("\n"));
 String example1 = examples.get(0);
 String example2 = examples.get(1);
 assertEquals("1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'", example1);
 assertEquals("2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes", example2);
 }

 @Test
 public void testCDCommand() throws IOException {
 File tempDir = File.createTempFile("temp", null);
 tempDir.deleteOnExit();
 explorer.createFSPath();
 File newDir = new File("newDir");
 newDir.mkdirs();
 newDir.deleteOnExit();
 explorer.configureExplorer(new String[]{"-cd", "newDir"});
 assertEquals(newDir, explorer.fsPath.getRootDirectory());
 }

 @Test
 public void testCDToNonExistentDirectory() {
 assertThrows(IOException.class, () -> explorer.configureExplorer(new String[]{"-cd", "nonExistentDir"}));
 }

 @Test
 public void testCDToParentDirectory() {
 explorer.configureExplorer(new String[]{"-cd", ".."});
 File expectedParent = explorer.fsPath.getRootDirectory().getParentFile();
 assertEquals(expectedParent, explorer.fsPath.getRootDirectory());
 }

 @Test
 public void testPWDCommand() throws IOException {
 File currentDirectory = explorer.fsPath.getRootDirectory();
 explorer.prompt.readLineAndConsume("pwd");
 assertEquals(currentDirectory.getAbsolutePath(), explorer.prompt.readLine());
 }

 @Test
 public void testInvalidCommand() {
 assertThrows(IllegalArgumentException.class, () -> explorer.prompt.readLineAndConsume("invalidCommand"));
 }

}