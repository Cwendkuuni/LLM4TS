package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.FSPathFactory;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

/**
 * Test class for FSPathExplorer
 */
public class FSPathExplorerTest {

 private FSPathExplorer fsPathExplorer;
 private FSPath fsPath;

 @Before
 public void setUp() {
 fsPathExplorer = new FSPathExplorer();
 fsPath = FSPathFactory.newFSPath();
 }

 @After
 public void tearDown() {
 fsPathExplorer = null;
 fsPath = null;
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#FSPathExplorer()}.
 */
 @Test
 public void testFSPathExplorerConstructor() {
 // checks if default constructor works fine
 fsPathExplorer = new FSPathExplorer();
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#configureExplorer(java.lang.String[])}.
 */
 @Test
 public void testConfigureExplorer() {
 // test with invalid arguments
 String[] args = new String[2];
 args[0] = "-p";
 args[1] = "";
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.configureExplorer(args);
 assertNull(fsPathExplorer.getRootPath());

 // test with valid arguments
 args = new String[3];
 args[0] = "-p";
 args[1] = "/home/user";
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.configureExplorer(args);
 assertEquals("/home/user", fsPathExplorer.getRootPath().getAbsolutePath());

 // test with find option
 args = new String[4];
 args[0] = "-f";
 args[1] = "testfile";
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.configureExplorer(args);
 assertEquals("", fsPathExplorer.getFsPathQuery());
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#createFSPath()}.
 */
 @Test
 public void testCreateFSPath() {
 // test without rootPath
 fsPathExplorer.createFSPath();
 assertNotNull(fsPathExplorer.getFsPath());

 // test with rootPath
 File rootPath = new File("/home/user");
 fsPathExplorer.setRootPath(rootPath);
 fsPathExplorer.createFSPath();
 assertEquals(rootPath, fsPathExplorer.getFsPath().getRootDirectory());
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 */
 @Test
 public void testStart() throws IOException {
 String query = "//dir[@name = 'test']";
 fsPathExplorer.setFsPathQuery(query);
 fsPathExplorer.start();
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testStartWithInvalidQuery() throws IOException {
 String query = "";
 fsPathExplorer.setFsPathQuery(query);
 fsPathExplorer.start();
 fail("An IOException should be thrown.");
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test
 public void testStartWithValidQuery() throws IOException {
 String query = "//dir[@name = 'test']";
 fsPathExplorer.setFsPathQuery(query);
 FSPathResultList results = fsPathExplorer.getFsPath().query(query);
 assertEquals(1, results.size());
 File expectedFile = new File("/test");
 assertEquals(expectedFile.getCanonicalPath(), results.get(0).getFile().getCanonicalPath());
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test
 public void testStartWithMultipleResults() throws IOException {
 String query = "//dir[@name = 'test']//file[@name = 'testfile']";
 fsPathExplorer.setFsPathQuery(query);
 FSPathResultList results = fsPathExplorer.getFsPath().query(query);
 assertEquals(2, results.size());
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test
 public void testStartWithInvalidQueryFormat() throws IOException {
 String query = "//dir[@name = test]";
 fsPathExplorer.setFsPathQuery(query);
 fsPathExplorer.start();
 fail("An IOException should be thrown.");
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test
 public void testStartWithEmptyQuery() throws IOException {
 String query = "";
 fsPathExplorer.setFsPathQuery(query);
 fsPathExplorer.start();
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testStartWithEmptyQueryFormat() throws IOException {
 String query = "//";
 fsPathExplorer.setFsPathQuery(query);
 fsPathExplorer.start();
 fail("An IOException should be thrown.");
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test
 public void testStartWithDirectoryQuery() throws IOException {
 String query = "//dir[@name = 'test']";
 fsPathExplorer.setFsPathQuery(query);
 fsPathExplorer.start();
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testStartWithFileQuery() throws IOException {
 String query = "//file[@name = 'test']";
 fsPathExplorer.setFsPathQuery(query);
 fsPathExplorer.start();
 fail("An IOException should be thrown.");
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test
 public void testStartWithInvalidAttributeQuery() throws IOException {
 String query = "//@test";
 fsPathExplorer.setFsPathQuery(query);
 fsPathExplorer.start();
 fail("An IOException should be thrown.");
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test
 public void testStartWithAttributeQuery() throws IOException {
 String query = "//file[@size > 100]";
 fsPathExplorer.setFsPathQuery(query);
 FSPathResultList results = fsPathExplorer.getFsPath().query(query);
 assertEquals(1, results.size());
 File expectedFile = new File("/path/to/file");
 assertEquals(expectedFile.getCanonicalPath(), results.get(0).getFile().getCanonicalPath());
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 *
 * @throws IOException
 */
 @Test
 public void testStartWithComplexQuery() throws IOException {
 String query = "//dir[@name = 'test']//file[@size > 100 and contains(@name, 'test')]";
 fsPathExplorer.setFsPathQuery(query);
 FSPathResultList results = fsPathExplorer.getFsPath().query(query);
 assertEquals(1, results.size());
 File expectedFile = new File("/path/to/testfile");
 assertEquals(expectedFile.getCanonicalPath(), results.get(0).getFile().getCanonicalPath());
 }

 /**
 * Test method for {@link net.sf.sugar.fspath.cli.FSPathExplorer#start()}.
 */
 @Test
 public void testStartWithExitCommand() {
 String query = Commands.EXIT.getCommandText();
 fsPathExplorer.setFsPathQuery(query);
 fsPathExplorer.start();
 }
}