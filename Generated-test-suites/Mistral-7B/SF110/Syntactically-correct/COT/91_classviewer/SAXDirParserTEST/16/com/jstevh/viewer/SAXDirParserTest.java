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

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 }

 // Test case for getWebData method
 @Test
 public void testGetWebData() {
 String expected = "";
 String actual = parser.getWebData();
 assertEquals(expected, actual);
 }

 // Test case for constructor
 @Test
 public void testSAXDirParser() throws Exception {
 parser = new SAXDirParser();
 String actual = parser.getWebData();
 assertEquals("", actual);
 }

 // Test case for startElement method with "BrowserLoc" tag
 @Test
 public void testStartElementBrowserLoc() throws Exception {
 parser.startElement("", "BrowserLoc", "BrowserLoc", null);
 assertTrue(parser.isCheck());
 assertEquals(0, parser.getLevel());
 parser.setCheck(false);
 parser.setLevel(-1);
 }

 // Test case for startElement method with "Editor" tag
 @Test
 public void testStartElementEditor() throws Exception {
 parser.startElement("", "Editor", "Editor", null);
 assertTrue(parser.isCheck());
 assertEquals(10, parser.getLevel());
 parser.setCheck(false);
 parser.setLevel(-1);
 }

 // Test case for startElement method with "acceptsLineNumber" tag
 @Test
 public void testStartElementAcceptsLineNumber() throws Exception {
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 assertTrue(parser.isCheck());
 assertEquals(11, parser.getLevel());
 parser.setCheck(false);
 parser.setLevel(-1);
 }

 // Test case for startElement method with "parameter" tag
 @Test
 public void testStartElementParameter() throws Exception {
 parser.startElement("", "parameter", "parameter", null);
 assertTrue(parser.isCheck());
 assertEquals(12, parser.getLevel());
 parser.setCheck(false);
 parser.setLevel(-1);
 }

 // Test case for startElement method with "Group" tag
 @Test
 public void testStartElementGroup() throws Exception {
 parser.startElement("", "Group", "Group", null);
 assertFalse(parser.isCheck());
 assertEquals(-1, parser.getLevel());
 parser.setCheck(false);
 parser.setLevel(-1);
 }

 // Test case for startElement method with "Web" tag
 @Test
 public void testStartElementWeb() throws Exception {
 parser.startElement("", "Web", "Web", null);
 assertTrue(parser.isCheck());
 assertEquals(30, parser.getLevel());
 parser.setCheck(false);
 parser.setLevel(-1);
 }

 // Test case for startElement method with "Local" tag
 @Test
 public void testStartElementLocal() throws Exception {
 parser.startElement("", "Local", "Local", null);
 assertTrue(parser.isCheck());
 assertEquals(31, parser.getLevel());
 parser.setCheck(false);
 parser.setLevel(-1);
 }

 // Test case for startElement method with "Names" tag
 @Test
 public void testStartElementNames() throws Exception {
 parser.startElement("", "Names", "Names", null);
 assertTrue(parser.isCheck());
 }

 // Test case for startElement method with "pkg" tag
 @Test
 public void testStartElementPkg() throws Exception {
 parser.startElement("", "pkg", "pkg", null);
 assertTrue(parser.isCheck());
 assertEquals(4, parser.getLevel());
 }

 // Test case for characters method with "BrowserLoc" tag
 @Test
 public void testCharactersBrowserLoc() throws Exception {
 char[] charArray = "Firefox".toCharArray();
 parser.characters(charArray, 0, charArray.length);
 assertEquals("Firefox", parser.getLocalBrowser());
 parser.setLocalBrowser("");
 parser.setLevel(-1);
 }

 // Test case for characters method with "Editor" tag
 @Test
 public void testCharactersEditor() throws Exception {
 char[] charArray = "Notepad".toCharArray();
 parser.characters(charArray, 0, charArray.length);
 assertEquals("Notepad", parser.getFile_editor());
 parser.setFile_editor("");
 parser.setLevel(-1);
 }

 // Test case for characters method with "acceptsLineNumber" tag
 @Test
 public void testCharactersAcceptsLineNumber() throws Exception {
 char[] charArray = "Yes".toCharArray();
 parser.characters(charArray, 0, charArray.length);
 assertTrue(parser.isLineNumAccepted());
 parser.setLineNumAccepted(false);
 parser.setLevel(-1);
 }

 // Test case for characters method with "parameter" tag
 @Test
 public void testCharactersParameter() throws Exception {
 char[] charArray = "123".toCharArray();
 parser.characters(charArray, 0, charArray.length);
 assertEquals("123", parser.getLineNumParam());
 parser.setLineNumParam("");
 parser.setLevel(-1);
 }

 // Test case for characters method with "Web" tag
 @Test
 public void testCharactersWeb() throws Exception {
 char[] charArray = "http://www.example.com".toCharArray();
 parser.characters(charArray, 0, charArray.length);
 assertEquals("http://www.example.com", parser.getLocation());
 parser.setLocation("");
 parser.setLevel(-1);
 }

 // Test case for characters method with "Local" tag
 @Test
 public void testCharactersLocal() throws Exception {
 char[] charArray = "true".toCharArray();
 parser.characters(charArray, 0, charArray.length);
 assertTrue(parser.isLocal());
 parser.setLocal(false);
 parser.setLevel(-1);
 }

 // Test case for characters method with "pkg" tag
 @Test
 public void testCharactersPkg() throws Exception {
 char[] charArray = "com.test".toCharArray();
 parser.characters(charArray, 0, charArray.length);
 String expected = "com.test";
 Map<String, String> actual = parser.getDirectory();
 assertEquals(expected, actual.get(expected));
 parser.setDirectory(new HashMap<String, String>());
 parser.setLevel(-1);
 }

 // Test case for getDirectory method
 @Test
 public void testGetDirectory() {
 Map<String, String> expected = new HashMap<String, String>();
 expected.put("java.lang", "java.lang.Class");
 expected.put("javax.swing.border", "javax.swing.Border");
 Map<String, String> actual = SAXDirParser.getDirectory();
 assertEquals(expected, actual);
 }

 // Test case for getLocalPackages method
 @Test
 public void testGetLocalPackages() {
 List<String> expected = new ArrayList<String>();
 expected.add("com.test");
 List<String> actual = SAXDirParser.getLocalPackages();
 assertEquals(expected, actual);
 }

 // Test case for getLocalBrowser method
 @Test
 public void testGetLocalBrowser() {
 String expected = "Firefox";
 String actual = SAXDirParser.getLocalBrowser();
 assertEquals(expected, actual);
 }

 // Test case for getEditor method
 @Test
 public void testGetEditor() {
 String expected = "Notepad";
 String actual = SAXDirParser.getEditor();
 assertEquals(expected, actual);
 }

 // Test case for acceptsLineNumber method
 @Test
 public void testAcceptsLineNumber() {
 boolean expected = true;
 boolean actual = SAXDirParser.acceptsLineNumber();
 assertEquals(expected, actual);
 }

 // Test case for lineNumberParameter method
 @Test
 public void testLineNumberParameter() {
 String expected = "123";
 String actual = SAXDirParser.lineNumberParameter();
 assertEquals(expected, actual);
 }

 // Test case for exception handling when getDirectory method is called before parsing XML
 @Test(expected = SAXException.class)
 public void testGetDirectoryBeforeParsing() throws Exception {
 SAXDirParser.getDirectory();
 }
}