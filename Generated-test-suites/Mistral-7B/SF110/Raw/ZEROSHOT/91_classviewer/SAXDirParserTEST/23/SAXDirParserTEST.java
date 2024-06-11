package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringList;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private XMLReader xmlReader;
 private Map<String, String> expectedDirectory;
 private StringList expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedFileEditor;
 private boolean expectedAcceptsLineNumber;
 private String expectedLineNumberParameter;

 @Before
 public void setUp() throws Exception {
 parser = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "expected_location_for_java.lang");
 expectedDirectory.put("javax.swing.border", "expected_location_for_javax.swing.border");
 expectedLocalPackages = new StringList();
 expectedLocalPackages.add("expected_local_package_1");
 expectedLocalPackages.add("expected_local_package_2");
 expectedLocalBrowser = "expected_local_browser";
 expectedFileEditor = "expected_file_editor";
 expectedAcceptsLineNumber = true;
 expectedLineNumberParameter = "expected_line_number_parameter";
 }

 @Test
 public void testGetDirectory() throws Exception {
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 String xmlString = "<Web>\n" +
         "<Local>\n" +
         "  <Names>\n" +
         "    <pkg name=\"java.lang\">java.lang</pkg>\n" +
         "    <pkg name=\"javax.swing.border\">javax.swing.border</pkg>\n" +
         "  </Names>\n" +
         "  <Local>\n" +
         "    <BrowserLoc>No</BrowserLoc>\n" +
         "    <Editor>expected_file_editor</Editor>\n" +
         "    <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         "    <parameter>expected_line_number_parameter</parameter>\n" +
         "  </Local>\n" +
         "  <Web>\n" +
         "    <Local>\n" +
         "      <Names>\n" +
         "        <pkg name=\"expected_local_package_1\">expected_location_for_expected_local_package_1</pkg>\n" +
         "        <pkg name=\"expected_local_package_2\">expected_location_for_expected_local_package_2</pkg>\n" +
         "      </Names>\n" +
         "    </Local>\n" +
         "    <Local>\n" +
         "      <BrowserLoc>expected_local_browser</BrowserLoc>\n" +
         "    </Local>\n" +
         "  </Web>\n" +
         "</Web>";
 xmlReader.parse(new InputSource(new StringReader(xmlString)));
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 String xmlString = "<Web>\n" +
         "<Local>\n" +
         "  <Names>\n" +
         "    <pkg name=\"java.lang\">java.lang</pkg>\n" +
         "    <pkg name=\"javax.swing.border\">javax.swing.border</pkg>\n" +
         "  </Names>\n" +
         "  <Local>\n" +
         "    <BrowserLoc>No</BrowserLoc>\n" +
         "    <Editor>expected_file_editor</Editor>\n" +
         "    <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
         "    <parameter>expected_line_number_parameter</parameter>\n" +
         "  </Local>\n" +
         "  <Web>\n" +
         "    <Local>\n" +
         "      <Names>\n" +
         "        <pkg name=\"expected_local_package_1\">expected_location_for_expected_local_package_1</pkg>\n" +
         "        <pkg name=\"expected_local_package_2\">expected_location_for_expected_local_package_2</pkg>\n" +
         "      </Names>\n" +
         "    </Local>\n" +
         "  </Web>\n" +
         "</Web>";
 xmlReader.parse(new InputSource(new StringReader(xmlString)));
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();xmlReader.setContentHandler(parser); String xmlString = "<Web>\n" + "  <Local>\n" + "    <Names>\n" + "      <pkg name=\"java.lang\">java.lang</pkg>\n" + "      <pkg name=\"javax.swing.border\">javax.swing.border</pkg>\n" + "    </Names>\n" + "    <Local>\n" + "      <BrowserLoc>expected_local_browser</BrowserLoc>\n" + "      <Editor>expected_file_editor</Editor>\n" + "      <acceptsLineNumber>Yes</acceptsLineNumber>\n" + "      <parameter>expected_line_number_parameter</parameter>\n" + "    </Local>\n" + "  </Web>\n" + "</Web>"; xmlReader.parse(new InputSource(new StringReader(xmlString))); String actualLocalBrowser = SAXDirParser.getLocalBrowser(); assertEquals(expectedLocalBrowser, actualLocalBrowser); }

 @Test public void testGetEditor() throws Exception { XMLReader xmlReader = XMLReaderFactory.createXMLReader(); xmlReader.setContentHandler(parser); String xmlString = "<Web>\n" + "  <Local>\n" + "    <Names>\n" + "      <pkg name=\"java.lang\">java.lang</pkg>\n" + "      <pkg name=\"javax.swing.border\">javax.swing.border</pkg>\n" + "    </Names>\n" + "    <Local>\n" + "      <BrowserLoc>No</BrowserLoc>\n" + "      <Editor>expected_file_editor</Editor>\n" + "      <acceptsLineNumber>Yes</acceptsLineNumber>\n" + "      <parameter>expected_line_number_parameter</parameter>\n" + "    </Local>\n" + "  </Web>\n" + "</Web>"; xmlReader.parse(new InputSource(new StringReader(xmlString))); String actualFileEditor = SAXDirParser.getEditor(); assertEquals(expectedFileEditor, actualFileEditor); }

 @Test public void testAcceptsLineNumber() throws Exception { XMLReader xmlReader = XMLReaderFactory.createXMLReader(); xmlReader.setContentHandler(parser); String xmlString = "<Web>\n" + "  <Local>\n" + "    <Names>\n" + "      <pkg name=\"java.lang\">java.lang</pkg>\n" + "      <pkg name=\"javax.swing.border\">javax.swing.border</pkg>\n" + "    </Names>\n" + "    <Local>\n" + "      <BrowserLoc>No</BrowserLoc>\n" + "      <Editor>expected_file_editor</Editor>\n" + "      <acceptsLineNumber>Yes</acceptsLineNumber>\n" + "      <parameter>expected_line_number_parameter</parameter>\n" + "    </Local>\n" + "  </Web>\n" + "</Web>"; xmlReader.parse(new InputSource(new StringReader(xmlString))); boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber(); assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber); }

 @Test public void testLineNumberParameter() throws Exception { XMLReader xmlReader = XMLReaderFactory.createXMLReader(); xmlReader.setContentHandler(parser); String xmlString = "<Web>\n" + "  <Local>\n" + "    <Names>\n" + "      <pkg name=\"java.lang\">java.lang</pkg>\n" + "      <pkg name=\"javax.swing.border\">javax.swing.border</pkg>\n" + "    </Names>\n" + "    <Local>\n" + "      <BrowserLoc>No</BrowserLoc>\n" + "      <Editor>expected_file_editor</Editor>\n" + "      <acceptsLineNumber>Yes</acceptsLineNumber>\n" + "      <parameter>expected_line_number_parameter</parameter>\n" + "    </Local>\n" + "  </Web>\n" + "</Web>"; xmlReader.parse(new InputSource(new StringReader(xmlString))); String actualLineNumberParameter = SAXDirParser.lineNumberParameter(); assertEquals(expectedLineNumberParameter, actualLineNumberParameter); }

}