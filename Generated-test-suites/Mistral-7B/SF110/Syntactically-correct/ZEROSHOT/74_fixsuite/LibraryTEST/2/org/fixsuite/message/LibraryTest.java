package org.fixsuite.message;

import static org.junit.Assert.*;
import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.*;

import java.io.*;
import java.text.*;
import java.util.*;

public class LibraryTest {

 private Library library;
 private static final String LIBRARY_PATH = "path/to/library";

 @Before
 public void setUp() {
 library = new Library();
 }

 @Test
 public void testLoadFromDirectory() {
 // Test loading from a valid directory
 assertTrue(library.loadFromDirectory(LIBRARY_PATH));

 // Test loading from an empty directory
 File emptyDirectory = new File("path/to/empty_directory");
 assertFalse(library.loadFromDirectory(emptyDirectory.getAbsolutePath()));

 // Test loading from a non-existent directory
 File nonExistentDirectory = new File("path/to/non_existent_directory");
 assertFalse(library.loadFromDirectory(nonExistentDirectory.getAbsolutePath()));
 }

 @Test
 public void testGetDictionaries() {
 // Test getting dictionaries after loading from a valid directory
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWithNullVersion() {
 library.getDictionary(null);
 }

 @Test
 public void testGetDictionary() {
 // Test getting dictionary for a valid version
 DictionaryInfo dictionary = library.getDictionary("1.0");
 assertNotNull(dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryWithNullLibrary() {
 Library nullLibrary = null;
 DictionaryInfo dictionary = nullLibrary.getDictionary("1.0");
 }

 @Test
 public void testGetDictionaryWithInvalidVersion() {
 // Test getting dictionary for an invalid version
 Library library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 DictionaryInfo dictionary = library.getDictionary("2.0");
 assertNull(dictionary);
 }
}