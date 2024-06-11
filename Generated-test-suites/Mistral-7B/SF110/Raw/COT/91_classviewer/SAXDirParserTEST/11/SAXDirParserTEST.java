package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private Map<String, String> expectedDirectory;
 private List<String> expectedLocalPackages;
 private String localBrowser;
 private String editor;
 private boolean acceptsLineNumber;
 private String lineNumberParameter;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "test1");
 expectedDirectory.put("javax.swing.border", "test2");
 expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("testPackage1");
 expectedLocalPackages.add("testPackage2");
 }

 @Test
 public void testGetWebData() {
 String actual = parser.getWebData();
 assertEquals("", actual);
 }

 @Test
 public void testStartElement_BrowserLoc() throws Exception {
 String xml = "<Directory>\n" +
               "<BrowserLoc>Firefox</BrowserLoc>\n" +
               "</Directory>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = XMLReaderFactory.createXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertEquals("Firefox", parser.getLocalBrowser());
 }

 @Test
 public void testStartElement_Editor() throws Exception {
 String xml = "<Directory>\n" +
               "<Editor>Notepad</Editor>\n" +
               "</Directory>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = XMLReaderFactory.createXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertEquals("Notepad", parser.getFile_editor());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws Exception {
 String xml = "<Directory>\n" +
               "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
               "</Directory>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = XMLReaderFactory.createXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElement_parameter() throws Exception {
 String xml = "<Directory>\n" +
               "<acceptsLineNumber>No</acceptsLineNumber>\n" +
               "<parameter>123</parameter>\n" +
               "</Directory>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = XMLReaderFactory.createXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertEquals("123", parser.lineNumberParameter());
 }

 @Test
 public void testStartElement_Group() throws Exception {
 String xml = "<Directory>\n" +
               "<Group>\n" +
               " <Web>Test</Web>\n" +
               "</Group>\n" +
               "</Directory>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 XMLReader reader = XMLReaderFactory.createXMLReader();
 reader.setContentHandler(parser);
 reader.parse(inputSource);
 assertFalse(parser.check);
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> actual = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actual);
 }

 @Test
 public void testGetLocalPackages() {
 List<String> actual = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLocalPackages_emptyDirectory() {
 SAXDirParser.getLocalPackages();
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackages_nullDirectory() {
 SAXDirParser.directory = null;
 SAXDirParser.getLocalPackages();
 }

 @Test
 public void testGetLocalBrowser() {
 String actual = SAXDirParser.getLocalBrowser();
 assertEquals("", actual);
 }

 @Test
 public void testGetEditor() {
 String actual = SAXDirParser.getEditor();
 assertEquals("", actual);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean actual = SAXDirParser.acceptsLineNumber();
 assertEquals(false, actual);
 }

 @Test
 public void testLineNumberParameter() {
 String actual = SAXDirParser.lineNumberParameter();
 assertEquals("", actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLocalPackages_uninitialized() {
 SAXDirParser.getLocalPackages();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetDirectory_uninitialized() {
 SAXDirParser.getDirectory();
 }
}