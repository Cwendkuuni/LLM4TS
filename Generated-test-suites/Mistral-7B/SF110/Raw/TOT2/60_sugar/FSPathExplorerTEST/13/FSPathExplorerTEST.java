import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 fsPath = FSPathFactory.newFSPath();
 }

 @After
 public void tearDown() {
 explorer = null;
 fsPath = null;
 }

 @Test
 public void testConstructor() {
 // Test constructing an instance of FSPathExplorer with no arguments
 explorer = new FSPathExplorer();
 assertNotNull(explorer);
 // Test constructing an instance of FSPathExplorer with a root path
 File rootPath = new File("/tmp");
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 assertNotNull(explorer.getRootPath());
 assertEquals(rootPath.getAbsolutePath(), explorer.getRootPath().getAbsolutePath());
 }

 @Test
 public void testShowWelcome() {
 String welcomeMessage = "+-------------------------------------------------------+\n" +
                        "|                                                       |\n" +
                        "| FSPathExplorer                                        |\n" +
                        "|                                                       |\n" +
                        "| Scanning filesystem metadata...                        |\n" +
                        "|                                                       |\n" +
                        "| PLEASE NOTE running FSPathExplorer from the top level   |\n" +
                        "| of your filesystem is not advised                     |\n" +
                        "|                                                       |\n" +
                        "+-------------------------------------------------------+";
 String output = new StringBuffer();
 explorer.showWelcome();
 while (explorer.prompt.hasNextLine()) {
 output.append(explorer.prompt.nextLine());
 }
 assertEquals(welcomeMessage, output.toString());
 }

 @Test
 public void testFindFileWithSimpleQuery() {
 // Test finding a file with a simple query
 fsPath.setRootDirectory(new File("/tmp/test"));
 fsPath.mkdirs();
 File fileToFind = new File(fsPath.getRootDirectory(), "testfile.txt");
 try {
 fileToFind.createNewFile();
 } catch (IOException e) {
 fail("Failed to create test file.");
 }
 FSPathResultList results = explorer.query(explorer, "//file[@name = 'testfile.txt']");
 assertEquals(1, results.size());
 File actualFile = results.get(0).getFile();
 assertTrue(actualFile.exists());
 assertEquals(fileToFind.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testFindDirectoryWithSimpleQuery() {
 // Test finding a directory with a simple query
 fsPath.setRootDirectory(new File("/tmp/test"));
 fsPath.mkdirs();
 File dirToFind = new File(fsPath.getRootDirectory(), "testdir");
 try {
 dirToFind.mkdir();
 } catch (IOException e) {
 fail("Failed to create test directory.");
 }
 FSPathResultList results = explorer.query(explorer, "//dir[@name = 'testdir']");
 assertEquals(1, results.size());
 File actualDir = results.get(0).getFile();
 assertTrue(actualDir.exists());
 assertTrue(actualDir.isDirectory());
 assertEquals(dirToFind.getAbsolutePath(), actualDir.getAbsolutePath());
 }

 @Test
 public void testFindFileWithComplexQuery() {
 // Test finding a file with a complex query
 File rootDir = new File("/tmp/test");
 fsPath.setRootDirectory(rootDir);
 rootDir.mkdirs();
 File dir1 = new File(rootDir, "dir1");
 dir1.mkdir();
 File dir2 = new File(dir1, "dir2");
 dir2.mkdir();
 File fileToFind = new File(dir2, "testfile.txt");
 try {
 fileToFind.createNewFile();
 } catch (IOException e) {
 fail("Failed to create test file.");
 }
 FSPathResultList results = explorer.query(explorer, "//dir[@name = 'dir1']//dir[@name = 'dir2']//file[@name = 'testfile.txt']");
 assertEquals(1, results.size());
 File actualFile = results.get(0).getFile();
 assertTrue(actualFile.exists());
 assertEquals(fileToFind.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testFindMultipleFilesWithComplexQuery() {
 // Test finding multiple files with a complex query
 File rootDir = new File("/tmp/test");
 fsPath.setRootDirectory(rootDir);
 rootDir.mkdirs();
 File dir1 = new File(rootDir, "dir1");
 dir1.mkdir();
 File dir2 = new File(dir1, "dir2");
 dir2.mkdir();
 File file1 = new File(dir2, "testfile1.txt");
 try {
 file1.createNewFile();
 } catch (IOException e) {
 fail("Failed to create test file 1.");
 }
 File file2 = new File(dir2, "testfile2.txt");
 try {
 file2.createNewFile();
 } catch (IOException e) {
 fail("Failed to create test file 2.");
 }
 FSPathResultList results = explorer.query(explorer, "//dir[@name = 'dir1']//dir[@name = 'dir2']//file[@name = 'testfile*']");
 assertEquals(2, results.size());
 }

 @Test
 public void testInvalidQuery() {
 FSPathResultList results = explorer.query(explorer, "//invalidquery");
 assertEquals(0, results.size());
 }

 @Test
 public void testInvalidQueryWithSlash() {
 FSPathResultList results = explorer.query(explorer, "/invalidquery");
 assertEquals(0, results.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullQuery() {
 explorer.query(explorer, null);
 }

 @Test
 public void testQueryWithEmptyResult() {
 FSPathResultList results = explorer.query(explorer, "//dir[@name = 'non-existent-directory']//file[@name = 'non-existent-file']");
 assertEquals(0, results.size());
 }

 @Test(expected = IOException.class)
 public void testQueryWithIOException() {
 File invalidDir = new File("/nonexistent");
 explorer.query(explorer, "//dir[@name = '" + invalidDir.getName() + "']");
 }

 @Test
 public void testHelpCommand() {
 String expectedHelpOutput = "FSPathExplorer help :\n\n" +
                            "Enter an FSPath query to search the filesystem\n\n" +
                            "Examples :\n\n" +
                            "1) //dir[@name = 'foo'] \n             finds any directory under the current directory named 'foo'\n" +
                            "2) //dir[@name = 'src']//file[@length > 10000] \n             finds any file which is nested anywhere below a 'src' directory\n" +
                            "   with a file size greater than 10000 bytes\n" +
                            "3) //dir[fs:matches(@name, '^[.].*')]\n           finds all directories in the directory sructure which begin with a '\n'";
 String output = new StringBuffer();
 explorer.showHelp();
 while (explorer.prompt.hasNextLine()) {
 output.append(explorer.prompt.nextLine());
 }
 assertEquals(expectedHelpOutput.toString(), output.toString());
 }
}