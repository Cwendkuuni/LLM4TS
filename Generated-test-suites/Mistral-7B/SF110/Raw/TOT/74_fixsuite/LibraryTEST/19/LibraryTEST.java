package org.fixsuite.message.test;

import org.fixsuite.message.*;
import org.junit.Assert;
import org.junit.Test;

public class LibraryTest {
    @Test
    public void testLoadFromDirectory() {
        // Given
        String libraryPath = "path/to/library";
        Library library = new Library();

        // When
        boolean result = library.loadFromDirectory(libraryPath);

        // Then
        Assert.assertTrue("Loading from directory should be successful", result);
        Assert.assertFalse("Previous dictionaries should be unloaded before new ones are loaded", library.getDictionaries().size() > 0);
        Assert.assertEquals("Number of dictionaries loaded should match the number of files in the directory", 5, library.getDictionaries().size());
    }

    @Test
    public void testGetDictionaries() {
        // Given
        String libraryPath = "path/to/library";
        Library library = new Library();
        library.loadFromDirectory(libraryPath);

        // When
        List<DictionaryInfo> dictionaries = library.getDictionaries();

        // Then
        Assert.assertFalse("Previous dictionaries should be empty after loading new ones", library.getDictionaries().isEmpty());
        for (DictionaryInfo dictionary : dictionaries) {
            Assert.assertNotNull("Each dictionary should be initialized correctly", dictionary);
        }
    }

    @Test
    public void testGetDictionary() {
        // Given
        String libraryPath = "path/to/library";
        Library library = new Library();
        library.loadFromDirectory(libraryPath);

        // When
        DictionaryInfo dictionary = library.getDictionary("version");

        // Then
        Assert.assertNotNull("Dictionary with the specified version should be present", dictionary);
        Assert.assertEquals("Dictionary version should match the specified version", "version", dictionary.getVersion());
    }
}