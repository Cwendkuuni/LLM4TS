package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private XMLReader xmlReader;

 @Before
 public void setUp() {
 saxDirParser = new SAXDirParser();
 xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "testLocation1");
 expectedDirectory.put("javax.swing.border", "testLocation2");

 xmlReader.parse(new StringReader("<Group>\n" +
         "<Web name=\"testWeb\">\n" +
         "<Names>\n" +
         "<Group>\n" +
         "<Local name=\"java.lang\">\n" +
         "<parameter name=\"location\">testLocation1</parameter>\n" +
         "</Local>\n" +
         "</Group>\n" +
         "<Group>\n" +
         "<Local name=\"javax.swing.border\">\n" +
         "<parameter name=\"location\">testLocation2</parameter>\n" +
         "</Local>\n" +
         "</Group>\n" +
         "</Names>\n" +
         "</Web>\n" +
         "</Group>"));

 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 ArrayList<String> expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("junit");
 expectedLocalPackages.add("hamcrest");
 expectedLocalPackages.add("mockito");

 xmlReader.parse(new StringReader("<Group>\n" +
         "<Web name=\"testWeb\">\n" +
         "<Names>\n" +
         "<Group>\n" +
         "<Local name=\"junit\">\n" +
         "</Local>\n" +
         "</Group>\n" +
         "<Group>\n" +
         "<Local name=\"hamcrest\">\n" +
         "</Local>\n" +
         "</Group>\n" +
         "<Group>\n" +
         "<Local name=\"mockito\">\n" +
         "</Local>\n" +
         "</Group>\n" +
         "</Names>\n" +
         "</Web>\n" +
         "</Group>"));

 ArrayList<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 String expectedLocalBrowser = "testBrowser";

 xmlReader.parse(new StringReader("<Group>\n" +
         "<Web name=\"testWeb\">\n" +
         "<Names>\n" +
         "<Group>\n" +
         "<BrowserLoc>\n" +
         "<parameter name=\"browser\">" + expectedLocalBrowser + "</parameter>\n" +
         "</BrowserLoc>\n" +
         "</Group>\n" +
         "</Names>\n" +
         "</Web>\n" +
         "</Group>"));

 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() {
 String expectedEditor = "testEditor";

 xmlReader.parse(new StringReader("<Group>\n" +
         "<Web name=\"testWeb\">\n" +
         "<Names>\n" +
         "<Group>\n" +
         "<Editor>\n" +
         "<parameter name=\"editor\">" + expectedEditor + "</parameter>\n" +
         "</Editor>\n" +
         "</Group>\n" +
         "</Names>\n" +
         "</Web>\n" +
         "</Group>"));

 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean expectedAcceptsLineNumber = true;

 xmlReader.parse(new StringReader("<Group>\n" +
         "<Web name=\"testWeb\">\n" +
         "<Names>\n" +
         "<Group>\n" +
         "<acceptsLineNumber>\n" +
         "<parameter name=\"acceptLineNumber\">" + expectedAcceptsLineNumber + "</parameter>\n" +
         "</acceptsLineNumber>\n" +
         "</Group>\n" +
         "</Names>\n" +
         "</Web>\n" +
         "</Group>"));

 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() {
 String expectedLineNumberParameter = "testParameter";

 xmlReader.parse(new StringReader("<Group>\n" +
         "<Web name=\"testWeb\">\n" +
         "<Names>\n" +
         "<Group>\n" +
         "<acceptsLineNumber>\n" +
         "<parameter name=\"parameter\">" + expectedLineNumberParameter + "</parameter>\n" +
         "</acceptsLineNumber>\n" +
         "</Group>\n" +
         "</Names>\n" +
         "</Web>\n" +
         "</Group>"));

 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

}