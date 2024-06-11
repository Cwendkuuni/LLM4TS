package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

public class FSPathExplorerTest {

    private FSPathExplorer fsPathExplorer;
    private CommandLineParser commandLineParser;
    private HelpFormatter helpFormatter;
    private PrintStream out;

    @Before
    public void setUp() {
        this.out = mock(PrintStream.class);
        this.commandLineParser = new DefaultParser();
        this.helpFormatter = new HelpFormatter();
        this.fsPathExplorer = new FSPathExplorer();
    }

    @Test
    public void testStartWithEmptyCommandLine() {
        fsPathExplorer.start();
        verify(out, times(1)).println("Type 'help' for commands...");
    }

    @Test
    public void testStartWithHelpCommand() {
        when(out.println(anyString())).thenAnswer(i -> true);
        fsPathExplorer.start();
        fsPathExplorer.prompt.printPrompt();
        fsPathExplorer.prompt.readLine("help");
        fsPathExplorer.prompt.close();
        verify(out, times(1)).println("FSPathExplorer help :");
        verify(out, times(1)).println("Type 'exit' to quit.");
    }

    @Test
    public void testStartWithInvalidCommand() {
        when(out.println(anyString())).thenAnswer(i -> true);
        fsPathExplorer.start();
        fsPathExplorer.prompt.printPrompt();
        fsPathExplorer.prompt.readLine("invalidCommand");
        fsPathExplorer.prompt.close();
        verify(out, times(1)).println("Command 'invalidCommand' not found.");
    }

    @Test
    public void testStartWithPwdCommand() {
        when(out.println(anyString())).thenAnswer(i -> true);
        File rootDirectory = new File(".");
        fsPathExplorer.rootPath = rootDirectory;
        fsPathExplorer.start();
        fsPathExplorer.prompt.printPrompt();
        fsPathExplorer.prompt.readLine("pwd");
        fsPathExplorer.prompt.close();
        verify(out, times(1)).println("Current directory : " + rootDirectory.getAbsolutePath());
    }

    @Test
    public void testStartWithCdCommand() {
        when(out.println(anyString())).thenAnswer(i -> true);
        File newDir = new File("path/to/new/directory");
        fsPathExplorer.rootPath = new File(".");
        fsPathExplorer.start();
        fsPathExplorer.prompt.printPrompt();
        fsPathExplorer.prompt.readLine("cd path/to/new/directory");
        fsPathExplorer.prompt.close();
        File newRootDirectory = newDir;
        fsPathExplorer.rootPath = newRootDirectory;
        fsPathExplorer.createFSPath();
        verify(out, times(1)).println("changed to : " + newDir.getAbsolutePath());
    }

    @Test
    public void testStartWithValidFSPathQuery() throws IOException {
        when(out.println(anyString())).thenAnswer(i -> true);
        when(out.println(any(FSPathResult.class)).getFile().getPath()).thenAnswer(fsPathResult ->
                fsPathExplorer.fsPath.query(fsPathQuery(fsPathExplorer.fsPath, "//dir[@name = 'foo']")).get(0).getFile().getPath());
        fsPathExplorer.rootPath = new File(".");
        fsPathExplorer.start();
        fsPathExplorer.prompt.printPrompt();
        fsPathExplorer.prompt.readLine("//dir[@name = 'foo']");
        fsPathExplorer.prompt.close();
        verify(out, times(1)).println(fsPathExplorer.fsPath.query(fsPathQuery(fsPathExplorer.fsPath, "//dir[@name = 'foo']")).get(0).getFile().getPath());
    }

    @Test
    public void testStartWithInvalidFSPathQuery() throws IOException {
        when(out.println(anyString())).thenAnswer(i -> true);
        fsPathExplorer.rootPath = new File(".");
        fsPathExplorer.start();
        fsPathExplorer.prompt.printPrompt();
        fsPathExplorer.prompt.readLine("invalidFSPathQuery");
        fsPathExplorer.prompt.close();
        verify(out, times(1)).println("Error parsing query : invalidFSPathQuery");
    }

    @Test
    public void testConfigureExplorerWithValidArguments() {
        String[] args = {"-p", "/path/to/root", "-f", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        assertEquals("/path/to/root", fsPathExplorer.rootPath.getAbsolutePath());
        assertEquals("//dir[@name = 'foo']", fsPathExplorer.fsPathQuery);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithoutRootPathArgument() {
        String[] args = {"-f", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        fail("Illegal argument exception expected but not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithoutFSPathQueryArgument() {
        String[] args = {"-p", "/path/to/root"};
        fsPathExplorer.configureExplorer(args);
        fail("Illegal argument exception expected but not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithInvalidPathArgument() {
        String[] args = {"-p", "/path/to/root/invalid", "-f", "//dir[@name = 'foo']"};
        fsPathExplorer.configureExplorer(args);
        fail("Illegal argument exception expected but not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithInvalidFSPathQueryArgument() {
        String[] args = {"-p", "/path/to/root", "-f", "invalidFSPathQuery"};
        fsPathExplorer.configureExplorer(args);
        fail("Illegal argument exception expected but not thrown.");
    }

    @Test
    public void testCreateFSPathWithNullRootPath() {
        fsPathExplorer.rootPath = null;
        fsPathExplorer.createFSPath();
        assertNull(fsPathExplorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithValidRootPath() {
        File rootDirectory = new File(".");
        fsPathExplorer.rootPath = rootDirectory;
        fsPathExplorer.createFSPath();
        assertEquals(fsPathExplorer.rootPath, fsPathExplorer.fsPath.getRootDirectory());
    }

    @Test
    public void testQueryWithEmptyFSPathQuery() throws IOException {
        when(out.println(anyString())).thenAnswer(i -> true);
        String emptyFSPathQuery = "";
        fsPathExplorer.createFSPath();
        FSPathResultList results = fsPathExplorer.fsPath.query(emptyFSPathQuery);
        assertEquals(0, results.size());
    }

    @Test
    public void testQueryWithValidFSPathQuery() throws IOException {
        when(out.println(anyString())).thenAnswer(i -> true);
        String fsPathQueryString = "//dir[@name = 'foo']";
        fsPathExplorer.createFSPath();
        FSPathResultList results = fsPathExplorer.fsPath.query(fsPathQueryString);
        assertEquals(1, results.size());
        FSPathResult result = results.get(0);
        File expectedFile = new File("/path/to/expected/file");
        assertEquals(expectedFile, result.getFile());
    }

    @Test
    public void testQueryWithInvalidFSPathQuery() throws IOException {
        when(out.println(anyString())).thenAnswer(i -> true);
        String invalidFSPathQueryString = "invalidFSPathQuery";
        fsPathExplorer.createFSPath();
        FSPathResultList results = fsPathExplorer.fsPath.query(invalidFSPathQueryString);
        assertEquals(0, results.size());
    }

    private String fsPathQuery(FSPath fsPath, String fsPathQueryString) {
        return fsPathQueryString;
    }
}