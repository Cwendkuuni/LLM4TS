package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import com.jstevh.tools.StringList;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private HashMap<String, String> expectedDirectory;
 private StringList expectedLocalPackages;
 private String expectedLocalBrowser;
 private String expectedEditor;
 private boolean expectedAcceptsLineNumber;
 private String expectedLineNumberParam;

 @Before
 public void setUp() {
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "path/to/java.lang");
 expectedDirectory.put("javax.swing.border", "path/to/javax.swing.border");
 expectedLocalPackages = new StringList();
 expectedLocalPackages.add("com.local.package1");
 expectedLocalPackages.add("com.local.package2");
 expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3095.110 Safari/537.36";
 expectedEditor = "Notepad++";
 expectedAcceptsLineNumber = true;
 expectedLineNumberParam = "123";
 }

 @Test
 public void testSAXDirParser() throws ParserConfigurationException, SAXException, Exception {
 // Testing getDirectory() method
 parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse("packagedirectory.xml");
 HashMap<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);

 // Testing getLocalPackages() method
 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);

 // Testing getLocalBrowser() method
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);

 // Testing getEditor() method
 String actualEditor = parser.getEditor();
 assertEquals(expectedEditor, actualEditor);

 // Testing acceptsLineNumber() method
 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);

 // Testing lineNumberParameter() method
 String actualLineNumberParam = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParam, actualLineNumberParam);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidDirectoryEntry() throws ParserConfigurationException, SAXException, Exception {
 parser = new SAXDirParser();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse("<invalid_xml>"); // Invalid XML file
 HashMap<String, String> actualDirectory = parser.getDirectory();
 fail("Expected an AssertionError but got a HashMap instead.");
 }

 @Test(expected = NullPointerException.class)
 public void testNullDirectory() throws ParserConfigurationException, SAXException, Exception {
 parser = new SAXDirParser();
 parser.setDirectory(null);
 HashMap<String, String> actualDirectory = parser.getDirectory();
 fail("Expected a NullPointerException but got a HashMap instead.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyDirectory() throws ParserConfigurationException, SAXException, Exception {
 parser = new SAXDirParser();
 HashMap<String, String> actualDirectory = parser.getDirectory();
 assertTrue(actualDirectory.isEmpty());
 }
}