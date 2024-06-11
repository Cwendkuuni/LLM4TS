import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private String workingDirectory;

 @Before
 public void setUp() throws IOException {
 workingDirectory = System.getProperty("user.dir");
 File tempDir = File.createTempFile("FSPathExplorerTest", null);
 explorer = new FSPathExplorer();
 explorer.rootPath = tempDir;
 }

 @Test
 public void testPwdCommand() {
 String expected = tempDir.getAbsolutePath();
 String actual = explorer.pwdCommand();
 assertEquals(expected, actual);
 }

 @Test
 public void testCdCommand() throws IOException {
 File parentDir = tempDir.getParentFile();
 explorer.cdCommand("..");
 String actual = explorer.pwdCommand();
 assertEquals(parentDir.getAbsolutePath(), actual);
 }

 @Test(expected = IOException.class)
 public void testInvalidCdCommand() throws IOException {
 explorer.cdCommand("INVALID_DIRECTORY");
 }

 @Test
 public void testHelpCommand() {
 explorer.helpCommand();
 }

 @Test
 public void testFindFilesWithValidQuery() throws IOException {
 List<String> results = explorer.findFilesWithValidQuery("//file[@name = 'test*.txt']");
 String expected = workingDirectory + File.separator + "FSPathExplorerTest" + File.separator + "FSPathExplorerTest.java";
 assertTrue(results.contains(expected));
 }

 @Test
 public void testFindFilesWithInvalidQuery() {
 List<String> results = explorer.findFilesWithValidQuery("//file[name = 'INVALID_QUERY']");
 assertEquals(0, results.size());
 }

 @Test(expected = IOException.class)
 public void testFindFilesWithInvalidPath() throws IOException {
 explorer.findFilesWithValidQuery("//file[name = 'test*.txt']/../invalidPath");
 }

 @Test
 public void testFindDirectoriesWithValidQuery() throws IOException {
 List<String> results = explorer.findDirectoriesWithValidQuery("//dir[@name = 'test']");
 String expected = workingDirectory + File.separator + "FSPathExplorerTest" + File.separator + "FSPathExplorerTest";
 assertTrue(results.get(0).endsWith(File.separator + "FSPathExplorerTest" + File.separator + "FSPathExplorerTest"));
 }

 @Test
 public void testFindDirectoriesWithInvalidQuery() {
 List<String> results = explorer.findDirectoriesWithValidQuery("//dir[name = 'INVALID_QUERY']");
 assertEquals(0, results.size());
 }

 @Test(expected = IOException.class)
 public void testFindDirectoriesWithInvalidPath() throws IOException {
 explorer.findDirectoriesWithValidQuery("//dir[@name = 'test']/../invalidPath");
 }

 @Test
 public void testExitCommand() {
 explorer.exitCommand();
 }

 @After
 public void tearDown() throws Exception {
 explorer.rootPath.delete();
 System.setProperty("user.dir", workingDirectory);
 }

}