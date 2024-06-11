package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;

 @Test
 public void testFSPathExplorerInitialization() {
 explorer = new FSPathExplorer();
 assertNotNull(explorer.getRootPath());
 assertNull(explorer.getFsPathQuery());
 assertNull(explorer.getFsPath());
 assertNotNull(explorer.getPrompt());
 assertNotNull(explorer.getOut());
 assertNotNull(explorer.getErr());
 }

 @Test
 public void testConfigureExplorerWithRootPath() {
 File rootPath = new File("/tmp");
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 assertEquals(rootPath, explorer.getRootPath());
 assertNull(explorer.getFsPathQuery());
 assertNull(explorer.getFsPath());
 }

 @Test
 public void testConfigureExplorerWithFSPathQuery() {
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-f", "//dir[@name = 'foo']"});
 assertEquals("//dir[@name = 'foo']", explorer.getFsPathQuery());
 assertNull(explorer.getRootPath());
 assertNull(explorer.getFsPath());
 }

 @Test
 public void testConfigureExplorerWithBothRootPathAndFSPathQuery() {
 File rootPath = new File("/tmp");
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//dir[@name = 'foo']"});
 assertEquals(rootPath, explorer.getRootPath());
 assertEquals("//dir[@name = 'foo']", explorer.getFsPathQuery());
 assertNull(explorer.getFsPath());
 }

 @Test
 public void testCreateFSPathWithRootPath() {
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 assertNotNull(explorer.getFsPath());
 assertEquals(explorer.getRootPath(), explorer.getFsPath().getRootDirectory());
 }

 @Test
 public void testCreateFSPathWithoutRootPath() {
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-f", "//dir[@name = 'foo']"});
 explorer.createFSPath();
 assertNotNull(explorer.getFsPath());
 assertNull(explorer.getRootPath());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateFSPathWithInvalidArgument() {
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-x", "invalid_option"});
 explorer.createFSPath();
 }

 @Test
 public void testStartCommand_PWD() {
 String expected = "/tmp";
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 String actual = explorer.prompt.readLineAndExecuteCommand("pwd");
 assertEquals(expected, actual);
 }

 @Test
 public void testStartCommand_CD() {
 File newDir = new File("/var");
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 explorer.prompt.readLineAndExecuteCommand("cd " + newDir.getAbsolutePath());
 assertEquals(newDir, explorer.getFsPath().getRootDirectory());
 }

 @Test(expected = IOException.class)
 public void testStartCommand_InvalidCD() {
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 explorer.prompt.readLineAndExecuteCommand("cd invalid_directory");
 }

 @Test
 public void testStartCommand_Help() {
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 explorer.prompt.readLineAndExecuteCommand("help");
 // add assertions for the output here
 }

 @Test
 public void testStartCommand_Find() {
 File rootPath = new File("/tmp");
 File queryDir = new File("/tmp/query");
 queryDir.mkdirs();
 File queryFile = new File(queryDir, "query.txt");
 queryFile.createNewFile();
 explorer = new FSPathExplorer();
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//file[@name = 'query.txt']"});
 explorer.createFSPath();
 explorer.prompt.readLineAndExecuteCommand("find");
 assertEquals(queryFile.getPath(), explorer.prompt.readLine());
 }

 @Test
 public void testStartCommand_Exit() {
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 explorer.prompt.readLineAndExecuteCommand("exit");
 assertNull(explorer);
 }

 @Test
 public void testStartCommand_InvalidQuery() {
 explorer = new FSPathExplorer();
 explorer.createFSPath();
 explorer.prompt.readLineAndExecuteCommand("find invalid_query");
 }

}