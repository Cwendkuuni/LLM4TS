package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import com.jstevh.tools.StringList;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import java.util.HashMap;
import java.util.Map;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private XMLReader xmlReader;
 private File xmlFile;

 @Before
 public void setUp() throws Exception {
 xmlFile = new File("src/test/resources/packagedirectory_test.xml");
 xmlReader = new SAXParserFactory().newSAXParser().getXMLReader();
 parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 }

 @After
 public void tearDown() {
 parser = null;
 xmlReader = null;
 xmlFile = null;
 }

 @Test
 public void testGetDirectory() throws SAXException, IOException, ParserConfigurationException {
 xmlReader.parse(xmlFile.getAbsolutePath());
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");
 assertEquals(expectedDirectory, SAXDirParser.getDirectory());
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectoryWhenXMLFileIsNotFound() throws SAXException, IOException, ParserConfigurationException {
 xmlReader.parse(new File("src/test/resources/nonexistentfile.xml").getAbsolutePath());
 assertNull(SAXDirParser.getDirectory());
 }

 @Test
 public void testGetLocalPackages() throws SAXException, IOException, ParserConfigurationException {
 xmlReader.parse(xmlFile.getAbsolutePath());
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());
 }

 @Test
 public void testGetLocalPackagesWhenXMLIsEmpty() throws SAXException, IOException, ParserConfigurationException {
 SAXDirParser.directory.clear();
 SAXDirParser.localPackages.clear();
 xmlReader.parse(xmlFile.getAbsolutePath());
 assertNull(SAXDirParser.getLocalPackages());
 }

 @Test
 public void testGetLocalBrowser() throws SAXException, IOException, ParserConfigurationException {
 xmlReader.parse(xmlFile.getAbsolutePath());
 String expectedLocalBrowser = "Firefox";
 assertEquals(expectedLocalBrowser, SAXDirParser.getLocalBrowser());
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalBrowserWhenXMLFileIsNotFound() throws SAXException, IOException, ParserConfigurationException {
 xmlReader.parse(new File("src/test/resources/nonexistentfile.xml").getAbsolutePath());
 assertNull(SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testAcceptsLineNumber() throws SAXException, IOException, ParserConfigurationException {
 xmlReader.parse(xmlFile.getAbsolutePath());
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testAcceptsLineNumberWhenXMLDoesNotContainLineNumberParam() throws SAXException, IOException, ParserConfigurationException {
 SAXDirParser.directory.clear();
 SAXDirParser.localPackages.clear();
 SAXDirParser.lineNumAccepted = false;
 xmlReader.parse(xmlFile.getAbsolutePath());
 assertFalse(SAXDirParser.acceptsLineNumber());
 }

 @Test(expected = NullPointerException.class)
 public void testLineNumberParameterWhenXMLFileIsNotFound() throws SAXException, IOException, ParserConfigurationException {
 xmlReader.parse(new File("src/test/resources/nonexistentfile.xml").getAbsolutePath());
 assertNull(SAXDirParser.lineNumberParameter());
 }

 @Test
 public void testLineNumberParameter() throws SAXException, IOException, ParserConfigurationException {
 xmlReader.parse(xmlFile.getAbsolutePath());
 String expectedLineNumberParam = "lineNumber";
 assertEquals(expectedLineNumberParam, SAXDirParser.lineNumberParameter());
 }
}