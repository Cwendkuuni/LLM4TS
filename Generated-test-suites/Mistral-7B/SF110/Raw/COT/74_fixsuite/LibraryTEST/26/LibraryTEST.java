import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.io.File;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.Library;

public class LibraryTest {

 private Library library;
 private List<File> files;

 @Before
 public void setup() {
 library = new Library();
 files = new ArrayList<File>();
 }

 @Test
 public void testLoadFromDirectory() {
 // Test with valid directory
 File validDirectory = new File("path/to/valid/directory");
 library.loadFromDirectory(validDirectory.getAbsolutePath());
 assertTrue(library.getDictionaries().size() > 0);

 // Test with empty directory
 File emptyDirectory = new File("path/to/empty/directory");
 library.loadFromDirectory(emptyDirectory.getAbsolutePath());
 assertTrue(library.getDictionaries().isEmpty());

 // Test with null directory
 library.loadFromDirectory(null);
 assertFalse(library.getDictionaries().isEmpty());
assertFalse(library.getDictionaries().size() > 0);
 }

 @Test
 public void testGetDictionaries() {
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertNotNull(dictionaries);
 assertTrue(dictionaries.size() > 0);
 }

 @Test
 public void testGetDictionary() {
 // Test with existing version
 String existingVersion = "1.0";
 DictionaryInfo dictionary = library.getDictionary(existingVersion);
 assertNotNull(dictionary);

 // Test with non-existing version
 String nonExistingVersion = "2.0";
 dictionary = library.getDictionary(nonExistingVersion);
 assertNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWithNullVersion() {
 library.getDictionary(null);
 }

 @Test
 public void testLoadDirectoryWithNullFilesList() {
 library.loadDirectory(null, "version");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWithEmptyFilesList() {
 library.loadDirectory(new ArrayList<>(), "version");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadDirectoryWithInvalidFileOrder() {
 File[] invalidFiles = new File[] {new File("Components.xml"), new File("MsgType.xml")};
 library.loadDirectory(Arrays.asList(invalidFiles), "version");
 }

 @Test
 public void testLoadDirectoryWithValidFiles() {
 File[] validFiles = new File[] {new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml")};
 library.loadDirectory(Arrays.asList(validFiles), "version");
 assertTrue(library.getDictionaries().size() > 0);
 }
}