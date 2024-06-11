package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private File tempDirectory;

 @Before
 public void setUp() {
 try {
 tempDirectory = File.createTempDirectory("FSPathExplorerTest", null);
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 explorer = new FSPathExplorer();
 explorer.rootPath = tempDirectory;
 }

 @After
 public void tearDown() {
 if (tempDirectory != null && tempDirectory.exists()) {
 tempDirectory.deleteRecursively();
 }
 }

 @Test
 public void testCreateFSPath() {
 assertEquals(tempDirectory, explorer.fsPath.getRootDirectory());
 }

 @Test
 public void testCreateFSPath_rootPathIsNull() {
 explorer.rootPath = null;
 assertEquals(FileSystem.getDefault(), explorer.fsPath.getRootDirectory());
 }

 @Test
 public void testQueryEmptyFSPath() {
 String query = "";
 List<FSPathResult> results = explorer.query(query);
 assertFalse(results.isEmpty());
 }

 @Test
 public void testQuerySimpleFSPath() {
 String query = "//file[@name = 'test.txt']";
 List<FSPathResult> results = explorer.query(query);
 assertFalse(results.isEmpty());
 File testFile = new File(tempDirectory, "test.txt");
 for (FSPathResult result : results) {
 assertEquals(testFile.getAbsolutePath(), result.getFile().getPath());
 }
 }

 @Test
 public void testQueryComplexFSPath() {
 String query = "//dir[@name = 'test']/file[@name = 'test2.txt']";
 File testDirectory = new File(tempDirectory, "test");
 testDirectory.mkdirs();
 File testFile = new File(testDirectory, "test2.txt");
 try {
 testFile.createNewFile();
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 List<FSPathResult> results = explorer.query(query);
 assertFalse(results.isEmpty());
 assertEquals(testFile.getAbsolutePath(), results.get(0).getFile().getPath());
 }

 @Test
 public void testQueryNonExistentFSPath() {
 String query = "//nonExistentFile.txt";
 List<FSPathResult> results = explorer.query(query);
 assertTrue(results.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testQueryNullFSPath() {
 explorer.query(null);
 }

 @Test
 public void testQueryEmptyDirectory() {
 explorer.rootPath = new File(".");
 String query = "//";
 List<FSPathResult> results = explorer.query(query);
 assertTrue(results.isEmpty());
 }

}