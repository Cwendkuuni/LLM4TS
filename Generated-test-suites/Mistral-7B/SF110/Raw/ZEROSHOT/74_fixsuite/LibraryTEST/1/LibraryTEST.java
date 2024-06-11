package org.fixsuite.message.test;

import static org.junit.Assert.*;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private static final String LIBRARY_PATH = "path/to/library";
 private Library library;
 private Logger log;

 @Before
 public void setUp() {
 library = new Library();
 log = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectory() {
 File libraryDirectory = new File(LIBRARY_PATH);
 assertTrue(libraryDirectory.isDirectory());
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 assertFalse(library.dictionaries.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromEmptyDirectory() {
 File emptyDirectory = new File("");
 assertFalse(library.loadFromDirectory(emptyDirectory.getPath()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromInvalidPath() {
 File invalidPath = new File("invalid/path");
 assertFalse(library.loadFromDirectory(invalidPath.getPath()));
 }

 @Test
 public void testGetDictionaries() {
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 String version = "1.0";
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullDictionary() {
 library.dictionaries = null;
 try {
 library.getDictionary("1.0");
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testLoadDirectory() {
 List<File> files = new ArrayList<File>();
 files.add(new File("Fields.xml"));
 files.add(new File("Enums.xml"));
 files.add(new File("Components.xml"));
 files.add(new File("MsgType.xml"));
 files.add(new File("MsgContents.xml"));
 library.loadDirectory(files, "1.0");
 assertFalse(library.dictionaries.isEmpty());
 }

 @Test
 public void testArrangeFiles() {
 File[] files = new File[5];
 for (int i = 0; i < files.length; i++) {
 files[i] = new File("file" + i + ".xml");
 }
 List<File> arrangedFiles = library.arrangeFiles(files);
 List<Integer> keys = new ArrayList<Integer>() {{
 add(Library.FIELDS_FILE_KEY);
 add(Library.ENUMS_FILE_KEY);
 add(Library.COMPONENTS_FILE_KEY);
 add(Library.MSG_TYPE_FILE_KEY);
 add(Library.MSG_CONTENTS_FILE_KEY);
 }};
 for (int i = 0; i < arrangedFiles.size(); i++) {
 assertEquals(keys.get(i), arrangedFiles.get(i).getName().hashCode());
 }
 }
}