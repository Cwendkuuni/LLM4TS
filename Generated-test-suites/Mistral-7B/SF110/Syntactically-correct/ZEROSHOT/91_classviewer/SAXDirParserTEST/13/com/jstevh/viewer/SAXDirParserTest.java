package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.AttributesImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private Map<String, String> expectedDirectory;
 private List<String> expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedLineNumAccepted;
 private String expectedLineNumParam;

 @Before
 public void setUp() {
 parser = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "/path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "/path/to/javax.swing.border");
 expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 expectedLocalBrowser = "Firefox";
 expectedEditor = "Notepad++";
 expectedLineNumAccepted = true;
 expectedLineNumParam = "5";
 }

 @Test
 public void testStartElementBrowserLoc() {
 parser.check = true;
 parser.startElement("", "BrowserLoc", "BrowserLoc", new AttributesImpl());
 assertEquals("Firefox", parser.localBrowser);
 parser.check = false;
 }

 @Test
 public void testStartElementEditor() {
 parser.check = true;
 parser.startElement("", "Editor", "Editor", new AttributesImpl());
 assertEquals("Notepad++", parser.file_editor);
 parser.check = false;
 }

 @Test
 public void testStartElementAcceptsLineNumber() {
 parser.check = true;
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", new AttributesImpl());
 assertEquals(expectedLineNumAccepted, parser.lineNumAccepted);
 parser.check = false;
 }

 @Test
 public void testStartElementLineNumberParameter() {
 parser.check = true;
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 parser.characters(new char[]{'5'}, 0, 1);
 assertEquals("5", parser.lineNumParam);
 parser.check = false;
 }

 @Test
 public void testStartElementWeb() {
 parser.check = true;
 parser.startElement("", "Web", "Web", new AttributesImpl());
 assertEquals("/", parser.location);
 parser.check = false;
 }

 @Test
 public void testStartElementLocal() {
 parser.check = true;
 parser.startElement("", "Local", "Local", new AttributesImpl());
 parser.check = false;
 parser.local = true;
 assertTrue(parser.local);
 }

 @Test
 public void testStartElementLocalNames() {
 parser.check = true;
 parser.startElement("", "Names", "Names", new AttributesImpl());
 parser.startElement("", "pkg", "pkg", new AttributesImpl());
 parser.characters(new char[]{'package1'.charAt(0)}, 0, 1);
 assertEquals("package1", parser.location);
 parser.check = false;
 }

 @Test
 public void testStartElementLocalpkg() {
 parser.check = true;
 parser.startElement("", "pkg", "pkg", new AttributesImpl());
 parser.characters(new char[]{'package2'.charAt(0)}, 0, 1);
 assertEquals("package2", parser.location);
 parser.check = false;
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 List<String> actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 assertEquals(expectedLocalBrowser, SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() {
 assertEquals(expectedEditor, SAXDirParser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() {
 assertEquals(expectedLineNumAccepted, SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 assertEquals(expectedLineNumParam, SAXDirParser.lineNumberParameter());
 }

}