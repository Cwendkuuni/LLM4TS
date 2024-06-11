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

 @Before
 public void setUp() {
 this.explorer = new FSPathExplorer();
 this.explorer.out = System.out;
 this.explorer.err = System.err;
 this.explorer.prompt = new Prompt(System.in, this.explorer.out, this.explorer.err);
 this.explorer.prompt.setPromptText("fspath>");
 this.fsPath = FSPathFactory.newFSPath();
 }

 @Test
 public void testEmptyQuery() {
 String query = "";
 FSPathResultList results = this.fsPath.query(query);
 Assert.assertEquals(0, results.size());
 }

 @Test
 public void testSimpleDirQuery() {
 String query = "//dir[@name = 'bin']";
 FSPathResultList results = this.fsPath.query(query);
 File expectedDir = new File("./bin");
 Assert.assertTrue(expectedDir.exists());
 Assert.assertTrue(expectedDir.isDirectory());
 Assert.assertEquals(expectedDir.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testSimpleFileQuery() {
 String query = "//file[contains(name, 'config.properties')]";
 FSPathResultList results = this.fsPath.query(query);
 File expectedFile = new File("./config.properties");
 Assert.assertTrue(expectedFile.exists());
 Assert.assertEquals(expectedFile.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testComplexQuery() {
 String query = "//dir[@name = 'src']//file[@length > 10000]";
 FSPathResultList results = this.fsPath.query(query);
 File expectedFile = new File("./src/largeFile.txt");
 Assert.assertTrue(expectedFile.exists());
 Assert.assertEquals(expectedFile.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery() {
 String query = "//dir[@name = 'invalid']//file[@length > 1000000]";
 FSPathResultList results = this.fsPath.query(query);
 }

 @Test
 public void testCDCommand() {
 String query = "cd ../";
 this.explorer.configureExplorer(new String[]{"-p", "."});
 this.explorer.createFSPath();
 this.explorer.start();
 this.explorer.prompt.write("cd ../\n");
 this.explorer.prompt.readLine();
 File expectedDir = new File("../");
 FSPathResultList results = this.fsPath.query("pwd");
 Assert.assertEquals(expectedDir.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testHelpCommand() {
 this.explorer.configureExplorer(new String[]{"-f", "help"});
 this.explorer.start();
 String expectedHelpText = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\n";
 String actualHelpText = this.explorer.prompt.readLine();
 Assert.assertEquals(expectedHelpText, actualHelpText);
 }

 @After
 public void tearDown() {
 this.explorer.prompt.close();
 }
}