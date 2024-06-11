import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import java.io.File;
import java.io.IOException;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 fsPath = explorer.fsPath;
 }

 @Test
 public void testPwdCommand() {
 String expected = System.getProperty("user.dir");
 String actual;
 try {
 actual = explorer.executeCommand("pwd");
 } catch (IOException e) {
 fail("An IOException occurred while executing 'pwd' command.");
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testCdCommand() {
 String currentDir = System.getProperty("user.dir");
 File newDir = new File(currentDir + "/test");
 try {
 explorer.executeCommand("cd " + newDir.getAbsolutePath());
 assertEquals(newDir.getAbsolutePath(), fsPath.getRootDirectory().getAbsolutePath());
 } catch (IOException e) {
 fail("An IOException occurred while executing 'cd' command.");
 }
 }

 @Test
 public void testHelpCommand() {
 String expected = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\n";
 String actual;
 try {
 actual = explorer.executeCommand("help");
 } catch (IOException e) {
 fail("An IOException occurred while executing 'help' command.");
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testFindFilesUnderRootDirectory() {
 String query = "//file";
 FSPathResultList results = fsPath.query(query);
 for (FSPathResult result : results) {
 assertTrue(result.getFile().isFile());
 }
 }

 @Test
 public void testFindFilesUnderSubDirectory() {
 String query = "//dir[@name = 'test']/file";
 FSPathResultList results = null;
 try {
 results = fsPath.query(query);
 } catch (IOException e) {
 e.printStackTrace();
 }
 for (FSPathResult result : results) {
 File file = result.getFile();
 assertTrue(file.isFile());
 assertTrue(file.getParentFile().getName().equals("test"));
 }
 }

 @Test
 public void testFindNonExistingDirectory() {
 String query = "//dir[@name = 'nonExistent']";
 FSPathResultList results = fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test(expected = IOException.class)
 public void testFindNonExistingFile() {
 String query = "//file[@name = 'nonExistent']";
 fsPath.query(query);
 }
}