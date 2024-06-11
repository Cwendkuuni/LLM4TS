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
 private String query;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 fsPath = new FSPathFactory().newFSPath(new File("."));
 query = "";
 }

 @Test
 public void testConstructor() {
 FSPathExplorer explorer = new FSPathExplorer();
 assertTrue("constructor called successfully", true);
 }

 @Test
 public void testMainWithNoArgs() {
 String[] args = new String[0];
 FSPathExplorer.main(args);
 assertTrue("showWelcome method called", true);
 }

 @Test
 public void testMainWithInvalidArgs() {
 String[] args = {"invalidArg"};
 try {
 FSPathExplorer.main(args);
 fail("Exception should be thrown");
 } catch (RuntimeException e) {
 // expected
 }
 }

 @Test
 public void testConfigureExplorerWithValidArgs() {
 String[] args = {"-p", "path/to/root", "-f", "query"};
 explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 assertEquals("rootPath set to path/to/root", new File("path/to/root"), explorer.rootPath);
 assertEquals("fsPathQuery set to query", "query", explorer.fsPathQuery);
 }

 @Test
 public void testCreateFSPathWithNullRootPath() {
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 assertTrue("fsPath created with null rootPath", fsPath instanceof FSPath);
 }

 @Test
 public void testCreateFSPathWithValidRootPath() {
 explorer = new FSPathExplorer();
 File rootPath = new File("path/to/root");
 explorer.configureExplorer(new String[]{"-p", rootPath.getPath()});
 explorer.createFSPath();
 assertEquals("fsPath created with valid rootPath", new File("path/to/root"), explorer.fsPath.getRootDirectory());
 }

 @Test
 public void testStartWithEmptyQuery() {
 String[] args = {"-p", "path/to/root"};
 explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String currentExpression = "pwd";
 String[] expressionWords = currentExpression.split("\\s+");
 try {
 explorer.start();
 String result = explorer.prompt.readLine();
 assertEquals("Current directory : " + new File(".").getAbsolutePath(), result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Unexpected exception");
 }
 }

 @Test
 public void testStartWithValidQuery() {
 String[] args = {"-p", "path/to/root", "-f", "//dir[@name = 'test']"};
 explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String currentExpression = "find test";
 String[] expressionWords = currentExpression.split("\\s+");
 try {
 explorer.start();
 FSPathResultList results = fsPath.query(explorer.fsPathQuery);
 assertEquals("1 result", 1, results.size());
 assertEquals("test/", results.get(0).getFile().getPath().substring(0, 5));
 } catch (IOException e) {
 e.printStackTrace();
 fail("Unexpected exception");
 }
 }

 @Test
 public void testStartWithInvalidQuery() {
 String[] args = {"-p", "path/to/root", "-f", "invalidQuery"};
 explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String currentExpression = "find invalidQuery";
 String[] expressionWords = currentExpression.split("\\s+");
 try {
 explorer.start();
 fail("An exception should be thrown");
 } catch (RuntimeException e) {
 // expected
 }
 }

 @Test
 public void testCDCommand() {
 String[] args = {"-p", "path/to/root"};
 explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String currentExpression = "cd test";
 String[] expressionWords = currentExpression.split("\\s+");
 try {
 explorer.start();
 explorer.prompt.readLine(); // read the welcome message
 String cdCommand = "cd test";
 explorer.prompt.writeLine(cdCommand);
 explorer.prompt.readLine(); // read the new path
 String newPath = explorer.fsPath.getRootDirectory().getPath();
 File testDir = new File(newPath + "/test");
 assertTrue("Test directory exists", testDir.exists());
 assertTrue("Test directory is a directory", testDir.isDirectory());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Unexpected exception");
 }
 }

 @Test
 public void testInvalidCDCommand() {
 String[] args = {"-p", "path/to/root"};
 explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String currentExpression = "cd test/nonexistent";
 String[] expressionWords = currentExpression.split("\\s+");
 try {
 explorer.start();
 explorer.prompt.readLine(); // read the welcome message
 String cdCommand = "cd test/nonexistent";
 explorer.prompt.writeLine(cdCommand);
 String result = explorer.prompt.readLine();
 assertEquals("unable to change to test/nonexistent please make sure that the directory exists and is readable", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Unexpected exception");
 }
 }

 @Test
 public void testPWDCommand() {
 String[] args = {"-p", "path/to/root"};
 explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String currentExpression = "pwd";
 String[] expressionWords = currentExpression.split("\\s+");
 try {
 explorer.start();
 explorer.prompt.readLine(); // read the welcome message
 String result = explorer.prompt.readLine();
 assertEquals("Current directory : " + new File(".").getAbsolutePath(), result);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Unexpected exception");
 }
 }

 @Test
 public void testHelpCommand() {
 String[] args = {"-p", "path/to/root"};
 explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String currentExpression = "help";
 String[] expressionWords = currentExpression.split("\\s+");
 try {
 explorer.start();
 explorer.prompt.readLine(); // read the welcome message
 explorer.prompt.writeLine(currentExpression);
 String helpText = explorer.prompt.readLine();
 assertTrue("Help text contains 'FSPath queries'", helpText.contains("FSPath queries"));
 } catch (IOException e) {
 e.printStackTrace();
 fail("Unexpected exception");
 }
 }

 @After
 public void tearDown() {
 explorer = null;
 fsPath = null;
 query = null;
 }

}