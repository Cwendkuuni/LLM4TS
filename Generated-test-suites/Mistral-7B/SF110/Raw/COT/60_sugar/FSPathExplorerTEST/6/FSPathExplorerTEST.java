import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    @Test
    public void testMain() {
        String[] args = {"-p", "/tmp"};
        FSPathExplorer.main(args);
        // This test is not comprehensive as it can't simulate user input
        // For a more thorough test, consider mocking the System.in and other components
    }

    @Test
    public void testShowWelcomeMessage() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showWelcome();
    }

    @Test
    public void testShowHelpMessage() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showHelp();
    }

    @Test
    public void testConfigureExplorer_ValidPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        String[] args = {"-p", rootPath.getAbsolutePath()};
        explorer.configureExplorer(args);
        assertEquals(rootPath, explorer.rootPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorer_InvalidRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-p", ""};
        explorer.configureExplorer(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorer_MissingPathArgument() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = {"-p"};
        explorer.configureExplorer(args);
    }

    @Test
    public void testCreateFSPath_NullRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPath_ValidRootPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        assertEquals(explorer.rootPath, explorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStart_NoQueries() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
    }

    @Test
    public void testStart_ValidQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "dir[@name = 'test']";
        explorer.fsPathQuery = query;
        explorer.start();
        List<FSPathResult> results = (List<FSPathResult>) explorer.fsPath.query(explorer.fsPathQuery);
        for (FSPathResult result : results) {
            String resultPath = result.getFile().getPath();
            assertTrue(resultPath.contains("test"));
        }
    }

    @Test
    public void testStart_InvalidQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "invalidquery";
        explorer.fsPathQuery = query;
        explorer.start();
        List<FSPathResult> results = (List<FSPathResult>) explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(0, results.size());
    }

    @Test(expected = IOException.class)
    public void testStart_IOException() {
        FSPathExplorer explorer = new FSPathExplorer();
        String query = "//dir[@name = 'root']";
        explorer.fsPathQuery = query;
        explorer.start();
    }

    @Test
    public void testPrompt_ReadLine() {
        FSPathExplorer explorer = new FSPathExplorer();
        Prompt prompt = new Prompt(System.in, explorer.out, explorer.err);
        String line = "ls";
        assertEquals(line, prompt.readLine());
    }

    @Test
    public void testCommands() {
        assertEquals(Commands.CD, Commands.getCommand("cd"));
        assertEquals(Commands.EXIT, Commands.getCommand("exit"));
        assertEquals(Commands.HELP, Commands.getCommand("help"));
        assertEquals(Commands.PWD, Commands.getCommand("pwd"));
    }
}