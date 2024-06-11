package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {
    private FSPathExplorer explorer;
    private FSPath fsPath;

    @Before
    public void setUp() {
        this.explorer = new FSPathExplorer();
        this.fsPath = FSPathFactory.newFSPath(new File("."));
    }

    @Test
    public void testConstructor() {
        assertNotNull(explorer);
    }

    @Test
    public void testMain() throws IOException {
        // Given: No arguments are provided
        FSPathExplorer.main(new String[0]);
        // Then: Show the welcome message and exit
        assertTrue(System.out.toString().contains("+-------------------------------------------------------+"));
        assertTrue(System.out.toString().contains("Type 'help' for commands..."));
        assertTrue(System.out.toString().contains("Scanning filesystem metadata..."));
        assertTrue(System.out.toString().contains("+-------------------------------------------------------+"));
    }

    @Test
    public void testShowWelcome() {
        FSPathExplorer.showWelcome();
        String expectedWelcomeMessage = "+-------------------------------------------------------+";
        String expectedHelpMessage = "Type 'help' for commands...";
        String expectedScanMessage = "Scanning filesystem metadata...";
        String expectedExitMessage = "+-------------------------------------------------------+";

        assertTrue(System.out.toString().contains(expectedWelcomeMessage));
        assertTrue(System.out.toString().contains(expectedHelpMessage));
        assertTrue(System.out.toString().contains(expectedScanMessage));
        assertTrue(System.out.toString().contains(expectedExitMessage));
    }

    @Test
    public void testShowHelp() {
        FSPathExplorer.showHelp();

        // The output should contain the help message
        String expectedHelpMessage = "FSPathExplorer help :";
        String expectedUsageMessage = "usage : java -jar FSPathExplorer [options] <-f|-find> <FSPath_query>";
        String expectedAttributesMessage = "Attributes :";
        String expectedCommandsMessage = "Commands :";

        assertTrue(System.out.toString().contains(expectedHelpMessage));
        assertTrue(System.out.toString().contains(expectedUsageMessage));
        assertTrue(System.out.toString().contains(expectedAttributesMessage));
        assertTrue(System.out.toString().contains(expectedCommandsMessage));

        // Test that the attributes and commands are displayed correctly
        for (FSPathAttributes attribute : FSPathAttributes.values()) {
            assertTrue(System.out.toString().contains("@" + attribute));
        }

        for (Commands command : Commands.values()) {
            assertTrue(System.out.toString().contains(command.getCommandText()));
        }
    }

    @Test
    public void testConfigureExplorer_ValidArguments() throws IOException {
        String[] args = {"-p", "/path/to/root", "-f", "query"};
        explorer.configureExplorer(args);

        assertEquals("/path/to/root", explorer.rootPath.getAbsolutePath());
        assertEquals("query", explorer.fsPathQuery);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorer_InvalidArguments() throws IOException {
        String[] args = {"-p", "/path/to/root", "-f", "query", "invalid argument"};
        explorer.configureExplorer(args);
    }

    @Test
    public void testCreateFSPath() {
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFSPath_NullRootPath() {
        explorer.fsPath = null;
        explorer.createFSPath();
    }

    @Test
    public void testQuery_EmptyFSPathQuery() throws IOException {
        FSPathResultList resultList = fsPath.query("");
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void testQuery_SimpleFSPathQuery() throws IOException {
        FSPathResultList resultList = fsPath.query("*.txt");
        assertFalse(resultList.isEmpty());
        for (FSPathResult result : resultList) {
            assertTrue(result.getFile().isFile());
            String expectedFileName = result.getFile().getName().toLowerCase();
            assertTrue(expectedFileName.endsWith(".txt"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQuery_InvalidFSPathQuery() throws IOException {
        fsPath.query("invalid query");
    }

    @Test(expected = IOException.class)
    public void testQuery_IOError() throws IOException {
        FSPathResultList resultList = fsPath.query("query");
        assertFalse(resultList.isEmpty());

        File invalidFile = new File("inexistentFile.txt");
        resultList = fsPath.query("file:" + invalidFile.getPath());
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void testStart() throws IOException {
        String expectedCommandPrompt = "fspath>";

        String command = "pwd";
        explorer.start();
        String currentDirectory = explorer.prompt.readLine();
        assertEquals(expectedCommandPrompt, currentCommand);
        assertEquals(new File(".").getAbsolutePath(), currentDirectory);

        command = "cd /path/to/test";
        explorer.prompt.writeLine(command);
        currentDirectory = explorer.prompt.readLine();
        assertEquals("/path/to/test", currentDirectory);

        command = "help";
        explorer.prompt.writeLine(command);
        String helpMessage = System.out.toString();
        assertTrue(helpMessage.contains("FSPathExplorer help :"));
        assertTrue(helpMessage.contains("Type 'cd <directory>' to change the current directory"));

        command = "quit";
        explorer.prompt.writeLine(command);
        assertNull(explorer.prompt.getInputStream());
        assertNull(explorer.prompt.getOutputStream());
    }
}