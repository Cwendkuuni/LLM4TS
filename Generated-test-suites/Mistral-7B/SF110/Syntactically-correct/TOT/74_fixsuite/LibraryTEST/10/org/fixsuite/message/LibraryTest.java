package org.fixsuite.message;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static final String LIBRARY_PATH = "test_resources/libraries"; // Replace with the actual path to the test library directory
 private Library library;
 private Logger logger;

 @Before
 public void setup() {
 library = new Library();
 logger = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectory() throws IOException {
 // Test loading from a valid directory
 File directory = new File(LIBRARY_PATH);
 assertTrue(library.loadFromDirectory(directory.getAbsolutePath()));
 // Test loading an empty directory
 File emptyDirectory = new File("test_resources/empty_library");
 assertFalse(library.loadFromDirectory(emptyDirectory.getAbsolutePath()));
 // Test loading a non-existent directory
 File nonExistentDirectory = new File("test_resources/non_existent_library");
 assertFalse(library.loadFromDirectory(nonExistentDirectory.getAbsolutePath()));
 }

 @Test
 public void testGetDictionaries() {
 // Test getting an empty list when no dictionaries are loaded
 assertTrue(library.getDictionaries().isEmpty());
 // Test getting the loaded dictionaries
 library.loadFromDirectory(new File(LIBRARY_PATH).getAbsolutePath());
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWithNullVersion() {
 library.getDictionary(null);
 }

 @Test
 public void testGetDictionaryWithValidVersion() {
 library.loadFromDirectory(new File(LIBRARY_PATH).getAbsolutePath());
 DictionaryInfo dictionary = library.getDictionary("test_version");
 assertNull(dictionary);
 }

 @Test
 public void testGetDictionaryWithLoadedDictionary() {
 library.loadFromDirectory(new File(LIBRARY_PATH).getAbsolutePath());
 DictionaryInfo dictionary = library.getDictionary("test_version");
 assertNotNull(dictionary);
 }
}