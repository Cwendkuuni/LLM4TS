package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
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
    public void testLoadFromDirectoryEmptyDirectory() {
        Library library = new Library();
        String emptyDirectoryPath = System.getProperty("user.dir");
        boolean result = library.loadFromDirectory(emptyDirectoryPath);
        assertTrue(result);
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectoryNonExistentDirectory() {
        Library library = new Library();
        String nonExistentDirectoryPath = "/nonExistentDirectory";
        boolean result = library.loadFromDirectory(nonExistentDirectoryPath);
        assertFalse(result);
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectoryInvalidDirectory() {
        Library library = new Library();
        String invalidDirectoryPath = "invalidDirectoryPath";
        boolean result = library.loadFromDirectory(invalidDirectoryPath);
        assertFalse(result);
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectoryInvalidFileInDirectory() {
        Library library = new Library();
        File testDirectory = new File(System.getProperty("user.dir") + File.separator + "testData" + File.separator + "invalidDirectory");
        testDirectory.mkdirs();
        File invalidFile = new File(testDirectory.getAbsolutePath() + File.separator + "invalidFile.txt");
        boolean result = library.loadFromDirectory(testDirectory.getAbsolutePath());
        assertFalse(result);
        testDirectory.delete();
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectoryValidDirectory() throws IOException {
        Library library = new Library();
        String validDirectoryPath = System.getProperty("user.dir") + File.separator + "testData" + File.separator + "validDirectory";
        File testDirectory = new File(validDirectoryPath);
        testDirectory.mkdirs();

        // Create test files
        File fieldsFile = new File(testDirectory.getAbsolutePath() + File.separator + "Fields.xml");
        FileUtils.writeStringToFile(fieldsFile, FileUtils.readFileToString(new File("src/test/resources/Fields.xml")), null);
        File enumsFile = new File(testDirectory.getAbsolutePath() + File.separator + "Enums.xml");
        FileUtils.writeStringToFile(enumsFile, FileUtils.readFileToString(new File("src/test/resources/Enums.xml")), null);
        File componentsFile = new File(testDirectory.getAbsolutePath() + File.separator + "Components.xml");
        FileUtils.writeStringToFile(componentsFile, FileUtils.readFileToString(new File("src/test/resources/Components.xml")), null);
        File msgTypeFile = new File(testDirectory.getAbsolutePath() + File.separator + "MsgType.xml");
        FileUtils.writeStringToFile(msgTypeFile, FileUtils.readFileToString(new File("src/test/resources/MsgType.xml")), null);
        File msgContentsFile = new File(testDirectory.getAbsolutePath() + File.separator + "MsgContents.xml");
        FileUtils.writeStringToFile(msgContentsFile, FileUtils.readFileToString(new File("src/test/resources/MsgContents.xml")), null);

        boolean result = library.loadFromDirectory(validDirectoryPath);
        assertTrue(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(5, dictionaries.size());
        for (DictionaryInfo dictionary : dictionaries) {
            assertNotNull(dictionary);
            assertTrue(dictionary.isLoaded());
        }

        testDirectory.delete();
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertTrue(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        String version = "1.0";
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNull(dictionary);
    }

    @Test
    public void testLoadDirectoryWithFiles() {
        Library library = new Library();
        File testDirectory = new File(System.getProperty("user.dir") + File.separator + "testData" + File.separator + "validDirectoryWithFiles");
        testDirectory.mkdirs();

        // Create test files
        File fieldsFile = new File(testDirectory.getAbsolutePath() + File.separator + "Fields.xml");
        FileUtils.writeStringToFile(fieldsFile, FileUtils.readFileToString(new File("src/test/resources/Fields.xml")), null);
        File enumsFile = new File(testDirectory.getAbsolutePath() + File.separator + "Enums.xml");
        FileUtils.writeStringToFile(enumsFile, FileUtils.readFileToString(new File("src/test/resources/Enums.xml")), null);
        File componentsFile = new File(testDirectory.getAbsolutePath() + File.separator + "Components.xml");
        FileUtils.writeStringToFile(componentsFile, FileUtils.readFileToString(new File("src/test/resources/Components.xml")), null);
        File msgTypeFile = new File(testDirectory.getAbsolutePath() + File.separator + "MsgType.xml");
        FileUtils.writeStringToFile(msgTypeFile, FileUtils.readFileToString(new File("src/test/resources/MsgType.xml")), null);
        File msgContentsFile = new File(testDirectory.getAbsolutePath() + File.separator + "MsgContents.xml");
        FileUtils.writeStringToFile(msgContentsFile, FileUtils.readFileToString(new File("src/test/resources/MsgContents.xml")), null);

        boolean result = library.loadFromDirectory(testDirectory.getAbsolutePath());
        assertTrue(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(5, dictionaries.size());
        for (DictionaryInfo dictionary : dictionaries) {
            assertNotNull(dictionary);
            assertTrue(dictionary.isLoaded());
        }

        testDirectory.delete();
    }

    @Test
    public void testParseDictionaryFailure() {
        Library library = new Library();
        File testDirectory = new File(System.getProperty("user.dir") + File.separator + "testData" + File.separator + "invalidDirectory");
        testDirectory.mkdirs();

        // Create invalid file
        File invalidFile = new File(testDirectory.getAbsolutePath() + File.separator + "Fields.xml");
        FileUtils.writeStringToFile(invalidFile, "Invalid XML", null);

        boolean result = library.loadFromDirectory(testDirectory.getAbsolutePath());
        assertFalse(result);
        testDirectory.delete();
    }

    @Test
    public void testArrangeFiles() {
        Library library = new Library();
        File[] files = {
            new File("Fields.xml"),
            new File("Enums.xml"),
            new File("Components.xml"),
            new File("MsgType.xml"),
            new File("MsgContents.xml"),
            new File("InvalidFile.txt")
        };
        List<File> arrangedFiles = library.arrangeFiles(files);
        List<File> expectedFiles = new ArrayList<>(Collections.singletonList(new File("Fields.xml"))) {
            {
                add(new File("Enums.xml"));
                add(new File("Components.xml"));
                add(new File("MsgType.xml"));
                add(new File("MsgContents.xml"));
            }
        };
        assertEquals(expectedFiles, arrangedFiles);
    }

    static {
        Logger.getLogger(LibraryTest.class).addAppender(LoggerFactory.getConsoleAppender());
    }
}