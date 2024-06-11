package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import com.jstevh.viewer.SAXDirParser;
import com.jstevh.tools.StringList;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Test
 public void testGetWebData() {
 SAXDirParser parser = new SAXDirParser();
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() throws SAXException {
 parser = new SAXDirParser();
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource("/testFile.xml"));
 reader.parse(inputSource);
 assertTrue(parser.isCheck());
 assertEquals(0, parser.getLevel());
 }

 @Test
 public void testStartElement_Editor() throws SAXException {
 parser = new SAXDirParser();
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource("/testFile.xml"));
 reader.parse(inputSource);
 assertTrue(parser.isCheck());
 assertEquals(10, parser.getLevel());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws SAXException {
 parser = new SAXDirParser();
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource("/testFile.xml"));
 reader.parse(inputSource);
 assertEquals("Yes", parser.isLineNumAccepted() ? "Yes" : "No");
 }

 @Test
 public void testStartElement_parameter() throws SAXException {
 parser = new SAXDirParser();
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource("/testFile.xml"));
 reader.parse(inputSource);
 assertEquals("parameterValue", parser.lineNumberParameter());
 }

 @Test
 public void testStartElement_Group() throws SAXException {
 parser = new SAXDirParser();
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource("/testFile.xml"));
 reader.parse(inputSource);
 assertFalse(parser.isCheck());
 }

 @Test
 public void testStartElement_Web() throws SAXException {
 parser = new SAXDirParser();
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource("/testFile.xml"));
 reader.parse(inputSource);
 assertTrue(parser.isCheck());
 assertEquals(30, parser.getLevel());
 }

 @Test
 public void testStartElement_Local() throws SAXException {
 parser = new SAXDirParser();
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource("/testFile.xml"));
 reader.parse(inputSource);
 assertTrue(parser.isCheck());
 assertEquals(31, parser.getLevel());
 }

 @Test
 public void testStartElement_Names() throws SAXException {
 parser = new SAXDirParser();
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource("/testFile.xml"));
 reader.parse(inputSource);
 assertFalse(parser.isCheck());
 }

 @Test
 public void testStartElement_pkg() throws SAXException {
 parser = new SAXDirParser();
 XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 InputSource inputSource = new InputSource(SAXDirParserTest.class.getResource("/testFile.xml"));
 reader.parse(inputSource);
 assertEquals("pkgName", parser.getDirectory().get("pkgName"));
 }

 @Test(expected = SAXException.class)
 public void testCharacters_invalidInput() throws SAXException {
 parser = new SAXDirParser();
 parser.characters(new char[]{'A'}, 0, 1);
 fail("Should throw SAXException");
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "locationJavaLang");
 expectedDirectory.put("javax.swing.border", "locationJSwingBorder");
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 List<String> expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("localPackage1");
 expectedLocalPackages.add("localPackage2");
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 assertEquals("localBrowserValue", SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() {
 assertEquals("editorValue", SAXDirParser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() {
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 assertEquals("lineNumberParameterValue", SAXDirParser.lineNumberParameter());
 }

 @Test
 public void testMain() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 assertEquals("test directory, test gives java.lang", parser.getDirectory().get("java.lang"));
 assertEquals("test directory, test gives javax.swing.border", parser.getDirectory().get("javax.swing.border"));
 assertEquals("localBrowserValue", SAXDirParser.getLocalBrowser());
 }

}