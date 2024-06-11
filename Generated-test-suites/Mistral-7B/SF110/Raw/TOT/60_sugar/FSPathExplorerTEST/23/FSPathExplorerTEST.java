package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private String query;
 private String rootPath;

 @Before
 public void setUp() {
 this.query = "/dir[@name = 'src']//file[@length > 10000]";
 this.rootPath = new File(".").getAbsolutePath();
 this.explorer = new FSPathExplorer();
 this.explorer.rootPath = new File(this.rootPath);
 this.explorer.configureExplorer(new String[]{"-p", this.rootPath, "-f", this.query});
 this.explorer.createFSPath();
 }

 @After
 public void tearDown() throws IOException {
 this.explorer.fsPath.deleteRecursively();
 }

 @Test
 public void testFindFilesWithLengthGreaterThan10000() {
 FSPathResultList results = this.explorer.fsPath.query(this.query);

 // typical use case
 assertTrue(results.hasNext());
 FSPathResult file = results.next();
 File fsFile = file.getFile();
 assertTrue(fsFile.exists());
 assertTrue(fsFile.length() > 10000);

 // edge case: no files match the query
 results = this.explorer.fsPath.query("/dir[@name = \"notExistingDir\"]//file[@length > 10000]");
 assertNull(results.next());

 // error scenario: query is null
 this.explorer.fsPathQuery = null;
 this.explorer.start();
 }

 @Test
 public void testFindFilesInNonExistingDirectory() {
 this.explorer.rootPath = new File("/nonExistingPath");
 this.explorer.configureExplorer(new String[]{"-p", "/nonExistingPath", "-f", this.query});
 this.explorer.createFSPath();

 // error scenario: directory does not exist
 this.explorer.start();
 }

 @Test
 public void testFindFilesInCurrentDirectory() {
 this.explorer.rootPath = null;
 this.explorer.configureExplorer(new String[]{"-f", this.query});
 this.explorer.createFSPath();

 // typical use case
 FSPathResultList results = this.explorer.fsPath.query(this.query);
 assertTrue(results.hasNext());
 FSPathResult file = results.next();
 File fsFile = file.getFile();
 assertTrue(fsFile.isFile());
 assertFalse(fsFile.isDirectory());
 assertTrue(fsFile.length() > 10000);

 // edge case: no files match the query
 results = this.explorer.fsPath.query("/dir[@name = \"notExistingDir\"]//file[@length > 10000]");
 assertNull(results.next());

 // error scenario: query is invalid
 this.explorer.fsPathQuery = "invalid query";
 this.explorer.start();
 }
}