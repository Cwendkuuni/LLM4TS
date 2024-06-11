package org.fixsuite.message.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibraryTest {

 private Library library;
 private Logger logger;

 @Before
 public void setup() {
 library = new Library();
 logger = Mockito.spy(LoggerFactory.getLogger(Library.class));
 }

 @Test
 public void testEmptyDirectory() {
 library.loadFromDirectory("src/test/resources/empty");
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testInvalidPath() {
 library.loadFromDirectory("src/test/resources/invalid");
 assertFalse(library.getDictionsaries().isEmpty());
 assertNull(library.getDictionary("1.0"));
 }

 @Test
 public void testValidDirectory() throws IOException {
 File directory = new File("src/test/resources/valid");
 library.loadFromDirectory(directory.getAbsolutePath());
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(5, dictionaries.size());
 DictionaryInfo dictionary1 = dictionaries.get(0);
 assertEquals("1.0", dictionary1.getVersion());
 DictionaryInfo dictionary2 = dictionaries.get(1);
 assertEquals("1.1", dictionary2.getVersion());
 assertNotNull(library.getDictionary("1.0"));
 assertNotNull(library.getDictionary("1.1"));
 assertNull(library.getDictionary("2.0"));
 }

 @Test
 public void testLoadDirectoryTwice() throws IOException {
 File directory = new File("src/test/resources/valid");
 library.loadFromDirectory(directory.getAbsolutePath());
 library.loadFromDirectory(directory.getAbsolutePath());
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(5, dictionaries.size());
 }

 @Test
 public void testGarbageCollection() throws IOException {
 File directory = new File("src/test/resources/valid");
 library.loadFromDirectory(directory.getAbsolutePath());
 library.getDictionaries();
 System.gc();
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(5, dictionaries.size());
 }

 @Test
 public void testParseDictionary() throws IOException {
 File file = new File("src/test/resources/Fields.xml");
 DictionaryInfo dictionary = new DictionaryInfo("1.0");
 library.loadDirectory(Arrays.asList(file), "test");
 assertEquals("Fields", dictionary.getName());
 }
}