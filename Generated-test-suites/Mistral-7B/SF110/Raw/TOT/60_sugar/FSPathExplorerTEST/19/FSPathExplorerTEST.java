import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for FSPathExplorer
 */
public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private ByteArrayOutputStream outContent;
 private ByteArrayOutputStream errContent;
 private ByteArrayInputStream inContent;
 private Prompt prompt;

 @Before
 public void setUp() throws IOException {
 // initialize test objects
 this.outContent = new ByteArrayOutputStream();
 this.errContent = new ByteArrayOutputStream();
 this.inContent = new ByteArrayInputStream(new byte[0]);
 this.prompt = new Prompt(this.inContent, new PrintStream(this.outContent), new PrintStream(this.errContent));
 this.explorer = new FSPathExplorer();
 this.explorer.out = new PrintStream(this.outContent);
 this.explorer.err = new PrintStream(this.errContent);
 this.explorer.prompt = this.prompt;
 }

 @Test
 public void testEmptyQuery() throws IOException {
 this.explorer.createFSPath();
 this.explorer.start();
 this.prompt.readLineAndConsume(); // consume "fspath>"
 this.prompt.readLineAndConsume(); // consume "Scanning filesystem metadata..."
 this.prompt.readLineAndConsume(); // consume "Type 'help' for commands..."
 this.prompt.readLineAndConsume(); // consume "enter a query"
 this.prompt.readLineAndConsume(); // consume empty query
 String output = this.outContent.toString();
 assertTrue(output.contains("Scanning filesystem metadata..."));
 assertTrue(output.contains("Type 'help' for commands..."));
 assertTrue(output.contains("enter a query"));
 assertTrue(output.contains(""));
 }

 @Test
 public void testInvalidQuery() throws IOException {
 this.explorer.createFSPath();
 this.createFSPath();
 this.explorer.start();
 this.prompt.readLineAndConsume(); // consume "fspath>"
 this.prompt.readLineAndConsume(); // consume "Scanning filesystem metadata..."
 this.prompt.readLineAndConsume(); // consume "Type 'help' for commands..."
 this.prompt.readLineAndConsume(); // consume "enter a query"
 this.prompt.readLineAndWrite("//invalid"); // write invalid query
 String output = this.outContent.toString();
 assertTrue(output.contains("Scanning filesystem metadata..."));
 assertTrue(output.contains("Type 'help' for commands..."));
 assertTrue(output.contains("enter a query"));
 assertTrue(output.contains("Invalid query: //invalid"));
 }

 @Test
 public void testSimpleFindDir() throws IOException {
 File tempDir = File.createTempFile("test", null);
 File testFile = new File(tempDir, "testFile.txt");
 tempDir.mkdir();
 testFile.createNewFile();
 this.explorer.createFSPath();
 this.explorer.start();
 this.prompt.readLineAndConsume(); // consume "fspath>"
 this.prompt.readLineAndConsume(); // consume "Scanning filesystem metadata..."
 this.prompt.readLineAndConsume(); // consume "Type 'help' for commands..."
 this.prompt.readLineAndConsume(); // consume "enter a query"
 this.prompt.readLineAndWrite("//dir[@name = '." + tempDir.getName() + "']/file[@name = 'testFile.txt']"); // write query
 String output = this.outContent.toString();
 assertTrue(output.contains("testFile.txt"));
 }

 @Test
 public void testSimpleFindDirWithErrors() throws IOException {
 File tempDir = File.createTempFile("test", null);
 File testFile = new File(tempDir, "testFile.txt");
 tempDir.mkdir();
 testFile.createNewFile();
 this.explorer.createFSPath();
 this.explorer.start();
 this.prompt.readLineAndConsume(); // consume "fspath>"
 this.prompt.readLineAndConsume(); // consume "Scanning filesystem metadata..."
 this.prompt.readLineAndConsume(); // consume "Type 'help' for commands..."
 this.prompt.readLineAndConsume(); // consume "enter a query"
 this.prompt.readLineAndWrite("//dir[@name = '." + tempDir.getName() + "']/file[@name = 'nonexistent']"); // write query with error
 String output = this.outContent.toString();
 assertTrue(output.contains("No results for query"));
 }

 @Test
 public void testFindMultilevelDir() throws IOException {
 File testDir1 = new File("test1");
 File testDir2 = new File(testDir1, "test2");
 File testFile = new File(testDir2, "testFile.txt");
 testDir1.mkdir();
 testDir2.mkdir();
 testFile.createNewFile();
 this.explorer.createFSPath();
 this.explorer.start();
 this.prompt.readLineAndConsume(); // consume "fspath>"
 this.prompt.readLineAndConsume(); // consume "Scanning filesystem metadata..."
 this.prompt.readLineAndConsume(); // consume "Type 'help' for commands..."
 this.prompt.readLineAndConsume(); // consume "enter a query"
 this.prompt.readLineAndWrite("//dir[@name = 'test1']/dir[@name = 'test2']/file[@name = 'testFile.txt']"); // write query
 String output = this.outContent.toString();
 assertTrue(output.contains("testFile.txt"));
 }

 @Test
 public void testFindMultilevelDirWithErrors() throws IOException {
 File testDir1 = new File("test1");
 File testDir2 = new File(testDir1, "test2");
 File testFile = new File(testDir2, "testFile.txt");
 testDir1.mkdir();
 testDir2.mkdir();
 testFile.createNewFile();
 this.explorer.createFSPath();
 this.explorer.start();
 this.prompt.readLineAndConsume(); // consume "fspath>"
 this.prompt.readLineAndConsume(); // consume "Scanning filesystem metadata..."
 this.prompt.readLineAndConsume(); // consume "Type 'help' for commands..."
 this.prompt.readLineAndConsume(); // consume "enter a query"
 this.prompt.readLineAndWrite("//dir[@name = 'test1']/dir[@name = 'test3']/file[@name = 'testFile.txt']"); // write query with error
 String output = this.outContent.toString();
 assertTrue(output.contains("No results for query"));
 }

 @Test
 public void testFindInvalidPath() throws IOException {
 File tempFile = new File("nonexistent");
 this.explorer.createFSPath();
 this.explorer.start();
 this.prompt.readLineAndConsume(); // consume "fspath>"
 this.prompt.readLineAndConsume(); // consume "Scanning filesystem metadata..."
 this.prompt.readLineAndConsume(); // consume "Type 'help' for commands..."
 this.prompt.readLineAndConsume(); // consume "enter a query"
 this.prompt.readLineAndWrite("//dir[@name = 'nonexistent']"); // write query with error
 String output = this.outContent.toString();
 assertTrue(output.contains("No results for query"));
 }

 @Test
 public void testHelpCommand() throws IOException {
 this.explorer.createFSPath();
 this.explorer.start();
 this.prompt.readLineAndConsume(); // consume "fspath>"
 this.prompt.readLineAndConsume(); // consume "Scanning filesystem metadata..."
 this.prompt.readLineAndConsume(); // consume "Type 'help' for commands..."
 String output = this.outContent.toString();
 assertTrue(output.contains("FSPathExplorer help"));
 }

 @Test
 public void testExitCommand() throws IOException {
 this.explorer.createFSPath();
 this.explorer.start();
 this.prompt.readLineAndConsume(); // consume "fspath>"
 this.prompt.readLineAndConsume(); // consume "Scanning filesystem metadata..."
 this.prompt.readLineAndConsume(); // consume "Type 'help' for commands..."
 this.prompt.readLineAndWrite(Commands.EXIT.getCommandText()); // write exit command
 String output = this.outContent.toString();
 assertTrue(output.contains("exiting....."));
 }
}