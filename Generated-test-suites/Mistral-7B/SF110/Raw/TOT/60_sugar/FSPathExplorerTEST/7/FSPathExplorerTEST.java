import org.junit.Assert;
import org.junit.Test;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import java.io.File;
import java.io.IOException;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;

 @Test
 public void testSimpleQuery() {
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"", "-find", "test/TestFile.java"});
 explorer.createFSPath();
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'TestFile.java']");

 Assert.assertEquals(1, results.size());
 File expected = new File("test/TestFile.java");
 Assert.assertEquals(expected.getAbsolutePath(), results.get(0).getFile().getPath());
 }

 @Test
 public void testNullQuery() {
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"", "-find", null});
 explorer.createFSPath();

 try {
 explorer.fsPath.query(null);
 FailureException.fail("Expected IOException to be thrown");
 } catch (IOException e) {
 //pass
 }
 }

 @Test
 public void testNonExistentQuery() {
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"", "-find", "nonExistentFile.txt"});
 explorer.createFSPath();
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'nonExistentFile.txt']");

 Assert.assertEquals(0, results.size());
 }

 @Test
 public void testEmptyDirectoryQuery() {
 explorer = new FSPathExplorer();
 File emptyDirectory = new File("test/empty");
 File emptyFile = new File("test/empty/emptyFile.txt");
 emptyDirectory.mkdirs();
 emptyFile.createNewFile();
 explorer.configureExplorer(new String[]{"", "-find", "test/empty"});
 explorer.createFSPath();
 FSPathResultList results = explorer.fsPath.query("//file");

 Assert.assertEquals(1, results.size());
 Assert.assertEquals(emptyFile.getAbsolutePath(), results.get(0).getFile().getPath());
 }

 @Test
 public void testChangeDirectory() {
 explorer = new FSPathExplorer();
 File root = new File("test");
 File newDirectory = new File(root, "newDirectory");
 File testFile = new File(root, "TestFile.java");
 newDirectory.mkdirs();
 testFile.createNewFile();
 explorer.configureExplorer(new String[]{"", "-find", "", "-cd", "newDirectory"});
 explorer.createFSPath();
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'TestFile.java']");

 Assert.assertEquals(1, results.size());
 Assert.assertEquals(testFile.getAbsolutePath(), results.get(0).getFile().getPath());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidChangeDirectory() {
 explorer = new FSPathExplorer();
 File invalidDirectory = new File("test/invalidDirectory");
 explorer.configureExplorer(new String[]{"", "-find", "", "-cd", invalidDirectory.getPath()});
 explorer.createFSPath();
 }

 @Test
 public void testHelpCommand() {
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"", "-help"});
 explorer.start();
 String output = "";
 try {
 output = explorer.out.readLine();
 } catch (IOException e) {
 e.printStackTrace();
 }
 Assert.assertTrue(output.contains("FSPathExplorer help :"));
 }

 @Test
 public void testQuitCommand() {
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"", "-find", "test/TestFile.java"});
 explorer.createFSPath();
 explorer.start();
 explorer.prompt.readLine();
 explorer.prompt.readLine();
 explorer.prompt.readLine();
 explorer.prompt.readLine();
 explorer.prompt.readLine();
 explorer.prompt.readLine().trim().equals(Commands.EXIT.getCommandText())
         && explorer.prompt.close();
 }
}