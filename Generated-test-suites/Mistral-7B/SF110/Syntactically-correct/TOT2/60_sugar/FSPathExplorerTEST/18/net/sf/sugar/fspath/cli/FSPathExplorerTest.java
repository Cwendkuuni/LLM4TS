import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {

 private File tempDirectory;
 private FSPathExplorer explorer;

 @Before
 public void setUp() throws IOException {
 tempDirectory = File.createTempDirectory("junit-fs-path-test", "");
 explorer = new FSPathExplorer();
 explorer.rootPath = tempDirectory;
 }

 @Test
 public void testSimpleQuery() throws IOException {
 String query = "//file[name = 'testFile.txt']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertTrue(results.size() > 0);
 File expectedFile = new File(tempDirectory, "testFile.txt");
 FSPathResult expectedResult = results.get(0);
 assertEquals(expectedFile.getAbsolutePath(), expectedResult.getFile().getAbsolutePath());
 }

 @Test
 public void testEmptyQuery() throws IOException {
 String query = "";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test
 public void testNegativeQuery() throws IOException {
 String query = "//file[name ! = 'testFile.txt']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertTrue(results.size() > 0);
 // Assume there is at least one file other than 'testFile.txt'
 File unexpectedFile = new File(tempDirectory, "unexpectedFile.txt");
 FSPathResult unexpectedResult = results.get(0);
 assertNotEquals(unexpectedFile.getAbsolutePath(), unexpectedResult.getFile().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery() throws IOException {
 String query = "//dir[@name = 'foo']//file[length > 10000";
 explorer.fsPath.query(query);
 fail("IOException should have been thrown");
 }

 @Test
 public void testEmptyDirectory() throws IOException {
 tempDirectory.delete();
 tempDirectory = new File(".");
 explorer.rootPath = tempDirectory;
 String query = "//file[name = 'testFile.txt']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test
 public void testPathCreation() throws IOException {
 File fileToCreate = new File(tempDirectory, "testFile.txt");
 fileToCreate.createNewFile();
 String query = "//file[name = 'testFile.txt']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertTrue(results.size() > 0);
 File expectedFile = new File(tempDirectory, "testFile.txt");
 FSPathResult expectedResult = results.get(0);
 assertEquals(expectedFile.getAbsolutePath(), expectedResult.getFile().getAbsolutePath());
 fileToCreate.delete();
 }

 @Test
 public void testCDCommand() throws IOException {
 String query = "cd " + tempDirectory.getAbsolutePath();
 explorer.prompt.writeLine(query);
 String expectedPath = tempDirectory.getParent().getAbsolutePath();
 String currentPath = explorer.prompt.readLine();
 assertEquals(expectedPath, currentPath);
 }

 @Test
 public void testHelpCommand() throws IOException {
 String query = Commands.HELP.getCommandText();
 explorer.prompt.writeLine(query);
 String output = explorer.prompt.readLine();
 // The output of this test is not shown here as it's quite long
 // but you can check it against the help message in the code
 // to ensure it's as expected.
 assertTrue(output.length() > 0);
 }

 @Test
 public void testExitCommand() throws IOException {
 String query = Commands.EXIT.getCommandText();
 explorer.prompt.writeLine(query);
 explorer.prompt.close();
 }

 @After
 public void tearDown() throws IOException {
 tempDirectory.delete();
 }
}