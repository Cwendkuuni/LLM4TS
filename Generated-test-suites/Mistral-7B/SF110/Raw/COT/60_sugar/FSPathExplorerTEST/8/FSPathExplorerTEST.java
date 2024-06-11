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
 * Test class for FSPathExplorer
 */
public class FSPathExplorerTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        assertNotNull(fsPathExplorer);
    }

    /**
     * Test main method with valid arguments
     */
    @Test
    public void testMainWithValidArguments() throws IOException, InterruptedException {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();

        String[] args = new String[]{"-path", "/tmp", "-find", "*.txt"};
        FSPathExplorer.main(args);

        // Assuming the test environment is running on a system with a '/tmp' directory
        // and that the '/tmp' directory contains some text files
        String expectedOutput = "Scanning filesystem metadata...\nType 'help' for commands...\nChanged to /tmp\n";
        String actualOutput = new String(System.out.array());
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test main method with invalid arguments
     */
    @Test(expected = RuntimeException.class)
    public void testMainWithInvalidArguments() {
        String[] args = new String[]{"-path", "invalid_path"};
        FSPathExplorer.main(args);
    }

    /**
     * Test configureExplorer method with valid arguments
     */
    @Test
    public void testConfigureExplorerWithValidArguments() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        String[] args = new String[]{"-path", "/tmp", "-find", "*.txt"};

        fsPathExplorer.configureExplorer(args);
        File expectedRootPath = new File("/tmp");
        assertEquals(expectedRootPath, fsPathExplorer.rootPath);
        String expectedQuery = "*" + File.pathSeparator + "*.txt";
        assertEquals(expectedQuery, fsPathExplorer.fsPathQuery);
    }

    /**
     * Test configureExplorer method with missing arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithMissingArguments() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        String[] args = new String[]{"-find", "*.txt"};
        fsPathExplorer.configureExplorer(args);
    }

    /**
     * Test createFSPath method
     */
    @Test
    public void testCreateFSPath() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        assertNotNull(fsPathExplorer.fsPath);

        File rootPath = new File("/");
        fsPathExplorer.createFSPath();
        assertEquals(rootPath, fsPathExplorer.fsPath.getRootDirectory());
    }

    /**
     * Test start method with valid arguments
     */
    @Test
    public void testStartWithValidArguments() throws IOException {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();

        String[] args = new String[]{"-path", "/tmp", "-find", Commands.PWD.getCommandText()};
        fsPathExplorer.configureExplorer(args);
        fsPathExplorer.createFSPath();
        fsPathExplorer.start();

        String expectedOutput = "Current directory : /tmp\n";
        String actualOutput = new String(System.out.array());
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test start method with invalid arguments
     */
    @Test(expected = IOException.class)
    public void testStartWithInvalidArguments() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.start();
    }
}