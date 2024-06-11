package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
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
 private static final String LIBRARY_PATH = "libraryPath"; // Replace with the actual path
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @Before
 public void setup() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Test when libraryPath is null
 assertFalse(library.loadFromDirectory(null));
 // Test when libraryPath is an empty directory
 File emptyDirectory = new File("emptyDirectory");
 assertFalse(library.loadFromDirectory(emptyDirectory.getAbsolutePath()));
 // Test when libraryPath is a non-existent directory
 File nonExistentDirectory = new File("nonExistentDirectory");
 assertFalse(library.loadFromDirectory(nonExistentDirectory.getAbsolutePath()));
 // Test when libraryPath is a valid directory with files
 File validDirectory = new File(LIBRARY_PATH);
 File[] files = validDirectory.listFiles();
 assertFalse(library.loadFromDirectory(null)); // Verify unloading previous dictionaries
 library.loadFromDirectory(validDirectory.getAbsolutePath());
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertTrue(dictionaries.size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectory_IllegalArgumentException() {
 // Test when libraryPath is not a directory
 File notADirectory = new File("notADirectory");
 library.loadFromDirectory(notADirectory.getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testLoadFromDirectory_IOException() {
 // Test when an I/O error occurs during loading
 File iOErrorFile = new File("iOErrorFile");
 iOErrorFile.deleteOnExit();
 try {
 iOErrorFile.createNewFile();
 library.loadFromDirectory(LIBRARY_PATH);
 library.loadFromDirectory(iOErrorFile.getAbsolutePath());
 fail("An I/O exception was not thrown.");
 } catch (IOException e) {
 // Expected
 } finally {
 try {
 iOErrorFile.delete();
 } catch (IOException e) {
 logger.error("Error deleting test file.", e);
 }
 }
 }

 @Test
 public void testGetDictionaries() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertTrue(dictionaries.size() > 0);
 }

 @Test
 public void testGetDictionary() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 DictionaryInfo dictionary = dictionaries.get(0);
 assertEquals("Expected dictionary version", dictionary.getVersion(), library.getDictionary(dictionary.getVersion()).getVersion());
 }
}