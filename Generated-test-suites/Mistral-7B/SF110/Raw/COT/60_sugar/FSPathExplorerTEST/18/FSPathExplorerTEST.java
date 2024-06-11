package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

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

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private File tempFolder;

 @Before
 public void setUp() throws Exception {
 tempFolder = File.createTempDirectory("junit", "");
 explorer = new FSPathExplorer();
 explorer.rootPath = tempFolder;
 }

 @After
 public void tearDown() throws Exception {
 tempFolder.delete();
 }

 @Test
 public void testConstructor() {
 assertNotNull(explorer);
 }

 @Test
 public void testSetRootPath() {
 File newRootPath = new File("/tmp");
 explorer.rootPath = newRootPath;
 assertEquals(newRootPath, explorer.rootPath);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNullRootPath() {
 explorer.rootPath = null;
 }

 @Test
 public void testSetFsPathQuery() {
 explorer.fsPathQuery = "//dir[@name = 'test']";
 assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
 }

 @Test
 public void testCreateFSPath() throws IOException {
 FSPath fsPath = explorer.createFSPath();
 assertNotNull(fsPath);
 }

 @Test
 public void testStart() throws IOException, InterruptedException {
 explorer.start();
 }

 @Test
 public void testQueryEmptyPath() throws IOException {
 FSPathResultList results = explorer.fsPath.query("");
 assertEquals(0, results.size());
 }

 @Test
 public void testQuerySimplePath() throws IOException {
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'test.txt']");
 assertEquals(1, results.size());
 File expectedFile = new File(tempFolder, "test.txt");
 FSPathResult result = results.get(0);
 assertEquals(expectedFile.getAbsolutePath(), result.getFile().getAbsolutePath());
 }

 @Test
 public void testQueryComplexPath() throws IOException {
 FSPathResultList results = explorer.fsPath.query("//dir[@name = 'test']///file[@name = 'test2.txt']");
 assertEquals(1, results.size());
 File expectedFile = new File(tempFolder, "test/test2.txt");
 FSPathResult result = results.get(0);
 assertEquals(expectedFile.getAbsolutePath(), result.getFile().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testQueryInvalidPath() throws IOException {
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'test']//dir[@name = 'test2']");
 }

 @Test
 public void testQueryPathWithSpecialChars() throws IOException {
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'test%20file.txt']");
 assertEquals(1, results.size());
 File expectedFile = new File(tempFolder, "test file.txt");
 FSPathResult result = results.get(0);
 assertEquals(expectedFile.getAbsolutePath(), result.getFile().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testQueryPathWithInvalidChar() throws IOException {
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'test%illegalchar.txt']");
 }

 @Test
 public void testQueryPathWithAttributes() throws IOException {
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'test.txt'][@size > 10]");
 File expectedFile = new File(tempFolder, "test.txt");
 if (expectedFile.exists() && expectedFile.length() > 10) {
 FSPathResult result = results.get(0);
 assertEquals(expectedFile.getAbsolutePath(), result.getFile().getAbsolutePath());
 } else {
 fail("The file test.txt does not exist or its size is less than 10 bytes");
 }
 }

 @Test
 public void testQueryPathWithInvalidAttribute() throws IOException {
 FSPathResultList results = explorer.fsPath.query("//file[@name = 'test.txt'][@color > 10]");
 assertEquals(0, results.size());
 }

}