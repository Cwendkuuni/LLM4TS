package org.fixsuite.message;

import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

    private static final String TEST_DIRECTORY = "src/test/resources/LibraryTestData";
    private static final Logger LOGGER = LoggerFactory.getLogger(Library.class);

    @Test
    public void testLoadFromDirectory() {
        Library library = new Library();
        boolean result = library.loadFromDirectory(TEST_DIRECTORY);
        assertTrue(result);

        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(5, dictionaries.size());

        DictionaryInfo dictionary1 = library.getDictionary("FIX 4.4");
        assertNotNull(dictionary1);
        assertEquals("FIX 4.4", dictionary1.getVersion());

        DictionaryInfo dictionary2 = library.getDictionary("FIX 5.0SP1");
        assertNotNull(dictionary2);
        assertEquals("FIX 5.0SP1", dictionary2.getVersion());
    }

    @Test
    public void testLoadFromDirectory_EmptyDirectory() {
        Library library = new Library();
        String emptyDirectoryPath = "src/test/resources/EmptyDirectory";
        boolean result = library.loadFromDirectory(emptyDirectoryPath);
        assertFalse(result);
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testLoadFromDirectory_InvalidDirectory() {
        Library library = new Library();
        String invalidDirectoryPath = "src/test/resources/InvalidDirectory";
        boolean result = library.loadFromDirectory(invalidDirectoryPath);
        assertFalse(result);
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testArrangeFiles() throws IOException {
        File directory = new File("src/test/resources/LibraryTestData");
        File[] files = directory.listFiles();
        List<File> arrangedFiles = new Library().arrangeFiles(files);

        assertEquals(5, arrangedFiles.size());

        File expectedFieldsXml = new File("src/test/resources/LibraryTestData/Fields.xml");
        File expectedEnumsXml = new File("src/test/resources/LibraryTestData/Enums.xml");
        File expectedComponentsXml = new File("src/test/resources/LibraryTestData/Components.xml");
        File expectedMsgTypeXml = new File("src/test/resources/LibraryTestData/MsgType.xml");
        File expectedMsgContentsXml = new File("src/test/resources/LibraryTestData/MsgContents.xml");

        File actualFieldsXml = arrangedFiles.get(0);
        File actualEnumsXml = arrangedFiles.get(1);
        File actualComponentsXml = arrangedFiles.get(2);
        File actualMsgTypeXml = arrangedFiles.get(3);
        File actualMsgContentsXml = arrangedFiles.get(4);

        assertEquals(expectedFieldsXml.getAbsolutePath(), actualFieldsXml.getAbsolutePath());
        assertEquals(expectedEnumsXml.getAbsolutePath(), actualEnumsXml.getAbsolutePath());
        assertEquals(expectedComponentsXml.getAbsolutePath(), actualComponentsXml.getAbsolutePath());
        assertEquals(expectedMsgTypeXml.getAbsolutePath(), actualMsgTypeXml.getAbsolutePath());
        assertEquals(expectedMsgContentsXml.getAbsolutePath(), actualMsgContentsXml.getAbsolutePath());
    }

    @Test
    public void testLoadDirectory_EmptyFileList() {
        Library library = new Library();
        List<File> emptyFileList = new ArrayList<>();
        library.loadDirectory(emptyFileList, "test");
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testLoadDirectory_NullFileList() {
        Library library = new Library();
        library.loadDirectory(null, "test");
        assertEquals(0, library.getDictionaries().size());
    }

    @Test
    public void testLoadDirectory_NullVersion() {
        Library library = new Library();
        String[] files = new String[]{"Fields.xml", "Enums.xml", "Components.xml", "MsgType.xml", "MsgContents.xml"};
        List<File> arrangedFiles = new ArrayList<>();
        for (String file : files) {
            arrangedFiles.add(new File("src/test/resources/LibraryTestData/" + file));
        }
        library.loadDirectory(arrangedFiles, null);
        assertEquals(0, library.getDictionaries().size());
    }
}