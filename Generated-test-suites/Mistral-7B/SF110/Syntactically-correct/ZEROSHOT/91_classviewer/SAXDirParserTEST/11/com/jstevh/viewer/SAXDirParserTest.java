package com.jstevh.viewer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringList;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private HashMap<String, String> expectedDirectory;
 private StringList expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedAcceptsLineNumber;
 private String expectedLineNumberParameter;

 @Before
 public void setUp() {
 saxDirParser = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "test1");
 expectedDirectory.put("javax.swing.border", "test2");
 expectedLocalPackages = new StringList();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 expectedLocalBrowser = "Firefox";
 expectedEditor = "Sublime Text";
 expectedAcceptsLineNumber = true;
 expectedLineNumberParameter = "32";
 }

 @After
 public void tearDown() {
 saxDirParser = null;
 }

 @Test
 public void testGetDirectory() throws Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(saxDirParser);
 myReader.parse(xmlFile);
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(saxDirParser);
 myReader.parse(xmlFile);
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(saxDirParser);
 myReader.parse(xmlFile);
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() throws Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(saxDirParser);
 myReader.parse(xmlFile);
 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(saxDirParser);
 myReader.parse(xmlFile);
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 File xmlFile = new File("packagedirectory.xml");
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(saxDirParser);
 myReader.parse(xmlFile);
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }
}