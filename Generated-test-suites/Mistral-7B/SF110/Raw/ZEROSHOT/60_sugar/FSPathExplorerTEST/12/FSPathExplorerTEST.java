package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

import org.junit.Before;
import org.junit.Test;

/**
 * This class contains the unit tests for FSPathExplorer class
 *
 * @author YourName
 */
public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private FSPath fsPathMock;
    private File rootPathMock;
    private PrintStream outMock;
    private Prompt promptMock;

    @Before
    public void setUp() {
        rootPathMock = mock(File.class);
        when(rootPathMock.exists()).thenReturn(true);
        when(rootPathMock.isDirectory()).thenReturn(true);
        when(rootPathMock.getAbsolutePath()).thenReturn("/test/root");

        fsPathMock = mock(FSPath.class);
        when(fsPathMock.query(anyString())).thenReturn(new FSPathResultList(Arrays.asList(new FSPathResult(new File("/test/test1")))));

        outMock = mock(PrintStream.class);
        promptMock = mock(Prompt.class);

        explorer = new FSPathExplorer();
        explorer.setRootPath(rootPathMock);
        explorer.setFsPath(fsPathMock);
        explorer.setOut(outMock);
        explorer.setPrompt(promptMock);
    }

    @Test
    public void testQuery() {
        when(promptMock.readLine()).thenReturn("//dir[@name = 'test']");
        explorer.start();

        verify(fsPathMock).query("//dir[@name = 'test']");
    }

    @Test
    public void testCdCommand() {
        when(promptMock.readLine()).thenReturn("cd test2");

        File test2Mock = mock(File.class);
        when(test2Mock.exists()).thenReturn(true);
        when(test2Mock.isDirectory()).thenReturn(true);
        when(test2Mock.getAbsolutePath()).thenReturn("/test/test2");

        explorer.setRootPath(rootPathMock);
        explorer.setFsPath(fsPathMock);
        explorer.setOut(outMock);
        explorer.setPrompt(promptMock);

        explorer.createFSPath();
        explorer.configureExplorer(new String[] {"-p", "/test/root"});

        when(rootPathMock.getParentFile()).thenReturn(test2Mock);

        explorer.start();

        verify(fsPathMock).query("//dir[@name = 'test2']");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCommand() {
        when(promptMock.readLine()).thenReturn("invalidCommand");

        explorer.setRootPath(rootPathMock);
        explorer.setFsPath(fsPathMock);
        explorer.setOut(outMock);
        explorer.setPrompt(promptMock);

        explorer.start();

        verifyNoMoreInteractions(fsPathMock);

        verify(outMock).println("unsupported command : invalidCommand");
    }

    @Test
    public void testPwdCommand() {
        when(promptMock.readLine()).thenReturn("pwd");

        explorer.setRootPath(rootPathMock);
        explorer.setFsPath(fsPathMock);
        explorer.setOut(outMock);
        explorer.setPrompt(promptMock);

        explorer.start();

        verify(outMock).println("Current directory : /test/root");
    }

    @Test(expected = IOException.class)
    public void testQueryException() throws IOException {
        when(promptMock.readLine()).thenReturn("//dir[@name = 'test']");
        when(fsPathMock.query("//dir[@name = 'test']")).thenThrow(new IOException());

        explorer.setRootPath(rootPathMock);
        explorer.setFsPath(fsPathMock);
        explorer.setOut(outMock);
        explorer.setPrompt(promptMock);

        explorer.start();

        verifyNoMoreInteractions(fsPathMock);

        verify(outMock).println("Error querying filesystem : ");
    }

    @Test
    public void testHelpCommand() {
        when(promptMock.readLine()).thenReturn("help");

        explorer.setRootPath(rootPathMock);
        explorer.setFsPath(fsPathMock);
        explorer.setOut(outMock);
        explorer.setPrompt(promptMock);

        explorer.start();

        String expectedHelp = "FSPathExplorer help :\n\n" +
            "Enter an FSPath query to search the filesystem\n\n" +
            "Examples :\n\n" +
            "1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n" +
            "2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n" +
            "   with a file size greater than 10000 bytes\n\n" +
            "3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\n" +
            "Attributes : (see javadoc for java.io.File for more info)\n" +
            "@\n" +
            "@size\n" +
            "@hidden\n" +
            "@isDirectory\n" +
            "@isFile\n" +
            "@isSymbolicLink\n" +
            "@lastModified\n" +
            "@length\n" +
            "@name\n" +
            "@parent\n" +
            "@canRead\n" +
            "@canWrite\n" +
            "@canExecute\n" +
            "@readable\n" +
            "@writable\n" +
            "@executable\n" +
            "@file\n" +
            "@directory\n" +
            "@link\n" +
            "@device\n" +
            "@blockSize\n" +
            "@freeSpace\n" +
            "@totalSpace\n" +
            "@usableSpace\n" +
            "@path\n" +
            "@absolutePath\n" +
            "@parentFile\n" +
            "@getParentFile\n" +
            "@getAbsolutePath\n" +
            "@getName\n" +
            "@length\n" +
            "@isDirectory\n" +
            "@isFile\n" +
            "@isHidden\n" +
            "@isReadable\n" +
            "@isWritable\n" +
            "@isFile\n" +
            "@getParent\n" +
            "@getParentFile\n" +
            "@mkdirs\n" +
            "@exists\n" +
            "@delete\n" +
            "@mkdir\n" +
            "@renameTo\n" +
            "@toFile\n" +
            "@listFiles\n" +
            "@listFilesRecursively\n" +
            "@getAbsolutePath\n" +
            "@getParentFile\n" +
            "@isDirectory\n" +
            "@isFile\n" +
            "@length\n" +
            "@lastModified\n" +
            "@getName\n" +
            "@setLastModified\n" +
            "@getBytes\n" +
            "@getParent\n" +
            "@getParentFile\n" +
            "@deleteOnExit\n" +
            "@getCanExecute\n" +
            "@setCanExecute\n" +
            "@setReadable\n" +
            "@setWritable\n" +
            "@setExecutable\n" +
            "@toFile\n" +
            "@setName\n" +
            "@setPath\n";

        explorer.start();

        String actualHelp = verifyPrintedHelp(outMock);

        assertEquals(expectedHelp, actualHelp);
    }

    private String verifyPrintedHelp(PrintStream out) {
        StringBuilder sb = new StringBuilder();
        while (nextLineToAppend(sb, out)) {}
        return sb.toString();
    }

    private boolean nextLineToAppend(StringBuilder sb, PrintStream out) {
        String line = (String) out.readLine();
        if (line == null) {
            return false;
        }
        sb.append(line).append("\n");
        return true;
    }
}

enum Commands {
    CD("cd <directory_name>"),
    EXIT("exit"),
    HELP("help"),
    PWD("pwd");

    private String commandText;

    Commands(String commandText) {
        this.commandText = commandText;
    }

    public String getCommandText() {
        return commandText;
    }

    public static boolean isCommand(String command) {
        for (Commands cmd : Commands.values()) {
            if (cmd.getCommandText().equalsIgnoreCase(command)) {
                return true;
            }
        }
        return false;
    }
}