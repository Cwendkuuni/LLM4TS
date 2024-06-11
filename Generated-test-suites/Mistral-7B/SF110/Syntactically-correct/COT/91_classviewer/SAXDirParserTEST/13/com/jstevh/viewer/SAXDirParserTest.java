package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.StringReader;
import java.util.*;
import com.jstevh.tools.*;
import javax.xml.parsers.*;
import org.junit.Test;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private StringReader reader;

 @Before
 public void setup() {
 parser = new SAXDirParser();
 reader = new StringReader("<Group>\n" +
         "<Web>some_web_data</Web>\n" +
         "<Local>\n" +
         " <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         " <parameter>line_number</parameter>\n" +
         "</Local>\n" +
         "<BrowserLoc>local_browser</BrowserLoc>\n" +
         "<Editor>file_editor</Editor>\n" +
         "<Names>\n" +
         " <pkg>java.lang</pkg>\n" +
         " <pkg>javax.swing.border</pkg>\n" +
         "</Names>\n" +
         "</Group>");
 }

 @Test
 public void testGetWebData() {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(reader));
 String actual = parser.getWebData();
 String expected = "some_web_data";
 assertEquals(expected, actual);
 }

 @Test
 public void testStartElement_BrowserLoc() {
 SAXDirParser parser = new SAXDirParser();
 parser.startElement("", "BrowserLoc", "BrowserLoc", null);
 assertTrue(parser.isCheck());
 assertEquals(0, parser.getLevel());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_UnexpectedElement() {
 SAXDirParser parser = new SAXDirParser();
 parser.startElement("", "UnexpectedTag", "UnexpectedTag", null);
 fail("Unexpected exception not thrown");
 }

 @Test
 public void testCharacters_AcceptsLineNumber_Yes() {
 SAXDirParser parser = new SAXDirParser();
 parser.characters(new char[]{'Y', 'e', 's'}, 0, 3);
 assertTrue(parser.isLineNumAccepted());
 }

 @Test
 public void testCharacters_AcceptsLineNumber_No() {
 SAXDirParser parser = new SAXDirParser();
 parser.characters(new char[]{'N', 'o'}, 0, 2);
 assertFalse(parser.isLineNumAccepted());
 }

 @Test
 public void testCharacters_Local() {
 SAXDirParser parser = new SAXDirParser();
 parser.characters(new char[]{'T', 'r', 'u', 'e'}, 0, 4);
 assertTrue(parser.isLocal());
 }

 @Test
 public void testCharacters_Local_False() {
 SAXDirParser parser = new SAXDirParser();
 parser.characters(new char[]{'F', 'a', 'l,', 's', 'e'}, 0, 6);
 assertFalse(parser.isLocal());
 }

 @Test
 public void testGetDirectory() {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(reader));
 Map<String, String> actual = parser.getDirectory();
 Map<String, String> expected = new HashMap<>();
 expected.put("java.lang", "some_web_data");
 expected.put("javax.swing.border", "some_web_data");
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalPackages() {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(reader));
 StringList actual = parser.getLocalPackages();
 StringList expected = new StringList();
 expected.add("java.lang");
 expected.add("javax.swing.border");
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalBrowser() {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(reader));
 String actual = parser.getLocalBrowser();
 String expected = "local_browser";
 assertEquals(expected, actual);
 }

 @Test
 public void testGetEditor() {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(reader));
 String actual = parser.getEditor();
 String expected = "file_editor";
 assertEquals(expected, actual);
 }

 @Test
 public void testAcceptsLineNumber() {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(reader));
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(new InputSource(reader));
 String actual = parser.lineNumberParameter();
 String expected = "line_number";
 assertEquals(expected, actual);
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testGetDirectory_Clone_Exception() {
 Map<String, String> actual = parser.getDirectory().clone();
 fail("Unexpected exception not thrown");
 }

 @Test (expected = NullPointerException.class)
 public void testGetLocalPackages_EmptyList_Null() {
 StringList actual = parser.getLocalPackages();
 assertNull(actual);
 }

}