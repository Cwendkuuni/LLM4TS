package org.fixsuite.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

 private static final String LIBRARY_PATH = "testData"; // specify your test data directory here
 private Library library;
 private List<File> arrangedFiles;
 private DictionaryInfo dictionary;

 @Before
 public void setup() {
 library = new Library();
 arrangedFiles = Collections.emptyList(); // initialize with an empty list for now
 }

 @Test
 public void testLibraryConstructor() {
 Library library = new Library();
 assertNotNull(library);
 }

 @Test
 public void testLoadFromDirectory_Success() throws IOException {
 File libraryPath = new File(LIBRARY_PATH);
 library.loadFromDirectory(libraryPath.getAbsolutePath());
 assertTrue(library.getDictionaries().size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectory_InvalidPath() {
 library.loadFromDirectory(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectory_EmptyDirectory() {
 File libraryPath = new File("");
 library.loadFromDirectory(libraryPath.getAbsolutePath());
 assertFalse(library.getDictionaries().size() > 0);
 }

 @Test
 public void testLoadFromDirectory_MixedFiles() throws IOException {
 File libraryPath = new File(LIBRARY_PATH);
 File[] files = libraryPath.listFiles();
 arrangedFiles = library.arrangeFiles(files);
 library.loadFromDirectory(arrangedFiles);
 assertTrue(library.getDictionaries().size() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectory_InvalidFileNames() throws IOException {
 File libraryPath = new File(LIBRARY_PATH);
 File[] files = { new File("invalidFileName1.xml"), new File("invalidFileName2.xml") };
 arrangedFiles = library.arrangeFiles(files);
 library.loadFromDirectory(arrangedFiles);
 assertFalse(library.getDictionaries().size() > 0);
 }

 @Test
 public void testGetDictionaries() {
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertNotNull(dictionaries);
 }

 @Test
 public void testGetDictionary() {
 dictionary = new DictionaryInfo("testVersion");
 library.getDictionaries().add(dictionary);
 DictionaryInfo loadedDictionary = library.getDictionary("testVersion");
 assertEquals(dictionary, loadedDictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionary_NullVersion() {
 library.getDictionary(null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionary_EmptyDictionary() {
 library = new Library();
 DictionaryInfo loadedDictionary = library.getDictionary("testVersion");
 assertNull(loadedDictionary);
 }

 @Test
 public void testLoadDirectory_EmptyFileList() {
 library.loadDirectory(Collections.emptyList(), "testVersion");
 assertFalse(library.getDictionaries().size() > 0);
 }

 @Test
 public void testLoadDirectory_InvalidFile() {
 File invalidFile = new File("testData/invalidFile.txt");
 library.loadDirectory(Collections.singletonList(invalidFile), "testVersion");
 assertFalse(library.getDictionaries().size() > 0);
 }

 @Test
 public void testLoadDirectory_Success() throws IOException {
 File file = new File("testData/Fields.xml");
 DictionaryInfo dictionary = new DictionaryInfo("testVersion");
 MainParser.parse(dictionary, file);
 library.loadDirectory(Collections.singletonList(file), "testVersion");
 assertEquals(1, library.getDictionaries().size());
 assertEquals(dictionary, library.getDictionary("testVersion"));
 }

}