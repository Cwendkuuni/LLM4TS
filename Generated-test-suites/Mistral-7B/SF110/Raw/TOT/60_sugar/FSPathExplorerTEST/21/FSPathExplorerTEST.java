import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import org.junit.Assert;
import org.junit.Test;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;

 @Test
 public void testDefaultBehavior() {
 explorer = new FSPathExplorer();

 // By default, no root path or query is set
 Assert.assertNull(explorer.getRootPath());
 Assert.assertEquals("", explorer.getFsPathQuery());

 // Create FSPath and check that it returns an empty result list
 fsPath = explorer.createFSPath();
 FSPathResultList results = fsPath.query(explorer.getFsPathQuery());
 Assert.assertEquals(0, results.size());
 }

 @Test
 public void testValidConfiguration() {
 explorer = new FSPathExplorer();

 // Configure with root path and query
 File rootPath = new File("/tmp");
 explorer.setRootPath(rootPath);
 explorer.setFsPathQuery("//dir[@name = 'test']");

 // Create FSPath and check that it returns the expected result
 fsPath = explorer.createFSPath();
 FSPathResultList results = fsPath.query(explorer.getFsPathQuery());
 File expectedResult = new File("/tmp/test");
 Assert.assertEquals(1, results.size());
 Assert.assertTrue(results.get(0).getFile().equals(expectedResult));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullRootPath() {
 explorer = new FSPathExplorer();
 explorer.setRootPath(null);
 explorer.createFSPath();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyRootPath() {
 explorer = new FSPathExplorer();
 explorer.setRootPath(new File(""));
 explorer.createFSPath();
 }

 @Test(expected = IOException.class)
 public void testInvalidRootPath() {
 explorer = new FSPathExplorer();
 explorer.setRootPath(new File("not_existing_directory"));
 explorer.createFSPath();
 }

 @Test
 public void testValidQuery() {
 explorer = new FSPathExplorer();

 // Configure with root path and query
 File rootPath = new File("/tmp");
 explorer.setRootPath(rootPath);
 explorer.setFsPathQuery("//file[@name = 'test.txt']");

 // Create FSPath and check that it returns the expected result
 fsPath = explorer.createFSPath();
 FSPathResultList results = fsPath.query(explorer.getFsPathQuery());
 File expectedResult = new File("/tmp/test.txt");
 Assert.assertEquals(1, results.size());
 Assert.assertTrue(results.get(0).getFile().equals(expectedResult));
 }

 @Test
 public void testEmptyQuery() {
 explorer = new FSPathExplorer();

 // Configure with root path and empty query
 File rootPath = new File("/tmp");
 explorer.setRootPath(rootPath);
 explorer.setFsPathQuery("");

 // Create FSPath and check that it returns an empty result list
 fsPath = explorer.createFSPath();
 FSPathResultList results = fsPath.query(explorer.getFsPathQuery());
 Assert.assertEquals(0, results.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidQuery() {
 explorer = new FSPathExplorer();

 // Configure with root path and invalid query
 File rootPath = new File("/tmp");
 explorer.setRootPath(rootPath);
 explorer.setFsPathQuery("//invalid_query");
 explorer.createFSPath();
 }

 @Test
 public void testPromptBehavior() {
 explorer = new FSPathExplorer();
 PrintStream out = new PrintStream(new ByteArrayOutputStream());
 PrintStream err = new PrintStream(new ByteArrayOutputStream());
 explorer.setOut(out);
 explorer.setErr(err);
 Prompt prompt = new Prompt(System.in, out, err);
 String query = "//dir[@name = 'test']";
 explorer.start();
 prompt.write("Enter an FSPath query to search the filesystem : " + query + "\n");
 prompt.write("> ");
 String input = "pwd\n";
 prompt.readLine();
 Assert.assertEquals("Current directory : " + explorer.getRootPath().getAbsolutePath() + "\n", out.getBytes());
 input = "cd /tmp\n";
 prompt.readLine();
 Assert.assertEquals("changed to : /tmp\n", out.getBytes());
 input = query + "\n";
 prompt.readLine();
 FSPathResultList results = fsPath.query(explorer.getFsPathQuery());
 File expectedResult = new File("/tmp/test");
 Assert.assertEquals(1, results.size());
 Assert.assertTrue(results.get(0).getFile().equals(expectedResult));
 }

}