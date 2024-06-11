package com.jstevh.viewer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.StringList;

import org.xml.sax.SAXException;

import com.jstevh.viewer.SAXDirParser;
import com.jstevh.viewer.SAXParserFactory;
import com.jstevh.viewer.StringList;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 parser = new SAXDirParser();
 }

 @Test
 public void testGetWebData() {
 String webData = parser.getWebData();
 assertEquals("", webData);
 }

 @Test
 public void testStartElement_BrowserLoc() throws SAXException {
 parser.startElement("", "BrowserLoc", "BrowserLoc", null);
 assertTrue(parser.isCheck());
 assertFalse(parser.isLocal());
 assertEquals(0, parser.getLevel());
 }

 @Test
 public void testStartElement_Editor() throws SAXException {
 parser.startElement("", "Editor", "Editor", null);
 assertTrue(parser.isCheck());
 assertTrue(parser.isLocal());
 assertEquals(10, parser.getLevel());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws SAXException {
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 assertTrue(parser.isCheck());
 assertFalse(parser.isLocal());
 assertEquals(11, parser.getLevel());
 }

 @Test
 public void testStartElement_parameter() throws SAXException {
 parser.startElement("", "parameter", "parameter", null);
 assertTrue(parser.isCheck());
 assertFalse(parser.isLocal());
 assertEquals(12, parser.getLevel());
 }

 @Test
 public void testStartElement_Group() throws SAXException {
 parser.startElement("", "Group", "Group", null);
 assertFalse(parser.isCheck());
 }

 @Test
 public void testStartElement_Web() throws SAXException {
 parser.startElement("", "Web", "Web", null);
 assertTrue(parser.isCheck());
 assertTrue(parser.isLocal());
 assertEquals(30, parser.getLevel());
 }

 @Test
 public void testStartElement_Local() throws SAXException {
 parser.startElement("", "Local", "Local", null);
 assertTrue(parser.isCheck());
 assertTrue(parser.isLocal());
 assertEquals(31, parser.getLevel());
 }

 @Test
 public void testStartElement_Names() throws SAXException {
 parser.startElement("", "Names", "Names", null);
 assertFalse(parser.isCheck());
 }

 @Test
 public void testStartElement_pkg() throws SAXException {
 parser.startElement("", "pkg", "pkg", null);
 assertTrue(parser.isCheck());
 assertFalse(parser.isLocal());
 assertEquals(4, parser.getLevel());
 }

 @Test
 public void testCharacters_lineNumberAccepted() throws SAXException {
 parser.characters("Yes".toCharArray(), 0, "Yes".length());
 assertTrue(parser.isLineNumAccepted());
 }

 @Test
 public void testCharacters_lineNumberAccepted_No() throws SAXException {
 parser.characters("No".toCharArray(), 0, "No".length());
 assertFalse(parser.isLineNumAccepted());
 }

 @Test
 public void testCharacters_lineNumberParam() throws SAXException {
 parser.characters("lineNumber".toCharArray(), 0, "lineNumber".length());
 assertEquals("lineNumber", parser.getLineNumParam());
 }

 @Test
 public void testCharacters_location() throws SAXException {
 parser.characters("java.lang".toCharArray(), 0, "java.lang".length());
 assertEquals("java.lang", parser.getLocation());
 }

 @Test
 public void testCharacters_local() throws SAXException {
 parser.characters("No".toCharArray(), 0, "No".length());
 assertFalse(parser.isLocal());
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> directory = SAXDirParser.getDirectory();
 assertNotNull(directory);
 assertEquals(2, directory.size());
 assertEquals("java.lang", directory.keySet().toArray()[0].toString());
 assertEquals("java.lang", directory.get(directory.keySet().toArray()[0]));
 assertEquals("javax.swing.border", directory.get(directory.keySet().toArray()[1]));
 }

 @Test
 public void testGetLocalPackages() {
 StringList localPackages = SAXDirParser.getLocalPackages();
 assertNotNull(localPackages);
 assertFalse(localPackages.isEmpty());
 assertEquals(1, localPackages.size());
 assertEquals("java.lang", localPackages.get(0).toString());
 }

 @Test
 public void testGetLocalBrowser() {
 String localBrowser = SAXDirParser.getLocalBrowser();
 assertEquals("", localBrowser);
 }

 @Test
 public void testGetEditor() {
 String editor = SAXDirParser.getEditor();
 assertEquals("", editor);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean acceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertFalse(acceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() {
 String lineNumberParam = SAXDirParser.lineNumberParameter();
 assertEquals("", lineNumberParam);
 }

 @Test(expected = SAXException.class)
 public void testInvalidElement() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser parser = factory.newSAXParser();
 XMLReader xmlReader = parser.getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse("invalid.xml");
 }

 @Test
 public void testNullArguments() {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser parser = factory.newSAXParser();
 XMLReader xmlReader = parser.getXMLReader();
 xmlReader.setContentHandler(null);
 xmlReader.parse("valid.xml");
 }

}