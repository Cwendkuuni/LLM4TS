package org.fixsuite.message;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class LibraryTest {

    private static final String LIBRARY_PATH = "test/library";
    private static final String DICTIONARY_VERSION = "1.0.0";
    private static final String[] FILE_NAMES = { "Fields.xml", "Enums.xml", "Components.xml", "MsgType.xml", "MsgContents.xml" };
    private Library library;
    private List<File> files;
    private List<File> arrangedFiles;
    private DictionaryInfo dictionary;

    @Before
    public void setup() throws IOException {
        library = new Library();
        files = Arrays.asList(new File(LIBRARY_PATH).listFiles());
        arrangedFiles = Arrays.asList(new File(LIBRARY_PATH + File.separator + "Lib1"), new File(LIBRARY_PATH + File.separator + "Lib2"));
    }

    @Test
    public void testLibraryConstructor() {
        assertNotNull(library);
        assertNotNull(library.dictionaries);
    }

    @Test
    public void testLoadFromDirectory_EmptyDirectory() {
        library.loadFromDirectory("");
        assertTrue(library.dictionaries.isEmpty());
    }

    @Test
    public void testLoadFromDirectory_InvalidPath() {
        library.loadFromDirectory("invalidPath");
        assertTrue(library.dictionaries.isEmpty());
        assertFalse(new File("invalidPath").isDirectory());
    }

    @Test
    public void testLoadFromDirectory_EmptyDirectoryContents() {
        library.loadFromDirectory(LIBRARY_PATH);
        assertTrue(library.dictionaries.isEmpty());
    }

    @Test
    public void testLoadFromDirectory_InvalidFileExtension() {
        File invalidFile = new File(LIBRARY_PATH + File.separator + "invalidFile.txt");
        arrangedFiles.add(invalidFile);
        library.loadFromDirectory(LIBRARY_PATH);
        assertTrue(library.dictionaries.isEmpty());
        assertFalse(library.dictionaries.containsKey(DICTIONARY_VERSION));
    }

    @Test
    public void testLoadFromDirectory_ValidFiles() throws IOException {
        library.loadFromDirectory(LIBRARY_PATH);
        assertFalse(library.dictionaries.isEmpty());
        assertNotNull(library.getDictionary(DICTIONARY_VERSION));
    }

    @Test
    public void testLoadFromDirectory_DuplicateDictionary() throws IOException {
        library.loadFromDirectory(LIBRARY_PATH);
        File duplicateDirectory = new File(LIBRARY_PATH + File.separator + "Lib3");
        File[] duplicateFiles = { new File(duplicateDirectory.getPath() + File.separator + "Fields.xml"), new File(duplicateDirectory.getPath() + File.separator + "Enums.xml") };
        library.loadDirectory(Arrays.asList(duplicateFiles), "Lib3");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(2, dictionaries.size());
        DictionaryInfo firstDictionary = dictionaries.get(0);
        DictionaryInfo secondDictionary = dictionaries.get(1);
        assertNotSame(firstDictionary, secondDictionary);
        assertEquals(DICTIONARY_VERSION, firstDictionary.getVersion());
        assertEquals(DICTIONARY_VERSION, secondDictionary.getVersion());
    }

    @Test
    public void testArrangeFiles() throws IOException {
        List<File> inputFiles = Arrays.asList(new File(LIBRARY_PATH + File.separator + "Lib1" + File.separator + FILE_NAMES[0]), new File(LIBRARY_PATH + File.separator + "Lib1" + File.separator + FILE_NAMES[1]),
                new File(LIBRARY_PATH + File.separator + "Lib1" + File.separator + FILE_NAMES[2]), new File(LIBRARY_PATH + File.separator + "Lib1" + File.separator + FILE_NAMES[3]), new File(LIBRARY_PATH + File.separator + "Lib1" + File.separator + FILE_NAMES[4]));
        List<File> expectedFiles = Arrays.asList(inputFiles.get(0), inputFiles.get(1), inputFiles.get(2), inputFiles.get(3), inputFiles.get(4));
        List<File> actualFiles = library.arrangeFiles(inputFiles.toArray(new File[inputFiles.size()]));
        assertSame(expectedFiles, actualFiles);
    }

    @Test
    public void testLoadDirectory() throws IOException {
        File dictionaryDir = new File(LIBRARY_PATH + File.separator + "Lib1");
        when(MainParser.parse(ArgumentMatchers.any(DictionaryInfo.class), ArgumentMatchers.any(File.class))).thenReturn(dictionary);
        library.loadDirectory(arrangedFiles, DICTIONARY_VERSION);
        assertNotNull(dictionary);
        assertEquals(DICTIONARY_VERSION, dictionary.getVersion());
        assertTrue(library.dictionaries.containsKey(DICTIONARY_VERSION));
    }

    @Test
    public void testGetDictionaries() {
        library.loadFromDirectory(LIBRARY_PATH);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        library.loadFromDirectory(LIBRARY_PATH);
        DictionaryInfo dictionaryInfo = library.getDictionary(DICTIONARY_VERSION);
        assertNotNull(dictionaryInfo);
        assertEquals(DICTIONARY_VERSION, dictionaryInfo.getVersion());
    }
}