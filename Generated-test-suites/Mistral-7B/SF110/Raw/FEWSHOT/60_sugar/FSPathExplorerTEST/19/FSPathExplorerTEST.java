package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 }

 @Test
 public void testCreateFSPathWithNullRootPath() {
 FSPath fsPath = explorer.fsPath;
 assertNull(fsPath);
 }

 @Test
 public void testCreateFSPathWithValidRootPath() throws IOException {
 File rootPath = new File("/tmp");
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 assertNotNull(explorer.fsPath);
 }

 @Test(expected = IOException.class)
 public void testCreateFSPathWithInvalidRootPath() throws IOException {
 File rootPath = new File("invalid_path");
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 }

 @Test
 public void testConfigureExplorerWithValidArguments() {
 String[] args = {"-p", "/tmp", "-f", "//dir[@name = 'test']"};
 explorer.configureExplorer(args);
 assertEquals(args[1], explorer.rootPath.getAbsolutePath());
 assertEquals(args[3], explorer.fsPathQuery);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testConfigureExplorerWithInvalidArguments() {
 String[] args = {"-p", "/tmp", "-f"};
 explorer.configureExplorer(args);
 }

 @Test
 public void testStartWithEmptyQuery() throws IOException {
 String currentExpression = "";
 explorer.start();
 // empty query should not throw an exception
 }

 @Test
 public void testStartWithValidCommand() throws IOException {
 String currentExpression = "cd /tmp";
 explorer.start();
 // valid command should not throw an exception
 }

 @Test
 public void testStartWithValidFSPathQuery() throws IOException {
 String currentExpression = "//dir[@name = 'test']";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 assertNotNull(results);
 assertFalse(results.isEmpty());
 }

 @Test
 public void testStartWithInvalidFSPathQuery() throws IOException {
 String currentExpression = "//dir[@name = 'test']/";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 assertNull(results);
 }

 @Test
 public void testStartWithInvalidFSPathQuery2() throws IOException {
 String currentExpression = "//dir[@name = 'test']//";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 assertNull(results);
 }

 @Test
 public void testStartWithInvalidFSPathQuery3() throws IOException {
 String currentExpression = "//dir[@name = 'test']//file[@name = 'test']//";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 assertNull(results);
 }

 @Test
 public void testStartWithInvalidFSPathQuery4() throws IOException {
 String currentExpression = "//dir[@name = 'test']//file[@name = 'test']";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 assertNotNull(results);
 assertFalse(results.isEmpty());
 }

 @Test
 public void testStartWithInvalidFSPathQuery5() throws IOException {
 String currentExpression = "//dir[@name = 'test']//file[name = 'test']";
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 assertNotNull(results);
 assertFalse(results.isEmpty());
 }

}