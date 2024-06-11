package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for FSPathExplorer.
 */
public class FSPathExplorerTest {

    // Instance under test
    private FSPathExplorer explorer;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
    }

    // -------------------------------------------------- Test methods for constructor --------------------------------------------------

    @Test
    public void testFSPathExplorerConstructor() {
        // Given

        // When
        FSPathExplorer explorer = new FSPathExplorer();

        // Then
        assertNull(explorer.rootPath);
        assertNull(explorer.fsPath);
        assertNull(explorer.prompt);
        assertNull(explorer.out);
        assertNull(explorer.err);
    }

    // -------------------------------------------------- Test methods for configureExplorer --------------------------------------------------

    @Test
    public void testConfigureExplorerWithValidPathAndQuery() {
        // Given
        String[] args = new String[]{"-path", "/root", "-find", "//file[name = 'test.txt']"};
        FSPathExplorer explorer = new FSPathExplorer();

        // When
        explorer.configureExplorer(args);

        // Then
        File expectedRootPath = new File("/root");
        assertEquals(expectedRootPath, explorer.rootPath);
        assertEquals("//file[name = 'test.txt']", explorer.fsPathQuery);
        assertNull(explorer.fsPath);
        assertNull(explorer.prompt);
        assertNull(explorer.out);
        assertNull(explorer.err);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testConfigureExplorerWithInvalidArgsNumber() {
        // Given
        String[] args = new String[]{"-path", "/root"};

        // When
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);

        // Then (expected failure)
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testConfigureExplorerWithInvalidPathIndex() {
        // Given
        String[] args = new String[]{"-path", "wrong_path", "-find", "//file[name = 'test.txt']"};

        // When
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);

        // Then (expected failure)
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testConfigureExplorerWithInvalidFindIndex() {
        // Given
        String[] args = new String[]{"-path", "/root", "-find", "//file[name = 'test.txt']", "wrong_arg"};

        // When
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);

        // Then (expected failure)
    }

    // -------------------------------------------------- Test methods for createFSPath --------------------------------------------------

    @Test
    public void testCreateFSPathWithNullRootPath() {
        // Given
        FSPathExplorer explorer = new FSPathExplorer();

        // When
        explorer.createFSPath();

        // Then
        assertNull(explorer.fsPath);
    }

    @Test
    public void testCreateFSPathWithValidRootPath() {
        // Given
        File rootPath = new File("/root");
        FSPathExplorer explorer = new FSPathExplorer();

        // When
        explorer.rootPath = rootPath;
        explorer.createFSPath();

        // Then
        assertEquals(rootPath, explorer.fsPath.getRootDirectory());
        assertNull(explorer.prompt);
        assertNull(explorer.out);
        assertNull(explorer.err);
    }

    // -------------------------------------------------- Test methods for start --------------------------------------------------

    @Test(expected = IOException.class)
    public void testStartWithNullFSPath() {
        // Given
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();

        // When
        explorer.start();

        // Then (expected failure)
    }

    @Test
    public void testStartWithValidFSPathAndQuery() throws IOException {
        // Given
        File rootPath = new File("/root");
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        String query = "//file[name = 'test.txt']";

        // When
        explorer.fsPathQuery = query;
        explorer.start();

        // Then
        String expectedResult = "/root/test.txt";
        String result = explorer.prompt.readLine();
        assertEquals(expectedResult, result);
    }

    // -------------------------------------------------- Test methods for FSPathExplorer methods (other than main) --------------------------------------------------

    @Test
    public void testQueryWithValidFSPathAndQuery() throws IOException {
        // Given
        File rootPath = new File("/root");
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        explorer.createFSPath();

        // When
        String query = "//file[name = 'test.txt']";
        FSPathResultList results = explorer.fsPath.query(query);

        // Then
        assertEquals(1, results.size());
        File expectedFile = new File("/root/test.txt");
        File resultFile = results.get(0).getFile();
        assertEquals(expectedFile, resultFile);
        assertEquals("test.txt", resultFile.getName());
        assertEquals("/root/test.txt", resultFile.getPath());
    }

    @Test
    public void testQueryWithEmptyQuery() throws IOException {
        // Given
        File rootPath = new File("/root");
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        explorer.createFSPath();

        // When
        String query = "";
        FSPathResultList results = explorer.fsPath.query(query);

        // Then
        assertEquals(0, results.size());
    }

    @Test
    public void testQueryWithInvalidQuery() throws IOException {
        // Given
        File rootPath = new File("/root");
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.rootPath = rootPath;
        explorer.createFSPath();

        // When
        String query = "//dir[name = 'test']//file[name = 'test.txt']//@length > 100";
        FSPathResultList results = explorer.fsPath.query(query);

        // Then
        assertEquals(0, results.size());
    }

    @Test(expected = IOException.class)
    public void testQueryWithNullFSPath() throws IOException {
        // Given
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.createFSPath();

        // When
        String query = "//file[name = 'test.txt']";
        FSPathResultList results = explorer.fsPath.query(query);

        // Then (expected failure)
    }
}