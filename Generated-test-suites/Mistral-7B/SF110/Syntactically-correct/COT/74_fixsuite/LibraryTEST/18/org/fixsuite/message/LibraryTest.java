package org.fixsuite.message;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.parsers.fpl.MainParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryTest {

 private static final String TEST_LIBRARY_PATH = "test_library_path";

 @Test
 public void testLibrary_loadFromDirectory_whenLibraryPathIsADirectory_thenLoadsDictionaries() {
  Library library = new Library();
  boolean result = library.loadFromDirectory(TEST_LIBRARY_PATH);
  assertTrue(result);

  List<DictionaryInfo> dictionaries = library.getDictionaries();
  assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLibrary_loadFromDirectory_whenLibraryPathIsEmpty_thenThrowsIllegalArgumentException() {
  Library library = new Library();
  boolean result = library.loadFromDirectory("");
  fail("Expected IllegalArgumentException but got " + result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLibrary_loadFromDirectory_whenLibraryPathIsNotADirectory_thenThrowsIllegalArgumentException() {
  Library library = new Library();
  boolean result = library.loadFromDirectory("test_library_file");
  fail("Expected IllegalArgumentException but got " + result);
 }

 @Test
 public void testLibrary_getDictionaries_whenDictionariesAreLoaded_thenReturnsThem() {
  Library library = new Library();
  library.loadFromDirectory(TEST_LIBRARY_PATH);
  List<DictionaryInfo> dictionaries = library.getDictionaries();
  assertFalse(dictionaries.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testLibrary_getDictionary_whenDictionaryIsNotLoaded_thenThrowsNullPointerException() {
  Library library = new Library();
  DictionaryInfo dictionary = library.getDictionary(null);
  fail("Expected NullPointerException but got " + dictionary);
 }

 @Test
 public void testLibrary_getDictionary_whenDictionaryIsLoaded_thenReturnsIt() {
  Library library = new Library();
  library.loadFromDirectory(TEST_LIBRARY_PATH);
  DictionaryInfo dictionary = library.getDictionary("test_version");
  assertNotNull(dictionary);
 }

 @Test
 public void testLibrary_loadDirectory_whenFilesAreArrangedCorrectly_thenParsesThem() {
  Library library = new Library();
  List<File> files = Arrays.asList(new File("Fields.xml"), new File("Enums.xml"), new File("Components.xml"), new File("MsgType.xml"), new File("MsgContents.xml"));
  String version = "test_version";
  library.loadDirectory(files, version);
  DictionaryInfo dictionary = library.getDictionary(version);
  assertTrue(dictionary.isLoaded());
 }

 @Test
 public void testLibrary_loadDirectory_whenFilesAreNotArrangedCorrectly_thenThrowsException() {
  Library library = new Library();
  List<File> files = Arrays.asList(new File("MsgContents.xml"), new File("Fields.xml"), new File("MsgType.xml"), new File("Enums.xml"), new File("Components.xml"));
  String version = "test_version";
  try {
   library.loadDirectory(files, version);
   fail("Expected exception but got no exception");
  } catch (Exception e) {
   // expected
  }
 }

 @Test
 public void testLibrary_parse_whenFileIsEmpty_thenParsesItSuccessfully() {
  File file = new File("empty_file.xml");
  Library library = new Library();
  DictionaryInfo dictionary = new DictionaryInfo("test_version");
  MainParser.parse(dictionary, file);
  assertTrue(dictionary.isLoaded());
 }

 @Test
 public void testLibrary_parse_whenFileContainsInvalidXml_thenParsesItSuccessfullyButDictionaryIsNotLoaded() {
  File file = new File("invalid_xml_file.xml");
  Library library = new Library();
  DictionaryInfo dictionary = new DictionaryInfo("test_version");
  try {
   MainParser.parse(dictionary, file);
   fail("Expected exception but got no exception");
  } catch (Exception e) {
   // expected
  }
  assertFalse(dictionary.isLoaded());
 }

 @Test
 public void testLibrary_parse_whenFileContainsEmptyTag_thenParsesItSuccessfullyButDictionaryIsNotLoaded() {
  String xml = "<Dictionary version=\"test_version\"></Dictionary>";
  File file = new File("empty_tag_file.xml");
  try (FileWriter writer = new FileWriter(file)) {
   writer.write(xml);
  } catch (IOException e) {
   e.printStackTrace();
   fail(e.getMessage());
  }
  Library library = new Library();
  DictionaryInfo dictionary = new DictionaryInfo("test_version");
  try {
   MainParser.parse(dictionary, file);
  } catch (Exception e) {
   fail(e.getMessage());
  } finally {
   file.delete();
  }
  assertFalse(dictionary.isLoaded());
 }

 @Test
 public void testLibrary_parse_whenFileContainsTagWithoutVersion_thenParsesItSuccessfullyButDictionaryIsNotLoaded() {
  String xml = "<Dictionary>\n" +
    "<MsgType name=\"test_msg_type\">\n" +
    "  <MsgName>Test Message</MsgName>\n" +
    "</MsgType>\n" +
    "</Dictionary>";
  File file = new File("tag_without_version_file.xml");
  try (FileWriter writer = new FileWriter(file)) {
   writer.write(xml);
  } catch (IOException e) {
   e.printStackTrace();
   fail(e.getMessage());
  }
  Library library = new Library();
  DictionaryInfo dictionary = new DictionaryInfo("test_version");
  try {
   MainParser.parse(dictionary, file);
  } catch (Exception e) {
   fail(e.getMessage());
  } finally {
   file.delete();
  }
  assertFalse(dictionary.isLoaded());
 }

 @Test
 public void testLibrary_parse_whenFileContainsMultipleRootTags_thenParsesEachTagIndependently() {
  String xml = "<Dictionary version=\"test_version_1\">\n" +
    "<MsgType name=\"test_msg_type_1\">\n" +
    "  <MsgName>Test Message 1</MsgName>\n" +
    "</MsgType>\n" +
    "</Dictionary>\n" +
    "<Dictionary version=\"test_version_2\">\n" +
    "<MsgType name=\"test_msg_type_2\">\n" +
    "  <MsgName>Test Message 2</MsgName>\n" +
    "</MsgType>\n" +
    "</Dictionary>";
  File file = new File("multiple_root_tags_file.xml");
  try (FileWriter writer = new FileWriter(file)) {
   writer.write(xml);
  } catch (IOException e) {
   e.printStackTrace();
   fail(e.getMessage());
  }
  Library library = new Library();
  Map<String, DictionaryInfo> dictionaries = library.getDictionaries();
  assertEquals(2, dictionaries.size());
  DictionaryInfo dictionary1 = dictionaries.get("test_version_1");
  assertTrue(dictionary1.isLoaded());
  DictionaryInfo dictionary2 = dictionaries.get("test_version_2");
  assertTrue(dictionary2.isLoaded());
  file.delete();
 }

}