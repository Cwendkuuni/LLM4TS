package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.jstevh.viewer.SAXDirParser;
import com.jstevh.viewer.SAXParserFactory;
import com.jstevh.viewer.StringList;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 }

 @Test
 public void testGetWebData() {
 String expected = "";
 String actual = parser.getWebData();
 assertEquals(expected, actual);
 }

 @Test
 public void testStartElement_BrowserLoc() throws SAXException {
 parser.startElement("", "BrowserLoc", "BrowserLoc", null);
 assertTrue(parser.isCheck());
 assertEquals(0, parser.getLevel());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_BrowserLoc_InvalidNameSpace() throws SAXException {
 parser.startElement("invalid", "BrowserLoc", "BrowserLoc", null);
 }

 @Test
 public void testStartElement_Editor() throws SAXException {
 parser.startElement("", "Editor", "Editor", null);
 assertTrue(parser.isCheck());
 assertEquals(10, parser.getLevel());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_Editor_InvalidNameSpace() throws SAXException {
 parser.startElement("invalid", "Editor", "Editor", null);
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws SAXException {
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 assertTrue(parser.isCheck());
 assertEquals(11, parser.getLevel());
 parser.characters("Yes".toCharArray(), 0, "Yes".length());
 assertTrue(parser.isLineNumAccepted());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_acceptsLineNumber_InvalidNameSpace() throws SAXException {
 parser.startElement("invalid", "acceptsLineNumber", "acceptsLineNumber", null);
 }

 @Test
 public void testStartElement_parameter() throws SAXException {
 parser.startElement("", "parameter", "parameter", null);
 assertTrue(parser.isCheck());
 assertEquals(12, parser.getLevel());
 parser.characters("paramValue".toCharArray(), 0, "paramValue".length());
 assertEquals("paramValue", parser.getLineNumParam());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_parameter_InvalidNameSpace() throws SAXException {
 parser.startElement("invalid", "parameter", "parameter", null);
 }

 @Test
 public void testStartElement_Group() throws SAXException {
 parser.startElement("", "Group", "Group", null);
 assertFalse(parser.isCheck());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_Group_InvalidNameSpace() throws SAXException {
 parser.startElement("invalid", "Group", "Group", null);
 }

 @Test
 public void testStartElement_Web() throws SAXException {
 parser.startElement("", "Web", "Web", null);
 assertTrue(parser.isCheck());
 assertEquals(30, parser.getLevel());
 parser.characters("webData".toCharArray(), 0, "webData".length());
 assertEquals("webData", parser.getLocation());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_Web_InvalidNameSpace() throws SAXException {
 parser.startElement("invalid", "Web", "Web", null);
 }

 @Test
 public void testStartElement_Local() throws SAXException {
 parser.startElement("", "Local", "Local", null);
 assertTrue(parser.isCheck());
 assertEquals(31, parser.getLevel());
 parser.characters("No".toCharArray(), 0, "No".length());
 assertFalse(parser.isLocal());
 }

 @Test(expected = SAXException.class)
 public void testStartElement_Local_InvalidNameSpace() throws SAXException {
 parser.startElement("invalid", "Local", "Local", null);
 }

 @Test
 public void testStartElement_pkg() throws SAXException {
 parser.startElement("", "pkg", "pkg", null);
 assertTrue(parser.isCheck());
 assertEquals(4, parser.getLevel());
 parser.characters("pkgName".toCharArray(), 0, "pkgName".length());
 assertEquals("pkgName", parser.getDirectory().get("pkgName"));
 }

 @Test(expected = SAXException.class)
 public void testStartElement_pkg_InvalidNameSpace() throws SAXException {
 parser.startElement("invalid", "pkg", "pkg", null);
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> expected = new HashMap<>();
 expected.put("java.lang", "java/lang");
 expected.put("javax.swing.border", "javax/swing/border");
 Map<String, String> actual = SAXDirParser.getDirectory();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalPackages() {
 List<String> expected = Arrays.asList("pkg1", "pkg2");
 List<String> actual = SAXDirParser.getLocalPackages();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalPackages_Empty() {
 assertEquals(null, SAXDirParser.getLocalPackages());
 }

 @Test
 public void testGetLocalBrowser() {
 String expected = "localBrowser";
 String actual = SAXDirParser.getLocalBrowser();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetEditor() {
 String expected = "fileEditor";
 String actual = SAXDirParser.getEditor();
 assertEquals(expected, actual);
 }

 @Test
 public void testAcceptsLineNumber() {
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testAcceptsLineNumber_False() {
 assertFalse(SAXDirParser.acceptsLineNumber());
 SAXDirParser.lineNumberParameter("Yes");
 SAXDirParser.acceptsLineNumber();
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 String expected = "lineNumberParam";
 String actual = SAXDirParser.lineNumberParameter();
 assertEquals(expected, actual);
 }
}