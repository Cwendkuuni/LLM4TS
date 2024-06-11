package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.jstevh.tools.StringList;
import com.jstevh.viewer.SAXDirParser;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Test
 public void testGetWebData() throws Exception {
 parser = new SAXDirParser();
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() throws Exception {
 parser = new SAXDirParser();
 String expected = "someBrowser";
 parser.startElement("", "BrowserLoc", "BrowserLoc", null);
 parser.characters(new char[]{'s', 'o', 'm', 'e', 'B', 'r', 'o', 'w', 's', 'e', 'r', '.'}, 0, 13);
 assertEquals(expected, parser.getLocalBrowser());
 }

 @Test
 public void testStartElement_Editor() throws Exception {
 parser = new SAXDirParser();
 String expected = "someEditor";
 parser.startElement("", "Editor", "Editor", null);
 parser.characters(new char[]{'s', 'o', 'm', 'e', 'E', 'd', 'i', 't', 'o', 'r'}, 0, 10);
 assertEquals(expected, parser.getFile_editor());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 parser.characters(new char[]{'Y', 'e', 's'}, 0, 3);
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElement_acceptsLineNumber_No() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 parser.characters(new char[]{'N', 'o'}, 0, 2);
 assertFalse(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElement_lineNumberParameter() throws Exception {
 parser = new SAXDirParser();
 String expected = "someParam";
 parser.startElement("", "parameter", "parameter", null);
 parser.characters(new char[]{'s', 'o', 'm', 'e', 'P', 'a', 'r', 'a', 'm'}, 0, 10);
 assertEquals(expected, parser.lineNumberParameter());
 }

 @Test
 public void testGetDirectory() throws Exception {
 parser = new SAXDirParser();
 HashMap<String, String> expected = new HashMap<>();
 expected.put("java.lang", "someLocationJavaLang");
 expected.put("javax.swing.border", "someLocationJSwingBorder");
 parser.startElement("", "Web", "Web", null);
 parser.startElement("", "Local", "Local", null);
 parser.startElement("", "Names", "Names", null);
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{'j', 'a', 'v', 'a', '.'}, 0, 5);
 parser.startElement("", "java.lang", "java.lang", null);
 parser.characters(new char[]{'s', 'o', 'm', 'e', 'L', 'o', 'c', 'a', 't', 'i', 'o', 'n'}, 0, 12);
 parser.endElement("", "pkg", null);
 parser.endElement("", "Names", null);
 parser.endElement("", "Local", null);
 parser.endElement("", "Web", null);
 parser.characters(new char[]{'s', 'o', 'm', 'e', 'L', 'o', 'c', 'a', 't', 'i', 'o', 'n', '.', 'j', 'a', 'v', 'a'}, 0, 16);
 parser.characters(new char[]{'.', '.', 'c', 'o', 'm', '.', 'j', 's', 't', 'e', 'v', 'h', '.', 'v', 'i', 'e', 'w'}, 0, 18);
 parser.characters(new char[]{'/', 'p', 'a', 'c', 'k', 'a', 'g', 'e', 'd', 'i', 'r', 'e', 'c', 't', 'o', 'r', '.', 'g', 'o', 'x'}, 0, 21);
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{'j', 's', 't', 'e', 'v', 'h', '.', 't', 'o', 'o', 'l', 's'}, 0, 12);
 parser.endElement("", "pkg", null);
 Map<String, String> actual = SAXDirParser.getDirectory();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 parser = new SAXDirParser();
 StringList expected = new ArrayList<>();
 expected.add("jstevh.tools");
 expected.add("java.lang");
 expected.add("javax.swing.border");
 parser.startElement("", "Web", "Web", null);
 parser.startElement("", "Local", "Local", null);
 parser.startElement("", "Names", "Names", null);
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{'j', 's', 't', 'e', 'v', 'h', '.', 't', 'o', 'o', 'l', 's'}, 0, 12);
 parser.endElement("", "pkg", null);
 parser.endElement("", "Names", null);
 parser.endElement("", "Local", null);
 parser.endElement("", "Web", null);
 parser.characters(new char[]{'j', 's', 't', 'e', 'v', 'h', '.', 't', 'o', 'o', 'l', 's'}, 0, 12);
 parser.characters(new char[]{'.'}, 0, 1);
 parser.startElement("", "Local", "Local", null);
 parser.startElement("", "pkg", "pkg", null);
 parser.characters(new char[]{'j', 'a', 'v', 'a'}, 0, 4);
 parser.endElement("", "pkg", null);
 parser.endElement("", "Local", null);
 StringList actual = SAXDirParser.getLocalPackages();
 assertEquals(expected, actual);
 }

 @Test(expected = SAXException.class)
 public void testStartElement_UnknownElement() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "Unknown", "Unknown", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetDirectory_Null() {
 parser = new SAXDirParser();
 assertNull(SAXDirParser.getDirectory());
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackages_Null() {
 parser = new SAXDirParser();
 assertNull(SAXDirParser.getLocalPackages());
 }

}