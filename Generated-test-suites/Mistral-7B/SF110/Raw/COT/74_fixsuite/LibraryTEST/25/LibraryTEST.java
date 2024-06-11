package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static Library library;
 private static final String LIBRARY_PATH = "test/resources/library"; // Replace this with the actual path to the library directory
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @BeforeClass
 public static void setup() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Test with a valid directory
 boolean result = library.loadFromDirectory(LIBRARY_PATH);
 assertTrue(result);
 // Test with an empty directory
 result = library.loadFromDirectory(new File(""));
 assertFalse(result);
 // Test with a non-existent directory
 result = library.loadFromDirectory(new File("non_existent_directory"));
 assertFalse(result);
 }

 @Test
 public void testGetDictionaries() {
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertNotNull(dictionaries);
 assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWithNullVersion() {
 library.getDictionary(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryWithEmptyVersion() {
 library.getDictionary("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryWithUnregisteredVersion() {
 library.getDictionary("UnregisteredVersion");
 }

 @Test
 public void testGetDictionaryWithRegisteredVersion() {
 String version = "1.0";
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNotNull(dictionary);
 assertEquals(version, dictionary.getVersion());
 }

 @Test
 public void testLoadDirectoryWithEmptyFileList() {
 List<File> emptyFiles = Collections.emptyList();
 library.loadDirectory(emptyFiles, "version");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectoryWithFilesMismatchingFileNames() {
 File[] files = new File[] { new File("Fields.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml") };
 library.loadDirectory(Arrays.asList(files), "version");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test(expected = IOException.class)
 public void testLoadDirectoryWithIOException() throws IOException {
 File invalidFile = new File("test/resources/library/invalidFile.txt");
 List<File> files = Arrays.asList(new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), invalidFile, new File("MsgContents.xml"));
 library.loadDirectory(files, "version");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectoryWithValidFiles() {
 File[] files = new File[] { new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml") };
 library.loadDirectory(Arrays.asList(files), "version");
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testArrangeFilesWithNullFiles() {
 File[] files = null;
 List<File> arrangedFiles = library.arrangeFiles(files);
 assertTrue(arrangedFiles.isEmpty());
 }

 @Test
 public void testArrangeFilesWithEmptyFileList() {
 File[] files = {};
 List<File> arrangedFiles = library.arrangeFiles(Arrays.asList(files));
 assertTrue(arrangedFiles.isEmpty());
 }

 @Test
 public void testArrangeFilesWithFilesInCorrectOrder() {
 File[] files = new File[] { new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml") };
 List<File> arrangedFiles = library.arrangeFiles(Arrays.asList(files));
 assertEquals(5, arrangedFiles.size());
 List<File> expectedFiles = Arrays.asList(files);
 List<File> actualFiles = new ArrayList<File>(arrangedFiles);
 Collections.sort(expectedFiles);
 Collections.sort(actualFiles);
 for (int i = 0; i < 5; ++i) {
 assertEquals(expectedFiles.get(i), actualFiles.get(i));
 }
 }

 @Test
 public void testArrangeFilesWithFilesInIncorrectOrder() {
 File[] files = new File[] { new File("Components.xml"), new File("Fields.xml"), new File("MsgContents.xml"), new File("MsgType.xml"), new File("Enums.xml") };
 List<File> arrangedFiles = library.arrangeFiles(Arrays.asList(files));
 assertEquals(5, arrangedFiles.size());
 List<File> expectedFiles = Arrays.asList(files);
 List<File> actualFiles = new ArrayList<File>(arrangedFiles);
 Collections.sort(expectedFiles);
 Collections.sort(actualFiles);
 for (int i = 0; i < 5; ++i) {
 fail("Unexpected file in position " + i + ".");
 }
 }
}