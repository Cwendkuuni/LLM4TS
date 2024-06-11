package org.fixsuite.message.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class LibraryTest {

    static Logger logger;

    @Test
    public void testLibraryInstanceCreation() {
        Library library = new Library();
        assertNotNull(library);
    }

    @Test
    public void testLoadFromDirectorySuccess() {
        String libraryPath = "test/resources/library";
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryEmptyDirectory() {
        String libraryPath = "test/resources/empty";
        Library library = new Library();
        assertFalse(library.loadFromDirectory(libraryPath));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryInvalidPath() {
        String libraryPath = "test/resources/invalid";
        Library library = new Library();
        assertFalse(library.loadFromDirectory(libraryPath));
    }

    @Test
    public void testGetDictionaries() {
        String libraryPath = "test/resources/library";
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionarySuccess() {
        String libraryPath = "test/resources/library";
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
        String version = "1.0";
        DictionaryInfo dictionary = library.getDictionary(version);
        assertNotNull(dictionary);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDictionaryNullVersion() {
        String libraryPath = "test/resources/library";
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
        assertNull(library.getDictionary(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDictionaryInvalidVersion() {
        String libraryPath = "test/resources/library";
        Library library = new Library();
        assertTrue(library.loadFromDirectory(libraryPath));
        String version = "invalid";
        assertNull(library.getDictionary(version));
    }

    @Test
    public void testLoadDirectorySuccess() {
        String libraryPath = "test/resources/library";
        Library library = new Library();
        List<File> files = getTestFiles("Dictionary");
        library.loadDirectory(files, "1.0");
        assertTrue(library.getDictionaries().size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadDirectoryInvalidVersion() {
        String libraryPath = "test/resources/library";
        Library library = new Library();
        List<File> files = getTestFiles("Dictionary");
        library.loadDirectory(files, "invalidVersion");
        assertTrue(library.getDictionaries().isEmpty());
    }

    private List<File> getTestFiles(String folderName) {
        File testFolder = new File("test/resources/testData");
        File[] files = testFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith(folderName);
            }
        });
        return Arrays.asList(files);
    }

    @Test
    public void testArrangeFilesSuccess() {
        String libraryPath = "test/resources/testData";
        Library library = new Library();
        List<File> files = Arrays.asList(new File(libraryPath, "Fields.xml"),
                                         new File(libraryPath, "Enums.xml"),
                                         new File(libraryPath, "Components.xml"),
                                         new File(libraryPath, "MsgType.xml"),
                                         new File(libraryPath, "MsgContents.xml"));
        List<File> arrangedFiles = library.arrangeFiles(files.toArray(new File[0]));
        List<Integer> expectedOrder = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> actualOrder = new ArrayList<>();
        for (File file : arrangedFiles) {
            String fileName = file.getName();
            int index = Arrays.asList(Library.FIELDS_FILE_NAME, Library.ENUMS_FILE_NAME, Library.COMPONENTS_FILE_NAME, Library.MSG_TYPE_FILE_NAME, Library.MSG_CONTENTS_FILE_NAME).indexOf(fileName);
            actualOrder.add(index);
        }
        assertEquals(expectedOrder, actualOrder);
    }

}