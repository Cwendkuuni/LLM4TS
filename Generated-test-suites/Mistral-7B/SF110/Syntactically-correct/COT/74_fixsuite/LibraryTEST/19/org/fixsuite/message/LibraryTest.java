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
import org.slf4j.LoggerFactory;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library"; // Replace with the actual library path
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @Test
 public void testLibrary() {
 Library library = new Library();
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryEmptyDirectory() {
 Library library = new Library();
 assertFalse(library.loadFromDirectory(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryNonExistentDirectory() {
 Library library = new Library();
 assertFalse(library.loadFromDirectory("/nonexistent/directory"));
 }

 @Test
 public void testGetDictionariesEmptyLibrary() {
 Library library = new Library();
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertTrue(dictionaries.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 Library library = new Library();
 assertNull(library.getDictionary(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryInvalidVersion() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 assertNull(library.getDictionary("InvalidVersion"));
 }

 @Test
 public void testGetDictionaryValidVersion() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary("ValidVersion");
 assertNotNull(dictionary);
 }

 @Test
 public void testLoadDirectoryEmptyFileList() {
 Library library = new Library();
 List<File> files = new ArrayList<>();
 library.loadDirectory(files, "Version");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectoryNonExistentFile() {
 Library library = new Library();
 List<File> files = new ArrayList<>();
 File nonExistentFile = new File("path/to/nonExistentFile.xml");
 files.add(nonExistentFile);
 library.loadDirectory(files, "Version");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectorySuccess() {
 Library library = new Library();
 List<File> files = new ArrayList<>();
 File fieldsFile = new File("path/to/Fields.xml");
 File enumsFile = new File("path/to/Enums.xml");
 File componentsFile = new File("path/to/Components.xml");
 File msgTypeFile = new File("path/to/MsgType.xml");
 File msgContentsFile = new File("path/to/MsgContents.xml");
 files.add(fieldsFile);
 files.add(enumsFile);
 files.add(componentsFile);
 files.add(msgTypeFile);
 files.add(msgContentsFile);
 library.loadDirectory(files, "Version");
 assertFalse(library.getDictionaries().isEmpty());
 }

 @Test
 public void testArrangeFilesEmptyFileList() {
 Library library = new Library();
 File[] files = {};
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertTrue(arrangedFiles.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrangeFilesNullFiles() {
 Library library = new Library();
 File[] files = null;
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertTrue(arrangedFiles.isEmpty());
 }

 @Test
 public void testArrangeFilesSuccess() {
 Library library = new Library();
 File[] files = {new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml")};
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertFalse(arrangedFiles.isEmpty());
 }
}