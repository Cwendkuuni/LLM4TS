package net.sf.sugar.fspath.cli;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 }

 @Test
 public void testEmptyQuery() {
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "";
 String[] expressionWords = currentExpression.split(" ");
 String command = expressionWords[0];
 assertEquals(Commands.PWD.getCommandText(), command);
}

 @Test
 public void testPwdCommand() {
 explorer.createFSPath();
 File rootPath = new File(".");
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "pwd";
 String[] expressionWords = currentExpression.split(" ");
 String command = expressionWords[0];
 assertEquals(Commands.PWD.getCommandText(), command);
 out.flush();
 String output = out.getHudsonStatus();
 assertEquals("Current directory : ." + File.separator, output);
}

 @Test
 public void testCdCommand() {
 explorer.createFSPath();
 File rootPath = new File(".");
 File newDir = new File("src");
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "cd src";
 String[] expressionWords = currentExpression.split(" ");
 String command = expressionWords[0];
 assertEquals(Commands.CD.getCommandText(), command);
 out.flush();
 String output = out.getHudsonStatus();
 assertTrue(output.contains("changed to : src"));
}

 @Test
 public void testHelpCommand() {
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "help";
 String[] expressionWords = currentExpression.split(" ");
 String command = expressionWords[0];
 assertEquals(Commands.HELP.getCommandText(), command);
}

 @Test
 public void testFindFilesWithSimpleQuery() throws IOException {
 File testFolder = new File("src/test/resources");
 explorer.rootPath = testFolder;
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "//file[@name = 'Test.java']";
 String[] expressionWords = currentExpression.split(" ");
 String command = expressionWords[0];
 assertFalse(Commands.isCommand(command));
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 FSPathResult firstResult = results.get(0);
 File expectedFile = new File(testFolder.getAbsolutePath() + "/test/resources/Test.java");
 assertEquals(expectedFile.getAbsolutePath(), firstResult.getFile().getAbsolutePath());
}

 @Test
 public void testFindFilesWithComplexQuery() throws IOException {
 File testFolder = new File("src/test/resources");
 File subFolder = new File("subfolder");
 File expectedFile = new File(testFolder.getAbsolutePath() + "/subfolder/Test2.txt");
 File subFolderFile = new File(testFolder.getAbsolutePath() + "/subfolder");
 subFolderFile.mkdir();
 File testFile = new File(subFolderFile.getAbsolutePath() + "/Test2.txt");
 testFile.createNewFile();
 explorer.rootPath = testFolder;
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "//dir[@name = 'subfolder']//file[@name = 'Test2.txt']";
 String[] expressionWords = currentExpression.split(" ");
 String command = expressionWords[0];
 assertFalse(Commands.isCommand(command));
 FSPathResultList results = explorer.fsPath.query(currentExpression);
 FSPathResult firstResult = results.get(0);
 assertEquals(expectedFile.getAbsolutePath(), firstResult.getFile().getAbsolutePath());
 subFolderFile.delete();
}

 @Test
 public void testExitCommand() {
 explorer.createFSPath();
 explorer.start();
 String currentExpression = "exit";
 String[] expressionWords = currentExpression.split(" ");
 String command = expressionWords[0];
 assertEquals(Commands.EXIT.getCommandText(), command);
}
}