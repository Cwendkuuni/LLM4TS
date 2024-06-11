package org.fixsuite.message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.*;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

public class LibraryTest {

    private Library library;
    private Logger logger;

    @Test
    public void testLoadFromDirectory_whenDirectoryIsEmpty_thenReturnFalse() {
        String libraryPath = "";
        library = new Library();
        assertFalse(library.loadFromDirectory(libraryPath));
    }

    @Test
    public void testLoadFromDirectory_whenDirectoryIsNotEmpty_thenReturnTrue() {
        String libraryPath = "/path/to/library";
        library = new Library();

        MockitoAnnotations.initMocks(this);

        File path = mock(File.class);
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[0]);

        List<File> files = library.arrangeFiles(path.listFiles());
        library.loadDirectory(files, "version");

        verify(logger, times(1)).debug("Unloading previous dictionaries");
        verify(logger, times(1)).debug("Supplied path is not a directory.");
        verify(logger, never()).debug("Unable to load dictionary from version.");
        verify(logger, never()).info("Loading time: 0 milliseconds.");

        assertTrue(library.loadFromDirectory(libraryPath));
    }

    @Test
    public void testLoadFromDirectory_whenDirectoryIsNotEmptyAndDictionaryIsLoaded_thenAddDictionaryToMap() {
        String libraryPath = "/path/to/library";
        library = new Library();

        MockitoAnnotations.initMocks(this);

        File path = mock(File.class);
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[1]);
        File file = mock(File.class);
        when(file.getName()).thenReturn("Fields.xml");

        List<File> files = library.arrangeFiles(path.listFiles());
        File arrangedFile = files.get(0);
        when(arrangedFile.getName()).thenReturn("Fields.xml");
        DictionaryInfo dictionary = mock(DictionaryInfo.class);
        when(dictionary.isLoaded()).thenReturn(true);
        when(dictionary.getVersion()).thenReturn("version");

        library.loadDirectory(files, "version");

        verify(logger, times(1)).debug("Unloading previous dictionaries");
        verify(logger, times(1)).debug("Loaded 1 FIX dictionaries.");
        verify(logger, times(1)).info("Loading time: 0 milliseconds.");
        verify(logger, never()).error("Unable to load any dictionaries");
        verify(logger, never()).error("Supplied directory is empty.");

        assertEquals("version", library.getDictionary("version").getVersion());
    }

    @Test
    public void testLoadFromDirectory_whenDirectoryIsNotEmptyAndDictionaryIsNotLoaded_thenDoNotAddDictionaryToMap() {
        String libraryPath = "/path/to/library";
        library = new Library();

        MockitoAnnotations.initMocks(this);

        File path = mock(File.class);
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[1]);
        File file = mock(File.class);
        when(file.getName()).thenReturn("Fields.xml");
        DictionaryInfo dictionary = mock(DictionaryInfo.class);
        when(dictionary.isLoaded()).thenReturn(false);

        List<File> files = library.arrangeFiles(path.listFiles());
        library.loadDirectory(files, "version");

        verify(logger, times(1)).debug("Unloading previous dictionaries");
        verify(logger, times(1)).debug("Unable to load dictionary from version.");
        verify(logger, times(0)).debug("Loaded 1 FIX dictionaries.");
        verify(logger, times(0)).info("Loading time: 0 milliseconds.");
        verify(logger, never()).error("Unable to load any dictionaries");
        verify(logger, never()).error("Supplied directory is empty.");

        assertNull(library.getDictionary("version"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectory_whenLibraryIsAlreadyLoaded_thenThrowIllegalArgumentException() {
        String libraryPath = "/path/to/library";
        library = new Library();
        library.dictionaries = new TreeMap<>();
        library.dictionaries.put("version", new DictionaryInfo("version"));

        library.loadFromDirectory(libraryPath);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.loadFromDirectory(libraryPath));
        assertEquals("Library is already loaded.", exception.getMessage());
    }

    @Test
    public void testGetDictionaries_whenLibraryIsLoaded_thenReturnListOfDictionaries() {
        String libraryPath = "/path/to/library";
        library = new Library();

        MockitoAnnotations.initMocks(this);

        File path = mock(File.class);
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[1]);
        File file = mock(File.class);
        when(file.getName()).thenReturn("Fields.xml");
        DictionaryInfo dictionary = mock(DictionaryInfo.class);
        when(dictionary.isLoaded()).thenReturn(true);
        when(dictionary.getVersion()).thenReturn("version");

        List<File> files = library.arrangeFiles(path.listFiles());
        library.loadDirectory(files, "version");

        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(1, dictionaries.size());
        assertEquals("version", dictionaries.get(0).getVersion());
    }

    @Test
    public void testGetDictionary_whenLibraryIsLoadedAndDictionaryExists_thenReturnDictionary() {
        String libraryPath = "/path/to/library";
        library = new Library();

        MockitoAnnotations.initMocks(this);

        File path = mock(File.class);
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[1]);
        File file = mock(File.class);
        when(file.getName()).thenReturn("Fields.xml");
        DictionaryInfo dictionary = mock(DictionaryInfo.class);
        when(dictionary.isLoaded()).thenReturn(true);
        when(dictionary.getVersion()).thenReturn("version");

        List<File> files = library.arrangeFiles(path.listFiles());
        library.loadDirectory(files, "version");

        DictionaryInfo loadedDictionary = library.getDictionary("version");
        assertEquals("version", loadedDictionary.getVersion());
    }

    @Test
    public void testGetDictionary_whenLibraryIsLoadedButDictionaryDoesNotExist_thenReturnNull() {
        String libraryPath = "/path/to/library";
        library = new Library();

        MockitoAnnotations.initMocks(this);

        File path = mock(File.class);
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[1]);
        File file = mock(File.class);
        when(file.getName()).thenReturn("Fields.xml");
        DictionaryInfo dictionary = mock(DictionaryInfo.class);
        when(dictionary.isLoaded()).thenReturn(true);
        when(dictionary.getVersion()).thenReturn("version1");

        List<File> files = library.arrangeFiles(path.listFiles());
        library.loadDirectory(files, "version1");

        DictionaryInfo loadedDictionary = library.getDictionary("version2");
        assertNull(loadedDictionary);
    }

    @Test
    public void testGetDictionary_whenLibraryIsNotLoaded_thenReturnNull() {
        String libraryPath = "/path/to/library";
        library = new Library();

        List<File> files = new ArrayList<>();
        files.add(new File("Fields.xml"));
        files.add(new File("Enums.xml"));

        library.loadDirectory(files, "version");

        DictionaryInfo loadedDictionary = library.getDictionary("version2");
        assertNull(loadedDictionary);
    }

    @Test
    public void testArrangeFiles_whenFilesArePresent_thenReturnListOfFiles() throws IOException {
        String libraryPath = "/path/to/library";
        library = new Library();

        File path = new File(libraryPath);
        File[] files = path.listFiles();

        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(5, arrangedFiles.size());

        for (int i = 0; i < 5; i++) {
            String fileName = "Fields" + (i + 1) + ".xml";
            File expectedFile = files[i];
            File actualFile = arrangedFiles.get(i);
            assertEquals(fileName, actualFile.getName());
            assertEquals(expectedFile, actualFile);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testArrangeFiles_whenFilesAreNull_thenThrowNullPointerException() {
        String libraryPath = "/path/to/library";
        library = new Library();

        List<File> files = null;
        List<File> arrangedFiles = library.arrangeFiles(files);

        IllegalArgumentException exception = assertThrows(NullPointerException.class, () -> library.arrangeFiles(files));
        assertEquals("files is marked non-null but is null", exception.getMessage());
    }

    @Before
    public void setUp() {
        logger = LoggerFactory.getLogger(Library.class);
    }
}