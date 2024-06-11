package org.fixsuite.message;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertLineEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.xml.sax.SAXException;

@RunWith(MockitoJUnitRunner.class)
public class LibraryTest {

 Library library = new Library();
 List<File> files = Arrays.asList(new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml"));
 List<File> arrangedFiles = Arrays.asList(new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml"));

 @Test
 public void testLibraryInitialization() {
 Library library = new Library();
 assertNull(library.getDictionaries());
 }

 @Test
 public void testLoadDirectoryEmpty() {
 assertFalse(library.loadFromDirectory(new File("")));
 }

 @Test
 public void testLoadDirectoryNull() {
 assertFalse(library.loadFromDirectory(null));
 }

 @Test
 public void testLoadDirectoryInvalidPath() {
 File invalidPath = new File("invalidPath");
 assertFalse(library.loadFromDirectory(invalidPath));
 }

 @Test
 public void testLoadDirectoryEmptyDirectory() throws IOException {
 File emptyDirectory = new File("emptyDirectory");
 assertFalse(library.loadFromDirectory(emptyDirectory));
 }

 @Test
 public void testLoadDirectoryInvalidFileExtension() throws IOException, SAXException {
 File invalidFile = new File("Fields.txt");
 File[] files = new File[] { invalidFile };
 File directory = new File("directory");
 when(library.arrangeFiles(files)).thenReturn(Arrays.asList(files[0]));
 when(library.loadDirectory(Arrays.asList(files[0]), "version")).thenThrow(new RuntimeException());
 assertFalse(library.loadFromDirectory(directory));
 }

 @Test
 public void testLoadDirectoryEmptyFiles() throws IOException, SAXException {
 File directory = new File("directory");
 when(library.arrangeFiles(files.toArray(new File[0]))).thenReturn(arrangedFiles);
 for (File file : arrangedFiles) {
 when(MainParser.parse(any(DictionaryInfo.class), any(File.class))).thenThrow(new RuntimeException());
 }
 assertFalse(library.loadFromDirectory(directory));
 }

 @Test
 public void testLoadDirectorySuccess() throws IOException, SAXException {
 File directory = new File("directory");
 when(library.arrangeFiles(files.toArray(new File[0]))).thenReturn(arrangedFiles);
 for (int i = 0; i < arrangedFiles.size(); ++i) {
 when(MainParser.parse(any(DictionaryInfo.class), arrangedFiles.get(i))).thenReturn(new DictionaryInfo("version"));
 }
 assertTrue(library.loadFromDirectory(directory));
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(1, dictionaries.size());
 assertNotNull(dictionaries.get(0));
 }

 @Test
 public void testGetDictionarySuccess() throws IOException, SAXException {
 File directory = new File("directory");
 when(library.arrangeFiles(files.toArray(new File[0]))).thenReturn(arrangedFiles);
 for (int i = 0; i < arrangedFiles.size(); ++i) {
 when(MainParser.parse(any(DictionaryInfo.class), arrangedFiles.get(i))).thenReturn(new DictionaryInfo("version"));
 }
 DictionaryInfo dictionary = library.getDictionary("version");
 assertNotNull(dictionary);
 assertEquals("version", dictionary.getVersion());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNull() {
 Library library = new Library();
 assertNull(library.getDictionary(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDictionaryInvalidVersion() throws IOException, SAXException {
 File directory = new File("directory");
 when(library.arrangeFiles(files.toArray(new File[0]))).thenReturn(arrangedFiles);
 for (int i = 0; i < arrangedFiles.size(); ++i) {
 when(MainParser.parse(any(DictionaryInfo.class), arrangedFiles.get(i))).thenReturn(new DictionaryInfo("version"));
 }
 Library library = new Library();
 library.loadFromDirectory(directory);
 assertNull(library.getDictionary("wrongVersion"));
 }

 @Test
 public void testGetDictionariesEmpty() {
 assertNotNull(library.getDictionaries());
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testGetDictionariesSuccess() throws IOException, SAXException {
 File directory = new File("directory");
 when(library.arrangeFiles(files.toArray(new File[0]))).thenReturn(arrangedFiles);
 for (int i = 0; i < arrangedFiles.size(); ++i) {
 when(MainParser.parse(any(DictionaryInfo.class), arrangedFiles.get(i))).thenReturn(new DictionaryInfo("version"));
 }
 Library library = new Library();
 library.loadFromDirectory(directory);
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(1, dictionaries.size());
 assertNotNull(dictionaries.get(0));
 }

 @Test
 public void testDictionaryVersionCompare() {
 DictionaryInfo dictionary1 = new DictionaryInfo("version1");
 DictionaryInfo dictionary2 = new DictionaryInfo("version2");
 int comparisonResult = dictionary1.compareTo(dictionary2);
 assertFalse(comparisonResult > 0);
 assertFalse(comparisonResult < 0);
 assertTrue(comparisonResult == 0);
 }

 @Test
 public void testDictionaryVersionCompareDifferent() {
 DictionaryInfo dictionary1 = new DictionaryInfo("version1");
 DictionaryInfo dictionary2 = new DictionaryInfo("version2");
 int comparisonResult = dictionary1.compareTo(dictionary2);
 assertFalse(comparisonResult > 0);
 assertTrue(comparisonResult < 0);
 }

 @Test
 public void testDictionaryInfoEquality() {
 DictionaryInfo dictionary1 = new DictionaryInfo("version1");
 DictionaryInfo dictionary2 = new DictionaryInfo("version1");
 assertTrue(dictionary1.equals(dictionary2));
 }

 @Test
 public void testDictionaryInfoInequality() {
 DictionaryInfo dictionary1 = new DictionaryInfo("version1");
 DictionaryInfo dictionary2 = new DictionaryInfo("version2");
 assertFalse(dictionary1.equals(dictionary2));
 }

 @Test
 public void testDictionaryInfoHashCode() {
 DictionaryInfo dictionary1 = new DictionaryInfo("version1");
 DictionaryInfo dictionary2 = new DictionaryInfo("version1");
 assertEquals(dictionary1.hashCode(), dictionary2.hashCode());
 }

 @Test
 public void testDictionaryInfoHashCodeDifferent() {
 DictionaryInfo dictionary1 = new DictionaryInfo("version1");
 DictionaryInfo dictionary2 = new DictionaryInfo("version2");
 assertNotSame(dictionary1.hashCode(), dictionary2.hashCode());
 }

 @Test
 public void testLibraryLogger() {
 Library library = new Library();
 Library.logger = mock(Logger.class);
 library.loadFromDirectory(new File("directory"));
 verify(Library.logger).debug("Unloading previous dictionaries");
 verify(Library.logger).debug("Supplied path is not a directory.");
 }

 @Test
 public void testLibraryLoggerLoadDirectorySuccess() throws IOException, SAXException {
 Library library = new Library();
 Library.logger = mock(Logger.class);
 when(library.loadFromDirectory(new File("directory"))).thenReturn(true);
 library.loadFromDirectory(new File("directory"));
 verify(Library.logger).debug("Supplied path is a directory.");
 verify(Library.logger).debug("Loaded 0 FIX dictionaries.");
 }

 @Test
 public void testLibraryLoggerLoadDirectoryFailed() {
 Library library = new Library();
 Library.logger = mock(Logger.class);
 when(library.loadFromDirectory(new File("directory"))).thenReturn(false);
 library.loadFromDirectory(new File("directory"));
 verify(Library.logger).debug("Supplied path is a directory.");
 verify(Library.logger).error("Unable to load any dictionaries");
 }

 @Test
 public void testLibraryLoggerLoadingTime() throws IOException, SAXException {
 Library library = new Library();
 Library.logger = mock(Logger.class);
 when(library.loadFromDirectory(new File("directory"))).thenReturn(true);
 long startTime = System.currentTimeMillis();
 library.loadFromDirectory(new File("directory"));
 long loadingTime = System.currentTimeMillis() - startTime;
 when(Library.logger).thenAnswer(invocation -> {
 String message = invocation.getArgument(0).toString();
 if (message.contains("Loading time")) {
 long milliseconds = Long.parseLong(message.split(": ")[1]);
 assertEquals(loadingTime, milliseconds);
 }
 });
 library.loadFromDirectory(new File("directory"));
 }

 @Test
 public void testLibraryLoggerApproximateMemoryAllocation() throws IOException, SAXException {
 Library library = new Library();
 Library.logger = mock(Logger.class);
 when(library.loadFromDirectory(new File("directory"))).thenReturn(true);
 long startMemory = Runtime.getRuntime().freeMemory();
 library.loadFromDirectory(new File("directory"));
 long approximateMemoryAllocation = Runtime.getRuntime().freeMemory() - startMemory;
 when(Library.logger).thenAnswer(invocation -> {
 String message = invocation.getArgument(0).toString();
 if (message.contains("Approximate memory allocation")) {
 float megabytes = Float.parseFloat(message.split(": ")[1].replaceAll(" MB", ""));
 assertEquals(approximateMemoryAllocation / 1000000.0, megabytes, 0.01f);
 }
 });
 library.loadFromDirectory(new File("directory"));
 }
}