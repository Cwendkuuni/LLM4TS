package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;

import com.jstevh.viewer.SAXDirParser;
import com.jstevh.viewer.SAXParserFactory;
import com.jstevh.viewer.StringList;

public class SAXDirParserTest {

 private SAXDirParser parser = null;

 @org.junit.Before
 public void setUp() throws Exception {
 parser = new SAXDirParser();
 }

 // Test for getWebData() method
 @org.junit.Test
 public void testGetWebData() {
 String expected = "";
 String actual = parser.getWebData();
 assertEquals(expected, actual);
 }

 // Test for startElement() method with "BrowserLoc" tag
 @org.junit.Test
 public void testStartElementBrowserLoc() throws SAXException {
 parser.startElement("", "BrowserLoc", "BrowserLoc", null);
 assertTrue(parser.isCheck());
 assertEquals(0, parser.getLevel());
 assertFalse(parser.isLocal());
 parser.endElement("", "BrowserLoc", "BrowserLoc");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for startElement() method with "Editor" tag
 @org.junit.Test
 public void testStartElementEditor() throws SAXException {
 parser.startElement("", "Editor", "Editor", null);
 assertTrue(parser.isCheck());
 assertEquals(10, parser.getLevel());
 assertFalse(parser.isLocal());
 parser.endElement("", "Editor", "Editor");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for startElement() method with "acceptsLineNumber" tag
 @org.junit.Test
 public void testStartElementAcceptsLineNumber() throws SAXException {
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 assertTrue(parser.isCheck());
 assertEquals(11, parser.getLevel());
 assertFalse(parser.isLocal());
 parser.endElement("", "acceptsLineNumber", "acceptsLineNumber");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for startElement() method with "parameter" tag
 @org.junit.Test
 public void testStartElementParameter() throws SAXException {
 parser.startElement("", "parameter", "parameter", null);
 assertTrue(parser.isCheck());
 assertEquals(12, parser.getLevel());
 assertFalse(parser.isLocal());
 parser.endElement("", "parameter", "parameter");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for startElement() method with "Group" tag
 @org.junit.Test
 public void testStartElementGroup() throws SAXException {
 parser.startElement("", "Group", "Group", null);
 assertFalse(parser.isCheck());
 assertEquals(-1, parser.getLevel());
 assertFalse(parser.isLocal());
 parser.endElement("", "Group", "Group");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for startElement() method with "Web" tag
 @org.junit.Test
 public void testStartElementWeb() throws SAXException {
 parser.startElement("", "Web", "Web", null);
 assertTrue(parser.isCheck());
 assertEquals(30, parser.getLevel());
 assertFalse(parser.isLocal());
 parser.endElement("", "Web", "Web");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for startElement() method with "Local" tag
 @org.junit.Test
 public void testStartElementLocal() throws SAXException {
 parser.startElement("", "Local", "Local", null);
 assertTrue(parser.isCheck());
 assertEquals(31, parser.getLevel());
 assertTrue(parser.isLocal());
 parser.endElement("", "Local", "Local");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for startElement() method with "Names" tag
 @org.junit.Test
 public void testStartElementNames() throws SAXException {
 parser.startElement("", "Names", "Names", null);
 assertFalse(parser.isCheck());
 assertEquals(-1, parser.getLevel());
 assertFalse(parser.isLocal());
 parser.endElement("", "Names", "Names");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for startElement() method with "pkg" tag
 @org.junit.Test
 public void testStartElementPkg() throws SAXException {
 parser.startElement("", "pkg", "pkg", null);
 assertTrue(parser.isCheck());
 assertEquals(4, parser.getLevel());
 assertFalse(parser.isLocal());
 String expected = "";
 String actual = parser.getLocation();
 assertEquals(expected, actual);
 parser.endElement("", "pkg", "pkg");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for characters() method with "Yes" parameter
 @org.junit.Test
 public void testCharactersYes() throws SAXException {
 char[] expected = new char[]{'Y', 'e', 's', ' ', 'Y', 'e', 's'};
 int start = 0;
 int length = 6;
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 parser.characters(expected, start, length);
 assertTrue(parser.isCheck());
 assertEquals(11, parser.getLevel());
 assertFalse(parser.isLocal());
 assertTrue(parser.isLineNumAccepted());
 parser.endElement("", "acceptsLineNumber", "acceptsLineNumber");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for characters() method with "No" parameter
 @org.junit.Test
 public void testCharactersNo() throws SAXException {
 char[] expected = new char[]{'N', 'o', ' '};
 int start = 0;
 int length = 2;
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 parser.characters(expected, start, length);
 assertTrue(parser.isCheck());
 assertEquals(11, parser.getLevel());
 assertFalse(parser.isLocal());
 assertFalse(parser.isLineNumAccepted());
 parser.endElement("", "acceptsLineNumber", "acceptsLineNumber");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for characters() method with valid package name
 @org.junit.Test
 public void testCharactersValidPackageName() throws SAXException {
 char[] expected = new char[]{'J', 'a', 'v', 'a', '.', 'L', 'a', 'n', 'g', '.', 'B', 'o', 'r', 'd', 'e', 'r', '.'};
 int start = 0;
 int length = 16;
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(expected, start, length);
 assertTrue(parser.isCheck());
 assertEquals(4, parser.getLevel());
 assertFalse(parser.isLocal());
 String expectedLocation = "JAVA.LANG.BORDER";
 String actualLocation = parser.getLocation();
 assertEquals(expectedLocation, actualLocation);
 parser.endElement("", "pkg", "pkg");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for characters() method with invalid package name
 @org.junit.Test
 public void testCharactersInvalidPackageName() throws SAXException {
 char[] expected = new char[]{'I', 'n', 'v', 'a', 'l', 'i', 'd', '.', 'I', 'n', 'v', 'a', 'l', 'i', 'd'};
 int start = 0;
 int length = 11;
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(expected, start, length);
 assertTrue(parser.isCheck());
 assertEquals(4, parser.getLevel());
 assertFalse(parser.isLocal());
 String expectedErrorMessage = "Error in parsing package name";
 String actualErrorMessage = "";
 try {
 parser.getLocation();
 } catch (Exception e) {
 actualErrorMessage = e.getMessage();
 }
 assertEquals(expectedErrorMessage, actualErrorMessage);
 parser.endElement("", "pkg", "pkg");
 assertFalse(parser.isCheck());
 assertFalse(parser.isLocal());
 }

 // Test for getDirectory() method
 @org.junit.Test
 public void testGetDirectory() throws Exception {
 Map<String, String> expected = new HashMap<String, String>();
 expected.put("java.lang", "JAVA.LANG.BORDER");
 expected.put("javax.swing.border", "JAVASWING.BORDER.ETC");
 Map<String, String> actual = SAXDirParser.getDirectory();
 assertEquals(expected, actual);
 }

 // Test for getLocalPackages() method
 @org.junit.Test
 public void testGetLocalPackages() throws Exception {
 List<String> expected = new ArrayList<String>();
 expected.add("java");
 expected.add("javax.swing.border");
 List<String> actual = SAXDirParser.getLocalPackages();
 assertEquals(expected, actual);
 }

 // Test for getLocalBrowser() method
 @org.junit.Test
 public void testGetLocalBrowser() throws Exception {
 String expected = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3092.0 Safari/537.36";
 String actual = SAXDirParser.getLocalBrowser();
 assertEquals(expected, actual);
 }

 // Test for getEditor() method
 @org.junit.Test
 public void testGetEditor() throws Exception {
 String expected = "Notepad++";
 String actual = SAXDirParser.getEditor();
 assertEquals(expected, actual);
 }

 // Test for acceptsLineNumber() method
 @org.junit.Test
 public void testAcceptsLineNumber() throws Exception {
 boolean expected = true;
 boolean actual = SAXDirParser.acceptsLineNumber();
 assertEquals(expected, actual);
 }

 // Test for lineNumberParameter() method
 @org.junit.Test
 public void testLineNumberParameter() throws Exception {
 String expected = "123";
 String actual = SAXDirParser.lineNumberParameter();
 assertEquals(expected, actual);
 }

 // Test for main() method
 @org.junit.Test
 public void testMain() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser parser = factory.newSAXParser();
 XMLReader xmlReader = parser.getXMLReader();
 xmlReader.setContentHandler(new SAXDirParser());
 xmlReader.parse("packagedirectory.xml");
 Map<String, String> expectedDirectory = new HashMap<String, String>();
 expectedDirectory.put("java.lang", "JAVA.LANG.BORDER");
 expectedDirectory.put("javax.swing.border", "JAVASWING.BORDER.ETC");
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 List<String> expectedLocalPackages = new ArrayList<String>();
 expectedLocalPackages.add("java");
 expectedLocalPackages.add("javax.swing.border");
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 String expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3092.0 Safari/537.36";
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 String expectedEditor = "Notepad++";
 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 boolean expectedAcceptLineNumber = true;
 boolean actualAcceptLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptLineNumber, actualAcceptLineNumber);
 String expectedLineNumberParameter = "123";
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }
}