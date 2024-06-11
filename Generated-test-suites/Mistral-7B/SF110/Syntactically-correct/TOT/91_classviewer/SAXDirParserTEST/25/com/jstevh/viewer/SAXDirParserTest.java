package com.jstevh.viewer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private Map<String, String> expectedDirectory;
 private StringList expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedAcceptLineNumber;
 private String expectedLineNumberParam;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 saxDirParser = new SAXDirParser();
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "location1");
 expectedDirectory.put("javax.swing.border", "location2");
 expectedLocalPackages = new StringList();
 expectedLocalPackages.add("package1");
 expectedLocalPackages.add("package2");
 expectedLocalBrowser = "localBrowser";
 expectedEditor = "editor";
 expectedAcceptLineNumber = true;
 expectedLineNumberParam = "lineNumberParam";
 }

 @Test
 public void testGetDirectory() throws ParserConfigurationException, SAXException, IOException {
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
 public void testGetLocalPackages() {
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testEditor() {
 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean actualAcceptLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptLineNumber, actualAcceptLineNumber);
 }

 @Test
 public void testLineNumberParameter() {
 String actualLineNumberParam = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParam, actualLineNumberParam);
 }

 @Test(expected = NullPointerException.class)
 public void testGetDirectoryWhenDirectoryIsEmpty() {
 SAXDirParser.getDirectory();
 }

 @Test(expected = NullPointerException.class)
 public void testGetLocalPackagesWhenLocalPackagesIsEmpty() {
 SAXDirParser.getLocalPackages();
 }

}