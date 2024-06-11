package org.fixsuite.message;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertLinesMatch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
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
    public void testEmptyLibrary() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertTrue(dictionaries.isEmpty());
    }

    @Test
    public void testLoadingEmptyDirectory() {
        Library library = new Library();
        String libraryPath = "/empty";
        assertFalse(library.loadFromDirectory(libraryPath));
    }

    @Test
    public void testLoadingInvalidDirectory() {
        Library library = new Library();
        String libraryPath = "/notADirectory";
        assertFalse(library.loadFromDirectory(libraryPath));
    }

    @Test
    public void testLoadingDictionaryFilesInCorrectOrder() throws IOException {
        Library library = new Library();
        String libraryPath = "/testData";
        File path = new File(libraryPath);
        File[] files = path.listFiles();
        List<File> arrangedFiles = library.arrangeFiles(files);
        library.loadFromDirectory(Collections.singletonList(path), "Test");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        DictionaryInfo dictionary = dictionaries.get(0);
        assertNotNull(dictionary);
        assertEquals("Test", dictionary.getVersion());
        File fieldsFile = arrangedFiles.get(FIELDS_FILE_KEY);
        File enumsFile = arrangedFiles.get(ENUMS_FILE_KEY);
        File componentsFile = arrangedFiles.get(COMPONENTS_FILE_KEY);
        File msgTypeFile = arrangedFiles.get(MSG_TYPE_FILE_KEY);
        File msgContentsFile = arrangedFiles.get(MSG_CONTENTS_FILE_KEY);
        assertTrue(MainParser.parse(dictionary, fieldsFile).isLoaded());
        assertTrue(MainParser.parse(dictionary, enumsFile).isLoaded());
        assertTrue(MainParser.parse(dictionary, componentsFile).isLoaded());
        assertTrue(MainParser.parse(dictionary, msgTypeFile).isLoaded());
        assertTrue(MainParser.parse(dictionary, msgContentsFile).isLoaded());
    }

    @Test
    public void testLoadingDuplicateFiles() throws IOException {
        Library library = new Library();
        String libraryPath = "/testDataDuplicateFiles";
        File path = new File(libraryPath);
        File[] files = path.listFiles();
        library.loadFromDirectory(Collections.singletonList(path), "Test");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        DictionaryInfo dictionary = dictionaries.get(0);
        assertNotNull(dictionary);
        assertEquals("Test", dictionary.getVersion());
        File fieldsFile = library.arrangeFiles(files)[FIELDS_FILE_KEY];
        File enumsFile = library.arrangeFiles(files)[ENUMS_FILE_KEY];
        File componentsFile = library.arrangeFiles(files)[COMPONENTS_FILE_KEY];
        File msgTypeFile = library.arrangeFiles(files)[MSG_TYPE_FILE_KEY];
        File msgContentsFile = library.arrangeFiles(files)[MSG_CONTENTS_FILE_KEY];
        File duplicateFieldsFile = library.arrangeFiles(files)[FIELDS_FILE_KEY];
        List<File> parseOrder = new ArrayList<>();
        parseOrder.add(fieldsFile);
        parseOrder.add(duplicateFieldsFile);
        for (File file : parseOrder) {
            assertFalse(MainParser.parse(dictionary, file).isLoaded());
        }
    }

    @Test
    public void testLoadingInvalidFiles() throws IOException {
        Library library = new Library();
        String libraryPath = "/testDataInvalidFiles";
        File path = new File(libraryPath);
        File[] files = path.listFiles();
        library.loadFromDirectory(Collections.singletonList(path), "Test");
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        DictionaryInfo dictionary = dictionaries.get(0);
        assertNotNull(dictionary);
        assertEquals("Test", dictionary.getVersion());
        for (File file : files) {
            if (!file.getName().equals("Fields.xml") && !file.getName().equals("Enums.xml")
                    && !file.getName().equals("Components.xml") && !file.getName().equals("MsgType.xml")
                    && !file.getName().equals("MsgContents.xml")) {
                File invalidFile = file;
                library.loadFromDirectory(Collections.singletonList(file), "Test");
                List<DictionaryInfo> dictionariesAfterLoad = library.getDictionaries();
                assertFalse(dictionariesAfterLoad.contains(dictionary));
                assertNull(dictionary.getFields());
                assertNull(dictionary.getEnums());
                assertNull(dictionary.getComponents());
                assertNull(dictionary.getMessageTypes());
                assertNull(dictionary.getMsgContents());
                assertNull(dictionary.getLogger());
            }
        }
    }

    @Test
    public void testLoadingMultipleVersions() throws IOException {
        Library library = new Library();
        String libraryPath = "/testDataMultipleVersions";
        File path = new File(libraryPath);
        library.loadFromDirectory(Collections.singletonList(path), "Test1");
        List<DictionaryInfo> dictionaries1 = library.getDictionaries();
        DictionaryInfo dictionary1 = dictionaries1.get(0);
        library.loadFromDirectory(Collections.singletonList(path), "Test2");
        List<DictionaryInfo> dictionaries2 = library.getDictionaries();
        DictionaryInfo dictionary2 = dictionaries2.get(0);
        assertNotNull(dictionary1);
        assertNotNull(dictionary2);
        assertNotSame(dictionary1, dictionary2);
        assertEquals("Test1", dictionary1.getVersion());
        assertEquals("Test2", dictionary2.getVersion());
        assertLinesMatch(dictionary1.getFields().toString(), dictionary2.getFields().toString());
        assertLinesMatch(dictionary1.getEnums().toString(), dictionary2.getEnums().toString());
        assertLinesMatch(dictionary1.getComponents().toString(), dictionary2.getComponents().toString());
        assertLinesMatch(dictionary1.getMessageTypes().toString(), dictionary2.getMessageTypes().toString());
        assertLinesMatch(dictionary1.getMsgContents().toString(), dictionary2.getMsgContents().toString());
    }

    @Test
    public void testLogger() {
        Library library = new Library();
        Logger logger = LoggerFactory.getLogger(Library.class);
        assertSame(library.logger, logger);
    }

    @Test
    public void testUnloadingPreviousDictionaries() {
        Library library = new Library();
        Map<String, DictionaryInfo> previousDictionaries = library.dictionaries;
        assertFalse(previousDictionaries.isEmpty());
        library.loadFromDirectory("/testData", "Test");
        Map<String, DictionaryInfo> currentDictionaries = library.dictionaries;
        assertTrue(currentDictions.isEmpty());
        assertSame(previousDictionaries, library.dictionaries);
    }
}