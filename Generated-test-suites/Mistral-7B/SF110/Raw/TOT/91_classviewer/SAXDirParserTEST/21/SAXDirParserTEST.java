package com.jstevh.viewer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.xml.sax.SAXException;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private final String xmlFilePath = "src/test/resources/packagedirectory_test.xml";

 @Test
 public void testGetDirectory() throws Exception {
  parser = new SAXDirParser();
  SAXParserFactory saxFactory = SAXParserFactory.newInstance();
  SAXParser myParser = saxFactory.newSAXParser();
  XMLReader myReader = myParser.getXMLReader();
  myReader.setContentHandler(parser);
  myReader.parse(new File(xmlFilePath));

  Map<String, String> expectedDirectory = new HashMap<>();
  expectedDirectory.put("java.lang", "path/to/java_lang");
  expectedDirectory.put("javax.swing.border", "path/to/javax_swing_border");

  Map<String, String> actualDirectory = parser.getDirectory();
  assertEquals("test directory, test gives wrong value for java.lang", expectedDirectory.get("java.lang"), actualDirectory.get("java.lang"));
  assertEquals("test directory, test gives wrong value for javax.swing.border", expectedDirectory.get("javax.swing.border"), actualDirectory.get("javax.swing.border"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectory_whenInputFileIsNull() {
  parser = new SAXDirParser();
  assertNull("test directory, test should return null when input file is null", parser.getDirectory());
 }

 @Test
 public void testGetLocalPackages() throws Exception {
  parser = new SAXDirParser();
  SAXParserFactory saxFactory = SAXParserFactory.newInstance();
  SAXParser myParser = saxFactory.newSAXParser();
  XMLReader myReader = myParser.getXMLReader();
  myReader.setContentHandler(parser);
  myReader.parse(new File(xmlFilePath));

  ArrayList<String> expectedLocalPackages = new ArrayList<>();
  expectedLocalPackages.add("package1");
  expectedLocalPackages.add("package2");

  ArrayList<String> actualLocalPackages = parser.getLocalPackages();
  assertEquals("test local packages, test gives wrong number of packages", expectedLocalPackages.size(), actualLocalPackages.size());
  for (int i = 0; i < expectedLocalPackages.size(); ++i) {
   assertTrue("test local packages, test gives wrong package name", expectedLocalPackages.get(i).equals(actualLocalPackages.get(i)));
  }
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackages_whenDirectoryIsNull() {
  parser = new SAXDirParser();
  assertNull("test local packages, test should return null when directory is null", parser.getLocalPackages());
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
  parser = new SAXDirParser();
  SAXParserFactory saxFactory = SAXParserFactory.newInstance();
  SAXParser myParser = saxFactory.newSAXParser();
  XMLReader myReader = myParser.getXMLReader();
  myReader.setContentHandler(parser);
  myReader.parse(new File(xmlFilePath));

  String expectedLocalBrowser = "Firefox";

  String actualLocalBrowser = parser.getLocalBrowser();
  assertEquals("test local browser, test gives wrong value", expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() throws Exception {
  parser = new SAXDirParser();
  SAXParserFactory saxFactory = SAXParserFactory.newInstance();
  SAXParser myParser = saxFactory.newSAXParser();
  XMLReader myReader = myParser.getXMLReader();
  myReader.setContentHandler(parser);
  myReader.parse(new File(xmlFilePath));

  String expectedEditor = "Notepad";

  String actualEditor = parser.getEditor();
  assertEquals("test editor, test gives wrong value", expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
  parser = new SAXDirParser();
  SAXParserFactory saxFactory = SAXParserFactory.newInstance();
  SAXParser myParser = saxFactory.newSAXParser();
  XMLReader myReader = myParser.getXMLReader();
  myReader.setContentHandler(parser);
  myReader.parse(new File(xmlFilePath));

  boolean expectedAcceptLineNumber = true;
  assertTrue("test accepts line number, test gives wrong value", expectedAcceptLineNumber == parser.acceptsLineNumber());

  parser = new SAXDirParser();
  assertFalse("test accepts line number, test gives wrong value", parser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() throws Exception {
  parser = new SAXDirParser();
  SAXParserFactory saxFactory = SAXParserFactory.newInstance();
  SAXParser myParser = saxFactory.newSAXParser();
  XMLReader myReader = myParser.getXMLReader();
  myReader.setContentHandler(parser);
  myReader.parse(new File(xmlFilePath));

  String expectedLineNumberParameter = "lineNumberParam";

  String actualLineNumberParameter = parser.lineNumberParameter();
  assertEquals("test line number parameter, test gives wrong value", expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test
 public void testMain_whenInputFileIsInvalid() throws IOException, SAXException {
  SAXParserFactory saxFactory = SAXParserFactory.newInstance();
  SAXParser myParser = saxFactory.newSAXParser();
  XMLReader myReader = myParser.getXMLReader();
  myReader.setContentHandler(parser);

  assertFalse("test main, test should not call system out print when input file is invalid", parser.main(new String[]{"src/test/resources/invalid_packagedirectory.xml"}));
 }
}