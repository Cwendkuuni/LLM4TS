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

/**
 * FSPathExplorerTest class
 */
public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;
 private String query;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 fsPath = FSPathFactory.newFSPath(new File("src/test/resources"));
 query = "//dir[@name = 'test']";
 }

 @Test
 public void testSimpleFSPathQuery() {
 FSPathResultList results = explorer.query(fsPath, query);
 File expected = new File("src/test/resources/test");
 assertTrue(results.contains(new FSPathResult(expected)));
 }

 @Test
 public void testEmptyFSPathQuery() {
 FSPathResultList results = explorer.query(fsPath, "");
 assertFalse(results.isEmpty());
 }

 @Test(expected = IOException.class)
 public void testInvalidFSPathQuery() {
 explorer.query(fsPath, "//dir[@name = 'invalidQuery']");
 }

 @Test
 public void testCDCommand() {
 FSPathResultList results = explorer.query(fsPath, "cd ../");
 File expected = new File("src/test/resources");
 assertEquals(expected.getPath(), fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testPWDCommand() {
 File expected = new File("src/test/resources");
 assertEquals(expected.getPath(), explorer.query(fsPath, "pwd").get(0).getFile().getPath());
 }

 @Test
 public void testHelpCommand() {
 String help = explorer.query(fsPath, "help").get(0).getContent();
 assertTrue(help.contains("Examples"));
 }

 @Test
 public void testExitCommand() {
 explorer.query(fsPath, "exit");
 }
}