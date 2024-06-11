import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 fsPath = FSPathFactory.newFSPath();
 }

 @Test
 public void testPwdCommand() {
 File rootDir = new File(System.getProperty("user.dir"));
 String expected = rootDir.getAbsolutePath();
 String result = explorer.pwdCommand(fsPath, rootDir);
 assertEquals(expected, result);
 }

 @Test
 public void testCdCommand_ValidDirectory() {
 File parentDir = new File(System.getProperty("user.dir")).getParentFile();
 String command = "cd test"; // assuming 'test' is a valid subdirectory
 explorer.configureExplorer(new String[]{"-p", parentDir.getAbsolutePath(), Commands.CD.getCommandText(), "test"});
 String result = explorer.queryFSPath(Commands.PWD.getCommandText());
 File expected = new File(parentDir, "test");
 assertEquals(expected.getAbsolutePath(), result);
 }

 @Test(expected = AssertionError.class)
 public void testCdCommand_InvalidDirectory() {
 File parentDir = new File(System.getProperty("user.dir")).getParentFile();
 String command = "cd non_existent_directory"; // assuming 'non_existent_directory' does not exist
 explorer.configureExplorer(new String[]{"-p", parentDir.getAbsolutePath(), Commands.CD.getCommandText(), "non_existent_directory"});
 String result = explorer.queryFSPath(Commands.PWD.getCommandText());
 fail("Expected an error when trying to change to a non-existent directory.");
 }

 @Test
 public void testHelpCommand() {
 explorer.configureExplorer(new String[]{"--help"});
 String result = explorer.queryFSPath(Commands.HELP.getCommandText());
 assertTrue(result.contains("FSPathExplorer help :"));
 assertTrue(result.contains("Examples :"));
 assertTrue(result.contains("Commands :"));
 }

 @Test
 public void testFindCommand_ValidQuery() {
 String query = "//dir[@name = 'src']//file[@length > 10000]";
 FSPathResultList results = fsPath.query(query);
 int expectedSize = 1; // replace with the actual number of files matching the query
 assertEquals(expectedSize, results.size());
 }

 @Test
 public void testFindCommand_InvalidQuery() {
 String query = "//dir[@name = 'src']//file[@length > 10000]//file[@name = 'non_existent_file']";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testFindCommand_EmptyDirectory() {
 File emptyDir = new File("path/to/empty_directory");
 explorer.configureExplorer(new String[]{"-p", emptyDir.getAbsolutePath(), Commands.PWD.getCommandText()});
 explorer.createFSPath();
 FSPathResultList results = fsPath.query("//file[@length > 0]");
 assertTrue(results.isEmpty());
 }

}