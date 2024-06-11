package net.sf.sugar.fspath.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FSPathExplorerTest {

    private final String workingDirectory;
    private final ByteArrayOutputStream outContent;
    private final ByteArrayOutputStream errContent;

    public FSPathExplorerTest() {
        workingDirectory = System.getProperty("user.dir");
    }

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void testMain_NoArgs() {
        String[] args = new String[0];
        runMain(args);
        assertEquals("", outContent.toString());
        assertEquals("", errContent.toString());
    }

    @Test
    public void testMain_InvalidArgs() {
        String[] args = new String[]{"invalidCommand"};
        runMain(args);
        assertTrue(errContent.toString().contains("Command 'invalidCommand' is not recognized."));
        assertEquals("Scanning filesystem metadata...\n", outContent.toString());
    }

    @Test
    public void testMain_HelpCommand() {
        String[] args = new String[]{"--help"};
        runMain(args);
        String expectedHelp = "FSPathExplorer help :\n\n" +
                "Enter an FSPath query to search the filesystem\n\n" +
                "Examples :\n\n" +
                "1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n" +
                "2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n" +
                "   with a file size greater than 10000 bytes\n" +
                "3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\n" +
                "Attributes : (see javadoc for java.io.File for more info)\n" +
                "@size size() : the size of the file or directory in bytes\n" +
                "@lastModified lastModified() : the last modified time of the file or directory\n" +
                "@name name() : the name of the file or directory\n" +
                "@length length() : the length of the file or the number of bytes in the directory\n" +
                "@isDirectory isDirectory() : true if the object is a directory, false otherwise\n" +
                "@isFile isFile() : true if the object is a file, false otherwise\n" +
                "@hidden hidden() : true if the file is hidden, false otherwise\n" +
                "@readable readable() : true if the file is readable, false otherwise\n" +
                "@writable writable() : true if the file is writable, false otherwise\n" +
                "@executable executable() : true if the file is executable, false otherwise\n" +
                "@canRead canRead() : true if the user has read access, false otherwise\n" +
                "@canWrite canWrite() : true if the user has write access, false otherwise\n" +
                "@canExecute canExecute() : true if the user has execute access, false otherwise\n" +
                "@extension extension() : the file extension without the leading dot\n" +
                "@path path() : the file or directory path\n\n" +
                "Commands :\n";
        assertEquals(expectedHelp, outContent.toString());
        assertEquals("", errContent.toString());
    }

    @Test
    public void testMain_PWDCommand() {
        String[] args = new String[]{"pwd"};
        runMain(args);
        String expected = "Current directory : " + workingDirectory + "\n";
        assertEquals(expected, outContent.toString());
        assertEquals("Scanning filesystem metadata...\n", errContent.toString());
    }

    @Test
    public void testMain_CDCommand() {
        Path tempDirPath = null;
        try {
            tempDirPath = Files.createTempDirectory("fsPathExplorerTest", null);
            String[] args = new String[]{"cd", tempDirPath.toString()};
            runMain(args);
            assertEquals("changed to : " + tempDirPath, outContent.toString());
            assertEquals("Scanning filesystem metadata...\n", errContent.toString());
        } catch (IOException e) {
            fail("Failed to create temporary directory: " + e.getMessage());
        } finally {
            if (tempDirPath != null) {
                try {
                    Files.deleteRecursively(tempDirPath);
                } catch (IOException e) {
                    fail("Failed to delete temporary directory: " + e.getMessage());
                }
            }
        }
    }

    @Test
    public void testMain_InvalidCDCommand() {
        String[] args = new String[]{"cd", "/nonexistent"};
        runMain(args);
        assertTrue(errContent.toString().contains("unable to change to /nonexistent"));
        assertEquals("Scanning filesystem metadata...\n", outContent.toString());
    }

    @Test
    public void testMain_FindCommandEmptyPathQuery() {
        String[] args = new String[]{"find", ""};
        runMain(args);
        assertEquals("", outContent.toString());
        assertEquals("Scanning filesystem metadata...\n", errContent.toString());
    }

    @Test
    public void testMain_FindCommandSimplePathQuery() {
        String[] args = new String[]{"find", "test.txt"};
        runMain(args);
        List<String> expected = Collections.singletonList(workingDirectory + "/test.txt");
        verifyResults(expected);
    }

    @Test
    public void testMain_FindCommandComplexPathQuery() {
        String[] args = new String[]{"find", "//file[@name = 'test.txt']"};
        runMain(args);
        List<String> expected = Collections.singletonList(workingDirectory + "/test.txt");
        verifyResults(expected);
    }

    @Test
    public void testMain_FindCommandComplexPathQueryWithHiddenFile() {
        String[] args = new String[]{"find", "//file[@hidden = 'true' and @name = '.hidden-file']"};
        runMain(args);
        List<String> expected = Collections.singletonList(workingDirectory + "/.hidden-file");
        verifyResults(expected);
    }

    @Test
    public void testMain_FindCommandComplexPathQueryWithMultipleFiles() {
        String[] args = new String[]{"find", "//file[@extension = 'txt']"};
        runMain(args);
        List<String> expected = Arrays.asList(
                workingDirectory + "/test.txt",
                workingDirectory + "/test2.txt",
                workingDirectory + "/hidden-test.txt"
        );
        verifyResults(expected);
    }

    @Test
    public void testMain_FindCommandComplexPathQueryWithParentDirectory() {
        Path tempDirPath = null;
        try {
            tempDirPath = Files.createTempDirectory("fsPathExplorerTest", null);
            String[] args = new String[]{"find", "//dir[@name = 'temp']/file[@extension = 'txt']"};
            runMain(args);
            List<String> expected = Arrays.asList(
                    tempDirPath.toString() + "/test.txt",
                    workingDirectory + "/test.txt"
            );
            verifyResults(expected);
        } catch (IOException e) {
            fail("Failed to create temporary directory: " + e.getMessage());
        } finally {
            if (tempDirPath != null) {
                try {
                    Files.deleteRecursively(tempDirPath);
                } catch (IOException e) {
                    fail("Failed to delete temporary directory: " + e.getMessage());
                }
            }
        }
    }

    @Test
    public void testMain_FindCommandComplexPathQueryWithParentDirectoryAndHiddenFile() {
        Path tempDirPath = null;
        try {
            tempDirPath = Files.createTempDirectory("fsPathExplorerTest", null);
            String[] args = new String[]{"find", "//dir[@name = 'temp']/file[@hidden = 'true' and @extension = 'txt']"};
            runMain(args);
            List<String> expected = Arrays.asList(
                    tempDirPath.toString() + "/hidden-test.txt"
            );
            verifyResults(expected);
        } catch (IOException e) {
            fail("Failed to create temporary directory: " + e.getMessage());
        } finally {
            if (tempDirPath != null) {
                try {
                    Files.deleteRecursively(tempDirPath);
                } catch (IOException e) {
                    fail("Failed to delete temporary directory: " + e.getMessage());
                }
            }
        }
    }

    @Test
    public void testMain_FindCommandWithInvalidPathQuery() {
        String[] args = new String[]{"find", "invalidQuery"};
        runMain(args);
        assertTrue(errContent.toString().contains("Error parsing FSPath query"));
        assertEquals("Scanning filesystem metadata...\n", outContent.toString());
    }

    private void runMain(String[] args) {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
    }

    private void verifyResults(List<String> expectedFiles) {
        List<String> resultFiles = getResultsAsList();
        assertEquals(expectedFiles.size(), resultFiles.size());
        for (int i = 0; i < expectedFiles.size(); i++) {
            assertEquals(expectedFiles.get(i), resultFiles.get(i));
        }
    }

    private List<String> getResultsAsList() {
        String[] lines = outContent.toString().split("\n");
        return Arrays.asList(Arrays.copyOfRange(lines, 0, lines.length - 1));
    }
}