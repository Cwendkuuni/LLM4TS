package org.fixsuite.message.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.DictionaryInfo;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.FieldsInfo;
import org.fixsuite.message.info.EnumsInfo;
import org.fixsuite.message.info.ComponentsInfo;
import org.fixsuite.message.info.MsgTypeInfo;
import org.fixsuite.message.info.MsgContentsInfo;
import org.junit.Test;

public class LibraryTest {

 private final String libraryPath = "path/to/library";
 private final Library library = new Library();

 @Test
 public void testLoadFromDirectory() {
 // Test loading dictionaries from a valid directory
 assertTrue(library.loadFromDirectory(libraryPath));

 // Test getting loaded dictionaries
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());

 // Test getting a specific dictionary
 DictionaryInfo dictionary = library.getDictionary("version");
 assertNotNull(dictionary);
 }

 @Test
 public void testLoadFromEmptyDirectory() {
 // Test loading dictionaries from an empty directory
 assertFalse(library.loadFromDirectory(libraryPath));
 }

 @Test
 public void testLoadFromInvalidPath() {
 // Test loading dictionaries from an invalid path
 assertFalse(library.loadFromDirectory("/invalid/path"));
 }

 @Test
 public void testGetDictionaries() {
 // Test getting all loaded dictionaries
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 // Test getting a specific dictionary
 DictionaryInfo dictionary = library.getDictionary("version");
 assertNotNull(dictionary);
 }

 @Test
 public void testParseFiles() {
 // Test parsing files in a dictionary
 File directory = new File(libraryPath);
 List<File> files = new ArrayList<File>();
 File[] fileArray = directory.listFiles();
 for (File file : fileArray) {
 files.add(file);
 }
 List<File> arrangedFiles = library.arrangeFiles(fileArray);
 for (File file : arrangedFiles) {
 Library.loadDirectory(library, file.getAbsolutePath());
 }

 // Test getting parsed data from the first dictionary
 DictionaryInfo dictionary = library.getDictionary(Library.FIELDS_FILE_NAME.split("\\.")[0]);
 assertNotNull(dictionary);
 FieldsInfo fieldsInfo = (FieldsInfo) dictionary.getInfo(Library.FIELDS_FILE_KEY);
 assertNotNull(fieldsInfo);
 }
}