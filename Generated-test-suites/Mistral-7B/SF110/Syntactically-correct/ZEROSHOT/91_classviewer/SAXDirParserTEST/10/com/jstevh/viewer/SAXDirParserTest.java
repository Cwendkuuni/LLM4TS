package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private HashMap<String, String> expectedDirectory;
 private List<String> expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedAcceptsLineNumber;
 private String expectedLineNumberParameter;

 @Before
 public void setUp() {
 saxDirParser = new SAXDirParser();
 expectedDirectory = new HashMap<String, String>();
 expectedDirectory.put("java.lang", "test/java/lang");
 expectedDirectory.put("javax.swing.border", "test/javax/swing/border");
 expectedLocalPackages = List.of("com.jstevh.local", "com.jstevh.viewer");
 expectedLocalBrowser = "Firefox";
 expectedEditor = "Notepad";
 expectedAcceptsLineNumber = true;
 expectedLineNumberParameter = "5";
 }

 @Test
 public void testParseXMLFile() throws Exception {
 File xmlFile = new File("packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);

 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 String actualEditor = SAXDirParser.getEditor();
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();

 assertEquals("test directory, test gives", expectedDirectory, actualDirectory);
 assertEquals("test local packages, test gives", expectedLocalPackages, actualLocalPackages);
 assertEquals("test local browser, test gives", expectedLocalBrowser, actualLocalBrowser);
 assertEquals("test editor, test gives", expectedEditor, actualEditor);
 assertEquals("test accepts line number, test gives", expectedAcceptsLineNumber, actualAcceptsLineNumber);
 assertEquals("test line number parameter, test gives", expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test(expected = Exception.class)
 public void testParseXMLFileWithException() throws Exception {
 File xmlFile = new File("nonexistentfile.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);
 fail("test parse XML file with exception should have thrown exception");
 }

 @Test
 public void testGetEmptyDirectory() {
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals("test empty directory, test gives", 0, actualDirectory.size());
 }

 @Test
 public void testGetEmptyLocalPackages() {
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertNull("test empty local packages, test gives", actualLocalPackages);
 }

 @Test
 public void testGetInvalidLocalBrowser() {
 SAXDirParser.localBrowser = "InvalidBrowser";
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals("test invalid local browser, test gives", "InvalidBrowser", actualLocalBrowser);
 assertEquals("test directory, test gives", expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetInvalidEditor() {
 SAXDirParser.file_editor = "InvalidEditor";
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 String actualEditor = SAXDirParser.getEditor();
 assertEquals("test invalid editor, test gives", "InvalidEditor", actualEditor);
 assertEquals("test directory, test gives", expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetAcceptsLineNumberFalse() {
 SAXDirParser.lineNumAccepted = false;
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertFalse("test accepts line number false, test gives", actualAcceptsLineNumber);
 assertEquals("test directory, test gives", expectedDirectory, SAXDirParser.getDirectory());
 }

 @Test
 public void testGetAcceptsLineNumberTrue() {
 SAXDirParser.lineNumAccepted = true;
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertTrue("test accepts line number true, test gives", actualAcceptsLineNumber);
 assertEquals("test directory, test gives", expectedDirectory, SAXDirParser.getDirectory());
 }

 @Test
 public void testGetLineNumberParameterEmpty() {
 SAXDirParser.lineNumParam = "";
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals("test line number parameter empty, test gives", "", actualLineNumberParameter);
 assertEquals("test directory, test gives", expectedDirectory, SAXDirParser.getDirectory());
 }

 @Test
 public void testGetLineNumberParameterValid() {
 SAXDirParser.lineNumParam = "5";
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals("test line number parameter valid, test gives", "5", actualLineNumberParameter);
 assertEquals("test directory, test gives", expectedDirectory, SAXDirParser.getDirectory());
 }

}