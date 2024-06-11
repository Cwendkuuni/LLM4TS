package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {
    private static final String LIBRARY_PATH = "src/test/resources/library"; // Replace with your test resource directory path
    private static final Logger logger = LoggerFactory.getLogger(Library.class);

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory(LIBRARY_PATH);
        assertTrue(result);
        assertFalse(library.dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        DictionaryInfo dictionary = library.getDictionary("TestVersion");
        assertNull(dictionary);
    }

    @Test
    public void testLoadDirectory() {
        Library library = new Library();
        List<File> files = new ArrayList<>();
        File dir = new File("src/test/resources/library/TestDirectory"); // Replace with your test directory path
        File[] testFiles = dir.listFiles();
        for (File file : testFiles) {
            files.add(file);
        }
        library.loadDirectory(files, "TestVersion");
        DictionaryInfo dictionary = library.getDictionaries().get(0);
        assertTrue(dictionary.isLoaded());
    }

    @Test
    public void testArrangeFiles() {
        File[] files = new File("src/test/resources/library/TestFiles").listFiles();
        List<File> arrangedFiles = new Library().arrangeFiles(files);

        assertEquals(5, arrangedFiles.size());
        assertEquals(Arrays.asList(new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml")), arrangedFiles);
    }
}