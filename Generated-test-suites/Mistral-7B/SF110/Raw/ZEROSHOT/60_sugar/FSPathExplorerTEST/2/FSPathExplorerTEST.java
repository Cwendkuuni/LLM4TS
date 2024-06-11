package net.sf.sugar.fspath.cli;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private File testDirectory;

 @Before
 public void setUp() throws Exception {
 testDirectory = new File("testFSPathExplorer");
 testDirectory.mkdir();
 testDirectory.deleteOnExit();
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-path", testDirectory.getAbsolutePath()});
 explorer.createFSPath();
 }

 @Test
 public void testPwd() {
 assertEquals(testDirectory.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testCd() {
 File testSubdirectory = new File(testDirectory, "testSubdirectory");
 testSubdirectory.mkdir();
 testSubdirectory.deleteOnExit();
 explorer.prompt.print("cd testSubdirectory\n");
 explorer.start();
 assertEquals(testSubdirectory.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testHelp() {
 explorer.prompt.print("help\n");
 explorer.start();
 //TODO: check output of help command
 }

 @Test
 public void testQuery() throws IOException {
 File testFile = new File(testDirectory, "testFile.txt");
 testFile.createNewFile();
 testFile.deleteOnExit();
 explorer.prompt.print("//file[@name = 'testFile.txt']\n");
 explorer.start();
 String result = explorer.prompt.readLine();
 assertEquals(testFile.getAbsolutePath(), result);
 }

 @Test
 public void testQueryMultiple() throws IOException {
 File testFile1 = new File(testDirectory, "testFile1.txt");
 testFile1.createNewFile();
 testFile1.deleteOnExit();
 File testFile2 = new File(testDirectory, "testFile2.txt");
 testFile2.createNewFile();
 testFile2.deleteOnExit();
 explorer.prompt.print("//file[@name = 'testFile.*']\n");
 explorer.start();
 String result1 = explorer.prompt.readLine();
 String result2 = explorer.prompt.readLine();
 assertEquals(testFile1.getAbsolutePath(), result1);
 assertEquals(testFile2.getAbsolutePath(), result2);
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery() throws IOException {
 explorer.prompt.print("//file[@name = 'testFile.txt']//file[@name = 'testFile2.txt']\n");
 explorer.start();
 fail("should throw an IOException for invalid query");
 }

 @Test(expected = IOException.class)
 public void testNoMatchesQuery() throws IOException {
 explorer.prompt.print("//file[@name = 'testFile3.txt']\n");
 explorer.start();
 assertTrue(explorer.prompt.promptContains("No matches found"));
 }

 @Test
 public void testQueryAttributes() throws IOException {
 FSPathResult result = explorer.fsPath.query("//file[@size > 10]").get(0);
 assertTrue(result.getFile().exists());
 assertEquals(result.getAttributes().size(), 11);
 for (FSPathAttributes attr : FSPathAttributes.values()) {
 if (!(attr.name().equals("dir") || attr.name().equals("file"))) {
 assertEquals(Boolean.TRUE, result.getAttributes().containsKey(attr));
 }
 }
 }
}