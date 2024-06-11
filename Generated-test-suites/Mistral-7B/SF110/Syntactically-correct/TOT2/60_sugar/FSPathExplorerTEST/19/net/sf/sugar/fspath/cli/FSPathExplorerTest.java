import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private Prompt prompt;
 private PrintStream out;
 private PrintStream err;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 out = System.out;
 err = System.err;
 prompt = new Prompt(System.in, out, err);
 }

 @After
 public void tearDown() {
 prompt.close();
 }

 @Test
 public void testConstructor() {
 FSPathExplorer explorer = new FSPathExplorer();
 assertNotNull(explorer);
 }

 @Test
 public void testMain() {
 String[] args = new String[]{"-path", "/tmp", "-find", "*.txt"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 explorer.createFSPath();
 explorer.start();
 String expectedOutput = "Scanning filesystem metadata...\n"
 + "Type 'help' for commands...\n"
 + "Current directory : /tmp\n";
 String output = "";
 try {
 out = new PrintStream(new ByteArrayOutputStream());
 explorer.main(args);
 } catch (IOException ioe) {
 ioe.printStackTrace();
 }
 output += out.getBytes();
 out.close();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testShowWelcome() {
 String expectedOutput = "+-------------------------------------------------------+\n"
 + "|                                                       |\n"
 + "| FSPathExplorer                                        |\n"
 + "|                                                       |\n"
 + "+-------------------------------------------------------+\n"
 + "Type 'help' for commands...\n"
 + "Scanning filesystem metadata...\n"
 + "+-------------------------------------------------------+\n"
 + "PLEASE NOTE running FSPathExplorer from the top level     |\n" + "of your filesystem is not advised                             |\n"
 + "+-------------------------------------------------------+";
 String output = "";
 try {
 explorer.showWelcome();
 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
 StringBuilder sb = new StringBuilder();
 String line;
 while ((line = reader.readLine()) != null) {
 sb.append(line);
 }
 output += sb.toString();
 } catch (IOException ioe) {
 ioe.printStackTrace();
 }
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testShowHelp() {
 String expectedOutput = "FSPathExplorer help :\n\n"
 + "Enter an FSPath query to search the filesystem\n\n"
 + "Examples :\n\n"
 + "1) //dir[@name = 'foo'] \n  finds any directory under the current directory named 'foo'\n"
 + "2) //dir[@name = 'src']//file[@length > 10000] \n  finds any file which is nested anywhere below a 'src' directory\n"
 + "   with a file size greater than 10000 bytes\n"
 + "3) //dir[fs:matches(@name, '^[.].*')] \n  finds all directories in the directory sructure which begin with a '.'\n\n"
 + "Attributes : (see javadoc for java.io.File for more info)\n"
 + "@\n"
 + "@dir\n"
 + "@file\n"
 + "@size\n"
 + "@name\n"
 + "@hidden\n"
 + "@readable\n"
 + "@writable\n"
 + "@executable\n"
 + "@lastModified\n"
 + "@lastAccessed\n"
 + "@created\n"
 + "@type\n"
 + "\nCommands :\n";
 String output = "";
 try {
 explorer.showHelp();
 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
 StringBuilder sb = new StringBuilder();
 String line;
 while ((line = reader.readLine()) != null) {
 sb.append(line);
 }
 output += sb.toString();
 } catch (IOException ioe) {
 ioe.printStackTrace();
 }
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testConfigureExplorer() {
 String[] args = new String[]{"-path", "/home/user", "-find", "*.txt"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 File rootPath = explorer.rootPath;
 String fsPathQuery = explorer.fsPathQuery;
 assertTrue(rootPath != null);
 assertTrue(fsPathQuery.equals("*.txt"));
 }

 @Test
 public void testCreateFSPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 FSPath fsPath = explorer.createFSPath();
 assertNotNull(fsPath);
 }

 @Test
 public void testStart() throws IOException {
 String[] args = new String[]{"-path", "/home/user"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 explorer.createFSPath();
 explorer.start();
 String expectedOutput = "Type 'help' for commands...\n";
 String output = "";
 try {
 out = new PrintStream(new ByteArrayOutputStream());
 prompt.setPromptText("fspath>");
 prompt.readLine();
 out.flush();
 } catch (IOException ioe) {
 ioe.printStackTrace();
 }
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-path", "//asdfasdfasdf"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFindQuery() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-find", "**//"});
 }

 @Test
 public void testQueryEmptyPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 List<FSPathResult> results = explorer.fsPath.query("");
 assertTrue(results.isEmpty());
 }

 @Test
 public void testQuerySimpleDirPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 File testPath = new File("/tmp");
 explorer.rootPath = testPath;
 List<FSPathResult> results = explorer.fsPath.query("/dir");
 assertEquals(1, results.size());
 File expectedResult = new File("/tmp/dir");
 assertEquals(expectedResult.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testQuerySimpleFilePath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 File testPath = new File("/tmp");
 explorer.rootPath = testPath;
 List<FSPathResult> results = explorer.fsPath.query("/file.txt");
 assertEquals(1, results.size());
 File expectedResult = new File("/tmp/file.txt");
 assertEquals(expectedResult.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testQueryComplexPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 File testPath = new File("/usr/local/bin");
 explorer.rootPath = testPath;
 List<FSPathResult> results = explorer.fsPath.query("//dir[@name = 'include']//file[@name = 'test.txt']");
 assertEquals(1, results.size());
 File expectedResult = new File("/usr/local/bin/include/test.txt");
 assertEquals(expectedResult.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testQueryComplexPathMultipleMatches() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 File testPath = new File("/usr/local/bin");
 explorer.rootPath = testPath;
 List<FSPathResult> results = explorer.fsPath.query("//dir[@name = 'include']//file[@size > 1000]");
 assertTrue(results.size() > 1);
 }

 @Test(expected = IOException.class)
 public void testQueryInvalidPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 List<FSPathResult> results = explorer.fsPath.query("//asdfasdf");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testQueryInvalidAttribute() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 List<FSPathResult> results = explorer.fsPath.query("//file[@asdf = 'asdf']");
 }

 @Test
 public void testQueryWithAttributes() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 File testPath = new File("/usr/local/bin");
 explorer.rootPath = testPath;
 List<FSPathResult> results = explorer.fsPath.query("//dir[@name = 'include']//file[@size > 1000 and @hidden = 'false']");
 assertTrue(results.size() > 0);
 }
}