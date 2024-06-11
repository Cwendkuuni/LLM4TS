package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private File xmlFile;

 @Before
 public void setUp() throws Exception {
 xmlFile = new File("path/to/packagedirectory.xml");
 parser = new SAXDirParser();
 }

 @Test
 public void testGetDirectory() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(xmlFile);

 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");

 HashMap<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetDirectory_EmptyXMLFile() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
 parser = new SAXDirParser();
 XMLInputFactory inputFactory = SAXParserFactory.newInstance().newXMLInputFactory();
 InputSource inputSource = new InputSource(new StringReader(""));
 XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(inputSource);
 parser.setInput(xmlReader);
 parser.parse();

 HashMap<String, String> actualDirectory = parser.getDirectory();
 assertNull(actualDirectory);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectory_NullXMLFile() {
 parser = new SAXDirParser();
 parser.setInput(null);
 parser.parse();
 parser.getDirectory();
 }

 @Test
 public void testGetLocalPackages() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(xmlFile);

 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");

 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalPackages_EmptyXMLFile() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
 parser = new SAXDirParser();
 XMLInputFactory inputFactory = SAXParserFactory.newInstance().newXMLInputFactory();
 InputSource inputSource = new InputSource(new StringReader(""));
 XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(inputSource);
 parser.setInput(xmlReader);
 parser.parse();

 StringList actualLocalPackages = parser.getLocalPackages();
 assertNull(actualLocalPackages);
 }

 @Test
 public void testGetLocalPackages_NullXMLFile() {
 parser = new SAXDirParser();
 parser.setInput(null);
 parser.parse();
 parser.getLocalPackages();
 }

 @Test
 public void testGetLocalBrowser() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(xmlFile);

 String expectedLocalBrowser = "Firefox";

 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetLocalBrowser_EmptyXMLFile() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
 parser = new SAXDirParser();
 XMLInputFactory inputFactory = SAXParserFactory.newInstance().newXMLInputFactory();
 InputSource inputSource = new InputSource(new StringReader(""));
 XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(inputSource);
 parser.setInput(xmlReader);
 parser.parse();

 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals("", actualLocalBrowser);
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalBrowser_NullXMLFile() {
 parser = new SAXDirParser();
 parser.setInput(null);
 parser.parse();
 parser.getLocalBrowser();
 }

 @Test
 public void testAcceptsLineNumber() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(xmlFile);

 boolean expectedAcceptsLineNumber = true;

 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testAcceptsLineNumber_EmptyXMLFile() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
 parser = new SAXDirParser();
 XMLInputFactory inputFactory = SAXParserFactory.newInstance().newXMLInputFactory();
 InputSource inputSource = new InputSource(new StringReader(""));
 XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(inputSource);
 parser.setInput(xmlReader);
 parser.parse();

 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertFalse(actualAcceptsLineNumber);
 }

 @Test(expected = NullPointerException.class)
 public void testAcceptsLineNumber_NullXMLFile() {
 parser = new SAXDirParser();
 parser.setInput(null);
 parser.parse();
 parser.acceptsLineNumber();
 }

 @Test
 public void testLineNumberParameter() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(xmlFile);

 String expectedLineNumberParameter = "5";

 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test
 public void testLineNumberParameter_EmptyXMLFile() throws ParserConfigurationException, SAXException, IOException, XMLStreamException {
 parser = new SAXDirParser();
 XMLInputFactory inputFactory = SAXParserFactory.newInstance().newXMLInputFactory();
 InputSource inputSource = new InputSource(new StringReader(""));
 XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(inputSource);
 parser.setInput(xmlReader);
 parser.parse();

 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals("", actualLineNumberParameter);
 }

 @Test(expected = NullPointerException.class)
 public void testLineNumberParameter_NullXMLFile() {
 parser = new SAXDirParser();
 parser.setInput(null);
 parser.parse();
 parser.lineNumberParameter();
 }

}