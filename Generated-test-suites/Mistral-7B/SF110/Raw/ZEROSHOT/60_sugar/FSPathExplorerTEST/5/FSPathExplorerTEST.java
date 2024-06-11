package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.attributes.FSPathAttributes;
import net.sf.sugar.fspath.factory.FSPathFactory;
import net.sf.sugar.fspath.result.FSPathResult;
import net.sf.sugar.fspath.result.FSPathResultList;

public class FSPathExplorerTest {

    private FSPathExplorer explorer;
    private File rootPath;

    @Before
    public void setUp() {
        explorer = new FSPathExplorer();
        rootPath = new File(".");
        explorer.configureExplorer(new String[] { "-p", rootPath.getAbsolutePath() });
        explorer.createFSPath();
    }

    @After
    public void tearDown() {
        explorer = null;
    }

    @Test
    public void testCreateFSPath() {
        assertNotNull(explorer.getFsPath());
    }

    @Test
    public void testQueryEmptyPath() throws IOException {
        FSPathResultList results = explorer.query("");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryRootPath() throws IOException {
        String query = "";
        FSPathResultList results = explorer.query(query);
        List<File> expectedFiles = new ArrayList<>();
        expectedFiles.add(rootPath);
        expectedFiles.add(new File(rootPath, "."));
        expectedFiles.add(new File(rootPath, ".."));
        for (FSPathResult result : results) {
            File actualFile = result.getFile();
            assertTrue(expectedFiles.contains(actualFile));
            expectedFiles.remove(actualFile);
        }
        assertFalse(expectedFiles.isEmpty());
    }

    @Test
    public void testQueryFile() throws IOException {
        String query = "file[@name = 'test.txt']";
        FSPathResultList results = explorer.query(query);
        File expectedFile = new File(rootPath, "test.txt");
        assertEquals(expectedFile, results.get(0).getFile());
    }

    @Test
    public void testQueryDirectory() throws IOException {
        String query = "dir[@name = 'src']";
        FSPathResultList results = explorer.query(query);
        File expectedDirectory = new File(rootPath, "src");
        assertEquals(expectedDirectory, results.get(0).getFile());
    }

    @Test
    public void testQueryNonExistentPath() throws IOException {
        String query = "file[@name = 'nonexistent.txt']";
        FSPathResultList results = explorer.query(query);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryFileWithExtensions() throws IOException {
        String query = "file[@name = '*.txt']";
        FSPathResultList results = explorer.query(query);
        List<File> expectedFiles = new ArrayList<>();
        File txtFile1 = new File(rootPath, "test1.txt");
        File txtFile2 = new File(rootPath, "test2.txt");
        expectedFiles.add(txtFile1);
        expectedFiles.add(txtFile2);
        for (FSPathResult result : results) {
            File actualFile = result.getFile();
            assertTrue(expectedFiles.contains(actualFile));
            expectedFiles.remove(actualFile);
        }
        assertFalse(expectedFiles.isEmpty());
    }

    @Test
    public void testQueryDirectoryWithSubdirectories() throws IOException {
        String query = "dir[@name = 'src']//dir[@name = 'test']//file[@name = '*']";
        FSPathResultList results = explorer.query(query);
        File srcDirectory = new File(rootPath, "src");
        File testDirectory = new File(srcDirectory, "test");
        File testFile1 = new File(testDirectory, "test1.txt");
        File testFile2 = new File(testDirectory, "test2.txt");
        List<File> expectedFiles = new ArrayList<>();
        expectedFiles.add(testFile1);
        expectedFiles.add(testFile2);
        for (FSPathResult result : results) {
            File actualFile = result.getFile();
            assertTrue(expectedFiles.contains(actualFile));
            expectedFiles.remove(actualFile);
        }
        assertFalse(expectedFiles.isEmpty());
    }

    @Test
    public void testQueryWithAttributes() throws IOException {
        String query = "file[@size > 10]";
        FSPathResultList results = explorer.query(query);
        List<File> expectedFiles = new ArrayList<>();
        File file1 = new File(rootPath, "largeFile.txt");
        File file2 = new File(rootPath, "anotherLargeFile.txt");
        expectedFiles.add(file1);
        expectedFiles.add(file2);
        for (FSPathResult result : results) {
            File actualFile = result.getFile();
            assertTrue(expectedFiles.contains(actualFile));
            expectedFiles.remove(actualFile);
        }
        assertFalse(expectedFiles.isEmpty());
    }

    @Test
    public void testQueryWithAttributesAndFilters() throws IOException {
        String query = "file[@size > 10 and @hidden = false]";
        FSPathResultList results = explorer.query(query);
        List<File> expectedFiles = new ArrayList<>();
        File file1 = new File(rootPath, "largeFile.txt");
        File file2 = new File(rootPath, "anotherLargeFile.txt");
        File file3 = new File(rootPath, "visibleLargeFile.txt");
        expectedFiles.add(file1);
        expectedFiles.add(file2);
        expectedFiles.add(file3);
        for (FSPathResult result : results) {
            File actualFile = result.getFile();
            assertTrue(expectedFiles.contains(actualFile));
            expectedFiles.remove(actualFile);
        }
        assertFalse(expectedFiles.isEmpty());
    }

    @Test
    public void testQueryWithFilterButNoAttribute() throws IOException {
        String query = "file[@hidden = false]";
        FSPathResultList results = explorer.query(query);
        List<File> expectedFiles = new ArrayList<>();
        File file1 = new File(rootPath, "visibleFile.txt");
        File file2 = new File(rootPath, "anotherVisibleFile.txt");
        expectedFiles.add(file1);
        expectedFiles.add(file2);
        for (FSPathResult result : results) {
            File actualFile = result.getFile();
            assertTrue(expectedFiles.contains(actualFile));
            expectedFiles.remove(actualFile);
        }
        assertFalse(expectedFiles.isEmpty());
    }

    @Test
    public void testQueryWithAttributesAndFiltersNegated() throws IOException {
        String query = "file[@size <= 10 and @hidden = true]";
        FSPathResultList results = explorer.query(query);
        List<File> expectedFiles = new ArrayList<>();
        File hiddenSmallFile1 = new File(rootPath, "hiddenSmallFile1.txt");
        File hiddenSmallFile2 = new File(rootPath, "hiddenSmallFile2.txt");
        expectedFiles.add(hiddenSmallFile1);
        expectedFiles.add(hiddenSmallFile2);
        for (FSPathResult result : results) {
            File actualFile = result.getFile();
            assertTrue(expectedFiles.contains(actualFile));
            expectedFiles.remove(actualFile);
        }
        assertFalse(expectedFiles.isEmpty());
    }

    @Test
    public void testQueryWithMultipleAttributes() throws IOException {
        String query = "file[@size > 10 and @hidden = false and @readable = true]";
        FSPathResultList results = explorer.query(query);
        List<File> expectedFiles = new ArrayList<>();
        File file1 = new File(rootPath, "largeFile.txt");
        File file2 = new File(rootPath, "anotherLargeFile.txt");
        File file3 = new File(rootPath, "visibleLargeFile.txt");
        expectedFiles.add(file1);
        expectedFiles.add(file2);
        expectedFiles.add(file3);
        for (FSPathResult result : results) {
            File actualFile = result.getFile();
            assertTrue(expectedFiles.contains(actualFile));
            expectedFiles.remove(actualFile);
        }
        assertFalse(expectedFiles.isEmpty());
    }

    @Test
    public void testQueryWithMultipleAttributesAndFilters() throws IOException {
        String query = "file[@size > 10 and @hidden = false and @readable = false and @hidden = true]";
        FSPathResultList results = explorer.query(query);
        File hiddenSmallFile1 = new File(rootPath, "hiddenSmallFile1.txt");
        File hiddenSmallFile2 = new File(rootPath, "hiddenSmallFile2.txt");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryWithMultipleAttributesAndFiltersNegated() throws IOException {
        String query = "file[@size <= 10 or @hidden = false or @readable = true]";
        FSPathResultList results = explorer.query(query);
        File file1 = new File(rootPath, "smallFile.txt");
        File file2 = new File(rootPath, "visibleFile.txt");
        File file3 = new File(rootPath, "anotherVisibleFile.txt");
        List<File> expectedFiles = new ArrayList<>();
        expectedFiles.add(file1);
        expectedFiles.add(file2);
        expectedFiles.add(file3);
        for (FSPathResult result : results) {
            File actualFile = result.getFile();
            assertTrue(expectedFiles.contains(actualFile));
            expectedFiles.remove(actualFile);
        }
        assertFalse(expectedFiles.isEmpty());
    }

    @Test
    public void testQueryWithMultipleAttributesAndFiltersNegatedAndOr() throws IOException {
        String query = "file[@size > 10 and (@hidden = false or @readable = true) and @hidden = true]";
        FSPathResultList results = explorer.query(query);
        File file1 = new File(rootPath, "largeFile.txt");
        File file2 = new File(rootPath, "anotherLargeFile.txt");
        File file3 = new File(rootPath, "anotherVisibleLargeFile.txt");
        List<File> expectedFiles = new ArrayList<>();
        expectedFiles.add(file1);
        expectedFiles.add(file2);
        expectedFiles.add(file3);
        for (FSPathResult result : results) {
            File actualFile = result.getFile();
            assertTrue(expectedFiles.contains(actualFile));
            expectedFiles.remove(actualFile);
        }
        assertFalse(expectedFiles.isEmpty());
    }

    @Test
    public void testQueryWithInvalidQuery() throws IOException {
        String query = "file[@size > 10 and @hidden = false and @hidden = false]";
        FSPathResultList results = explorer.query(query);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQueryWithInvalidAttribute() throws IOException {
        String query = "file[@size > 10 and @color = false and @hidden = false]";
        FSPathResultList results = explorer.query(query);
        assertTrue(results.isEmpty());
    }
}