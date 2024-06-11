package org.fixsuite.message;

import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

    private static final String LIBRARY_PATH = "test-resources/libraries"; // Replace with the actual path to your test library directory
    private static final String LIBRARY_VERSION = "test-version"; // Replace with the expected version of the library you want to test

    private Library library;

    @BeforeClass
    public static void setupBeforeClass() {
        Logger.getRootLogger().setLevel(org.slf4j.Level.DEBUG);
    }

    @Before
    public void setup() {
        library = new Library();
    }

    @Test
    public void testLoadFromDirectory() {
        library.loadFromDirectory(LIBRARY_PATH);
        assertTrue(library.getDictionaries().size() > 0);
        assertTrue(library.getDictionaries().get(0).getVersion().equals(LIBRARY_VERSION));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryEmptyPath() {
        library.loadFromDirectory(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryInvalidPath() {
        library.loadFromDirectory("non-existent-directory");
    }

    @Test(expected = IOException.class)
    public void testLoadFromDirectoryFileReadError() throws IOException {
        File testDirectory = new File(LIBRARY_PATH);
        File testFile = new File(testDirectory, "Fields.xml"); // Replace with a file that doesn't exist in the library directory
        testFile.deleteOnExit();
        testDirectory.mkdirs();
        library.loadFromDirectory(new File(LIBRARY_PATH));
    }

    @Test
    public void testGetDictionaries() {
        library.loadFromDirectory(LIBRARY_PATH);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertTrue(dictionaries.size() > 0);
    }

    @Test
    public void testGetDictionary() {
        library.loadFromDirectory(LIBRARY_PATH);
        DictionaryInfo dictionary = library.getDictionary(LIBRARY_VERSION);
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
    }

    @Test
    public void testGetDictionaryNullVersion() {
        library.loadFromDirectory(LIBRARY_PATH);
        assertNull(library.getDictionary(null));
    }
}