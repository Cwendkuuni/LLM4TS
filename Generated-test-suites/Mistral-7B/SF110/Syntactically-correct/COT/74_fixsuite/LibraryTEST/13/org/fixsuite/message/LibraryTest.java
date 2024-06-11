package org.fixsuite.message.test;

import static org.junit.Assert.*;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static final String LIBRARY_PATH = "library_path"; // Replace with the actual directory path
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @Test
 public void testLibraryInstance() {
 Library library = new Library();
 assertNotNull(library);
 }

 @Test
 public void testLoadFromDirectory_EmptyDirectory() {
 Library library = new Library();
 boolean result = library.loadFromDirectory(LIBRARY_PATH);
 assertFalse(result);
 }

 @Test
 public void testLoadFromDirectory_NoFiles() {
 Library library = new Library();
 File file = new File(LIBRARY_PATH);
 file.setWritable(true);
assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 assertEquals(0, library.getDictionaries().size());
 }

 @Test
 public void testLoadFromDirectory_InvalidFile() {
 Library library = new Library();
 File directory = new File(LIBRARY_PATH);
 directory.mkdir();
 File invalidFile = new File(directory, "invalid_file.txt");
 invalidFile.createNewFile();
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 assertFalse(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadFromDirectory_MixedFiles() {
 Library library = new Library();
 File directory = new File(LIBRARY_PATH);
 directory.mkdir();
 File[] files = new File[] {
 new File(directory, "Fields.xml"),
 new File(directory, "Components.xml"),
 new File(directory, "MsgType.xml"),
 new File(directory, "MsgContents.xml"),
 new File(directory, "Enums.xml"),
 new File(directory, "invalid_file.txt")
 };
 Arrays.sort(files);
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertEquals(5, arrangedFiles.size());
 List<File> expectedFiles = Arrays.asList(
 new File("Fields.xml"),
 new File("Enums.xml"),
 new File("Components.xml"),
 new File("MsgType.xml"),
 new File("MsgContents.xml")
 );
 assertEquals(expectedFiles, arrangedFiles);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectory_NullDirectoryPath() {
 Library library = new Library();
 library.loadFromDirectory(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectory_NullFiles() {
 Library library = new Library();
 File directory = new File(LIBRARY_PATH);
 library.loadFromDirectory(null, directory);
 }

 @Test
 public void testGetDictionaries() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertNotNull(dictionaries);
 assertTrue(dictionaries.size() > 0);
 }

 @Test
 public void testGetDictionary() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary("version");
 assertNotNull(dictionary);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionary_NullVersion() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 library.getDictionary(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionary_InvalidVersion() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 library.getDictionary("invalid_version");
 }

 @Test
 public void testDictionaryLoading() throws IOException {
 Library library = new Library();
 File dictionaryFile = new File("test_dictionary.xml"); // Replace with the actual path to a valid dictionary file
 dictionaryFile.createNewFile();
 try {
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 DictionaryInfo dictionary = dictionaries.get(0);
 assertNotNull(dictionary);
 assertTrue(dictionary.isLoaded());
 }
 finally {
 dictionaryFile.delete();
 }
 }
}