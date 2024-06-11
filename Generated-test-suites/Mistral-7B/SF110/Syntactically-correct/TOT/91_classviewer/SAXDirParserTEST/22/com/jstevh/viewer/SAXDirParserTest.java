package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 }

 @Test
 public void testGetDirectory() throws ParserConfigurationException, SAXException, IOException {
 // Test with valid XML file
 File file = new File("path/to/valid/packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(file);

 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");

 HashMap<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);

 // Test with empty XML file
 File emptyFile = new File("path/to/empty/packagedirectory.xml");
 xmlReader.setContentHandler(parser);
 xmlReader.parse(emptyFile);

 HashMap<String, String> emptyDirectory = parser.getDirectory();
 assertNotNull(emptyDirectory);
 assertEquals(0, emptyDirectory.size());
 }

 @Test(expected = SAXException.class)
 public void testStartElementWithUnexpectedTag() throws ParserConfigurationException, SAXException, IOException {
 // Test with XML file containing unexpected tag
 File unexpectedFile = new File("path/to/unexpected/packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(unexpectedFile);
 fail("SAXException expected but not thrown");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDirectoryBeforeParse() {
 parser.getDirectory(); // Should throw IllegalStateException
 }

 @Test
 public void testGetLocalPackages() {
 // Test with valid XML file
 File file = new File("path/to/valid/packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(file);

 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("java.lang");

 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);

 // Test with empty XML file
 File emptyFile = new File("path/to/empty/packagedirectory.xml");
 XMLReader emptyXmlReader = XMLReaderFactory.createXMLReader();
 emptyXmlReader.setContentHandler(parser);
 emptyXmlReader.parse(emptyFile);

 StringList emptyLocalPackages = parser.getLocalPackages();
 assertNotNull(emptyLocalPackages);
 assertEquals(0, emptyLocalPackages.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLocalPackagesBeforeParse() {
 parser.getLocalPackages(); // Should throw IllegalStateException
 }

 @Test
 public void testGetLocalBrowser() {
 // Test with valid XML file
 File file = new File("path/to/valid/packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(file);

 String expectedLocalBrowser = "expectedLocalBrowser";
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);

 // Test with empty XML file
 File emptyFile = new File("path/to/empty/packagedirectory.xml");
 XMLReader emptyXmlReader = XMLReaderFactory.createXMLReader();
 emptyXmlReader.setContentHandler(parser);
 emptyXmlReader.parse(emptyFile);

 String emptyLocalBrowser = parser.getLocalBrowser();
 assertNull(emptyLocalBrowser);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLocalBrowserBeforeParse() {
 parser.getLocalBrowser(); // Should throw IllegalStateException
 }

 @Test
 public void testAcceptsLineNumber() {
 // Test with valid XML file
 File file = new File("path/to/valid/packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(file);

 assertTrue(parser.acceptsLineNumber());

 // Test with empty XML file
 File emptyFile = new File("path/to/empty/packagedirectory.xml");
 XMLReader emptyXmlReader = XMLReaderFactory.createXMLReader();
 emptyXmlReader.setContentHandler(parser);
 emptyXmlReader.parse(emptyFile);

 assertFalse(parser.acceptsLineNumber());
 }

 @Test(expected = IllegalStateException.class)
 public void testAcceptsLineNumberBeforeParse() {
 parser.acceptsLineNumber(); // Should throw IllegalStateException
 }

 @Test
 public void testLineNumberParameter() {
 // Test with valid XML file
 File file = new File("path/to/valid/packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(file);

 String expectedLineNumberParameter = "expectedLineNumberParameter";
 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);

 // Test with empty XML file
 File emptyFile = new File("path/to/empty/packagedirectory.xml");
 XMLReader emptyXmlReader = XMLReaderFactory.createXMLReader();
 emptyXmlReader.setContentHandler(parser);
 emptyXmlReader.parse(emptyFile);

 assertNull(parser.lineNumberParameter());
 }

 @Test(expected = IllegalStateException.class)
 public void testLineNumberParameterBeforeParse() {
 parser.lineNumberParameter(); // Should throw IllegalStateException
 }
}