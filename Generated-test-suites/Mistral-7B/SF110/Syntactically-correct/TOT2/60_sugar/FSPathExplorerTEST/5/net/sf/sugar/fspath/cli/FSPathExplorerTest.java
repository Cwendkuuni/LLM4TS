import static org.junit.Assert.*;
import static net.sf.sugar.fspath.cli.Commands.*;
import static net.sf.sugar.fspath.cli.FSPathExplorer.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Prompt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {

 private File testRoot;
 private FSPathExplorer explorer;
 private Prompt prompt;

 @Before
 public void setUp() throws Exception {
 testRoot = new File(System.getProperty("user.dir"));
 explorer = new FSPathExplorer();
 explorer.out = System.out;
 explorer.err = System.err;
 explorer.prompt = new Prompt(System.in, System.out, System.err);
 explorer.prompt.setPromptText("fspath>");
 }

 @After
 public void tearDown() throws Exception {
 explorer = null;
 }

 @Test
 public void testConstructors() {
 FSPathExplorer explorer = new FSPathExplorer();
 assertNotNull(explorer);
 }

 @Test
 public void testMainWithNoArgs() throws IOException {
 String[] args = new String[0];
 FSPathExplorer.main(args);
 assertNotNull(explorer.fsPath);
 }

 @Test
 public void testMainWithInvalidArgs() throws IOException {
 String[] args = {"-invalid"};
 try {
 FSPathExplorer.main(args);
 fail("Expected an error message");
 } catch (Exception e) {
 //expected
 }
 }

 @Test
 public void testMainWithValidArgs() throws IOException {
 String[] args = {"-path", "src", "-find", "*.java"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 assertEquals("/src", explorer.fsPath.getRootDirectory().getAbsolutePath());
 explorer.start();
}

 @Test
 public void testFindFilesBySimpleQuery() throws IOException {
 String[] args = {"-path", "src", "-find", "*.java"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 explorer.start();
 String query = "//file[@name = '*.java']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(13, results.size()); // assuming there are 13 java files in src
 }

 @Test
 public void testFindFilesByComplexQuery() throws IOException {
 String[] args = {"-path", "src", "-find", ""};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 explorer.start();
 String query = "//dir[@name = 'lib']//file[@name = '*test*']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(2, results.size()); // assuming there are 2 test files in lib
 }

 @Test
 public void testFindDirectoriesByQuery() throws IOException {
 String[] args = {"-path", "src", "-find", "dir[@name = 'lib']"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 explorer.start();
 String query = "//dir[@name = 'lib']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(1, results.size()); // assuming there is one lib directory in src
 }

 @Test
 public void testFindFilesWithinDirectory() throws IOException {
 String[] args = {"-path", "src", "-find", "dir[@name = 'lib']//file[@name = 'TestFile.class']"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 explorer.start();
 String query = "dir[@name = 'lib']//file[@name = 'TestFile.class']";
 FSPathResultList results = explorer.fsPath.query(query);
 assertEquals(1, results.size()); // assuming there is one TestFile.class in lib
 }

 @Test(expected = IOException.class)
 public void testFindFilesWithInvalidQuery() throws IOException {
 String[] args = {"-path", "src", "-find", "fs:invalidQuery"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String query = "//dir[@name = 'lib']//file[@name = 'TestFile.class']";
 FSPathResultList results = explorer.fsPath.query(query);
 fail("Expected an IOException");
 }

 @Test
 public void testChangeDirectory() throws IOException {
 String[] args = {"-path", "src", "-find", "*.java"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String query = "cd ../";
 explorer.start();
 String newQuery = "//file[@name = '*.java']";
 FSPathResultList results = explorer.fsPath.query(newQuery);
 assertEquals(11, results.size()); // assuming there are 11 java files in parent directory of src
 }

 @Test
 public void testInvalidDirectoryChange() throws IOException {
 String[] args = {"-path", "src", "-find", "*.java"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String query = "cd xyz";
 explorer.start();
 assertFalse(explorer.prompt.isOpen());
 }

 @Test
 public void testHelpCommand() throws IOException {
 String[] args = {"-help"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 explorer.start();
 String helpQuery = "help";
 FSPathResultList results = explorer.fsPath.query(helpQuery);
 String helpString = results.get(0).getContent();
 assertTrue(helpString.indexOf("Usage") > 0);
 assertFalse(helpString.indexOf("Invalid command") > 0);
 }

 @Test
 public void testInvalidHelpCommand() throws IOException {
 String[] args = {"-path", "src", "-find", "*.java"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String query = "invalidHelp";
 explorer.start();
 assertFalse(explorer.prompt.isOpen());
 }

 @Test
 public void testPwdCommand() throws IOException {
 String[] args = {"-path", "src", "-find", "*.java"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String query = "pwd";
 explorer.start();
 String pwd = explorer.prompt.readLine();
 assertEquals(testRoot.getAbsolutePath(), pwd);
 }

}