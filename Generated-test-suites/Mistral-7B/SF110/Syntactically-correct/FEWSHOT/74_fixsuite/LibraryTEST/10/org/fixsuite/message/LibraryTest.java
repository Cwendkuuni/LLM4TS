package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory() {
        String libraryPath = "testData/library"; // Replace with the path to your test data directory
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
        assertEquals(5, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        String libraryPath = "testData/library"; // Replace with the path to your test data directory
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
        DictionaryInfo dictionary = library.getDictionary("4.4");
        assertNotNull(dictionary);
    }

    @Test
    public void testGetDictionaries() {
        String libraryPath = "testData/library"; // Replace with the path to your test data directory
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
        for (DictionaryInfo dictionary : dictionaries) {
            assertNotNull(dictionary.getVersion());
        }
    }

    @Test
    public void testLoadDirectory() {
        String libraryPath = "testData/library"; // Replace with the path to your test data directory
        Library library = new Library();
        List<File> files = new ArrayList<>();
        File directory = new File(libraryPath);
        File[] fileArray = directory.listFiles();
        for (File file : fileArray) {
            files.add(file);
        }
        library.loadDirectory(files, "testVersion");
        DictionaryInfo dictionary = library.getDictionary("testVersion");
        assertNotNull(dictionary);
    }

    @Test
    public void testArrangeFiles() {
        String libraryPath = "testData/library"; // Replace with the path to your test data directory
        File directory = new File(libraryPath);
        File[] files = directory.listFiles();
        Map<Integer, File> expectedFiles = new TreeMap<>();
        for (File file : files) {
            expectedFiles.put(file.getName().equals("Fields.xml") ? 1 :
                            file.getName().equals("Enums.xml") ? 2 :
                            file.getName().equals("Components.xml") ? 3 :
                            file.getName().equals("MsgType.xml") ? 4 : 5, file);
        }
        List<File> actualFiles = new Library().arrangeFiles(files);
        List<Integer> keys = new ArrayList<>(expectedFiles.keySet());
        for (int i = 0; i < keys.size(); i++) {
            File expectedFile = expectedFiles.get(keys.get(i));
            File actualFile = actualFiles.get(i);
            assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
        }
    }
}