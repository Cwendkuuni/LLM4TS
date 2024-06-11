package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library";
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Test loading from valid directory
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 // Test loading empty directory
 assertFalse(library.loadFromDirectory(""));
 // Test loading non-existent directory
 assertFalse(library.loadFromDirectory("non-existent-directory"));
 // Test loading null directory
 assertFalse(library.loadFromDirectory(null));
 }

 @Test
 public void testGetDictionaries() {
 // Test getting empty dictionary list
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 assertEquals(0, library.getDictionaries().size());
 // Test getting populated dictionary list
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 // Test getting dictionary with valid version
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary("1.0");
 assertNotNull(dictionary);
 // Test getting dictionary with invalid version
 library.loadFromDirectory(LIBRARY_PATH);
 dictionary = library.getDictionary("2.0");
 assertNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWithNullLibrary() {
 Library nullLibrary = null;
 assertNull(nullLibrary.getDictionary("1.0"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryWithInvalidVersion() {
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary(null);
 }

 @Test
 public void testLoadDirectory() {
 // Test loading dictionary from single file
 List<File> files = new ArrayList<>();
 files.add(new File("Fields.xml"));
 library.loadDirectory(files, "1.0");
 assertFalse(library.getDictionaries().isEmpty());
 // Test loading dictionary from multiple files
 files = new ArrayList<>();
 files.add(new File("Fields.xml"));
 files.add(new File("Enums.xml"));
 library.loadDirectory(files, "1.0");
 assertFalse(library.getDictionaries().isEmpty());
 // Test loading dictionary with empty file list
 files = new ArrayList<>();
 library.loadDirectory(files, "1.0");
 assertTrue(library.getDictionaries().isEmpty());
 // Test loading dictionary with null file list
 library.loadDirectory(null, "1.0");
 assertTrue(library.getDictionaries().isEmpty());
 // Test loading dictionary with null version
 library.loadDirectory(Collections.singletonList(new File("Fields.xml")), null);
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testLoadDirectoryWithNullFiles() {
 List<File> nullFiles = null;
 library.loadDirectory(nullFiles, "1.0");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWithNullVersion() {
 List<File> files = new ArrayList<>();
 files.add(new File("Fields.xml"));
 library.loadDirectory(files, null);
 }

 @Test
 public void testArrangeFiles() {
 // Test arranging files with valid file names
 File[] files = new File[] {new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml")};
 List<File> arrangedFiles = library.arrangeFiles(files);
 List<Integer> expectedKeys = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> actualKeys = new ArrayList<>();
 for (File file : arrangedFiles) {
 actualKeys.add(library.getLibraryKey(file.getName()));
 }
 assertEquals(expectedKeys, actualKeys);
 // Test arranging empty file array
 arrangedFiles = library.arrangeFiles(new File[0]);
 assertTrue(arrangedFiles.isEmpty());
 // Test arranging null file array
 arrangedFiles = library.arrangeFiles(null);
 assertTrue(arrangedFiles.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testArrangeFilesWithNullFiles() {
 List<File> nullFiles = null;
 library.arrangeFiles(nullFiles);
 }
}