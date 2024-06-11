package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testCreateFSPathExplorer() {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        assertNotNull(fsPathExplorer.fsPath);
    }

    @Test
    public void testConfigureExplorerWithRootPath() {
        String rootPath = "/tmp";
        String fsPathQuery = "";
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.configureExplorer(new String[]{"-p", rootPath});
        assertEquals(new File(rootPath), fsPathExplorer.rootPath);
        assertEquals(fsPathQuery, fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testConfigureExplorerWithFSPathQuery() {
        String rootPath = "/tmp";
        String fsPathQuery = "//dir[@name = 'foo']";
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        fsPathExplorer.configureExplorer(new String[]{"-find", rootPath, fsPathQuery});
        assertEquals(new File(rootPath), fsPathExplorer.rootPath);
        assertEquals(fsPathQuery, fsPathExplorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() throws IOException {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        fsPathExplorer.rootPath = rootPath;
        fsPathExplorer.createFSPath();
        assertNotNull(fsPathExplorer.fsPath);
        assertEquals(rootPath, fsPathExplorer.fsPath.getRootDirectory());
    }

    @Test
    public void testStartWithValidQuery() throws IOException {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        String rootPath = "/tmp";
        String fsPathQuery = "//dir[@name = 'foo']";
        fsPathExplorer.rootPath = new File(rootPath);
        fsPathExplorer.configureExplorer(new String[]{"-p", rootPath, "-find", fsPathQuery});
        fsPathExplorer.createFSPath();
        fsPathExplorer.start();
        String expectedResult = "/tmp/foo";
        File file = new File(expectedResult);
        if (file.exists() && file.isDirectory()) {
            String[] files = file.list();
            assertNotNull(files);
            for (String filePath : files) {
                if (filePath.equals(expectedResult)) {
                    fsPathExplorer.prompt.println(expectedResult);
                    break;
                }
            }
        } else {
            assertNull(fsPathExplorer.prompt.readLine());
        }
    }

    @Test
    public void testStartWithInvalidQuery() throws IOException {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        String rootPath = "/tmp";
        String fsPathQuery = "//invalidQuery";
        fsPathExplorer.rootPath = new File(rootPath);
        fsPathExplorer.configureExplorer(new String[]{"-p", rootPath, "-find", fsPathQuery});
        fsPathExplorer.createFSPath();
        fsPathExplorer.start();
        assertNull(fsPathExplorer.prompt.readLine());
    }

    @Test
    public void testStartWithEmptyQuery() throws IOException {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        String rootPath = "/tmp";
        String fsPathQuery = "";
        fsPathExplorer.rootPath = new File(rootPath);
        fsPathExplorer.configureExplorer(new String[]{"-p", rootPath, "-find", fsPathQuery});
        fsPathExplorer.createFSPath();
        fsPathExplorer.start();
        assertNull(fsPathExplorer.prompt.readLine());
    }

    @Test
    public void testStartWithHelpCommand() throws IOException {
        FSPathExplorer fsPathExplorer = new FSPathExplorer();
        String rootPath = "/tmp";
        fsPathExplorer.rootPath = new File(rootPath);
        fsPathExplorer.configureExplorer(new String[]{"-p", rootPath, "help"});
        fsPathExplorer.createFSPath();
        fsPathExplorer.start();
        String expectedHelpText = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n      with a file size greater than 10000 bytes\n\n3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'";
        String helpText = "";
        String line;
        while ((line = fsPathExplorer.prompt.readLine()) != null) {
            helpText += line;
        }
        assertEquals(expectedHelpText, helpText);
    }
}