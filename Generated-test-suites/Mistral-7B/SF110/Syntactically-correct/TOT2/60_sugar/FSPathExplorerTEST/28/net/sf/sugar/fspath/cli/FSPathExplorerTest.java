import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Command;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;

/**
 * Test class for FSPathExplorer.
 */
public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private File tempDir;

 @Before
 public void setUp() throws IOException {
 tempDir = File.createTempDirectory("test", "fsPathExplorer");
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-path", tempDir.getAbsolutePath()});
 explorer.createFSPath();
 }

 @After
 public void tearDown() throws IOException {
 tempDir.delete();
 }

 /**
 * Test method for {@link FSPathExplorer#FSPathExplorer()}.
 */
 @Test
 public void testFSPathExplorerConstructor() {
 assertEquals("Expected System.out as the out stream", System.out, explorer.getOut());
 assertEquals("Expected System.err as the err stream", System.err, explorer.getErr());
 }

 /**
 * Test method for {@link FSPathExplorer#configureExplorer(String[])}.
 */
 @Test
 public void testConfigureExplorer() {
 File rootPath = new File("/tmp");
 String query = "//dir[@name = 'test']";
 explorer.configureExplorer(new String[]{"-path", rootPath.getAbsolutePath(), "-find", query});
 assertEquals(rootPath, explorer.getRootPath());
 assertEquals(query, explorer.getFsPathQuery());
 }

 /**
 * Test method for {@link FSPathExplorer#createFSPath()}.
 */
 @Test
 public void testCreateFSPath() {
 assertEquals(tempDir, explorer.getRootPath());
 }

 /**
 * Test method for {@link FSPathExplorer#start()}.
 */
 @Test
 public void testStart() throws IOException {
 explorer.start();
 String currentExpression = "pwd";
 String[] expressionWords = currentExpression.split(" ");
 Command command = Commands.getCommand(expressionWords[0]);
 assertEquals("Expected PWD command", command, explorer.getCommand(expressionWords));
 assertEquals("Current directory should be the test temp directory", tempDir.getAbsolutePath(), explorer.getPrompt().readLine());
 }

 /**
 * Test method for {@link FSPathExplorer#start()}.
 */
 @Test
 public void testStartWithInvalidCommand() throws IOException {
 explorer.start();
 String currentExpression = "invalidCommand";
 String[] expressionWords = currentExpression.split(" ");
 Command command = Commands.getCommand(expressionWords[0]);
 assertNull("Expected null command for invalid command", command);
 }

 /**
 * Test method for {@link FSPathExplorer#start()}.
 */
 @Test
 public void testStartWithFindCommand() throws IOException {
 explorer.start();
 String currentExpression = "find test";
 String[] expressionWords = currentExpression.split(" ");
 Command command = Commands.getCommand(expressionWords[0]);
 assertEquals("Expected Find command", command, explorer.getCommand(expressionWords));
 assertEquals("Expected empty query string after find command", "", explorer.getFsPathQuery());
 FSPathResultList results = explorer.fsPath.query("");
 assertTrue("Expected at least one result for empty query", results.size() > 0);
 }

 /**
 * Test method for {@link FSPathExplorer#start()}.
 */
 @Test
 public void testStartWithFindCommandWithQuery() throws IOException {
 File testFile = File.createTempFile("test", null);
 explorer.start();
 String currentExpression = "find test";
 String[] expressionWords = currentExpression.split(" ");
 Command command = Commands.getCommand(expressionWords[0]);
 assertEquals("Expected Find command", command, explorer.getCommand(expressionWords));
 assertEquals("Expected empty query string after find command", "", explorer.getFsPathQuery());
 explorer.configureExplorer(new String[]{"-find", "test"});
 FSPathResultList results = explorer.fsPath.query(explorer.getFsPathQuery());
 assertTrue("Expected at least one result for test query", results.size() > 0);
 File expectedFile = results.get(0).getFile();
 assertTrue("Expected test file to exist", expectedFile.exists());
 testFile.delete();
 }

 /**
 * Test method for {@link FSPathExplorer#start()}.
 */
 @Test(expected = IOException.class)
 public void testStartWithInvalidPath() throws IOException {
 explorer.start();
 String currentExpression = "find /invalid/path";
 String[] expressionWords = currentExpression.split(" ");
 Command command = Commands.getCommand(expressionWords[0]);
 assertEquals("Expected Find command", command, explorer.getCommand(expressionWords));
 explorer.configureExplorer(new String[]{"-find", "invalid/path"});
 }

 /**
 * Test method for {@link FSPathExplorer#start()}.
 */
 @Test(expected = IOException.class)
 public void testStartWithInvalidQuery() throws IOException {
 explorer.start();
 String currentExpression = "find invalidQuery";
 String[] expressionWords = currentExpression.split(" ");
 Command command = Commands.getCommand(expressionWords[0]);
 assertEquals("Expected Find command", command, explorer.getCommand(expressionWords));
 explorer.configureExplorer(new String[]{"-find", "invalidQuery"});
 }

}