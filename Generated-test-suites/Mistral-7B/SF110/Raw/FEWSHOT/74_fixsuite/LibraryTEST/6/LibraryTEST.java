package org.fixsuite.message;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

    private static final String LIBRARY_PATH = "test_data/fixsuite/message"; // Replace with the actual path to your test data directory

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory(LIBRARY_PATH);
        assertTrue(result);
    }

    @Test
    public void testLoadFromDirectoryEmptyDirectory() {
        Library library = new Library();
        String emptyDirectoryPath = "test_data/fixsuite/message/empty"; // Replace with the actual path to an empty directory
        boolean result = library.loadFromDirectory(emptyDirectoryPath);
        assertFalse(result);
    }

    @Test
    public void testLoadFromDirectoryInvalidPath() {
        Library library = new Library();
        String invalidPath = "test_data/fixsuite/message/invalid"; // Replace with the actual path to an invalid directory
        boolean result = library.loadFromDirectory(invalidPath);
        assertFalse(result);
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
        DictionaryInfo dictionary = library.getDictionary("FIX 5.0 SP2");
        assertNotNull(dictionary);
    }

    @Test
    public void testGetDictionaryNullVersion() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        DictionaryInfo dictionary = library.getDictionary(null);
        assertNull(dictionary);
    }

    @Test
    public void testGetDictionaryInvalidVersion() {
        Library library = new Library();
        library.loadFromDirectory(LIBRARY_PATH);
        DictionaryInfo dictionary = library.getDictionary("INVALID_VERSION");
        assertNull(dictionary);
    }

    @Test
    public void testParseFile() throws IOException {
        Library library = new Library();
        File fieldsFile = new File("test_data/fixsuite/message/Fields.xml");
        File enumsFile = new File("test_data/fixsuite/message/Enums.xml");
        List<File> files = Collections.singletonList(new File("test_data/fixsuite/message/test_file.xml"));

        library.loadDirectory(library.arrangeFiles(files.toArray(new File[0])), "TEST_VERSION");

        DictionaryInfo dictionary = library.getDictionary("TEST_VERSION");
        assertNotNull(dictionary);

        String fieldName = "TestField";
        FieldInfo field = dictionary.getFieldInfo(fieldName);
        assertNotNull(field);
        assertEquals(fieldName, field.getName());

        EnumInfo enumInfo = dictionary.getEnumInfo("TestEnum");
        assertNotNull(enumInfo);
        assertEquals("TestEnum", enumInfo.getName());
    }

    @Test
    public void testParseFilesWithInvalidFile() throws IOException {
        Library library = new Library();
        File[] files = {new File("test_data/fixsuite/message/Fields.xml"), new File("test_data/fixsuite/message/invalidFile.xml"), new File("test_data/fixsuite/message/Enums.xml")};

        library.loadDirectory(Arrays.asList(files), "TEST_VERSION");

        DictionaryInfo dictionary = library.getDictionary("TEST_VERSION");
        assertNotNull(dictionary);

        List<String> errors = library.getLoadErrors();
        assertFalse(errors.isEmpty());
        assertEquals(1, errors.size());
        String error = errors.get(0);
        assertTrue(error.contains("failed to parse"));
    }
}