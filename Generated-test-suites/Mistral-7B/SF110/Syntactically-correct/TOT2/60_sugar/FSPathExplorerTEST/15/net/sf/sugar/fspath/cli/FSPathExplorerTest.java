import static org.junit.Assert.*;
import static net.sf.sugar.fspath.cli.Commands.*;
import static net.sf.sugar.fspath.cli.Opt.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer fsPathExplorer;
 private Prompt prompt;
 private PrintStream out;
 private PrintStream err;

 @Before
 public void setup() {
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.out = out;
 fsPathExplorer.err = err;
 prompt = fsPathExplorer.prompt;
 }

 @After
 public void teardown() {
 fsPathExplorer = null;
 }

 @Test
 public void testConstructor() {
 FSPathExplorer explorer = new FSPathExplorer();
 assertNotNull(explorer);
 }

 @Test
 public void testMain() throws IOException {
 String[] args = new String[]{"-p", "/home/user", "-f", "//file[contains(name, '.txt')]"};
 fsPathExplorer.configureExplorer(args);
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 }

 @Test
 public void testInvalidQuery() {
 String currentExpression = "invalidQuery";
 try {
 fsPathExplorer.start();
 fail("expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testInvalidDirectory() throws IOException {
 String[] args = new String[]{"-p", "/nonexistentdirectory", "-f", "//file[contains(name, '.txt')]"};
 fsPathExplorer.configureExplorer(args);
 fsPathExplorer.createFSPath();
 try {
 fsPathExplorer.start();
 fail("expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testQueryEmptyDirectory() throws IOException {
 File rootPath = new File("/");
 fsPathExplorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//"});
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 prompt.readLine(); // consume initial prompt
 String currentExpression = "//dir[@name = '']";
 fsPathExplorer.fsPathQuery = currentExpression;
 FSPathResultList results = fsPathExplorer.fsPath.query(currentExpression);
 assertEquals(0, results.size());
 }

 @Test
 public void testQueryNonExistentFile() throws IOException {
 File rootPath = new File("/");
 fsPathExplorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//file[@name = 'nonexistentfile']"});
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 prompt.readLine(); // consume initial prompt
 String currentExpression = "//file[@name = 'nonexistentfile']";
 fsPathExplorer.fsPathQuery = currentExpression;
 FSPathResultList results = fsPathExplorer.fsPath.query(currentExpression);
 assertEquals(0, results.size());
 }

 @Test
 public void testQueryFile() throws IOException {
 File rootPath = new File("/");
 fsPathExplorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//file[contains(name, 'readme.txt')]"});
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 prompt.readLine(); // consume initial prompt
 String currentExpression = "//file[contains(name, 'readme.txt')]";
 fsPathExplorer.fsPathQuery = currentExpression;
 FSPathResultList results = fsPathExplorer.fsPath.query(currentExpression);
 assertEquals(1, results.size());
 File resultFile = ((FSPathResult) results.get(0)).getFile();
 assertTrue(resultFile.exists());
 }

 @Test
 public void testQueryDirectory() throws IOException {
 File rootPath = new File("/");
 fsPathExplorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//dir[@name = 'usr']/dir[@name = 'local']"});
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 prompt.readLine(); // consume initial prompt
 String currentExpression = "//dir[@name = 'usr']/dir[@name = 'local']";
 fsPathExplorer.fsPathQuery = currentExpression;
 FSPathResultList results = fsPathExplorer.fsPath.query(currentExpression);
 assertEquals(1, results.size());
 File resultFile = ((FSPathResult) results.get(0)).getFile();
 assertTrue(resultFile.isDirectory());
 }

 @Test
 public void testQueryNestedDirectory() throws IOException {
 File rootPath = new File("/");
 fsPathExplorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//dir[@name = 'usr']/dir[@name = 'local']/dir[@name = 'bin']"});
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 prompt.readLine(); // consume initial prompt
 String currentExpression = "//dir[@name = 'usr']/dir[@name = 'local']/dir[@name = 'bin']";
 fsPathExplorer.fsPathQuery = currentExpression;
 FSPathResultList results = fsPathExplorer.fsPath.query(currentExpression);
 assertEquals(1, results.size());
 File resultFile = ((FSPathResult) results.get(0)).getFile();
 assertTrue(resultFile.isDirectory());
 }

 @Test
 public void testQueryMultipleFiles() throws IOException {
 File rootPath = new File("/");
 fsPathExplorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//file[contains(name, '.txt')]"});
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 prompt.readLine(); // consume initial prompt
 int resultCount = 0;
 while (true) {
 String currentExpression = prompt.readLine();
 if (currentExpression == null || currentExpression.equalsIgnoreCase(Commands.EXIT.getCommandText())) {
 break;
 }
 fsPathExplorer.fsPathQuery = currentExpression;
 FSPathResultList results = fsPathExplorer.fsPath.query(currentExpression);
 resultCount += results.size();
 }
 assertEquals(11, resultCount);
 }

 @Test
 public void testHelpCommand() throws IOException {
 fsPathExplorer.configureExplorer(new String[]{});
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 prompt.readLine(); // consume initial prompt
 String currentExpression = Commands.HELP.getCommandText();
 fsPathExplorer.fsPathQuery = currentExpression;
 FSPathResultList results = fsPathExplorer.fsPath.query(currentExpression);
 assertEquals(1, results.size());
 File resultFile = ((FSPathResult) results.get(0)).getFile();
 assertFalse(resultFile.exists());
 String helpText = resultFile.getCanonicalPath();
 String expectedHelpText = new File("src/test/resources/expectedHelpText.txt").getCanonicalPath();
 assertEquals(expectedHelpText, helpText);
 }

 @Test
 public void testCdCommand() throws IOException {
 File rootPath = new File("/");
 fsPathExplorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 prompt.readLine(); // consume initial prompt
 String currentExpression = CD.getCommandText() + " " + "/tmp";
 fsPathExplorer.fsPathQuery = currentExpression;
 File newDirectory = new File("/tmp");
 fsPathExplorer.configureExplorer(new String[]{});
 fsPathExplorer.createFSPath();
 assertEquals(newDirectory.getAbsolutePath(), fsPathExplorer.fsPath.getRootDirectory().getAbsolutePath());
 }
}