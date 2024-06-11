package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;

 @Test
 public void testCreateFSPathExplorer() {
 explorer = new FSPathExplorer();
 assertNotNull(explorer);
 }

 @Test
 public void testQueryEmpty() {
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 String query = "";
 FSPathResultList results = explorer.fsPath.query(query);
 assertNotNull(results);
 assertEquals(0, results.size());
 }

 @Test
 public void testQuerySimplePath() throws IOException {
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 String query = "/tmp/";
 File expectedFile = new File("/tmp/");
 FSPathResultList results = explorer.fsPath.query(query);
 assertNotNull(results);
 assertEquals(1, results.size());
 FSPathResult result = results.get(0);
 assertSame(expectedFile, result.getFile());
 }

 @Test
 public void testQueryInvalidPath() throws IOException {
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 String query = "/tmp/this/is/an/invalid/path";
 FSPathResultList results = explorer.fsPath.query(query);
 assertNotNull(results);
 assertEquals(0, results.size());
 }

 @Test
 public void testQueryEmptyDirectory() throws IOException {
 explorer = new FSPathExplorer();
 File root = new File("/");
 explorer.createFSPath(root);
 String query = "/";
 FSPathResultList results = explorer.fsPath.query(query);
 assertNotNull(results);
 assertEquals(0, results.size());
 }

 @Test
 public void testQueryNonExistentDirectory() throws IOException {
 explorer = new FSPathExplorer();
 File root = new File("/");
 explorer.createFSPath(root);
 String query = "/non-existent";
 FSPathResultList results = explorer.fsPath.query(query);
 assertNotNull(results);
 assertEquals(0, results.size());
 }

 @Test
 public void testQueryFile() throws IOException {
 explorer = new FSPathExplorer();
 File root = new File("/");
 explorer.createFSPath(root);
 String query = "/etc/passwd";
 File expectedFile = new File("/etc/passwd");
 FSPathResultList results = explorer.fsPath.query(query);
 assertNotNull(results);
 assertEquals(1, results.size());
 FSPathResult result = results.get(0);
 assertSame(expectedFile, result.getFile());
 }

 @Test
 public void testQueryMultipleFiles() throws IOException {
 explorer = new FSPathExplorer();
 File root = new File("/");
 explorer.createFSPath(root);
 String query = "//file[contains(name, 'config')]";
 FSPathResultList results = explorer.fsPath.query(query);
 assertNotNull(results);
 assertEquals(2, results.size());
 File file1 = new File("/etc/passwd");
 File file2 = new File("/etc/hosts");
 for (FSPathResult result : results) {
 assertSame(file1, result.getFile()) || assertSame(file2, result.getFile());
 }
 }

 @Test
 public void testQueryNestedFiles() throws IOException {
 explorer = new FSPathExplorer();
 File root = new File("/usr/local");
 explorer.createFSPath(root);
 String query = "//dir[contains(@name, 'lib')]/file[contains(name, 'config')]";
 FSPathResultList results = explorer.fsPath.query(query);
 assertNotNull(results);
 assertEquals(3, results.size());
 File file1 = new File("/usr/local/lib/mysql/config.ini");
 File file2 = new File("/usr/local/lib/apache/config.ini");
 File file3 = new File("/usr/local/lib/php/config.ini");
 for (FSPathResult result : results) {
 assertSame(file1, result.getFile()) || assertSame(file2, result.getFile()) || assertSame(file3, result.getFile());
 }
 }

 @Test
 public void testQueryInvalidQuery() throws IOException {
 explorer = new FSPathExplorer();
 File root = new File("/");
 explorer.createFSPath(root);
 String query = "//dir[@name = ''";
 FSPathResultList results = explorer.fsPath.query(query);
 assertNotNull(results);
 assertEquals(0, results.size());
 }

 @Test
 public void testConfigureExplorer() {
 explorer = new FSPathExplorer();
 String[] args = new String[]{"-p", "/tmp/"};
 explorer.configureExplorer(args);
 assertNotNull(explorer.rootPath);
 assertEquals("/tmp", explorer.rootPath.getAbsolutePath());
 }

 @Test
 public void testConfigureExplorerWithFindOption() {
 explorer = new FSPathExplorer();
 String[] args = new String[]{"-p", "/tmp/", "-f", "file"};
 explorer.configureExplorer(args);
 assertNotNull(explorer.rootPath);
 assertEquals("/tmp", explorer.rootPath.getAbsolutePath());
 assertEquals("file", explorer.fsPathQuery);
 }
}