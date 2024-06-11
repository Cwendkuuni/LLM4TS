package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

/**
 * Test class for FSPathExplorer.
 */
public class FSPathExplorerTest {

    private final String welcomeMessage = "+-------------------------------------------------------+"
            + "\n|                                                       |"
            + "\n| FSPathExplorer                                        |"
            + "\n|                                                       |"
            + "\n+-------------------------------------------------------+\n"
            + "\nType 'help' for commands...\n"
            + "\nScanning filesystem metadata...\n"
            + "\n+-------------------------------------------------------+";
    private final String usageMessage = "usage : java -jar FSPathExplorer [options] <-f|-find> <FSPath_query>\n"
            + "\n\n\t Options : \n\t\t -path <root_path> : the relative or absolute path of the directory to start from.\n\t\t -p <root_path> : see -path.\n\n\t Example FSPath queries :\n\t\t //dir[@name = 'var']/dir[@name = 'www']/dir[@name = 'htdocs']/file[contains(name, '.html')]\n"
            + "\t\t finds any file which is nested anywhere below a 'src' directory\n\t\t with a file size greater than 10000 bytes\n\n";
    private final String helpMessage = "FSPathExplorer help :\n"
            + "\n\nEnter an FSPath query to search the filesystem\n\n"
            + "Examples :\n\n"
            + "\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n"
            + "\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n"
            + "   with a file size greater than 10000 bytes\n\n"
            + "\nAttributes : (see javadoc for java.io.File for more info)\n";

    private FSPathExplorer explorer;
    private ByteArrayOutputStream outContent;
    private ByteArrayInputStream inContent;
    private PrintStream originalOut;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        inContent = new ByteArrayInputStream(welcomeMessage.getBytes(StandardCharsets.UTF_8));
        System.setIn(inContent);
        explorer = new FSPathExplorer();
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }

    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer.fsPath);
        assertEquals("", explorer.fsPathQuery);
        assertEquals(System.out, explorer.out);
        assertEquals(System.err, explorer.err);
    }

    @Test
    public void testMainNoArgs() {
        String[] args = {};
        FSPathExplorer.main(args);
        String output = outContent.toString();
        String expected = "Error: missing argument: <-f|-find> <FSPath_query>\n"
                + usageMessage;
        assertTrue(output.contains(expected));
    }

    @Test
    public void testMainInvalidOption() {
        String[] args = {"-x", "-f", "query"};
        FSPathExplorer.main(args);
        String output = outContent.toString();
        String expected = "Invalid option: -x\n" + usageMessage;
        assertTrue(output.contains(expected));
    }

    @Test
    public void testMainValidArgs() {
        String[] args = {"-p", "/tmp", "-f", "//dir[@name = 'foo']"};
        FSPathExplorer.main(args);
        String output = outContent.toString();
        // The output of the test is not stable as it depends on the content of "/tmp"
        // Instead, we can check if the expected phrases are present in the output
        String[] expectedLines = {"Scanning filesystem metadata...", "Found 0 results."};
        List<String> lines = Arrays.asList(output.split("\n"));
        for (String line : expectedLines) {
            assertTrue(lines.contains(line));
        }
        // The last line should be an empty line
        String lastLine = lines.get(lines.size() - 1);
        assertTrue(lastLine.isEmpty());
    }

    @Test
    public void testShowWelcome() {
        explorer.showWelcome();
        String output = outContent.toString();
        assertEquals(welcomeMessage, output);
    }

    @Test
    public void testShowHelp() {
        explorer.showHelp();
        String output = outContent.toString();
        assertEquals(helpMessage, output);
    }

    @Test
    public void testConfigureExplorerWithValidArgs() {
        String[] args = {"-p", "/tmp", "-f", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        File rootPath = explorer.rootPath;
        String query = explorer.fsPathQuery;
        assertNotNull(rootPath);
        assertEquals("/tmp", rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", query);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithMissingPathArg() {
        String[] args = {"-f", "//dir[@name = 'foo']"};
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithMissingFindArg() {
        String[] args = {"-p", "/tmp"};
        explorer.configureExplorer(args);
        assertNull(explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() {
        explorer.createFSPath();
        FSPath fsPath = explorer.fsPath;
        assertNotNull(fsPath);
        if (explorer.rootPath != null) {
            assertEquals(explorer.rootPath, fsPath.getRootDirectory());
        } else {
            assertEquals(System.getProperty("user.dir"), fsPath.getRootDirectory().getAbsolutePath());
        }
    }

    @Test
    public void testStartNoQuery() {
        explorer = new FSPathExplorer();
        explorer.start();
        String output = outContent.toString();
        String expected = "Error: missing argument: <FSPath_query>\n" + usageMessage;
        assertTrue(output.contains(expected));
    }

    @Test
    public void testStartWithQuery() throws IOException {
        File testDir = new File("/tmp/junit-test-files");
        testDir.mkdirs();
        String[] args = {"-p", testDir.getAbsolutePath(), "-f", "//file[name = 'test.txt']"};
        explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        String query = explorer.fsPathQuery;
        explorer.start();
        String output = outContent.toString();
        // The output of the test is not stable as it depends on the content of the test directory
        // Instead, we can check if the expected phrases are present in the output
        String[] expectedLines = {"Scanning filesystem metadata...", "Found 1 result(s).", testDir.getAbsolutePath() + "/test.txt"};
        List<String> lines = Arrays.asList(output.split("\n"));
        for (String line : expectedLines) {
            assertTrue(lines.contains(line));
        }
        // The last line should be an empty line
        String lastLine = lines.get(lines.size() - 1);
        assertTrue(lastLine.isEmpty());
        testDir.delete();
    }

    @Test(expected = IOException.class)
    public void testStartWithInvalidPath() throws IOException {
        File testDir = new File("/tmp/junit-test-files");
        testDir.mkdirs();
        String[] args = {"-p", "//not/existing/directory", "-f", "//dir[@name = 'foo']"};
        explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
    }

    @Test
    public void testPwdCommand() {
        File testDir = new File(System.getProperty("user.dir") + "/junit-test-files");
        testDir.mkdirs();
        String[] args = {"-p", testDir.getAbsolutePath()};
        explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        String query = explorer.fsPathQuery;
        explorer.start();
        String input = "pwd\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);
        explorer.start();
        String output = outContent.toString();
        String expected = testDir.getAbsolutePath();
        assertEquals(expected, output.split("\n")[1]);
        testDir.delete();
    }

    @Test
    public void testCdCommand() {
        File testDir1 = new File(System.getProperty("user.dir") + "/junit-test-files1");
        testDir1.mkdirs();
        File testDir2 = new File(System.getProperty("user.dir") + "/junit-test-files2");
        testDir2.mkdirs();
        String[] args = {"-p", System.getProperty("user.dir")};
        explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        String input = "cd " + testDir1.getName() + "\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);
        explorer.start();
        String output = outContent.toString();
        String expected = testDir1.getAbsolutePath();
        assertEquals(expected, output.split("\n")[1]);
        input = "cd ..\n";
        in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);
        explorer.start();
        output = outContent.toString();
        assertEquals(System.getProperty("user.dir"), output.split("\n")[1]);
        testDir1.delete();
        testDir2.delete();
    }

    @Test
    public void testHelpCommand() {
        explorer = new FSPathExplorer();
        explorer.start();
        String input = "help\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(in);
        explorer.start();
        String output = outContent.toString();
        String expected = helpMessage;
        assertEquals(expected, output);
    }

}