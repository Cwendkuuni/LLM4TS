import static org.junit.Assert.*;
import static net.sf.sugar.fspath.cli.Commands.*;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * FSPathExplorerTest class
 */
public class FSPathExplorerTest {

    @Test
    public void testConstructor() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);
    }

    @Test
    public void testMain() throws IOException {
        String[] args = new String[]{"-path", "/tmp"};
        FSPathExplorer.main(args);
        // Add assertions for the expected behavior of the main method
    }

    @Test
    public void testShowWelcome() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showWelcome();
        // Add assertions for the expected output of the showWelcome method
    }

    @Test
    public void testShowHelp() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.showHelp();
        // Add assertions for the expected output of the showHelp method
    }

    @Test
    public void testConfigureExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-path", "/tmp", "-find", ".txt"};
        explorer.configureExplorer(args);
        assertEquals("/tmp", explorer.rootPath.getAbsolutePath());
        assertEquals(".txt", explorer.fsPathQuery);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerMissingPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-find", ".txt"};
        explorer.configureExplorer(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerMissingQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-path", "/tmp"};
        explorer.configureExplorer(args);
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.createFSPath();
        assertEquals(explorer.fsPath.getRootDirectory(), rootPath);

        explorer = new FSPathExplorer();
        explorer.createFSPath();
        assertEquals(explorer.fsPath.getRootDirectory(), new File("."));
    }

    @Test(expected = IllegalStateException.class)
    public void testStartNoQuery() {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();
        explorer.start();
    }

    @Test
    public void testStartWithQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-path", "/tmp", "-find", "*.txt"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.start();
        // Add assertions for the expected output of the start method
    }

    @Test
    public void testQueryEmpty() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-path", "/tmp", "-find", ""};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(0, results.size());
    }

    @Test
    public void testQuerySingleResult() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-path", "/tmp", "-find", "test.txt"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(1, results.size());
        File expectedFile = new File("/tmp/test.txt");
        assertEquals(expectedFile, results.get(0).getFile());
    }

    @Test
    public void testQueryMultipleResults() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-path", "/tmp", "-find", "*.txt"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        FSPathResultList results = explorer.fsPath.query(explorer.fsPathQuery);
        assertEquals(2, results.size());
        File file1 = new File("/tmp/file1.txt");
        File file2 = new File("/tmp/file2.txt");
        assertTrue(results.contains(new FSPathResult(file1)));
        assertTrue(results.contains(new FSPathResult(file2)));
    }

    @Test(expected = IOException.class)
    public void testQueryInvalidPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        String[] args = new String[]{"-path", "/nonexistent", "-find", "*.txt"};
        explorer.configureExplorer(args);
        explorer.createFSPath();
        explorer.fsPath.query(explorer.fsPathQuery);
    }
}