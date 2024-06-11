package net.sf.sugar.fspath.cli;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

/**
 * Tests for FSPathExplorer class
 *
 * @author John Doe
 */
public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private String workingDirectory;

 @Before
 public void setup() {
 workingDirectory = System.getProperty("user.dir");
 explorer = new FSPathExplorer();
 }

 @Test
 public void testCreateNewInstance() {
 // check that a new instance is created
 assertNotNull(explorer);
 }

 @Test
 public void testMainMethod() throws IOException {
 String[] args = {"-path", "/tmp"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 explorer.start();
 }

 @Test
 public void testHelpCommand() throws IOException {
 explorer.prompt.setInput("help");
 explorer.queryFSPath();
 }

 @Test
 public void testPwdCommand() throws IOException {
 explorer.prompt.setInput("pwd");
 String currentDirectory = explorer.queryFSPath();
 assertEquals(workingDirectory, currentDirectory);
 }

 @Test
 public void testCdCommand() throws IOException {
 File tempDir = File.createTempFile("test_cd", null);
 tempDir.deleteOnExit();
 explorer.prompt.setInput("cd " + tempDir.getAbsolutePath());
 explorer.queryFSPath();
 assertNotNull(tempDir.exists());
 explorer.prompt.setInput("pwd");
 String newDirectory = explorer.queryFSPath();
 assertEquals(tempDir.getAbsolutePath(), newDirectory);
 }

 @Test(expected = IOException.class)
 public void testCdToNonExistingDirectory() throws IOException {
 explorer.prompt.setInput("cd /nonexistingdirectory");
 explorer.queryFSPath();
 }

 @Test
 public void testFindFilesWithNamesContainingString() throws IOException {
 File testDir = new File(workingDirectory, "test_files");
 testDir.mkdirs();
 File file1 = new File(testDir, "test1.txt");
 File file2 = new File(testDir, "test2.txt");
 file1.createNewFile();
 file2.createNewFile();
 explorer.prompt.setInput("cd " + testDir.getAbsolutePath());
 explorer.queryFSPath();
 explorer.prompt.setInput("find . -name '*test*.txt'");
 FSPathResultList results = (FSPathResultList) explorer.queryFSPath();
 assertNotNull(results);
 assertEquals(2, results.size());
 }

 @Test
 public void testFindFilesWithExtensions() throws IOException {
 File testDir = new File(workingDirectory, "test_files");
 testDir.mkdirs();
 File file1 = new File(testDir, "test1.txt");
 File file2 = new File(testDir, "test2.jpg");
 file1.createNewFile();
 file2.createNewFile();
 explorer.prompt.setInput("cd " + testDir.getAbsolutePath());
 explorer.queryFSPath();
 explorer.prompt.setInput("find . -name '*.txt'");
 FSPathResultList results = (FSPathResultList) explorer.queryFSPath();
 assertNotNull(results);
 assertEquals(1, results.size());
 explorer.prompt.setInput("find . -name '*.jpg'");
 results = (FSPathResultList) explorer.queryFSPath();
 assertNotNull(results);
 assertEquals(1, results.size());
 }

 @Test
 public void testFindFilesWithXPathLikeExpressions() throws IOException {
 File testDir = new File(workingDirectory, "test_files");
 testDir.mkdirs();
 File dir1 = new File(testDir, "folder1");
 dir1.mkdir();
 File file1 = new File(testDir, "test1.txt");
 File file2 = new File(testDir, "test2.txt");
 File file3 = new File(testDir, "test3.txt");
 File fileInFolder1 = new File(dir1, "testFolderInFolder1.txt");
 file1.createNewFile();
 file2.createNewFile();
 file3.createNewFile();
 fileInFolder1.createNewFile();
 explorer.prompt.setInput("cd " + testDir.getAbsolutePath());
 explorer.queryFSPath();
 explorer.prompt.setInput("find //dir[@name = 'folder1']//file[contains(name, 'testFolderInFolder1.txt')]");
 FSPathResultList results = (FSPathResultList) explorer.queryFSPath();
 assertNotNull(results);
 assertEquals(1, results.size());
 }

 @Test
 public void testFindDirectoriesWithXPathLikeExpressions() throws IOException {
 File testDir = new File(workingDirectory, "test_files");
 testDir.mkdirs();
 File dir1 = new File(testDir, "folder1");
 File dir2 = new File(testDir, "folder2");
 dir1.mkdir();
 dir2.mkdir();
 File fileInDir1 = new File(dir1, "testFileInDir1.txt");
 File fileInDir2 = new File(dir2, "testFileInDir2.txt");
 fileInDir1.createNewFile();
 fileInDir2.createNewFile();
 explorer.prompt.setInput("cd " + testDir.getAbsolutePath());
 explorer.queryFSPath();
 explorer.prompt.setInput("find //dir[@name = 'folder1']");
 FSPathResultList results = (FSPathResultList) explorer.queryFSPath();
 assertNotNull(results);
 assertEquals(1, results.size());
 }

 @Test
 public void testFindFilesWithAttributes() throws IOException {
 File testDir = new File(workingDirectory, "test_files");
 testDir.mkdirs();
 File file1 = new File(testDir, "test1.txt");
 file1.setReadOnly();
 File file2 = new File(testDir, "test2.txt");
 file2.setHidden();
 explorer.prompt.setInput("cd " + testDir.getAbsolutePath());
 explorer.queryFSPath();
 explorer.prompt.setInput("find //file[@hidden = 'true']");
 FSPathResultList results = (FSPathResultList) explorer.queryFSPath();
 assertEquals(1, results.size());
 File testHiddenFile = results.get(0).getFile();
 assertNotNull(testHiddenFile.isHidden());
 explorer.prompt.setInput("find //file[@readonly = 'true']");
 results = (FSPathResultList) explorer.queryFSPath();
 assertEquals(1, results.size());
 File testReadOnlyFile = results.get(0).getFile();
 assertNotNull(testReadOnlyFile.canRead() && testReadOnlyFile.isFile() && testReadOnlyFile.isReadOnly());
 }

 @Test
 public void testFindDirectoriesWithAttributes() throws IOException {
 File testDir = new File(workingDirectory, "test_files");
 testDir.mkdirs();
 File dir1 = new File(testDir, "folder1");
 File dir2 = new File(testDir, "folder2");
 File hiddenDir = new File(testDir, "hiddenDir");
 hiddenDir.mkdir();
 File hiddenDirInHiddenDir = new File(hiddenDir, "hiddenDirInHiddenDir");
 hiddenDirInHiddenDir.mkdir();
 explorer.prompt.setInput("cd " + testDir.getAbsolutePath());
 explorer.queryFSPath();
 explorer.prompt.setInput("find //dir[@hidden = 'true']");
 FSPathResultList results = (FSPathResultList) explorer.queryFSPath();
 assertEquals(1, results.size());
 File testHiddenDir = results.get(0).getFile();
 assertNotNull(testHiddenDir.isDirectory() && testHiddenDir.isHidden());
 explorer.prompt.setInput("find //dir[@readonly = 'true']");
 results = (FSPathResultList) explorer.queryFSPath();
 assertEquals(0, results.size());
 }

 @Test
 public void testQueryingForNonExistingPaths() throws IOException {
 explorer.prompt.setInput("find /nonexistingpath");
 FSPathResultList results = (FSPathResultList) explorer.queryFSPath();
 assertNotNull(results);
 assertEquals(0, results.size());
 }

 @Test
 public void testQueryingForEmptyPath() throws IOException {
 File testDir = new File(workingDirectory, "test_files");
 explorer.prompt.setInput("cd " + testDir.getAbsolutePath());
 explorer.queryFSPath();
 explorer.prompt.setInput("find . ");
 FSPathResultList results = (FSPathResultList) explorer.queryFSPath();
 assertEquals(0, results.size());
 }

 @Test
 public void testQueryingForRootPath() throws IOException {
 explorer.prompt.setInput("find /");
 FSPathResultList results = (FSPathResultList) explorer.queryFSPath();
 assertNotNull(results);
 assertEquals(0, results.size());
 }

 @Test
 public void testQuittingApplication() throws IOException {
 explorer.prompt.setInput("exit");
 explorer.queryFSPath();
 }
}