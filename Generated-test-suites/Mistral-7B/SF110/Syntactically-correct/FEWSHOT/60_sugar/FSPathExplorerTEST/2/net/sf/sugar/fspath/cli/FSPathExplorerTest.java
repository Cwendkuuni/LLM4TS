package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FSPathExplorerTest {

 FSPathExplorer explorer = new FSPathExplorer();
 File testDirectory = new File("target/test-classes"); // Assuming test classes are in target/test-classes

 @Test
 public void testSetup() {
 assertNotNull(explorer);
 assertNull(explorer.rootPath);
 assertNull(explorer.fsPath);
 }

 @Test
 public void testSetRootPath() throws IOException {
 File rootPath = new File("/tmp");
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 explorer.createFSPath();
 assertEquals(rootPath, explorer.rootPath);
 assertNotNull(explorer.fsPath);
 }

 @Test
 public void testSetQuery() throws IOException {
 explorer.configureExplorer(new String[]{"-f", "//dir[@name = 'test']"});
 assertEquals("//dir[@name = 'test']", explorer.fsPathQuery);
 }

 @Test
 public void testEmptyQuery() throws IOException {
 explorer.configureExplorer(new String[]{"-f", ""});
 assertEquals("", explorer.fsPathQuery);
 }

 @Test(expected = IOException.class)
 public void testInvalidQuery() throws IOException {
 explorer.configureExplorer(new String[]{"-f", "//"});
 }

 @Test
 public void testPwdCommand() throws IOException {
 explorer.createFSPath();
 explorer.start();
 String currentDirectory = explorer.prompt.readLine();
 assertEquals(testDirectory.getAbsolutePath(), currentDirectory);
 }

 @Test
 public void testCdCommand() throws IOException {
 explorer.createFSPath();
 explorer.start();
 explorer.prompt.sendKeyPresses(68, 69, 76, 76, 49); // cd ..\..
 File newDirectory = new File("src/test/resources");
 explorer.prompt.sendKeyPresses(68, 65, 83, 84, 49, 48, 49, 48, 48, 49, 48, 48, 50); // cd src/test/resources/
 assertEquals(newDirectory.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testHelpCommand() throws IOException {
 explorer.createFSPath();
 explorer.start();
 explorer.prompt.sendKeyPresses(68, 65, 69, 65, 6E, 74, 65, 72, 6E, 61, 6D, 65, 20, 68, 74, 74, 70, 32, 65, 6E, 63, 6F, 64, 65, 64, 20, 73, 79, 73, 74, 65, 6D); // help fsPath
 }

 @Test
 public void testValidQuery() throws IOException {
 explorer.createFSPath();
 explorer.start();
 explorer.fsPathQuery = "//dir[@name = 'test']";
 explorer.prompt.sendKeyPresses(68, 65, 6D, 65, 64, 73, 61, 6D, 79, 20, 63, 61, 63, 68, 65, 72, 73, 45, 64, 69, 67, 6E, 65, 64, 61, 74, 65, 20, 73, 65, 72, 76, 69, 78, 68, 73, 73, 20, 61, 20); // find dir test -type file .html
 FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
 assertNotNull(results);
 assertEquals(1, results.size());
 File expectedFile = new File(testDirectory.getAbsolutePath(), "test.html");
 assertEquals(expectedFile.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testInvalidQueryWithHelp() throws IOException {
 explorer.createFSPath();
 explorer.start();
 explorer.fsPathQuery = "//";
 explorer.prompt.sendKeyPresses(68, 65, 6E, 63, 6F, 64, 65, 64, 61, 74, 65, 20, 68, 65, 6D, 65, 64, 73, 61, 6D, 79, 20, 73, 68, 6F, 77, 6E, 20, 65, 78, 65, 78, 74, 21); // help //
 }

 @Test
 public void testValidQueryWithHelp() throws IOException {
 explorer.createFSPath();
 explorer.start();
 explorer.fsPathQuery = "//dir[@name = 'test']";
 explorer.prompt.sendKeyPresses(68, 65, 6D, 65, 64, 73, 61, 6D, 79, 20, 63, 61, 63, 68, 65, 72, 73, 45, 64, 69, 67, 6E, 65, 64, 61, 74, 65, 20, 68, 74, 74, 70, 32, 65, 6E, 63, 6F, 64, 65, 64, 20, 73, 79, 73, 74, 65, 6D, 21); // find dir test -type file .html help
 }

 @Test
 public void testInvalidQueryWithHelpAndExit() throws IOException {
 explorer.createFSPath();
 explorer.start();
 explorer.fsPathQuery = "//";
 explorer.prompt.sendKeyPresses(68, 65, 6E, 63, 6F, 64, 65, 64, 61, 74, 65, 20, 68, 65, 6D, 65, 64, 73, 61, 6D, 79, 20, 73, 68, 6F, 77, 6E, 20, 65, 78, 65, 78, 74, 21, 65, 78, 61, 72, 65, 20, 73, 65, 74, 65, 72, 6E, 65, 6E, 74, 68, 69, 73, 20, 65, 73, 65, 72, 76, 65, 72, 20, 74, 68, 69, 73, 2E, 65, 78, 61, 72, 65, 21); // help // exit
 }

 @Test
 public void testValidQueryWithHelpAndExit() throws IOException {
 explorer.createFSPath();
 explorer.start();
 explorer.fsPathQuery = "//dir[@name = 'test']";
 explorer.prompt.sendKeyPresses(68, 65, 6D, 65, 64, 73, 61, 6D, 79, 20, 63, 61, 63, 68, 65, 72, 73, 45, 64, 69, 67, 6E, 65, 64, 61, 74, 65, 20, 68, 74, 74, 70, 32, 65, 6E, 63, 6F, 64, 65, 64, 20, 73, 79, 73, 74, 65, 6D, 21, 65, 73, 65, 72, 76, 65, 72, 20, 74, 68, 69, 73, 2E, 65, 78, 61, 72, 65, 21, 65, 74, 68, 65, 72, 65, 20, 73, 65, 74, 65, 72, 6E, 65, 6E, 74, 68, 69, 73, 20, 65, 73, 65, 72, 76, 65, 72, 21); // find dir test -type file .html help exit
 }

 @Test
 public void testQuitCommand() throws IOException {
 explorer.createFSPath();
 explorer.start();
 explorer.prompt.sendKeyPresses(68, 65, 6E, 69, 65, 6E, 67, 20, 71, 75, 74, 68, 65, 72, 21); // quit
 }

}