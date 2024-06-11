package org.fixsuite.message;

import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryTest {

    private static final String LIBRARY_PATH = "path/to/library"; // Replace this with the actual path to the library directory
    private static final Logger logger = LoggerFactory.getLogger(Library.class);

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
        for (DictionaryInfo dictionary : dictionaries) {
            assertTrue(dictionary.isLoaded());
        }
        assertEquals(5, dictionaries.size());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        String version = "version_number"; // Replace this with a valid dictionary version number
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNotNull(dictionary);
        assertTrue(dictionary.isLoaded());
    }

    @Test
    public void testLoadDirectory() throws IOException {
        Library library = new Library();
        File tempDirectory = new File(System.getProperty("java.io.tmpdir"));
        File libraryDirectory = new File(tempDirectory, "library");
        libraryDirectory.mkdirs();
        File[] files = {
                createFile(libraryDirectory, "Fields.xml"),
                createFile(libraryDirectory, "Enums.xml"),
                createFile(libraryDirectory, "Components.xml"),
                createFile(libraryDirectory, "MsgType.xml"),
                createFile(libraryDirectory, "MsgContents.xml")
        };
        library.loadDirectory(Arrays.asList(files), libraryDirectory.getName());
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
        for (DictionaryInfo dictionary : dictionaries) {
            assertTrue(dictionary.isLoaded());
        }
        assertEquals(5, dictionaries.size());
    }

    @Test
    public void testArrangeFiles() {
        File[] files = {
                new File("Fields.xml"),
                new File("Enums.xml"),
                new File("Components.xml"),
                new File("MsgType.xml"),
                new File("MsgContents.xml")
        };
        List<File> arrangedFiles = new Library().arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());
        for (int i = 0; i < arrangedFiles.size(); ++i) {
            int expectedKey = i + 1;
            File file = arrangedFiles.get(i);
            String fileName = file.getName();
            switch (expectedKey) {
                case 1:
                    assertEquals("Fields.xml", fileName);
                    break;
                case 2:
                    assertEquals("Enums.xml", fileName);
                    break;
                case 3:
                    assertEquals("Components.xml", fileName);
                    break;
                case 4:
                    assertEquals("MsgType.xml", fileName);
                    break;
                case 5:
                    assertEquals("MsgContents.xml", fileName);
                    break;
                default:
                    fail("Unexpected file: " + file.getName());
            }
        }
    }

    private File createFile(File directory, String fileName) throws IOException {
        File file = new File(directory, fileName);
        FileWriter writer = new FileWriter(file);
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.close();
        return file;
    }

    @After
    public void cleanUp() {
        File tempDirectory = new File(System.getProperty("java.io.tmpdir"));
        File libraryDirectory = new File(tempDirectory, "library");
        if (libraryDirectory.exists()) {
            deleteRecursively(libraryDirectory);
        }
    }

    private void deleteRecursively(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            for (File child : children) {
                deleteRecursively(child);
            }
        }
        if (fileOrDirectory.exists()) {
            fileOrDirectory.delete();
        }
    }
}