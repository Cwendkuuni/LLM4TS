package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;
 private Prompt prompt;
 private PrintStream out;
 private PrintStream err;

 @Before
 public void setUp() {
 out = mock(PrintStream.class);
 err = mock(PrintStream.class);
 prompt = new Prompt(System.in, out, err);
 explorer = new FSPathExplorer();
 explorer.out = out;
 explorer.err = err;
 explorer.prompt = prompt;
 }

 @After
 public void tearDown() {
 }

 @Test
 public void testCreateFSPath() throws IOException {
 File rootPath = new File("/");
 explorer.rootPath = rootPath;
 explorer.createFSPath();
 assertEquals("/", explorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testStart() throws IOException {
 FSPathResultList results = new FSPathResultList();
 results.add(new FSPathResult(new File("/test1.txt")));
 results.add(new FSPathResult(new File("/test2.txt")));
 when(fsPath.query(anyString())).thenReturn(results);

 explorer.createFSPath();
 String query = "//file[@name = 'test.*']";
 explorer.fsPathQuery = query;
 explorer.start();
 verify(out, times(2)).println("/test1.txt");
 verify(out, times(2)).println("/test2.txt");
 }

 @Test
 public void testConfigureExplorerWithPathAndQuery() {
 String[] args = {"-p", "/path/to/root", "-f", "//file[@name = 'test.*']"};
 explorer.configureExplorer(args);
 assertEquals("/path/to/root", explorer.rootPath.getAbsolutePath());
 assertEquals("//file[@name = 'test.*']", explorer.fsPathQuery);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConfigureExplorerWithInvalidOption() {
 String[] args = {"-z", "/path/to/root", "-f", "//file[@name = 'test.*']"};
 explorer.configureExplorer(args);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test
 public void testShowWelcome() {
 String expected = "+-------------------------------------------------------+\n" +
         "|                                                       |\n" +
         "| FSPathExplorer                                        |\n" +
         "|                                                       |\n" +
         "+-------------------------------------------------------+\n" +
         "Type 'help' for commands...\n" +
         "Scanning filesystem metadata...\n" +
         "+-------------------------------------------------------+\n" +
         "PLEASE NOTE running FSPathExplorer from the top level \n" +
         "of your filesystem is not advised                     +\n" +
         "+-------------------------------------------------------+";
 String result = FSPathExplorer.showWelcome();
 assertEquals(expected, result);
 }
}