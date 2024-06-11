package org.fixsuite.message;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Test;

public class LibraryTest {

 private Library library;

 @Test
 public void testLibraryInstance() {
 Library library = new Library();
 assertTrue(library instanceof Library);
 }

 @Test
 public void testLoadFromDirectoryEmptyDirectory() {
 library = new Library();
 String emptyDirectory = "";
 library.loadFromDirectory(emptyDirectory);
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadFromDirectoryInvalidPath() {
 library = new Library();
 String invalidPath = "/nonexistentdirectory";
 library.loadFromDirectory(invalidPath);
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadFromDirectoryInvalidFileInDirectory() {
 library = new Library();
 String libraryPath = "src/test/resources/LibraryTesting"; // Ensure this directory exists in your project
 File invalidFile = new File(libraryPath, "invalidFile.txt");
 library.loadFromDirectory(libraryPath);
 assertTrue(library.getDictionaries().isEmpty());
 assertFalse(library.getDictionaries().contains(new DictionaryInfo("anyVersion", invalidFile.getName())));
 }

 @Test
 public void testLoadFromDirectoryMultipleFilesInDirectory() {
 library = new Library();
 String libraryPath = "src/test/resources/LibraryTesting"; // Ensure this directory exists in your project
 List<File> files = new ArrayList<>();
 files.add(new File(libraryPath, "Fields.xml"));
 files.add(new File(libraryPath, "Enums.xml"));
 files.add(new File(libraryPath, "Components.xml"));
 files.add(new File(libraryPath, "MsgType.xml"));
 files.add(new File(libraryPath, "MsgContents.xml"));
 library.loadFromDirectory(libraryPath);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(5, dictionaries.size());
 for (DictionaryInfo dictionaryInfo : dictionaries) {
 assertNotNull(dictionaryInfo);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryNullDirectory() {
 library = new Library();
 library.loadFromDirectory(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryNullFiles() {
 library = new Library();
 String libraryPath = "src/test/resources/LibraryTesting"; // Ensure this directory exists in your project
 library.loadFromDirectory(libraryPath, null);
 }

 @Test
 public void testGetDictionaries() {
 library = new Library();
 String libraryPath = "src/test/resources/LibraryTesting"; // Ensure this directory exists in your project
 library.loadFromDirectory(libraryPath);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(5, dictionaries.size());
 for (DictionaryInfo dictionaryInfo : dictionaries) {
 assertNotNull(dictionaryInfo);
 }
 }

 @Test
 public void testGetDictionaryNullVersion() {
 library = new Library();
 library.loadFromDirectory("src/test/resources/LibraryTesting");
 assertNull(library.getDictionary(null));
 }

 @Test
 public void testGetDictionaryInvalidVersion() {
 library = new Library();
 library.loadFromDirectory("src/test/resources/LibraryTesting");
 assertNull(library.getDictionary("invalidVersion"));
 }

 @Test
 public void testGetDictionaryValidVersion() {
 library = new Library();
 library.loadFromDirectory("src/test/resources/LibraryTesting");
 DictionaryInfo dictionaryInfo = library.getDictionary("FIX 4.4");
 assertNotNull(dictionaryInfo);
 }
}