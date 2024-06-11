package net.sf.sugar.fspath.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FSPathExplorerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final String rootPath = "src/test/resources";
    private FSPathExplorer explorer;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-path", rootPath});
        explorer.createFSPath();
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void testCreateFSPath() {
        assertNotNull(explorer.getFSPath());
    }

    @Test
    public void testStart_PWD() {
        explorer.start();
        String expected = "Current directory : " + rootPath + "\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testStart_CD() throws IOException {
        explorer.start();
        explorer.prompt.sendLine("cd test");
        String expected = "changed to : " + (new File(rootPath, "test")).getAbsolutePath() + "\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testStart_Help() {
        explorer.start();
        explorer.prompt.sendLine("help");

        String expected = "FSPathExplorer help :\n\n" +
                "Enter an FSPath query to search the filesystem\n\n" +
                "Examples :\n\n" +
                "1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n" +
                "2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n" +
                "   with a file size greater than 10000 bytes\n\n" +
                "Attributes : (see javadoc for java.io.File for more info)\n";

        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testStart_Query() throws IOException {
        explorer.start();
        explorer.prompt.sendLine("find //file[@name = 'TestFile.txt']");

        List<FSPathResult> results = explorer.getFSPath().query(explorer.getFSPathQuery());
        assertEquals(1, results.size());
        File expected = new File(rootPath, "test/TestFile.txt");
        assertEquals(expected.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
    }

    public FSPath getFSPath() {
        return explorer.getFSPath();
    }

    public String getFSPathQuery() {
        return explorer.getFSPathQuery();
    }
}