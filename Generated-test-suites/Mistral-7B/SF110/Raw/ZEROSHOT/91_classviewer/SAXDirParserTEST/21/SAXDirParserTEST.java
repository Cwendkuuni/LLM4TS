package com.jstevh.viewer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private XMLReader reader;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 reader = new org.apache.xml.apis.ext.DefaultSAXParserFactory().newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 }

 @After
 public void tearDown() {
 parser = null;
 reader = null;
 }

 @Test
 public void testParseDirectory() throws SAXException {
 String xml = "<Web>\n" +
 " <Group>\n" +
 " <Local>\n" +
 " <Names>\n" +
 " <pkg>java.lang</pkg>\n" +
 " <Names>\n" +
 " <pkg>javax.swing.border</pkg>\n" +
 " </Names>\n" +
 " </Local>\n" +
 " </Group>\n" +
 " </Web>";

 InputSource inputSource = new InputSource(new StringReader(xml));
 reader.parse(inputSource);

 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "test directory, test gives java.lang");
 expectedDirectory.put("javax.swing.border", "test directory, test gives javax.swing.border");

 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testParseEmptyDirectory() throws SAXException {
 String xml = "<Web>\n" +
 " <Group>\n" +
 " </Group>\n" +
 " </Web>";

 InputSource inputSource = new InputSource(new StringReader(xml));
 reader.parse(inputSource);

 Map<String, String> actualDirectory = parser.getDirectory();
 assertNull(actualDirectory);
 }

 @Test
 public void testParseLocalBrowser() throws SAXException {
 String xml = "<Web>\n" +
 " <BrowserLoc>Firefox</BrowserLoc>\n" +
 " <Group>\n" +
 " </Group>\n" +
 " </Web>";

 InputSource inputSource = new InputSource(new StringReader(xml));
 reader.parse(inputSource);

 String expectedLocalBrowser = "Firefox";
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testParseNoLocalBrowser() throws SAXException {
 String xml = "<Web>\n" +
 " <Group>\n" +
 " </Group>\n" +
 " </Web>";

 InputSource inputSource = new InputSource(new StringReader(xml));
 reader.parse(inputSource);

 String actualLocalBrowser = parser.getLocalBrowser();
 assertNull(actualLocalBrowser);
 }

 @Test
 public void testParseLocalPackages() throws SAXException {
 String xml = "<Web>\n" +
 " <Group>\n" +
 " <Local>\n" +
 " <Names>\n" +
 " <pkg>java.lang</pkg>\n" +
 " <pkg>javax.swing.border</pkg>\n" +
 " </Names>\n" +
 " </Local>\n" +
 " </Group>\n" +
 " </Web>";

 InputSource inputSource = new InputSource(new StringReader(xml));
 reader.parse(inputSource);

 List<String> expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("java.lang");
 expectedLocalPackages.add("javax.swing.border");

 List<String> actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testParseEmptyLocalPackages() throws SAXException {
 String xml = "<Web>\n" +
 " <Group>\n" +
 " <Local>\n" +
 " <Names>\n" +
 " </Names>\n" +
 " </Local>\n" +
 " </Group>\n" +
 " </Web>";

 InputSource inputSource = new InputSource(new StringReader(xml));
 reader.parse(inputSource);

 List<String> actualLocalPackages = parser.getLocalPackages();
 assertNull(actualLocalPackages);
 }

 @Test
 public void testParseAcceptsLineNumber() throws SAXException {
 String xml = "<Web>\n" +
 " <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 " <Group>\n" +
 " </Group>\n" +
 " </Web>";

 InputSource inputSource = new InputSource(new StringReader(xml));
 reader.parse(inputSource);

 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testParseNoAcceptsLineNumber() throws SAXException {
 String xml = "<Web>\n" +
 " <Group>\n" +
 " </Group>\n" +
 " </Web>";

 InputSource inputSource = new InputSource(new StringReader(xml));
 reader.parse(inputSource);

 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertFalse(actualAcceptsLineNumber);
 }

 @Test
 public void testParseLineNumberParameter() throws SAXException {
 String xml = "<Web>\n" +
 " <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 " <parameter>123</parameter>\n" +
 " <Group>\n" +
 " </Group>\n" +
 " </Web>";

 InputSource inputSource = new InputSource(new StringReader(xml));
 reader.parse(inputSource);

 String expectedLineNumberParameter = "123";
 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test
 public void testParseNoLineNumberParameter() throws SAXException {
 String xml = "<Web>\n" +
 " <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
 " <Group>\n" +
 " </Group>\n" +
 " </Web>";

 InputSource inputSource = new InputSource(new StringReader(xml));
 reader.parse(inputSource);

 String actualLineNumberParameter = parser.lineNumberParameter();
 assertNull(actualLineNumberParameter);
 }
}