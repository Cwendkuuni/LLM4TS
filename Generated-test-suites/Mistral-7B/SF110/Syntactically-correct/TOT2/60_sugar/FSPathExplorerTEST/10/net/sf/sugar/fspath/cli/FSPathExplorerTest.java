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

import org.junit.Before;
import org.junit.Test;

/**
 * FSPathExplorerTest.java
 *
 * Testing the functionality of FSPathExplorer class
 *
 * @author [Your Name]
 */

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private FSPath fsPathMock;
    private Prompt promptMock;
    private PrintStream outMock;
    private PrintStream errMock;

    @Before
    public void setUp() {
        this.explorer = new FSPathExplorer();
        this.fsPathMock = mock(FSPath.class);
        this.promptMock = mock(Prompt.class);
        this.outMock = mock(PrintStream.class);
        this.errMock = mock(PrintStream.class);
        this.explorer.out = this.outMock;
        this.explorer.err = this.errMock;
        this.explorer.prompt = this.promptMock;
    }

    @Test
    public void testConstructor() {
        assertEquals("default constructor should set out and err to system out and err respectively", System.out, explorer.out);
        assertEquals("default constructor should set out and err to system err respectively", System.err, explorer.err);
        assertNotNull("default constructor should initialize prompt", explorer.prompt);
    }

    @Test
    public void testMain() throws IOException {
        when(this.promptMock.readLine()).thenReturn("help");

        String[] args = new String[0];
        FSPathExplorer.main(args);

        verify(this.promptMock, times(1)).readLine();
        verify(this.promptMock, times(1)).close();
        verifyNoMoreInteractions(this.promptMock);
    }

    @Test
    public void testShowWelcome() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showWelcome();

        String expected = "+-------------------------------------------------------+\n" +
                "|                                                       |\n" +
                "| FSPathExplorer                                        |\n" +
                "|                                                       |\n" +
                "+-------------------------------------------------------+\n" +
                "Type 'help' for commands...\n" +
                "Scanning filesystem metadata...\n" +
                "+-------------------------------------------------------+\n" +
                "PLEASE NOTE running FSPathExplorer from the top level    \n" +
                "of your filesystem is not advised                       \n" +
                "+-------------------------------------------------------+";

        verify(this.outMock, times(1)).println(expected);
        verifyNoMoreInteractions(this.outMock);
    }

    @Test
    public void testShowHelp() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showHelp();

        String expected = "FSPathExplorer help :\n\n" +
                "Enter an FSPath query to search the filesystem\n\n" +
                "Examples :\n\n" +
                "1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n" +
                "2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n" +
                "   with a file size greater than 10000 bytes\n" +
                "3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\n" +
                "Attributes : (see javadoc for java.io.File for more info)\n" +
                "@size : gets the size of the file or directory\n" +
                "@name : gets the name of the file or directory\n" +
                "@type : gets the type of the file or directory\n" +
                "@exists : checks if the file or directory exists\n" +
                "@canRead : checks if the file or directory is readable\n" +
                "@canWrite : checks if the file or directory is writable\n" +
                "@canExecute : checks if the file or directory is executable\n" +
                "@modified : gets the last modified date of the file or directory\n" +
                "@isDirectory : checks if the file or directory is a directory\n" +
                "@isFile : checks if the file or directory is a file\n" +
                "@isHidden : checks if the file or directory is hidden\n" +
                "@isAbsolute : checks if the file or directory is absolute\n" +
                "\n\nCommands :\n";

        verify(this.outMock, times(1)).println(expected);
        verifyNoMoreInteractions(this.outMock);
    }

    @Test
    public void testConfigureExplorerWithValidPath() {
        String path = "/path/to/root";
        File rootPath = new File(path);

        when(this.promptMock.readLine()).thenReturn("path " + path);

        this.explorer.configureExplorer(new String[0]);

        verify(this.explorer.rootPath).equals(rootPath);
        verifyNoMoreInteractions(this.promptMock);
    }

    @Test
    public void testConfigureExplorerWithInvalidPath() {
        when(this.promptMock.readLine()).thenReturn("path nonExistentPath");

        this.explorer.configureExplorer(new String[0]);

        verify(this.promptMock, times(1)).println("unable to change to nonExistentPath please make sure that the directory exists and is readable.");
        verifyNoMoreInteractions(this.promptMock);
    }

    @Test
    public void testConfigureExplorerWithValidFindQuery() {
        String query = "find query";

        when(this.promptMock.readLine()).thenReturn("path /");
        when(this.promptMock.readLine()).thenReturn("find " + query);

        this.explorer.configureExplorer(new String[0]);

        verify(this.explorer.fsPathQuery).equals(query);
        verifyNoMoreInteractions(this.promptMock);
    }

    @Test
    public void testCreateFSPathWithValidPath() {
        File rootPath = new File("/path/to/root");

        this.explorer.configureExplorer(new String[]{"path", "/path/to/root"});
        this.explorer.createFSPath();

        verify(this.fsPathMock, times(1)).newFSPath(rootPath);
        verifyNoMoreInteractions(this.fsPathMock);
    }

    @Test
    public void testCreateFSPathWithoutPath() {
        this.explorer.configureExplorer(new String[0]);
        this.explorer.createFSPath();

        verify(this.fsPathMock, times(1)).newFSPath();
        verifyNoMoreInteractions(this.fsPathMock);
    }

    @Test
    public void testStartWithValidQuery() throws IOException {
        String query = "//dir[@name = 'foo']";
        List<FSPathResult> results = new FSPathResultList();
        results.add(new FSPathResult(new File("/path/to/foo")));

        when(this.fsPathMock.query(query)).thenReturn(results);

        this.explorer.configureExplorer(new String[]{"find", query});
        this.explorer.createFSPath();
        this.explorer.start();

        verify(this.promptMock, times(1)).readLine();
        verify(this.outMock, times(1)).println("/path/to/foo");
        verifyNoMoreInteractions(this.promptMock);
    }

    @Test
    public void testStartWithInvalidQuery() throws IOException {
        String query = "//dir[@name = 'foo']//file[size > 100000]";
        List<FSPathResult> results = new FSPathResultList();

        when(this.fsPathMock.query(query)).thenReturn(results);

        this.explorer.configureExplorer(new String[]{"find", query});
        this.explorer.createFSPath();
        this.explorer.start();

        verify(this.promptMock, times(1)).println("No results found for query " + query);
        verifyNoMoreInteractions(this.promptMock);
    }

    @Test(expected = IOException.class)
    public void testStartWithInvalidPath() throws IOException {
        when(this.fsPathMock.query(anyString())).thenThrow(new IOException());

        this.explorer.configureExplorer(new String[0]);
        this.explorer.createFSPath();
        this.explorer.start();
    }
}