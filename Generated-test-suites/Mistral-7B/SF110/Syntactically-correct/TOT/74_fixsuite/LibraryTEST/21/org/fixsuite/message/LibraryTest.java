
package org.fixsuite.message;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class LibraryTest {

    private static final String LIBRARY_PATH = "path/to/valid/directory";
    private Library library;
    private List<File> arrangedFiles;
    private Logger logger;

    @Before
    public void setup() {
        this.library = new Library();
        this.logger = Mockito.mock(Logger.class);
        Library.logger = this.logger;
        this.arrangedFiles = Arrays.asList(new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml"));
    }

    @Test
    public void testLoadFromDirectory() {
        // Given
        Mockito.when(logger.debug(anyString())).thenReturn(library);
        Mockito.when(logger.error(anyString())).thenReturn(library);
        Mockito.when(logger.info(anyString())).thenReturn(library);

        // When
        assertTrue(library.loadFromDirectory(LIBRARY_PATH));

        // Then
        assertEquals(5, library.getDictionaries().size());
        assertNotNull(library.getDictionaries().get(0).getVersion());
        assertNull(library.getDictionary("non-existing-version"));
        library.loadFromDirectory(LIBRARY_PATH); // Test clearing existing dictionaries
        assertEquals(5, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        library.loadFromDirectory("path/to/valid/directory");

        List<DictionaryInfo> dictionaries = library.getDictionaries();
        int size = dictionaries.size();

        // Check if the method returns all the loaded dictionaries
        for (int i = 0; i < size; i++) {
            DictionaryInfo dictionaryInfo = dictionaries.get(i);
            assertNotNull(dictionaryInfo);
        }
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        library.loadFromDirectory("path/to/valid/directory");

        DictionaryInfo dictionary = library.getDictionary("some-existing-version");
        assertNotNull(dictionary);
        assertEquals("some-existing-version", dictionary.getVersion());
    }

    @Test
    public void testLoadFromDirectory_EmptyDirectory() {
        // Given
        Library library = new Library();

        // When
        assertFalse(library.loadFromDirectory("path/to/empty/directory"));

        // Then
        assertTrue(library.getDictionaries().isEmpty());
    }

    @Test
    public void testLoadFromDirectory_InvalidPath() {
        // Given
        Library library = new Library();

        // When
        assertFalse(library.loadFromDirectory("path/to/non-existent/directory"));

        // Then
        assertTrue(library.getDictionaries().isEmpty());
    }
}