package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringList;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private XMLReader xmlReader;
 private Map<String, String> expectedDirectory;
 private StringList expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedLineNumAccepted;
 private String expectedLineNumParam;

 @Before
 public void setUp() {
 saxDirParser = new SAXDirParser();
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

 @Test
 public void testParseXML() throws Exception {
 File xmlFile = new File("packagedirectory.xml");
 xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);

 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 String actualEditor = SAXDirParser.getEditor();
 boolean actualLineNumAccepted = SAXDirParser.acceptsLineNumber();
 String actualLineNumParam = SAXDirParser.lineNumberParameter();

 assertEquals(expectedDirectory, actualDirectory);
 assertEquals(expectedLocalPackages, actualLocalPackages);
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 assertEquals(expectedEditor, actualEditor);
 assertEquals(expectedLineNumAccepted, actualLineNumAccepted);
 assertEquals(expectedLineNumParam, actualLineNumParam);
 }

 @Test(expected = SAXException.class)
 public void testParseXMLWithException() throws Exception {
 File xmlFile = new File("nonexistentfile.xml");
 xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);
 fail("Expected SAXException but it did not occur.");
 }

}