import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer fsPathExplorer;
 private FSPath fsPath;

 @Before
 public void setUp() {
 fsPathExplorer = new FSPathExplorer();
 fsPath = FSPathFactory.newFSPath();
 }

 @Test
 public void testEmptyQuery() {
 FSPathResultList results = fsPathExplorer.query(fsPath, "");
 Assert.assertEquals(0, results.size());
 }

 @Test
 public void testSimpleQuery() {
 File root = new File("/");
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.configureExplorer(new String[]{"-p", root.getAbsolutePath(), "-f", "//file[@name = 'test.txt']"});
 fsPathExplorer.createFSPath();
 FSPathResultList results = fsPathExplorer.query(fsPath, "");
 Assert.assertEquals(1, results.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidQuery() {
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.query(fsPath, "//dir[@name = 'root']/!file");
 }

 @Test(expected = NullPointerException.class)
 public void testNullQuery() {
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.query(fsPath, null);
 }

 @Test
 public void testFindCommand() {
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.configureExplorer(new String[]{"-f", "find"});
 fsPathExplorer.createFSPath();
 String query = fsPathExplorer.prompt.readLine();
 Assert.assertEquals(Commands.FIND.getCommandText(), query);
 }

 @Test
 public void testPathCommand() {
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.configureExplorer(new String[]{"-p", "/tmp"});
 fsPathExplorer.createFSPath();
 String query = fsPathExplorer.prompt.readLine();
 Assert.assertEquals(Commands.PATH.getCommandText(), query);
 }

 @Test
 public void testPwdCommand() {
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.query(fsPath, Commands.PWD.getCommandText());
 }

 @Test
 public void testCdCommand() {
 File tempDir = new File(System.getProperty("java.io.tmpdir"));
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.query(fsPath, Commands.CD.getCommandText() + " " + tempDir.getAbsolutePath());
 }

 @Test
 public void testHelpCommand() {
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.query(fsPath, Commands.HELP.getCommandText());
 }

 @Test
 public void testQuitCommand() {
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.query(fsPath, Commands.EXIT.getCommandText());
 }

}