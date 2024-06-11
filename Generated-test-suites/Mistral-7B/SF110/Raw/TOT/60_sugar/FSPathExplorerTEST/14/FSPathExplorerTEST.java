import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
 explorer = new FSPathExplorer();
 fsPath = FSPathFactory.newFSPath();
 }

 @Test
 public void testEmptyQuery() {
 FSPathResultList results = explorer.query(fsPath, "");
 assertNotNull(results);
 assertEquals(0, results.size());
 }

 @Test
 public void testSimpleQuery() {
 File root = new File(".");
 fsPath = FSPathFactory.newFSPath(root);
 String query = "//dir[@name = 'src']";
 FSPathResultList results = explorer.query(fsPath, query);
 assertNotNull(results);
 assertEquals(1, results.size());
 File expected = new File("src");
 assertEquals(expected.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testQueryWithError() {
 File root = new File(".");
 fsPath = FSPathFactory.newFSPath(root);
 String query = "//file[@name = 'xxxxxxxxxxxxxxxxxxxxxx']";
 FSPathResultList results = explorer.query(fsPath, query);
 assertNotNull(results);
 assertEquals(0, results.size());
 }

 @Test
 public void testQueryWithMultipleResults() {
 File root = new File("/tmp");
 fsPath = FSPathFactory.newFSPath(root);
 String query = "//file[@length > 10000]";
 FSPathResultList results = explorer.query(fsPath, query);
 assertNotNull(results);
 assertTrue(results.size() > 0);
 }

 @Test
 public void testChangeDirectory() throws IOException {
 File root = new File("/tmp");
 File newDir = new File("/tmp/test");
 fsPath = FSPathFactory.newFSPath(root);
 explorer.changeDirectory(fsPath, newDir.getAbsolutePath());
 File expected = new File(newDir, ".");
 assertEquals(expected.getAbsolutePath(), fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testChangeDirectoryInvalid() throws IOException {
 File root = new File(".");
 File newDir = new File("xxxxxxxxxxxxxxxxx");
 fsPath = FSPathFactory.newFSPath(root);
 explorer.changeDirectory(fsPath, newDir.getAbsolutePath());
 }

 @Test
 public void testHelpCommand() {
 String query = "help";
 FSPathResultList results = explorer.query(fsPath, query);
 assertNotNull(results);
 assertEquals(1, results.size());
 String expected = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '\n\nAttributes : (see javadoc for java.io.File for more info)\n\n";
 assertEquals(expected, results.get(0).getContent());
 }
}