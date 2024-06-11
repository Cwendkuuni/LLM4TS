package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.*;

import org.junit.*;
import org.xml.sax.helpers.DefaultHandler;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private String expectedDirectory;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedAcceptsLineNumber;
 private String expectedLineNumberParameter;

 @Before
 public void setUp() {
 this.parser = new SAXDirParser();
 this.expectedDirectory = new HashMap<String, String>() {{
 put("java.lang", "test1.java");
 put("javax.swing.border", "test2.java");
 }};
 this.expectedLocalBrowser = "Google Chrome";
 this.expectedEditor = "Eclipse";
 this.expectedAcceptsLineNumber = true;
 this.expectedLineNumberParameter = "5";
 }

 @Test
 public void testParsingXML() throws Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(xmlFile);

 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);

 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);

 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);

 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);

 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);

 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertNotNull(actualLocalPackages);
 }

 @Test
 public void testEmptyXML() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new File("empty.xml"));

 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertNull(actualDirectory);

 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals("", actualLocalBrowser);

 String actualEditor = SAXDirParser.getEditor();
 assertEquals("", actualEditor);

 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertFalse(actualAcceptsLineNumber);

 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals("", actualLineNumberParameter);
 }

 @Test
 public void testMalformedXML() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser saxParser = factory.newSAXParser();
 XMLReader reader = saxParser.getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new File("malformed.xml"));

 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertNull(actualDirectory);
 }
}