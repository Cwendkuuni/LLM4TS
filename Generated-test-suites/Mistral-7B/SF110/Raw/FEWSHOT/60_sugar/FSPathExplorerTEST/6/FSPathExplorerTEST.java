package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathExplorerTest {

 @Test
 public void testConfigureExplorerWithValidPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/home/user/ Documents"};
 explorer.configureExplorer(args);
 assertEquals("/home/user/ Documents", explorer.rootPath.getAbsolutePath());
 }

 @Test
 public void testConfigureExplorerWithInvalidPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "invalid_path"};
 assertNull(explorer.rootPath);
 }

 @Test
 public void testConfigureExplorerWithValidFindQuery() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/home/user/ Documents", "-f", "*.txt"};
 explorer.configureExplorer(args);
 assertEquals(".txt", explorer.fsPathQuery.substring(explorer.fsPathQuery.length() - 3));
 }

 @Test
 public void testConfigureExplorerWithInvalidFindQuery() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/home/user/ Documents", "-f", "invalid_query"};
 assertNull(explorer.fsPathQuery);
 }

 @Test
 public void testCreateFSPathWithValidRootPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-p", "/home/user/ Documents"});
 explorer.createFSPath();
 assertNotNull(explorer.fsPath);
 }

 @Test(expected = RuntimeException.class)
 public void testCreateFSPathWithNullRootPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 fail("Expected an RuntimeException for null rootPath");
 }

 @Test
 public void testStartWithValidQuery() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/home/user/ Documents", "-f", "*.txt"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String expectedOutput = "/home/user/ Documents/TextFile1.txt\n/home/user/ Documents/TextFile2.txt";
 String actualOutput = "";
 explorer.start();
 while (explorer.prompt.isOpen()) {
 String line = explorer.prompt.readLine();
 actualOutput += line + "\n";
 }
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testStartWithInvalidQuery() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/home/user/ Documents", "-f", "invalid_query"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String expectedOutput = "";
 String actualOutput = "";
 explorer.start();
 while (explorer.prompt.isOpen()) {
 String line = explorer.prompt.readLine();
 actualOutput += line + "\n";
 }
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testStartWithHelpCommand() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/home/user/ Documents", "-f", "help"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 String expectedOutput = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\n";
 String actualOutput = "";
 explorer.start();
 while (explorer.prompt.isOpen()) {
 String line = explorer.prompt.readLine();
 actualOutput += line + "\n";
 }
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testStartWithPwdCommand() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/home/user/ Documents"};
 explorer.configureExplorer(args);
 explorer.createFSPath();
 explorer.start();
 String expectedOutput = "Current directory : /home/user/ Documents\n";
 String actualOutput = "";
 explorer.prompt.readLine(); // Read the prompt to start the query execution
 explorer.prompt.readLine(); // Read the command output
 assertEquals(expectedOutput, actualOutput);
 }
}