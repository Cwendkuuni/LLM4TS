package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SAXDirParserTest {

 private static SAXDirParser saxDirParser;
 private static Map<String, String> expectedDirectory;
 private static List<String> expectedLocalPackages;

 @BeforeClass
 public static void setUp() throws Exception {
 saxDirParser = new SAXDirParser();
 expectedDirectory = new HashMap<String, String>();
 expectedDirectory.put("java.lang", "testLocation1");
 expectedDirectory.put("javax.swing.border", "testLocation2");
 expectedLocalPackages = new LinkedList<String>();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 }

 @Test
 public void testGetDirectory() throws SAXException, Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XmlReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals("directory size should be equal", expectedDirectory.size(), actualDirectory.size());
 for (Map.Entry<String, String> entry : expectedDirectory.entrySet()) {
 String key = entry.getKey();
 String expectedValue = entry.getValue();
 String actualValue = actualDirectory.get(key);
 assertEquals("expected value for key " + key, expectedValue, actualValue);
 }
 }

 @Test
 public void testGetLocalPackages() throws SAXException, Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XmlReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals("local packages size should be equal", expectedLocalPackages.size(), actualLocalPackages.size());
 for (int i = 0; i < expectedLocalPackages.size(); i++) {
 String expectedValue = expectedLocalPackages.get(i);
 String actualValue = actualLocalPackages.get(i);
 assertEquals("expected value for index " + i, expectedValue, actualValue);
 }
 }

 @Test
 public void testGetLocalBrowser() throws SAXException, Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XmlReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals("expected local browser", "", actualLocalBrowser);
 }

 @Test
 public void testGetEditor() throws SAXException, Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XmlReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);
 String actualEditor = SAXDirParser.getEditor();
 assertEquals("expected editor", "", actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() throws SAXException, Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XmlReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertFalse("expected acceptsLineNumber to be false", actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() throws SAXException, Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XmlReader xmlReader = saxFactory.newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertNull("expected lineNumberParameter to be null", actualLineNumberParameter);
 }
}