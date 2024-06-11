
package org.fixsuite.message;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {
    // Existing test cases and new test cases

    @Test
    public void testLibraryConstructor() {
        Library library = new Library();
        // Check if dictionaries map is empty
        Assert.assertTrue(library.dictionaries.isEmpty());
    }

    @Test
    public void testLoadFromDirectorySuccess() {
        Library library = new Library();
        String libraryPath = "testData"; // Assuming testData is a valid directory
        boolean result = library.loadFromDirectory(libraryPath);
        // Check if dictionaries map is not empty
        Assert.assertFalse(library.dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionaries() {
        Library library = new Library();
        String libraryPath = "testData"; // Assuming testData is a valid directory
        library.loadFromDirectory(libraryPath);
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        // Check if the size of the list is greater than zero
        Assert.assertFalse(dictionaries.isEmpty());
    }

    @Test
    public void testGetDictionary() {
        Library library = new Library();
        String libraryPath = "testData"; // Assuming testData is a valid directory with a dictionary version
        library.loadFromDirectory(libraryPath);
        DictionaryInfo dictionary = library.getDictionary("versionNumber"); // Assuming a valid version number
        // Check if the dictionary is not null
        Assert.assertNotNull(dictionary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryEmptyDirectory() {
        Library library = new Library();
        String libraryPath = "emptyDirectory"; // Assuming emptyDirectory is an empty directory
        library.loadFromDirectory(libraryPath);
        // The method should throw an IllegalArgumentException if the directory is empty
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromDirectoryInvalidPath() {
        Library library = new Library();
        String libraryPath = "invalidPath"; // Assuming invalidPath is an invalid path
        library.loadFromDirectory(libraryPath);
        // The method should throw an IllegalArgumentException if the path is invalid
    }

    @Test(expected = NullPointerException.class)
    public void testGetDictionaryNullVersion() {
        Library library = new Library();
        library.loadFromDirectory("testData"); // Assuming testData is a valid directory
        DictionaryInfo dictionary = library.getDictionary(null);
        // The method should throw a NullPointerException if the version is null
    }
}