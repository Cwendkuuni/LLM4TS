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
 private File tempDirectory;
 private FSPath fsPath;

 @Before
 public void setUp() throws Exception {
 tempDirectory = File.createTempDirectory("junit", "test");
 explorer = new FSPathExplorer();
 explorer.rootPath = tempDirectory;
 fsPath = FSPathFactory.newFSPath(tempDirectory);
 }

 @Test
 public void testEmptyQuery() {
 FSPathResultList results = explorer.fsPath.query("");
 assertEquals(0, results.size());
 }

 @Test
 public void testSingleDirectoryQuery() {
 File testDirectory = new File(tempDirectory, "testDirectory");
 testDirectory.mkdir();
 FSPathResultList results = explorer.fsPath.query("//dir[@name = 'testDirectory']");
 assertEquals(1, results.size());
 File matchedFile = results.get(0).getFile();
 assertTrue(matchedFile.isDirectory());
 assertTrue(new File(matchedFile.getPath()).equals(testDirectory.getAbsoluteFile()));
 }

 @Test
 public void testFileQuery() {
 File testFile = File.createTempFile("testFile", null);
 testFile.createNewFile();
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'testFile']");
 assertEquals(1, results.size());
 File matchedFile = results.get(0).getFile();
 assertTrue(matchedFile.exists());
 assertTrue(new File(matchedFile.getPath()).equals(testFile.getAbsoluteFile()));
 }

 @Test
 public void testMultipleMatchingFiles() {
 File testFile1 = File.createTempFile("testFile1", null);
 testFile1.createNewFile();
 File testFile2 = File.createTempFile("testFile2", null);
 testFile2.createNewFile();
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'testFile']");
 assertEquals(2, results.size());
 File matchedFile1 = results.get(0).getFile();
 File matchedFile2 = results.get(1).getFile();
 assertTrue(matchedFile1.exists());
 assertTrue(new File(matchedFile1.getPath()).equals(testFile1.getAbsoluteFile()));
 assertTrue(matchedFile2.exists());
 assertTrue(new File(matchedFile2.getPath()).equals(testFile2.getAbsoluteFile()));
 }

 @Test
 public void testNoMatchingFiles() {
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'nonExistentFile']");
 assertEquals(0, results.size());
 }

 @Test
 public void testComplexQuery() {
 File testDirectory = new File(tempDirectory, "testComplex");
 testDirectory.mkdir();
 File testFile1 = new File(testDirectory, "testFile1.txt");
 testFile1.createNewFile();
 File testFile2 = new File(testDirectory, "testFile2.txt");
 testFile2.createNewFile();
 FSPathResultList results = explorer.fsPath.query("//dir[@name = 'testComplex']//file[@name = 'testFile' and @length > 0]");
 assertEquals(2, results.size());
 File matchedFile1 = results.get(0).getFile();
 File matchedFile2 = results.get(1).getFile();
 assertTrue(matchedFile1.exists());
 assertTrue(new File(matchedFile1.getPath()).equals(testFile1.getAbsoluteFile()));
 assertTrue(matchedFile2.exists());
 assertTrue(new File(matchedFile2.getPath()).equals(testFile2.getAbsoluteFile()));
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery() {
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'testFile']//@length > 0");
 fail();
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery2() {
 FSPathResultList results = explorer.fsPath.query("");
 fail();
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery3() {
 FSPathResultList results = explorer.fsPath.query("//@length > 10");
 fail();
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery4() {
 FSPathResultList results = explorer.fsPath.query("//file[@name = testFile]");
 fail();
 }

 @Test
 public void testCDCommand() throws IOException {
 explorer.fsPath.cd("../");
 assertFalse(new File(explorer.rootPath.getPath()).equals(explorer.fsPath.getRootDirectory().getAbsolutePath()));
 explorer.fsPath.cd(".");
 assertTrue(new File(explorer.rootPath.getPath()).equals(explorer.fsPath.getRootDirectory().getAbsolutePath()));
 }

 @Test
 public void testPWDCommand() {
 String currentPath = explorer.prompt.readLineAndConsume("pwd");
 assertEquals(explorer.rootPath.getAbsolutePath(), currentPath);
 }

 @Test
 public void testHelpCommand() {
 explorer.prompt.readLineAndConsume("help");
 // Additional asserts can be added here to verify the help text
 }

 @Test
 public void testQuitCommand() {
 explorer.prompt.readLineAndConsume("quit");
 assertTrue(explorer.prompt.isClosed());
 }

 @Test
 public void testFindCommand() {
 explorer.prompt.readLineAndConsume("find");
 assertEquals(1, explorer.fsPathQuery.length());
 explorer.prompt.readLineAndConsume("testDirectory/testFile");
 FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
 assertEquals(1, results.size());
 File matchedFile = results.get(0).getFile();
 assertTrue(new File(matchedFile.getPath()).isFile());
 assertTrue(new File(matchedFile.getPath()).getParentFile().equals(new File(testDirectory.getAbsolutePath())));
 }

 @After
 public void tearDown() throws Exception {
 tempDirectory.delete();
 }
}