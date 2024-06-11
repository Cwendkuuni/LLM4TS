import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private File rootPath;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    public void setUp() {
        rootPath = new File("/test");
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        explorer.out = new PrintStream(outContent);
        explorer.err = new PrintStream(errContent);

        Mockito.spy(System.in);
    }

    @Test
    public void testEmptyQuery() {
        explorer.start();
        Assertions.assertEquals("", outContent.toString());
        Assertions.assertEquals("", errContent.toString());
    }

    @Test
    public void testBasicQuery() {
        explorer.fsPathQuery = "//dir[@name = 'test']";
        explorer.start();
        List<File> expected = Arrays.asList(new File("/test/test"), new File("/test/test/test"));
        assertFilesFound(expected);
    }

    @Test
    public void testFindFile() {
        explorer.fsPathQuery = "//file[@name = 'test.txt']";
        explorer.start();
        List<File> expected = Arrays.asList(new File("/test/test.txt"));
        assertFilesFound(expected);
    }

    @Test
    public void testFindDirectory() {
        explorer.fsPathQuery = "//dir[@name = 'test']";
        explorer.start();
        List<File> expected = Arrays.asList(new File("/test"), new File("/test/test"), new File("/test/test/test"));
        assertFilesFound(expected);
    }

    @Test
    public void testFindFileInDirectory() {
        explorer.fsPathQuery = "//dir[@name = 'test']/file[@name = 'test.txt']";
        explorer.start();
        List<File> expected = Arrays.asList(new File("/test/test.txt"));
        assertFilesFound(expected);
    }

    @Test
    public void testMultipleFilesWithSameName() {
        File testDir = new File("/test/test");
        testDir.mkdirs();
        File file1 = new File(testDir, "test.txt");
        File file2 = new File(testDir, "test.txt");
        explorer.rootPath = new File("/");
        explorer.fsPathQuery = "//file[@name = 'test.txt']";
        explorer.start();
        List<File> expected = Arrays.asList(file1, file2);
        assertFilesFound(expected);
    }

    @Test
    public void testEmptyDirectory() {
        explorer.fsPathQuery = "//dir[@name = 'test']/file";
        explorer.start();
        Assertions.assertEquals("", outContent.toString());
    }

    @Test
    public void testInvalidQuery() {
        explorer.fsPathQuery = "//file[@name = test.txt]";
        explorer.start();
        Assertions.assertEquals("", outContent.toString());
        Assertions.assertEquals("Query '//file[@name = test.txt]' is invalid\n", errContent.toString());
    }

    @Test
    public void testInvalidPath() {
        explorer.rootPath = null;
        explorer.start();
        Assertions.assertEquals("", outContent.toString());
        Assertions.assertEquals("FSPathExplorer: Unable to determine the current directory\n", errContent.toString());
    }

    @Test
    public void testInvalidPath2() {
        explorer.rootPath = null;
        explorer.fsPathQuery = "//dir[@name = 'test']";
        explorer.start();
        Assertions.assertEquals("", outContent.toString());
        Assertions.assertEquals("FSPathExplorer: Unable to determine the current directory\n", errContent.toString());
    }

    @Test
    public void testInvalidPath3() {
        explorer.rootPath = null;
        explorer.fsPathQuery = "//dir[@name = 'test']/file[@name = 'test.txt']";
        explorer.start();
        Assertions.assertEquals("", outContent.toString());
        Assertions.assertEquals("FSPathExplorer: Unable to determine the current directory\n", errContent.toString());
    }

    @Test
    public void testEmptyDirectory2() {
        explorer.rootPath = new File("/test");
        explorer.fsPathQuery = "//dir[@name = 'test']/file";
        explorer.start();
        Assertions.assertEquals("", outContent.toString());
    }

    @Test
    public void testQuitCommand() {
        explorer.fsPathQuery = Commands.EXIT.getCommandText();
        explorer.start();
    }

    @Test
    public void testHelpCommand() {
        explorer.fsPathQuery = Commands.HELP.getCommandText();
        explorer.start();
        String expectedHelp = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\n" +
                "Examples :\n\n" +
                "1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n" +
                "2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n" +
                "   with a file size greater than 10000 bytes\n" +
                "3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory structure which begin with a '.'\n\n" +
                "Attributes : (see javadoc for java.io.File for more info)\n";
        Assertions.assertEquals(expectedHelp, outContent.toString());
    }

    @Test
    public void testChangeDirectory() {
        explorer.fsPathQuery = "cd test";
        explorer.start();
        Assertions.assertEquals("/test", outContent.toString());
    }

    @Test
    public void testChangeDirectoryToNonExistentDirectory() {
        explorer.fsPathQuery = "cd nonExistent";
        explorer.start();
        Assertions.assertEquals("cd: directory nonExistent does not exist\n", errContent.toString());
    }

    @Test
    public void testChangeDirectoryToRoot() {
        explorer.fsPathQuery = "cd /";
        explorer.start();
        Assertions.assertEquals("/", outContent.toString());
    }

    @Test
    public void testChangeDirectoryToParent() {
        File testDir = new File("/test/test");
        testDir.mkdirs();
        explorer.rootPath = testDir;
        explorer.fsPathQuery = "cd ../";
        explorer.start();
        Assertions.assertEquals("/test", outContent.toString());
    }

    private void assertFilesFound(List<File> expected) {
        FSPathResultList results = Mockito.mock(FSPathResultList.class);
        Map<String, FSPathResult> resultsMap = new HashMap<>();
        for (File file : expected) {
            FSPathResult result = Mockito.mock(FSPathResult.class);
            Mockito.when(result.getFile()).thenReturn(file);
            resultsMap.put(file.getAbsolutePath(), result);
        }
        Mockito.when(results.getResultList()).thenReturn(resultsMap.values());
        Mockito.when(explorer.fsPath.query(Mockito.anyString())).thenReturn(results);

        explorer.start();
        Assertions.assertEquals(expected.size(), outContent.toString().split("\n").length);
    }

    @AfterEach
    public void tearDown() {
        if (rootPath != null && rootPath.exists()) {
            rootPath.deleteRecursively();
        }

        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}