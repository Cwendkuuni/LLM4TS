package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.jstevh.viewer.SAXDirParser;
import com.jstevh.viewer.SAXParserFactory;
import com.jstevh.viewer.StringList;

public class SAXDirParserTest {

 // Test case for getWebData method
 @Test
 public void testGetWebData() {
 SAXDirParser parser = new SAXDirParser();
 assertEquals("", parser.getWebData());
 }

 // Test case for constructor
 @Test(expected = Exception.class)
 public void testSAXDirParserException() throws Exception {
 new SAXDirParser();
 fail("SAXDirParser constructor should throw an exception");
 }

 // Test case for startElement method with "BrowserLoc" tag
 @Test
 public void testStartElementBrowserLoc() throws SAXException {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group><BrowserLoc>TestBrowser</BrowserLoc></Group></Web>";
 parser.parse(new StringReader(xmlData));
 assertEquals("TestBrowser", SAXDirParser.getLocalBrowser());
 }

 // Test case for startElement method with "Editor" tag
 @Test
 public void testStartElementEditor() throws SAXException {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group><Editor>TestEditor</Editor></Group></Web>";
 parser.parse(new StringReader(xmlData));
 assertEquals("TestEditor", SAXDirParser.getEditor());
 }

 // Test case for startElement method with "acceptsLineNumber" tag
 @Test
 public void testStartElementAcceptsLineNumber() throws SAXException {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group><acceptsLineNumber>Yes</acceptsLineNumber></Group></Web>";
 parser.parse(new StringReader(xmlData));
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 // Test case for startElement method with "acceptsLineNumber" tag and "No" value
 @Test
 public void testStartElementAcceptsLineNumberNo() throws SAXException {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group><acceptsLineNumber>No</acceptsLineNumber></Group></Web>";
 parser.parse(new StringReader(xmlData));
 assertFalse(SAXDirParser.acceptsLineNumber());
 }

 // Test case for startElement method with "parameter" tag
 @Test
 public void testStartElementParameter() throws SAXException {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group><parameter>testParameter</parameter></Group></Web>";
 assertEquals("testParameter", SAXDirParser.lineNumberParameter());
 }

 // Test case for startElement method with "Group" tag
 @Test
 public void testStartElementGroup() throws SAXException {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group></Group></Web>";
 assertEquals(null, SAXDirParser.getDirectory());
 }

 // Test case for getDirectory method
 @Test
 public void testGetDirectory() {
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "/path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "/path/to/javax.swing.border");
 assertEquals(expectedDirectory, SAXDirParser.getDirectory());
 }

 // Test case for getLocalPackages method with empty directory
 @Test
 public void testGetLocalPackagesEmpty() {
 assertEquals(null, SAXDirParser.getLocalPackages());
 }

 // Test case for getLocalPackages method with packages in the directory
 @Test
 public void testGetLocalPackagesWithPackages() {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group><pkg>junit</pkg></Group>"
 + "<Web><Group><pkg>apache commons io</pkg></Group>"
 + "<Web><Group><pkg>junit</pkg></Group></Web>";
 parser.parse(new StringReader(xmlData));
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("junit");
 expectedLocalPackages.add("apache commons io");
 assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());
 }

 // Test case for getLocalBrowser method
 @Test
 public void testGetLocalBrowser() {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group><BrowserLoc>TestBrowser</BrowserLoc></Group></Web>";
 parser.parse(new StringReader(xmlData));
 assertEquals("TestBrowser", SAXDirParser.getLocalBrowser());
 }

 // Test case for getEditor method
 @Test
 public void testGetEditor() {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group><Editor>TestEditor</Editor></Group></Web>";
 parser.parse(new StringReader(xmlData));
 assertEquals("TestEditor", SAXDirParser.getEditor());
 }

 // Test case for acceptsLineNumber method
 @Test
 public void testAcceptsLineNumber() {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group><acceptsLineNumber>Yes</acceptsLineNumber></Group></Web>";
 parser.parse(new StringReader(xmlData));
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 // Test case for lineNumberParameter method
 @Test
 public void testLineNumberParameter() {
 SAXDirParser parser = new SAXDirParser();
 String xmlData = "<Web><Group><parameter>testParameter</parameter></Group></Web>";
 parser.parse(new StringReader(xmlData));
 assertEquals("testParameter", SAXDirParser.lineNumberParameter());
 }

}