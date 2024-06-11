package com.jstevh.viewer;

import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringList;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private static Map<String, String> expectedDirectory;
 private static StringList expectedLocalPackages;
 private static String expectedLocalBrowser;
 private static String expectedEditor;
 private static boolean expectedAcceptsLineNumber;
 private static String expectedLineNumParam;

 @Before
 public void setUp() {
 saxDirParser = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "/path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "/path/to/javax.swing.border");
 expectedLocalPackages = new StringList();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3094.110 Safari/537.36";
 expectedEditor = "Notepad++";
 expectedAcceptsLineNumber = true;
 expectedLineNumParam = "true";
 }

 @Test
 public void testStartElement() throws Exception {
 saxDirParser.parse("src/test/resources/packagedirectory.xml");

 // Test 'BrowserLoc' element
 assertEquals(expectedLocalBrowser, SAXDirParser.getLocalBrowser());

 // Test 'Editor' element
 assertEquals(expectedEditor, SAXDirParser.getEditor());

 // Test 'acceptsLineNumber' element
 assertTrue(SAXDirParser.acceptsLineNumber());
 assertEquals(expectedLineNumParam, SAXDirParser.lineNumberParameter());

 // Test 'Web' element
 assertEquals(expectedDirectory.get("java.lang"), SAXDirParser.getDirectory().get("java.lang"));
 assertEquals(expectedDirectory.get("javax.swing.border"), SAXDirParser.getDirectory().get("javax.swing.border"));

 // Test 'Local' element
 assertTrue(SAXDirParser.getLocalPackages().containsAll(expectedLocalPackages));
 }

 @Test(expected = Exception.class)
 public void testInvalidXml() {
 saxDirParser.parse("src/test/resources/invalidxml.xml");
 fail("Exception expected but not thrown");
 }
}