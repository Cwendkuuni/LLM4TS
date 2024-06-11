package org.fixsuite.message.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.Library;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class LibraryTest {

 private static final String LIBRARY_PATH = "target/test-classes/testData";
 private Library library;
 private List<File> arrangedFiles;

 @Before
 public void setUp() {
 library = new Library();
 arrangedFiles = new ArrayList<File>();
 }

 @Test
 public void testLoadFromDirectory() {
 // Given
 File libraryPath = new File(LIBRARY_PATH);
 File[] files = libraryPath.listFiles();
 File[] arrangedFilesExpected = { new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml") };
 // When
 library.loadFromDirectory(LIBRARY_PATH);
 // Then
 List<File> arrangedFilesActual = library.arrangeFiles(files);
 assertEquals("Unexpected number of files", arrangedFilesExpected.length, arrangedFilesActual.size());
 for (int i = 0; i < arrangedFilesExpected.length; i++) {
 assertEquals("Unexpected file " + i + " in arrangeFiles result", arrangedFilesExpected[i], arrangedFilesActual.get(i));
 }
 // Then (library state)
 assertFalse("Dictionaries should be empty before loading", library.getDictionaries().isEmpty());
 // Logging statements may differ based on the implementation, so we won't test them
 }

 @Test
 public void testGetDictionaries() {
 // Given
 library.loadFromDirectory(LIBRARY_PATH);
 // When
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 // Then
 assertNotNull("Dictionary list should not be null", dictionaries);
 }

 @Test
 public void testGetDictionary() {
 // Given
 library.loadFromDirectory(LIBRARY_PATH);
 // When
 String version = "1.0";
 DictionaryInfo dictionary = library.getDictionary(version);
 // Then
 assertNotNull("Dictionary for version " + version + " should not be null", dictionary);
 assertEquals("Unexpected dictionary version", version, dictionary.getVersion());
 }

 @Test
 public void testGetDictionaryNullVersion() {
 // Given
 library.loadFromDirectory(LIBRARY_PATH);
 // When
 String version = null;
 DictionaryInfo dictionary = library.getDictionary(version);
 // Then
 assertNull("Dictionary for null version should be null", dictionary);
 }

 @Test
 public void testGetDictionaryEmptyLibrary() {
 // Given
 library = new Library();
 // When
 String version = "1.0";
 DictionaryInfo dictionary = library.getDictionary(version);
 // Then
 assertNull("Dictionary for version " + version + " should be null for empty library", dictionary);
 }
}