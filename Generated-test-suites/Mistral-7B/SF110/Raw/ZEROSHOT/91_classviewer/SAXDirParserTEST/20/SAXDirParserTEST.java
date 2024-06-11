package com.jstevh.viewer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.StringList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private Map<String, String> expectedDirectory;
 private StringList expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedLineNumAccepted;
 private String expectedLineNumParam;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "expected_location_java_lang");
 expectedDirectory.put("javax.swing.border", "expected_location_javax_swing_border");
 expectedLocalPackages = new StringList();
 expectedLocalPackages.add("expected_package1");
 expectedLocalPackages.add("expected_package2");
 expectedLocalBrowser = "expected_local_browser";
 expectedEditor = "expected_editor";
 expectedLineNumAccepted = true;
 expectedLineNumParam = "expected_line_number_param";
 }

 @After
 public void tearDown() {
 parser = null;
 expectedDirectory = null;
 expectedLocalPackages = null;
 expectedLocalBrowser = null;
 expectedEditor = null;
 expectedLineNumAccepted = false;
 expectedLineNumParam = null;
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals("Size of the directories should be equal", expectedDirectory.size(), actualDirectory.size());
 for (Map.Entry<String, String> entry : expectedDirectory.entrySet()) {
 String key = entry.getKey();
 String expectedValue = entry.getValue();
 String actualValue = actualDirectory.get(key);
 assertEquals("Location of key: " + key + " should be equal", expectedValue, actualValue);
 }
 }

 @Test
 public void testGetLocalPackages() {
 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals("Size of the local packages should be equal", expectedLocalPackages.size(), actualLocalPackages.size());
 for (int i = 0; i < expectedLocalPackages.size(); i++) {
 String expectedPackage = expectedLocalPackages.get(i);
 String actualPackage = actualLocalPackages.get(i);
 assertEquals("Local package at index: " + i + " should be equal", expectedPackage, actualPackage);
 }
 assertNull("Local packages should be empty if null is returned", actualLocalPackages.get(expectedLocalPackages.size()));
 }

 @Test
 public void testGetLocalBrowser() {
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals("Local browser should be equal to the expected value", expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() {
 String actualEditor = parser.getEditor();
 assertEquals("Editor should be equal to the expected value", expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean actualLineNumAccepted = parser.acceptsLineNumber();
 assertEquals("Line number acceptance should be equal to the expected value", expectedLineNumAccepted, actualLineNumAccepted);
 }

 @Test
 public void testLineNumberParameter() {
 String actualLineNumParam = parser.lineNumberParameter();
 assertEquals("Line number parameter should be equal to the expected value", expectedLineNumParam, actualLineNumParam);
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullFile() {
 SAXParserFactory saxFactory = SAXParserFactory.newInstance();
 SAXParser myParser = saxFactory.newSAXParser();
 XMLReader myReader = myParser.getXMLReader();
 myReader.setContentHandler(parser);
 myReader.parse(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyFile() {
 parser.parse("");
 }

 @Test(expected = SAXException.class)
 public void testParseInvalidXml() {
 parser.parse("<invalidXml>");
 }
}