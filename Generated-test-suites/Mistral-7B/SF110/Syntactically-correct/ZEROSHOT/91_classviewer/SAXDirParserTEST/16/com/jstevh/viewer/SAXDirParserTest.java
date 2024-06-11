package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SAXDirParserTest {

 private static final String XML_FILE = "path/to/your/packagedirectory.xml";

 @org.junit.BeforeClass
 static public void setup() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 XMLReader reader = XMLReaderFactory.createXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new File(XML_FILE));
 }

 @org.junit.Test
 public void testGetDirectory() {
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "expected_location_for_java.lang");
 expectedDirectory.put("javax.swing.border", "expected_location_for_javax.swing.border");
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @org.junit.Test
 public void testGetLocalPackages() {
 List<String> expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("expected_package_name_1");
 expectedLocalPackages.add("expected_package_name_2");
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @org.junit.Test
 public void testGetLocalBrowser() {
 String expectedLocalBrowser = "expected_local_browser";
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @org.junit.Test
 public void testGetEditor() {
 String expectedEditor = "expected_editor";
 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @org.junit.Test
 public void testAcceptsLineNumber() {
 boolean expectedLineNumAccepted = true; // or false
 boolean actualLineNumAccepted = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedLineNumAccepted, actualLineNumAccepted);
 }

 @org.junit.Test
 public void testLineNumberParameter() {
 String expectedLineNumberParam = "expected_line_number_parameter";
 String actualLineNumberParam = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParam, actualLineNumberParam);
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testGetDirectoryNull() {
 SAXDirParser parser = new SAXDirParser();
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertNull(actualDirectory);
 }
}