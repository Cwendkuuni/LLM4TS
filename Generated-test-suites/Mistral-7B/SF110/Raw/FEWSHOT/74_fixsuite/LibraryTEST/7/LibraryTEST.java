package org.fixsuite.message;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.MsgType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

    private static final String LIBRARY_PATH = "src/test/resources/library";

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        assertTrue(library.getDictionaries().size() > 0);
    }

    @Test
    public void testLoadFromEmptyDirectory() {
        Library library = new Library();
        File emptyDir = new File("src/test/resources/empty");
        boolean result = library.loadFromDirectory(emptyDir.getAbsolutePath());
        assertFalse(result);
    }

    @Test
    public void testLoadFromInvalidPath() {
        Library library = new Library();
        File invalidPath = new File("src/test/resources/invalid");
        boolean result = library.loadFromDirectory(invalidPath.getAbsolutePath());
        assertFalse(result);
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        assertEquals(5, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        DictionaryInfo dictionary = library.getDictionary("FIX 5.0");
        assertNotNull(dictionary);
        assertEquals("FIX 5.0", dictionary.getVersion());
    }

    @Test
    public void testGetDictionaryNull() {
        Library library = new Library();
        assertNull(library.getDictionary(null));
    }

    @Test
    public void testGetDictionaryUnloaded() {
        Library library = new Library();
        DictionaryInfo dictionary = library.getDictionary("TEST");
        assertNull(dictionary);
    }

    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        String version = "FIX 5.0";
        List<File> files = Arrays.asList(new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml"));
        library.loadDirectory(files, version);
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNotNull(dictionary);
        assertEquals(version, dictionary.getVersion());
        assertTrue(dictionary.isLoaded());
    }

    @Test
    public void testLoadDirectoryEmptyList() {
        Library library = new Library();
        String version = "FIX 5.0";
        List<File> files = Collections.emptyList();
        library.loadDirectory(files, version);
        assertNull(library.getDictionary(version));
    }

    @Test
    public void testLoadDirectoryNullFiles() {
        Library library = new Library();
        String version = "FIX 5.0";
        List<File> files = null;
        try {
            library.loadDirectory(files, version);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File directory = new File("src/test/resources/library");
        File[] files = directory.listFiles();
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
        Map<Integer, File> expectedFiles = new HashMap<>();
        expectedFiles.put(1, new File("Fields.xml"));
        expectedFiles.put(2, new File("Enums.xml"));
        expectedFiles.put(3, new File("Components.xml"));
        expectedFiles.put(4, new File("MsgType.xml"));
        expectedFiles.put(5, new File("MsgContents.xml"));
        for (int i = 0; i < 5; i++) {
            File file = arrangedFiles.get(i);
            File expectedFile = expectedFiles.get(Integer.valueOf(i + 1));
            assertEquals(expectedFile.getName(), file.getName());
            assertEquals(expectedFile.getAbsolutePath(), file.getAbsolutePath());
        }
    }

    @Test
    public void testArrangeFilesEmptyDirectory() {
        Library library = new Library();
        File emptyDir = new File("src/test/resources/empty");
        List<File> arrangedFiles = library.arrangeFiles(emptyDir.listFiles());
        assertEquals(0, arrangedFiles.size());
    }

    @Test
    public void testArrangeFilesInvalidFileName() {
        Library library = new Library();
        File directory = new File("src/test/resources/library");
        File invalidFile = new File(directory.getAbsolutePath() + "/test.txt");
        List<File> files = Arrays.asList(new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml"), invalidFile);
        try {
            library.arrangeFiles(files);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}