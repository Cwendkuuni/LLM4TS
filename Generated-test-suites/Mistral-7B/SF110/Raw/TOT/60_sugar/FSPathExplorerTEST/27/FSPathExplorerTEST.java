import org.junit.Assert;
import org.junit.Test;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private ByteArrayOutputStream outContent;
 private ByteArrayOutputStream errContent;
 private ByteArrayInputStream inContent;

 @Test
 public void testBasicFunctionality() throws IOException {
  explorer = new FSPathExplorer();
  outContent = new ByteArrayOutputStream();
  errContent = new ByteArrayOutputStream();
  inContent = new ByteArrayInputStream(("p /home/user\n").getBytes()); // set working directory to /home/user
  explorer.createFSPath();
  explorer.configureExplorer(new String[0]);
  explorer.setIn(inContent);
  explorer.setOut(new PrintStream(outContent));
  explorer.setErr(new PrintStream(errContent));
  explorer.start();
  String expectedOutput = "Current directory : /home/user\n";
  Assert.assertEquals("Incorrect output when setting working directory.", expectedOutput, outContent.toString());
  Assert.assertTrue("Should not have any error messages.", errContent.toString().isEmpty());
 }

 @Test
 public void testInvalidCommand() throws IOException {
  explorer = new FSPathExplorer();
  outContent = new ByteArrayOutputStream();
  errContent = new ByteArrayOutputStream();
  inContent = new ByteArrayInputStream(("p /home/user\ninvalidcommmand\n").getBytes());
  explorer.createFSPath();
  explorer.configureExplorer(new String[0]);
  explorer.setIn(inContent);
  explorer.setOut(new PrintStream(outContent));
  explorer.setErr(new PrintStream(errContent));
  explorer.start();
  String expectedErrorMessage = "invalidcommmand is not a valid command.\n";
  Assert.assertEquals("Incorrect error message when entering an invalid command.", expectedErrorMessage, errContent.toString());
 }

 @Test
 public void testCDCommand() throws IOException {
  explorer = new FSPathExplorer();
  outContent = new ByteArrayOutputStream();
  errContent = new ByteArrayOutputStream();
  inContent = new ByteArrayInputStream(("p /home/user\ncd /tmp\nls\nquit\n").getBytes());
  explorer.createFSPath();
  explorer.configureExplorer(new String[0]);
  explorer.setIn(inContent);
  explorer.setOut(new PrintStream(outContent));
  explorer.setErr(new PrintStream(errContent));
  explorer.start();
  String expectedDirectoryOutput = "Current directory : /tmp\n";
  String expectedFileOutput = ".";
  Assert.assertEquals("Incorrect output when changing directory.", expectedDirectoryOutput, outContent.toString().substring(0, outContent.toString().indexOf(expectedFileOutput)));
  Assert.assertTrue("Should not have any error messages.", errContent.toString().isEmpty());
 }

 @Test
 public void testFindCommand() throws IOException {
  explorer = new FSPathExplorer();
  outContent = new ByteArrayOutputStream();
  errContent = new ByteArrayOutputStream();
  inContent = new ByteArrayInputStream(("p /home/user\nfind . -name '*.txt'\nquit\n").getBytes()); // find all .txt files in the current directory
  explorer.createFSPath();
  explorer.configureExplorer(new String[0]);
  explorer.setIn(inContent);
  explorer.setOut(new PrintStream(outContent));
  explorer.setErr(new PrintStream(errContent));
  explorer.start();
  String expectedFileOutput = "./file1.txt\n./file2.txt\n"; // assuming there are two .txt files in the current directory
  Assert.assertEquals("Incorrect output when executing a find command.", expectedFileOutput, outContent.toString().substring(outContent.toString().indexOf(expectedFileOutput)));
 }

 @Test
 public void testHelpCommand() throws IOException {
  explorer = new FSPathExplorer();
  outContent = new ByteArrayOutputStream();
  errContent = new ByteArrayOutputStream();
  inContent = new ByteArrayInputStream(("p /home/user\nhelp\nquit\n").getBytes());
  explorer.createFSPath();
  explorer.configureExplorer(new String[0]);
  explorer.setIn(inContent);
  explorer.setOut(new PrintStream(outContent));
  explorer.setErr(new PrintStream(errContent));
  explorer.start();
  String expectedHelpOutput = "FSPathExplorer help :\n\n"; // print the help text
  Assert.assertEquals("Incorrect output when entering the help command.", expectedHelpOutput, outContent.toString());
 }

 @Test
 public void testInvalidPath() throws IOException {
  explorer = new FSPathExplorer();
  outContent = new ByteArrayOutputStream();
  errContent = new ByteArrayOutputStream();
  inContent = new ByteArrayInputStream(("p /home/user\nfind /nonexistent/directory -name '*.txt'\nquit\n").getBytes());
  explorer.createFSPath();
  explorer.configureExplorer(new String[0]);
  explorer.setIn(inContent);
  explorer.setOut(new PrintStream(outContent));
  explorer.setErr(new PrintStream(errContent));
  explorer.start();
  String expectedErrorMessage = "Error: Could not find or list directory /nonexistent/directory\n";
  Assert.assertEquals("Incorrect error message when trying to search a non-existent directory.", expectedErrorMessage, errContent.toString());
 }
}