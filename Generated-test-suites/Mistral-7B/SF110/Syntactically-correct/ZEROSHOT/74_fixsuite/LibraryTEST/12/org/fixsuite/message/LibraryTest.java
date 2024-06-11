package org.fixsuite.message.test;

import org.fixsuite.message.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LibraryTest {

 private Library library;
 private final String libraryPath = "test_library_path"; // Replace with the path to your test library

 @Before
 public void setUp() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 File libraryDirectory = new File(libraryPath);
 assertTrue(libraryDirectory.isDirectory());

 List<File> files = Arrays.asList(libraryDirectory.listFiles());
 List<File> expectedFiles = Arrays.asList(new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml"));

 List<File> arrangedFiles = library.arrangeFiles(files.toArray(new File[0]));
 assertEquals(expectedFiles, arrangedFiles);

 assertTrue(library.loadFromDirectory(libraryPath));

 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 String version = "test_version";
 DictionaryInfo dictionary = new DictionaryInfo(version);
 library.getDictionary(version);
 assertNotNull(library.getDictionary(version));
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 assertNull(library.getDictionary(null));
 }
}