package com.jstevh.viewer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.SAXException;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private XMLFilterImpl filter;

 @Before
 public void setup() {
 parser = new SAXDirParser();
 filter = new XMLFilterImpl(parser);
 }

 @Test
 public void testGetWebData() throws SAXException {
 parser.parse("src/test/resources/packagedirectory.xml");
 assertNotNull(parser.getWebData());
 }

 @Test
 public void testGetDirectory() throws SAXException {
 parser.parse("src/test/resources/packagedirectory.xml");
 Map<String, String> expected = new HashMap<>();
 expected.put("java.lang", "java.lang");
 expected.put("javax.swing.border", "javax.swing.border");
 assertSame(expected, parser.getDirectory());
 }

 @Test
 public void testGetLocalPackages() throws SAXException {
 parser.parse("src/test/resources/packagedirectory.xml");
 List<String> expected = new ArrayList<>();
 expected.add("java.lang");
 expected.add("javax.swing.border");
 List<String> result = parser.getLocalPackages();
 if (result == null) {
 assertNull(result);
 } else {
 assertSame(expected, result);
 }
 }

 @Test
 public void testGetLocalBrowser() throws SAXException {
 parser.parse("src/test/resources/packagedirectory.xml");
 String expected = "Local Browser";
 assertEquals(expected, parser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() throws SAXException {
 parser.parse("src/test/resources/packagedirectory.xml");
 String expected = "TextPad";
 assertEquals(expected, parser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() throws SAXException {
 parser.parse("src/test/resources/packagedirectory.xml");
 assertFalse(parser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() throws SAXException {
 parser.parse("src/test/resources/packagedirectory.xml");
 String expected = "5";
 assertEquals(expected, parser.lineNumberParameter());
 }

 @Test(expected = SAXException.class)
 public void testInvalidElement() throws SAXException {
 filter.characters("Invalid Element".getBytes(), 0, "Invalid Element".length());
 }

 @Test
 public void testValidElements() throws SAXException {
 String xml = "<Group><Web name=\"java.lang\"><Local>No</Local><Names>java.lang</Names></Web>"
 + "<Web name=\"javax.swing.border\"><Local>Yes</Local><Names>javax.swing.border</Names></Web></Group>";
 filter.parse(xml.getBytes());
 assertFalse(parser.acceptsLineNumber());
 assertEquals("java.lang", parser.getDirectory().get("java.lang"));
 assertEquals("javax.swing.border", parser.getDirectory().get("javax.swing.border"));
 assertEquals("Local Browser", parser.getLocalBrowser());
 assertEquals("TextPad", parser.getEditor());
 assertTrue(parser.acceptsLineNumber());
 assertEquals("5", parser.lineNumberParameter());
 }
}