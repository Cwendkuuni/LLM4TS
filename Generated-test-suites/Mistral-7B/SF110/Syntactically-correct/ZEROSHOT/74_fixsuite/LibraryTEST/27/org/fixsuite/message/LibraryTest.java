package org.fixsuite.message.test;

import static org.junit.Assert.*;
import org.fixsuite.message.*;
import org.xml.sax.SAXException;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class LibraryTest {

 private static Library library;
 private static final String LIBRARY_PATH = "path/to/library"; // Replace with the actual path to the library directory

 @BeforeClass
 public static void setUpBeforeClass() throws IOException, SAXException {
 library = new Library();
 library.loadFromDirectory(LIBRARY_PATH);
 }

 @Test
 public void testLoadFromDirectory() {
 // Test if the library is loaded correctly
 assertEquals("Unexpected number of dictionaries loaded", 5, library.getDictionaries().size());
 }

 @Test
 public void testGetDictionary() {
 // Test getting a dictionary by version
 DictionaryInfo dictionary = library.getDictionary("FIX 5.0SP2");
 assertNotNull("Dictionary not found", dictionary);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDictionaryNullVersion() {
 // Test getting a dictionary with a null version
 library.getDictionary(null);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetDictionaryUnknownVersion() {
 // Test getting a dictionary with an unknown version
 library.getDictionary("UnknownVersion");
 }
}