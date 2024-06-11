package com.jstevh.viewer.test;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.jstevh.viewer.*;
import static org.junit.Assert.*;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Test
 public void testGetWebData() {
 SAXDirParser parser = new SAXDirParser();
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() throws SAXException {
 parser = new SAXDirParser();
 String expectedBrowser = "expectedBrowser";
 parser.startElement("", "BrowserLoc", "BrowserLoc", null);
 parser.characters(new char[]{}, 0, 0);
 parser.characters(expectedBrowser.toCharArray(), 0, expectedBrowser.length());
 assertEquals(expectedBrowser, parser.getLocalBrowser());
 }

 @Test
 public void testStartElement_Editor() throws SAXException {
 parser = new SAXDirParser();
 String expectedEditor = "expectedEditor";
 parser.startElement("", "Editor", "Editor", null);
 parser.characters(new char[]{}, 0, 0);
 parser.characters(expectedEditor.toCharArray(), 0, expectedEditor.length());
 assertEquals(expectedEditor, parser.getFile_editor());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws SAXException {
 parser = new SAXDirParser();
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 parser.characters(new char[]{'Y', 'e', 's'.toCharArray()}, 0, 3);
 assertTrue(parser.acceptsLineNumber());
 parser.characters(new char[]{'N'.toCharArray()}, 0, 1);
 assertFalse(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElement_parameter() throws SAXException {
 parser = new SAXDirParser();
 String expectedParameter = "expectedParameter";
 parser.startElement("", "parameter", "parameter", null);
 parser.characters(new char[]{}, 0, 0);
 parser.characters(expectedParameter.toCharArray(), 0, expectedParameter.length());
 assertEquals(expectedParameter, parser.lineNumberParameter());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_Web() throws SAXException {
 parser = new SAXDirParser();
 parser.startElement("", "Web", "Web", null);
 }

 @Test
 public void testStartElement_Local() throws SAXException {
 parser = new SAXDirParser();
 parser.startElement("", "Local", "Local", null);
 parser.characters(new char[]{'N'.toCharArray()}, 0, 1);
 assertFalse(parser.isLocal());
 parser.characters(new char[]{'Y'.toCharArray()}, 0, 1);
 assertTrue(parser.isLocal());
 }

 @Test
 public void testStartElement_Names() throws SAXException {
 parser = new SAXDirParser();
 parser.startElement("", "Names", "Names", null);
 }

 @Test
 public void testStartElement_pkg() throws SAXException {
 parser = new SAXDirParser();
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{'java'.toCharArray()}, 0, 4);
 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java", "expectedLocation1");
 assertEquals(expectedDirectory, SAXDirParser.getDirectory());
 }

 @Test
 public void testStartElement_pkg_emptyString() throws SAXException {
 parser = new SAXDirParser();
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{''.toCharArray()}, 0, 0);
 HashMap<String, String> expectedDirectory = new HashMap<>();
 assertTrue(SAXDirParser.getDirectory().isEmpty());
 }

 @Test
 public void testStartElement_pkg_null() throws SAXException {
 parser = new SAXDirParser();
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{}, 0, 0);
 HashMap<String, String> expectedDirectory = new HashMap<>();
 assertTrue(SAXDirParser.getDirectory().isEmpty());
 }

 @Test
 public void testGetDirectory() {
 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "expectedLocation1");
 expectedDirectory.put("javax.swing.border", "expectedLocation2");
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("java");
 expectedLocalPackages.add("javax.swing.border");
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalPackages_emptyList() {
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertTrue(actualLocalPackages.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackages_nullLocalPackages() {
 SAXDirParser.localPackages = null;
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 }

 @Test
 public void testGetLocalBrowser() {
 String expectedLocalBrowser = "expectedLocalBrowser";
 SAXDirParser.localBrowser = expectedLocalBrowser;
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() {
 String expectedEditor = "expectedEditor";
 SAXDirParser.file_editor = expectedEditor;
 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() {
 SAXDirParser.lineNumAccepted = true;
 assertTrue(SAXDirParser.acceptsLineNumber());
 SAXDirParser.lineNumAccepted = false;
 assertFalse(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 String expectedLineNumberParameter = "expectedLineNumberParameter";
 SAXDirParser.lineNumParam = expectedLineNumberParameter;
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test
 public void testMain() throws Exception {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(new SAXDirParser());
 myReader.parse("packagedirectory.xml");
 }

}