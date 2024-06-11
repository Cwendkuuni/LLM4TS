package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.XMLParseException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private XMLReader xmlReader;

 @Before
 public void setUp() throws Exception {
 parser = new SAXDirParser();
 xmlReader = new org.apache.xerces.parsers.SAXParserFactory().newSAXParser().getXMLReader();
 xmlReader.setContentHandler(parser);
 }

 @Test
 public void testGetWebData() throws Exception {
 parser.parse("testData.xml");
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 String namespace = "";
 String local = "BrowserLoc";
 String qname = local;
 Attributes atts = null;
 parser.startElement(namespace, local, qname, atts);
 assertTrue(parser.isCheck());
 assertEquals(0, parser.getLevel());
 }

 @Test
 public void testStartElement_Editor() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 String namespace = "";
 String local = "Editor";
 String qname = local;
 Attributes atts = null;
 parser.startElement(namespace, local, qname, atts);
 assertTrue(parser.isCheck());
 assertEquals(10, parser.getLevel());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 String namespace = "";
 String local = "acceptsLineNumber";
 String qname = local;
 Attributes atts = null;
 parser.startElement(namespace, local, qname, atts);
 assertTrue(parser.isCheck());
 assertEquals(11, parser.getLevel());
 }

 @Test
 public void testStartElement_parameter() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 String namespace = "";
 String local = "parameter";
 String qname = local;
 Attributes atts = null;
 parser.startElement(namespace, local, qname, atts);
 assertTrue(parser.isCheck());
 assertEquals(12, parser.getLevel());
 }

 @Test
 public void testStartElement_Group() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 String namespace = "";
 String local = "Group";
 String qname = local;
 Attributes atts = null;
 parser.startElement(namespace, local, qname, atts);
 assertFalse(parser.isCheck());
 }

 @Test
 public void testStartElement_Web() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 String namespace = "";
 String local = "Web";
 String qname = local;
 Attributes atts = null;
 parser.startElement(namespace, local, qname, atts);
 assertTrue(parser.isCheck());
 assertEquals(30, parser.getLevel());
 }

 @Test
 public void testStartElement_Local() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 String namespace = "";
 String local = "Local";
 String qname = local;
 Attributes atts = null;
 parser.startElement(namespace, local, qname, atts);
 assertTrue(parser.isCheck());
 assertEquals(31, parser.getLevel());
 }

 @Test
 public void testStartElement_Names() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 String namespace = "";
 String local = "Names";
 String qname = local;
 Attributes atts = null;
 parser.startElement(namespace, local, qname, atts);
 assertTrue(parser.isCheck());
 }

 @Test
 public void testStartElement_pkg() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 String namespace = "";
 String local = "pkg";
 String qname = local;
 Attributes atts = null;
 parser.startElement(namespace, local, qname, atts);
 assertEquals(4, parser.getLevel());
 }

 @Test
 public void testCharacters_getLocalBrowser() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 StringBuilder sb = new StringBuilder();
 for (int i = 0; i < 15; ++i) {
 char c = (char) 65533; // Invalid Unicode character
 sb.append(c);
 }
 parser.characters(sb.toString().toCharArray(), 0, sb.toString().length());
 parser.checkLevelAndThrowException();
 assertEquals("", parser.getLocalBrowser());
 }

 @Test
 public void testCharacters_getEditor() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 StringBuilder sb = new StringBuilder();
 sb.append("EditorName");
 parser.characters(sb.toString().toCharArray(), 0, sb.toString().length());
 parser.checkLevelAndThrowException();
 assertEquals("EditorName", parser.getEditor());
 }

 @Test
 public void testCharacters_acceptsLineNumber() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 StringBuilder sb = new StringBuilder();
 sb.append("Yes");
 parser.characters(sb.toString().toCharArray(), 0, sb.toString().length());
 parser.checkLevelAndThrowException();
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testCharacters_lineNumberParameter() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 StringBuilder sb = new StringBuilder();
 sb.append("lineNumberParamValue");
 parser.characters(sb.toString().toCharArray(), 0, sb.toString().length());
 parser.checkLevelAndThrowException();
 assertEquals("lineNumberParamValue", parser.lineNumberParameter());
 }

 @Test
 public void testCharacters_directory() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 StringBuilder sb = new StringBuilder();
 sb.append("java.lang");
 parser.characters(sb.toString().toCharArray(), 0, sb.toString().length());
 parser.characters(":".toCharArray(), 0, 1);
 parser.characters(sb.toString().toCharArray(), 0, sb.toString().length());
 parser.characters(":".toCharArray(), 0, 1);
 parser.characters("test".toCharArray(), 0, 4);
 parser.checkLevelAndThrowException();
 assertEquals("test", parser.getDirectory().get("java.lang"));
 }

 @Test
 public void testCharacters_localPackages() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 StringBuilder sb = new StringBuilder();
 sb.append("java.util");
 parser.characters(sb.toString().toCharArray(), 0, sb.toString().length());
 parser.characters(":".toCharArray(), 0, 1);
 parser.characters("java.io".toCharArray(), 0, 6);
 parser.checkLevelAndThrowException();
 String expected = "java.io, java.util";
 ArrayList<String> actual = new ArrayList<>((ArrayList<?>) parser.getLocalPackages());
 assertEquals(expected, actual.toString());
 }

 @Test
 public void testEndElement_Group() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 parser.setLevel(-1);
 parser.setCheck(false);
 String namespace = "";
 String local = "Group";
 String qname = local;
 parser.endElement(namespace, local, qname);
 assertFalse(parser.isCheck());
 }

 @Test
 public void testGetDirectory() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 Map<String, String> expected = new HashMap<>();
 expected.put("java.lang", "test");
 expected.put("javax.swing.border", "test2");
 assertEquals(expected, parser.getDirectory());
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 ArrayList<String> expected = new ArrayList<>();
 expected.add("java.util");
 expected.add("java.io");
 assertEquals(expected, parser.getLocalPackages());
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 assertEquals("", parser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 assertEquals("EditorName", parser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 parser = new SAXDirParser();
 xmlReader.parse(new FileInputStream("testData.xml"));
 assertEquals("lineNumberParamValue", parser.lineNumberParameter());
 }

 @Test
 public void testParse_InvalidXml() throws Exception {
 parser = new SAXDirParser();
 try {
 xmlReader.parse(new FileInputStream("invalidXml.xml"));
 fail("Expected SAXException");
 } catch (SAXException e) {
 // Expected
 }
 }

 private void checkLevelAndThrowException() throws SAXException {
 if (parser.getLevel() != -1) {
 throw new SAXException("Unexpected level: " + parser.getLevel());
 }
 }
}