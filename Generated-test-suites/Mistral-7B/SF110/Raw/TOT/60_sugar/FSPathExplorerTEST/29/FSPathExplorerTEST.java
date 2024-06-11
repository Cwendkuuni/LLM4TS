import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import java.io.File;
import java.io.IOException;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private File tempDir;

 @Before
 public void setUp() throws IOException {
 tempDir = File.createTempDirectory("FSPathExplorerTest", "");
 explorer = new FSPathExplorer();
 explorer.rootPath = tempDir;
 }

 @Test
 public void testEmptyQuery() throws IOException {
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testSimpleFind() throws IOException {
 explorer.createFSPath();
 File testFile = File.createTempFile("test", null);
 testFile.createNewFile();
 FSPathResult result = explorer.fsPath.query("//file[@name = 'test']").get(0);
 assertEquals(testFile.getAbsolutePath(), result.getFile().getPath());
 }

 @Test
 public void testFindInSubdirectory() throws IOException {
 explorer.createFSPath();
 File testDir = new File(tempDir, "testDir");
 testDir.mkdir();
 File testFile = new File(testDir, "testFile.txt");
 testFile.createNewFile();
 FSPathResult result = explorer.fsPath.query("//dir[@name = 'testDir']//file[@name = 'testFile.txt']").get(0);
 assertEquals(testFile.getAbsolutePath(), result.getFile().getPath());
 }

 @Test
 public void testFindInRootDirectory() throws IOException {
 explorer.createFSPath(null);
 File testFile = File.createTempFile("test", null);
 testFile.createNewFile();
 FSPathResult result = explorer.fsPath.query("//file[@name = 'test']").get(0);
 assertEquals(testFile.getAbsolutePath(), result.getFile().getPath());
 }

 @Test
 public void testFindWithMultipleMatches() throws IOException {
 explorer.createFSPath();
 File testFile1 = File.createTempFile("test1", null);
 testFile1.createNewFile();
 File testFile2 = File.createTempFile("test2", null);
 testFile2.createNewFile();
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'test']");
 assertEquals(2, results.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithInvalidQuery() throws IOException {
 explorer.createFSPath();
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'test']//@name = 'test2'");
 fail("expected IllegalArgumentException but got " + results.toString());
 }

 @Test
 public void testFindWithAttributes() throws IOException {
 explorer.createFSPath();
 File testFile = new File(tempDir, "testFile.txt");
 testFile.createNewFile();
 testFile.setReadOnly();
 FSPathResult result = explorer.fsPath.query("//file[@name = 'testFile.txt'][@readOnly = 'true']").get(0);
 assertEquals(testFile.getAbsolutePath(), result.getFile().getPath());
 }

 @Test
 public void testFindWithAttributesInSubdirectory() throws IOException {
 explorer.createFSPath();
 File testDir = new File(tempDir, "testDir");
 testDir.mkdir();
 File testFile = new File(testDir, "testFile.txt");
 testFile.createNewFile();
 testFile.setReadOnly();
 FSPathResult result = explorer.fsPath.query("//dir[@name = 'testDir']//file[@name = 'testFile.txt'][@readOnly = 'true']").get(0);
 assertEquals(testFile.getAbsolutePath(), result.getFile().getPath());
 }

 @Test
 public void testFindWithRegularExpression() throws IOException {
 explorer.createFSPath();
 File testFile1 = File.createTempFile("test1", null);
 testFile1.createNewFile();
 File testFile2 = new File(tempDir, "testRegularExpression.txt");
 testFile2.createNewFile();
 FSPathResult result = explorer.fsPath.query("//file[@name = 'test[123]']").get(0);
 assertEquals(testFile1.getAbsolutePath(), result.getFile().getPath());
 FSPathResult result2 = explorer.fsPath.query("//file[@name = 'testRegularExpression.txt']").get(0);
 assertEquals(testFile2.getAbsolutePath(), result2.getFile().getPath());
 }

 @Test(expected = IOException.class)
 public void testFindWithInvalidPath() throws IOException {
 explorer.createFSPath();
 FSPathResultList results = explorer.fsPath.query("//file[@name = '//']");
 fail("expected IOException but got " + results.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindWithInvalidAttribute() throws IOException {
 explorer.createFSPath();
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'test'][@attributeName = 'invalidAttribute']");
 fail("expected IllegalArgumentException but got " + results.toString());
 }

 @Test
 public void testChangeDirectory() throws IOException {
 explorer.createFSPath();
 File testDir = new File(tempDir, "testDir");
 testDir.mkdir();
 explorer.prompt.setWorkingDirectory(testDir);
 FSPathResult result = explorer.fsPath.query("//..").get(0);
 assertEquals(tempDir.getAbsolutePath(), result.getFile().getPath());
 }

 @Test
 public void testInvalidChangeDirectory() throws IOException {
 explorer.createFSPath();
 File testDir = new File(tempDir, "testDir");
 testDir.mkdir();
 explorer.prompt.setWorkingDirectory(testDir);
 FSPathResult result = explorer.fsPath.query("//../..").get(0);
 fail("expected IOException but got " + result.getFile().getPath());
 }

 @Test
 public void testInvalidCommand() throws IOException {
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "invalidCommand";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 fail("expected IllegalArgumentException but got " + results.toString());
 }

 @Test
 public void testHelpCommand() throws IOException {
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "help";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 for (FSPathResult result : results) {
 System.out.println(result.getFile().getPath());
 }
 }

 @Test
 public void testExitCommand() throws IOException {
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "exit";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testInvalidExpression() throws IOException {
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "//file[@name = 'test'][10]";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 fail("expected IllegalArgumentException but got " + results.toString());
 }

 @Test
 public void testFindWithMultipleAttributes() throws IOException {
 explorer.createFSPath();
 File testFile = new File(tempDir, "testFile.txt");
 testFile.createNewFile();
 testFile.setReadOnly();
 testFile.setHidden();
 FSPathResult result = explorer.fsPath.query("//file[@name = 'testFile.txt'][@readOnly = 'true'][@hidden = 'true']").get(0);
 assertEquals(testFile.getAbsolutePath(), result.getFile().getPath());
 }

 @Test
 public void testFindWithMultipleAttributesInSubdirectory() throws IOException {
 explorer.createFSPath();
 File testDir = new File(tempDir, "testDir");
 testDir.mkdir();
 File testFile = new File(testDir, "testFile.txt");
 testFile.createNewFile();
 testFile.setReadOnly();
 testFile.setHidden();
 FSPathResult result = explorer.fsPath.query("//dir[@name = 'testDir']//file[@name = 'testFile.txt'][@readOnly = 'true'][@hidden = 'true']").get(0);
 assertEquals(testFile.getAbsolutePath(), result.getFile().getPath());
 }
}