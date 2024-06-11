package net.sf.sugar.fspath.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class FSPathExplorerTest {

 private FSPathExplorer fsPathExplorer;
 private String rootPath;

 @Before
 public void setUp() {
 rootPath = new File(".").getAbsolutePath();
 fsPathExplorer = new FSPathExplorer();
 fsPathExplorer.rootPath = new File(rootPath);
 }

 @Test
 public void testSimpleQuery() {
 String query = "//dir[@name = 'src']//file[@name = 'Test.java']";
 FSPathResultList results = fsPathExplorer.fsPath.query(query);
 assertTrue(results.size() > 0);
 File expectedFile = new File(rootPath + "/src/Test.java");
 File actualFile = results.get(0).getFile();
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testEmptyQuery() {
 String query = "";
 FSPathResultList results = fsPathExplorer.fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test
 public void testInvalidQuery() {
 String query = "//dir[@name = 'invalid']//file[@name = 'Test.java']";
 FSPathResultList results = fsPathExplorer.fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test
 public void testInvalidPath() {
 String query = "//dir[@name = '/invalid']//file[@name = 'Test.java']";
 FSPathResultList results = fsPathExplorer.fsPath.query(query);
 assertEquals(0, results.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPath2() {
 String query = "//dir[@name = '..']//file[@name = 'Test.java']";
 fsPathExplorer.fsPath.query(query);
 }

 @Test
 public void testChangeDirectory() {
 String query = "p ..";
 fsPathExplorer.prompt.setIn(System.in);
 fsPathExplorer.prompt.setOut(System.out);
 fsPathExplorer.prompt.setErr(System.err);
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 String currentDirectory = fsPathExplorer.prompt.readLine();
 assertEquals("/", currentDirectory);
 fsPathExplorer.prompt.write("cd ../");
 fsPathExplorer.prompt.readLine();
 String newDirectory = fsPathExplorer.prompt.readLine();
 assertEquals(".", newDirectory);
 query = "p .";
 FSPathResultList results = fsPathExplorer.fsPath.query(query);
 File expectedFile = new File(rootPath + "/Test.java"); //assuming Test.java exists in the root directory
 File actualFile = results.get(0).getFile();
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testHelpCommand() {
 String query = "help";
 fsPathExplorer.prompt.setIn(System.in);
 fsPathExplorer.prompt.setOut(System.out);
 fsPathExplorer.prompt.setErr(System.err);
 fsPathExplorer.createFSPath();
 fsPathExplorer.start();
 fsPathExplorer.prompt.write(query);
 String output = fsPathExplorer.prompt.readAllLines();
 //Assuming the help text is printed properly, this assertion can be modified accordingly.
 assertTrue(output.contains("FSPathExplorer help :") && output.contains("Commands :"));
 }

 @After
 public void tearDown() {
 fsPathExplorer = null;
 }
}