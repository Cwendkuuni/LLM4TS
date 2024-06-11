package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

/**
 * Tests for the FSPathExplorer class
 *
 * @author your_name
 */
public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private FSPath fsPath;

 @Before
 public void setUp() {
 explorer = new FSPathExplorer();
 fsPath = FSPathFactory.newFSPath(new File("."));
 explorer.fsPath = fsPath;
 }

 @Test
 public void testConstructor() {
 // check that a new instance is created
 assertNotNull(explorer);
 }

 @Test
 public void testMain() throws IOException, InterruptedException {
 // create a test array of command line arguments
 String[] args = new String[]{"-p", "/tmp"};
 // simulate the main method call
 FSPathExplorer.main(args);
 // check that the rootPath is set correctly
 assertEquals("/tmp", explorer.rootPath.getAbsolutePath());
 }

 @Test
 public void testShowWelcome() {
 // check that the welcome message is displayed correctly
 String welcome = explorer.showWelcome().toString();
 // define expected output
 String expected = "+-------------------------------------------------------+\n" +
                  "|                                                       |\n" +
                  "| FSPathExplorer                                        |\n" +
                  "|                                                       |\n" +
                  "|                                                       |\n" +
                  "| FSPathExplorer                                        |\n" +
                  "|                                                       |\n" +
                  "| Type 'help' for commands...\n" +
                  "| Scanning filesystem metadata...\n" +
                  "| +-------------------------------------------------------+\n" +
                  "| PLEASE NOTE running FSPathExplorer from the top level    |\n" +
                  "| of your filesystem is not advised                       |\n" +
                  "| +-------------------------------------------------------+";
 // check that the output matches the expected
 assertEquals(expected, welcome);
 }

 @Test
 public void testShowHelp() {
 // check that the help message is displayed correctly
 String help = explorer.showHelp().toString();
 // define expected output
 String expected = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n" +
                 "   with a file size greater than 10000 bytes\n\nAttributes : (see javadoc for java.io.File for more info)\n" +
                 "  @size\n" +
                 "  @hidden\n" +
                 "  @system\n" +
                 "  @directory\n" +
                 "  @file\n" +
                 "  @regularFile\n" +
                 "  @symbolicLink\n" +
                 "  @device\n" +
                 "  @regularFileExist\n" +
                 "  @directoryExist\n" +
                 "  @readable\n" +
                 "  @writable\n" +
                 "  @executable\n" +
                 "  @hidden\n" +
                 "  @size\n" +
                 "  @lastModified\n" +
                 "  @path\n" +
                 "\nCommands :\n" +
                 " help : Display this help message\n" +
                 " pwd : Print the current directory\n" +
                 " cd <path> : Change the current directory to the given path\n" +
                 " exit : Exit FSPathExplorer\n";
 // check that the output matches the expected
 assertEquals(expected, help);
 }

 @Test
 public void testConfigureExplorer() {
 // create a test array of command line arguments
 String[] args = new String[]{"-p", "/tmp", "-f", "//file[@name = 'test.txt']"};
 // simulate the configureExplorer method call
 explorer.configureExplorer(args);
 // check that the rootPath and fsPathQuery are set correctly
 assertEquals("/tmp", explorer.rootPath.getAbsolutePath());
 assertEquals("//file[@name = 'test.txt']", explorer.fsPathQuery);
 }

 @Test
 public void testCreateFSPath() {
 // create a test File object
 File testFile = new File("/tmp/test.txt");
 // simulate the createFSPath method call
 fsPath = FSPathFactory.newFSPath(testFile);
 // check that the fsPath object is created correctly
 assertEquals("/tmp/test.txt", fsPath.getPathString());
 }

 @Test
 public void testStart() throws IOException {
 // create test query strings
 String pwdQuery = Commands.PWD.getCommandText();
 String cdQuery = "cd /tmp";
 String findQuery = "//file[@name = 'test.txt']";
 // simulate the start method call for each query
 String[] pwdArgs = new String[0];
 explorer.start();
 explorer.prompt.print("> " + pwdQuery + "\n");
 String pwdOutput = explorer.prompt.readLine();
 assertEquals(explorer.fsPath.getRootDirectory().getAbsolutePath(), pwdOutput);
 explorer.prompt.print("> " + cdQuery + "\n");
 explorer.prompt.print("> " + findQuery + "\n");
 FSPathResultList results = (FSPathResultList) explorer.prompt.readObject();
 // define expected output for findQuery
 FSPathResult expectedResult = new FSPathResult(testFile, FSPathAttributes.FILE);
 FSPathResultList expectedResults = new FSPathResultList(expectedResult);
 // check that the output matches the expected
 assertEquals(expectedResults.toString(), results.toString());
 }

 @Test(expected = IOException.class)
 public void testStartWithInvalidQuery() throws IOException {
 // simulate the start method call with an invalid query
 explorer.start();
 // check that an IOException is thrown
 explorer.prompt.print("> //dir[@name = 'test']/file[@name = 'test.txt']\n");
 }

 @Test
 public void testStartWithInvalidPath() {
 // simulate the start method call with an invalid path
 explorer.start();
 explorer.prompt.print("> cd /test/test/test\n");
 // check that an error message is displayed
 String errorMessage = explorer.prompt.readLine();
 // define expected error message
 String expected = "unable to change to /test/test/test please make sure that the directory exists and is readable";
 // check that the error message matches the expected
 assertEquals(expected, errorMessage);
 }
}