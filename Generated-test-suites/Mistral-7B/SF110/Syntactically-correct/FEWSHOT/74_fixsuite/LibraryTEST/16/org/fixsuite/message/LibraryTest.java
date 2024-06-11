package org.fixsuite.message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.parsers.fpl.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.*;

public class LibraryTest {

 private Library library;
 private Logger logger;
 private File path;
 private File[] files;
 private List<File> arrangedFiles;
 private List<File> filesToLoad;

 @Before
 public void setUp() {
 library = new Library();
 logger = Mockito.spy(LoggerFactory.getLogger(Library.class));
 path = Mockito.mock(File.class);
 files = new File[] {new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml")};
 arrangedFiles = Arrays.asList(files);
 filesToLoad = Arranges.asList(files[0], files[1], files[2], files[3], files[4]);
 }

 @Test
 public void testLoadFromDirectory_EmptyDirectory_ReturnsFalse() {
 when(path.listFiles()).thenReturn(new File[0]);
 assertFalse(library.loadFromDirectory(path.getAbsolutePath()));
 }

 @Test
 public void testLoadFromDirectory_DirectoryContainsInvalidFileType_ReturnsFalse() {
 when(path.listFiles()).thenReturn(new File[] {new File("invalidFileType.txt")});
 assertFalse(library.loadFromDirectory(path.getAbsolutePath()));
 }

 @Test
 public void testLoadFromDirectory_DirectoryContainsValidFiles_ReturnsTrue() {
 when(path.listFiles()).thenReturn(files);
 when(files[0].isDirectory()).thenReturn(false);
 when(files[1].isDirectory()).thenReturn(false);
 when(files[2].isDirectory()).thenReturn(false);
 when(files[3].isDirectory()).thenReturn(false);
 when(files[4].isDirectory()).thenReturn(false);
 when(files[0].getName()).thenReturn("Fields.xml");
 when(files[1].getName()).thenReturn("Enums.xml");
 when(files[2].getName()).thenReturn("Components.xml");
 when(files[3].getName()).thenReturn("MsgType.xml");
 when(files[4].getName()).thenReturn("MsgContents.xml");
 when(files[0].length()).thenReturn(100L);
 when(files[1].length()).thenReturn(200L);
 when(files[2].length()).thenReturn(300L);
 when(files[3].length()).thenReturn(400L);
 when(files[4].length()).thenReturn(500L);

 Mockito.doAnswer(invocation -> {
 DictionaryInfo dictionary = new DictionaryInfo("testVersion");
 for (File file : invocation.getArgument(1)) {
 MainParser.parse(dictionary, file);
 }
 return dictionary;
 }).when(library).loadDirectory(arrangedFiles, "testVersion");

 assertTrue(library.loadFromDirectory(path.getAbsolutePath()));
 verify(logger, times(1)).debug("Loading time: \\d+ milliseconds.");
 verify(logger, times(1)).info("Loaded \\d+ FIX dictionaries.");
 }

 @Test
 public void testGetDictionaries_ReturnsEmptyList_WhenNoDictionariesLoaded() {
 assertEquals(0, library.getDictionaries().size());
 }

 @Test
 public void testGetDictionaries_ReturnsListOfDictionaries_WhenDictionariesLoaded() {
 List<DictionaryInfo> dictionaries = Arrays.asList(new DictionaryInfo("version1"), new DictionaryInfo("version2"));
 library.loadFromDirectory(path.getAbsolutePath());
 library.dictionaries.putAll(dictionaries);
 assertEquals(2, library.getDictionaries().size());
 }

 @Test
 public void testGetDictionary_ReturnsNull_WhenNoDictionaryWithVersionLoaded() {
 assertNull(library.getDictionary("version1"));
 }

 @Test
 public void testGetDictionary_ReturnsDictionary_WhenDictionaryWithVersionLoaded() {
 DictionaryInfo dictionary = new DictionaryInfo("version1");
 library.loadFromDirectory(path.getAbsolutePath());
 library.dictionaries.put(dictionary.getVersion(), dictionary);
 assertEquals(dictionary, library.getDictionary("version1"));
 }
}