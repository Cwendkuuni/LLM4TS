import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import java.io.*;
import java.util.*;

public class LibraryTest {

 private Library library;
 private final String libraryPath = "testLibraryPath"; // replace with the actual test library path

 @Before
 public void setUp() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Basic functionality test
 assertTrue(library.loadFromDirectory(libraryPath));
 // Edge case: empty directory
 File emptyDirectory = new File(libraryPath + "/emptyDirectory");
 emptyDirectory.mkdir();
 assertFalse(library.loadFromDirectory(emptyDirectory.getAbsolutePath()));
 emptyDirectory.delete();
 // Edge case: non-existing directory
 assertFalse(library.loadFromDirectory("nonExistingDirectory"));
 // Exception handling: IOException
 File ioExceptionFile = new File(libraryPath + "/ioExceptionFile");
 try {
 ioExceptionFile.createNewFile();
 FileInputStream inputStream = new FileInputStream(ioExceptionFile);
 inputStream.close();
 IOException ioException = new IOException();
 inputStream.close(); // this should throw an IOException
 library.loadFromDirectory(libraryPath);
 fail("Expected IOException but no exception was thrown");
 } catch (IOException e) {
 // expected
 } finally {
 ioExceptionFile.delete();
 }
 }

 @Test
 public void testGetDictionaries() {
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertNotNull(dictionaries);
 assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 String version = "version1";
 DictionaryInfo dictionary = library.getDictionary(version);
 assertNotNull(dictionary);
 assertEquals(version, dictionary.getVersion());
 }

 @Test (expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 library.getDictionary(null);
 }
}