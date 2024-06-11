import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import java.io.File;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private String testDirectoryPath;

 @Before
 public void setUp() {
 testDirectoryPath = System.getProperty("user.dir") + File.separator + "test-data";
 explorer = new FSPathExplorer();
 explorer.rootPath = new File(testDirectoryPath);
 }

 @Test
 public void testValidQuery() {
 String query = "//dir[@name = 'test']";
 FSPath fsPath = explorer.fsPath;
 FSPathResultList results = fsPath.query(query);
 File expectedFile = new File(testDirectoryPath + File.separator + "test");
 assertTrue(results.size() > 0);
 assertEquals(expectedFile.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testEmptyQuery() {
 String query = "";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidQuery() {
 String query = "//fileasdfsdfsd[@name = 'test']";
 FSPathResultList results = explorer.fsPath.query(query);
 fail("Expected RuntimeException, but no exception was thrown.");
 }

 @Test
 public void testInvalidDirectory() {
 String query = "//dir[@name = 'non-existent-directory']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test
 public void testChangeDirectory() {
 String query = "p ../";
 explorer.prompt.setPromptText("fspath>");
 explorer.prompt.readLine(); //empty line
 explorer.prompt.readLine(); //p command
 File expectedDirectory = new File(testDirectoryPath + File.separator + "parent");
 explorer.rootPath = new File(expectedDirectory.getParentFile().getAbsolutePath());
 assertEquals(expectedDirectory.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testInvalidChangeDirectory() {
 String query = "p ../non-existent-directory";
 explorer.prompt.setPromptText("fspath>");
 explorer.prompt.readLine(); //empty line
 explorer.prompt.readLine(); //p command
 fail("Expected IOException, but no exception was thrown.");
 }

 @Test
 public void testHelpCommand() {
 String query = "help";
 explorer.prompt.setPromptText("fspath>");
 explorer.prompt.readLine();
 explorer.prompt.readLine(); //help command
 }

}