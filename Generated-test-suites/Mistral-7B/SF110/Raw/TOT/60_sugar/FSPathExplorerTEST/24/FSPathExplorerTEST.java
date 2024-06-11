import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private String query;
 private String rootPath;

 @Before
 public void setUp() {
 rootPath = new File(".").getAbsolutePath();
 explorer = new FSPathExplorer();
 explorer.rootPath = new File(rootPath);
 explorer.fsPath = FSPathFactory.newFSPath(rootPath);
 }

 @Test
 public void testEmptyQuery() {
 FSPathResultList results = explorer.fsPath.query("");
 assertEquals(0, results.size());
 }

 @Test
 public void testValidQuery() {
 query = "//dir[@name = 'src']//file[@length > 10000]";
 FSPathResultList results = explorer.fsPath.query(query);
 File expectedFile = new File("src/main/java/net/sf/sugar/fspath/cli/FSPathExplorerTest.java");
 assertTrue(results.contains(new FSPathResult(expectedFile)));
 }

 @Test
 public void testNonExistentQuery() {
 query = "//dir[@name = 'abc']//file[@name = 'xyz.txt']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery() {
 query = "//dir[@name = 'abc']//file[@length = 123456";
 explorer.fsPath.query(query);
 }

 @Test
 public void testChangeDirectory() {
 explorer.fsPath = FSPathFactory.newFSPath(new File("/test"));
 File testFile = new File("/test/testFile.txt");
 explorer.fsPath.query("cd ../");
 assertEquals(testFile.getParentFile().getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testHelpCommand() {
 String commandOutput = explorer.prompt.readLine();
 commandOutput = explorer.prompt.readLine();
 commandOutput = explorer.prompt.readLine();
 String expectedHelp = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\n" +
                      "Examples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n" +
                      "2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n" +
                      "   with a file size greater than 10000 bytes\n\n" +
                      "3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\n" +
                      "Attributes : (see javadoc for java.io.File for more info)\n";
 assertEquals(expectedHelp, commandOutput);
 }

 @Test
 public void testPWDCommand() {
 String currentDirectory = explorer.fsPath.getRootDirectory().getAbsolutePath();
 String commandOutput = explorer.prompt.readLine();
 assertEquals(currentDirectory, commandOutput);
 }
}