import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.FSPathResult;
import net.sf.sugar.fspath.cli.FSPathResultList;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private File rootPath;

 @Before
 public void setUp() {
 rootPath = new File(".");
 explorer = new FSPathExplorer();
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 }

 @Test
 public void testEmptyQuery() {
 List<FSPathResult> results = explorer.fsPath.query("");
 assertTrue(results.isEmpty());
 }

 @Test
 public void testquerySimpleDirectory() {
 File directoryToTest = new File("src");
 explorer.rootPath = directoryToTest;
 explorer.createFSPath();
 String query = "dir";
 List<FSPathResult> results = explorer.fsPath.query(query);
 List<File> expectedFiles = new File(directoryToTest, "*").listFiles();
 for (FSPathResult result : results) {
 assertEquals(new File(directoryToTest, result.getName()).getAbsolutePath(), result.getFile().getAbsolutePath());
 }
 assertEquals(expectedFiles.size(), results.size());
 }

 @Test
 public void testquerySimpleFile() {
 File fileToTest = new File("src/test/java/FSPathExplorerTest.java");
 explorer.rootPath = new File("src/test");
 explorer.createFSPath();
 String query = "file[name = 'FSPathExplorerTest.java']";
 List<FSPathResult> results = explorer.fsPath.query(query);
 List<File> expectedFiles = new File(explorer.fsPath.getRootDirectory(), "*").listFiles();
 File testFile = null;
 for (File file : expectedFiles) {
 if (file.getName().equals("FSPathExplorerTest.java")) {
 testFile = file;
 break;
 }
 }
 assertEquals(testFile.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testqueryComplexExpression() {
 explorer.rootPath = new File("/");
 explorer.createFSPath();
 String query = "//dir[@name = 'usr']//file[contains(@name, '.conf')]";
 List<FSPathResult> results = explorer.fsPath.query(query);
 File expectedFile = new File("/usr/local/etc/hosts");
 assertEquals(expectedFile.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery() {
 explorer.rootPath = new File("/");
 explorer.createFSPath();
 String query = "//dir[@name = 'usr']/file[length = 1000000000]";
 explorer.fsPath.query(query);
 }

 @Test(expected = IOException.class)
 public void testQueryNotFound() {
 explorer.rootPath = new File("/");
 explorer.createFSPath();
 String query = "//dir[@name = 'nonExistentDirectory']//file[name = 'nonExistentFile.txt']";
 explorer.fsPath.query(query);
 }

 @Test
 public void testCDCommand() {
 explorer.rootPath = new File("/");
 explorer.createFSPath();
 String query = "cd src";
 explorer.prompt = new Prompt(System.in, explorer.out, explorer.err);
 explorer.executeCommand(query);
 File expectedDirectory = new File("/src");
 assertEquals(expectedDirectory.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testCDInvalidDirectory() {
 explorer.rootPath = new File("/");
 explorer.createFSPath();
 String query = "cd nonExistentDirectory";
 explorer.prompt = new Prompt(System.in, explorer.out, explorer.err);
 explorer.executeCommand(query);
 }
}