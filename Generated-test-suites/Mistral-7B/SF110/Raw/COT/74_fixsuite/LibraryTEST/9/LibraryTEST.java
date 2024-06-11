package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.*;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private Library library;
 static final String LIBRARY_PATH = "test/resources/library "; // Replace with your test directory path

 @Test
 public void testLibraryInstance() {
 library = new Library();
 assertNotNull(library);
 }

 @Test
 public void testLoadFromDirectory_EmptyDirectory() {
 library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadFromDirectory_ValidDirectory() {
 File libraryDirectory = new File(LIBRARY_PATH);
 File[] files = libraryDirectory.listFiles();
 List<File> arrangedFiles = library.arrangeFiles(files);
 library = new Library();
 library.loadFromDirectory(Collections.singletonList(new File(LIBRARY_PATH)));
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(arrangedFiles.size(), dictionaries.size());
 for (DictionaryInfo dictionary : dictionaries) {
 // Perform more checks on dictionary objects as needed
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectory_InvalidPath() {
 library = new Library();
 library.loadFromDirectory("");
 fail("Expected IllegalArgumentException for empty path");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectory_InvalidDirectory() {
 library = new Library();
 library.loadFromDirectory(new File("test/resources/invalid"));
 fail("Expected IllegalArgumentException for invalid directory");
 }

 @Test
 public void testGetDictionaries() {
 library = new Library();
 library.loadFromDirectory(new File(LIBRARY_PATH));
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 library = new Library();
 library.loadFromDirectory(new File(LIBRARY_PATH));
 DictionaryInfo dictionary = library.getDictionary("testVersion");
 assertNull(dictionary);
 }

 @Test
 public void testGetDictionary_ValidVersion() {
 library = new Library();
 library.loadFromDirectory(new File(LIBRARY_PATH));
 DictionaryInfo dictionary = library.getDictionary(Library.DEFAULT_VERSION);
 assertNotNull(dictionary);
 }

 @Test
 public void testLoadDirectory() {
 List<File> arrangedFiles = new ArrayList<>();
 arrangedFiles.add(new File("Fields.xml"));
 arrangedFiles.add(new File("Enums.xml"));
 arrangedFiles.add(new File("Components.xml"));
 arrangedFiles.add(new File("MsgType.xml"));
 arrangedFiles.add(new File("MsgContents.xml"));
 DictionaryInfo dictionary = new DictionaryInfo("testVersion");
 library = new Library();
 library.loadDirectory(arrangedFiles, "testVersion");
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(1, dictionaries.size());
 assertEquals(dictionary, dictionaries.get(0));
 }
}