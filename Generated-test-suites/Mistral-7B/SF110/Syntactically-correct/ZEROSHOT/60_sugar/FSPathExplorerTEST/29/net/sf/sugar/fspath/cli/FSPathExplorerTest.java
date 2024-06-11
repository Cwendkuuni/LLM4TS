package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

/**
 * Test class for FSPathExplorer.java
 *
 * @author <Your Name>
 */
public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;
 private String query;
 private File rootDirectory;

 @Before
 public void setUp() throws IOException {
 // create a temporary directory for testing
 rootDirectory = new File(System.getProperty("java.io.tmpdir"), "FSPathExplorerTest");
 rootDirectory.mkdirs();

 // initialize the FSPathExplorer with the root directory
 explorer = new FSPathExplorer();
 explorer.rootPath = rootDirectory;
 explorer.createFSPath();
 query = "//file[@name = 'testFile']";
 }

 @After
 public void tearDown() {
 // delete the temporary directory
 if (rootDirectory.exists()) {
 rootDirectory.deleteRecursively();
 }
 }

 @Test
 public void testCreateFSPath() {
 assertNotNull(explorer.fsPath);
 }

 @Test
 public void testQueryEmptyDirectory() throws IOException {
 // create an empty directory and test querying it
 File emptyDir = new File(rootDirectory, "emptyDir");
 emptyDir.mkdir();
 FSPath emptyFSPath = explorer.fsPath.getChild(emptyDir.getName());
 FSPathResultList results = emptyFSPath.query(query);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testQuerySingleFile() throws IOException {
 // create a test file and test querying it
 File testFile = new File(rootDirectory, "testFile");
 testFile.createNewFile();
 FSPath testFSPath = explorer.fsPath.getChild(testFile.getName());
 FSPathResultList results = testFSPath.query(query);
 assertEquals(1, results.size());
 assertEquals(testFile.getAbsolutePath(), results.get(0).getFile().getPath());
 }

 @Test
 public void testQueryMultipleFiles() throws IOException {
 // create multiple test files and test querying them
 File testFile1 = new File(rootDirectory, "testFile1");
 testFile1.createNewFile();
 File testFile2 = new File(rootDirectory, "testFile2");
 testFile2.createNewFile();
 FSPath testFSPath1 = explorer.fsPath.getChild(testFile1.getName());
 FSPath testFSPath2 = explorer.fsPath.getChild(testFile2.getName());
 FSPathResultList results1 = testFSPath1.query(query);
 assertEquals(1, results1.size());
 assertEquals(testFile1.getAbsolutePath(), results1.get(0).getFile().getPath());
 FSPathResultList results2 = testFSPath2.query(query);
 assertEquals(1, results2.size());
 assertEquals(testFile2.getAbsolutePath(), results2.get(0).getFile().getPath());
 }

 @Test
 public void testQueryWithSubDirectories() throws IOException {
 // create sub directories and test querying them
 File subDir1 = new File(rootDirectory, "subDir1");
 subDir1.mkdir();
 File subDir2 = new File(subDir1, "subDir2");
 subDir2.mkdir();
 File testFile = new File(subDir2, "testFile");
 testFile.createNewFile();
 FSPath subFSPath1 = explorer.fsPath.getChild(subDir1.getName());
 FSPath subFSPath2 = explorer.fsPath.getChild(subDir2.getName());
 FSPath testFSPath = explorer.fsPath.getChild(testFile.getName());
 FSPathResultList results = testFSPath.query(query);
 assertEquals(1, results.size());
 assertEquals(testFile.getAbsolutePath(), results.get(0).getFile().getPath());
 }

 @Test(expected = IOException.class)
 public void testQueryNonExistingFile() throws IOException {
 // test querying a non-existent file
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'nonExistingFile']").query(query);
 assertTrue(results.isEmpty());
 }

 @Test(expected = IOException.class)
 public void testQueryNonExistingDirectory() throws IOException {
 // test querying a non-existent directory
 FSPathResultList results = explorer.fsPath.query("//dir[@name = 'nonExistingDir']").query(query);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testHelpCommand() {
 explorer.prompt.print("help");
 }

 @Test
 public void testPwdCommand() throws IOException {
 File currentDirectory = rootDirectory;
 explorer.prompt.print("pwd");
 assertEquals(rootDirectory.getAbsolutePath(), explorer.out.readLine());
 // change the working directory and test pwd command again
 currentDirectory = new File(rootDirectory, "subDir1");
 currentDirectory.mkdir();
 explorer.fsPath = explorer.fsPath.getChild(currentDirectory.getName());
 explorer.prompt.print("pwd");
 assertEquals(currentDirectory.getAbsolutePath(), explorer.out.readLine());
 }

 @Test
 public void testCdCommand() throws IOException {
 File newDirectory = new File(rootDirectory, "newDir");
 newDirectory.mkdir();
 explorer.prompt.print("cd newDir");
 explorer.fsPath = explorer.fsPath.getChild(newDirectory.getName());
 explorer.prompt.print("pwd");
 assertEquals(newDirectory.getAbsolutePath(), explorer.out.readLine());
 }

 @Test(expected = IOException.class)
 public void testCdCommandInvalidDirectory() throws IOException {
 File invalidDirectory = new File(rootDirectory, "nonexistentDir");
 explorer.prompt.print("cd nonexistentDir");
 }

 @Test
 public void testExitCommand() {
 explorer.prompt.print("exit");
 }
}