package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LibraryTest {

 private Library library;

 @Test
 public void testLibraryInstance() {
 Library library = new Library();
 assertNotNull(library);
 }

 @Test
 public void testLoadFromDirectory() throws Exception {
 String libraryPath = "testData"; // Replace with the valid path to your test data directory
 Library library = new Library();
 assertTrue(library.loadFromDirectory(libraryPath));
 Collection<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryWhenPathIsNull() {
 Library library = new Library();
 assertFalse(library.loadFromDirectory(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectoryWhenPathIsNotADirectory() {
 Library library = new Library();
 assertFalse(library.loadFromDirectory("testData/testFile.txt"));
 }

 @Test(expected = IOException.class)
 public void testLoadFromDirectoryWhenFileIsNotFound() {
 Library library = new Library();
 String libraryPath = "testData"; // Replace with the valid path to your test data directory
 File file = new File(libraryPath, "nonExistentFile.xml");
 assertFalse(library.loadFromDirectory(libraryPath));
 try {
 library.loadDirectory(Arrays.asList(file.listFiles()), file.getName());
 fail();
 } catch (NullPointerException e) {
 // Expected as the listFiles() method may return null in this case
 }
 }

 @Test
 public void testGetDictionaries() {
 Library library = new Library();
 library.loadFromDirectory("testData");
 Collection<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 Library library = new Library();
 library.loadFromDirectory("testData");
 DictionaryInfo dictionary = library.getDictionary("1.0.0");
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWhenDictionaryIsNotFound() {
 Library library = new Library();
 library.loadFromDirectory("testData");
 DictionaryInfo dictionary = library.getDictionary(null);
 fail();
 }

 @Test
 public void testLoadDirectory() {
 String version = "1.0.0";
 List<File> files = new ArrayList<File>() {{
 add(new File("testData/Fields.xml"));
 add(new File("testData/Enums.xml"));
 add(new File("testData/Components.xml"));
 add(new File("testData/MsgType.xml"));
 add(new File("testData/MsgContents.xml"));
 }};
 Library library = new Library();
 library.loadDirectory(files, version);
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testLoadDirectoryWhenFilesIsEmpty() {
 List<File> files = new ArrayList<File>();
 Library library = new Library();
 library.loadDirectory(files, "1.0.0");
 fail();
 }

 @Test(expected = NullPointerException.class)
 public void testLoadDirectoryWhenFilesIsNull() {
 Library library = new Library();
 library.loadDirectory(null, "1.0.0");
 fail();
 }

 @Test(expected = NullPointerException.class)
 public void testLoadDirectoryWhenVersionIsNull() {
 List<File> files = new ArrayList<File>() {{
 add(new File("testData/Fields.xml"));
 add(new File("testData/Enums.xml"));
 add(new File("testData/Components.xml"));
 add(new File("testData/MsgType.xml"));
 add(new File("testData/MsgContents.xml"));
 }};
 Library library = new Library();
 library.loadDirectory(files, null);
 fail();
 }

 @Test
 public void testArrangeFiles() {
 File directory = new File("testData");
 File[] files = directory.listFiles();
 Map<Integer, File> expectedFiles = new HashMap<Integer, File>() {{
 put(1, new File("testData/Fields.xml"));
 put(2, new File("testData/Enums.xml"));
 put(3, new File("testData/Components.xml"));
 put(4, new File("testData/MsgType.xml"));
 put(5, new File("testData/MsgContents.xml"));
 }};
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertFalse(arrangedFiles.isEmpty());
 for (int i = 0; i < arrangedFiles.size(); i++) {
 File expectedFile = expectedFiles.get(i + 1);
 File actualFile = arrangedFiles.get(i);
 assertEquals(expectedFile.getName(), actualFile.getName());
 }
 }

}