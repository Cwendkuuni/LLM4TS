
package org.fixsuite.message.test;

import org.fixsuite.message.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.*;

public class LibraryTest {

    @BeforeClass
    public static void setUp() {
        Library.logger = LoggerFactory.getLogger(LibraryTest.class);
    }

    @Test
    public void testLoadFromDirectory() {
        // Given
        String libraryPath = "test_data";
        Library library = new Library();
        List<File> expectedFiles = createExpectedFiles();

        // When
        boolean result = library.loadFromDirectory(libraryPath);

        // Then
        Assert.assertTrue(result);
        List<File> actualFiles = library.arrangeFiles(library.getFiles(libraryPath));
        Assert.assertArrayEquals(expectedFiles.toArray(), actualFiles.toArray());
        List<DictionaryInfo> actualDictionaries = library.getDictionaries();
        Assert.assertEquals(expectedFiles.size(), actualDictionaries.size());
        for (DictionaryInfo dictionary : actualDictionaries) {
            Assert.assertNotNull(dictionary);
            Assert.assertNotNull(dictionary.getVersion());
            Assert.assertFalse(dictionary.isEmpty());
            Assert.assertEquals(dictionary.getVersion(), dictionary.getFileName());
        }
    }

    private List<File> createExpectedFiles() {
        // Create expected files for testing
        List<File> expectedFiles = new ArrayList<>();
        expectedFiles.add(new File("Fields.xml"));
        expectedFiles.add(new File("Enums.xml"));
        expectedFiles.add(new File("Components.xml"));
        expectedFiles.add(new File("MsgType.xml"));
        expectedFiles.add(new File("MsgContents.xml"));
        return expectedFiles;
    }

    @Test
    public void testGetDictionaries() {
        // Given
        Library library = new Library();
        library.loadFromDirectory("test_data");

        // When
        List<DictionaryInfo> dictionaries = library.getDictionaries();

        // Then
        Assert.assertFalse(dictionaries.isEmpty());
        for (DictionaryInfo dictionary : dictionaries) {
            Assert.assertNotNull(dictionary);
            Assert.assertNotNull(dictionary.getVersion());
            Assert.assertFalse(dictionary.isEmpty());
        }
    }

    @Test
    public void testGetDictionary() {
        // Given
        Library library = new Library();
        library.loadFromDirectory("test_data");

        // When
        DictionaryInfo dictionary = library.getDictionary("FIX 5.0 SP1");

        // Then
        Assert.assertNotNull(dictionary);
        Assert.assertEquals("FIX 5.0 SP1", dictionary.getVersion());
        Assert.assertFalse(dictionary.isEmpty());
    }

    @AfterClass
    public static void tearDown() {
        Library.logger = null;
    }
}
