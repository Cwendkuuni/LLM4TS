package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private Library library;
 private Logger log;

 @Before
 public void setUp() {
 // Initialize the logger for testing
 log = LoggerFactory.getLogger(Library.class);
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Arrange
 String libraryPath = "src/test/resources/library";

 // Act
 boolean result = library.loadFromDirectory(libraryPath);

 // Assert
 assertTrue(result);
 // Check if dictionaries were loaded
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertTrue(dictionaries.size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryEmptyDirectory() {
 // Arrange
 String libraryPath = "";

 // Act and Assert
 library.loadFromDirectory(libraryPath);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryNonDirectory() {
 // Arrange
 String libraryPath = "src/test/resources/file.txt";

 // Act and Assert
 library.loadFromDirectory(libraryPath);
 }

 @Test
 public void testGetDictionaries() {
 // Arrange: Dictionaries are loaded in the setUp method

 // Act
 List<DictionaryInfo> dictionaries = library.getDictionaries();

 // Assert
 assertNotNull(dictionaries);
 }

 @Test
 public void testGetDictionary() {
 // Arrange: Dictionaries are loaded in the setUp method
 String version = "latest";

 // Act
 DictionaryInfo dictionary = library.getDictionary(version);

 // Assert
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 // Arrange: Dictionaries are loaded in the setUp method

 // Act and Assert
 library.getDictionary(null);
 }
}