package org.fixsuite.message;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LibraryTest {

 private Library library;
 private Logger logger;
 private List<File> files;

 @Before
 public void setUp() {
 library = new Library();
 logger = Mockito.mock(Logger.class);
 Library.logger = logger;
 files = Arrays.asList(new File("path/to/library"), new File("path/to/empty_directory"));
 }

 @Test
 public void testLoadFromDirectory() {
 // Happy path test
 library.loadFromDirectory("path/to/library");
 assertEquals(5, library.getDictionaries().size());
 // Test with empty directory
 library.loadFromDirectory("path/to/empty_directory");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testGetDictionaries() {
 // Test with loaded dictionaries
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(5, dictionaries.size());
 // Test with empty dictionaries
 library.loadFromDirectory("path/to/empty_directory");
 assertNotNull(dictionaries);
 assertEquals(0, dictionaries.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryWithInvalidVersion() {
 library.loadFromDirectory("path/to/library");
 assertNotNull(library.getDictionary("InvalidVersion"));
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetDictionaryWithValidVersion() {
 library.loadFromDirectory("path/to/library");
 DictionaryInfo dictionary = library.getDictionary("1.0");
 assertNotNull(dictionary);
 }
}