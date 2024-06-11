import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.FSPathResult;
import java.io.File;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 fsPath = explorer.fsPath;
 }

 @Test
 public void testEmptyQuery() {
 FSPathResultList results = fsPath.query("");
 assertFalse(results.size() > 0);
 }

 @Test
 public void testSimplePathQuery() {
 File rootPath = new File("/tmp");
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 FSPathResultList results = fsPath.query(".");
 File expectedResult = new File("/tmp");
 assertFalse(results.size() < 1);
 assertEquals(expectedResult.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testFindDirectoryQuery() {
 File rootPath = new File("/tmp");
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 FSPathResultList results = fsPath.query("dir[@name = 'tmp']");
 File expectedResult = new File("/tmp");
 assertFalse(results.size() < 1);
 assertEquals(expectedResult.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testFindFileWithAttributesQuery() {
 File rootPath = new File("/tmp");
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 FSPathResultList results = fsPath.query("file[@length > 10]");
 File expectedResult = new File("/tmp/testfile"); // replace with an actual file that satisfies the condition
 assertFalse(results.size() < 1);
 assertEquals(expectedResult.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidQuery() {
 FSPathResultList results = fsPath.query("//dir[@name = 'foo']/file[@length > 10000000000]");
 fail("This query should throw an IllegalArgumentException as it is invalid");
 }

 @Test
 public void testQueryWithCDCommand() {
 File rootPath = new File("/tmp");
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 explorer.prompt.setInput("cd test");
 explorer.start();
 File expectedResult = new File("/tmp/test");
 FSPathResultList results = fsPath.query(".");
 assertEquals(expectedResult.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testHelpCommand() {
 explorer.prompt.setInput("help");
 explorer.start();
 // Verify that the help text is printed
 String helpText = System.console().readLine();
 assertNotNull(helpText);
 assertTrue(helpText.contains("Examples :"));
 }
}