package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class SAXDirParserTest {

 private SAXDirParser parserUnderTest;
 private Map<String, String> expectedDirectory;
 private List<String> expectedLocalPackages;
 private String expectedLocalBrowser;
 private boolean expectedLineNumAccepted;
 private String expectedLineNumParam;

 @Before
 public void setUp() {
 parserUnderTest = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "testLocation1");
 expectedDirectory.put("javax.swing.border", "testLocation2");
 expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 expectedLocalBrowser = "Mozilla/5.0";
 expectedLineNumAccepted = true;
 expectedLineNumParam = "lineNumberParam";
 }

 @Test
 public void testStartElement() throws Exception {
 parserUnderTest = new SAXDirParser();
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parserUnderTest);
 File inputFile = new File("packagedirectory.xml");
 xmlReader.parse(inputFile);

 // Test for BrowserLoc element
 assertEquals("Mozilla/5.0", parserUnderTest.getWebData());

 // Test for Editor element
 assertNull(parserUnderTest.getEditor());

 // Test for acceptsLineNumber element
 assertTrue(parserUnderTest.acceptsLineNumber());

 // Test for parameter element
 assertEquals("lineNumberParam", parserUnderTest.lineNumberParameter());
 }

 @Test(expected = SAXException.class)
 public void testUnexpectedElement() throws Exception {
 parserUnderTest = new SAXDirParser();
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parserUnderTest);
 File inputFile = new File("packagedirectory.xml");
 xmlReader.parse(inputFile);

 // Test for unexpected element
 parserUnderTest.startElement("", "Unexpected", "unexpectedElement");
 fail("Unexpected exception not thrown");
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
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() {
 String actualEditor = SAXDirParser.getEditor();
 assertNull(actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean actualLineNumAccepted = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedLineNumAccepted, actualLineNumAccepted);
 }

 @Test
 public void testLineNumberParameter() {
 String actualLineNumParam = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumParam, actualLineNumParam);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectoryWhenEmpty() {
 SAXDirParser.getDirectory();
 fail("NullPointerException not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackagesWhenEmpty() {
 SAXDirParser.getLocalPackages();
 fail("NullPointerException not thrown");
 }
}