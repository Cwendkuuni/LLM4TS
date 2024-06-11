import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import java.io.File;
import java.io.IOException;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;
 private File rootPath;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 rootPath = new File(System.getProperty("user.dir"));
 explorer.rootPath = rootPath;
 explorer.fsPath = FSPathFactory.newFSPath(rootPath);
 }

 @Test
 public void testSimpleQuery() {
 String query = "//dir[@name = 'src']";
 FSPathResultList results = explorer.fsPath.query(query);
 File expected = new File(rootPath, "src");
 assertTrue(results.size() > 0);
 assertEquals(expected.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testComplexQuery() {
 String query = "//dir[@name = 'src']//file[@length > 10000]";
 FSPathResultList results = explorer.fsPath.query(query);
 assertTrue(results.size() > 0);
 }

 @Test(expected = IOException.class)
 public void testInvalidPath() {
 String query = "//invalid";
 explorer.fsPathQuery = query;
 try {
 explorer.fsPath.query(query);
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testEmptyQuery() {
 String query = "";
 FSPathResultList results = explorer.fsPath.query(query);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testInvalidQuery() {
 String query = "//invalid[@name = 'invalid']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertTrue(results.isEmpty());
 }
}