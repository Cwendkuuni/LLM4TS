import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import java.io.File;
import java.io.IOException;

public class FSPathExplorerTest {

 private FSPathExplorer fsPathExplorer;
 private FSPath fsPath;
 private String currentDirectory;

 @Before
 public void setup() {
 fsPathExplorer = new FSPathExplorer();
 fsPath = fsPathExplorer.getFSPath();
 currentDirectory = System.getProperty("user.dir");
 }

 @Test
 public void testPwdCommand() {
 assertEquals(currentDirectory, fsPathExplorer.executeCommand(Commands.PWD));
 }

 @Test
 public void testCdCommand() {
 File testDirectory = new File("src/test/resources");
 fsPathExplorer.executeCommand(Commands.CD, testDirectory.getAbsolutePath());
 assertEquals(testDirectory.getAbsolutePath(), fsPathExplorer.getCurrentDirectory());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCdCommandInvalidDirectory() {
 fsPathExplorer.executeCommand(Commands.CD, "invalidDirectory");
 }

 @Test
 public void testHelpCommand() {
 fsPathExplorer.executeCommand(Commands.HELP);
 }

 @Test
 public void testFindCommandSimpleQuery() {
 String query = "//file[name = 'test.txt']";
 FSPathResultList results = fsPath.query(query);
 assertEquals(1, results.size());
 }

 @Test
 public void testFindCommandComplexQuery() {
 String query = "//dir[@name = 'src']//file[@name = '*Test.java']";
 FSPathResultList results = fsPath.query(query);
 //Assuming that there exists at least one test file under src directory
 assertEquals(results.size(), 1);
 }

 @Test(expected = IOException.class)
 public void testFindCommandInvalidQuery() {
 String query = "//file[@name = 'invalidQuery']";
 fsPath.query(query);
 }

 @Test
 public void testExitCommand() {
 fsPathExplorer.executeCommand(Commands.EXIT);
 }
}