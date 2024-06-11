package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXParseException;

public class SAXDirParserTest {

 private static final String VALID_XML = "valid_packageddirectory.xml";
 private static final String INVALID_XML_MISSING_GROUP = "invalid_packageddirectory_missing_group.xml";
 private static final String INVALID_XML_MISSING_ELEMENT = "invalid_packageddirectory_missing_element.xml";

 @Test
 public void testGetDirectory() throws ParserConfigurationException, SAXException, IOException {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser parser = factory.newSAXParser();
 XMLReader xmlReader = parser.getXMLReader();
 SAXDirParser handler = new SAXDirParser();
 xmlReader.setContentHandler(handler);

 File file = new File(VALID_XML);
 xmlReader.parse(file);

 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");

 Map<String, String> actualDirectory = handler.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test(expected = SAXParseException.class)
 public void testGetDirectoryInvalidXmlMissingGroup() throws ParserConfigurationException, SAXException, IOException {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser parser = factory.newSAXParser();
 XMLReader xmlReader = parser.getXMLReader();
 SAXDirParser handler = new SAXDirParser();
 xmlReader.setContentHandler(handler);

 File file = new File(INVALID_XML_MISSING_GROUP);
 xmlReader.parse(file);

 assertFalse(handler.getDirectory().isEmpty());
 }

 @Test(expected = SAXParseException.class)
 public void testGetDirectoryInvalidXmlMissingElement() throws ParserConfigurationException, SAXException, IOException {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser parser = factory.newSAXParser();
 XMLReader xmlReader = parser.getXMLReader();
 SAXDirParser handler = new SAXDirParser();
 xmlReader.setContentHandler(handler);

 File file = new File(INVALID_XML_MISSING_ELEMENT);
 xmlReader.parse(file);

 assertFalse(handler.getDirectory().isEmpty());
 }
}