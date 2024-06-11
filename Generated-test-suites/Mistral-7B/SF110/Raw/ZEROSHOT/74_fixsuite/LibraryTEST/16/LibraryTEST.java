package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import static java.util.Arrays.*;

public class LibraryTest {

 private static final String TEST_DIR = "src/test/resources/";
 private Library library;
 private List<File> files;

 @Before
 public void setUp() {
 library = new Library();
 files = arrangeFiles(new File(TEST_DIR).listFiles());
 }

 @Test
 public void testLoadFromDirectory() {
 // Test loading an empty directory
 File emptyDir = new File(TEST_DIR + "empty");
 boolean result = library.loadFromDirectory(emptyDir.getAbsolutePath());
 assertTrue("Loading an empty directory should succeed", result);
 assertEquals("No dictionaries should be loaded", 0, library.getDictionaries().size());

 // Test loading a valid directory
 result = library.loadFromDirectory(TEST_DIR);
 assertTrue("Loading a valid directory should succeed", result);
 assertFalse("At least one dictionary should be loaded", library.getDictionaries().isEmpty());

 // Test loading a non-existent directory
 File nonExistentDir = new File(TEST_DIR + "nonExistent");
 result = library.loadFromDirectory(nonExistentDir.getAbsolutePath());
 assertFalse("Loading a non-existent directory should fail", result);
 }

 @Test
 public void testGetDictionaries() {
 // Test getting an empty list of dictionaries
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertTrue("Getting an empty list of dictionaries should succeed", !dictionaries.isEmpty());

 // Test getting a list of dictionaries from a valid load
 library.loadFromDirectory(TEST_DIR);
 dictionaries = library.getDictionaries();
 assertFalse("Getting a list of dictionaries from a valid load should not be empty", dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 // Test getting a null dictionary from an empty list
 DictionaryInfo dictionary = library.getDictionary(null);
 assertNull("Getting a null dictionary from an empty list should return null", dictionary);

 // Test getting a dictionary from a valid load
 library.loadFromDirectory(TEST_DIR);
 dictionary = library.getDictionary("FIX 4.4");
 assertNotNull("Getting a dictionary from a valid load should not be null", dictionary);
 assertEquals("Getting the wrong dictionary version", "FIX 4.4", dictionary.getVersion());
 }

 @Test
 public void testLoadDirectory() {
 Library testLibrary = new Library();
 List<File> testFiles = arrangeFiles(new File(TEST_DIR).listFiles());
 testLibrary.loadDirectory(testFiles, "FIX 4.4");
 assertEquals("The loaded dictionary version should match the provided version", "FIX 4.4", testLibrary.getDictionary("FIX 4.4").getVersion());
 }

 @Test
 public void testArrangeFiles() {
 File[] files = new File(TEST_DIR).listFiles();
 List<File> arrangedFiles = Library.arrangeFiles(files);
 List<Integer> keys = asList(1, 2, 3, 4, 5);
 for (int i = 0; i < arrangedFiles.size(); ++i) {
 int key = i + 1;
 assertEquals("The arranged files should match the expected keys", key, arrangedFiles.get(i).getName().hashCode() % 10);
 }
 }

 @Test
 public void testParse() {
 DictionaryInfo dictionary = new DictionaryInfo("FIX 4.4");
 File fieldsFile = new File(TEST_DIR + "Fields.xml");
 MainParser.parse(dictionary, fieldsFile);
 assertEquals("The parsed fields should not be empty", !dictionary.getFields().isEmpty(), true);
 }

}