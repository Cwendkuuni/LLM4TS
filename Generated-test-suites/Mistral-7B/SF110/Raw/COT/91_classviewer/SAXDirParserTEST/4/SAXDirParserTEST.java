package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import org.xml.sax.helpers.DefaultHandler;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.jstevh.viewer.*;
import javax.xml.parsers.*;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Before
 public void setUp() throws Exception {
 parser = new SAXDirParser();
 }

 @Test
 public void testGetWebData() {
 String expected = "";
 String actual = parser.getWebData();
 assertEquals(expected, actual);
 }

 @Test(expected = Exception.class)
 public void testSAXDirParserConstructorThrowsException() throws Exception {
 parser = new SAXDirParser(); // should throw an exception
 fail("SAXDirParser constructor should have thrown an exception");
 }

 @Test
 public void testStartElementBrowserLoc() throws SAXException {
 parser.startElement("", "BrowserLoc", "BrowserLoc", null);
 assertTrue(parser.check);
 assertEquals(0, parser.level);
 }

 @Test
 public void testStartElementEditor() throws SAXException {
 parser.startElement("", "Editor", "Editor", null);
 assertTrue(parser.check);
 assertEquals(10, parser.level);
 }

 @Test
 public void testStartElementAcceptsLineNumber() throws SAXException {
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 assertTrue(parser.check);
 assertEquals(11, parser.level);
 }

 @Test
 public void testStartElementParameter() throws SAXException {
 parser.startElement("", "parameter", "parameter", null);
 assertTrue(parser.check);
 assertEquals(12, parser.level);
 }

 @Test
 public void testStartElementGroup() throws SAXException {
 parser.startElement("", "Group", "Group", null);
 assertFalse(parser.check);
 }

 @Test
 public void testStartElementWeb() throws SAXException {
 parser.startElement("", "Web", "Web", null);
 assertTrue(parser.check);
 assertEquals(30, parser.level);
 }

 @Test
 public void testStartElementLocal() throws SAXException {
 parser.startElement("", "Local", "Local", null);
 assertTrue(parser.check);
 assertEquals(31, parser.level);
 }

 @Test
 public void testStartElementNames() throws SAXException {
 parser.startElement("", "Names", "Names", null);
 assertTrue(parser.check);
 }

 @Test
 public void testStartElementPkg() throws SAXException {
 parser.startElement("", "pkg", "pkg", null);
 assertEquals(4, parser.level);
 }

 @Test
 public void testCharactersValidDirectoryName() throws SAXException {
 parser.characters("java.lang".toCharArray(), 0, "java.lang".length());
 assertEquals("java.lang", parser.location);
 assertTrue(parser.local);
 }

 @Test
 public void testCharactersInvalidDirectoryName() throws SAXException {
 parser.characters("java.lang".toCharArray(), 0, "java.lang".length());
 assertFalse(parser.local);
 }

 @Test
 public void testCharactersEmptyDirectoryName() throws SAXException {
 parser.characters("".toCharArray(), 0, 0);
 assertNull(parser.location);
 }

 @Test
 public void testCharactersTrailingPeriod() throws SAXException {
 parser.characters("javax.swing.border.".toCharArray(), 0, "javax.swing.border.".length());
 assertEquals("javax.swing.border", parser.location);
 }

 @Test
 public void testEndElementGroup() throws SAXException {
 parser.endElement("", "Group", "Group");
 assertFalse(parser.check);
 }

 @Test
 public void testGetDirectory() throws Exception {
 HashMap<String, String> expected = new HashMap<>();
 expected.put("java.lang", "testLocation1");
 expected.put("javax.swing.border", "testLocation2");
 HashMap<String, String> actual = SAXDirParser.getDirectory();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 StringList expected = new StringList();
 expected.add("java.lang");
 expected.add("javax.swing.border");
 StringList actual = SAXDirParser.getLocalPackages();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 String expected = "testLocalBrowser";
 String actual = SAXDirParser.getLocalBrowser();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetEditor() throws Exception {
 String expected = "testEditor";
 String actual = SAXDirParser.getEditor();
 assertEquals(expected, actual);
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 boolean expected = true;
 boolean actual = SAXDirParser.acceptsLineNumber();
 assertEquals(expected, actual);
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 String expected = "testParameter";
 String actual = SAXDirParser.lineNumberParameter();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDirectoryAfterEndParse() throws Exception {
 SAXDirParser.getDirectory();
 fail("SAXDirParser must not be able to get the directory after parsing has ended");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLocalPackagesAfterEndParse() throws Exception {
 SAXDirParser.getLocalPackages();
 fail("SAXDirParser must not be able to get the local packages after parsing has ended");
 }

 @Test(expected = IllegalStateException.class)
 public void testLocalBrowserAfterEndParse() throws Exception {
 SAXDirParser.getLocalBrowser();
 fail("SAXDirParser must not be able to get the local browser after parsing has ended");
 }

 @Test(expected = IllegalStateException.class)
 public void testEditorAfterEndParse() throws Exception {
 SAXDirParser.getEditor();
 fail("SAXDirParser must not be able to get the editor after parsing has ended");
 }

 @Test(expected = IllegalStateException.class)
 public void testAcceptsLineNumberAfterEndParse() throws Exception {
 SAXDirParser.acceptsLineNumber();
 fail("SAXDirParser must not be able to check accepts line number after parsing has ended");
 }

 @Test(expected = IllegalStateException.class)
 public void testLineNumberParameterAfterEndParse() throws Exception {
 SAXDirParser.lineNumberParameter();
 fail("SAXDirParser must not be able to get the line number parameter after parsing has ended");
 }
}