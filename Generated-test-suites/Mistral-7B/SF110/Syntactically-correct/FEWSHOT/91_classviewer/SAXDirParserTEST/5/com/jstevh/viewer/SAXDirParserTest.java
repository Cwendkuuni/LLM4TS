package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import java.util.*;
import org.xml.sax.*;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private StringReader reader;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 reader = new StringReader("<Group>\n" +
                          "<Web name=\"java.lang\">\n" +
                          "<Local>Yes</Local>\n" +
                          "<Names>\n" +
                          " <pkg>java.lang</pkg>\n" +
                          " <pkg>java.util</pkg>\n" +
                          "</Names>\n" +
                          "</Web>\n" +
                          "<Web name=\"javax.swing.border\">\n" +
                          "<Local>No</Local>\n" +
                          "<Names>\n" +
                          " <pkg>javax.swing</pkg>\n" +
                          " <pkg>javax.swing.border</pkg>\n" +
                          "</Names>\n" +
                          "</Web>\n" +
                          "<BrowserLoc>Firefox</BrowserLoc>\n" +
                          "<Editor>Notepad</Editor>\n" +
                          "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                          "<parameter>123</parameter>\n" +
                          "</Group>");
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "java.lang");
 expectedDirectory.put("javax.swing.border", "javax.swing.border");
 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("java.lang");
 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 String expectedLocalBrowser = "Firefox";
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() {
 String expectedEditor = "Notepad";
 String actualEditor = parser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() {
 String expectedLineNumberParameter = "123";
 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test (expected = SAXException.class)
 public void testUnexpectedElement() {
 parser = new SAXDirParser();
 reader = new StringReader("<Group>\n" +
                          "<Web name=\"java.lang\">\n" +
                          "<Local>Yes</Local>\n" +
                          "<Names>\n" +
                          " <pkg>java.lang</pkg>\n" +
                          "</Names>\n" +
                          "<BrowserLoc>Firefox</BrowserLoc>\n" +
                          "<Editor>Notepad</Editor>\n" +
                          "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                          "<parameter>123</parameter>\n" +
                          "<UnexpectedElement>Unexpected element</UnexpectedElement>\n" +
                          "</Group>");
 parser.parse(reader);
 }

 @Test (expected = SAXException.class)
 public void testMissingElement() {
 parser = new SAXDirParser();
 reader = new StringReader("<Group>\n" +
                          "<Web name=\"java.lang\">\n" +
                          "<Local>Yes</Local>\n" +
                          "<Names>\n" +
                          " <pkg>java.lang</pkg>\n" +
                          "</Names>\n" +
                          "</Web>\n" +
                          "</Group>");
 parser.parse(reader);
 }
}