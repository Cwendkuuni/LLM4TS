package org.fixsuite.message.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.DictionaryInfo;
import org.fixsuite.message.Library;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library"; // Replace with the actual path to the library directory
 private static final String DICTIONARY_VERSION = "4.4"; // Replace with the actual dictionary version

 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @Test
 public void testLoadFromDirectory() {
  Library library = new Library();
  assertTrue(library.loadFromDirectory(LIBRARY_PATH));
  List<DictionaryInfo> dictionaries = library.getDictionaries();
  assertFalse(dictionaries.isEmpty());
  DictionaryInfo dictionary = library.getDictionary(DICTIONARY_VERSION);
  assertNotNull(dictionary);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryEmptyDirectory() {
  Library library = new Library();
  String emptyDirectoryPath = ""; // Replace with the actual empty directory path
  assertFalse(library.loadFromDirectory(emptyDirectoryPath));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryInvalidPath() {
  Library library = new Library();
  String invalidPath = "invalid_path"; // Replace with the actual invalid path
  assertFalse(library.loadFromDirectory(invalidPath));
 }

 @Test
 public void testGetDictionaries() {
  Library library = new Library();
  library.loadFromDirectory(LIBRARY_PATH);
  List<DictionaryInfo> dictionaries = library.getDictionaries();
  assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
  Library library = new Library();
  library.loadFromDirectory(LIBRARY_PATH);
  DictionaryInfo dictionary = library.getDictionary(DICTIONARY_VERSION);
  assertNotNull(dictionary);
  assertNotNull(dictionary.getVersion());
 }

 @Test
 public void testArrangeFiles() {
  Library library = new Library();
  File[] files = new File("path/to/library/files").listFiles();
  List<File> arrangedFiles = library.arrangeFiles(files);
  assertFalse(arrangedFiles.isEmpty());
  assertEquals(5, arrangedFiles.size());
 }
}