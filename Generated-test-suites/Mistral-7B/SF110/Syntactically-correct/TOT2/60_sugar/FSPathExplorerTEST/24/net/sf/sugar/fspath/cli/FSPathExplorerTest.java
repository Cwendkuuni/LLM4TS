import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import static net.sf.sugar.fspath.cli.Commands.EXIT;
import static net.sf.sugar.fspath.cli.Commands.PWD;
import static net.sf.sugar.fspath.cli.Commands.CD;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private Prompt prompt;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 prompt = new Prompt(System.in, System.out, System.err);
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 explorer = new FSPathExplorer();
 assertNotNull(explorer.prompt);
 }

 @Test
 public void testMain() throws IOException {
 String[] args = {"args1", "args2"};
 explorer.main(args);
 assertEquals("Scanning filesystem metadata...\n", explorer.out.getHistory(0).toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMainWithEmptyArgs() {
 String[] args = {};
 explorer.main(args);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testShowWelcome() {
 explorer.showWelcome();
 assertFalse(explorer.out.hasError());
 }

 @Test
 public void testConfigureExplorer() {
 String[] args = {"--path", "/home/user"};
 explorer.configureExplorer(args);
 assertEquals("/home/user", explorer.rootPath.getAbsolutePath());
 }

 @Test
 public void testCreateFSPath() {
 File rootPath = new File("/home/user");
 explorer.createFSPath();
 assertEquals(rootPath, explorer.fsPath.getRootDirectory());
 }

 @Test
 public void testStartWithEmptyQuery() throws IOException {
 String query = "";
 explorer.start();
 assertEquals("Current directory : /\n", explorer.out.getHistory(0).toString());
 }

 @Test
 public void testStartWithQueryThatMatchesFile() throws IOException {
 String query = "//file[@name = 'test.txt']";
 explorer.start();
 explorer.prompt.writeLine(query);
 String expected = "/home/user/test.txt";
 assertTrue(expected.equals(explorer.fsPath.getFile(explorer.fsPath.getRootDirectory(), "test.txt").getPath()));
 }

 @Test
 public void testStartWithQueryThatMatchesDirectory() throws IOException {
 String query = "//dir[@name = 'test']";
 explorer.start();
 explorer.prompt.writeLine(query);
 String expected = "/home/user/test";
 assertTrue(expected.equals(explorer.fsPath.getFile(explorer.fsPath.getRootDirectory(), "test").getPath()));
 }

 @Test
 public void testStartWithQueryThatMatchesMultipleFiles() throws IOException {
 String query = "//file[@name = 'test*']";
 explorer.start();
 explorer.prompt.writeLine(query);
 String expected1 = "/home/user/test1.txt";
 String expected2 = "/home/user/test2.txt";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(2, results.size());
 for (FSPathResult result : results) {
 String filePath = result.getFile().getPath();
 assertTrue(expected1.equals(filePath) || expected2.equals(filePath));
 }
 }

 @Test
 public void testStartWithQueryThatDoesntMatchAnything() throws IOException {
 String query = "//file[@name = 'nonexistent']";
 explorer.start();
 explorer.prompt.writeLine(query);
 assertTrue(explorer.out.getHistory(0).toString().isEmpty());
 }

 @Test
 public void testStartWithQueryThatIsInvalid() throws IOException {
 String query = "//dir[fs:matches(@name, '^[.].*')]";
 explorer.start();
 explorer.prompt.writeLine(query);
 assertTrue(explorer.out.getHistory(0).toString().contains("Invalid query"));
 }

 @Test
 public void testCD() throws IOException {
 File newDir = new File("/home/user/test");
 explorer.configureExplorer(new String[]{"--path", "/home/user"});
 explorer.createFSPath();
 explorer.start();
 explorer.prompt.writeLine(CD.getCommandText() + " test");
 assertEquals(newDir.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testCDToNonExistentDirectory() throws IOException {
 explorer.configureExplorer(new String[]{"--path", "/home/user"});
 explorer.createFSPath();
 explorer.start();
 explorer.prompt.writeLine(CD.getCommandText() + " nonExistent");
 String expected = "unable to change to nonExistent please make sure that the directory existst and a is readable";
 assertTrue(expected.equals(explorer.out.getHistory(0).toString()));
 }

 @Test
 public void testPWD() throws IOException {
 explorer.configureExplorer(new String[]{"--path", "/home/user"});
 explorer.createFSPath();
 explorer.start();
 explorer.prompt.writeLine(PWD.getCommandText());
 String expected = "/home/user";
 assertEquals(expected, explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testHelp() throws IOException {
 explorer.configureExplorer(new String[]{"--path", "/home/user"});
 explorer.createFSPath();
 explorer.start();
 explorer.prompt.writeLine(EXIT.getCommandText());
 explorer.prompt.writeLine(HELP.getCommandText());
 String expected = "FSPathExplorer help :";
 // The rest of the help message is too long to be stored in a single String
 String helpMessage = explorer.out.getHistory(0).toString();
 assertTrue(helpMessage.startsWith(expected));
 }

 @Test
 public void testExit() throws IOException {
 explorer.configureExplorer(new String[]{"--path", "/home/user"});
 explorer.createFSPath();
 explorer.start();
 explorer.prompt.writeLine(EXIT.getCommandText());
 assertTrue(explorer.prompt.isClosed());
 }
}