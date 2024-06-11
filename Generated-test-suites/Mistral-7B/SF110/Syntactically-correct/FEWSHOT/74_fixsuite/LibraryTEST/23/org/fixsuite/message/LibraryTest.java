package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.Library;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {
    private static final String TEST_DIRECTORY = "src/test/resources/library";
    private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);
    private Library library;

    @Test
    public void testLoadFromDirectory() {
        library = new Library();
        boolean result = library.loadFromDirectory(TEST_DIRECTORY);
        assertTrue(result);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(5, dictionaries.size());
        for (DictionaryInfo dictionary : dictionaries) {
            assertNotNull(dictionary);
        }
        logger.info("Loaded dictionaries: {}", library.getDictionaries());
    }

    @Test
    public void testGetDictionary() {
        library = new Library();
        library.loadFromDirectory(TEST_DIRECTORY);
        DictionaryInfo dictionary = library.getDictionary("FIX 4.4");
        assertNotNull(dictionary);
        assertEquals("FIX 4.4", dictionary.getVersion());
    }

    @Test
    public void testLoadDirectoryWithNullVersionedFiles() {
        File testDirectory = new File("src/test/resources/library_null_versioned_files");
        Library library = new Library();
        boolean result = library.loadFromDirectory(testDirectory.getAbsolutePath());
        assertFalse(result);
    }

    @Test
    public void testLoadDirectoryWithEmptyDirectory() {
        File testDirectory = new File("src/test/resources/library_empty_directory");
        Library library = new Library();
        boolean result = library.loadFromDirectory(testDirectory.getAbsolutePath());
        assertFalse(result);
    }

    @Test
    public void testLoadDirectoryWithInvalidFiles() {
        File testDirectory = new File("src/test/resources/library_invalid_files");
        Library library = new Library();
        boolean result = library.loadFromDirectory(testDirectory.getAbsolutePath());
        assertFalse(result);
    }

    @Test
    public void testParseFile() throws IOException {
        File fieldsXml = new File("src/test/resources/library/Fields.xml");
        File dictionaryFile = File.createTempFile("test", ".xml");
        try (Writer writer = new BufferedWriter(new FileWriter(dictionaryFile))) {
            MainParser.parse(null, fieldsXml);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Dictionary version=\"1.0\">\n<Name>Test Dictionary</Name>\n</Dictionary>");
            writer.flush();
        }
        Library library = new Library();
        List<File> arrangedFiles = library.arrangeFiles(new File("src/test/resources/library").listFiles());
        File testFile = arrangedFiles.get(0);
        library.loadDirectory(Arrays.asList(dictionaryFile), "Test Library");
        DictionaryInfo dictionaryInfo = library.getDictionary("Test Library");
        assertNotNull(dictionaryInfo);
        assertTrue(dictionaryInfo.isLoaded());
    }

    @Test
    public void testArrangeFiles() {
        File libraryDirectory = new File("src/test/resources/library");
        File[] files = libraryDirectory.listFiles();
        List<File> expectedFiles = new ArrayList<>();
        for (File file : files) {
            String name = file.getName();
            if (name.equals("Fields.xml")) {
                expectedFiles.add(file);
                continue;
            }
            if (name.equals("Enums.xml")) {
                expectedFiles.add(file);
                continue;
            }
            if (name.equals("Components.xml")) {
                expectedFiles.add(file);
                continue;
            }
            if (name.equals("MsgType.xml")) {
                expectedFiles.add(file);
                continue;
            }
            if (name.equals("MsgContents.xml")) {
                expectedFiles.add(file);
                continue;
            }
        }
        List<File> arrangedFiles = library.arrangeFiles(files);
        assertEquals(expectedFiles.size(), arrangedFiles.size());
        for (int i = 0; i < expectedFiles.size(); i++) {
            assertEquals(expectedFiles.get(i), arrangedFiles.get(i));
        }
    }
}