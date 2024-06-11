package org.fixsuite.message;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

 private Library library;
 private Logger logger;
 private File testDirectory;

 @Before
 public void setup() {
 // Initialize test directory with test files
 testDirectory = new File("src/test/resources/library");
 library = new Library();
 logger = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectory_EmptyDirectory_ReturnsFalse() {
 // Arrange: empty directory
 File emptyDirectory = new File("src/test/resources/emptyLibrary");
 library.loadFromDirectory(emptyDirectory.getAbsolutePath());
 assertTrue(library.getDictionaries().isEmpty());
 assertFalse(library.loadFromDirectory(emptyDirectory.getAbsolutePath()));
 }

 @Test
 public void testLoadFromDirectory_AllFilesPresent_ReturnsTrue() {
 // Arrange: files present in directory
 File filesDirectory = testDirectory;
 library.loadFromDirectory(filesDirectory.getAbsolutePath());
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(5, dictionaries.size());
 // Act: second load, should return true and not clear previous dictionaries
 library.loadFromDirectory(filesDirectory.getAbsolutePath());
 List<DictionaryInfo> dictionariesAfterSecondLoad = library.getDictionaries();
 assertEquals(5, dictionariesAfterSecondLoad.size());
 // Assert: dictionaries are the same
 DictionaryInfo dictionary0 = dictionaries.get(0);
 DictionaryInfo dictionary0AfterSecondLoad = dictionariesAfterSecondLoad.get(0);
 assertEquals(dictionary0, dictionary0AfterSecondLoad);
 }

 @Test
 public void testLoadFromDirectory_FileMissing_ReturnsFalse() {
 // Arrange: one file missing
 File missingFileDirectory = new File("src/test/resources/missingFileLibrary");
 library.loadFromDirectory(missingFileDirectory.getAbsolutePath());
 assertTrue(library.getDictionaries().isEmpty());
 assertFalse(library.loadFromDirectory(missingFileDirectory.getAbsolutePath()));
 }

 @Test
 public void testGetDictionary_VersionExists_ReturnsDictionary() {
 // Arrange: dictionaries loaded
 library.loadFromDirectory(testDirectory.getAbsolutePath());
 String version = "4.4";
 DictionaryInfo dictionary = library.getDictionary(version);
 // Assert: dictionary is not null
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionary_VersionDoesntExist_ThrowsNullPointerException() {
 // Arrange: dictionaries not loaded
 library = new Library();
 String version = null;
 DictionaryInfo dictionary = library.getDictionary(version);
 }
}