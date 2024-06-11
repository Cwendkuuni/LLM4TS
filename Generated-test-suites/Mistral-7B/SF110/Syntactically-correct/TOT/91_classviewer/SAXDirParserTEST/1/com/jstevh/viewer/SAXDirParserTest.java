package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SAXDirParserTest {

 @Test
 public void testGetDirectory() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");

 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetDirectory_EmptyFile() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("emptyfile.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 Map<String, String> actualDirectory = parser.getDirectory();
 assertNull(actualDirectory);
 }

 @Test
 public void testGetDirectory_InvalidXML() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("invalidxml.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 Throwable exception = assertThrows(SAXException.class, () -> parser.getDirectory());
 assertEquals("Content is not allowed in prolog.", exception.getCause().getMessage());
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 List<String> expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("com.jstevh.viewer");
 expectedLocalPackages.add("java.lang");

 List<String> actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalPackages_EmptyFile() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("emptyfile.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 List<String> actualLocalPackages = parser.getLocalPackages();
 assertNull(actualLocalPackages);
 }

 @Test
 public void testGetLocalPackages_InvalidXML() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("invalidxml.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 Throwable exception = assertThrows(SAXException.class, () -> parser.getLocalPackages());
 assertEquals("Content is not allowed in prolog.", exception.getCause().getMessage());
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 String expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36";
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetLocalBrowser_EmptyFile() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("emptyfile.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 String actualLocalBrowser = parser.getLocalBrowser();
 assertNull(actualLocalBrowser);
 }

 @Test
 public void testGetLocalBrowser_InvalidXML() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("invalidxml.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 Throwable exception = assertThrows(SAXException.class, () -> parser.getLocalBrowser());
 assertEquals("Content is not allowed in prolog.", exception.getCause().getMessage());
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testAcceptsLineNumber_EmptyFile() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("emptyfile.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertFalse(actualAcceptsLineNumber);
 }

 @Test
 public void testAcceptsLineNumber_InvalidXML() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("invalidxml.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 Throwable exception = assertThrows(SAXException.class, () -> parser.acceptsLineNumber());
 assertEquals("Content is not allowed in prolog.", exception.getCause().getMessage());
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 String expectedLineNumberParameter = "5";
 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test
 public void testLineNumberParameter_EmptyFile() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("emptyfile.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 String actualLineNumberParameter = parser.lineNumberParameter();
 assertNull(actualLineNumberParameter);
 }

 @Test
 public void testLineNumberParameter_InvalidXML() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 File inputFile = new File("invalidxml.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 xmlReader.parse(inputFile);

 Throwable exception = assertThrows(SAXException.class, () -> parser.lineNumberParameter());
 assertEquals("Content is not allowed in prolog.", exception.getCause().getMessage());
 }
}