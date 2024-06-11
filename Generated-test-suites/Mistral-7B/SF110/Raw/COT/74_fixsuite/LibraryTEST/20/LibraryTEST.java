package org.fixsuite.message;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

 private Library library;
 private static final String LIBRARY_PATH = "test_library_path"; // Set this to the path of your test library
 private static final String LIBRARY_VERSION = "test_library_version"; // Set this to the version of your test library
 private Logger logger;

 @Before
 public void setUp() {
 library = new Library();
 logger = LoggerFactory.getLogger(LibraryTest.class);
 }

 @After
 public void tearDown() throws Exception {
 library.getDictionaries().clear();
 }

 @Test
 public void testLoadFromDirectory() {
 // Test case for loading dictionaries from a valid directory
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals("Expected exactly one dictionary in the library", 1, dictionaries.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryNullPath() {
 // Test case for loading dictionaries from a null directory path
 library.loadFromDirectory(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryEmptyDirectory() {
 // Test case for loading dictionaries from an empty directory
 File emptyDirectory = new File("");
 library.loadFromDirectory(emptyDirectory.listFiles());
 }

 @Test(expected = IOException.class)
 public void testLoadFromDirectoryInvalidFile() throws IOException {
 // Test case for loading dictionaries from a file that does not exist
 File invalidFile = new File(LIBRARY_PATH, "InvalidFile.xml");
 library.loadFromDirectory(new File[] { invalidFile });
 }

 @Test
 public void testGetDictionaries() {
 // Test case for retrieving all dictionaries from the library
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertNotNull("Expected dictionaries to be retrieved from the library", dictionaries);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 // Test case for retrieving a dictionary with a null version
 library.getDictionary(null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryEmptyLibrary() {
 // Test case for retrieving a dictionary from an empty library
 library.getDictionary("");
 }

 @Test
 public void testGetDictionaryValidVersion() {
 // Test case for retrieving a dictionary with a valid version
 DictionaryInfo dictionary = library.getDictionary(LIBRARY_VERSION);
 assertNotNull("Expected dictionary to be retrieved from the library", dictionary);
 }

}