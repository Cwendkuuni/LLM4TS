package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testFSPathExplorerInstance() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    @Test
    public void testConfigureExplorer() throws IOException {
        String[] args = new String[]{"-p", "/path/to/root", "-f", "query"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertEquals("/path/to/root", explorer.rootPath.getAbsolutePath());
        assertEquals("query", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        assertEquals(rootPath, explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStart() throws IOException {
        String[] args = new String[]{"-p", "/path/to/root", "-f", "query"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        String expectedQuery = "query";
        explorer.start();
        String currentExpression = null;
        while (currentExpression == null) {
            currentExpression = explorer.prompt.readLine();
        }
        assertEquals(expectedQuery, currentExpression);
    }

    @Test
    public void testPWDCommand() throws IOException {
        String[] args = new String[]{"-p", "/path/to/root"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        String currentExpression = null;
        while (!currentExpression.equalsIgnoreCase(Commands.PWD.getCommandText())) {
            currentExpression = explorer.prompt.readLine();
        }
        String currentDirectory = explorer.fsPath.getRootDirectory().getAbsolutePath();
        explorer.prompt.readLine(); // read the result of the pwd command
        assertEquals(currentDirectory, currentExpression);
    }

    @Test
    public void testCDCommand() throws IOException {
        String[] args = new String[]{"-p", "/path/to/root"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        File newDir = new File("/new/directory");
        explorer.start();
        String currentExpression = null;
        while (!currentExpression.equalsIgnoreCase(Commands.CD.getCommandText())) {
            currentExpression = explorer.prompt.readLine();
        }
        explorer.prompt.readLine(); // read the current directory
        explorer.prompt.readLine(); // read the new directory path
        explorer.prompt.readLine(); // read the confirmation message
        File expectedNewDir = new File("/new/directory");
        explorer.prompt.readLine(); // read the new current directory
        assertEquals(expectedNewDir.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testHelpCommand() throws IOException {
        String[] args = new String[]{"-p", "/path/to/root"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        String currentExpression = null;
        while (!currentExpression.equalsIgnoreCase(Commands.HELP.getCommandText())) {
            currentExpression = explorer.prompt.readLine();
        }
        explorer.prompt.readLine(); // read the help text
    }

    @Test
    public void testInvalidCommand() throws IOException {
        String[] args = new String[]{"-p", "/path/to/root"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        String currentExpression = null;
        while (currentExpression == null || !currentExpression.equalsIgnoreCase(Commands.EXIT.getCommandText())) {
            currentExpression = explorer.prompt.readLine();
            if (currentExpression != null) {
                String[] expressionWords = currentExpression.split(" ");
                String command = expressionWords[0];
                if (!Commands.isCommand(command)) {
                    explorer.prompt.print("'{}' is not a valid command.\n", command);
                    explorer.prompt.readLine();
                }
            }
        }
    }

    @Test
    public void testInvalidFSPathQuery() throws IOException {
        String[] args = new String[]{"-p", "/path/to/root"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        String currentExpression = null;
        while (currentExpression == null) {
            currentExpression = explorer.prompt.readLine();
        }
        String[] expressionWords = currentExpression.split(" ");
        String query = "";
        for (int i = 1; i < expressionWords.length; i++) {
            query += expressionWords[i] + " ";
        }
        explorer.fsPathQuery = query;
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertNull(results);
    }

    @Test
    public void testValidFSPathQuery() throws IOException {
        String[] args = new String[]{"-p", "/path/to/root"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        String currentExpression = null;
        while (currentExpression == null) {
            currentExpression = explorer.prompt.readLine();
        }
        String[] expressionWords = currentExpression.split(" ");
        String query = "";
        for (int i = 1; i < expressionWords.length; i++) {
            query += expressionWords[i] + " ";
        }
        explorer.fsPathQuery = query;
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertNotNull(results);
    }

}