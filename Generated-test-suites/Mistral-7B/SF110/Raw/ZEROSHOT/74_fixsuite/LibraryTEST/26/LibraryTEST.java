package org.fixsuite.message.test;

import static org.junit.Assert.*;
import org.fixsuite.message.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library"; // Replace with the actual library path
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @Test
 public void testLoadFromDirectory() {
 Library library = new Library();
 boolean result = library.loadFromDirectory(LIBRARY_PATH);
 assertTrue(result);
 }

 @Test
 public void testGetDictionaries() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertNotNull(dictionaries);
 assertTrue(dictionaries.size() > 0);
 }

 @Test
 public void testGetDictionary() {
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary("version"); // Replace with the actual dictionary version
 assertNotNull(dictionary);
 }

 @Test
 public void testLoadDirectory() {
 Library library = new Library();
 List<File> files = new ArrayList<>();
 files.add(new File("Fields.xml"));
 files.add(new File("Enums.xml"));
 files.add(new File("Components.xml"));
 files.add(new File("MsgType.xml"));
 files.add(new File("MsgContents.xml"));
 library.loadDirectory(files, "library_version"); // Replace with the actual library version
 DictionaryInfo dictionary = library.getDictionary("version"); // Replace with the actual dictionary version
 assertNotNull(dictionary);
 }

 @Test
 public void testArrangeFiles() {
 File[] files = new File[] { new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml") };
 List<File> arrangedFiles = Library.arrangeFiles(files);
 assertEquals(5, arrangedFiles.size());
 for (int i = 0; i < arrangedFiles.size(); ++i) {
 String expectedFileName = ""; // Expected file name based on the file key (1 for Fields.xml, 2 for Enums.xml, etc.)
 switch (i) {
 case 0:
 expectedFileName = "Fields.xml";
 break;
 case 1:
 expectedFileName = "Enums.xml";
 break;
 case 2:
 expectedFileName = "Components.xml";
 break;
 case 3:
 expectedFileName = "MsgType.xml";
 break;
 case 4:
 expectedFileName = "MsgContents.xml";
 break;
 }
 assertEquals(expectedFileName, arrangedFiles.get(i).getName());
 }
 }
}