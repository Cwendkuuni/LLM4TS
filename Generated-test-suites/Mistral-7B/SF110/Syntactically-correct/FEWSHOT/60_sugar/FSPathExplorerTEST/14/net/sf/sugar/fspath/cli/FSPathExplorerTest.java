package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathExplorerTest {

 // Test for configureExplorer method with valid arguments
 @Test
 public void testConfigureExplorerWithValidArguments() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-p", "/path/to/root", "-f", "query"};
 explorer.configureExplorer(args);
 assertEquals("/path/to/root", explorer.rootPath.getAbsolutePath());
 assertEquals("query", explorer.fsPathQuery);
 }

 // Test for configureExplorer method with missing arguments
 @Test(expected = IllegalArgumentException.class)
 public void testConfigureExplorerWithMissingArguments() {
 FSPathExplorer explorer = new FSPathExplorer();
 String[] args = {"-f", "query"};
 explorer.configureExplorer(args);
 assertNull(explorer.rootPath);
 assertNull(explorer.fsPathQuery);
 }

 // Test for createFSPath method
 @Test
 public void testCreateFSPath() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.createFSPath();
 assertNotNull(explorer.fsPath);
 }

 // Test for start method with valid query
 @Test
 public void testStartWithValidQuery() throws IOException {
 FSPathExplorer explorer = new FSPathExplorer();
 String query = "//dir[@name = 'test']";
 explorer.start();
 promptInput("fspath> " + query + "\n");
 FSPathResultList results = mockFSPathQueryResults(query);
 when(explorer.fsPath.query(eq(query))).thenReturn(results);
 assertEquals(1, results.size());
 verify(explorer.out).println(results.get(0).getFile().getPath());
 }

 // Test for start method with invalid query
 @Test
 public void testStartWithInvalidQuery() throws IOException {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.start();
 promptInput("fspath> invalidQuery\n");
 verify(explorer.err).println("FSPath query error : invalidQuery\n");
 }

 // Test for start method with help command
 @Test
 public void testStartWithHelpCommand() {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.start();
 promptInput("fspath> help\n");
 verify(explorer.out).println("FSPathExplorer help :");
 }

 // Test for start method with cd command
 @Test
 public void testStartWithCdCommand() throws IOException {
 FSPathExplorer explorer = new FSPathExplorer();
 File newDir = new File("/path/to/new/dir");
 explorer.start();
 promptInput("fspath> cd /path/to/new/dir\n");
 assertEquals("/path/to/new/dir", explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 // Test for start method with pwd command
 @Test
 public void testStartWithPwdCommand() throws IOException {
 FSPathExplorer explorer = new FSPathExplorer();
 File rootDir = new File("/root");
 explorer.rootPath = rootDir;
 explorer.createFSPath();
 explorer.start();
 promptInput("fspath> pwd\n");
 assertEquals(rootDir.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 // Test for start method with exit command
 @Test
 public void testStartWithExitCommand() throws IOException {
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.start();
 promptInput("fspath> exit\n");
 assertFalse(explorer.isAlive());
 }

 private void promptInput(String input) throws IOException {
 ProcessBuilder pb = new ProcessBuilder();
 pb.redirectErrorStream(true);
 Process p = pb.start("java", "-cp", "target/classes:lib/*", FSPathExplorer.class.getName(), (String[])null);
 p.getOutputStream().write(input.getBytes());
 p.getOutputStream().flush();
 p.getOutputStream().close();
 p.waitFor();
 }

 private FSPathResultList mockFSPathQueryResults(String query) {
 FSPathResultList results = mock(FSPathResultList.class);
 when(results.size()).thenReturn(1);
 FSPathResult result = mock(FSPathResult.class);
 when(result.getFile()).thenReturn(new File("/path/to/result"));
 when(results.get(0)).thenReturn(result);
 return results;
 }

}