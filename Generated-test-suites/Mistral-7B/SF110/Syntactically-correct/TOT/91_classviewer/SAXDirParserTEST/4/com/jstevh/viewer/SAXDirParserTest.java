package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private String xmlData;

 @Before
 public void setUp() {
 try {
 parser = new SAXDirParser();
 xmlData = "<Groups>\n" +
 " <Group>\n" +
 " <Web>Java.lang</Web>\n" +
 " <Local>true</Local>\n" +
 " <Names>\n" +
 " <pkg>java</pkg>\n" +
 " <pkg>javax.swing.border</pkg>\n" +
 " </Names>\n" +
 " </Group>\n" +
 " <Group>\n" +
 " <Web>javax.swing.border</Web>\n" +
 " <Local>false</Local>\n" +
 " <Names>\n" +
 " <pkg>com.jstevh.viewer</pkg>\n" +
 " </Names>\n" +
 " </Group>\n" +
 " </Groups>";
 } catch (Exception e) {
 e.printStackTrace();
 fail("Initialization failed");
 }
 }

 @Test
 public void testGetDirectory() throws SAXException, IOException {
 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "Java.lang");
 expectedDirectory.put("javax.swing.border", "javax.swing.border");
 expectedDirectory.put("com.jstevh.viewer", "com.jstevh.viewer");

 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser);
 StringReader reader = new StringReader(xmlData);
 InputSource source = new InputSource(reader);
 myReader.parse(source);

 HashMap<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() throws SAXException, IOException {
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("java");
 expectedLocalPackages.add("javax.swing.border");
 expectedLocalPackages.add("com.jstevh.viewer");

 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser);
 StringReader reader = new StringReader(xmlData);
 InputSource source = new InputSource(reader);
 myReader.parse(source);

 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() throws SAXException, IOException {
 String expectedLocalBrowser = "";

 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser);
 StringReader reader = new StringReader(xmlData);
 InputSource source = new InputSource(reader);
 myReader.parse(source);

 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testAcceptsLineNumber() throws SAXException, IOException {
 boolean expectedAcceptsLineNumber = false;
 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() throws SAXException, IOException {
 String expectedLineNumberParameter = "";

 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser);
 StringReader reader = new StringReader(xmlData);
 InputSource source = new InputSource(reader);
 myReader.parse(source);

 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test
 public void testEmptyDirectory() throws SAXException, IOException {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser);
 StringReader reader = new StringReader("");
 InputSource source = new InputSource(reader);
 myReader.parse(source);

 HashMap<String, String> actualDirectory = parser.getDirectory();
 assertNull(actualDirectory);
 }

 @Test
 public void testNullXmlData() throws SAXException, IOException {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser);
 StringReader reader = new StringReader(null);
 InputSource source = new InputSource(reader);
 myReader.parse(source);

 HashMap<String, String> actualDirectory = parser.getDirectory();
 assertNull(actualDirectory);
 }

 @Test(expected = SAXException.class)
 public void testMalformedXmlData() throws SAXException, IOException {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser);
 StringReader reader = new StringReader("<Groups>\n" +
 " <Group>\n" +
 " <Web>Java.lang</Web>\n" +
 " <Local>true</Local>\n" +
 " <Names>\n" +
 " <pkg>java</pkg>\n" +
 " <pkg>javax.swing.border</pkg>\n" +
 " </Names>\n" +
 " </Group>\n" +
 " <Group>\n" +
 " <Web>javax.swing.border</Web>\n" +
 " <Local>false</Local>\n" +
 " <Names>\n" +
 " <pkg>com.jstevh.viewer</pkg>\n" +
 " </Names>\n" +
 " <Web>Invalid</Web>\n" +
 " </Group>\n" +
 " </Groups>");
 StringReader reader2 = new StringReader(xmlData);
 InputSource source = new InputSource(reader);
 myReader.parse(source);
 }
}