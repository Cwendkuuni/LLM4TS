package org.fixsuite.message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LibraryTest {

    private Library library;
    private List<File> arrangedFiles;
    private DictionaryInfo dictionary;
    private List<File> files;

    @Before
    public void setUp() {
        library = new Library();
        arrangedFiles = new ArrayList<File>();
        files = new ArrayList<File>();
        dictionary = mock(DictionaryInfo.class);
    }

    @Test
    public void testLoadFromDirectory_validDirectory_loadsDictionaries() {
        File directory = Mockito.mock(File.class);
        when(directory.isDirectory()).thenReturn(true);
        when(directory.listFiles()).thenReturn(files.toArray(new File[0]));

        library.loadFromDirectory("/path/to/directory");

        verify(library, times(1)).loadDirectory(eq(files), eq("/path/to/directory"));
    }

    @Test
    public void testLoadFromDirectory_emptyDirectory_noDictionariesLoaded() {
        File directory = Mockito.mock(File.class);
        when(directory.isDirectory()).thenReturn(true);
        when(directory.listFiles()).thenReturn(new File[0]);

        library.loadFromDirectory("/path/to/empty/directory");

        assertFalse(library.getDictionaries().isEmpty());
    }

    @Test
    public void testLoadFromDirectory_invalidPath_noDictionariesLoaded() {
        File invalidPath = Mockito.mock(File.class);
        when(invalidPath.isDirectory()).thenReturn(false);

        library.loadFromDirectory(invalidPath.getPath());

        assertFalse(library.getDictionaries().isEmpty());
    }

    @Test
    public void testLoadFromDirectory_validFiles_loadsDictionaries() {
        File fieldsXml = Mockito.mock(File.class);
        when(fieldsXml.getName()).thenReturn("Fields.xml");
        File enumsXml = Mockito.mock(File.class);
        when(enumsXml.getName()).thenReturn("Enums.xml");
        File componentsXml = Mockito.mock(File.class);
        when(componentsXml.getName()).thenReturn("Components.xml");
        File msgTypeXml = Mockito.mock(File.class);
        when(msgTypeXml.getName()).thenReturn("MsgType.xml");
        File msgContentsXml = Mockito.mock(File.class);
        when(msgContentsXml.getName()).thenReturn("MsgContents.xml");

        arrangedFiles.add(fieldsXml);
        arrangedFiles.add(enumsXml);
        arrangedFiles.add(componentsXml);
        arrangedFiles.add(msgTypeXml);
        arrangedFiles.add(msgContentsXml);

        when(files[0].getName()).thenReturn("Fields.xml");
        when(files[1].getName()).thenReturn("Enums.xml");
        when(files[2].getName()).thenReturn("Components.xml");
        when(files[3].getName()).thenReturn("MsgType.xml");
        when(files[4].getName()).thenReturn("MsgContents.xml");

        doReturn(arrangedFiles).when(library).arrangeFiles(files);

        library.loadFromDirectory("/path/to/directory");

        verify(library, times(1)).loadDirectory(eq(arrangedFiles), eq("/path/to/directory"));
        verify(dictionary, times(5)).parse(any(DictionaryInfo.class), any(File.class));
        verify(library, times(1)).dictionaries.put(eq("version"), eq(dictionary));
    }

    @Test
    public void testGetDictionaries_loadedDictionaries_returnsDictionaryList() {
        DictionaryInfo dictionary1 = new DictionaryInfo("version1");
        DictionaryInfo dictionary2 = new DictionaryInfo("version2");
        when(dictionary1.isLoaded()).thenReturn(true);
        when(dictionary2.isLoaded()).thenReturn(true);
        library.dictionaries.put("version1", dictionary1);
        library.dictionaries.put("version2", dictionary2);

        List<DictionaryInfo> dictionaries = library.getDictionaries();

        assertEquals(2, dictionaries.size());
        assertTrue(dictionaries.contains(dictionary1));
        assertTrue(dictionaries.contains(dictionary2));
    }

    @Test
    public void testGetDictionary_loadedDictionary_returnsDictionaryInfo() {
        DictionaryInfo dictionary = new DictionaryInfo("version");
        library.dictionaries.put("version", dictionary);

        DictionaryInfo retrievedDictionary = library.getDictionary("version");

        assertEquals(dictionary, retrievedDictionary);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDictionary_nullVersion_throwsNullPointerException() {
        library.getDictionary(null);
    }

    @Test
    public void testGetDictionary_unloadedDictionary_returnsNull() {
        DictionaryInfo dictionary = new DictionaryInfo("version");
        library.dictionaries.put("version", dictionary);

        DictionaryInfo retrievedDictionary = library.getDictionary("invalidVersion");

        assertNull(retrievedDictionary);
    }
}