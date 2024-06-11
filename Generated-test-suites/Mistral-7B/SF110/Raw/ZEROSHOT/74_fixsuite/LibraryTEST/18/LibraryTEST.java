package org.fixsuite.message;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Test;

public class LibraryTest {

 private Library library;

 @Before
 public void setUp() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 String libraryPath = "testData/library"; // Replace with the actual path to your test data
 boolean result = library.loadFromDirectory(libraryPath);
 assertTrue(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryNullPath() {
 library.loadFromDirectory(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryEmptyPath() {
 File path = new File("");
 library.loadFromDirectory(path.getAbsolutePath());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryNotADirectory() {
 File file = new File("testData/library/notADirectory.txt");
 library.loadFromDirectory(file.getAbsolutePath());
 }

 @Test
 public void testGetDictionaries() {
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertNotNull(dictionaries);
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 String version = "FIX 4.4";
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNotNull(dictionary);
 assertNotNull(dictionary.getVersion());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 library.getDictionary(null);
 }

 @Test
 public void testLoadDirectory() {
 List<File> files = new ArrayList<>();
 files.add(new File("Fields.xml"));
 files.add(new File("Enums.xml"));
 files.add(new File("Components.xml"));
 files.add(new File("MsgType.xml"));
 files.add(new File("MsgContents.xml"));
 library.loadDirectory(files, "testVersion");
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertTrue(dictionaries.size() > 0);
 }

 @Test(expected = NullPointerException.class)
 public void testLoadDirectoryNullFiles() {
 library.loadDirectory(null, "testVersion");
 }

 @Test
 public void testArrangeFiles() {
 File directory = new File("testData/library");
 File[] files = directory.listFiles();
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertEquals(5, arrangedFiles.size());
 }

 @Test
 public void testArrangeFilesNullFiles() {
 File[] files = null;
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertEquals(0, arrangedFiles.size());
 }
}