package com.jstevh.viewer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private Map<String, String> expectedDirectory;
 private String expectedLocalBrowser;
 private boolean expectedAcceptsLineNumber;
 private String expectedLineNumberParameter;

 @Before
 public void init() {
 parser = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang.xml");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border.xml");
 expectedLocalBrowser = "Firefox";
 expectedAcceptsLineNumber = true;
 expectedLineNumberParameter = "56";
 }

 @Test
 public void testGetDirectory() throws SAXException, IOException {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(parser);
 myReader.parse(xmlFile);

 Map<String, String> actualDirectory = parser.getDirectory();
 Assert.assertEquals("Test failed: incorrect directory mapping", expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 Assert.assertNotNull("Test failed: getLocalPackages returned null", parser.getLocalPackages());
 }

 @Test
 public void testGetLocalBrowser() {
 Assert.assertEquals("Test failed: incorrect local browser", expectedLocalBrowser, parser.getLocalBrowser());
 }

 @Test
 public void testAcceptsLineNumber() {
 Assert.assertEquals("Test failed: incorrect line number acceptance", expectedAcceptsLineNumber, parser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 Assert.assertEquals("Test failed: incorrect line number parameter", expectedLineNumberParameter, parser.lineNumberParameter());
 }

 @Test(expected = RuntimeException.class)
 public void testWrongElementNameInXml() throws SAXException, IOException {
 File xmlFile = new File("packagedirectory_wrong.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(parser);
 myReader.parse(xmlFile);
 }
}