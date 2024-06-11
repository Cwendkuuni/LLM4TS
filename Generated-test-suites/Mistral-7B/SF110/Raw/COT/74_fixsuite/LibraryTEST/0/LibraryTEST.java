package org.fixsuite.message;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

 private Library library;
 private static final String LIBRARY_PATH = "testData"; // Replace with the actual test data directory path
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @Before
 public void setUp() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Test case for loading dictionaries from a valid directory
 List<File> files = new ArrayList(Arrays.asList(new File(LIBRARY_PATH).listFiles()));
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 assertEquals(5, library.getDictionaries().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryNullDirectory() {
 library.loadFromDirectory(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryEmptyDirectory() {
 File emptyDirectory = new File("");
 library.loadFromDirectory(emptyDirectory.getAbsolutePath());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryNotADirectory() {
 File file = new File("testData/testFile.txt");
 library.loadFromDirectory(file.getAbsolutePath());
 }

 @Test
 public void testGetDictionaries() {
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertNotNull(dictionaries);
 assertEquals(5, dictionaries.size());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionariesNullLibrary() {
 Library nullLibrary = null;
 List<DictionaryInfo> dictionaries = nullLibrary.getDictionaries();
 }

 @Test
 public void testGetDictionary() {
 String testVersion = "testVersion";
 DictionaryInfo dictionary = library.getDictionary(testVersion);
 assertNull(dictionary);
 }

 @Test
 public void testGetDictionaryPresentVersion() {
 String testVersion = "testVersion1";
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary(testVersion);
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullLibrary() {
 Library nullLibrary = null;
 DictionaryInfo dictionary = nullLibrary.getDictionary("testVersion");
 }
}