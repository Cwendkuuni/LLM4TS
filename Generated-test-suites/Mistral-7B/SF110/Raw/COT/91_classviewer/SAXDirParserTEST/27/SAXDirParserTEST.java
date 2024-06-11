package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringList;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.jstevh.viewer.SAXDirParser;
import com.jstevh.viewer.SAXParserFactory;
import com.jstevh.viewer.StringList;

public class SAXDirParserTest {

 // Test all getter methods
 @Test
 public void testGetters() {
  SAXDirParser parser = new SAXDirParser();
  HashMap<String, String> directory = SAXDirParser.getDirectory();
  StringList localPackages = SAXDirParser.getLocalPackages();
  String localBrowser = SAXDirParser.getLocalBrowser();
  String editor = SAXDirParser.getEditor();
  boolean acceptsLineNumber = SAXDirParser.acceptsLineNumber();
  String lineNumberParameter = SAXDirParser.lineNumberParameter();

  assertEquals("", parser.getWebData());

  assertEquals(directory, parser.getDirectory());
  assertEquals(localPackages, parser.getLocalPackages());
  assertEquals(localBrowser, parser.getLocalBrowser());
  assertEquals(editor, parser.getEditor());
  assertEquals(acceptsLineNumber, parser.acceptsLineNumber());
  assertEquals(lineNumberParameter, parser.lineNumberParameter());
 }

 // Test startElement method with valid elements
 @Test
 public void testStartElementValid() throws SAXException {
  SAXDirParser parser = new SAXDirParser();
  StringReader reader = new StringReader("<Web><Local><acceptsLineNumber>Yes</acceptsLineNumber><parameter>10</parameter></Local></Web>");
  XMLReader xmlReader = XMLReaderFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(new InputSource(new StringReader(reader.toString())));

  assertTrue(parser.isCheck());
  assertEquals(0, parser.getLevel());
  assertEquals("Web", parser.getLocation());
 }

 // Test startElement method with invalid elements
 @Test(expected = SAXException.class)
 public void testStartElementInvalid() throws SAXException {
  SAXDirParser parser = new SAXDirParser();
  StringReader reader = new StringReader("<Web><Editor>Test</Editor></Web>");
  XMLReader xmlReader = XMLReaderFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(new InputSource(new StringReader(reader.toString())));

  assertFalse(parser.isCheck());
 }

 // Test characters method with valid data
 @Test
 public void testCharactersValid() throws SAXException {
  SAXDirParser parser = new SAXDirParser();
  StringReader reader = new StringReader("<Web><Local><acceptsLineNumber>Yes</acceptsLineNumber><parameter>10</parameter></Local></Web>");
  XMLReader xmlReader = XMLReaderFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(new InputSource(new StringReader(reader.toString())));

  assertTrue(parser.isCheck());
  assertEquals(true, parser.isLineNumAccepted());
  assertEquals("10", parser.getLineNumParam());
  assertEquals(30, parser.getLevel());
  assertEquals("Web", parser.getLocation());
 }

 // Test characters method with invalid data
 @Test(expected = SAXException.class)
 public void testCharactersInvalid() throws SAXException {
  SAXDirParser parser = new SAXDirParser();
  StringReader reader = new StringReader("<Web><Local><acceptsLineNumber>Yes</acceptsLineNumber><parameter>Test</parameter></Local></Web>");
  XMLReader xmlReader = XMLReaderFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(new InputSource(new StringReader(reader.toString())));

  assertFalse(parser.isCheck());
 }

 // Test getDirectory method with empty XML
 @Test
 public void testGetDirectoryEmptyXML() {
  SAXDirParser parser = new SAXDirParser();
  HashMap<String, String> expected = new HashMap<>();
  HashMap<String, String> result = SAXDirParser.getDirectory();

  assertEquals(expected, result);
 }

 // Test getDirectory method with valid XML
 @Test
 public void testGetDirectoryValidXML() {
  SAXDirParser parser = new SAXDirParser();
  String xml = "<Web><Local package=\"java.lang\">java.lang</Local><Local package=\"javax.swing.border\">javax.swing.border</Local></Web>";
  SAXParserFactory factory = SAXParserFactory.newInstance();
  XMLInputFactory inputFactory = factory.newXMLInputFactory();
  InputSource inputSource = new InputSource(new StringReader(xml));
  XMLReader xmlReader = inputFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(inputSource);

  HashMap<String, String> expected = new HashMap<>();
  expected.put("java.lang", "java.lang");
  expected.put("javax.swing.border", "javax.swing.border");

  HashMap<String, String> result = SAXDirParser.getDirectory();

  assertEquals(expected, result);
 }

 // Test getLocalPackages method with empty XML
 @Test
 public void testGetLocalPackagesEmptyXML() {
  SAXDirParser parser = new SAXDirParser();
  StringList expected = new StringList();
  StringList result = SAXDirParser.getLocalPackages();

  assertEquals(expected, result);
 }

 // Test getLocalPackages method with valid XML
 @Test
 public void testGetLocalPackagesValidXML() {
  SAXDirParser parser = new SAXDirParser();
  String xml = "<Web><Local package=\"java.lang\"/><Local package=\"javax.swing.border\"/></Web>";
  SAXParserFactory factory = SAXParserFactory.newInstance();
  XMLInputFactory inputFactory = factory.newXMLInputFactory();
  InputSource inputSource = new InputSource(new StringReader(xml));
  XMLReader xmlReader = inputFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(inputSource);

  StringList expected = new StringList();
  expected.add("java.lang");
  expected.add("javax.swing.border");

  StringList result = SAXDirParser.getLocalPackages();

  assertEquals(expected, result);
 }

 // Test getLocalBrowser method with empty XML
 @Test
 public void testGetLocalBrowserEmptyXML() {
  SAXDirParser parser = new SAXDirParser();
  String expected = "";
  String result = SAXDirParser.getLocalBrowser();

  assertEquals(expected, result);
 }

 // Test getLocalBrowser method with valid XML
 @Test
 public void testGetLocalBrowserValidXML() {
  SAXDirParser parser = new SAXDirParser();
  String xml = "<Web><Local acceptsLineNumber=\"Yes\"><parameter>Firefox</parameter></Local></Web>";
  SAXParserFactory factory = SAXParserFactory.newInstance();
  XMLInputFactory inputFactory = factory.newXMLInputFactory();
  InputSource inputSource = new InputSource(new StringReader(xml));
  XMLReader xmlReader = inputFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(inputSource);

  String expected = "Firefox";
  String result = SAXDirParser.getLocalBrowser();

  assertEquals(expected, result);
 }

 // Test acceptsLineNumber method with empty XML
 @Test
 public void testAcceptsLineNumberEmptyXML() {
  SAXDirParser parser = new SAXDirParser();
  boolean expected = false;
  boolean result = parser.acceptsLineNumber();

  assertEquals(expected, result);
 }

 // Test acceptsLineNumber method with valid XML
 @Test
 public void testAcceptsLineNumberValidXML() {
  SAXDirParser parser = new SAXDirParser();
  String xml = "<Web><Local acceptsLineNumber=\"Yes\"/></Web>";
  SAXParserFactory factory = SAXParserFactory.newInstance();
  XMLInputFactory inputFactory = factory.newXMLInputFactory();
  InputSource inputSource = new InputSource(new StringReader(xml));
  XMLReader xmlReader = inputFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(inputSource);

  boolean expected = true;
  boolean result = parser.acceptsLineNumber();

  assertEquals(expected, result);
 }

 // Test lineNumberParameter method with empty XML
 @Test
 public void testLineNumberParameterEmptyXML() {
  SAXDirParser parser = new SAXDirParser();
  String expected = "";
  String result = parser.lineNumberParameter();

  assertEquals(expected, result);
 }

 // Test lineNumberParameter method with valid XML
 @Test
 public void testLineNumberParameterValidXML() {
  SAXDirParser parser = new SAXDirParser();
  String xml = "<Web><Local acceptsLineNumber=\"Yes\"><parameter>123</parameter></Local></Web>";
  SAXParserFactory factory = SAXParserFactory.newInstance();
  XMLInputFactory inputFactory = factory.newXMLInputFactory();
  InputSource inputSource = new InputSource(new StringReader(xml));
  XMLReader xmlReader = inputFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(inputSource);

  String expected = "123";
  String result = parser.lineNumberParameter();

  assertEquals(expected, result);
 }

 // Test main method with valid XML
 @Test
 public void testMainValidXML() throws Exception {
  SAXDirParser parser = new SAXDirParser();
  String xml = "<Web><Local package=\"java.lang\"><acceptsLineNumber>Yes</acceptsLineNumber><parameter>123</parameter></Local></Web>";
  SAXParserFactory factory = SAXParserFactory.newInstance();
  XMLInputFactory inputFactory = factory.newXMLInputFactory();
  InputSource inputSource = new InputSource(new StringReader(xml));
  XMLReader xmlReader = inputFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(inputSource);

  HashMap<String, String> directory = SAXDirParser.getDirectory();
  StringList localPackages = SAXDirParser.getLocalPackages();
  String localBrowser = SAXDirParser.getLocalBrowser();
  String editor = SAXDirParser.getEditor();
  boolean acceptsLineNumber = SAXDirParser.acceptsLineNumber();
  String lineNumberParameter = SAXDirParser.lineNumberParameter();

  assertEquals(directory, parser.getDirectory());
  assertEquals(localPackages, parser.getLocalPackages());
  assertEquals(localBrowser, parser.getLocalBrowser());
  assertEquals(editor, parser.getEditor());
  assertEquals(acceptsLineNumber, parser.acceptsLineNumber());
  assertEquals(lineNumberParameter, parser.lineNumberParameter());

  System.out.println("test directory, test gives " + directory.get("java.lang"));
  System.out.println("test directory, test gives " + directory.get("javax.swing.border"));
  System.out.println("test local browser, test gives " + localBrowser);
 }

 // Test main method with invalid XML
 @Test(expected = SAXException.class)
 public void testMainInvalidXML() throws Exception {
  SAXDirParser parser = new SAXDirParser();
  String xml = "<Web><Local package=\"java.lang\"><acceptsLineNumber>Yes</acceptsLineNumber><parameter>123</parameter></Local></Web>";
  SAXParserFactory factory = SAXParserFactory.newInstance();
  XMLInputFactory inputFactory = factory.newXMLInputFactory();
  InputSource inputSource = new InputSource(new File("invalidXML.xml"));
  XMLReader xmlReader = inputFactory.createXMLReader();
  xmlReader.setContentHandler(parser);
  xmlReader.parse(inputSource);
 }
}