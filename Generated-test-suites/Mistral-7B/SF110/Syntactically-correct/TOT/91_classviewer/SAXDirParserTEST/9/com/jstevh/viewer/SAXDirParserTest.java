package com.jstevh.viewer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.XMLParseException;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private HashMap<String, String> expectedDirectory;
 private StringList expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedLineNumAccepted;
 private String expectedLineNumParam;

 @Before
 public void setUp() {
 saxDirParser = new SAXDirParser();
 expectedDirectory = new HashMap<String, String>();
 expectedLocalPackages = new ArrayList<String>();
 expectedLocalBrowser = "";
 expectedEditor = "";
 expectedLineNumAccepted = false;
 expectedLineNumParam = "";
 }

 @Test
 public void testGetDirectory() throws ParserConfigurationException, SAXException, IOException, XMLParseException {
 File xmlFile = new File("src/test/resources/packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);

 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);

 // Test for typical use cases
 expectedDirectory.put("java.lang", "Java Language");
 expectedDirectory.put("javax.swing.border", "Swing Border Packages");
 assertEquals(expectedDirectory.get("java.lang"), saxDirParser.getWebData());
 assertEquals(expectedDirectory.get("javax.swing.border"), saxDirParser.getWebData());

 // Test for edge cases
 expectedDirectory.clear();
 expectedDirectory.put("", "Empty Directory");
 assertEquals(expectedDirectory.get(""), saxDirParser.getWebData());

 // Test for error scenarios
 expectedDirectory.clear();
 expectedDirectory.put("", "null");
 saxDirParser = null;
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testGetLocalPackages() throws ParserConfigurationException, SAXException, IOException, XMLParseException {
 File xmlFile = new File("src/test/resources/packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);

 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);

 // Test for typical use cases
 expectedLocalPackages.add("com.jstevh.tools");
 assertEquals(expectedLocalPackages.get(0), saxDirParser.getWebData());

 // Test for edge cases
 expectedLocalPackages.clear();
 assertEquals(expectedLocalPackages, saxDirParser.getLocalPackages());

 // Test for error scenarios
 expectedLocalPackages.clear();
 expectedLocalPackages.add("NonExistentPackage");
 saxDirParser = new SAXDirParser();
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testGetLocalBrowser() throws ParserConfigurationException, SAXException, IOException, XMLParseException {
 File xmlFile = new File("src/test/resources/packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);

 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);

 // Test for typical use cases
 expectedLocalBrowser = "Google Chrome";
 assertEquals(expectedLocalBrowser, saxDirParser.getWebData());

 // Test for edge cases
 expectedLocalBrowser = "";
 assertEquals(expectedLocalBrowser, saxDirParser.getWebData());

 // Test for error scenarios
 expectedLocalBrowser = null;
 saxDirParser = new SAXDirParser();
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testGetEditor() throws ParserConfigurationException, SAXException, IOException, XMLParseException {
 File xmlFile = new File("src/test/resources/packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);

 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);

 // Test for typical use cases
 expectedEditor = "Notepad++";
 assertEquals(expectedEditor, saxDirParser.getWebData());

 // Test for edge cases
 expectedEditor = "";
 assertEquals(expectedEditor, saxDirParser.getWebData());

 // Test for error scenarios
 expectedEditor = null;
 saxDirParser = new SAXDirParser();
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testAcceptsLineNumber() throws ParserConfigurationException, SAXException, IOException, XMLParseException {
 File xmlFile = new File("src/test/resources/packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);

 boolean actualLineNumAccepted = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedLineNumAccepted, actualLineNumAccepted);

 // Test for typical use cases
 expectedLineNumAccepted = true;
 assertEquals(expectedLineNumAccepted, saxDirParser.acceptsLineNumber());

 // Test for edge cases
 expectedLineNumAccepted = false;
 assertFalse(saxDirParser.acceptsLineNumber());

 // Test for error scenarios
 expectedLineNumAccepted = false;
 saxDirParser = new SAXDirParser();
 assertFalse(saxDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() throws ParserConfigurationException, SAXException, IOException, XMLParseException {
 File xmlFile = new File("src/test/resources/packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);

 String actualLineNumParam = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumParam, actualLineNumParam);

 // Test for typical use cases
 expectedLineNumParam = "123";
 assertEquals(expectedLineNumParam, saxDirParser.getWebData());

 // Test for edge cases
 expectedLineNumParam = "";
 assertEquals(expectedLineNumParam, saxDirParser.getWebData());

 // Test for error scenarios
 expectedLineNumParam = null;
 saxDirParser = new SAXDirParser();
 assertNull(saxDirParser.getWebData());
 }
}