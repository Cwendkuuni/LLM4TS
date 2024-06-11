package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

/**
 * Test class for FSPathExplorer.
 */
public class FSPathExplorerTest {

    /**
     * Test of main method, of class FSPathExplorer.
     */
    @Test
    public void testMain() throws IOException {
        File testDirectory = new File(System.getProperty("user.dir"));
        String[] args = new String[]{"-p", testDirectory.getAbsolutePath(), "-f", "//dir[@name = 'test']"};
        FSPathExplorer.main(args);
    }

    /**
     * Test of configureExplorer method, of class FSPathExplorer.
     */
    @Test
    public void testConfigureExplorer() {
        FSPathExplorer instance = new FSPathExplorer();
        String[] args = new String[]{"-p", "."};
        instance.configureExplorer(args);
        File expectedRootPath = new File(".");
        File actualRootPath = instance.rootPath;
        assertEquals(expectedRootPath, actualRootPath);
    }

    /**
     * Test of createFSPath method, of class FSPathExplorer.
     */
    @Test
    public void testCreateFSPath() {
        FSPathExplorer instance = new FSPathExplorer();
        instance.createFSPath();
        FSPath expectedFSPath = FSPathFactory.newFSPath();
        FSPath actualFSPath = instance.fsPath;
        assertEquals(expectedFSPath, actualFSPath);
    }

    /**
     * Test of start method, of class FSPathExplorer.
     * @throws IOException
     */
    @Test
    public void testStart() throws IOException {
        FSPathExplorer instance = new FSPathExplorer();
        String expectedResult = "Current directory : .";
        File testDirectory = new File(System.getProperty("user.dir"));
        String[] args = new String[]{"-p", testDirectory.getAbsolutePath(), "-f", "PWD"};
        instance.configureExplorer(args);
        instance.createFSPath();
        instance.start();
        String actualResult = instance.prompt.readLine();
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test of query method, of class FSPath.
     * @throws IOException
     */
    @Test
    public void testQuery() throws IOException {
        FSPathExplorer instance = new FSPathExplorer();
        String[] args = new String[]{"-p", ".", "-f", "//file[@name = 'testfile.txt']"};
        instance.configureExplorer(args);
        instance.createFSPath();
        FSPathResultList expectedResults = new FSPathResultList();
        expectedResults.add(new FSPathResult(new File("testfile.txt")));
        FSPathResultList actualResults = instance.fsPath.query(instance.fsPathQuery);
        assertEquals(expectedResults, actualResults);
    }

    /**
     * Test of query method with empty query, of class FSPath.
     * @throws IOException
     */
    @Test
    public void testQueryEmptyQuery() throws IOException {
        FSPathExplorer instance = new FSPathExplorer();
        String[] args = new String[]{"-p", ".", "-f", ""};
        instance.configureExplorer(args);
        instance.createFSPath();
        FSPathResultList expectedResults = new FSPathResultList();
        FSPathResultList actualResults = instance.fsPath.query(instance.fsPathQuery);
        assertEquals(expectedResults, actualResults);
    }
}