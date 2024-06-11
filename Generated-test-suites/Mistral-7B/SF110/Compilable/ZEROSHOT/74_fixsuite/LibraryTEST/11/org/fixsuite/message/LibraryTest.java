package org.fixsuite.message;

import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LibraryTest {

 private Library library;
 private File tempDirectory;

 @Before
 public void setUp() {
 tempDirectory = new File("target/test-classes/LibraryTest");
 tempDirectory.mkdirs();
 }

 @Test
 public void testLoadFromDirectorySuccess() {
 // Arrange
 library = new Library();
 String libraryPath = tempDirectory.getAbsolutePath();
 File[] files = new File[] {
 new File(tempDirectory, "Fields.xml"),
 new File(tempDirectory, "Enums.xml"),
 new File(tempDirectory, "Components.xml"),
 new File(tempDirectory, "MsgType.xml"),
 new File(tempDirectory, "MsgContents.xml")
 };
 List<File> arrangedFiles = new ArrayList<File>();
 for (File file : files) {
 arrangedFiles.add(file);
 }
 // Act
 library.loadFromDirectory(libraryPath);
 // Assert
 assertNotNull(library.getDictionaries());
 assertTrue(library.getDictionaries().size() > 0);
 }

 @Test
 public void testLoadFromDirectoryEmptyDirectory() {
 // Arrange
 library = new Library();
 String libraryPath = tempDirectory.getAbsolutePath();
 // Act & Assert
 assertFalse(library.loadFromDirectory(libraryPath));
 }

 @Test
 public void testLoadFromDirectoryInvalidDirectory() {
 // Arrange
 library = new Library();
 String libraryPath = "invalid_directory";
 // Act & Assert
 assertFalse(library.loadFromDirectory(libraryPath));
 }

 @Test
 public void testGetDictionary() {
 // Arrange
 library = new Library();
 library.loadFromDirectory(tempDirectory.getAbsolutePath());
 String dictionaryVersion = "1.0";
 // Act
 DictionaryInfo dictionary = library.getDictionary(dictionaryVersion);
 // Assert
 assertNull(dictionary);
 }

 @Test
 public void testGetDictionaryValidVersion() {
 // Arrange
 library = new Library();
 library.loadFromDirectory(tempDirectory.getAbsolutePath());
 String dictionaryVersion = "1.0";
 // Act
 DictionaryInfo dictionary = library.getDictionary(dictionaryVersion);
 // Assert
 assertNotNull(dictionary);
 }

 @Test
 public void testGetDictionaries() {
 // Arrange
 library = new Library();
 library.loadFromDirectory(tempDirectory.getAbsolutePath());
 // Act
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 // Assert
 assertNotNull(dictionaries);
 assertFalse(dictionaries.isEmpty());
 }
}