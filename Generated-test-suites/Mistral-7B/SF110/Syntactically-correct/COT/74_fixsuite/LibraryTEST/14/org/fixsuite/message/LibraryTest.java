package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.junit.Test;
import org.slf4j.*;

import java.io.*;
import java.util.*;

public class LibraryTest {

 private static final String LIBRARY_PATH = "testData"; // Replace with the actual library path
 private static final Library library = new Library();

 @Test
 public void testLoadFromDirectory() {
 // Test when libraryPath is a directory
 File libraryDirectory = new File(LIBRARY_PATH);
 assertTrue(libraryDirectory.isDirectory());
 boolean result = library.loadFromDirectory(LIBRARY_PATH);
 assertTrue(result);
 // Test when libraryPath is empty
 File emptyLibraryPath = new File("");
 assertFalse(library.loadFromDirectory(emptyLibraryPath.getAbsolutePath()));
 // Test when libraryPath is not a directory
 File nonDirectoryFile = new File("testData/nonDirectoryFile.txt");
 assertFalse(library.loadFromDirectory(nonDirectoryFile.getAbsolutePath()));
 }

 @Test
 public void testGetDictionaries() {
 // Test when dictionaries are loaded
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 // Test when dictionaries are not loaded
 library.loadFromDirectory(LIBRARY_PATH); // Load dictionaries first
 library.getDictionaries(); // Get dictionaries
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWithNullVersion() {
 library.loadFromDirectory(LIBRARY_PATH);
 assertNull(library.getDictionary(null));
 }

 @Test
 public void testGetDictionaryWithValidVersion() {
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary("1.0");
 assertNotNull(dictionary);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryWithInvalidVersion() {
 library.loadFromDirectory(LIBRARY_PATH);
 library.getDictionary("invalidVersion");
 }

 @Test
 public void testLoadDirectory() {
 // Test when files are arranged properly
 File libraryDirectory = new File(LIBRARY_PATH);
 File[] files = libraryDirectory.listFiles();
 List<File> arrangedFiles = library.arrangeFiles(files);
 List<Integer> expectedKeys = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> actualKeys = new ArrayList<>();
 for (File file : arrangedFiles) {
 actualKeys.add(getFileKey(file.getName()));
 }
 assertTrue(actualKeys.equals(expectedKeys));
 // Test when files are not arranged properly
 File misplacedFile = new File("misplacedFile.xml");
 arrangedFiles.add(0, misplacedFile);
 List<Integer> misplacedKeys = library.arrangeFiles(arrangedFiles.toArray(new File[0]));
 assertFalse(misplacedKeys.equals(expectedKeys));
 }

 private int getFileKey(String fileName) {
 switch (fileName) {
 case "Fields.xml":
 return FIELD_FILE_KEY;
 case "Enums.xml":
 return ENUMS_FILE_KEY;
 case "Components.xml":
 return COMPONENTS_FILE_KEY;
 case "MsgType.xml":
 return MSG_TYPE_FILE_KEY;
 case "MsgContents.xml":
 return MSG_CONTENTS_FILE_KEY;
 default:
 throw new IllegalArgumentException("Invalid file name: " + fileName);
 }
}

}