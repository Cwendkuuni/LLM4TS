package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXParseException;
import org.junit.Test;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private XMLReader reader;

 @Test
 public void testGetWebData() {
 SAXDirParser parser = new SAXDirParser();
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() throws SAXException {
 parser = new SAXDirParser();
 reader = new DefaultSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputStream inputStream = new File("testData.xml").getInputStream();
 reader.parse(inputStream);
 assertTrue(parser.check);
 assertEquals(0, parser.level);
 }

 @Test(expected = SAXParseException.class)
 public void testStartElement_InvalidElement() throws SAXException {
 parser = new SAXDirParser();
 reader = new DefaultSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputStream inputStream = new File("testData.xml").getInputStream();
 reader.parse(inputStream);
 assertFalse(parser.check);
 }

 @Test
 public void testCharacters_Editor() throws SAXException {
 parser = new SAXDirParser();
 reader = new DefaultSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputStream inputStream = new File("testData.xml").getInputStream();
 reader.parse(inputStream);
 assertTrue(parser.check);
 assertEquals(10, parser.level);
 }

 @Test
 public void testCharacters_acceptsLineNumber() throws SAXException {
 parser = new SAXDirParser();
 reader = new DefaultSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputStream inputStream = new File("testData.xml").getInputStream();
 reader.parse(inputStream);
 assertFalse(parser.lineNumAccepted);

 parser = new SAXDirParser();
 reader = new DefaultSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputStream inputStream2 = new File("testDataWithYes.xml").getInputStream();
 reader.parse(inputStream2);
 assertTrue(parser.lineNumAccepted);
 }

 @Test
 public void testCharacters_lineNumberParameter() throws SAXException {
 parser = new SAXDirParser();
 reader = new DefaultSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputStream inputStream = new File("testDataWithParameter.xml").getInputStream();
 reader.parse(inputStream);
 assertEquals("lineNum", parser.lineNumParam);
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");

 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 List<String> expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");

 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 assertEquals("", SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testAcceptsLineNumber() {
 assertEquals(false, SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 assertEquals("", SAXDirParser.lineNumberParameter());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectory_EmptyDirectory() {
 SAXDirParser.getDirectory();
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackages_EmptyLocalPackages() {
 SAXDirParser.getLocalPackages();
 }

 @Test(expected = IllegalStateException.class)
 public void testStartElement_UnevenStructure() throws SAXException {
 parser = new SAXDirParser();
 reader = new DefaultSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputStream inputStream = new File("testDataUneven.xml").getInputStream();
 reader.parse(inputStream);
 fail("Unexpected state: parser.check is " + parser.check);
 }
}