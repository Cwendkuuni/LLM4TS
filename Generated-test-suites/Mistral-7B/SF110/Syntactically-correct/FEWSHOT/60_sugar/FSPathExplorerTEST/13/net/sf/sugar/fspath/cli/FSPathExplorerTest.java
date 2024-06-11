package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testCreateFSPathExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNull(explorer.fsPath);
        assertNull(explorer.prompt);
        assertNull(explorer.out);
        assertNull(explorer.err);
    }

    @Test
    public void testConfigureExplorerWithValidArguments() {
        String[] args = {"-p", "/test", "-f", "//dir[@name = 'test']"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        File rootPath = explorer.rootPath;
        assertNotNull(rootPath);
        assertEquals("/test", rootPath.getAbsolutePath());
        String fsPathQuery = explorer.fsPathQuery;
        assertEquals("//dir[@name = 'test']", fsPathQuery);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureExplorerWithInvalidArguments() {
        String[] args = {"-p", "/test", "-f"};
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
    }

    @Test
    public void testCreateFSPath() {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/test");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        FSPath fsPath = explorer.fsPath;
        assertNotNull(fsPath);
        assertEquals(rootPath, fsPath.getRootDirectory());
    }

    @Test
    public void testStartWithEmptyFSPathQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();
        String promptText = explorer.prompt.getPromptText();
        assertEquals("fspath>", promptText);
    }

    @Test
    public void testStartWithValidFSPathQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/test");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        explorer.fsPathQuery = "//dir[@name = 'test']//file[@name = 'test.txt']";
        explorer.start();
        String currentExpression = explorer.prompt.readLine();
        String[] expressionWords = currentExpression.split(" ");
        assertEquals("help", expressionWords[0]);
        List<String> results = Arrays.asList("/test/test/test.txt");
        FSPathResultList resultList = explorer.fsPath.query(explorer.fsPathQuery);
        List<String> expectedResults = new ArrayList<>();
        for (FSPathResult result : resultList) {
            expectedResults.add(result.getFile().getPath());
        }
        for (int i = 0; i < results.size(); i++) {
            assertEquals(results.get(i), expectedResults.get(i));
        }
    }

    @Test
    public void testStartWithInvalidFSPathQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/test");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        explorer.fsPathQuery = "//dir[@name = 'test']//file[@name = abc]";
        explorer.start();
        String promptText = explorer.prompt.getPromptText();
        assertEquals("fspath>", promptText);
    }

    @Test
    public void testCDCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/test");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        explorer.start();
        String currentExpression = explorer.prompt.readLine();
        String[] expressionWords = currentExpression.split(" ");
        assertEquals("cd", expressionWords[0]);
        String newDirectoryPath = "/test/subdir";
        explorer.prompt.write(newDirectoryPath + "\n");
        String newDirectoryPathResult = explorer.prompt.readLine();
        assertEquals("changed to : " + newDirectoryPath, newDirectoryPathResult);
        String newDirectoryPathAbsolute = new File(newDirectoryPath).getAbsolutePath();
        assertEquals(newDirectoryPathAbsolute, explorer.fsPath.getRootDirectory().getAbsolutePath());
    }

    @Test
    public void testPWDCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/test");
        explorer.rootPath = rootPath;
        explorer.createFSPath();
        explorer.start();
        String currentExpression = explorer.prompt.readLine();
        String[] expressionWords = currentExpression.split(" ");
        assertEquals("pwd", expressionWords[0]);
        String currentDirectoryPath = explorer.fsPath.getRootDirectory().getAbsolutePath();
        String currentDirectoryPathResult = explorer.prompt.readLine();
        assertEquals("Current directory : " + currentDirectoryPath, currentDirectoryPathResult);
    }
}