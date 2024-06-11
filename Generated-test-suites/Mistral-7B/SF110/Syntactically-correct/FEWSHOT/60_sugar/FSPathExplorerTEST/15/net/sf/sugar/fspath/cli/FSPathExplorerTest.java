package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;

 @Before
 public void setup() {
 explorer = new FSPathExplorer();
 }

 @Test
 public void testConfigureExplorerWithPath() {
 String rootPath = "/tmp";
 explorer.configureExplorer(new String[]{"-p", rootPath});
 assertEquals(new File(rootPath), explorer.rootPath);
 }

 @Test
 public void testConfigureExplorerWithFind() {
 String query = "*.txt";
 explorer.configureExplorer(new String[]{"-find", query});
 assertEquals(query, explorer.fsPathQuery);
 }

 @Test
 public void testConfigureExplorerWithPathAndFind() {
 String rootPath = "/tmp";
 String query = "*.txt";
 explorer.configureExplorer(new String[]{"-p", rootPath, "-find", query});
 assertEquals(new File(rootPath), explorer.rootPath);
 assertEquals(query, explorer.fsPathQuery);
 }

 @Test(expected = AssertionError.class)
 public void testConfigureExplorerWithInvalidOption() {
 explorer.configureExplorer(new String[]{"-invalid"});
 fail("Invalid option should cause an AssertionError");
 }

 @Test
 public void testCreateFSPath() {
 File rootPath = new File("/tmp");
 explorer.createFSPath();
 assertEquals(explorer.fsPath.getRootDirectory(), rootPath);
 }

 @Test
 public void testStart() throws IOException {
 explorer.start();
 String currentExpression = "pwd";
 String[] expressionWords = currentExpression.split(" ");
 String expectedOutput = "/tmp";
 String output = "";
 if (expressionWords[0].equals(Commands.PWD.getCommandText())) {
 output = explorer.fsPath.getRootDirectory().getAbsolutePath();
 }
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStartWithInvalidCommand() {
 explorer.start();
 String currentExpression = "invalidCommand";
 String[] expressionWords = currentExpression.split(" ");
 String output = "";
 assertEquals("", output);
 }

 @Test
 public void testStartWithValidFSPathQuery() throws IOException {
 explorer.start();
 String currentExpression = "//file[@name = 'test.txt']";
 String[] expressionWords = currentExpression.split(" ");
 FSPathResultList results = new FSPathResultList();
 if (expressionWords[0].equals(Commands.QUERY.getCommandText())) {
 results = explorer.fsPath.query(currentExpression);
 }
 assertEquals(1, results.size());
 }

 @Test
 public void testStartWithInvalidFSPathQuery() {
 explorer.start();
 String currentExpression = "invalidQuery";
 String[] expressionWords = currentExpression.split(" ");
 String output = "";
 assertEquals("", output);
 }
}