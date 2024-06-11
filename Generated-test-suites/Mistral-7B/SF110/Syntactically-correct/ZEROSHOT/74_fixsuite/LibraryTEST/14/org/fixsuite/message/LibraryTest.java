package org.fixsuite.message.test;

import static org.junit.Assert.*;
import org.fixsuite.message.*;
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
 public void setup() {
 // Initialize Logger
 log = LoggerFactory.getLogger(Library.class);

 // Initialize Library
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Given
 String libraryPath = "test/resources/library";

 // When
 boolean result = library.loadFromDirectory(libraryPath);

 // Then
 assertTrue(result);

 // Verify Dictionaries are loaded
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 // Given
 String version = "4.4";
 Library library = new Library();
 library.loadFromDirectory("test/resources/library");

 // When
 DictionaryInfo dictionary = library.getDictionary(version);

 // Then
 assertNotNull(dictionary);
 assertEquals(version, dictionary.getVersion());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWithNullVersion() {
 // Given
 Library library = new Library();

 // When
 DictionaryInfo dictionary = library.getDictionary(null);

 // Then
 fail("Expected NullPointerException");
 }

 @Test
 public void testGetDictionaryWithEmptyLibrary() {
 // Given
 Library library = new Library();

 // When
 DictionaryInfo dictionary = library.getDictionary("1.0");

 // Then
 assertNull(dictionary);
 }

 @Test
 public void testLoadDirectoryWithEmptyDirectory() {
 // Given
 String libraryPath = "test/resources/emptyLibrary";
 Library library = new Library();

 // When
 boolean result = library.loadFromDirectory(libraryPath);

 // Then
 assertFalse(result);
 }

 @Test
 public void testLoadDirectoryWithInvalidDirectory() {
 // Given
 String libraryPath = "test/resources/invalidLibrary";
 Library library = new Library();

 // When
 boolean result = library.loadFromDirectory(libraryPath);

 // Then
 assertFalse(result);
 }
}