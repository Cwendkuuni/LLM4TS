import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.FSPathResult;
import net.sf.sugar.fspath.cli.FSPathResultList;

public class FSPathExplorerTest {
 private FSPathExplorer fsPathExplorer;
 private String rootPath;
 private String fsPathQuery;

 @Before
 public void setUp() {
 fsPathExplorer = new FSPathExplorer();
 rootPath = getClass().getResource("/testData").getFile();
 fsPathExplorer.configureExplorer(new String[]{"-p", rootPath});
 fsPathExplorer.createFSPath();
 }

 @Test
 public void testPWDCommand() {
 String expected = rootPath;
 String actual = fsPathExplorer.pwd();
 assertEquals(expected, actual);
 }

 @Test
 public void testCDCommand() {
 File newDir = new File(rootPath, "testData2");
 fsPathExplorer.cd("testData2");
 String expected = newDir.getAbsolutePath();
 String actual = fsPathExplorer.pwd();
 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testInvalidCDCommand() {
 fsPathExplorer.cd("invalidDir");
 }

 @Test
 public void testHelpCommand() {
 fsPathExplorer.help();
 }

 @Test
 public void testFindSimpleQuery() {
 String query = "*.txt";
 List<FSPathResult> results = fsPathExplorer.query(query);
 int expectedSize = 3;
 int actualSize = results.size();
 assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testFindComplexQuery() {
 String query = "//dir[@name = 'testData']//file[@size > 100]";
 List<FSPathResult> results = fsPathExplorer.query(query);
 int expectedSize = 1;
 int actualSize = results.size();
 assertEquals(expectedSize, actualSize);
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery() {
 fsPathExplorer.query("invalidQuery");
 }

 @Test
 public void testExitCommand() {
 fsPathExplorer.exit();
 }
}