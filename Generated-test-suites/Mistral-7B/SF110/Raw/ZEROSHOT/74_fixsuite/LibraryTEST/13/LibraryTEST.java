package org.fixsuite.message.test;

import static org.junit.Assert.*;
import org.fixsuite.message.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library";
 private static final String LIBRARY_VERSION = "4.4";

 @Test
 public void testLoadFromDirectory() {
  Library library = new Library();
  Logger logger = LoggerFactory.getLogger(Library.class);

  // Test loading from an existing directory
  assertTrue(library.loadFromDirectory(LIBRARY_PATH));
  List<DictionaryInfo> dictionaries = library.getDictionaries();
  assertEquals(1, dictionaries.size());
  DictionaryInfo dictionary = dictionaries.get(0);
  assertEquals(LIBRARY_VERSION, dictionary.getVersion());

  // Test loading an empty directory
  File emptyDirectory = new File("path/to/empty_directory");
  assertFalse(library.loadFromDirectory(emptyDirectory.getAbsolutePath()));

  // Test loading a non-existent directory
  File nonExistentDirectory = new File("path/to/non_existent_directory");
  assertFalse(library.loadFromDirectory(nonExistentDirectory.getAbsolutePath()));
 }

 @Test
 public void testGetDictionary() {
  Library library = new Library();
  Library.logger = LoggerFactory.getLogger(Library.class);
  library.loadFromDirectory(LIBRARY_PATH);
  DictionaryInfo dictionary = library.getDictionary(LIBRARY_VERSION);
  assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullDictionary() {
  Library library = new Library();
  Library.logger = LoggerFactory.getLogger(Library.class);
  library.getDictionary(null);
 }

 @Test
 public void testGetEmptyDictionaryList() {
  Library library = new Library();
  List<DictionaryInfo> dictionaries = library.getDictionaries();
  assertEquals(0, dictionaries.size());
 }
}