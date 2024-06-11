package net.sf.sugar.fspath.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for FSPathExplorer class.
 */
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
 if (explorer != null) {
 explorer = null;
 }
 if (fsPath != null) {
 try {
 fsPath.close();
 } catch (Exception e) {
 // do nothing
 }
 fsPath = null;
 }
 }

 @Test
 public void testPathQuery() throws IOException {
 // given
 String expected = "path/to/directory";
 File rootDirectory = new File(expected);
 explorer.rootPath = rootDirectory;
 explorer.createFSPath();
 String query = ".";

 // when
 FSPathResultList results = explorer.fsPath.query(query);

 // then
 assertEquals(1, results.size());
 File actual = results.get(0).getFile();
 assertEquals(expected, actual.getAbsolutePath());
 }

 @Test
 public void testFindQuery() throws IOException {
 // given
 File directory = new File("src/test/resources");
 explorer.rootPath = new File(".");
 explorer.createFSPath();
 String query = "dir[@name = 'test']/file[@name = 'FileTest.java']";

 // when
 FSPathResultList results = explorer.fsPath.query(query);

 // then
 assertEquals(1, results.size());
 File actual = results.get(0).getFile();
 File expected = new File(directory, "FileTest.java");
 assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery() throws IOException {
 // given
 explorer.rootPath = new File(".");
 explorer.createFSPath();
 String query = "//dir[@name = 'foo']/file[@name = 'bar']";

 // when
 FSPathResultList results = explorer.fsPath.query(query);

 // then
 // this test should fail with an IOException because the query is invalid
 }
}