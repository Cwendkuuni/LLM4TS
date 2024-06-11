package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private HashMap<String, String> expectedDirectory;
 private String expectedLocalBrowser;
 private boolean expectedAcceptsLineNumber;
 private String expectedLineNumberParameter;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");
 expectedLocalBrowser = "firefox";
 expectedAcceptsLineNumber = true;
 expectedLineNumberParameter = "5";
 }

 @Test
 public void testParseXML() throws IOException, SAXException {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(xmlFile);

 // Test getDirectory()
 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals("Size of expected and actual directory maps should be equal", expectedDirectory.size(), actualDirectory.size());
 for (Map.Entry<String, String> entry : expectedDirectory.entrySet()) {
 String key = entry.getKey();
 String expectedValue = entry.getValue();
 String actualValue = actualDirectory.get(key);
 assertEquals("Expected and actual values for key [" + key + "] should be equal", expectedValue, actualValue);
 }

 // Test getLocalPackages()
 List<String> actualLocalPackages = parser.getLocalPackages();
 assertEquals("Expected and actual local packages lists should be equal", expectedLocalPackages.size(), actualLocalPackages.size());
 for (int i = 0; i < expectedLocalPackages.size(); i++) {
 String expectedPackage = expectedLocalPackages.get(i);
 String actualPackage = actualLocalPackages.get(i);
 assertEquals("Expected and actual local packages at index [" + i + "] should be equal", expectedPackage, actualPackage);
 }

 // Test getLocalBrowser()
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals("Expected and actual local browser should be equal", expectedLocalBrowser, actualLocalBrowser);

 // Test acceptsLineNumber()
 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals("Expected and actual acceptsLineNumber should be equal", expectedAcceptsLineNumber, actualAcceptsLineNumber);

 // Test lineNumberParameter()
 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals("Expected and actual lineNumberParameter should be equal", expectedLineNumberParameter, actualLineNumberParameter);
 }
}