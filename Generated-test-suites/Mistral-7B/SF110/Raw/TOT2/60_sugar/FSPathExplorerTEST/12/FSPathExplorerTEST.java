import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
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
 private Prompt prompt;
 private FSPath fsPath;
 private File rootPath;
 private String fsPathQuery;

 @Before
 public void setUp() throws Exception {
 explorer = new FSPathExplorer();
 fsPath = explorer.getFSPath();
 rootPath = new File("./src/test/java");
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 explorer.createFSPath();
 prompt = explorer.getPrompt();
 }

 @Test
 public void testPwdCommand() {
 String expected = rootPath.getAbsolutePath();
 String result = explorer.promptAndExecute("pwd");
 assertEquals(expected, result);
 }

 @Test
 public void testCdCommandValidDirectory() throws IOException {
 File newDir = new File(rootPath, "test");
 newDir.mkdir();
 explorer.configureExplorer(new String[]{"-cd", newDir.getAbsolutePath()});
 assertEquals(newDir.getAbsolutePath(), explorer.getFSPath().getRootDirectory().getAbsolutePath());
 newDir.delete();
 }

 @Test(expected = IOException.class)
 public void testCdCommandInvalidDirectory() throws IOException {
 explorer.configureExplorer(new String[]{"-cd", "/nonexistingdirectory"});
 fail("Should have thrown an IOException");
 }

 @Test
 public void testFindSimpleQuery() throws IOException {
 String expected = rootPath.getAbsolutePath() + "/test/FSPathExplorerTest.java";
 String query = "//file[@name = 'FSPathExplorerTest.java']";
 String result = explorer.promptAndExecute(query);
 assertEquals(expected, result);
 }

 @Test
 public void testFindComplexQuery() throws IOException {
 String expected = rootPath.getAbsolutePath() + "/test/java/net/sf/sugar/fspath/cli/FSPathExplorer.java";
 String query = "//file[@name = 'FSPathExplorer.java']";
 String result = explorer.promptAndExecute(query);
 assertEquals(expected, result);
 }

 @Test
 public void testFindNonExistentQuery() throws IOException {
 String query = "//file[@name = 'NonExistentFile.java']";
 String result = explorer.promptAndExecute(query);
 assertNull(result);
 }

 @Test
 public void testFindEmptyQuery() throws IOException {
 String query = "";
 String result = explorer.promptAndExecute(query);
 assertNull(result);
 }

 @Test
 public void testHelpCommand() throws IOException {
 String result = explorer.promptAndExecute("help");
 assertTrue(result.contains("FSPathExplorer help :"));
 }

 @Test
 public void testExitCommand() throws IOException {
 explorer.promptAndExecute("exit");
 }

 @After
 public void tearDown() throws Exception {
 explorer.shutdown();
 }

}