import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Commands;
import java.io.File;
import java.io.IOException;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private String rootPath;

 @Before
 public void setup() {
 explorer = new FSPathExplorer();
 rootPath = new File(".").getAbsolutePath();
 explorer.configureExplorer(new String[]{"-p", rootPath});
 }

 @Test
 public void testConstructor() {
 assertEquals(rootPath, explorer.rootPath.getAbsolutePath());
 assertNull(explorer.fsPathQuery);
 }

 @Test
 public void testShowWelcome() {
 assertEquals(true, explorer.showWelcome().contains("Scanning filesystem metadata..."));
 }

 @Test
 public void testShowHelp() {
 String helpText = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nAttributes : (see javadoc for java.io.File for more info)\n";
 for (FSPathAttributes a : FSPathAttributes.values()) {
 helpText += "\n@" + a;
 }
 helpText += "\n\nCommands :\n";
 for (Commands c : Commands.values()) {
 helpText += "\n" + c.getCommandText() + " : " + c.getDescription();
 }
 assertEquals(true, explorer.showHelp().contains(helpText));
 }

 @Test
 public void testQueryEmptyQuery() {
 String query = "";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test
 public void testQuerySimplePath() {
 String query = "/home/user/Desktop";
 FSPathResultList results = explorer.fsPath.query(query);
 File expectedFile = new File(query);
 FSPathResult result = results.get(0);
 assertEquals(expectedFile.getAbsolutePath(), result.getFile().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testQueryNonExistentPath() {
 String query = "/nonExistentPath";
 FSPathResultList results = explorer.fsPath.query(query);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testQueryInvalidPath() {
 String query = "invalidPath";
 FSPathResultList results = explorer.fsPath.query(query);
 }

 @Test
 public void testQuerySimpleQuery() {
 String query = "//dir[@name = 'foo']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(1, results.size());
 }

 @Test
 public void testQueryComplexQuery() {
 String query = "//dir[@name = 'src']//file[@length > 10000]";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(1, results.size());
 }

 @Test
 public void testCdCommand() {
 File tempDir = new File("temp");
 tempDir.mkdir();
 explorer.configureExplorer(new String[]{"-p", rootPath, "-f", "cd", "temp"});
 assertEquals(tempDir.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
 tempDir.delete();
 }

 @Test
 public void testPwdCommand() {
 explorer.configureExplorer(new String[]{"-p", rootPath, "-f", "pwd"});
 assertEquals(rootPath, explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testHelpCommand() {
 explorer.configureExplorer(new String[]{"-p", rootPath, "-f", "help"});
 }

 @Test
 public void testExitCommand() {
 explorer.configureExplorer(new String[]{"-p", rootPath, "-f", "exit"});
 }
}