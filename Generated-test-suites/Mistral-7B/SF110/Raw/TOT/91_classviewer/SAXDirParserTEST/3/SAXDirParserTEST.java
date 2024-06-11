package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import static org.mockito.Mockito.*;
import java.io.StringReader;
import java.util.*;
import javax.xml.parsers.*;

import org.junit.Before;
import org.junit.Test;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private HashMap<String, String> expectedDirectory;
 private StringReader input;

 @Before
 public void setup() {
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");
 parser = new SAXDirParser();
 input = new StringReader("<Directories>\n" +
 "<Group name=\"Java Packages\" version=\"1.0\">\n" +
 "<Web name=\"root\">\n" +
 "<Local name=\"java\">\n" +
 "<pkg>java.lang</pkg>\n" +
 "<pkg>java.util</pkg>\n" +
 "</Local>\n" +
 "</Web>\n" +
 "<Web name=\"javax\">\n" +
 "<Local name=\"javax\">\n" +
 "<pkg>javax.swing.border</pkg>\n" +
 "</Local>\n" +
 "</Web>\n" +
 "</Group>\n" +
 "</Directories>");
 }

 @Test
 public void testGetDirectory() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser parser = factory.newSAXParser();
 XMLReader xmlReader = parser.getXMLReader();
 StaxUtils staxUtils = mock(StaxUtils.class);
 xmlReader.setContentHandler(parser);
 xmlReader.setEntityResolver(staxUtils);
 InputSource inputSource = new InputSource(input);
 xmlReader.parse(inputSource);
 HashMap<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test(expected = Exception.class)
 public void testGetDirectoryWithNullInput() {
 parser.getDirectory();
 fail("Expected Exception to be thrown");
 }

 @Test
 public void testGetLocalPackages() {
 assertEquals(2, parser.getLocalPackages().size());
 String[] expectedLocalPackages = {"java.lang", "javax.swing.border"};
 assertArrayEquals(expectedLocalPackages, parser.getLocalPackages().toArray());
 }

 @Test
 public void testGetLocalPackagesWithEmptyList() {
 assertEquals(0, parser.getLocalPackages().size());
 }

 @Test
 public void testLocalBrowser() {
 assertEquals("root", parser.getLocalBrowser());
 }

 @Test
 public void testEditor() {
 assertEquals("", parser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() {
 assertFalse(parser.acceptsLineNumber());
 }

 @Test
 public void testAcceptsLineNumberWithYes() throws Exception {
 when(staxUtils.getLineNumber(anyInt())).thenReturn(42);
 input = new StringReader("<Directories>\n" +
 "<Group name=\"Java Packages\" version=\"1.0\">\n" +
 "<Web name=\"root\">\n" +
 "<Local name=\"java\">\n" +
 "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 "</Local>\n" +
 "</Web>\n" +
 "</Group>\n" +
 "</Directories>");
 parser.parse(input);
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 assertEquals("", parser.lineNumberParameter());
 }
}