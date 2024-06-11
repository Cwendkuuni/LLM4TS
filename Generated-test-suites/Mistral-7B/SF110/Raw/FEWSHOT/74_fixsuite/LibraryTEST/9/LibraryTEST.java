package org.fixsuite.message;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

    private static final String LIBRARY_PATH = "test/resources/libraries";

    @Test
    public void testLibraryInstance() {
        Library lib = new Library();
        assertNotNull(lib);
    }

    @Test
    public void testLoadFromDirectorySuccess() throws IOException {
        Library lib = new Library();
        boolean result = lib.loadFromDirectory(LIBRARY_PATH);
        assertTrue(result);
    }

    @Test
    public void testLoadFromDirectoryFailureEmptyDirectory() throws IOException {
        Library lib = new Library();
        String emptyDirPath = "test/resources/empty_library";
        boolean result = lib.loadFromDirectory(emptyDirPath);
        assertFalse(result);
    }

    @Test
    public void testLoadFromDirectoryFailureInvalidPath() throws IOException {
        Library lib = new Library();
        String invalidPath = "test/resources/libraries_invalid";
        boolean result = lib.loadFromDirectory(invalidPath);
        assertFalse(result);
    }

    @Test
    public void testGetDictionariesSuccess() throws IOException {
        Library lib = new Library();
        lib.loadFromDirectory(LIBRARY_PATH);
        List<DictionaryInfo> dicts = lib.getDictionaries();
        assertNotNull(dicts);
        assertFalse(dicts.isEmpty());
    }

    @Test
    public void testGetDictionarySuccess() throws IOException {
        Library lib = new Library();
        lib.loadFromDirectory(LIBRARY_PATH);
        String version = "FIX 5.0";
        DictionaryInfo dict = lib.getDictionary(version);
        assertNotNull(dict);
        assertEquals(version, dict.getVersion());
    }

    @Test(expected = NullPointerException.class)
    public void testGetDictionaryFailureNullVersion() {
        Library lib = new Library();
        String nullVersion = null;
        DictionaryInfo dict = lib.getDictionary(nullVersion);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDictionaryFailureNullLibrary() {
        Library nullLib = null;
        String version = "FIX 5.0";
        DictionaryInfo dict = nullLib.getDictionary(version);
    }

    @Test
    public void testLoadDirectorySuccess() throws IOException {
        Library lib = new Library();
        File testDir = new File(LIBRARY_PATH);
        List<File> files = Arrays.asList(testDir.listFiles());
        lib.loadDirectory(files, "test_version");
        List<DictionaryInfo> dicts = lib.getDictionaries();
        assertFalse(dicts.isEmpty());
    }

    @Test
    public void testLoadDirectoryFailureEmptyDirectory() throws IOException {
        Library lib = new Library();
        String emptyDirPath = "test/resources/empty_library";
        File testDir = new File(emptyDirPath);
        List<File> files = Arrays.asList(testDir.listFiles());
        lib.loadDirectory(files, "test_version");
        List<DictionaryInfo> dicts = lib.getDictionaries();
        assertTrue(dicts.isEmpty());
    }

    @Test
    public void testArrangeFilesCorrectOrder() throws IOException {
        Library lib = new Library();
        File testDir = new File(LIBRARY_PATH);
        List<File> files = Arrays.asList(testDir.listFiles());
        List<File> arrangedFiles = lib.arrangeFiles(files.toArray(new File[0]));
        List<Integer> keys = Arrays.asList(Library.FIELDS_FILE_KEY, Library.ENUMS_FILE_KEY,
                Library.COMPONENTS_FILE_KEY, Library.MSG_TYPE_FILE_KEY, Library.MSG_CONTENTS_FILE_KEY);
        for (int i = 0; i < arrangedFiles.size(); i++) {
            File file = arrangedFiles.get(i);
            String name = file.getName();
            int key = keys.get(i);
            assertEquals(name, Arrays.asList(Library.FIELDS_FILE_NAME, Library.ENUMS_FILE_NAME, Library.COMPONENTS_FILE_NAME,
                    Library.MSG_TYPE_FILE_NAME, Library.MSG_CONTENTS_FILE_NAME).get(i));
            assertEquals(key, Library.FIELDS_FILE_KEY + i);
        }
    }

    @Test
    public void testArrangeFilesNullFiles() {
        Library lib = new Library();
        List<File> files = null;
        try {
            lib.arrangeFiles(files);
            fail();
        } catch (NullPointerException e) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrangeFilesInvalidFiles() {
        Library lib = new Library();
        List<File> files = Arrays.asList(new File("test/resources/libraries/Fields.xml"), new File("test/resources/libraries/Components.xml"),
                new File("test/resources/libraries/MsgContents.xml"), new File("test/resources/libraries/MsgType.xml"),
                new File("test/resources/libraries/Fields.xml"));
        lib.arrangeFiles(files);
    }

    @Test
    public void testParseSuccess() throws IOException {
        Library lib = new Library();
        File testFile = new File("test/resources/libraries/Fields.xml");
        List<File> files = Arrays.asList(testFile);
        lib.loadDirectory(files, "test_version");
        DictionaryInfo dict = lib.getDictionary("test_version");
        assertNotNull(dict);
        assertTrue(dict.isLoaded());
    }

    @Test
    public void testParseFailureEmptyFile() throws IOException {
        Library lib = new Library();
        File testFile = new File("test/resources/libraries/empty_Fields.xml");
        List<File> files = Arrays.asList(testFile);
        lib.loadDirectory(files, "test_version");
        DictionaryInfo dict = lib.getDictionary("test_version");
        assertNull(dict);
    }

    @Test
    public void testParseFailureInvalidXml() throws IOException {
        Library lib = new Library();
        File testFile = new File("test/resources/libraries/invalid_Fields.xml");
        List<File> files = Arrays.asList(testFile);
        lib.loadDirectory(files, "test_version");
        DictionaryInfo dict = lib.getDictionary("test_version");
        assertNull(dict);
    }

    @Test
    public void testLogger() {
        Logger logger = LoggerFactory.getLogger(Library.class);
        assertNotNull(logger);
    }

    @Test
    public void testMemoryAllocation() throws IOException {
        Library lib = new Library();
        boolean result = lib.loadFromDirectory(LIBRARY_PATH);
        assertTrue(result);
        long startMemory = Runtime.getRuntime().freeMemory();
        lib = null;
        System.gc();
        long endMemory = Runtime.getRuntime().freeMemory();
        long memory = endMemory - startMemory;
        assertTrue(memory > 0);
    }

    @Test
    public void testLoadingTime() throws IOException {
        Library lib = new Library();
        boolean result = lib.loadFromDirectory(LIBRARY_PATH);
        assertTrue(result);
        long startTime = System.currentTimeMillis();
        lib = null;
        System.gc();
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        assertTrue(time > 0);
    }

}