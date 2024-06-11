package org.fixsuite.message;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private Library library;
 private Logger logger;

 @Before
 public void setUp() {
 library = new Library();
 logger = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectory_ValidDirectory_ReturnsTrue() {
 // Given: A valid directory
 String libraryPath = "src/test/resources/validDirectory";
 File file = new File(libraryPath);
 File[] files = file.listFiles();

 // When: Library is loaded from valid directory
 boolean result = library.loadFromDirectory(libraryPath);

 // Then: Result should be true
 assertTrue(result);
 }

 @Test
 public void testLoadFromDirectory_EmptyDirectory_ReturnsFalse() {
 // Given: An empty directory
 String libraryPath = "src/test/resources/emptyDirectory";
 File file = new File(libraryPath);

 // When: Library is loaded from empty directory
 boolean result = library.loadFromDirectory(libraryPath);

 // Then: Result should be false
 assertFalse(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromDirectory_NullDirectory_ThrowsException() {
 // Given: A null directory
 String libraryPath = null;

 // When: Library is loaded from null directory
 library.loadFromDirectory(libraryPath);

 // Then: Exception should be thrown
 }

 @Test(expected = IOException.class)
 public void testLoadFromDirectory_IOException_ThrowsException() {
 // Given: A directory with an IOException
 String libraryPath = "src/test/resources/ioExceptionDirectory";
 File file = new File(libraryPath);
 doReturn(new FileInputStream(file)).when(library).openFile(anyString());
 doThrow(new IOException()).when(library).parse(any(DictionaryInfo.class), any(File.class));

 // When: Library is loaded from directory
 boolean result = library.loadFromDirectory(libraryPath);

 // Then: Exception should be thrown
 }

 @Test
 public void testGetDictionaries_LoadedLibrary_ReturnsDictionaries() {
 // Given: A loaded library
 library.loadFromDirectory("src/test/resources/validDirectory");

 // When: Dictionaries are retrieved
 List<DictionaryInfo> dictionaries = library.getDictionaries();

 // Then: Dictionaries should not be empty
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary_ValidVersion_ReturnsDictionary() {
 // Given: A loaded library with a dictionary for the given version
 library.loadFromDirectory("src/test/resources/validDirectory");
 DictionaryInfo dictionary = new DictionaryInfo("1.0");
 library.dictionaries.put(dictionary.getVersion(), dictionary);

 // When: Dictionary for the given version is retrieved
 DictionaryInfo result = library.getDictionary("1.0");

 // Then: Result should match the expected dictionary
 assertEquals(dictionary, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionary_NullVersion_ThrowsException() {
 // Given: A loaded library
 library.loadFromDirectory("src/test/resources/validDirectory");

 // When: Dictionary for a null version is retrieved
 library.getDictionary(null);

 // Then: Exception should be thrown
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionary_InvalidVersion_ThrowsException() {
 // Given: A loaded library
 library.loadFromDirectory("src/test/resources/validDirectory");

 // When: Dictionary for an invalid version is retrieved
 library.getDictionary("InvalidVersion");

 // Then: Exception should be thrown
 }

 @Test
 public void testLoadDirectory_ValidFiles_PopulatesDictionary() {
 // Given: A list of files to be loaded
 File[] files = { new File("src/test/resources/Fields.xml"),
 new File("src/test/resources/Enums.xml"),
 new File("src/test/resources/Components.xml"),
 new File("src/test/resources/MsgType.xml"),
 new File("src/test/resources/MsgContents.xml") };
 String version = "1.0";
 DictionaryInfo dictionary = new DictionaryInfo(version);
 // When: Library is loaded with valid files
 library.loadDirectory(Arrays.asList(files), version);
 // Then: Dictionary should be populated with data from files
 assertEquals(5, library.dictionaries.size());
 DictionaryInfo actualDictionary = library.dictionaries.get(version);
 Map<String, Object> fields = actualDictionary.getFields();
 assertEquals(10, fields.size());
 List<EnumInfo> enums = actualDictionary.getEnums();
 assertEquals(3, enums.size());
 List<ComponentInfo> components = actualDictionary.getComponents();
 assertEquals(2, components.size());
 List<MessageDefinition> msgTypes = actualDictionary.getMessageDefinitions();
 assertEquals(2, msgTypes.size());
 List<Message> msgContents = actualDictionary.getMessageContents();
 assertEquals(1, msgContents.size());
 }

 @Test
 public void testArrangeFiles_ValidFiles_ReturnsFiles() {
 // Given: A list of valid files
 File[] files = { new File("src/test/resources/Fields.xml"),
 new File("src/test/resources/Enums.xml"),
 new File("src/test/resources/Components.xml"),
 new File("src/test/resources/MsgType.xml"),
 new File("src/test/resources/MsgContents.xml") };
 // When: Files are arranged
 List<File> arrangedFiles = library.arrangeFiles(files);
 // Then: Arranged files should match expected files
 List<File> expectedFiles = Arrays.asList(files);
 assertEquals(expectedFiles, arrangedFiles);
 }

 @Test
 public void testArrangeFiles_InvalidFileName_ThrowsException() {
 // Given: A list of files with an invalid filename
 File[] files = { new File("src/test/resources/Fields.xml"),
 new File("src/test/resources/InvalidFileName.xml"),
 new File("src/test/resources/Components.xml"),
 new File("src/test/resources/MsgType.xml"),
 new File("src/test/resources/MsgContents.xml") };
 // When: Files are arranged
 try {
 library.arrangeFiles(files);
 fail("Exception should be thrown");
 } catch (IllegalArgumentException e) {
 // Then: Exception message should match expected message
 assertEquals("Invalid file name: InvalidFileName.xml", e.getMessage());
 }
 }

 @Test
 public void testOpenFile_ValidFile_ReturnsFileInputStream() {
 // Given: A valid file
 String fileName = "src/test/resources/Fields.xml";
 File file = new File(fileName);
 // When: File input stream is opened
 InputStream inputStream = library.openFile(fileName);
 // Then: Input stream should not be null
 assertNotNull(inputStream);
 // And: Input stream should be of type FileInputStream
 assertTrue(inputStream instanceof FileInputStream);
 }

 @Test(expected = FileNotFoundException.class)
 public void testOpenFile_InvalidFile_ThrowsException() {
 // Given: An invalid file
 String fileName = "src/test/resources/InvalidFile.xml";
 // When: File input stream is opened
 library.openFile(fileName);
 // Then: Exception should be thrown
 }

}