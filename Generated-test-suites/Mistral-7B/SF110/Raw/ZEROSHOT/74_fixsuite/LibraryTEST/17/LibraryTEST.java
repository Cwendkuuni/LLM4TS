package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LibraryTest {

 private Library library;
 private List<File> arrangedFiles;
 private static final String LIBRARY_PATH = "path/to/library";

 @Before
 public void setUp() {
 library = new Library();
 arrangedFiles = new ArrayList<>();
 }

 @Test
 public void testLoadFromDirectory() {
 final File libraryPath = new File(LIBRARY_PATH);
 assertTrue(libraryPath.isDirectory());
 library.loadFromDirectory(LIBRARY_PATH);
 assertFalse(library.dictionaries.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryEmptyDirectory() {
 File emptyDirectory = new File("path/to/empty/directory");
 assertFalse(emptyDirectory.isDirectory());
 library.loadFromDirectory(emptyDirectory.getAbsolutePath());
 }

 @Test
 public void testGetDictionaries() {
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 String version = "testVersion";
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNull(dictionary);

 library.loadFromDirectory(LIBRARY_PATH);
 dictionary = library.getDictionary(version);
 assertNotNull(dictionary);
 }

 @Test
 public void testLoadDirectory() {
 arrangedFiles = library.arrangeFiles(new File("path/to/library/testDirectory/").listFiles());
 library.loadDirectory(arrangedFiles, "testVersion");
 assertFalse(library.dictionaries.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryEmptyList() {
 library.loadDirectory(new ArrayList<>(), "testVersion");
 }

 @Test
 public void testArrangeFiles() {
 File[] files = new File("path/to/library/testDirectory/").listFiles();
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertEquals(5, arrangedFiles.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrangeFilesNullFiles() {
 List<File> nullFiles = null;
 library.arrangeFiles(nullFiles);
 }

 @Test
 public void testParseDictionary() {
 DictionaryInfo dictionary = new DictionaryInfo("testVersion");
 arrangedFiles = library.arrangeFiles(new File("path/to/library/testDirectory/").listFiles());
 MainParser.parse(dictionary, arrangedFiles.get(0));
 assertTrue(dictionary.isLoaded());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDictionaryNullFile() {
 DictionaryInfo dictionary = new DictionaryInfo("testVersion");
 MainParser.parse(dictionary, null);
 }
}