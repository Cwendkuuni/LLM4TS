package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;

import com.jstevh.viewer.SAXDirParser;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private Map<String, String> directory;
 private List<String> localPackages;
 private String localBrowser;
 private String fileEditor;
 private boolean lineNumAccepted;
 private String lineNumParam;

 @Before
 public void setUp() {
 saxDirParser = new SAXDirParser();
 directory = new HashMap<>();
 localPackages = new ArrayList<>();
 lineNumAccepted = false;
 lineNumParam = "";
 }

 @Test
 public void testGetWebData() {
 SAXDirParser saxDirParser = new SAXDirParser();
 String webData = saxDirParser.getWebData();
 assertEquals("", webData);
 }

 @Test
 public void testStartElement_BrowserLoc() throws SAXException {
 saxDirParser.startElement("", "BrowserLoc", "BrowserLoc", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(0, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.acceptsLineNumber());
 assertNull(saxDirParser.lineNumberParameter());
 }

 @Test
 public void testStartElement_Editor() throws SAXException {
 saxDirParser.startElement("", "Editor", "Editor", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(10, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws SAXException {
 saxDirParser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(11, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertTrue(saxDirParser.isLineNumAccepted());
 assertNull(saxDirParser.lineNumberParameter());
 }

 @Test
 public void testStartElement_parameter() throws SAXException {
 saxDirParser.startElement("", "parameter", "parameter", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(12, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertEquals("", saxDirParser.lineNumberParameter());
 }

 @Test
 public void testStartElement_Group() throws SAXException {
 saxDirParser.startElement("", "Group", "Group", null);
 assertFalse(saxDirParser.isCheck());
 }

 @Test
 public void testStartElement_Web() throws SAXException {
 saxDirParser.startElement("", "Web", "Web", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(30, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testStartElement_Local() throws SAXException {
 saxDirParser.startElement("", "Local", "Local", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(31, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testStartElement_Names() throws SAXException {
 saxDirParser.startElement("", "Names", "Names", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(2, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testStartElement_pkg() throws SAXException {
 saxDirParser.startElement("", "pkg", "pkg", null);
 assertTrue(saxDirParser.isCheck());
 assertEquals(4, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertEquals("", saxDirParser.getDirectory().get(""));
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_checkAndLevelGreaterThanOrEqualZero() throws SAXException {
 char[] charArray = new char[]{'t', 'e', 's', 't', ' ', 'd', 'i', 'r', 'e', 'c', 't', 'o', 'r', 'y', '.', ' ', 'G', 'o', 'u', 'p', ' ', 'o', 'f', ' ', 'p', 'a', 'c', 'k', 'a', 'g', 'e', 's'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertTrue(saxDirParser.isCheck());
 assertEquals(0, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_BrowserLoc() throws SAXException {
 char[] charArray = new char[]{'B', 'r', 'o', 'w', 's', 'e', 'r', ' ', 'L', 'o', 'c', ' ', 'l', 'o', 'c', 'a', 't', 'i', 'o', 'n', '=', ' ', 'T', 'e', 's', 't', ' ', 'B', 'r', 'o', 'w', 's', 'e', 'r'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertEquals("Test Browser", saxDirParser.getLocalBrowser());
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(-1, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_Editor() throws SAXException {
 char[] charArray = new char[]{'E', 'd', 'i', 't', 'o', 'r', '=', ' ', 'T', 'e', 's', 't', ' ', 'E', 'd', 'i', 't', 'o', 'r'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertEquals("Test Editor", saxDirParser.getFileEditor());
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(-1, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_acceptsLineNumberYes() throws SAXException {
 char[] charArray = new char[]{'a', 'c', 'c', 'e', 'p', 't', 's', ' ', 'L', 'i', 'n', 'e', ' ', 'N', 'u', 'm', 'b', 'e', 'r', ' ', 'y', 'e', 's'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertTrue(saxDirParser.isLineNumAccepted());
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(-1, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_acceptsLineNumberNo() throws SAXException {
 char[] charArray = new char[]{'a', 'c', 'c', 'e', 'p', 't', 's', ' ', 'L', 'i', 'n', 'e', ' ', 'n', 'o'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertFalse(saxDirParser.isLineNumAccepted());
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(-1, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_parameter() throws SAXException {
 char[] charArray = new char[]{'p', 'a', 'r', 'a', 'm', 'e', 't', 'e', 'r', '=', ' ', '5'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertEquals("5", saxDirParser.lineNumberParameter());
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(-1, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_Web() throws SAXException {
 char[] charArray = new char[]{'T', 'e', 's', 't', ' ', 'W', 'e', 'b', '\r', '\n'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertEquals("Test", saxDirParser.getLocation());
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(-1, saxDirParser.getLevel());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_Local() throws SAXException {
 char[] charArray = new char[]{'N', 'o', '\r', '\n'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertFalse(saxDirParser.isLocal());
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(-1, saxDirParser.getLevel());
 assertNull(saxDirParser.getLocation());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_Names() throws SAXException {
 char[] charArray = new char[]{'N', 'a', 'm', 'e', 's', '\r', '\n'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertNull(saxDirParser.getLocation());
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(-1, saxDirParser.getLevel());
 assertNull(saxDirParser.getEditor());
 assertNull(saxDirParser.getDirectory());
 assertNull(saxDirParser.getLocalPackages());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_pkg() throws SAXException {
 char[] charArray = new char[]{'j', 'a', 'v', 'a', '.', 'l', 'a', 'n', 'g', '.', 'p', 'a', 'c', 'k', 'a', 'g', 'e', 's', '\r', '\n'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertEquals("java.lang.packages", saxDirParser.getDirectory().get("java.lang"));
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(-1, saxDirParser.getLevel());
 assertNull(saxDirParser.getEditor());
 assertNotNull(saxDirParser.getLocalPackages());
 assertFalse(saxDirParser.isLocal());
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testCharacters_pkgAcceptLocal() throws SAXException {
 char[] charArray = new char[]{'j', 's', 'r', '.', 'j', 's', 'r', '.', 'p', 'a', 'c', 'k', 'a', 'g', 'e', 's', '\r', '\n'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 assertEquals("jsr.packeges", saxDirParser.getDirectory().get("jsr"));
 assertTrue(saxDirParser.isLocal());
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(-1, saxDirParser.getLevel());
 assertNull(saxDirParser.getEditor());
 assertNotNull(saxDirParser.getLocalPackages());
 assertNull(saxDirParser.getWebData());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_UnexpectedElement() throws SAXException {
 saxDirParser.startElement("", "UnexpectedElement", "UnexpectedElement", null);
 }

 @Test(expected = SAXException.class)
 public void testCharacters_UnexpectedCharacter() throws SAXException {
 char[] charArray = new char[]{'@', 't', 'e', 's', 't'};
 int start = 0;
 int length = charArray.length;
 saxDirParser.characters(charArray, start, length);
 }

 @After
 public void tearDown() {
 saxDirParser = null;
 directory.clear();
 localPackages.clear();
 localBrowser = null;
 fileEditor = null;
 lineNumAccepted = false;
 lineNumParam = null;
 }
}