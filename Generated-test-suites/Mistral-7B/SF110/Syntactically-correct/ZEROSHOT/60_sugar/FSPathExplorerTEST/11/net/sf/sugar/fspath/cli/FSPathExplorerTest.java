package net.sf.sugar.fspath.cli;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FSPathExplorerTest {

    private FSPathExplorer fsPathExplorer;
    private File rootPath;
    private String fsPathQuery = "//dir[@name = 'test']";

    @Before
    public void setUp() {
        rootPath = new File("src/test/resources");
        fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.configureExplorer(new String[]{"-path", rootPath.getPath()});
        fsPathExplorer.createFSPath();
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteRecursively(Paths.get("src/test/resources/test"));
    }

    @Test
    public void testQueryWithEmptyPath() {
        FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathQuery);
        Assert.assertEquals(0, resultList.size());
    }

    @Test
    public void testQueryWithValidPath() throws IOException {
        Files.createDirectories(Paths.get("src/test/resources/test"));
        Files.createDirectory(Paths.get("src/test/resources/test/subdir1"));
        Files.createDirectory(Paths.get("src/test/resources/test/subdir2/subsubdir"));
        Files.createFile(Paths.get("src/test/resources/test/file1.txt"));

        FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathQuery);
        List<FSPathResult> resultListOut = resultList.getResults();
        assertEquals("src/test/resources/test/subdir1", resultListOut.get(0).getFile().getPath());
        assertEquals("src/test/resources/test/subdir2/subsubdir", resultListOut.get(1).getFile().getPath());
    }

    @Test(expected = IOException.class)
    public void testQueryWithInvalidPath() {
        fsPathExplorer.fsPathQuery = "//dir[@name = 'invalid']";
        fsPathExplorer.fsPath.query(fsPathQuery);
    }
}