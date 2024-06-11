package org.fixsuite.message;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LibraryTest {

 private Library library;
 private Logger loggerMock;
 private File testDirectory;

 @Before
 public void setUp() {
 library = new Library();
 loggerMock = Mockito.mock(Logger.class);
 Library.logger = loggerMock;
 testDirectory = new File("testPath");
 }

 @Test
 public void testLoadFromDirectory_whenDirectoryIsEmpty_thenReturnsFalse() {
 given(loggerMock.debug(anyString())).willAnswer(invocation -> {});
 given(loggerMock.error(anyString())).willAnswer(invocation -> {});
 given(loggerMock.info(anyString())).willAnswer(invocation -> {});

 library.loadFromDirectory(testDirectory.getPath());

 verify(loggerMock, times(1)).error("Supplied directory is empty.");
 verifyNoInteractions(library);
 assertFalse(library.dictionaries.isEmpty());
 }

 @Test
 public void testLoadFromDirectory_whenDirectoryIsNotADirectory_thenReturnsFalse() {
 given(loggerMock.debug(anyString())).willAnswer(invocation -> {});
 given(loggerMock.error(anyString())).willAnswer(invocation -> {});
 given(loggerMock.info(anyString())).willAnswer(invocation -> {});

 library.loadFromDirectory("notADirectoryPath");

 verify(loggerMock, times(1)).error("Supplied path is not a directory.");
 verifyNoInteractions(library);
 }

 @Test
 public void testLoadFromDirectory_whenValidDirectory_thenLoadsDictionaries() {
 given(loggerMock.debug(anyString())).willAnswer(invocation -> {});
 given(loggerMock.error(anyString())).willAnswer(invocation -> {});
 given(loggerMock.info(anyString())).willAnswer(invocation -> {});

 File[] testFiles = {new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml")};
 when(testDirectory.listFiles()).thenReturn(testFiles);

 library.loadFromDirectory(testDirectory.getPath());

 List<File> expectedFiles = Arrays.asList(testFiles);
 verify(library, times(1)).loadDirectory(eq(expectedFiles), eq("testPath"));
 verify(library, times(1)).getDictionaries();
 assertFalse(library.dictionaries.isEmpty());
 }

 @Test
 public void testGetDictionaries_whenDictionariesAreLoaded_thenReturnsList() {
 given(loggerMock.debug(anyString())).willAnswer(invocation -> {});
 given(loggerMock.error(anyString())).willAnswer(invocation -> {});
 given(loggerMock.info(anyString())).willAnswer(invocation -> {});

 library.loadFromDirectory(testDirectory.getPath());

 List<DictionaryInfo> expectedDictionaries = new ArrayList<>();
 expectedDictionaries.add(new DictionaryInfo("version1"));
 expectedDictionaries.add(new DictionaryInfo("version2"));
 when(library.getDictionaries()).thenReturn(expectedDictionaries);

 List<DictionaryInfo> actualDictionaries = library.getDictionaries();
 assertNotNull(actualDictionaries);
 assertEquals(expectedDictionaries, actualDictionaries);
 }

 @Test
 public void testGetDictionary_whenDictionaryIsLoaded_thenReturnsDictionary() {
 given(loggerMock.debug(anyString())).willAnswer(invocation -> {});
 given(loggerMock.error(anyString())).willAnswer(invocation -> {});
 given(loggerMock.info(anyString())).willAnswer(invocation -> {});
 given(library.getDictionaries()).willReturn(Arrays.asList(new DictionaryInfo("version1"), new DictionaryInfo("version2")));

 DictionaryInfo expectedDictionary = new DictionaryInfo("version1");
 when(library.getDictionary("version1")).thenReturn(expectedDictionary);

 DictionaryInfo actualDictionary = library.getDictionary("version1");
 assertNotNull(actualDictionary);
 assertEquals(expectedDictionary, actualDictionary);
 }

 @Test
 public void testGetDictionary_whenDictionaryIsNotLoaded_thenReturnsNull() {
 given(loggerMock.debug(anyString())).willAnswer(invocation -> {});
 given(loggerMock.error(anyString())).willAnswer(invocation -> {});
 given(loggerMock.info(anyString())).willAnswer(invocation -> {});
 given(library.getDictionaries()).willReturn(Arrays.asList(new DictionaryInfo("version2")));

 DictionaryInfo actualDictionary = library.getDictionary("version1");
 assertNull(actualDictionary);
 }
}