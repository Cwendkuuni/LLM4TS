package com.jstevh.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Test
 public void testGetDirectory() throws Exception {
 SAXDirParser.directory.clear();
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser = new SAXDirParser());
 myReader.parse("packagedirectory.xml");

 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");

 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackagesIsEmpty() throws Exception {
 SAXDirParser.localPackages.clear();
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser = new SAXDirParser());
 myReader.parse("packagedirectory.xml");

 List<String> expectedLocalPackages = new ArrayList<>();
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertNull(actualLocalPackages);
 }

 @Test
 public void testGetLocalPackagesIsNotEmpty() throws Exception {
 SAXDirParser.localPackages.clear();
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser = new SAXDirParser());
 myReader.parse("packagedirectory.xml");

 List<String> expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 SAXDirParser.localPackages.addAll(expectedLocalPackages);
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertSame(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser = new SAXDirParser());
 myReader.parse("packagedirectory.xml");

 String expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.111 Safari/537.36";
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditorIsEmpty() throws Exception {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser = new SAXDirParser());
 myReader.parse("packagedirectory.xml");

 String expectedEditor = null;
 String actualEditor = SAXDirParser.getEditor();
 assertNull(actualEditor);
 }

 @Test
 public void testGetEditorIsNotEmpty() throws Exception {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser = new SAXDirParser());
 myReader.parse("packagedirectory.xml");

 String expectedEditor = "Notepad++";
 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser = new SAXDirParser());
 myReader.parse("packagedirectory.xml");

 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertTrue(expectedAcceptsLineNumber == actualAcceptsLineNumber);
 }

 @Test
 public void testAcceptsLineNumberIsFalse() throws Exception {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();myReader.setContentHandler(parser = new SAXDirParser());
 myReader.parse("packagedirectory_without_acceptsLineNumber.xml");

 boolean expectedAcceptsLineNumber = false;
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertFalse(expectedAcceptsLineNumber == actualAcceptsLineNumber);
 }

 @Test(expected = AssertionError.class)
 public void testLineNumberParameterIsEmpty() throws Exception {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser = new SAXDirParser());
 myReader.parse("packagedirectory.xml");

 String expectedLineNumberParameter = null;
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertNull(actualLineNumberParameter);
 }

 @Test
 public void testLineNumberParameterIsNotEmpty() throws Exception {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 XMLReader myReader = saxFactory.newSAXParser().getXMLReader();
 myReader.setContentHandler(parser = new SAXDirParser());
 myReader.parse("packagedirectory.xml");

 String expectedLineNumberParameter = "line:123";
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }
}