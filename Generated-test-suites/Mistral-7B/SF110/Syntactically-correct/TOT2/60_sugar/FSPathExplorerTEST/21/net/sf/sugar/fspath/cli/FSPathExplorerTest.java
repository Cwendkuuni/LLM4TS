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
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;
 private Prompt prompt;
 private PrintStream out;
 private PrintStream err;

 @Before
 public void setUp() {
 this.explorer = new FSPathExplorer();
 this.fsPath = this.explorer.getFSPath();
 this.prompt = this.explorer.getPrompt();
 this.out = this.explorer.getOut();
 this.err = this.explorer.getErr();
 }

 @Test
 public void testConstructor() {
 FSPathExplorer explorer = new FSPathExplorer();
 assertNotNull(explorer);
 }

 @Test
 public void testMain() throws IOException, InterruptedException {
 String[] args = {"-path", "/tmp"};
 FSPathExplorer.main(args);
 assertEquals("Current directory : /tmp\n", this.prompt.readLine());
 }

 @Test
 public void testConfigureExplorer() throws IOException {
 String[] args = {"-path", "/tmp", "-find", "test.txt"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 assertEquals(new File("/tmp"), explorer.getRootPath());
 assertEquals("test.txt", explorer.getFsPathQuery());
 }

 @Test
 public void testCreateFSPath() {
 File rootPath = new File("/tmp");
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-path", "/tmp"});
 explorer.createFSPath();
 assertEquals(rootPath, explorer.getFSPath().getRootDirectory());
 }

 @Test
 public void testStart() throws IOException {
 this.explorer.configureExplorer(new String[]{"-find", "test.txt"});
 this.explorer.start();
 String expectedOutput = "//file[@name = 'test.txt']\n";
 String output = this.prompt.readLine();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testInvalidCommand() {
 this.explorer.start();
 String output = this.prompt.readLine();
 assertFalse(Commands.isCommand(output.split(" ")[0]));
 }

 @Test
 public void testPWDCommand() throws IOException, InterruptedException {
 this.explorer.start();
 this.prompt.sendLine("pwd");
 String expectedOutput = "Current directory : " + this.fsPath.getRootDirectory().getAbsolutePath() + "\n";
 String output = this.prompt.readLine();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCDCommand() throws IOException, InterruptedException {
 this.explorer.start();
 this.prompt.sendLine("cd test");
 String expectedOutput = "changed to : test\n";
 String output = this.prompt.readLine();
 assertEquals(expectedOutput, output);
 assertTrue(new File("test").exists() && new File("test").isDirectory() && new File("test").canRead());
 }

 @Test(expected = IOException.class)
 public void testInvalidCDCommand() throws IOException, InterruptedException {
 this.explorer.start();
 this.prompt.sendLine("cd test123");
 }

 @Test
 public void testQueryDirectory() throws IOException {
 this.explorer.createFSPath();
 File root = this.explorer.getFSPath().getRootDirectory();
 List<FSPathResult> results = this.fsPath.query("//dir");
 for (FSPathResult result : results) {
 out.println(result.getFile().getPath());
 }
 File[] files = root.listFiles();
 for (File file : files) {
 if (file.isDirectory()) {
 FSPathResult expectedResult = new FSPathResult(file);
 assertSame(expectedResult, results.get(results.size() - 1));
 }
 }
 }

 @Test
 public void testQueryFile() throws IOException {
 this.explorer.createFSPath();
 this.fsPath.query("//file[@name = 'test.txt']");
 }

 @Test(expected = IOException.class)
 public void testQueryNonExistentFile() throws IOException {
 this.explorer.createFSPath();
 this.fsPath.query("//file[@name = 'nonexistent.txt']");
 }

 @Test
 public void testQueryEmptyDirectory() throws IOException {
 this.explorer.createFSPath();
 File emptyDir = new File("test");
 this.prompt.sendLine("mkdir test");
 this.prompt.sendLine("cd test");
 this.prompt.sendLine("mkdir test2");
 this.prompt.sendLine("rmdir test2");
 this.prompt.sendLine("rmdir test");
 this.fsPath.query("//dir[@name = 'test']//file");
 }

 @Test
 public void testQuerySubDirectory() throws IOException {
 this.explorer.createFSPath();
 File subDir = new File("test");
 this.prompt.sendLine("mkdir test");
 this.prompt.sendLine("cd test");
 this.prompt.sendLine("mkdir test2");
 this.prompt.sendLine("cd test2");
 this.prompt.sendLine("touch test.txt");
 this.prompt.sendLine("cd ..");
 this.prompt.sendLine("rmdir test2");
 this.prompt.sendLine("rmdir test");
 FSPathResultList results = this.fsPath.query("//dir[@name = 'test']//file[@name = 'test.txt']");
 assertEquals(1, results.size());
 }

 @Test
 public void testQueryMultipleMatches() throws IOException {
 this.explorer.createFSPath();
 File dir1 = new File("test1");
 File dir2 = new File("test2");
 this.prompt.sendLine("mkdir test1");
 this.prompt.sendLine("mkdir test2");
 this.prompt.sendLine("cd test1");
 this.prompt.sendLine("touch test1.txt");
 this.prompt.sendLine("cd ../test2");
 this.prompt.sendLine("touch test2.txt");
 this.prompt.sendLine("cd ..");
 FSPathResultList results = this.fsPath.query("//file[@name = 'test.txt']");
 assertEquals(2, results.size());
 }

 @Test
 public void testQueryAttributes() throws IOException {
 this.explorer.createFSPath();
 FSPathResultList results = this.fsPath.query("//file[@name = 'test.txt']//attribute[@name = 'size']");
 assertEquals(1, results.size());
 assertEquals(new Long(11), results.get(0).getAttributeValue().getLongValue());
 }

 @Test
 public void testQueryAttributesWithMultipleMatches() throws IOException {
 this.explorer.createFSPath();
 File file1 = new File("test1.txt");
 File file2 = new File("test2.txt");
 this.prompt.sendLine("mkdir test");
 this.prompt.sendLine("cd test");
 this.prompt.sendLine("touch test1.txt");
 this.prompt.sendLine("touch test2.txt");
 this.prompt.sendLine("cd ..");
 FSPathResultList results = this.fsPath.query("//file[@name = 'test.*']//attribute[@name = 'size']");
 assertEquals(2, results.size());
 }
}