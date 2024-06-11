package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private String query = "/dir[@name = 'src']//file[@length > 10000]";

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 }

 @Test
 public void testCreateFSPath() throws IOException {
 // test creating FSPath with no root path
 FSPath fsPath = explorer.fsPath;
 assertTrue(fsPath.getRootDirectory().isDirectory());

 // test creating FSPath with root path
 File rootPath = new File("/tmp");
 explorer = new FSPathExplorer();
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 fsPath = explorer.fsPath;
 assertEquals(rootPath, fsPath.getRootDirectory());
 }

 @Test
 public void testQueryWithEmptyPath() throws IOException {
 FSPathResultList resultList = explorer.query(explorer, "", "");
 assertEquals(0, resultList.size());
 }

 @Test
 public void testQueryWithValidPath() throws IOException {
 FSPathResultList resultList = explorer.query(explorer, query, "");
 assertEquals(6, resultList.size());
 }

 @Test(expected = IOException.class)
 public void testQueryWithInvalidPath() throws IOException {
 explorer.query(explorer, "//invalid", "");
 }

 @Test(expected = IOException.class)
 public void testQueryWithInvalidPathCharacter() throws IOException {
 explorer.query(explorer, "//dir[@name = 'src']//file[@length > 10000]%", "");
 }

 @Test(expected = IOException.class)
 public void testQueryWithNoDirectory() throws IOException {
 explorer.query(explorer, "//file[@name = 'testFile']", "");
 }

 @Test
 public void testQueryWithDirectory() throws IOException {
 File testDir = new File("/tmp/testDirectory");
 testDir.mkdirs();
 FSPathResultList resultList = explorer.query(explorer, "//dir[@name = 'testDirectory']", "");
 assertEquals(1, resultList.size());
 File resultFile = new File(resultList.get(0).getFile().getPath());
 File testFile = new File(testDir, "testFile.txt");
 testFile.createNewFile();
 resultList = explorer.query(explorer, "//dir[@name = 'testDirectory']//file[@name = 'testFile.txt']", "");
 assertEquals(1, resultList.size());
 assertEquals(testFile.getCanonicalPath(), resultList.get(0).getFile().getCanonicalPath());
 testDir.delete();
 }

 @Test
 public void testQueryWithMultipleMatches() throws IOException {
 File testDir = new File("/tmp/testDirectory");
 testDir.mkdirs();
 File file1 = new File(testDir, "testFile1.txt");
 file1.createNewFile();
 File file2 = new File(testDir, "testFile2.txt");
 file2.createNewFile();
 FSPathResultList resultList = explorer.query(explorer, "//dir[@name = 'testDirectory']//file[@length > 0]", "");
 assertEquals(2, resultList.size());
 testDir.delete();
 }

 @Test
 public void testQueryWithAttributes() throws IOException {
 FSPathResultList resultList = explorer.query(explorer, "//dir[@hidden = 'true']", "");
 assertEquals(0, resultList.size());
 resultList = explorer.query(explorer, "//dir[@hidden = 'false']", "");
 assertEquals(1, resultList.size());
 resultList = explorer.query(explorer, "//file[@hidden = 'true']", "");
 assertEquals(0, resultList.size());
 resultList = explorer.query(explorer, "//file[@hidden = 'false']", "");
 assertEquals(1, resultList.size());
 resultList = explorer.query(explorer, "//file[@readable = 'false']", "");
 assertEquals(0, resultList.size());
 resultList = explorer.query(explorer, "//file[@readable = 'true']", "");
 assertEquals(1, resultList.size());
 }
}