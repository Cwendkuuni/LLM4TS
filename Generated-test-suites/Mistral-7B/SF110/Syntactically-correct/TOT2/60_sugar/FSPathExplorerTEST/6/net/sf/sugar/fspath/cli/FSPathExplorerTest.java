import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
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
 private File rootPath;
 private Prompt prompt;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 rootPath = new File("/tmp");
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 explorer.createFSPath();
 fsPath = explorer.getFSPath();
 prompt = explorer.getPrompt();
 }

 @Test
 public void testValidQuery() throws IOException {
 String query = "//dir[@name = 'test']/file[@name = 'testFile.txt']";
 FSPathResultList results = fsPath.query(query);
 File expectedResult = new File(rootPath, "test/testFile.txt");
 assertTrue(results.size() > 0);
 assertEquals(expectedResult.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testInvalidQuery() throws IOException {
 String query = "//dir[@name = 'test']/file[@name = 'testFile.txt']dsfsdfs";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testEmptyQuery() throws IOException {
 String query = "";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testInvalidCommand() throws IOException {
 String query = "invalidCommand";
 try {
 fsPath.query(query);
 fail("Expected IOException not thrown");
 } catch (IOException e) {
 //expected
 }
 }

 @Test
 public void testHelpCommand() throws IOException {
 String query = Commands.HELP.getCommandText();
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.size() > 0);
 }

 @Test
 public void testPwdCommand() throws IOException {
 String query = Commands.PWD.getCommandText();
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.size() > 0);
 assertEquals(rootPath.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testCdCommand() throws IOException {
 String query = "cd test";
 fsPath.query(query);
 assertTrue(fsPath.getRootDirectory().getAbsolutePath().endsWith("test"));
 }

 @Test(expected = IOException.class)
 public void testCdToNonExistentDirectory() throws IOException {
 String query = "cd test123";
 fsPath.query(query);
 fail("Expected IOException not thrown");
 }

 @Test
 public void testInvalidPathArgument() throws IOException {
 String query = "cd ../.. //file[@length = 10]";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testValidPathArgument() throws IOException {
 String query = "cd ../.. //file[@length = 10]";
 fsPath.query("cd ../..");
 File expectedResult = new File(rootPath, ".." + File.separator + ".." + File.separator + "file" + File.separator + "test" + File.separator + "testFile.txt");
 assertTrue(expectedResult.exists());
 FSPathResultList results = fsPath.query("//file[@length = 10]");
 assertTrue(results.size() > 0);
 assertEquals(expectedResult.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testInvalidAttribute() throws IOException {
 String query = "//file[@length = 10]dsfsdfs";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testFindFileWithLengthAttribute() throws IOException {
 String query = "//file[@length = 10]";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.size() > 0);
 }

 @Test
 public void testFindFileWithNameAttribute() throws IOException {
 String query = "//file[@name = 'testFile.txt']";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.size() > 0);
 }

 @Test
 public void testFindDirectoryWithNameAttribute() throws IOException {
 String query = "//dir[@name = 'test']";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.size() > 0);
 }

 @Test
 public void testFindFileWithMultipleConditions() throws IOException {
 String query = "//file[@name = 'testFile.txt'][@length > 1000]";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.size() > 0);
 }

 @Test
 public void testFindDirectoryWithMultipleConditions() throws IOException {
 String query = "//dir[@name = 'test'][@attributes = 'd']";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.size() > 0);
 }

 @Test
 public void testFindFileWithPatternAttribute() throws IOException {
 String query = "//file[@name matches '.*\\.txt']";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.size() > 0);
 }

 @Test
 public void testFindDirectoryWithPatternAttribute() throws IOException {
 String query = "//dir[@name matches '.*test.*']";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.size() > 0);
 }

 @Test
 public void testFindWithInvalidAttributeSelector() throws IOException {
 String query = "//file[@invalidAttribute = 'test']";
 FSPathResultList results = fsPath.query(query);
 assertTrue(results.isEmpty());
 }

 @Test(expected = IOException.class)
 public void testFindWithInvalidPath() throws IOException {
 String query = "/../..//file[@name = 'testFile.txt']";
 FSPathResultList results = fsPath.query(query);
 fail("Expected IOException not thrown");
 }

}