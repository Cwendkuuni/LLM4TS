package org.fixsuite.message.test;

import org.junit.*;
import static org.junit.Assert.*;
import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import java.io.*;
import java.util.*;

public class LibraryTest {

    @BeforeClass
    public static void setup() {
        Library.logger = org.slf4j.LoggerFactory.getLogger(Library.class);
    }

    @Test
    public void testLoadFromDirectory() {
        String libraryPath = "testLibraryPath";
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testLoadFromEmptyDirectory() {
        String libraryPath = "testEmptyLibraryPath";
        Library library = new Library();
        assertFalse(library.loadFromDirectory(libraryPath));
    }

    @Test
    public void testLoadFromNonExistentDirectory() {
        String libraryPath = "testNonExistentLibraryPath";
        Library library = new Library();
        assertFalse(library.loadFromDirectory(libraryPath));
    }

    @Test
    public void testLoadFromDirectoryWithInvalidFileExtensions() {
        String libraryPath = "testInvalidLibraryPath";
        File invalidFile = new File(libraryPath, "InvalidFile.txt");
        File[] files = {new File("Fields.xml"), invalidFile, new File("MsgType.xml")};
        Library library = new Library();
        List<File> arrangedFiles = library.arrangeFiles(files);
        library.loadDirectory(Arrays.asList(arrangedFiles.toArray(new File[0])), "testVersion");
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testLoadDictionariesMultipleTimes() {
        String libraryPath = "testLibraryPath";
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
        assertTrue(library.loadFromDirectory(libraryPath));
        // Add more assertions based on the expected behavior
    }

    @Test(expected = NullPointerException.class)
    public void testLoadFromDirectoryWithNullLibraryPath() {
        Library library = new Library();
        library.loadFromDirectory(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadDictionariesWhenAlreadyLoaded() {
        String libraryPath = "testLibraryPath";
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
        assertTrue(library.loadFromDirectory(libraryPath));
    }

    @AfterClass
    public static void teardown() {
        Library.logger = null;
    }
}