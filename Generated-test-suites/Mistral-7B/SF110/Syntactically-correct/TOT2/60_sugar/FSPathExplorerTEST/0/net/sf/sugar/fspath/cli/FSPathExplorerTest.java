import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.FSPathResult;
import net.sf.sugar.fspath.cli.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private String currentQuery;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 currentQuery = "";
 }

 @Test
 public void testConstructor() {
 // check that the constructor works correctly
 assertNotNull(explorer);
 }

 @Test
 public void testEmptyQuery() {
 currentQuery = "";
 List<FSPathResult> results = explorer.search(currentQuery);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testValidQuery() {
 // test simple query with a known result
 currentQuery = "/dir[@name = 'src']//file[@length > 10000]";
 File rootPath = new File("/home/user/projects/fspath");
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 explorer.createFSPath();
 List<FSPathResult> results = explorer.search(currentQuery);
 assertEquals(1, results.size());
 File resultFile = new File(results.get(0).getFile().getAbsolutePath());
 assertTrue(resultFile.exists());
 assertTrue(resultFile.length() > 10000);
 }

 @Test
 public void testInvalidQuery() {
 currentQuery = "//dir[@name = 'foo']//file[@length foo]";
 List<FSPathResult> results = explorer.search(currentQuery);
 assertTrue(results.isEmpty());
 }

 @Test(expected = IOException.class)
 public void testInvalidPath() {
 currentQuery = "";
 String invalidPath = "//non_existent_directory/file.txt";
 explorer.configureExplorer(new String[]{"-p", "/home/user/projects/fspath"});
 explorer.createFSPath();
 List<FSPathResult> results = explorer.search(invalidPath);
 fail("IOException expected but not thrown");
 }

 @Test
 public void testHelpCommand() {
 currentQuery = Commands.HELP.getCommandText();
 List<FSPathResult> results = explorer.search(currentQuery);
 assertEquals(1, results.size());
 String helpText = results.get(0).getFile().toString();
 assertTrue(helpText.contains("FSPathExplorer help :"));
 }

 @Test
 public void testFindCommand() {
 currentQuery = Commands.FIND.getCommandText() + " " + Commands.PWD.getCommandText();
 List<FSPathResult> results = explorer.search(currentQuery);
 assertEquals(1, results.size());
 String pwd = results.get(0).getFile().toString();
 File currentDir = new File(pwd);
 assertTrue(currentDir.isDirectory());
 }

 @Test
 public void testChangeDirectory() {
 currentQuery = Commands.CD.getCommandText() + " /home/user/temp";
 List<FSPathResult> results = explorer.search(currentQuery);
 assertTrue(results.isEmpty());

 explorer.configureExplorer(new String[]{"-p", "/home/user/projects/fspath"});
 explorer.createFSPath();
 currentQuery = Commands.PWD.getCommandText();
 List<FSPathResult> pwdResults = explorer.search(currentQuery);
 assertEquals(1, pwdResults.size());
 File startDir = new File(pwdResults.get(0).getFile().getAbsolutePath());

 currentQuery = Commands.CD.getCommandText() + "/temp";
 List<FSPathResult> cdResults = explorer.search(currentQuery);
 assertEquals(1, cdResults.size());
 File tempDir = new File(cdResults.get(0).getFile().getAbsolutePath());

 currentQuery = Commands.PWD.getCommandText();
 List<FSPathResult> finalPwdResults = explorer.search(currentQuery);
 assertEquals(1, finalPwdResults.size());
 File finalPwd = new File(finalPwdResults.get(0).getFile().getAbsolutePath());
 assertTrue(finalPwd.getAbsolutePath().startsWith(tempDir.getAbsolutePath()));
 }

 @Test
 public void testExitCommand() {
 currentQuery = Commands.EXIT.getCommandText();
 explorer.search(currentQuery);
 fail("System.exit() expected but not called");
 }

}