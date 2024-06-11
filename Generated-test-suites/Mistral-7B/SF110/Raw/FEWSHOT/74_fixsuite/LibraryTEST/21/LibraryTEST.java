package org.fixsuite.message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.*;
import org.junit.Test;
import org.mockito.Mockito;

public class LibraryTest {

    @Test
    public void testLoadFromDirectory_ValidDirectoryWithFiles_ReturnsTrueAndLoadsDictionaries() {
        String libraryPath = "test/resources/validLibrary";
        Library library = new Library();

        File path = Mockito.mock(File.class);
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[] { new File("Fields.xml"), new File("Components.xml"), new File("Enums.xml"), new File("MsgType.xml"), new File("MsgContents.xml") });

        Library.logger = Mockito.mock(Logger.class);

        library.loadFromDirectory(libraryPath);

        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(5, dictionaries.size());
        assertTrue(dictionaries.get(0) instanceof FieldsDictionaryInfo);
        assertTrue(dictionaries.get(1) instanceof ComponentsDictionaryInfo);
        assertTrue(dictionaries.get(2) instanceof EnumsDictionaryInfo);
        assertTrue(dictionaries.get(3) instanceof MsgTypeDictionaryInfo);
        assertTrue(dictionaries.get(4) instanceof MsgContentsDictionaryInfo);

        verify(Library.logger, times(1)).debug("Loading time: " + anyInt() + " milliseconds.");
        verify(Library.logger, times(1)).info("Loaded " + anyInt() + " FIX dictionaries.");
    }

    @Test
    public void testLoadFromDirectory_EmptyDirectory_ReturnsFalse() {
        String libraryPath = "test/resources/emptyLibrary";
        Library library = new Library();

        File path = Mockito.mock(File.class);
        when(path.isDirectory()).thenReturn(true);
        when(path.listFiles()).thenReturn(new File[0]);

        Library.logger = Mockito.mock(Logger.class);

        assertFalse(library.loadFromDirectory(libraryPath));
        verify(Library.logger, times(1)).error("Supplied directory is empty.");
    }

    @Test
    public void testLoadFromDirectory_InvalidPath_ReturnsFalse() {
        String libraryPath = "test/resources/invalidPath";
        Library library = new Library();

        File path = Mockito.mock(File.class);
        when(path.isDirectory()).thenReturn(false);

        Library.logger = Mockito.mock(Logger.class);

        assertFalse(library.loadFromDirectory(libraryPath));
        verify(Library.logger, times(1)).error("Supplied path is not a directory.");
    }

    @Test(expected = NullPointerException.class)
    public void testLoadFromDirectory_NullDictionaries_ThrowsNullPointerException() {
        String libraryPath = "test/resources/validLibrary";
        Library library = new Library();

        Library.logger = Mockito.mock(Logger.class);

        library.dictionaries = null;
        library.loadFromDirectory(libraryPath);
    }

    @Test
    public void testGetDictionaries_LoadedDictionaries_ReturnsNonEmptyList() {
        Library library = new Library();
        library.loadFromDirectory("test/resources/validLibrary");

        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary_ValidVersion_ReturnsDictionaryInfo() {
        Library library = new Library();
        library.loadFromDirectory("test/resources/validLibrary");

        DictionaryInfo dictionary = library.getDictionary("4.4");
        assertNotNull(dictionary);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDictionary_NullVersion_ThrowsNullPointerException() {
        Library library = new Library();
        library.loadFromDirectory("test/resources/validLibrary");

        library.getDictionary(null);
    }

    @Test
    public void testGetDictionary_InvalidVersion_ReturnsNull() {
        Library library = new Library();
        library.loadFromDirectory("test/resources/validLibrary");

        DictionaryInfo dictionary = library.getDictionary("5.0");
        assertNull(dictionary);
    }
}