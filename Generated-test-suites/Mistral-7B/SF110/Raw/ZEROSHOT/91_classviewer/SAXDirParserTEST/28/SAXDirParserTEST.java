package com.jstevh.viewer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.jstevh.tools.StringList;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Test
 public void testGetDirectory() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 File file = new File("path/to/packagedirectory.xml");
 xmlReader.parse(file);

 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "expectedLocation1");
 expectedDirectory.put("javax.swing.border", "expectedLocation2");

 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals("test getDirectory, test fails", expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 File file = new File("path/to/packagedirectory.xml");
 xmlReader.parse(file);

 List<String> expectedLocalPackages = List.of("package1", "package2");
 List<String> actualLocalPackages = parser.getLocalPackages();
 assertEquals("test getLocalPackages, test fails", expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 File file = new File("path/to/packagedirectory.xml");
 xmlReader.parse(file);

 String expectedLocalBrowser = "expectedBrowser";
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals("test getLocalBrowser, test fails", expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 File file = new File("path/to/packagedirectory.xml");
 xmlReader.parse(file);

 String expectedEditor = "expectedEditor";
 String actualEditor = parser.getEditor();
 assertEquals("test getEditor, test fails", expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 File file = new File("path/to/packagedirectory.xml");
 xmlReader.parse(file);

 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals("test acceptsLineNumber, test fails", expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 File file = new File("path/to/packagedirectory.xml");
 xmlReader.parse(file);

 String expectedLineNumberParameter = "expectedParameter";
 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals("test lineNumberParameter, test fails", expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test(expected = SAXException.class)
 public void testExceptionThrown() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader xmlReader = saxParser.getXMLReader();
 parser = new SAXDirParser();
 xmlReader.setContentHandler(parser);
 // Replace this with a valid XML file path
 File file = new File("invalidXMLFile.xml");
 xmlReader.parse(file);
 }
}