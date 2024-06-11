package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import com.jstevh.viewer.SAXDirParser;
import com.jstevh.tools.StringList;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Test
 public void testGetWebData() {
 parser = new SAXDirParser();
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElementBrowserLoc() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
           "<BrowserLoc>Firefox</BrowserLoc>\n" +
           "</Web>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertEquals("Firefox", parser.getWebData());
 }

 @Test
 public void testStartElementEditor() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
           "<Editor>Notepad</Editor>\n" +
           "</Web>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertEquals("Notepad", parser.getFileEditor());
 }

 @Test
 public void testStartElementAcceptsLineNumber() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
           "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
           "</Web>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElementLineNumberParameter() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
           "<acceptsLineNumber>No</acceptsLineNumber>\n" +
           "<parameter>123</parameter>\n" +
           "</Web>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertEquals("123", parser.lineNumberParameter());
 }

 @Test
 public void testStartElementLocal() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
           "<Local>true</Local>\n" +
           "</Web>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertTrue(parser.isLocal());
 }

 @Test
 public void testStartElementNames() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
           "<Names>\n" +
           "<pkg>java.lang</pkg>\n" +
           "<pkg>javax.swing.border</pkg>\n" +
           "</Names>\n" +
           "</Web>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertEquals(2, parser.getDirectory().size());
 }

 @Test(expected = SAXException.class)
 public void testStartElementLocalWithEmptyPackageName() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
           "<Names>\n" +
           "<pkg></pkg>\n" +
           "</Names>\n" +
           "</Web>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 fail("Expected SAXException but didn't get one.");
 }

 @Test(expected = SAXException.class)
 public void testStartElementLocalWithEmptyLocation() throws SAXException {
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
           "<Names>\n" +
           "<pkg>java.lang</pkg>\n" +
           "</Names>\n" +
           "<Local>\n" +
           "</Local>\n" +
           "</Web>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 fail("Expected SAXException but didn't get one.");
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "testLocation1");
 expectedDirectory.put("javax.swing.border", "testLocation2");
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
           "<Names>\n" +
           "<pkg>java.lang</pkg>\n" +
           "<pkg>javax.swing.border</pkg>\n" +
           "</Names>\n" +
           "<Local>\n" +
           "<pkg>java.lang</pkg>\n" +
           "<location>testLocation1</location>\n" +
           "</Local>\n" +
           "<Local>\n" +
           "<pkg>javax.swing.border</pkg>\n" +
           "<location>testLocation2</location>\n" +
           "</Local>\n" +
           "</Web>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertEquals(expectedDirectory, parser.getDirectory());
 }

 @Test
 public void testGetLocalPackages() {
 List<String> expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("java.lang");
 parser = new SAXDirParser();
 String xml = "<Web>\n" +
           "<Names>\n" +
           "<pkg>java.lang</pkg>\n" +
           "</Names>\n" +
           "<Local>\n" +
           "<pkg>java.lang</pkg>\n" +
           "</Local>\n" +
           "</Web>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 List<String> actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalPackagesEmpty() {
 List<String> expectedLocalPackages = new ArrayList<>();
 parser = new SAXDirParser();
 InputSource inputSource = new InputSource(new StringReader("<Web><Names/><Local/></Web>"));
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 List<String> actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

}