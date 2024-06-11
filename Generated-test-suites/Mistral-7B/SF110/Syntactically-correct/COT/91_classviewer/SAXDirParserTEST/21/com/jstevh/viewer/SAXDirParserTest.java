package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.jstevh.viewer.SAXDirParser;
import com.jstevh.viewer.SAXParserFactory;
import com.jstevh.viewer.StringList;

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
 String xml = "<Web>\r\n<Group>\r\n<BrowserLoc>Google</BrowserLoc>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 assertEquals("Google", parser.getLocalBrowser());
 }

 @Test
 public void testStartElement_Editor() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\r\n<Group>\r\n<Editor>Notepad</Editor>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 assertEquals("Notepad", parser.getFile_editor());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\r\n<Group>\r\n<acceptsLineNumber>Yes</acceptsLineNumber>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElement_parameter() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\r\n<Group>\r\n<parameter>123</parameter>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 assertEquals("123", parser.lineNumberParameter());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_invalidTag() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\r\n<Group>\r\n<InvalidTag>123</InvalidTag>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 fail("SAXException expected but not thrown");
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> directory = SAXDirParser.getDirectory();
 assertTrue(directory.isEmpty());
 }

 @Test
 public void testGetDirectory_withData() {
 SAXDirParser parser = new SAXDirParser();
 String xml = "<Web>\r\n<Group>\r\n<pkg>java.lang</pkg>\r\n<location>Java Language</location>\r\n</Group>\r\n<Group>\r\n<pkg>javax.swing.border</pkg>\r\n<location>Swing Border</location>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 Map<String, String> expected = new HashMap<>();
 expected.put("java.lang", "Java Language");
 expected.put("javax.swing.border", "Swing Border");
 Map<String, String> actual = SAXDirParser.getDirectory();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalPackages_empty() {
 List<String> localPackages = SAXDirParser.getLocalPackages();
 assertTrue(localPackages.isEmpty());
 }

 @Test
 public void testGetLocalPackages_withData() {
 SAXDirParser parser = new SAXDirParser();
 String xml = "<Web>\r\n<Group>\r\n<pkg>java.lang</pkg>\r\n<location>Java Language</location>\r\n</Group>\r\n<Group local=\"true\">\r\n<pkg>com.jstevh.viewer</pkg>\r\n<location>Viewer Package</location>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 List<String> expected = new ArrayList<>();
 expected.add("java.lang");
 expected.add("com.jstevh.viewer");
 List<String> actual = SAXDirParser.getLocalPackages();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalBrowser_empty() {
 String localBrowser = SAXDirParser.getLocalBrowser();
 assertEquals("", localBrowser);
 }

 @Test
 public void testGetLocalBrowser_withData() {
 SAXDirParser parser = new SAXDirParser();
 String xml = "<Web>\r\n<Group>\r\n<BrowserLoc local=\"true\">Firefox</BrowserLoc>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 String expected = "Firefox";
 String actual = SAXDirParser.getLocalBrowser();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetEditor_empty() {
 String editor = SAXDirParser.getEditor();
 assertEquals("", editor);
 }

 @Test
 public void testGetEditor_withData() {
 SAXDirParser parser = new SAXDirParser();
 String xml = "<Web>\r\n<Group>\r\n<Editor>Notepad</Editor>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 String expected = "Notepad";
 String actual = SAXDirParser.getEditor();
 assertEquals(expected, actual);
 }

 @Test
 public void testAcceptsLineNumber_false() {
 assertFalse(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testAcceptsLineNumber_true() {
 SAXDirParser parser = new SAXDirParser();
 String xml = "<Web>\r\n<Group>\r\n<acceptsLineNumber>Yes</acceptsLineNumber>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter_empty() {
 String parameter = SAXDirParser.lineNumberParameter();
 assertEquals("", parameter);
 }

 @Test
 public void testLineNumberParameter_withData() {
 SAXDirParser parser = new SAXDirParser();
 String xml = "<Web>\r\n<Group>\r\n<parameter>123</parameter>\r\n</Group>\r\n</Web>";
 parser.startDocument();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new StringReader(xml));
 String expected = "123";
 String actual = SAXDirParser.lineNumberParameter();
 assertEquals(expected, actual);
 }

}