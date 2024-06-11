package org.fixsuite.message;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Test;
import org.slf4j.Logger;

public class LibraryTest {

 private Library library;
 private static final String LIBRARY_PATH = "path/to/library"; // Replace with the actual library path

 @Test
 public void testLoadFromDirectory() {
 library = new Library();
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryWhenPathIsNull() {
 library = new Library();
 assertFalse(library.loadFromDirectory(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryWhenPathIsEmpty() {
 library = new Library();
 assertFalse(library.loadFromDirectory(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryWhenPathIsNotADirectory() {
 library = new Library();
 assertFalse(library.loadFromDirectory("file.txt"));
 }

 @Test
 public void testGetDictionaries() {
 library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionsaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWhenDictionaryMapIsEmpty() {
 library = new Library();
 assertNull(library.getDictionary(null));
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWhenDictionaryVersionIsNull() {
 library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 assertNull(library.getDictionary(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryWhenDictionaryVersionIsEmpty() {
 library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 assertNull(library.getDictionary(""));
 }

 @Test
 public void testGetDictionary() {
 library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary("FIX 5.0SP1");
 assertNotNull(dictionary);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWhenFilesArrayIsNull() {
 library = new Library();
 library.loadDirectory(null, "version");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWhenFilesArrayIsEmpty() {
 library = new Library();
 File[] emptyFiles = {};
 library.loadDirectory(emptyFiles, "version");
 }

 @Test
 public void testLoadDirectory() {
 library = new Library();
 File[] files = new File("path/to/files").listFiles();
 library.loadDirectory(files, "version");
 }

 @Test
 public void testArrangeFilesWhenFilesArrayIsNull() {
 library = new Library();
 File[] files = null;
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertTrue(arrangedFiles.isEmpty());
 }

 @Test
 public void testArrangeFiles() {
 library = new Library();
 File[] files = new File("path/to/files").listFiles();
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertFalse(arrangedFiles.isEmpty());
 }

 @Test
 public void testParse() {
 DictionaryInfo dictionary = new DictionaryInfo("FIX 5.0SP1");
 File file = new File("path/to/file.xml");
 MainParser.parse(dictionary, file);
 assertNotNull(dictionary.getFields());
 }
}