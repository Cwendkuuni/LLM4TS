package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.Library;
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
 private Logger log;

 @Before
 public void setUp() {
 library = new Library();
 log = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectory() {
 // Test loading from a valid directory
 String libraryPath = "test/resources/validDirectory";
 assertTrue(library.loadFromDirectory(libraryPath));

 // Test loading from an empty directory
 String emptyLibraryPath = "test/resources/emptyDirectory";
 assertFalse(library.loadFromDirectory(emptyLibraryPath));

 // Test loading from a non-existent directory
 String nonExistentLibraryPath = "test/resources/nonExistentDirectory";
 assertFalse(library.loadFromDirectory(nonExistentLibraryPath));
 }

 @Test
 public void testGetDictionaries() {
 // Test getting dictionaries after loading from a valid directory
 String libraryPath = "test/resources/validDirectory";
 library.loadFromDirectory(libraryPath);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());

 // Test getting dictionaries before loading from a directory
 assertNull(library.getDictionaries());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWithNullVersion() {
 library.getDictionary(null);
 }

 @Test
 public void testGetDictionary() {
 // Test getting dictionary with valid version
 String libraryPath = "test/resources/validDirectory";
 library.loadFromDirectory(libraryPath);
 DictionaryInfo dictionary = library.getDictionary("FIX 4.4");
 assertNotNull(dictionary);

 // Test getting dictionary with invalid version
 String invalidVersion = "INVALID_VERSION";
 assertNull(library.getDictionary(invalidVersion));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWithNullFiles() {
 library.loadDirectory(null, "version");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWithEmptyFiles() {
 List<File> emptyFiles = new ArrayList<>();
 library.loadDirectory(emptyFiles, "version");
 }

 @Test
 public void testLoadDirectory() {
 // Test loading dictionaries from files with valid names
 String libraryPath = "test/resources/validDirectory";
 library.loadFromDirectory(libraryPath);
 List<File> arrangedFiles = library.arrangeFiles(libraryPath.toPath().toFile().listFiles());
 List<File> expectedFiles = new ArrayList<>();
 expectedFiles.add(new File("Fields.xml"));
 expectedFiles.add(new File("Enums.xml"));
 expectedFiles.add(new File("Components.xml"));
 expectedFiles.add(new File("MsgType.xml"));
 expectedFiles.add(new File("MsgContents.xml"));
 assertEquals(expectedFiles, arrangedFiles);

 // Test loading dictionaries from files with invalid names
 String invalidLibraryPath = "test/resources/invalidDirectory";
 library = new Library();
 File[] files = invalidLibraryPath.toPath().toFile().listFiles();
 List<File> invalidFiles = new ArrayList<>();
 for (File file : files) {
 invalidFiles.add(file);
 }
 library.loadDirectory(invalidFiles, "version");
 assertFalse(library.getDictionaries().isEmpty());
 }

 @Test(expected = IOException.class)
 public void testParseFileWithNullFile() {
 MainParser.parse(null, new File("test.xml"));
 }

 @Test
 public void testParseFile() {
 // Test parsing a valid file
 File validFile = new File("test/resources/validFile.xml");
 MainParser.parse(new DictionaryInfo("1.0"), validFile);

 // Test parsing an empty file
 File emptyFile = new File("test/resources/emptyFile.xml");
 MainParser.parse(new DictionaryInfo("1.0"), emptyFile);
 }
}