package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library"; // Replace with the actual library path
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @Test
 public void testLoadFromDirectory() {
 Library library = new Library();
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryEmptyPath() {
 Library library = new Library();
 assertFalse(library.loadFromDirectory(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryInvalidPath() {
 Library library = new Library();
 assertFalse(library.loadFromDirectory("invalid_path"));
 }

 @Test(expected = IllegalStateException.class)
 public void testLoadFromDirectoryFailedToLoad() {
 Library library = new Library();
 String invalidLibraryPath = "path/to/invalid_library"; // Replace with an invalid library path
 assertFalse(library.loadFromDirectory(invalidLibraryPath));
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertTrue(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 Library library = new Library();
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 DictionaryInfo dictionary = library.getDictionary("latest_version");
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 Library library = new Library();
 assertNull(library.getDictionary(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryInvalidVersion() {
 Library library = new Library();
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 assertNull(library.getDictionary("invalid_version"));
 }

 @Test
 public void testGetDictionaries() {
 Library library = new Library();
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testLibraryStaticInitializer() {
 Library library = new Library();
 assertNotNull(Library.logger);
 }
}