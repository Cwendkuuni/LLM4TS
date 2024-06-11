package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.cli.Util;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private Prompt promptMock;
    private FSPath fsPathMock;
    private FSPathFactory fsPathFactoryMock;

    @org.junit.Before
    public void setup() throws Exception {
        fsPathFactoryMock = mock(FSPathFactory.class);
        fsPathMock = mock(FSPath.class);
        when(fsPathFactoryMock.newFSPath(any(File.class))).thenReturn(fsPathMock);
        when(fsPathMock.query(anyString())).thenReturn(new FSPathResultList(new FSPathResult[]{new FSPathResult(new File("testPath1"))}, null));

        promptMock = mock(Prompt.class);

        explorer = new FSPathExplorer();
        explorer.fsPathFactory = fsPathFactoryMock;
        explorer.prompt = promptMock;
    }

    @org.junit.Test
    public void testStartWithValidArguments() throws IOException {
        String[] args = new String[]{"-p", "/path/to/root", "-f", "//file[name = 'testFile']"};

        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();

        String actualQuery = explorer.fsPathQuery;
        String expectedQuery = "//file[name = 'testFile']";

        assertEquals(expectedQuery, actualQuery);

        verify(promptMock, times(1)).readLine();
        verify(fsPathMock, times(1)).query(expectedQuery);
    }

    @org.junit.Test
    public void testStartWithInvalidArguments() {
        String[] args = new String[]{};

        explorer.configureExplorer(args);
        explorer.createFSPath();

        try {
            explorer.start();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testHelpCommand() {
        explorer.start();
        verify(out, times(1)).println("FSPathExplorer help :");
    }

    @org.junit.Test
    public void testPwdCommand() {
        String currentPath = "/path/to/root";
        File currentDirectory = new File(currentPath);
        when(fsPathMock.getRootDirectory()).thenReturn(currentDirectory);

        explorer.createFSPath();
        explorer.start();
        String expectedOutput = "Current directory : " + currentPath;

        String actualOutput = "";
        String line = "";
        do {
            line = promptMock.readLine();
            actualOutput += line + "\n";
        } while (!line.equalsIgnoreCase(Commands.PWD.getCommandText()));

        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void testCdCommand() throws IOException {
        String newPath = "/path/to/new";
        File newDirectory = new File(newPath);

        explorer.createFSPath();
        explorer.start();

        String line = "";
        String expectedOutput = "changed to : " + newPath;

        do {
            line = promptMock.readLine();
        } while (!line.equalsIgnoreCase(Commands.CD.getCommandText()));
        promptMock.readLine(); // read the argument

        explorer.configureExplorer(new String[]{"-p", "/path/to/root", "-f", "-1", "-c", "d", newPath});
        explorer.createFSPath();

        verify(fsPathFactoryMock, times(1)).newFSPath(newDirectory);
        verify(fsPathMock, times(1)).query(anyString());
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void testQueryNoResults() throws IOException {
        when(fsPathMock.query(anyString())).thenReturn(new FSPathResultList(new FSPathResult[0], null));

        explorer.createFSPath();
        explorer.start();

        String line = "";
        String expectedOutput = "";

        do {
            line = promptMock.readLine();
            expectedOutput += line + "\n";
        } while (!line.equalsIgnoreCase(Commands.PWD.getCommandText())
                && !line.equalsIgnoreCase(Commands.CD.getCommandText())
                && !line.equalsIgnoreCase(Commands.QUIT.getCommandText()));

        String actualOutput = "";
        do {
            actualOutput += promptMock.readLine();
        } while (!actualOutput.equalsIgnoreCase(Commands.QUIT.getCommandText()));

        assertEquals("", actualOutput);
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void testQueryWithResults() throws IOException {
        when(fsPathMock.query(anyString())).thenReturn(new FSPathResultList(new FSPathResult[]{new FSPathResult(new File("testPath1")), new FSPathResult(new File("testPath2"))}, null));

        explorer.createFSPath();
        explorer.start();

        String line = "";
        String expectedOutput = "testPath1\n";

        do {
            line = promptMock.readLine();
        } while (!line.equalsIgnoreCase(Commands.PWD.getCommandText())
                && !line.equalsIgnoreCase(Commands.CD.getCommandText())
                && !line.equalsIgnoreCase(Commands.QUIT.getCommandText()));

        String actualOutput = "";
        String currentResult = "";
        do {
            currentResult = promptMock.readLine();
            actualOutput += currentResult + "\n";
        } while (!currentResult.equalsIgnoreCase(Commands.QUIT.getCommandText()));

        assertEquals(expectedOutput, actualOutput);
    }
}