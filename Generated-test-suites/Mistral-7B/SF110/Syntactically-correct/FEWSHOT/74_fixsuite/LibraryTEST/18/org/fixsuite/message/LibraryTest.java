package org.fixsuite.message;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertLinesMatch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

    @Test
    public void testLibraryInstance() {
        Library library = new Library();
        assertNotNull(library);
    }

    @Test
    public void testLoadFromDirectoryFalseNoDirectory() {
        Library library = new Library();
        assertFalse(library.loadFromDirectory(null));
    }

    @Test
    public void testLoadFromDirectoryFalseEmptyDirectory() {
        Library library = new Library();
        File directory = new File("src/test/resources/empty");
        assertFalse(library.loadFromDirectory(directory.getAbsolutePath()));
    }

    @Test
    public void testLoadFromDirectoryEmptyDictionaryMap() {
        Library library = new Library();
        File directory = new File("src/test/resources/valid");
        assertTrue(library.loadFromDirectory(directory.getAbsolutePath()));
        assertNotNull(library.getDictionaries());
        assertTrue(library.getDictionaries().isEmpty());
    }

    @Test
    public void testLoadFromDirectoryWithValidDictionaries() throws IOException {
        Library library = new Library();
        File directory = new File("src/test/resources/valid");
        assertTrue(library.loadFromDirectory(directory.getAbsolutePath()));
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        int size = dictionaries.size();
        assertEquals(4, size);

        DictionaryInfo dictionary1 = dictionaries.get(0);
        assertEquals("FIX 4.4", dictionary1.getVersion());

        DictionaryInfo dictionary2 = dictionaries.get(1);
        assertEquals("FIX 5.0SP2", dictionary2.getVersion());

        DictionaryInfo dictionary3 = dictionaries.get(2);
        assertEquals("FIX 5.0", dictionary3.getVersion());

        DictionaryInfo dictionary4 = dictionaries.get(3);
        assertEquals("FIX 4.4", dictionary4.getVersion());
    }

    @Test
    public void testLoadFromDirectoryWithDuplicateDictionaryFiles() throws IOException {
        Library library = new Library();
        File directory = new File("src/test/resources/duplicate");
        assertTrue(library.loadFromDirectory(directory.getAbsolutePath()));
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertNotNull(dictionaries);
        int size = dictionaries.size();
        assertEquals(2, size);

        DictionaryInfo dictionary1 = dictionaries.get(0);
        assertEquals("FIX 5.0", dictionary1.getVersion());

        DictionaryInfo dictionary2 = dictionaries.get(1);
        assertEquals("FIX 5.0", dictionary2.getVersion());
    }

    @Test
    public void testLoadFromDirectoryWithInvalidFiles() throws IOException {
        Library library = new Library();
        File directory = new File("src/test/resources/invalid");
        assertFalse(library.loadFromDirectory(directory.getAbsolutePath()));
        assertNotNull(library.getDictionaries());
        assertTrue(library.getDictionaries().isEmpty());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        Library.logger.debug("Before loading dictionaries");
        assertNull(library.getDictionary("FIX 4.4"));

        File directory = new File("src/test/resources/valid");
        assertTrue(library.loadFromDirectory(directory.getAbsolutePath()));

        DictionaryInfo dictionary = library.getDictionary("FIX 4.4");
        assertNotNull(dictionary);
        assertEquals("FIX 4.4", dictionary.getVersion());
    }

    @Test
    public void testGetDictionaryNullVersion() {
        Library library = new Library();
        assertNull(library.getDictionary(null));
    }

    @Test
    public void testGetDictionaryNullDictionaries() {
        Library library = new Library();
        library.dictionaries = null;
        assertNull(library.getDictionary("FIX 4.4"));
    }

    @Test
    public void testGetDictionaryUnloadedDictionary() {
        Library library = new Library();
        File directory = new File("src/test/resources/valid");
        List<File> files = new ArrayList<>();
        files.add(new File("src/test/resources/valid/Components.xml"));
        library.loadDirectory(files, "FIX 4.4");
        assertNull(library.getDictionary("FIX 5.0"));
    }
}