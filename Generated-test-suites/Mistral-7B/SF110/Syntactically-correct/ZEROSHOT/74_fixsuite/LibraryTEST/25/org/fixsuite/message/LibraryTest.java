package org.fixsuite.message.test;

import static org.junit.Assert.*;
import org.fixsuite.message.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
 // Test with valid library path
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));

 // Test with empty library path
 assertFalse(library.loadFromDirectory(""));

 // Test with null library path
 assertFalse(library.loadFromDirectory(null));

 // Test with non-existent library path
 assertFalse(library.loadFromDirectory("non-existent-path"));
 }

 @Test
 public void testGetDictionaries() {
 // Test loading dictionaries from valid library path
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());

 // Test getting dictionaries after unloading previous ones
 library.loadFromDirectory(LIBRARY_PATH);
 library.getDictionaries().clear();
 List<DictionaryInfo> dictionaries2 = library.getDictionaries();
 assertTrue(dictionaries2.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 // Test getting dictionary with valid version
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 String version = dictionaries.get(0).getVersion();
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNotNull(dictionary);

 // Test getting dictionary with invalid version
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries2 = library.getDictionaries();
 String invalidVersion = "invalid-version";
 dictionary = library.getDictionary(invalidVersion);
 assertNull(dictionary);
 }

 @Test
 public void testLoadDirectory() {
 // Test loading dictionaries from valid files
 File libraryDirectory = new File(LIBRARY_PATH);
 File[] files = libraryDirectory.listFiles();
 List<File> arrangedFiles = new ArrayList<>();
 for (File file : files) {
 arrangedFiles.add(file);
 }
 library.loadDirectory(arrangedFiles, "version");

 // Test loading empty directory
 library.loadDirectory(Collections.emptyList(), "version");

 // Test loading null directory
 library.loadDirectory(null, "version");
 }

 @Test
 public void testArrangeFiles() {
 // Test arranging files with valid files
 File libraryDirectory = new File("path/to/library");
 File[] files = libraryDirectory.listFiles();
 Map<Integer, File> arrangedFiles = new TreeMap<>();
 for (File file : files) {
 arrangedFiles.put(getFileKey(file.getName()), file);
 }
 List<File> arrangedFilesList = new ArrayList<>(arrangedFiles.values());
 List<File> arrangedFilesTest = library.arrangeFiles(files);
 assertEquals(arrangedFilesList, arrangedFilesTest);

 // Test arranging empty directory
 File emptyLibraryDirectory = new File("path/to/empty-library");
 List<File> emptyArrangedFiles = library.arrangeFiles(emptyLibraryDirectory.listFiles());
 assertEquals(Collections.emptyList(), emptyArrangedFiles);

 // Test arranging null directory
 List<File> nullArrangedFiles = library.arrangeFiles(null);
 assertEquals(Collections.emptyList(), nullArrangedFiles);
 }

 private int getFileKey(String fileName) {
 switch (fileName) {
 case "Fields.xml":
 return Library.FIELDS_FILE_KEY;
 case "Enums.xml":
 return Library.ENUMS_FILE_KEY;
 case "Components.xml":
 return Library.COMPONENTS_FILE_KEY;
 case "MsgType.xml":
 return Library.MSG_TYPE_FILE_KEY;
 case "MsgContents.xml":
 return Library.MSG_CONTENTS_FILE_KEY;
 default:
 return -1;
 }
 }
}