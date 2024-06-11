package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.XMLInputFactory;
import javax.xml.parsers.XMLParserConfigurationException;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private String webData;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 webData = "";
 }

 @Test
 public void testGetWebData() {
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() throws SAXException {
 parser = new SAXDirParser();
 InputStream is = new ByteArrayInputStream("<XML><BrowserLoc>TestBrowser</BrowserLoc>".getBytes());
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 saxParser.parse(inputFactory.createXMLDocumentParser(is, null).getInputSource(), parser);
 assertEquals("TestBrowser", parser.getWebData());
 }

 @Test
 public void testStartElement_Editor() throws SAXException {
 parser = new SAXDirParser();
 InputStream is = new ByteArrayInputStream("<XML><Editor>TestEditor</Editor>".getBytes());
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 saxParser.parse(inputFactory.createXMLDocumentParser(is, null).getInputSource(), parser);
 assertEquals("TestEditor", parser.getWebData());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws SAXException {
 parser = new SAXDirParser();
 InputStream is = new ByteArrayInputStream("<XML><acceptsLineNumber>Yes</acceptsLineNumber></XML>".getBytes());
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 saxParser.parse(inputFactory.createXMLDocumentParser(is, null).getInputSource(), parser);
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElement_acceptsLineNumber_No() throws SAXException {
 parser = new SAXDirParser();
 InputStream is = new ByteArrayInputStream("<XML><acceptsLineNumber>No</acceptsLineNumber></XML>".getBytes());
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 saxParser.parse(inputFactory.createXMLDocumentParser(is, null).getInputSource(), parser);
 assertFalse(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElement_parameter() throws SAXException {
 parser = new SAXDirParser();
 InputStream is = new ByteArrayInputStream("<XML><parameter>TestParameter</parameter></XML>".getBytes());
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 saxParser.parse(inputFactory.createXMLDocumentParser(is, null).getInputSource(), parser);
 assertEquals("TestParameter", parser.lineNumberParameter());
 }

 @Test
 public void testLocalPackagesAndDirectory() throws ParserConfigurationException, SAXException, IOException, XMLParserConfigurationException {
 parser = new SAXDirParser();
 InputStream is = new ByteArrayInputStream("<XML><Web><Local><pkg>java.lang</pkg></Local><pkg>javax.swing.border</pkg></Web></XML>".getBytes());
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 saxParser.parse(inputFactory.createXMLDocumentParser(is, null).getInputSource(), parser);
 Map<String, String> directory = parser.getDirectory();
 List<String> localPackages = parser.getLocalPackages();
 assertEquals(2, directory.size());
 assertEquals("", directory.get(""));
 assertEquals("java.lang", directory.get("java.lang"));
 assertEquals("javax.swing.border", directory.get("javax.swing.border"));
 assertEquals(2, localPackages.size());
 assertEquals("java.lang", localPackages.get(0));
 assertEquals("javax.swing.border", localPackages.get(1));
 }

 @Test
 public void testLocalPackagesAndDirectory_NoLocal() throws ParserConfigurationException, SAXException, IOException, XMLParserConfigurationException {
 parser = new SAXDirParser();
 InputStream is = new ByteArrayInputStream("<XML><Web><Local><pkg>java.lang</pkg></Local></Web></XML>".getBytes());
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 saxParser.parse(inputFactory.createXMLDocumentParser(is, null).getInputSource(), parser);
 Map<String, String> directory = parser.getDirectory();
 List<String> localPackages = parser.getLocalPackages();
 assertEquals(1, directory.size());
 assertEquals("", directory.get(""));
 assertEquals(0, localPackages.size());
 }

 @Test
 public void testStartElement_Group() throws SAXException {
 parser = new SAXDirParser();
 InputStream is = new ByteArrayInputStream("<XML><Group></Group></XML>".getBytes());
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 saxParser.parse(inputFactory.createXMLDocumentParser(is, null).getInputSource(), parser);
 assertFalse(parser.acceptsLineNumber());
 }
}