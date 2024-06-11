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

 @Test
 public void testLibraryInstance() {
 Library library = new Library();
 assertNotNull(library);
 }

 @Test
 public void testLoadFromDirectory_EmptyDirectory() {
 Library library = new Library();
 library.loadFromDirectory("");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadFromDirectory_InvalidPath() {
 Library library = new Library();
 library.loadFromDirectory("invalidPath");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadFromDirectory_ValidDirectory() {
 // Given
 File directory = new File("path/to/valid/directory");
 // When
 Library library = new Library();
 library.loadFromDirectory(directory.getAbsolutePath());
 // Then
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectory_NullLibraryPath() {
 Library library = new Library();
 library.loadFromDirectory(null);
 }

 @Test
 public void testGetDictionaries() {
 Library library = new Library();
 library.loadFromDirectory("path/to/valid/directory");
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary_ValidVersion() {
 Library library = new Library();
 library.loadFromDirectory("path/to/valid/directory");
 DictionaryInfo dictionary = library.getDictionary("1.0");
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionary_NullVersion() {
 Library library = new Library();
 library.loadFromDirectory("path/to/valid/directory");
 DictionaryInfo dictionary = library.getDictionary(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionary_InvalidVersion() {
 Library library = new Library();
 library.loadFromDirectory("path/to/valid/directory");
 DictionaryInfo dictionary = library.getDictionary("InvalidVersion");
 }

 @Test
 public void testLoadDirectory_EmptyFileList() {
 Library library = new Library();
 List<File> files = new ArrayList<File>();
 library.loadDirectory(files, "version");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectory_InvalidFileExtension() {
 Library library = new Library();
 File file = new File("path/to/file.txt");
 List<File> files = new ArrayList<File>();
 files.add(file);
 library.loadDirectory(files, "version");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectory_ValidFileList() {
 // Given
 File fieldsFile = new File("path/to/Fields.xml");
 File enumsFile = new File("path/to/Enums.xml");
 File componentsFile = new File("path/to/Components.xml");
 File msgTypeFile = new File("path/to/MsgType.xml");
 File msgContentsFile = new File("path/to/MsgContents.xml");
 List<File> files = new ArrayList<File>();
 files.add(fieldsFile);
 files.add(enumsFile);
 files.add(componentsFile);
 files.add(msgTypeFile);
 files.add(msgContentsFile);
 // When
 Library library = new Library();
 library.loadDirectory(files, "version");
 // Then
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

}