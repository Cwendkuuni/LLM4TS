package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.StringReader;

import com.jstevh.viewer.*;
import static com.jstevh.viewer.SAXDirParser.*;

public class SAXDirParserTest {

 private SAXParser parser;
 private StringReader reader;
 private SAXDirParser handler;

 @Before
 public void setup() throws Exception {
 parser = SAXParserFactory.newInstance().newSAXParser();
 reader = new StringReader("<Web><Local>Yes</Local><Editor>Notepad</Editor><acceptsLineNumber>Yes</acceptsLineNumber><parameter>123</parameter><Group><Names><pkg>java.lang</pkg><Web>Java</Web></Names></Group></Web>");
 handler = new SAXDirParser();
 parser.getXMLReader().setContentHandler(handler);
 }

 @Test
 public void testGetWebData() {
 SAXDirParser parserInstance = new SAXDirParser();
 assertEquals("", parserInstance.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() throws SAXException {
 handler.check = true;
 handler.startElement("", "BrowserLoc", "BrowserLoc", null);
 assertTrue(handler.check);
 assertEquals(0, handler.level);
 }

 @Test
 public void testStartElement_Editor() throws SAXException {
 handler.check = true;
 handler.startElement("", "Editor", "Editor", null);
 assertTrue(handler.check);
 assertEquals(10, handler.level);
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws SAXException {
 handler.check = true;
 handler.startElement("", "acceptsLineNumber", "acceptsLineNumber", null);
 assertTrue(handler.check);
 assertEquals(11, handler.level);
 }

 @Test
 public void testStartElement_parameter() throws SAXException {
 handler.check = true;
 handler.startElement("", "parameter", "parameter", null);
 assertTrue(handler.check);
 assertEquals(12, handler.level);
 }

 @Test
 public void testStartElement_Group() throws SAXException {
 handler.check = false;
 handler.startElement("", "Group", "Group", null);
 assertFalse(handler.check);
 assertEquals(-1, handler.level);
 }

 @Test
 public void testStartElement_Web() throws SAXException {
 handler.check = true;
 handler.startElement("", "Web", "Web", null);
 assertTrue(handler.check);
 assertEquals(30, handler.level);
 }

 @Test
 public void testStartElement_Local() throws SAXException {
 handler.check = true;
 handler.startElement("", "Local", "Local", null);
 assertTrue(handler.check);
 assertEquals(31, handler.level);
 }

 @Test
 public void testStartElement_Names() throws SAXException {
 handler.check = true;
 handler.startElement("", "Names", "Names", null);
 assertTrue(handler.check);
 assertEquals(2, handler.level);
 }

 @Test
 public void testStartElement_pkg() throws SAXException {
 handler.check = false;
 handler.startElement("", "pkg", "pkg", null);
 assertEquals(4, handler.level);
 }

 @Test
 public void testCharacters_acceptsLineNumber_Yes() throws SAXException {
 handler.check = true;
 handler.characters('Y', 0, 2);
 assertTrue(handler.check);
 assertEquals(11, handler.level);
 assertTrue(handler.lineNumAccepted);
 }

 @Test
 public void testCharacters_acceptsLineNumber_No() throws SAXException {
 handler.check = true;
 handler.characters('N', 0, 1);
 assertTrue(handler.check);
 assertEquals(11, handler.level);
 assertFalse(handler.lineNumAccepted);
 }

 @Test
 public void testCharacters_parameter() throws SAXException {
 handler.check = true;
 handler.characters("123".getBytes(), 0, 3);
 assertTrue(handler.check);
 assertEquals(12, handler.level);
 assertEquals("123", handler.lineNumParam);
 }

 @Test(expected = SAXException.class)
 public void testCharacters_emptyString() throws SAXException {
 handler.check = true;
 handler.characters("".getBytes(), 0, 0);
 fail("Expected SAXException but didn't get one.");
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> result = SAXDirParser.getDirectory();
 assertEquals(1, result.size());
 assertEquals("Java", result.get("java.lang"));
 }

 @Test
 public void testGetLocalPackages() {
 StringList result = SAXDirParser.getLocalPackages();
 assertNotNull(result);
 assertEquals(1, result.size());
 assertEquals("java.lang", result.get(0));
 }

 @Test
 public void testGetLocalBrowser() {
 String result = SAXDirParser.getLocalBrowser();
 assertEquals("", result);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean result = SAXDirParser.acceptsLineNumber();
 assertFalse(result);
 }

 @Test
 public void testLineNumberParameter() {
 String result = SAXDirParser.lineNumberParameter();
 assertEquals("", result);
 }

 @Test
 public void testMain() throws Exception {
 SAXDirParser parserInstance = new SAXDirParser();
 parserInstance.parse("packagedirectory.xml");
 assertEquals("test directory, test gives Java", parserInstance.getWebData());
 }

 @After
 public void tearDown() {
 handler = null;
 parser = null;
 reader = null;
 }
}