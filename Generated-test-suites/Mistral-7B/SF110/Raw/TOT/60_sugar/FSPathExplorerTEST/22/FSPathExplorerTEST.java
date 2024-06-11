package net.sf.sugar.fspath.cli;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private File tempDir;

 @Before
 public void setUp() throws IOException {
 tempDir = File.createTempDirectory("junit-test-", null);
 explorer = new FSPathExplorer();
 explorer.rootPath = tempDir;
 explorer.fsPath = FSPathFactory.newFSPath(tempDir);
 }

 @Test
 public void testNoQuery() {
 explorer.start();
 String input = explorer.prompt.readLine();
 assertNull(input);
 }

 @Test
 public void testPwdCommand() throws IOException {
 explorer.start();
 String input = "pwd";
 explorer.prompt.writeLine(input);
 String output = explorer.prompt.readLine();
 assertEquals(tempDir.getAbsolutePath(), output);
 }

 @Test
 public void testCdCommand() throws IOException {
 File newDir = new File(tempDir, "subdir");
 newDir.mkdir();
 explorer.start();
 String input = "cd subdir";
 explorer.prompt.writeLine(input);
 String output = explorer.prompt.readLine();
 assertEquals("changed to : " + newDir.getAbsolutePath(), output);
 }

 @Test
 public void testCDInvalidDir() throws IOException {
 explorer.start();
 String input = "cd non-existent-dir";
 explorer.prompt.writeLine(input);
 String output = explorer.prompt.readLine();
 assertEquals("unable to change to non-existent-dir please make sure that the directory exists and is readable", output);
 }

 @Test
 public void testHelpCommand() throws IOException {
 explorer.start();
 String input = "help";
 explorer.prompt.writeLine(input);
 // assuming that this method reads the help text from a file or a hardcoded string, we can check the output here
 // for now we just check that something is returned
 String output = explorer.prompt.readLine();
 assertTrue(output != null && !output.isEmpty());
 }

 @Test
 public void testFindSimpleQuery() throws IOException {
 explorer.start();
 String input = "find //file[name = 'temp-file.txt']";
 explorer.prompt.writeLine(input);
 FSPathResultList results = explorer.fsPath.query(input);
 File expectedFile = new File(tempDir, "temp-file.txt");
 assertTrue(results.size() == 1);
 assertEquals(expectedFile.getPath(), results.get(0).getFile().getPath());
 }

 @Test
 public void testFindComplexQuery() throws IOException {
 File subDir = new File(tempDir, "subdir");
 newDir.mkdir();
 File subSubDir = new File(subDir, "subsubdir");
 subSubDir.mkdir();
 File fileToFind = new File(subSubDir, "test-file.txt");
 fileToFind.createNewFile();
 explorer.start();
 String input = "find //dir[@name = 'subdir']//file[name = 'test-file.txt']";
 explorer.prompt.writeLine(input);
 FSPathResultList results = explorer.fsPath.query(input);
 assertTrue(results.size() == 1);
 assertEquals(fileToFind.getPath(), results.get(0).getFile().getPath());
 }

 @Test
 public void testFindNonExistentQuery() throws IOException {
 explorer.start();
 String input = "find //dir[@name = 'non-existent-dir']//file[name = 'test-file.txt']";
 explorer.prompt.writeLine(input);
 FSPathResultList results = explorer.fsPath.query(input);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testFindEmptyDirectoryQuery() throws IOException {
 File emptyDir = new File(tempDir, "empty");
 emptyDir.mkdir();
 explorer.start();
 String input = "find //dir[@name = 'empty']";
 explorer.prompt.writeLine(input);
 FSPathResultList results = explorer.fsPath.query(input);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testFindParentDirectoryQuery() throws IOException {
 File parentDir = new File(tempDir, "parent");
 parentDir.mkdir();
 File childFile = new File(parentDir, "child-file.txt");
 childFile.createNewFile();
 explorer.start();
 String input = "find //dir[@name = '..']/file[name = 'child-file.txt']";
 explorer.prompt.writeLine(input);
 FSPathResultList results = explorer.fsPath.query(input);
 File expectedFile = new File(tempDir, "child-file.txt");
 assertTrue(results.size() == 1);
 assertEquals(expectedFile.getPath(), results.get(0).getFile().getPath());
 }

 @Test(expected = IOException.class)
 public void testFindInvalidQuery() throws IOException {
 explorer.start();
 String input = "find //file[@name = 'this-should-fail']";
 explorer.prompt.writeLine(input);
 }
}