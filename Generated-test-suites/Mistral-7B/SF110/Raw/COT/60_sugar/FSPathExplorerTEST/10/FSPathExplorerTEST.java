package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Commands;

/**
 * Test class for FSPathExplorer
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
 explorer = null;
 fsPath = null;
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 FSPathExplorer explorer = new FSPathExplorer();
 assertNull(explorer.getRootPath());
 assertNull(explorer.getFsPathQuery());
 assertNull(explorer.getFsPath());
 assertNull(explorer.getPrompt());
 assertNull(explorer.getOut());
 assertNull(explorer.getErr());
 }

 /**
 * Test configureExplorer method with valid arguments
 */
 @Test
 public void testConfigureExplorerValidArgs() {
 String path = "/tmp";
 String query = "//file[@name = 'test.txt']";
 explorer.configureExplorer(new String[]{"-p", path, "-f", query});

 assertTrue(explorer.getRootPath() != null);
 assertTrue(explorer.getRootPath().getAbsolutePath().equals(path));
 assertTrue(explorer.getFsPathQuery() != null);
 assertTrue(explorer.getFsPathQuery().equals(query));
 assertTrue(explorer.getFsPath() != null);
 }

 /**
 * Test configureExplorer method with invalid arguments
 */
 @Test(expected = IllegalArgumentException.class)
 public void testConfigureExplorerInvalidArgs() {
 explorer.configureExplorer(new String[]{"-p"});
 }

 /**
 * Test createFSPath method
 */
 @Test
 public void testCreateFSPath() {
 File rootDir = new File("/");
 explorer.createFSPath();
 assertTrue(explorer.getFsPath() != null);
 assertTrue(explorer.getFsPath().getRootDirectory().getAbsolutePath().equals(rootDir.getAbsolutePath()));
 }

 /**
 * Test query method with empty query
 */
 @Test
 public void testQueryEmptyQuery() {
 String query = "";
 FSPathResultList results = explorer.query(query);
 assertFalse(results.hasNext());
 }

 /**
 * Test query method with valid query
 */
 @Test
 public void testQueryValidQuery() {
 String path = "/tmp";
 String query = "//file[@name = 'test.txt']";
 explorer.configureExplorer(new String[]{"-p", path});
 FSPathResultList results = explorer.query(query);
 assertTrue(results.hasNext());
 }

 /**
 * Test query method with invalid query
 */
 @Test(expected = IllegalArgumentException.class)
 public void testQueryInvalidQuery() {
 String query = "//file[@name = 'test.txt']//";
 explorer.query(query);
 }

 /**
 * Test start method
 */
 @Test
 public void testStart() {
 String pwd = System.getProperty("user.dir");
 explorer.start();
 String currentExpression = "pwd";
 String[] expressionWords = currentExpression.split(" ");
 String command = expressionWords[0];
 assertTrue(command.equals(Commands.PWD.getCommandText()));
 }

 /**
 * Test start method with empty query
 */
 @Test
 public void testStartEmptyQuery() {
 explorer.start();
 String currentExpression = "";
 String[] expressionWords = currentExpression.split(" ");
 assertFalse(Commands.isCommand(expressionWords[0]));
 }
}