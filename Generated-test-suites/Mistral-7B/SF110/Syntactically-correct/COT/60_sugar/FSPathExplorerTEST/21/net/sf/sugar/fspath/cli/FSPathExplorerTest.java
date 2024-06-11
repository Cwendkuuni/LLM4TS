package net.sf.sugar.fspath.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * FSPathExplorerTest class
 */
public class FSPathExplorerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final String ROOT_PATH = ".";
    private static final String EMPTY_QUERY = "";
    private static final String VALID_QUERY = "//dir[@name = 'var']/dir[@name = 'www']/dir[@name = 'htdocs']/file[contains(name, '.html')]";

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testDefaultConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer.getRootPath());
        assertEquals(EMPTY_QUERY, explorer.getFsPathQuery());
        assertNull(explorer.getFsPath());
        assertNull(explorer.getPrompt());
        assertNull(explorer.getOut());
        assertNull(explorer.getErr());
    }

    @Test
    public void testConstructorWithArgs() throws IOException {
        String rootPath = "/path/to/root";
        String query = "//file";
        String[] args = {"-p", rootPath, "-f", query};

        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);

        assertEquals(rootPath, explorer.getRootPath().getAbsolutePath());
        assertEquals(query, explorer.getFsPathQuery());
        assertNotNull(explorer.getFsPath());
        assertNotNull(explorer.getPrompt());
        assertNotNull(explorer.getOut());
        assertNotNull(explorer.getErr());
    }

    @Test
    public void testShowWelcome() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showWelcome();
        String expectedWelcomeMessage = new StringBuffer("+-------------------------------------------------------+")
                .append("\n|                                                       |")
                .append("\n| FSPathExplorer                                        |")
                .append("\n|                                                       |")
                .append("\n+-------------------------------------------------------+\n")
                .append("\nType 'help' for commands...\n")
                .append("\nScanning filesystem metadata...\n")
                .append("\n+-------------------------------------------------------+")
                .append("\n| PLEASE NOTE running FSPathExplorer from the top level |")
                .append("\n| of your filesystem is not advised                     |")
                .append("\n+-------------------------------------------------------+")
                .toString();
        assertEquals(expectedWelcomeMessage, outContent.toString());
    }

    @Test
    public void testShowHelp() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showHelp();
        String expectedHelpMessage = new StringBuffer("FSPathExplorer help :")
                .append("\n\nEnter an FSPath query to search the filesystem")
                .append("\n\nExamples :")
                .append("\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'")
                .append("\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory")
                .append("\n   with a file size greater than 10000 bytes")
                .append("\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'")
                .append("\n\nAttributes : (see javadoc for java.io.File for more info)")
                .append("\n")
                .append("@")
                .append(FSPathAttributes.size)
                .append("\n\nCommands :\n")
                .append("\n").append(Commands.EXIT.getCommandText()).append("\t: exit the application\n")
                .append("\n").append(Commands.PWD.getCommandText()).append("\t: print the current directory\n")
                .append("\n")
                .append(Commands.CD.getCommandText()).append("\t: change directory\n")
                .append("\n").append(Commands.HELP.getCommandText()).append("\t: print this help message\n")
                .toString();
        assertEquals(expectedHelpMessage, outContent.toString());
    }

    @Test
    public void testConfigureExplorerWithValidArguments() {
        String rootPath = "/path/to/root";
        String query = "//file";
        String[] args = {"-p", rootPath, "-f", query};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);

        assertEquals(rootPath, explorer.getRootPath().getAbsolutePath());
        assertEquals(query, explorer.getFsPathQuery());
        assertNotNull(explorer.getFsPath());
        assertNotNull(explorer.getPrompt());
        assertNotNull(explorer.getOut());
        assertNotNull(explorer.getErr());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithMissingArguments() {
        String[] args = {"-f", "//file"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        fail("IllegalArgumentException expected, but not thrown.");
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.getFsPath());

        File root = explorer.getFsPath().getRootDirectory();
        if (explorer.getRootPath() != null) {
            assertEquals(explorer.getRootPath().getAbsolutePath(), root.getAbsolutePath());
        } else {
            assertEquals(ROOT_PATH, root.getAbsolutePath());
        }
    }

    @Test
    public void testStartWithEmptyQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();

        String expectedMessage = "Scanning filesystem metadata...";
        assertEquals(expectedMessage, outContent.toString());
        String expectedPwdMessage = "Current directory : .";
        String pwd = explorer.prompt.readLine();
        assertEquals(expectedPwdMessage, pwd);
    }

    @Test
    public void testStartWithValidQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.configureExplorer(new String[]{"-f", VALID_QUERY});
        explorer.start();

        String expectedMessage1 = "Scanning filesystem metadata...";
        String expectedMessage2 = "Type 'help' for commands...";
        String expectedMessage3 = "Current directory : /path/to/root";
        String expectedMessage4 = "+-------------------------------------------------------+";
        String expectedMessage5 = "|                                                       |";
        String expectedMessage6 = "| FSPathExplorer                                        |";
        String expectedMessage7 = "|                                                       |";
        String expectedMessage8 = "+-------------------------------------------------------+\n";

        assertEquals(expectedMessage1, outContent.toString().substring(0, expectedMessage1.length()));
        assertEquals(expectedMessage2, outContent.toString().substring(expectedMessage1.length(), expectedMessage2.length()));
        assertEquals(expectedMessage3, outContent.toString().substring(expectedMessage2.length(), expectedMessage3.length()));
        assertEquals(expectedMessage4, outContent.toString().substring(expectedMessage3.length(), expectedMessage4.length()));
        assertEquals(expectedMessage5, outContent.toString().substring(expectedMessage4.length(), expectedMessage5.length()));
        assertEquals(expectedMessage6, outContent.toString().substring(expectedMessage5.length(), expectedMessage6.length()));
        assertEquals(expectedMessage7, outContent.toString().substring(expectedMessage6.length(), expectedMessage7.length()));
        assertEquals(expectedMessage8, outContent.toString().substring(expectedMessage7.length()));

        String expectedQueryResult = "/path/to/var/www/htdocs/file1.html" + System.lineSeparator()
                + "/path/to/var/www/htdocs/file2.html";
        String expectedQueryResultMessage = "Found: " + expectedQueryResult.trim();
        assertEquals(expectedQueryResultMessage, outContent.toString().substring(outContent.toString().length() - expectedQueryResultMessage.length()));
    }

    @Test
    public void testStartWithInvalidPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.configureExplorer(new String[]{"-p", "invalid_path"});
        explorer.start();

        String expectedMessage = "unable to change to invalid_path please make sure that the directory exists and is readable";
        assertEquals(expectedMessage, outContent.toString());
    }

    @Test
    public void testStartWithInvalidQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.configureExplorer(new String[]{"-f", "invalid_query"});
        explorer.start();

        String expectedMessage = "No results found for query 'invalid_query'";
        assertEquals(expectedMessage, outContent.toString());
    }
}