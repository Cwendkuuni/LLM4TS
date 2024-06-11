package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.junit.Test;

public class SAXDirParserTest {

 private static final String XML_FILE = "testData.xml"; // Replace with the path to your test XML file

 @Test
 public void testGetDirectory() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 SAXDirParser parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource(XML_FILE));
 xmlReader.parse(inputSource);

 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "someLocation1");
 expectedDirectory.put("javax.swing.border", "someLocation2");

 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test(expected = SAXException.class)
 public void testInvalidXMLFile() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 SAXDirParser parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 InputSource inputSource = new InputSource(new StringReader("")); // Empty XML file
 xmlReader.parse(inputSource);
 fail("SAXException should have been thrown for empty XML file.");
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 SAXDirParser parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource(XML_FILE));
 xmlReader.parse(inputSource);

 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");

 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackagesWhenEmpty() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 SAXDirParser parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 InputSource inputSource = new InputSource(new StringReader("")); // Empty XML file
 xmlReader.parse(inputSource);

 StringList actualLocalPackages = parser.getLocalPackages();
 assertNull(actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 SAXDirParser parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource(XML_FILE));
 xmlReader.parse(inputSource);

 String expectedLocalBrowser = "Firefox";
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLocalBrowserWhenNotSet() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 SAXDirParser parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 InputSource inputSource = new InputSource(new StringReader("")); // Empty XML file
 xmlReader.parse(inputSource);

 String actualLocalBrowser = parser.getLocalBrowser();
 fail("IllegalStateException should have been thrown for getLocalBrowser when not set.");
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 SAXDirParser parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource(XML_FILE));
 xmlReader.parse(inputSource);

 boolean expectedAcceptLineNumber = true;
 boolean actualAcceptLineNumber = parser.acceptsLineNumber();
 assertEquals(expectedAcceptLineNumber, actualAcceptLineNumber);
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 SAXDirParser parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource(XML_FILE));
 xmlReader.parse(inputSource);

 String expectedLineNumberParameter = "5";
 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }
}