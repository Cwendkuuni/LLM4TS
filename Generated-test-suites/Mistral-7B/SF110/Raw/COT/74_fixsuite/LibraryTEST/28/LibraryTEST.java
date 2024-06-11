package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.Library;
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

 private Library library;
 private static final String LIBRARY_PATH = "path/to/library";
 private static final String DICTIONARY_VERSION = "version";
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @Before
 public void setUp() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Test loading from a valid directory
 boolean result = library.loadFromDirectory(LIBRARY_PATH);
 assertTrue(result);
 // Test loading an empty directory
 result = library.loadFromDirectory("empty_directory");
 assertFalse(result);
 }

 @Test
 public void testGetDictionaries() {
 // Test getting an empty list when no dictionaries are loaded
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertTrue(dictionaries.isEmpty());
 // Test getting a list of dictionaries when they are loaded
 library.loadFromDirectory(LIBRARY_PATH);
 dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 // Test getting null when no dictionary with the given version is loaded
 DictionaryInfo dictionary = library.getDictionary(DICTIONARY_VERSION);
 assertNull(dictionary);
 // Test getting a dictionary when it is loaded
 library.loadFromDirectory(LIBRARY_PATH);
 dictionary = library.getDictionary(DICTIONARY_VERSION);
 assertNotNull(dictionary);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryWithNullLibraryPath() {
 library.loadFromDirectory(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryWithInvalidLibraryPath() {
 library.loadFromDirectory("invalid_library_path");
 }

 @Test(expected = IOException.class)
 public void testLoadDirectoryWithIOException() throws IOException {
 File file = new File("io_exception_file");
 List<File> files = library.arrangeFiles(new File[] { file });
 library.loadDirectory(files, DICTIONARY_VERSION);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWithNullFiles() {
 library.loadDirectory(null, DICTIONARY_VERSION);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWithNullVersion() {
 library.loadDirectory(new ArrayList<>(), null);
 }
}