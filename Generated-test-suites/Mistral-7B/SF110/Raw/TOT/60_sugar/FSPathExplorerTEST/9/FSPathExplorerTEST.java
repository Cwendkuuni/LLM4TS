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
 fsPath = FSPathFactory.newFSPath();
 }

 @Test
 public void testNormalQuery() {
 String query = "//dir[@name = 'src']//file[@length > 10000]";
 FSPathResultList results = fsPath.query(query);
 assertEquals(5, results.size());
 for (FSPathResult result : results) {
 assertTrue(result.getFile().length() > 10000);
 }
 }

 @Test
 public void testEmptyQuery() {
 String query = "";
 FSPathResultList results = fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test
 public void testInvalidQuery() {
 String query = "//dir[@name = xxx]//file[@length > 10000]";
 FSPathResultList results = fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test
 public void testInvalidPath() {
 File rootPath = new File("non_existent_directory");
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 explorer.createFSPath();
 assertNull(explorer.fsPath);
 }

 @Test(expected = IOException.class)
 public void testInvalidQueryWithInvalidPath() {
 File rootPath = new File("non_existent_directory");
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "//dir[@name = 'src']//file[@length > 10000]"});
 explorer.createFSPath();
 explorer.start();
 }

 @Test
 public void testHelpCommand() {
 String query = "help";
 FSPathResultList results = fsPath.query(query);
 assertNotNull(results);
 assertEquals(1, results.size());
 String expectedHelpText = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\n";
 assertEquals(expectedHelpText, results.get(0).getFile().getPath());
 }

 @Test
 public void testCDCommand() {
 String query = "cd ../../";
 explorer.configureExplorer(new String[]{"-p", "/home/user/project"});
 explorer.createFSPath();
 explorer.start();
 String currentPath = explorer.prompt.readLine();
 assertEquals("/home/user", currentPath.substring(0, currentPath.lastIndexOf('/')));
 query = "cd src";
 explorer.prompt.setInput(query);
 explorer.prompt.sendKeyPresses(2);
 currentPath = explorer.prompt.readLine();
 assertEquals("src", currentPath.substring(currentPath.lastIndexOf('/') + 1));
 }

 @Test
 public void testPWDCommand() {
 explorer.configureExplorer(new String[]{"-p", "/home/user/project"});
 explorer.createFSPath();
 explorer.start();
 String currentPath = explorer.prompt.readLine();
 query = "pwd";
 explorer.prompt.setInput(query);
 explorer.prompt.sendKeyPresses(1);
 currentPath = explorer.prompt.readLine();
 assertEquals("/home/user/project", currentPath);
 }

 @Test
 public void testExitCommand() {
 explorer.configureExplorer(new String[]{"-p", "/home/user/project"});
 explorer.createFSPath();
 explorer.start();
 String currentPath = explorer.prompt.readLine();
 query = "exit";
 explorer.prompt.setInput(query);
 explorer.prompt.sendKeyPresses(1);
 assertFalse(explorer.isRunning());
 }

 @After
 public void tearDown() {
 explorer = null;
 fsPath = null;
 }

}