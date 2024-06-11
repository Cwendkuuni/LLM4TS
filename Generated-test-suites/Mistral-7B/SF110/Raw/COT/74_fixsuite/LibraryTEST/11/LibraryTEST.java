package org.fixsuite.message;

 import static org.junit.Assert.*;
 import org.junit.Before;
 import org.junit.Test;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;

 import java.io.File;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;
 import java.util.TreeMap;

 import org.fixsuite.message.info.DictionaryInfo;

 public class LibraryTest {

 private Library library;
 private static final String LIBRARY_PATH = "path/to/library"; // Replace with the actual path to the library directory
 private static final Logger logger = LoggerFactory.getLogger(LibraryTest.class);

 @Before
 public void setUp() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Test case for loading from a valid directory
 File directory = new File(LIBRARY_PATH);
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));
 // Test case for loading from an empty directory
 File emptyDirectory = new File(".");
 assertFalse(library.loadFromDirectory(emptyDirectory.getAbsolutePath()));
 // Test case for loading from a non-existing directory
 File nonExistingDirectory = new File("/nonexistingdirectory");
 assertFalse(library.loadFromDirectory(nonExistingDirectory.getAbsolutePath()));
 }

 @Test
 public void testGetDictionaries() {
 // Test case for getting all loaded dictionaries
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 // Test case for getting dictionary with null version
 library.getDictionary(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryInvalidVersion() {
 // Test case for getting dictionary with invalid version
 library.getDictionary("InvalidVersion");
 }

 @Test
 public void testGetDictionaryValidVersion() {
 // Test case for getting dictionary with valid version
 String version = "001";
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNotNull(dictionary);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryNullFiles() {
 // Test case for loading directory with null files
 List<File> files = null;
 library.loadDirectory(files, "Version");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryEmptyFiles() {
 // Test case for loading directory with empty files
 List<File> files = new ArrayList<>();
 library.loadDirectory(files, "Version");
 }

 @Test
 public void testLoadDirectoryValidFiles() throws IOException {
 // Test case for loading dictionary from valid files
 File fieldsFile = new File("path/to/Fields.xml");
 File enumsFile = new File("path/to/Enums.xml");
 File componentsFile = new File("path/to/Components.xml");
 File msgTypeFile = new File("path/to/MsgType.xml");
 File msgContentsFile = new File("path/to/MsgContents.xml");
 List<File> files = new ArrayList<>();
 files.add(fieldsFile);
 files.add(enumsFile);
 files.add(componentsFile);
 files.add(msgTypeFile);
 files.add(msgContentsFile);
 library.loadDirectory(files, "Version");
 // Test if the dictionary was loaded correctly
 assertTrue(library.getDictionaries().size() > 0);
 }
 }