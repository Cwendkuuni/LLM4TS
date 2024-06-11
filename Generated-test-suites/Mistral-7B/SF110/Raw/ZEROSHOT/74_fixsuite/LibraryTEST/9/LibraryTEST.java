package org.fixsuite.message.test;

import static org.junit.Assert.*;
import org.fixsuite.message.*;
import org.goldenport.collection.treemap.TreeMap;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LibraryTest {
    private static final String LIBRARY_PATH = "test/resources/library"; // Replace this with the actual directory path
    private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        Map<String, DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
        assertEquals(5, dictionaries.size());
        for (DictionaryInfo dictionaryInfo : dictionaries.values()) {
            assertNotNull(dictionaryInfo);
            assertTrue(dictionaryInfo.isLoaded());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryEmptyDirectory() {
        Library library = new Library();
        File emptyDirectory = new File("test/resources/emptyDirectory");
        library.loadFromDirectory(emptyDirectory.getAbsolutePath());
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryInvalidPath() {
        Library library = new Library();
        File invalidPath = new File("test/resources/invalidPath");
        library.loadFromDirectory(invalidPath.getAbsolutePath());
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
        assertEquals(5, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        DictionaryInfo dictionary = library.getDictionary("1.0");
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
    }

    @Test(expected = NullPointerException.class)
    public void testGetDictionaryNullVersion() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        DictionaryInfo dictionary = library.getDictionary(null);
        fail("Expected NullPointerException");
    }
}