package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Commands;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 this.explorer = new FSPathExplorer();
 }

 @Test
 public void testConstructor() {
 FSPathExplorer explorer = new FSPathExplorer();
 assertNotNull(explorer);
 }

 @Test
 public void testMain() throws IOException {
 String[] args = {};
 FSPathExplorer.main(args);
 // verify that the welcome message is displayed
 String welcomeMessage = "Scanning filesystem metadata...";
 String line = System.out.readLine();
 assertEquals(welcomeMessage, line);
 }

 @Test
 public void testShowWelcome() {
 String expectedWelcome = "+-------------------------------------------------------+\n"
 + "|                                                       |\n"
 + "| FSPathExplorer                                        |\n"
 + "|                                                       |\n"
 + "+-------------------------------------------------------+\n"
 + "Type 'help' for commands...\n"
 + "Scanning filesystem metadata...\n"
 + "+-------------------------------------------------------+\n"
 + "| PLEASE NOTE running FSPathExplorer from the top level |\n"
 + "| of your filesystem is not advised                     |\n"
 + "+-------------------------------------------------------+";
 String actualWelcome = FSPathExplorer.showWelcome().toString();
 assertEquals(expectedWelcome, actualWelcome);
 }

 @Test
 public void testConfigureExplorer_NoArgs() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[] {});
 assertNull(explorer.getRootPath());
 assertNull(explorer.getFsPathQuery());
 }

 @Test
 public void testConfigureExplorer_SinglePathArg() throws IOException {
 File rootDir = new File("/tmp");
 String[] args = {"/tmp"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 assertEquals(rootDir, explorer.getRootPath());
 assertNull(explorer.getFsPathQuery());
 }

 @Test
 public void testConfigureExplorer_SingleFindArg() throws IOException {
 String[] args = {"find", "*.txt"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 assertNull(explorer.getRootPath());
 assertEquals("*.txt", explorer.getFsPathQuery());
 }

 @Test
 public void testConfigureExplorer_MultipleArgs() throws IOException {
 File rootDir = new File("/tmp");
 String[] args = {"path", "/tmp", "find", "*.txt"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(args);
 assertEquals(rootDir, explorer.getRootPath());
 assertEquals("*.txt", explorer.getFsPathQuery());
 }

 @Test
 public void testCreateFSPath_NoRootPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 assertNotNull(explorer.getFsPath());
 }

 @Test
 public void testCreateFSPath_WithRootPath() {
 File rootDir = new File("/tmp");
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 explorer.setRootPath(rootDir);
 explorer.createFSPath();
 assertEquals(rootDir, explorer.getFsPath().getRootDirectory());
 }

 @Test
 public void testStart() throws IOException {
 String expectedOutput = "Current directory : /\n";
 explorer.start();assertEquals(expectedOutput, System.out.readLine());
 }

 @Test(expected = IOException.class)
 public void testStart_InvalidQuery() throws IOException {
 String invalidQuery = "//dir[@name = 'foo']/dir[@name = 'bar']/file[contains(name, 'foo')]//";
 explorer.setFsPathQuery(invalidQuery);
 explorer.start();
 }

 @Test
 public void testStart_ValidQuery() throws IOException {
 String expectedOutput = "changed to : /tmp\n";
 explorer.setRootPath(new File("/tmp"));
 explorer.start();
 explorer.setFsPathQuery("cd /tmp");
 explorer.start();
 assertEquals(expectedOutput, System.out.readLine());
 }

 @Test
 public void testStart_ValidQueryWithResults() throws IOException {
 File testDir = new File("/tmp/test");
 testDir.mkdirs();
 File file1 = new File(testDir, "test1.txt");
 File file2 = new File(testDir, "test2.txt");
 file1.createNewFile();
 file2.createNewFile();
 explorer.setRootPath(new File("/tmp"));
 explorer.start();
 explorer.setFsPathQuery("find *.txt");
 explorer.start();
 String expectedOutput1 = "/tmp/test/test1.txt\n";
 String expectedOutput2 = "/tmp/test/test2.txt\n";
 String line1 = System.out.readLine();
 String line2 = System.out.readLine();
 assertEquals(expectedOutput1, line1);
 assertEquals(expectedOutput2, line2);
 }

 @Test
 public void testStart_ValidQueryWithEmptyResult() throws IOException {
 explorer.setRootPath(new File("/tmp"));
 explorer.start();
 explorer.setFsPathQuery("find non-existent-file.txt");
 explorer.start();
 String expectedOutput = "";
 String line = System.out.readLine();
 assertEquals(expectedOutput, line);
 }

 @Test
 public void testStart_EmptyQuery() throws IOException {
 explorer.setRootPath(new File("/tmp"));
 explorer.start();
 explorer.setFsPathQuery("");
 explorer.start();
 assertEquals("", System.out.readLine());
 }

 @Test
 public void testStart_ValidQueryWithException() throws IOException {
 explorer.setRootPath(new File("/tmp"));
 explorer.start();
 explorer.setFsPathQuery("find /root/file.txt");
 explorer.start();
 String expectedOutput = "unable to change to /root please make sure that the directory exists and is readable\n";
 String line = System.out.readLine();
 assertEquals(expectedOutput, line);
 }

 @Test
 public void testStart_ValidQueryWithInvalidCommand() throws IOException {
 explorer.setRootPath(new File("/tmp"));
 explorer.start();
 explorer.setFsPathQuery("unknownCommand");
 explorer.start();
 String expectedOutput = "unknownCommand is not a valid command\n";
 String line = System.out.readLine();
 assertEquals(expectedOutput, line);
 }

 @Test
 public void testPrompt_ReadLine() throws IOException {
 String input = "ls -l";
 String expectedOutput = "ls\n";
 explorer.setPrompt(new Prompt(System.in, System.out, System.err));
 explorer.setPromptText("fspath>");
 String actualOutput = explorer.prompt.readLine();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testPrompt_ReadLineWithInput() throws IOException {
 String input = "ls -l";
 String expectedOutput = "ls\n";
 explorer.setPrompt(new Prompt(System.in, System.out, System.err));
 explorer.setPromptText("fspath>");
 explorer.prompt.setInput(input);
 String actualOutput = explorer.prompt.readLine();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testPrompt_ReadLine_EmptyInput() throws IOException {
 explorer.setPrompt(new Prompt(System.in, System.out, System.err));
 explorer.setPromptText("fspath>");
 explorer.prompt.setInput("");
 String actualOutput = explorer.prompt.readLine();
 assertEquals("", actualOutput);
 }

 @Test
 public void testHelpCommand() throws IOException {
 explorer.start();
 explorer.setFsPathQuery("help");
 explorer.start();
 String expectedOutput = "FSPathExplorer help :\n\n"
 + "Enter an FSPath query to search the filesystem\n"
 + "\n\nExamples :\n\n"
 + "\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n"
 + "\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n"
 + "\n   with a file size greater than 10000 bytes\n"
 + "\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory structure which begin with a '.'\n"
 + "\n\nAttributes : (see javadoc for java.io.File for more info)\n";
 String line = System.out.readLine();
 assertEquals(expectedOutput, line);
 }
}