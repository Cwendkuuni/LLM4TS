package org.fixsuite.message;

import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

    private static final String TEST_LIBRARY_PATH = "path/to/test/library";
    private static final Logger logger = LoggerFactory.getLogger(Library.class);

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory(TEST_LIBRARY_PATH);
        assertTrue(result);
        assertFalse(library.getDictionaries().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryNullPath() {
        Library library = new Library();
        boolean result = library.loadFromDirectory(null);
        fail("Expected IllegalArgumentException but got: " + result);
    }

    @Test
    public void testLoadFromDirectoryEmptyDirectory() {
        Library library = new Library();
        String emptyDirectoryPath = "path/to/empty/directory";
        boolean result = library.loadFromDirectory(emptyDirectoryPath);
        assertFalse(result);
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        library.loadFromDirectory(TEST_LIBRARY_PATH);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionaryValidVersion() {
        Library library = new Library();
        library.loadFromDirectory(TEST_LIBRARY_PATH);
        DictionaryInfo dictionary = library.getDictionary("4.4");
        assertNotNull(dictionary);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDictionaryNullVersion() {
        Library library = new Library();
        library.loadFromDirectory(TEST_LIBRARY_PATH);
        DictionaryInfo dictionary = library.getDictionary(null);
        fail("Expected NullPointerException but got: " + dictionary);
    }

    @Test
    public void testGetDictionaryInvalidVersion() {
        Library library = new Library();
        library.loadFromDirectory(TEST_LIBRARY_PATH);
        DictionaryInfo dictionary = library.getDictionary("1.0"); // Replace with an invalid version
        assertNull(dictionary);
    }

    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = new File("path/to/test/files").listFiles();
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
        assertNotNull(arrangedFiles.get(0)); // Fields.xml
        assertNotNull(arrangedFiles.get(1)); // Enums.xml
        assertNotNull(arrangedFiles.get(2)); // Components.xml
        assertNotNull(arrangedFiles.get(3)); // MsgType.xml
        assertNotNull(arrangedFiles.get(4)); // MsgContents.xml
    }
}