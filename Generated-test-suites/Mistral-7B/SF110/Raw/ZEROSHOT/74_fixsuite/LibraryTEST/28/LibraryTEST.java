package org.fixsuite.message.test;

import static org.junit.Assert.*;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library";
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @Test
 public void testLibraryLoadFromDirectory() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);

 // Test if dictionaries are loaded
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());

 // Test if specific dictionary is present
 DictionaryInfo dictionary = library.getDictionary("version");
 assertNotNull(dictionary);
 assertEquals("version", dictionary.getVersion());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLibraryLoadEmptyDirectory() {
 Library library = new Library();
 library.loadFromDirectory("");
 fail("Expected IllegalArgumentException but got none");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLibraryLoadInvalidPath() {
 Library library = new Library();
 library.loadFromDirectory("invalidPath");
 fail("Expected IllegalArgumentException but got none");
 }

 @Test(expected = NullPointerException.class)
 public void testLibraryUnloadPreviousDictionariesNull() {
 Library library = new Library();
 library.dictionaries = null;
 library.loadFromDirectory(LIBRARY_PATH);
 fail("Expected NullPointerException but got none");
 }

 @Test
 public void testLibraryMemoryAllocation() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 long initialMemory = Runtime.getRuntime().freeMemory();
 library.loadFromDirectory(LIBRARY_PATH);
 long finalMemory = Runtime.getRuntime().freeMemory();
 assertEquals("Memory allocation should not increase", initialMemory, finalMemory, 0);
 }

 @Test
 public void testLibraryLoadingTime() {
 Library library = new Library();
 long startTime = System.currentTimeMillis();
 library.loadFromDirectory(LIBRARY_PATH);
 long endTime = System.currentTimeMillis();
 long loadingTime = endTime - startTime;
 assertTrue("Loading time should be less than 5000ms", loadingTime < 5000);
 }
}