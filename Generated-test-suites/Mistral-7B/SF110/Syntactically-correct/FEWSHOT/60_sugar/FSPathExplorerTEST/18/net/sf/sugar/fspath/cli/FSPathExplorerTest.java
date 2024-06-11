package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathExplorerTest {

 @Test
 public void testConfigureExplorerWithValidPathArgument() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/Users/testuser/testfolder"};
 explorer.configureExplorer(args);
 assertEquals(new File("/Users/testuser/testfolder"), explorer.rootPath);
 }

 @Test
 public void testConfigureExplorerWithValidFindArgument() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/Users/testuser/testfolder", "-f", "testfile.txt"};
 explorer.configureExplorer(args);
 assertEquals(new File("/Users/testuser/testfolder"), explorer.rootPath);
 assertEquals("testfile.txt", explorer.fsPathQuery);
 }

 @Test
 public void testCreateFSPathWithValidRootPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-p", "/Users/testuser/testfolder"});
 explorer.createFSPath();
 assertTrue(explorer.fsPath.getRootDirectory() instanceof File);
 assertEquals("/Users/testuser/testfolder", explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testCreateFSPathWithNullRootPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 assertNotNull(explorer.fsPath);
 }

 @Test
 public void testStartWithValidQuery() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/Users/testuser/testfolder", "-f", "*.txt"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String query = "*.txt";
 String expectedOutput = "/Users/testuser/testfolder/testfile.txt";
 String input = explorer.prompt.readLine();
 explorer.start();
 assertEquals(expectedOutput, input);
 }

 @Test
 public void testStartWithInvalidCommand() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/Users/testuser/testfolder", "invalidcommand"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String input = explorer.prompt.readLine();
 assertNull(input);
 }

 @Test
 public void testStartWithHelpCommand() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/Users/testuser/testfolder", "help"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String expectedOutput = "FSPathExplorer help :\n\n";
 String input = explorer.prompt.readLine();
 assertEquals(expectedOutput, input);
 }

 @Test
 public void testStartWithValidCDCommand() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/Users/testuser/testfolder"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String input = explorer.prompt.readLine();
 explorer.start();
 String cdCommand = "cd testsubfolder";
 explorer.start();
 String expectedOutput = "/Users/testuser/testfolder/testsubfolder";
 assertEquals(expectedOutput, input);
 }

 @Test
 public void testStartWithInvalidCDCommand() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/Users/testuser/testfolder"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String input = explorer.prompt.readLine();
 explorer.start();
 String cdCommand = "cd testsubfolderx";
 explorer.start();
 assertNull(input);
 }
}