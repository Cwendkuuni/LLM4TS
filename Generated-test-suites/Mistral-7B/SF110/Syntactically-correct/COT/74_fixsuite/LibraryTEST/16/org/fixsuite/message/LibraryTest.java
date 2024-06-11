package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.Library;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library";
 private static Library library;
 private static Logger logger;

 @BeforeClass
 public static void setup() {
 library = new Library();
 logger = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectory() {
 // Test loading from an existing directory
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 // Test loading from an empty directory
 File emptyDir = new File("path/to/empty_directory");
 assertFalse(library.loadFromDirectory(emptyDir.getAbsolutePath()));
 // Test loading from a non-existent directory
 File nonExistentDir = new File("path/to/non_existent_directory");
 assertFalse(library.loadFromDirectory(nonExistentDir.getAbsolutePath()));
 }

 @Test
 public void testGetDictionaries() {
 // Test getting all dictionaries from a loaded library
 List<DictionaryInfo> dictionaries = library.getDictionsaries();
 assertFalse(dictionaries.isEmpty());
 // Test getting an empty list when no dictionaries are loaded
 library.getDictionaries(); // Load an empty library first
 assertEmpty(library.getDictionaries());
 }

 @Test
 public void testGetDictionary() {
 // Test getting a dictionary by version
 String version = "1.0";
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNotNull(dictionary);
 // Test getting a null dictionary when no such version is loaded
 version = "2.0";
 assertNull(library.getDictionary(version));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullDictionary() {
 // Test exception handling when getDictionary is called with null
 library = new Library();
 assertNull(library.getDictionary(null));
 }

 @Test
 public void testLoadDirectory() {
 // Test loading a single file
 List<File> files = new ArrayList<>();
 files.add(new File("path/to/file1.xml"));
 library.loadDirectory(files, "version1");
 assertTrue(library.getDictionary("version1").isLoaded());
 // Test loading multiple files
 files.clear();
 files.add(new File("path/to/file1.xml"));
 files.add(new File("path/to/file2.xml"));
 library.loadDirectory(files, "version2");
 assertTrue(library.getDictionary("version2").isLoaded());
 assertTrue(library.getDictionary("version2").getFiles().size() == 2);
 // Test loading with empty file list
 library.loadDirectory(new ArrayList<>(), "version3");
 assertFalse(library.getDictionary("version3").isLoaded());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWithNullVersion() {
 // Test exception handling when loading with a null version
 library.loadDirectory(new ArrayList<>(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWithNullFiles() {
 // Test exception handling when loading with a null file list
 library.loadDirectory(null, "version1");
 }

 @Test
 public void testArrangeFiles() {
 // Test arranging files with correct order
 File[] originalFiles = {new File("Fields.xml"), new File("Components.xml"), new File("Enums.xml"), new File("MsgType.xml"), new File("MsgContents.xml")};
 List<File> arrangedFiles = library.arrangeFiles(originalFiles);
 assertArrayEquals(originalFiles, arrangedFiles.toArray());
 // Test arranging empty file list
 File[] emptyFiles = {};
 arrangedFiles = library.arrangeFiles(Arrays.asList(emptyFiles));
 assertEmpty(arrangedFiles);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrangeFilesWithNullFiles() {
 // Test exception handling when arranging null files
 library.arrangeFiles(null);
 }
}