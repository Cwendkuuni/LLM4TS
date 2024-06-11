import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private Library library;
 private Logger log;

 @Before
 public void setUp() {
 library = new Library();
 log = LoggerFactory.getLogger(Library.class);
 }

 @Test
 public void testLoadFromDirectory() {
 // Given
 String libraryPath = "path/to/library";
 File directory = new File(libraryPath);
 List<File> files = new ArrayList<File>(Arrays.asList(directory.listFiles()));

 // When
 library.loadFromDirectory(libraryPath);

 // Then
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 Assert.assertFalse(dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionary() {
 // Given
 String version = "4.4";
 library.loadFromDirectory("path/to/library");

 // When
 DictionaryInfo dictionary = library.getDictionary(version);

 // Then
 Assert.assertNotNull(dictionary);
 Assert.assertEquals(version, dictionary.getVersion());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryNullVersion() {
 // Given
 library.loadFromDirectory("path/to/library");

 // When, Then
 library.getDictionary(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryEmptyVersion() {
 // Given
 library.loadFromDirectory("path/to/library");

 // When, Then
 library.getDictionary("");
 }

 @Test
 public void testGetDictionaryMissingVersion() {
 // Given
 library.loadFromDirectory("path/to/library");

 // When
 DictionaryInfo dictionary = library.getDictionary("1.11");

 // Then
 Assert.assertNull(dictionary);
 }

 @Test
 public void testGetDictionaryInvalidFormatVersion() {
 // Given
 library.loadFromDirectory("path/to/library");

 // When
 String invalidVersion = "1.1.1";
 DictionaryInfo dictionary = library.getDictionary(invalidVersion);

 // Then
 Assert.assertNull(dictionary);
 }
}