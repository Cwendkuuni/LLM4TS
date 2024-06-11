import org.junit.Before;
import org.junit.Test;
import org.fixsuite.message.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class LibraryTest {

 private Library library;
 private Logger logger;
 private File directory;

 @Before
 public void setup() {
 library = new Library();
 logger = Mockito.spy(Library.logger);
 directory = new File("testDirectory");
 }

 @Test
 public void testLoadFromDirectory() {
 // Given
 Mockito.doReturn(true).when(logger).isDebugEnabled();
 Mockito.doReturn(true).when(logger).isInfoEnabled();
 Mockito.doReturn(directory).when(library).arrangeFiles(Mockito.any(File[].class));

 // When
 assertTrue(library.loadFromDirectory(directory.getAbsolutePath()));

 // Then
 assertTrue(library.getDictionaries().size() > 0);
 }

 @Test
 public void testLoadFromDirectoryWhenNoFilesFound() {
 // Given
 Mockito.doReturn(false).when(logger).isDebugEnabled();
 Mockito.doReturn(directory).when(library).arrangeFiles(Mockito.any(File[].class));

 // When
 assertFalse(library.loadFromDirectory(directory.getAbsolutePath()));

 // Then
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testGetDictionary() {
 // Given
 DictionaryInfo dictionary = new DictionaryInfo("1.0");
 library.getDictionaries().put("1.0", dictionary);

 // When
 DictionaryInfo result = library.getDictionary("1.0");

 // Then
 assertSame(dictionary, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWhenDictionaryNotLoaded() {
 // Given

 // When
 library.getDictionary("1.0");

 // Then
 fail("Should throw NullPointerException as dictionaries map is empty");
 }

 @Test
 public void testGetDictionaries() {
 // Given
 DictionaryInfo dictionary1 = new DictionaryInfo("1.0");
 DictionaryInfo dictionary2 = new DictionaryInfo("2.0");
 library.getDictionaries().put("1.0", dictionary1);
 library.getDictionaries().put("2.0", dictionary2);

 // When
 List<DictionaryInfo> result = library.getDictionaries();

 // Then
 assertEquals(2, result.size());
 assertSame(dictionary1, result.get(0));
 assertSame(dictionary2, result.get(1));
 }

 @Test
 public void testLoadDirectoryWithFiles() {
 // Given
 File fieldsXml = new File("testFiles/Fields.xml");
 File enumsXml = new File("testFiles/Enums.xml");
 File componentsXml = new File("testFiles/Components.xml");
 File msgTypeXml = new File("testFiles/MsgType.xml");
 File msgContentsXml = new File("testFiles/MsgContents.xml");
 List<File> files = new ArrayList<>();
 files.add(fieldsXml);
 files.add(enumsXml);
 files.add(componentsXml);
 files.add(msgTypeXml);
 files.add(msgContentsXml);
 Mockito.doReturn(files).when(library).arrangeFiles(Mockito.any(File[].class));

 // When
 library.loadDirectory(files, "testVersion");

 // Then
 assertTrue(library.getDictionaries().size() > 0);
 }

 @Test
 public void testLoadDirectoryWithEmptyDirectory() {
 // Given
 File emptyDirectory = new File("testDirectory");
 Mockito.doReturn(new File[0]) //
 .when(library).arrangeFiles(Mockito.any(File[].class));

 // When
 assertFalse(library.loadFromDirectory(emptyDirectory.getAbsolutePath()));

 // Then
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLoadDirectoryWithInvalidFileExtension() {
 // Given
 File invalidFile = new File("testFiles/InvalidFile.txt");
 List<File> files = new ArrayList<>();
 files.add(invalidFile);
 Mockito.doReturn(files).when(library).arrangeFiles(Mockito.any(File[].class));

 // When
 assertFalse(library.loadFromDirectory(directory.getAbsolutePath()));

 // Then
 assertTrue(library.getDictionaries().isEmpty());
 }
}