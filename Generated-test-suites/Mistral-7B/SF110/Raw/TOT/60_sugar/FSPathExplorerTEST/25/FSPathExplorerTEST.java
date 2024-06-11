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
 FSPathResultList resultList = explorer.query(fsPath, "");
 assertEquals(0, resultList.size());
 }

 @Test
 public void testValidQuery() {
 File rootDir = new File(".");
 fsPath = FSPathFactory.newFSPath(rootDir);
 String query = "dir[@name = 'src']";

 FSPathResultList resultList = explorer.query(fsPath, query);
 File expectedFile = new File("src");
 assertTrue(fsPath.exists(expectedFile.getPath()));
 assertTrue(resultList.contains(expectedFile));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidQuery() {
 String query = "invalidQuery";
 FSPathResultList resultList = explorer.query(fsPath, query);
 fail("expected IllegalArgumentException but got " + resultList);
 }

 @Test(expected = IOException.class)
 public void testQueryIOException() throws IOException {
 String query = "//file[@length > 1000000]";
 File tempFile = File.createTempFile("test", null);
 File parentDir = tempFile.getParentFile();
 fsPath = FSPathFactory.newFSPath(parentDir);
 tempFile.deleteOnExit();

 //create large file
 try (PrintStream out = new PrintStream(tempFile)) {
 for (int i = 0; i < 1024 * 1024; i++) {
 out.println(i);
 }
 }

 FSPathResultList resultList = explorer.query(fsPath, query);
 fail("expected IOException but got " + resultList);
 }

 @Test
 public void testValidQueryWithAttributes() {
 File rootDir = new File(".");
 fsPath = FSPathFactory.newFSPath(rootDir);
 String query = "//file[@hidden = 'false']";

 FSPathResultList resultList = explorer.query(fsPath, query);
 File hiddenFile = new File(".hiddenFile");
 assertFalse(hiddenFile.canRead());
 assertFalse(resultList.contains(hiddenFile));

 File readFile = new File("readableFile.txt");
 assertTrue(readFile.canRead());
 assertTrue(resultList.contains(readFile));
 }

 @Test
 public void testInvalidPath() {
 String query = "//file[@name = '/etc/passwd']";
 FSPathResultList resultList = explorer.query(fsPath, query);
 assertEquals(0, resultList.size());
 }
}