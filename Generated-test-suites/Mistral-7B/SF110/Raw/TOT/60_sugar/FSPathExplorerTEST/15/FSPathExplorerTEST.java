import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Commands;

public class FSPathExplorerTest {

 private FSPathExplorer fsPathExplorer;
 private FSPath fsPath;

 @Before
 public void setUp() {
 fsPathExplorer = new FSPathExplorer();
 fsPath = FSPathFactory.newFSPath(new File("src/test/resources"));
 fsPathExplorer.fsPath = fsPath;
 }

 @Test
 public void testPwdCommand() {
 String expected = "src/test/resources";
 String actual;

 fsPathExplorer.prompt = new Prompt(System.in, System.out, System.err);
 fsPathExplorer.prompt.setPromptText("fspath>");

 actual = fsPathExplorer.prompt.readLineAndConsumeNewline("PWD");

 assertEquals(expected, actual);
 }

 @Test
 public void testCdCommand() {
 String expected = "src/test/resources/testData";
 String query = "testData";

 fsPathExplorer.prompt = new Prompt(System.in, System.out, System.err);
 fsPathExplorer.prompt.setPromptText("fspath>");

 fsPathExplorer.prompt.writeLineAndConsumeNewline("CD " + query);
 String actual = fsPathExplorer.prompt.readLineAndConsumeNewline();

 assertEquals(expected, actual);
 }

 @Test
 public void testFindCommandSimple() {
 String expected = "src/test/resources/testData/file1.txt";
 String query = "//file[name = 'file1.txt']";

 FSPathResultList results = fsPathExplorer.query(query);
 String actual = results.get(0).getFile().getPath();

 assertEquals(expected, actual);
 }

 @Test
 public void testFindCommandComplex() {
 String expected = "src/test/resources/testData/dir1/file2.txt";
 String query = "//dir[@name = 'dir1']//file[name = 'file2.txt']";

 FSPathResultList results = fsPathExplorer.query(query);
 String actual = results.get(0).getFile().getPath();

 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testFindCommandNonExistentPath() {
 String query = "//dir[@name = 'nonExistentDirectory']";

 fsPathExplorer.query(query);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindCommandInvalidQuery() {
 String query = "//dir[fs:matches(@name, '^[.].*')]";

 fsPathExplorer.query(query);
 }

 @Test
 public void testHelpCommand() {
 String expected = "FSPathExplorer help :";
 //...

 fsPathExplorer.prompt = new Prompt(System.in, System.out, System.err);
 fsPathExplorer.prompt.setPromptText("fspath>");

 fsPathExplorer.prompt.writeLineAndConsumeNewline("HELP");
 String actual = fsPathExplorer.prompt.readLine();

 assertEquals(expected, actual.substring(0, expected.length() - 1));
 }

 @Test
 public void testExitCommand() {
 String expected = "exiting.....";

 fsPathExplorer.prompt = new Prompt(System.in, System.out, System.err);
 fsPathExplorer.prompt.setPromptText("fspath>");

 fsPathExplorer.prompt.writeLineAndConsumeNewline("EXIT");
 }
}