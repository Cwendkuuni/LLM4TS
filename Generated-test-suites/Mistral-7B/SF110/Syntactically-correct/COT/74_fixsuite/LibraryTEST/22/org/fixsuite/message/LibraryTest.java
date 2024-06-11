package org.fixsuite.message;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Test;
import org.slf4j.Logger;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library"; // Replace with the actual library path
 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Test normal scenario
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 // Test empty directory
 assertFalse(library.loadFromDirectory(""));
 // Test null directory
 assertFalse(library.loadFromDirectory(null));
 // Test non-existent directory
 assertFalse(library.loadFromDirectory("/non/existent/directory"));
 }

 @Test
 public void testGetDictionaries() {
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary("1.0");
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 library.loadFromDirectory(LIBRARY_PATH);
 library.getDictionary(null);
 }

 @Test
 public void testLoadDirectoryEmptyFiles() {
 List<File> files = Collections.emptyList();
 library.loadDirectory(files, "1.0");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectoryNullFiles() {
 library.loadDirectory(null, "1.0");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectoryEmptyDirectory() {
 File directory = new File("");
 library.loadDirectory(Collections.emptyList(), directory.getName());
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectoryInvalidFileNames() {
 File directory = new File("path/to/invalid_files");
 List<File> files = Arrays.asList(new File("invalid_file_1.xml"), new File("invalid_file_2.xml"));
 library.loadDirectory(files, directory.getName());
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectoryMismatchedFiles() {
 File directory = new File("path/to/mismatched_files");
 Map<Integer, File> arrangedFiles = new HashMap<>();
 arrangedFiles.put(1, new File("Fields.xml"));
 arrangedFiles.put(3, new File("Components.xml"));
 library.loadDirectory(new ArrayList<File>(arrangedFiles.values()), directory.getName());
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectoryMultipleVersions() {
 File directory = new File("path/to/multiple_versions");
 List<File> files = Arrays.asList(
 new File("Fields_1.0.xml"), new File("Fields_1.1.xml"), new File("Components.xml"), new File("Components_1.1.xml"));
 library.loadDirectory(files, directory.getName());
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(2, dictionaries.size());
 }
}