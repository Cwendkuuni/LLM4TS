package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.*;
import org.junit.Test;
import org.slf4j.Logger;

public class LibraryTest {

 private static final String TEST_DATA_DIRECTORY = "testData";
 private static Library library;

 @Test
 public void testLibraryInstance() {
 Library testLibrary = new Library();
 assertNotNull(testLibrary);
 }

 @Test
 public void testLoadFromDirectoryEmptyDirectory() {
 library = new Library();
 library.loadFromDirectory("");
 assertFalse(library.getDictionaries().size() > 0);
 }

 @Test
 public void testLoadFromDirectoryInvalidPath() {
 library = new Library();
 library.loadFromDirectory("InvalidPath");
 assertFalse(library.getDictionaries().size() > 0);
 }

 @Test
 public void testLoadFromDirectoryWithFiles() throws IOException {
 File testDirectory = new File(TEST_DATA_Directory);
 library = new Library();
 library.loadFromDirectory(testDirectory.getAbsolutePath());
 assertEquals(5, library.getDictionaries().size());
 }

 @Test
 public void testLoadFromDirectoryWithFilesEmptyDirectory() throws IOException {
 File testDirectory = new File(TEST_DATA_Directory + "/emptyDirectory");
 library = new Library();
 library.loadFromDirectory(testDirectory.getAbsolutePath());
 assertFalse(library.getDictionaries().size() > 0);
 }

 @Test
 public void testGetDictionaries() {
 Library testLibrary = new Library();
 testLibrary.loadFromDirectory(new File("testData").getAbsolutePath());
 List<DictionaryInfo> dictionaries = testLibrary.getDictionaries();
 assertNotNull(dictionaries);
 }

 @Test
 public void testGetDictionary() {
 Library testLibrary = new Library();
 testLibrary.loadFromDirectory(new File("testData").getAbsolutePath());
 DictionaryInfo dictionary = testLibrary.getDictionary("FIX 4.4");
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 Library testLibrary = new Library();
 testLibrary.loadFromDirectory(new File("testData").getAbsolutePath());
 DictionaryInfo dictionary = testLibrary.getDictionary(null);
 }

 @Test
 public void testGetDictionaryNonExistentVersion() {
 Library testLibrary = new Library();
 testLibrary.loadFromDirectory(new File("testData").getAbsolutePath());
 DictionaryInfo dictionary = testLibrary.getDictionary("FIX 5.0");
 assertNull(dictionary);
 }

 @Test
 public void testLoadDirectory() throws IOException {
 Library testLibrary = new Library();
 List<File> files = Arrays.asList(new File("testData/Fields.xml"), new File("testData/Enums.xml"),
 new File("testData/Components.xml"), new File("testData/MsgType.xml"), new File("testData/MsgContents.xml"));
 testLibrary.loadDirectory(files, "FIX 4.4");
 List<DictionaryInfo> dictionaries = testLibrary.getDictionaries();
 assertEquals(5, dictionaries.size());
 }

 @Test
 public void testArrangeFiles() {
 File[] files = new File[] { new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"),
 new File("MsgType.xml"), new File("MsgContents.xml") };
 List<File> arrangedFiles = new Library().arrangeFiles(files);
 List<Integer> expectedKeys = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> keys = new ArrayList<>();
 for (File file : arrangedFiles) {
 keys.add(new File(file.getName()).getName().hashCode());
 }
 Collections.sort(keys, new Comparator<Integer>() {
 @Override
 public int compare(Integer o1, Integer o2) {
 return o1.compareTo(o2);
 }
 });
 List<Integer> sortedKeys = keys;
 for (int i = 0; i < expectedKeys.size(); i++) {
 assertEquals(expectedKeys.get(i).intValue(), sortedKeys.get(i).intValue());
 }
 }

 @Test
 public void testLoadFromDirectoryWithFilesEmptyFiles() throws IOException {
 File testDirectory = new File("testData/emptyFiles");
 Library testLibrary = new Library();
 testLibrary.loadFromDirectory(testDirectory.getAbsolutePath());
 assertTrue(testLibrary.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadFromDirectoryWithFilesInvalidFile() throws IOException {
 File testDirectory = new File("testData/invalidFile");
 Library testLibrary = new Library();
 testLibrary.loadFromDirectory(testDirectory.getAbsolutePath());
 assertFalse(library.getDictionaries().size() > 0);
 }

 @Test
 public void testLoadFromDirectoryWithFilesMissingFile() throws IOException {
 File testDirectory = new File("testData/missingFile");
 Library testLibrary = new Library();
 testLibrary.loadFromDirectory(testDirectory.getAbsolutePath());
 assertEquals(4, testLibrary.getDictionaries().size());
 }
}