package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testCreateFSPathExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNull(explorer.getRootPath());
        assertNull(explorer.getFsPath());
    }

    @Test
    public void testCreateFSPathExplorerWithRootPath() {
        File rootPath = new File("/tmp");
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(rootPath);
        assertEquals(rootPath, explorer.getRootPath());
        assertNotNull(explorer.getFsPath());
    }

    @Test
    public void testStartWithEmptyQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/tmp"));
        explorer.createFSPath();
        explorer.start();

        String expected = "Current directory : /tmp\n";
        String actual = System.out.getHistogram().get(0).getDisplayText();
        assertEquals(expected, actual);
    }

    @Test
    public void testStartWithCDCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("cd /tmp");
        explorer.prompt.newline();

        String expected = "changed to : /tmp\n";
        String actual = System.out.getHistogram().get(1).getDisplayText();
        assertEquals(expected, actual);
    }

    @Test
    public void testStartWithPWDCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/tmp"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("pwd");
        explorer.prompt.newline();

        String expected = "Current directory : /tmp\n";
        String actual = System.out.getHistogram().get(1).getDisplayText();
        assertEquals(expected, actual);
    }

    @Test
    public void testStartWithHelpCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/tmp"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("help");
        explorer.prompt.newline();

        String expected = "FSPathExplorer help :\n\n"
                + "Enter an FSPath query to search the filesystem\n\n"
                + "Examples :\n\n"
                + "1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n"
                + "2) //dir[@name = 'src']//file[@length > 10000] \n   finds any file which is nested anywhere below a 'src' directory\n"
                + "   with a file size greater than 10000 bytes\n\n"
                + "3) //dir[fs:matches(@name, '^[.].*')] \n   finds all directories in the directory sructure which begin with a '.'\n\n"
                + "Attributes : (see javadoc for java.io.File for more info)\n"
                + "@size @dir @file @lastModified @lastModifiedTime @length @name @parent @parentFile @absolutePath @canRead @canWrite @isDirectory @isFile @isHidden\n\n"
                + "Commands :\n\n"
                + "cd : change the current directory\n\t usage : cd <new_directory>\n"
                + "exit : exit the FSPath Explorer application\n\t usage : exit\n"
                + "help : display available commands and help\n\t usage : help\n"
                + "pwd : print the current working directory\n\t usage : pwd";
        String actual = System.out.getHistogram().get(1).getDisplayText();
        assertEquals(expected, actual);
    }

    @Test
    public void testStartWithQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("//dir[@name = 'usr']/@name");
        explorer.prompt.newline();

        List<FSPathResult> results = explorer.getFsPath().getQueryResults();
        assertEquals(1, results.size());
        FSPathResult result = results.get(0);
        assertEquals("/usr", result.getFile().getPath());
    }

    @Test
    public void testStartWithQueryWithMultipleMatches() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("//file[@length > 1000]");
        explorer.prompt.newline();

        List<FSPathResult> results = explorer.getFsPath().getQueryResults();
        int expectedSize = (int) (Math.pow(10, 5) * Math.pow(2, 40) * 0.1); // approximation for the number of files with size > 1000
        assertEquals(expectedSize, results.size());
    }

    @Test
    public void testStartWithQueriesWithAndOperator() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("//dir[@name = 'usr']/file[@name = 'config.ini']");
        explorer.prompt.newline();

        List<FSPathResult> results = explorer.getFsPath().getQueryResults();
        assertEquals(1, results.size());
        FSPathResult result = results.get(0);
        File file = result.getFile();
        assertTrue(file.isFile());
        assertEquals("config.ini", file.getName());
        assertEquals("/usr/config.ini", file.getPath());
    }

    @Test
    public void testStartWithQueriesWithOrOperator() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("//file[@length > 1000][@name = 'config.ini']");
        explorer.prompt.newline();

        List<FSPathResult> results = explorer.getFsPath().getQueryResults();
        assertEquals(2, results.size());
        for (FSPathResult result : results) {
            File file = result.getFile();
            assertTrue(file.isFile());
            if (file.getName().equals("config.ini")) {
                assertEquals("/etc/config.ini", result.getFile().getPath());
            } else {
                assertEquals("/usr/local/config.ini", result.getFile().getPath());
            }
        }
    }

    @Test
    public void testStartWithQueriesWithGlobPattern() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("//file[@name = '*.txt']");
        explorer.prompt.newline();

        List<FSPathResult> results = explorer.getFsPath().getQueryResults();
        int expectedSize = (int) (Math.pow(10, 5) * Math.pow(2, 40) * 0.05); // approximation for the number of files with name ending with '.txt'
        assertEquals(expectedSize, results.size());
    }

    @Test
    public void testStartWithQueriesWithNegatedGlobPattern() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("//file[@name != '*.txt']");
        explorer.prompt.newline();

        List<FSPathResult> results = explorer.getFsPath().getQueryResults();
        int totalFiles = (int) (Math.pow(10, 5) * Math.pow(2, 40)); // approximation for the total number of files in the file system
        int expectedSize = totalFiles - expectedSize; // approximation for the number of files without '.txt' in their names
        assertEquals(expectedSize, results.size());
    }

    @Test
    public void testStartWithQueriesWithAttributeFiltering() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("//file[@length > 1000][@isDirectory = false]");
        explorer.prompt.newline();

        List<FSPathResult> results = explorer.getFsPath().getQueryResults();
        int expectedSize = (int) (Math.pow(10, 5) * Math.pow(2, 40) * 0.05 * 0.95); // approximation for the number of files with size > 1000 and not directories
        assertEquals(expectedSize, results.size());
    }

    @Test(expected = IOException.class)
    public void testStartWithQueriesWithInvalidQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.setRootPath(new File("/"));
        explorer.createFSPath();
        explorer.start();

        explorer.prompt.write("//file[@name = 'foo'][@bar = 'bar']");
        explorer.prompt.newline();
    }
}