import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private Prompt prompt;

 @Before
 public void setUp() {
 this.explorer = new FSPathExplorer();
 this.prompt = new Prompt(System.in, System.out, System.err);
 this.prompt.setPromptText(">");
 }

 @Test
 public void testConstructor() {
 FSPathExplorer explorer = new FSPathExplorer();
 assertNotNull(explorer);
 }

 @Test
 public void testMainWithNoArguments() {
 String[] args = new String[0];
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that the welcome message was printed
 assertTrue(explorer.isWelcomeMessagePrinted());
 }

 @Test
 public void testMainWithInvalidArgument() {
 String[] args = {"--invalid"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that the usage message was printed
 assertTrue(explorer.isUsageMessagePrinted());
 }

 @Test
 public void testMainWithValidArguments() {
 String[] args = {"-p", "/tmp", "-f", "//file[@name = 'test.txt']"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that the prompt was initialized and that the directory was changed
 assertTrue(explorer.isPromptInitialized());
 assertEquals("/tmp", explorer.getRootPath().getAbsolutePath());
 }

 @Test
 public void testChangeDirectoryFailed() {
 String[] args = {"-cd", "/nonexistent"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that the error message was printed
 assertTrue(explorer.isErrorMessagePrinted());
 }

 @Test
 public void testFindFilesWithSimpleQuery() {
 String[] args = {"-p", "/tmp", "-f", "//file[@name = 'test.txt']"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // run the query and check that one result was returned
 List<FSPathResult> results = explorer.getResults();
 assertEquals(1, results.size());
 }

 @Test
 public void testFindFilesWithComplexQuery() {
 String[] args = {"-p", "/tmp", "-f", "//dir[@name = 'foo']/file[@name = 'bar']"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // run the query and check that one result was returned
 List<FSPathResult> results = explorer.getResults();
 assertEquals(1, results.size());
 }

 @Test
 public void testFindNoFiles() {
 String[] args = {"-p", "/tmp", "-f", "//file[@name = 'nonexistent.txt']"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that no results were returned
 List<FSPathResult> results = explorer.getResults();
 assertNull(results);
 }

 @Test
 public void testFindFilesWithEmptyQuery() {
 String[] args = {"-p", "/tmp", "-f", ""};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that no results were returned
 List<FSPathResult> results = explorer.getResults();
 assertNull(results);
 }

 @Test
 public void testFindFilesWithInvalidQuery() {
 String[] args = {"-p", "/tmp", "-f", "//file[@name = 'test.txt']//"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that no results were returned and that an error message was printed
 List<FSPathResult> results = explorer.getResults();
 assertNull(results);
 assertTrue(explorer.isErrorMessagePrinted());
 }

 @Test
 public void testPrintHelp() {
 String[] args = {"-h"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that the help message was printed
 assertTrue(explorer.isHelpMessagePrinted());
 }

 @Test
 public void testPrintUsage() {
 String[] args = {"-u"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that the usage message was printed
 assertTrue(explorer.isUsageMessagePrinted());
 }

 @Test
 public void testPrintWelcome() {
 String[] args = {"-w"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that the welcome message was printed
 assertTrue(explorer.isWelcomeMessagePrinted());
 }

 @Test
 public void testQuit() {
 String[] args = {"-q"};
 FSPathExplorer explorer = new FSPathExplorer();
 explorer.main(args);
 // check that the program exits
 assertFalse(explorer.isRunning());
 }

 @After
 public void tearDown() {
 this.prompt.close();
 }

 private boolean isRunning() {
 return this.explorer.isRunning();
 }

 private boolean isPromptInitialized() {
 return this.explorer.isPromptInitialized();
 }

 private boolean isWelcomeMessagePrinted() {
 return this.prompt.getPrintedLines().contains(FSPathExplorer.showWelcome().toString());
 }

 private boolean isHelpMessagePrinted() {
 return this.prompt.getPrintedLines().contains(FSPathExplorer.showHelp().toString());
 }

 private boolean isUsageMessagePrinted() {
 return this.prompt.getPrintedLines().contains(FSPathExplorer.showUsage().toString());
 }

 private List<FSPathResult> getResults() {
 return this.explorer.getResults();
 }
}