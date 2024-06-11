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

 private SAXDirParser parser;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 SAXDirParser.directory = new HashMap<String, String>();
 SAXDirParser.localPackages = new StringList();
 SAXDirParser.localBrowser = "";
 SAXDirParser.file_editor = "";
 SAXDirParser.lineNumAccepted = false;
 SAXDirParser.lineNumParam = "";
 parser = new SAXDirParser();
 }

 @Test
 public void testGetWebData() {
 String expected = "";
 String actual = parser.getWebData();
 assertEquals(expected, actual);
 }

 @Test
 public void testStartElement_BrowserLoc() throws SAXException {
 parser.startElement("", "BrowserLoc", "BrowserLoc", null);
 assertTrue(parser.isCheck());
 assertEquals(0, parser.getLevel());
 assertFalse(parser.isLocal());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_BrowserLoc_InvalidQname() throws SAXException {
 parser.startElement("", "BrowserLocx", "BrowserLocx", null);
 }

 @Test
 public void testStartElement_Editor() throws SAXException {
 parser.startElement("", "Editor", "Editor", null);
 assertTrue(parser.isCheck());
 assertEquals(10, parser.getLevel());
 assertFalse(parser.isLocal());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws SAXException {
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 assertTrue(parser.isCheck());
 assertEquals(11, parser.getLevel());
 }

 @Test
 public void testStartElement_acceptsLineNumber_InvalidValue() throws SAXException {
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 parser.characters(new char[]{'Y'}, 0, 1);
 assertFalse(parser.isLineNumAccepted());
 }

 @Test
 public void testStartElement_parameter() throws SAXException {
 parser.startElement("", "parameter", "parameter", null);
 assertTrue(parser.isCheck());
 assertEquals(12, parser.getLevel());
 assertFalse(parser.isLocal());
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
 assertEquals(30, parser.getLevel());
 assertFalse(parser.isLocal());
 }

 @Test
 public void testStartElement_Local() throws SAXException {
 parser.startElement("", "Local", "Local", null);
 assertTrue(parser.isCheck());
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
 assertEquals(4, parser.getLevel());
 assertFalse(parser.isLocal());
 }

 @Test
 public void testCharacters_directory_ValidValue() throws SAXException {
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{'java', '.', 'l', 'a', 'n', 'g'}, 0, 6);
 parser.characters(new char[]{'.', 'j', 's', 't', 'e', 'v', 'h', '.', 'v', 'i', 'e', 'w', 'e'}, 0, 13);
 Map<String, String> expected = new HashMap<String, String>();
 expected.put("java.lang", "jsviewer.java");
 expected.put("javax.swing.border", "border.java");
 assertEquals(expected, parser.getDirectory());
 }

 @Test
 public void testCharacters_directory_EmptyValue() throws SAXException {
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{'', '\n'}, 0, 1);
 assertEquals(new HashMap<String, String>(), parser.getDirectory());
 }

 @Test
 public void testCharacters_localPackages_ValidValue() throws SAXException {
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{'j', 's', 't', 'e', 'v', 'h'}, 0, 6);
 List<String> expected = new ArrayList<String>();
 expected.add("jsviewer");
 assertEquals(expected, parser.getLocalPackages());
 }

 @Test
 public void testCharacters_localPackages_EmptyValue() throws SAXException {
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{'', '\n'}, 0, 1);
 assertNull(parser.getLocalPackages());
 }

 @Test
 public void testCharacters_localBrowser() throws SAXException {
 parser.startElement("", "BrowserLoc", "BrowserLoc", null);
 parser.characters(new char[]{'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/537.36'}, 0, 111);
 assertEquals("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/537.36", parser.getLocalBrowser());
 }

 @Test
 public void testCharacters_editor() throws SAXException {
 parser.startElement("", "Editor", "Editor", null);
 parser.characters(new char[]{'Notepad++'}, 0, 9);
 assertEquals("Notepad++", parser.getEditor());
 }

 @Test
 public void testCharacters_acceptsLineNumber() throws SAXException {
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 parser.characters(new char[]{'Yes'}, 0, 3);
 assertTrue(parser.isLineNumAccepted());
 }

 @Test
 public void testCharacters_lineNumberParameter() throws SAXException {
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 parser.characters(new char[]{'123'}, 0, 3);
 assertEquals("123", parser.getLineNumberParameter());
 }

 @Test
 public void testEndElement_Group() throws SAXException {
 parser.startElement("", "Group", "Group", null);
 parser.startElement("", "Group", "Group", null);
 parser.endElement("", "Group", "Group");
 assertFalse(parser.isCheck());
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> expected = new HashMap<String, String>();
 expected.put("java.lang", "jsviewer.java");
 expected.put("javax.swing.border", "border.java");
 Map<String, String> actual = SAXDirParser.getDirectory();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalPackages() {
 List<String> expected = new ArrayList<String>();
 expected.add("jsviewer");
 List<String> actual = SAXDirParser.getLocalPackages();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalBrowser() {
 String expected = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/537.36";
 String actual = SAXDirParser.getLocalBrowser();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetEditor() {
 String expected = "Notepad++";
 String actual = SAXDirParser.getEditor();
 assertEquals(expected, actual);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean expected = true;
 boolean actual = SAXDirParser.acceptsLineNumber();
 assertEquals(expected, actual);
 }

 @Test
 public void testLineNumberParameter() {
 String expected = "123";
 String actual = SAXDirParser.lineNumberParameter();
 assertEquals(expected, actual);
 }

 @Test
 public void testMain() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(new SAXDirParser());
 reader.parse("packagedirectory.xml");
 Map<String, String> expectedDirectory = new HashMap<String, String>();
 expectedDirectory.put("java.lang", "jsviewer.java");
 expectedDirectory.put("javax.swing.border", "border.java");
 List<String> expectedLocalPackages = new ArrayList<String>();
 expectedLocalPackages.add("jsviewer");
 String expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/537.36";
 String expectedEditor = "Notepad++";
 boolean expectedLineNumAccepted = true;
 String expectedLineNumberParameter = "123";
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 String actualEditor = SAXDirParser.getEditor();
 boolean actualLineNumAccepted = SAXDirParser.acceptsLineNumber();
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertTrue(expectedDirectory.equals(actualDirectory));
 assertTrue(expectedLocalPackages.equals(actualLocalPackages));
 assertTrue(expectedLocalBrowser.equals(actualLocalBrowser));
 assertTrue(expectedEditor.equals(actualEditor));
 assertTrue(expectedLineNumAccepted == actualLineNumAccepted);
 assertTrue(expectedLineNumberParameter.equals(actualLineNumberParameter));
 }

}