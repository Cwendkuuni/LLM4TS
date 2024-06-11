package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.*;

import org.junit.Test;

public class SAXDirParserTest {

 private static final String XML_FILE = "path/to/your/packagedirectory.xml";

 @Test
 public void testGetDirectory() throws Exception {
  SAXDirParser parser = new SAXDirParser();
  DefaultHandler handler = parser;
  InputSource inputSource = new InputSource(new FileInputStream(XML_FILE));
  XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
  xmlReader.setContentHandler(handler);
  xmlReader.parse(inputSource);

  HashMap<String, String> expectedDirectory = new HashMap<>();
  expectedDirectory.put("java.lang", "testLocation1");
  expectedDirectory.put("javax.swing.border", "testLocation2");

  HashMap<String, String> actualDirectory = parser.getDirectory();

  assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() throws Exception {
  SAXDirParser parser = new SAXDirParser();
  DefaultHandler handler = parser;
  InputSource inputSource = new InputSource(new FileInputStream(XML_FILE));
  XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
  xmlReader.setContentHandler(handler);
  xmlReader.parse(inputSource);

  StringList expectedLocalPackages = new StringList();
  expectedLocalPackages.add("package1");
  expectedLocalPackages.add("package2");

  StringList actualLocalPackages = parser.getLocalPackages();

  assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
  SAXDirParser parser = new SAXDirParser();
  DefaultHandler handler = parser;
  InputSource inputSource = new InputSource(new FileInputStream(XML_FILE));
  XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
  xmlReader.setContentHandler(handler);
  xmlReader.parse(inputSource);

  String expectedLocalBrowser = "testBrowser";

  String actualLocalBrowser = parser.getLocalBrowser();

  assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() throws Exception {
  SAXDirParser parser = new SAXDirParser();
  DefaultHandler handler = parser;
  InputSource inputSource = new InputSource(new FileInputStream(XML_FILE));
  XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
  xmlReader.setContentHandler(handler);
  xmlReader.parse(inputSource);

  String expectedEditor = "testEditor";

  String actualEditor = parser.getEditor();

  assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
  SAXDirParser parser = new SAXDirParser();
  DefaultHandler handler = parser;
  InputSource inputSource = new InputSource(new FileInputStream(XML_FILE));
  XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
  xmlReader.setContentHandler(handler);
  xmlReader.parse(inputSource);

  boolean expectedAcceptsLineNumber = true;

  boolean actualAcceptsLineNumber = parser.acceptsLineNumber();

  assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() throws Exception {
  SAXDirParser parser = new SAXDirParser();
  DefaultHandler handler = parser;
  InputSource inputSource = new InputSource(new FileInputStream(XML_FILE));
  XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
  xmlReader.setContentHandler(handler);
  xmlReader.parse(inputSource);

  String expectedLineNumberParameter = "testParameter";

  String actualLineNumberParameter = parser.lineNumberParameter();

  assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }
}