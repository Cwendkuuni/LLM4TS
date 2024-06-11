package org.junit.test;

import org.junit.*;
import static org.junit.Assert.*;
import org.fixsuite.message.Library;
import java.io.*;
import java.io.IOException;
import java.util.*;
import java.util.Arrays;
import java.util.List;

public class LibraryTest {

    private static Library library;
    private static String libraryPath;

    @BeforeClass
    public static void setup() {
        Library.logger = LoggerFactory.getLogger(Library.class);
        library = new Library();
        libraryPath = "test/resources/library";
    }

    @Test
    public void testLibraryConstructor() {
        assertNotNull(library);
    }

    @Test
    public void testLoadFromDirectory() {
        assertTrue(library.loadFromDirectory(libraryPath));
        assertFalse(library.dictionaries.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromInvalidDirectory() {
        String invalidDirectory = "test/resources/LibraryTest";
        assertFalse(library.loadFromDirectory(invalidDirectory));
    }

    @Test
    public void testLoadEmptyDirectory() {
        File emptyDirectory = new File("test/resources/emptyLibrary");
        assertFalse(library.loadFromDirectory(emptyDirectory.getAbsolutePath()));
        assertTrue(library.dictionaries.isEmpty());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadNonExistentFile() {
        File[] files = library.listFiles(new File(libraryPath));
        File nonExistentFile = new File("test/resources/nonExistentFile.xml");
        library.loadDirectory(Arrays.asList(nonExistentFile), "nonExistentVersion");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInvalidFileFormat() {
        File[] files = library.listFiles(new File(libraryPath));
        File invalidFile = new File("test/resources/library/invalidFile.txt");
        library.loadDirectory(Arrays.asList(invalidFile), "invalidVersion");
    }

    @Test(expected = IOException.class)
    public void testParsingFailure() {
        File[] files = library.listFiles(new File(libraryPath));
        File parsingFailureFile = new File("test/resources/library/parsingFailure.xml");
        library.loadDirectory(Arrays.asList(parsingFailureFile), "parsingFailureVersion");
    }

    @Test
    public void testGetDictionaries() {
        library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        library.loadFromDirectory(libraryPath);
        DictionaryInfo dictionary = library.getDictionary("testVersion");
        assertNotNull(dictionary);
    }

    @Test
    public void testLoadDirectory() {
        File[] files = library.listFiles(new File(libraryPath));
        List<File> filesToLoad = Arrays.asList(files);
        library.loadDirectory(filesToLoad, "testVersion");
        assertFalse(library.dictionaries.isEmpty());
    }

    @Test
    public void testArrangeFiles() {
        File testDirectory = new File(libraryPath);
        File[] files = library.listFiles(testDirectory);
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
    }
}