package org.fixsuite.message.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private Library library;
 private Logger logger;

 @Before
 public void setUp() {
 library = new Library();
 logger = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectory() {
 // Given
 String libraryPath = "test/resources/libraries";
 // When
 boolean result = library.loadFromDirectory(libraryPath);
 // Then
 Assert.assertTrue(result);
 // Verify number of dictionaries loaded
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 Assert.assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryEmptyDirectory() {
 // Given
 String libraryPath = "test/resources/empty";
 // When
 library.loadFromDirectory(libraryPath);
 // Then
 // Should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryNonDirectory() {
 // Given
 String libraryPath = "test/resources/file.txt";
 // When
 library.loadFromDirectory(libraryPath);
 // Then
 // Should throw an IllegalArgumentException
 }

 @Test
 public void testGetDictionaries() {
 // Given
 library.loadFromDirectory("test/resources/libraries");
 // When
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 // Then
 // Verify the list is not empty
 Assert.assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 // Given
 library.loadFromDirectory("test/resources/libraries");
 // When
 DictionaryInfo dictionary = library.getDictionary("FIX 5.0 SP2");
 // Then
 // Verify the dictionary is not null
 Assert.assertNotNull(dictionary);
 // Verify the dictionary version is correct
 Assert.assertEquals("FIX 5.0 SP2", dictionary.getVersion());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 // Given
 library.loadFromDirectory("test/resources/libraries");
 // When
 DictionaryInfo dictionary = library.getDictionary(null);
 // Then
 // Should throw a NullPointerException
 }
}