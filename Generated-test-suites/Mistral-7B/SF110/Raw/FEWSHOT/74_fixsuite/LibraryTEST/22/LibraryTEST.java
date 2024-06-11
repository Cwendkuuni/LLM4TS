package org.fixsuite.message;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertLinesMatch;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.fixsuite.message.info.*;
import org.fixsuite.message.parsers.fpl.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.io.*;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Library.class)
public class LibraryTest {

 Library library = null;
 List<File> arrangedFiles = null;
 List<File> files = null;
 File path = null;

 @Before
 public void setup() {
 library = new Library();
 files = new ArrayList<>();
 arrangedFiles = new ArrayList<>();
 path = new File("testLibraryPath");
 }

 @Test
 public void testLibraryEmptyDirectory() {
 library.loadFromDirectory(path.getPath());
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLibraryNoFiles() {
 File emptyDir = new File(path.getPath() + "/empty");
 library.loadFromDirectory(emptyDir.getPath());
 assertTrue(library.getDictionaries().isEmpty());
 }

 @Test
 public void testLibraryOneFilePerDir() {
 File fieldsFile = new File(path.getPath() + "/Fields.xml");
 File enumsFile = new File(path.getPath() + "/Enums.xml");
 File componentsFile = new File(path.getPath() + "/Components.xml");
 File msgTypeFile = new File(path.getPath() + "/MsgType.xml");
 File msgContentsFile = new File(path.getPath() + "/MsgContents.xml");

 files.add(fieldsFile);
 files.add(enumsFile);
 files.add(componentsFile);
 files.add(msgTypeFile);
 files.add(msgContentsFile);

 arrangedFiles = library.arrangeFiles(files.toArray(new File[files.size()]));
 assertArrayEquals(new File[] { files.get(0), files.get(1), files.get(2), files.get(3), files.get(4) }, arrangedFiles.toArray());
 }

 @Test
 public void testLibraryMissingFile() {
 File fieldsFile = new File(path.getPath() + "/Fields.xml");
 File missingFile = new File(path.getPath() + "/MissingFile.xml");

 files.add(fieldsFile);
 files.add(missingFile);

 try {
 library.loadFromDirectory(path.getPath());
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testLibraryUnableToParseFile() {
 File fieldsFile = new File(path.getPath() + "/Fields.xml");
 PowerMockito.mockStatic(MainParser.class);
 Mockito.when(MainParser.parse(Mockito.any(DictionaryInfo.class), Mockito.any(File.class))).thenThrow(new RuntimeException());

 files.add(fieldsFile);
 try {
 library.loadFromDirectory(path.getPath());
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testLibraryLoadDictionary() {
 File fieldsFile = new File(path.getPath() + "/Fields.xml");
 File enumsFile = new File(path.getPath() + "/Enums.xml");
 File componentsFile = new File(path.getPath() + "/Components.xml");
 File msgTypeFile = new File(path.getPath() + "/MsgType.xml");
 File msgContentsFile = new File(path.getPath() + "/MsgContents.xml");

 files.add(fieldsFile);
 files.add(enumsFile);
 files.add(componentsFile);
 files.add(msgTypeFile);
 files.add(msgContentsFile);

 arrangedFiles = library.arrangeFiles(files.toArray(new File[files.size()]));
 try {
 library.loadFromDirectory(path.getPath());
 DictionaryInfo dictionary = library.getDictionary("v1.0");
 assertNotNull(dictionary);
 } catch (Exception e) {
 fail("Unexpected Exception: " + e.getMessage());
 }
 }

 @Test
 public void testLibraryLoadMultipleDictionaries() {
 File fieldsFile1 = new File(path.getPath() + "/Fields1.xml");
 File fieldsFile2 = new File(path.getPath() + "/Fields2.xml");
 File enumsFile1 = new File(path.getPath() + "/Enums1.xml");
 File enumsFile2 = new File(path.getPath() + "/Enums2.xml");
 File componentsFile1 = new File(path.getPath() + "/Components1.xml");
 File componentsFile2 = new File(path.getPath() + "/Components2.xml");
 File msgTypeFile1 = new File(path.getPath() + "/MsgType1.xml");
 File msgTypeFile2 = new File(path.getPath() + "/MsgType2.xml");
 File msgContentsFile1 = new File(path.getPath() + "/MsgContents1.xml");
 File msgContentsFile2 = new File(path.getPath() + "/MsgContents2.xml");

 files.add(fieldsFile1);
 files.add(fieldsFile2);
 files.add(enumsFile1);
 files.add(enumsFile2);
 files.add(componentsFile1);
 files.add(componentsFile2);
 files.add(msgTypeFile1);
 files.add(msgTypeFile2);
 files.add(msgContentsFile1);
 files.add(msgContentsFile2);

 arrangedFiles = library.arrangeFiles(files.toArray(new File[files.size()]));
 try {
 library.loadFromDirectory(path.getPath());
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(2, dictionaries.size());
 } catch (Exception e) {
 fail("Unexpected Exception: " + e.getMessage());
 }
 }

 @Test
 public void testLibraryLoadDictionariesWithSameVersion() {
 File fieldsFile1 = new File(path.getPath() + "/Fields1.xml");
 File fieldsFile2 = new File(path.getPath() + "/Fields2.xml");
 File enumsFile1 = new File(path.getPath() + "/Enums1.xml");
 File enumsFile2 = new File(path.getPath() + "/Enums2.xml");
 File componentsFile1 = new File(path.getPath() + "/Components1.xml");
 File componentsFile2 = new File(path.getPath() + "/Components2.xml");
 File msgTypeFile1 = new File(path.getPath() + "/MsgType1.xml");
 File msgTypeFile2 = new File(path.getPath() + "/MsgType2.xml");
 File msgContentsFile1 = new File(path.getPath() + "/MsgContents1.xml");
 File msgContentsFile2 = new File(path.getPath() + "/MsgContents2.xml");

 files.add(fieldsFile1);
 files.add(fieldsFile2);
 files.add(enumsFile1);
 files.add(enumsFile2);
 files.add(componentsFile1);
 files.add(componentsFile2);
 files.add(msgTypeFile1);
 files.add(msgTypeFile2);
 files.add(msgContentsFile1);
 files.add(msgContentsFile2);

 arrangedFiles = library.arrangeFiles(files.toArray(new File[files.size()]));
 try {
 library.loadFromDirectory(path.getPath());
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(1, dictionaries.size());
 assertSame(dictionaries.get(0), library.getDictionary("v1.0"));
 } catch (Exception e) {
 fail("Unexpected Exception: " + e.getMessage());
 }
 }

 @Test
 public void testLibraryLoadDictionariesWithDifferentVersions() {
 File fieldsFile1 = new File(path.getPath() + "/Fields1.xml");
 File fieldsFile2 = new File(path.getPath() + "/Fields2.xml");
 File enumsFile1 = new File(path.getPath() + "/Enums1.xml");
 File enumsFile2 = new File(path.getPath() + "/Enums2.xml");
 File componentsFile1 = new File(path.getPath() + "/Components1.xml");
 File componentsFile2 = new File(path.getPath() + "/Components2.xml");
 File msgTypeFile1 = new File(path.getPath() + "/MsgType1.xml");
 File msgTypeFile2 = new File(path.getPath() + "/MsgType2.xml");
 File msgContentsFile1 = new File(path.getPath() + "/MsgContents1.xml");
 File msgContentsFile2 = new File(path.getPath() + "/MsgContents2.xml");

 files.add(fieldsFile1);
 files.add(fieldsFile2);
 files.add(enumsFile1);
 files.add(enumsFile2);
 files.add(componentsFile1);
 files.add(componentsFile2);
 files.add(msgTypeFile1);
 files.add(msgTypeFile2);
 files.add(msgContentsFile1);
 files.add(msgContentsFile2);

 arrangedFiles = library.arrangeFiles(files.toArray(new File[files.size()]));
 try {
 library.loadFromDirectory(path.getPath());
 List<DictionaryInfo> dictionaries = library.getDictionaries();
 assertEquals(2, dictionaries.size());
 assertNotSame(dictionaries.get(0), dictionaries.get(1));
 } catch (Exception e) {
 fail("Unexpected Exception: " + e.getMessage());
 }
 }
}