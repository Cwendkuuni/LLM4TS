package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class LibraryTest {

    private static final String LIBRARY_PATH = "target/test-classes/library";
    private Logger logger;
    private Library library;
    private List<File> arrangedFiles;
    private List<File> files;

    @Before
    public void setUp() {
        logger = mock(Logger.class);
        library = new Library();
        arrangedFiles = new ArrayList<>();
        File path = new File(LIBRARY_PATH);
        files = Arrays.asList(new File(path, "Fields.xml"), new File(path, "Enums.xml"), new File(path, "Components.xml"),
                new File(path, "MsgType.xml"), new File(path, "MsgContents.xml"));
    }

    @Test
    public void testLoadFromDirectory_whenDirectoryExists_shouldLoadAllFilesAndReturnTrue() {
        when(logger.debug(String.valueOf(String.format("Loading dictionary from %s.", "1")))).thenReturn(library);
        when(logger.debug(String.valueOf(String.format("Loaded %d FIX dictionaries.", 5)))).thenReturn(library);

        library.loadFromDirectory(LIBRARY_PATH);

        assertTrue(library.getDictionaries().size() > 0);
    }

    @Test
    public void testLoadFromDirectory_whenDirectoryDoesNotExist_shouldReturnFalse() {
        when(logger.error(String.valueOf(String.format("Supplied path is not a directory.", "")))).thenReturn(library);

        assertFalse(library.loadFromDirectory(""));
    }

    @Test
    public void testLoadFromDirectory_whenEmptyDirectory_shouldReturnFalse() {
        when(logger.error(String.valueOf(String.format("Supplied directory is empty.", "")))).thenReturn(library);

        assertFalse(library.loadFromDirectory(LIBRARY_PATH));
    }

    @Test
    public void testLoadDirectory_whenFilesAreParsedSuccessfully_shouldAddDictionaryToMap() {
        DictionaryInfo dictionary = mock(DictionaryInfo.class);
        when(dictionary.isLoaded()).thenReturn(true);

        library.loadDirectory(arrangedFiles, "version");

        assertEquals(1, library.dictionaries.size());
        assertEquals("version", library.dictionaries.keySet().iterator().next());
        assertNotNull(library.dictionaries.get("version"));
    }

    @Test
    public void testLoadDirectory_whenFilesAreNotParsedSuccessfully_shouldNotAddDictionaryToMap() {
        DictionaryInfo dictionary = mock(DictionaryInfo.class);
        when(dictionary.isLoaded()).thenReturn(false);

        library.loadDirectory(arrangedFiles, "version");

        assertEquals(0, library.dictionaries.size());
    }

    @Test
    public void testArrangeFiles_whenFilesAreSortedCorrectly_shouldReturnListOfFiles() throws IOException {
        when(logger.debug(String.valueOf(String.format("Loading dictionary from %s.", "1")))).thenReturn(library);

        List<File> result = library.arrangeFiles(files.toArray(new File[0]));

        assertEquals(files.size(), result.size());
        for (int i = 0; i < files.size(); i++) {
            assertEquals(files.get(i).getName(), result.get(i).getName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrangeFiles_whenFilesAreNull_shouldThrowIllegalArgumentException() {
        library.arrangeFiles(null);
    }

    @Test
    public void testGetDictionaries_whenDictionariesAreLoaded_shouldReturnListOfDictionaries() {
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        library.dictionaries.put("version1", dictionary1);
        library.dictionaries.put("version2", dictionary2);

        assertEquals(2, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionary_whenDictionaryIsLoaded_shouldReturnDictionaryInfo() {
        DictionaryInfo dictionary = new DictionaryInfo("version1");
        library.dictionaries.put("version1", dictionary);

        assertEquals(dictionary, library.getDictionary("version1"));
    }

    @Test
    public void testGetDictionary_whenDictionaryIsNotLoaded_shouldReturnNull() {
        library.getDictionary("version2");

        assertNull(library.getDictionary("version2"));
    }
}