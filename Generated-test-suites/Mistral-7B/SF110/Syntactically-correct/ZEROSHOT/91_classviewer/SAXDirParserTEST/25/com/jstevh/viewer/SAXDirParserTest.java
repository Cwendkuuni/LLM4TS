package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.jstevh.tools.StringList;
import com.jstevh.viewer.SAXDirParser;

public class SAXDirParserTest {

 private static File xmlFile;

 @BeforeClass
 public static void setup() throws IOException {
 // Replace this with the path to your XML file
 xmlFile = new File("path/to/packagedirectory.xml");
 }

 @Test
 public void testGetDirectory() throws Exception {
 // Testing getDirectory method
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(new SAXDirParser());
 xmlReader.parse(xmlFile);
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "test1");
 expectedDirectory.put("javax.swing.border", "test2");
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 // Testing getLocalPackages method
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(new SAXDirParser());
 xmlReader.parse(xmlFile);
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 // Testing getLocalBrowser method
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(new SAXDirParser());
 xmlReader.parse(xmlFile);
 String expectedLocalBrowser = "firefox";
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() throws Exception {
 // Testing getEditor method
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(new SAXDirParser());
 xmlReader.parse(xmlFile);
 String expectedEditor = "notepad";
 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 // Testing acceptsLineNumber method
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(new SAXDirParser());
 xmlReader.parse(xmlFile);
 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 // Testing lineNumberParameter method
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 xmlReader.setContentHandler(new SAXDirParser());
 xmlReader.parse(xmlFile);
 String expectedLineNumberParameter = "123";
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }
}