package org.fixsuite.message;

import org.junit.Test;
import org.junit.Assert;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryTest {
    @Test
    public void testLibraryConstruction() {
        Library library = new Library();
        Assert.assertNotNull(library);
    }

    @Test
    public void testLoadEmptyDirectory() {
        boolean result = Library.loadFromDirectory("/path/to/empty_directory");
        Assert.assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testLoadNullLibraryPath() {
        boolean result = Library.loadFromDirectory(null);
    }

    @Test
    public void testLoadDirectoryWithFiles() {
        Library library = new Library();
        File directory = new File("/path/to/directory");
        File[] files = directory.listFiles();
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            fileList.add(file);
        }
        library.loadFromDirectory("/path/to/directory");
        Assert.assertTrue(library.getDictionaries().size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadDirectoryWithInvalidExtension() {
        Library library = new Library();
        File directory = new File("/path/to/directory");
        File[] files = directory.listFiles();
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            if (!file.getName().endsWith(".xml")) {
                fileList.add(file);
            }
        }
        library.loadFromDirectory("/path/to/directory", fileList.toArray(new File[0]));
    }

    @Test
    public void testLoadCorruptedXmlFile() {
        Library library = new Library();
        File directory = new File("/path/to/directory");
        File[] files = directory.listFiles();
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.getName().endsWith(".xml")) {
                File corruptedFile = new File("/path/to/corrupted_file.xml");
                try {
                    library.loadFromDirectory("/path/to/directory", file, corruptedFile);
                } catch (Exception e) {
                    // Expected exception
                }
            }
            fileList.add(file);
        }
        library.loadFromDirectory("/path/to/directory", fileList.toArray(new File[0]));
        Assert.assertTrue(library.getDictionaries().size() > 0);
    }

    // Add other test cases for getDictionaries(), getDictionary(String version), arrangeFiles(File[] files), and loadDirectory(List<File> files, String version) methods
}