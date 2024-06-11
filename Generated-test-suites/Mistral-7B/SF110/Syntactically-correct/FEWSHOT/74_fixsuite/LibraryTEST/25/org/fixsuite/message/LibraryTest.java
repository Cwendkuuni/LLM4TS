package org.fixsuite.message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LibraryTest {

private Library library;
private List<File> files;
private File directory;

@Before
public void setUp() {
library = new Library();
files = Arrays.asList(mock(File.class), mock(File.class), mock(File.class), mock(File.class), mock(File.class));
directory = Mockito.mock(File.class);
when(directory.listFiles()).thenReturn(files.toArray(new File[files.size()]));
}

@Test
public void testLoadFromDirectoryEmptyDirectory() {
Library.logger = Mockito.mock(Logger.class);
Library library = new Library();
File emptyDirectory = mock(File.class);
when(emptyDirectory.isDirectory()).thenReturn(true);
when(emptyDirectory.listFiles()).thenReturn(new File[0]);
boolean result = library.loadFromDirectory(emptyDirectory.getAbsolutePath());
assertTrue(result);
assertEquals(0, library.getDictionaries().size());
}

@Test
public void testLoadFromDirectoryNoFiles() {
Library.logger = Mockito.mock(Logger.class);
Library library = new Library();
boolean result = library.loadFromDirectory("nonExistentDirectory");
assertFalse(result);
assertEquals(0, library.getDictionaries().size());
}

@Test
public void testLoadFromDirectoryWithFiles() {
Library.logger = Mockito.mock(Logger.class);
Library library = new Library();
Library.logger.debug(isA(String.class)); // suppress logging warning
when(directory.getName()).thenReturn("testDirectory");
when(files.get(0).getName()).thenReturn("Fields.xml");
when(files.get(1).getName()).thenReturn("Enums.xml");
when(files.get(2).getName()).thenReturn("Components.xml");
when(files.get(3).getName()).thenReturn("MsgType.xml");
when(files.get(4).getName()).thenReturn("MsgContents.xml");
when(files[0].isDirectory()).thenReturn(false);
when(files[1].isDirectory()).thenReturn(false);
when(files[2].isDirectory()).thenReturn(false);
when(files[3].isDirectory()).thenReturn(false);
when(files[4].isDirectory()).thenReturn(false);
List<File> arrangedFiles = library.arrangeFiles(files.toArray(new File[files.size()]));
Library.logger.debug(isA(String.class)); // suppress logging warning
when(arrangedFiles.get(0).getName()).thenReturn("Fields.xml");
when(arrangedFiles.get(1).getName()).thenReturn("Enums.xml");
when(arrangedFiles.get(2).getName()).thenReturn("Components.xml");
when(arrangedFiles.get(3).getName()).thenReturn("MsgType.xml");
when(arrangedFiles.get(4).getName()).thenReturn("MsgContents.xml");
library.loadDirectory(arrangedFiles, "testDirectory");
assertEquals(5, library.getDictionaries().size());
}

@Test
public void testGetDictionaries() {
Library library = new Library();
library.loadFromDirectory("testDirectory");
List<DictionaryInfo> dictionaries = library.getDictionaries();
assertTrue(dictionaries.size() > 0);
}

@Test
public void testGetDictionary() {
Library library = new Library();
library.loadFromDirectory("testDirectory");
DictionaryInfo dictionary = library.getDictionary("1.0");
assertNotNull(dictionary);
}
}