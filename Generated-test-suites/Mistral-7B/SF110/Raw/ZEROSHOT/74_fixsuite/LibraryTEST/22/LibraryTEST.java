package org.fixsuite.message.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.DictionaryInfo;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo.DictionaryStatus;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

 private Library library;
 private Logger logger;

 @Before
 public void setup() {
 library = new Library();
 logger = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectorySuccess() {
 String libraryPath = "src/test/resources/library";
 boolean result = library.loadFromDirectory(libraryPath);
 assertTrue(result);
 }

 @Test
 public void testLoadFromDirectoryEmptyDirectory() {
 String libraryPath = "src/test/resources/emptyLibrary";
 boolean result = library.loadFromDirectory(libraryPath);
 assertFalse(result);
 }

 @Test
 public void testLoadFromDirectoryInvalidPath() {
 String libraryPath = "src/test/resources/invalidLibrary";
 boolean result = library.loadFromDirectory(libraryPath);
 assertFalse(result);
 }

 @Test
 public void testLoadFromDirectoryFailToLoadDictionary() {
 String libraryPath = "src/test/resources/libraryWithFailingDictionary";
 boolean result = library.loadFromDirectory(libraryPath);
 assertTrue(result);
 // Assuming that the failing dictionary is the one with version "Test_Fail"
 DictionaryInfo testFailDictionary = library.getDictionary("Test_Fail");
 assertNull(testFailDictionary);
 }

 @Test
 public void testGetDictionariesEmptyLibrary() {
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertTrue(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionariesLoadedLibrary() {
 library.loadFromDirectory("src/test/resources/library");
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 List<String> expectedVersions = new ArrayList<String>(Collections.nCopies(5, "Version_1"));
 Collections.shuffle(expectedVersions);
 for (DictionaryInfo dictionary : dictionaries) {
 String version = dictionary.getVersion();
 assertTrue(expectedVersions.contains(version));
 }
 }

 @Test
 public void testGetDictionaryEmptyLibrary() {
 String version = "Version_1";
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNull(dictionary);
 }

 @Test
 public void testGetDictionaryLoadedLibrary() {
 String version = "Version_1";
 library.loadFromDirectory("src/test/resources/library");
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNotNull(dictionary);
 assertNotNull(dictionary.getFields());
 assertNotNull(dictionary.getEnums());
 assertNotNull(dictionary.getComponents());
 assertNotNull(dictionary.getMsgTypes());
 assertNotNull(dictionary.getMsgContents());
 assertTrue(dictionary.isLoaded());
 assertNotNull(dictionary.getLogger());
 }

 @Test
 public void testGetDictionaryNullVersion() {
 DictionaryInfo dictionary = library.getDictionary(null);
 assertNull(dictionary);
 }

 @Test
 public void testGetDictionaryInvalidVersion() {
 String invalidVersion = "Version_X";
 DictionaryInfo dictionary = library.getDictionary(invalidVersion);
 assertNull(dictionary);
 }
}