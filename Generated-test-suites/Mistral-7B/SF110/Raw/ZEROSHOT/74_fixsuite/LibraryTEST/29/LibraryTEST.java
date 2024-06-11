package org.fixsuite.message.test;

import static org.junit.Assert.*;
import org.fixsuite.message.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library"; // Replace with the actual directory path
 private static Library library;
 private static Logger logger;

 @BeforeClass
 public static void setup() {
 library = new Library();
 logger = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectory() {
 // Test loading from a valid directory
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 // Test loading from an empty directory
 File emptyDirectory = new File("path/to/empty_directory"); // Replace with the actual empty directory path
 assertFalse(library.loadFromDirectory(emptyDirectory.getAbsolutePath()));
 // Test loading from a non-existent directory
 File nonExistentDirectory = new File("path/to/non_existent_directory"); // Replace with the actual non-existent directory path
 assertFalse(library.loadFromDirectory(nonExistentDirectory.getAbsolutePath()));
 }

 @Test
 public void testGetDictionaries() {
 // Test getting dictionaries after loading from a valid directory
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 // Test getting dictionaries after loading from an empty directory
 library.loadFromDirectory(new File("path/to/empty_directory").getAbsolutePath());
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testGetDictionary() {
 // Test getting a dictionary by version after loading from a valid directory
 DictionaryInfo dictionary = library.getDictionary("1.0");
 assertNotNull(dictionary);
 // Test getting a non-existent dictionary by version
 dictionary = library.getDictionary("2.0");
 assertNull(dictionary);
 }
}