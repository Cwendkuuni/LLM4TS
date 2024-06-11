package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.Library;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LibraryTest {

 private static final String LIBRARY_PATH = "libraryPath"; // replace with actual path
 private Library library;
 private Logger logger;

 @Before
 public void setUp() {
 library = new Library();
 logger = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectory() {
 // Test when libraryPath is empty
 assertFalse(library.loadFromDirectory(""));
 // Test when libraryPath is not a directory
 assertFalse(library.loadFromDirectory("notADirectory"));
 // Test when libraryPath is null
 assertFalse(library.loadFromDirectory(null));
 // Test when there are no files in the directory
 File directory = new File(LIBRARY_PATH);
 assertFalse(library.loadFromDirectory(directory));
 // Test when all files are loaded successfully
 File[] files = new File("testData/libraryData").listFiles();
 List<File> arrangedFiles = library.arrangeFiles(files);
 library.loadDirectory(arrangedFiles, "testVersion");
 assertTrue(library.getDictionaries().size() > 0);
 }

 @Test
 public void testGetDictionaries() {
 // Test when there are no dictionaries loaded
 assertNotNull(library.getDictionaries());
 assertEquals(0, library.getDictionaries().size());
 // Test when there are dictionaries loaded
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertTrue(dictionaries.size() > 0);
 }

 @Test
 public void testGetDictionary() {
 // Test when there are no dictionaries loaded
 assertNull(library.getDictionary("testVersion"));
 // Test when there is a dictionary loaded
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary("testVersion");
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 library.getDictionary(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryEmptyVersion() {
 library.getDictionary("");
 }
}