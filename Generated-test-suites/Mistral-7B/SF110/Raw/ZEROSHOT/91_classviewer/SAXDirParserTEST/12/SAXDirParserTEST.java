package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private XMLReader reader;

 @Before
 public void setUp() throws Exception {
 parser = new SAXDirParser();
 reader = XMLReaderFactory.createXMLReader();
 reader.setContentHandler(parser);
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "test_java_lang");
 expectedDirectory.put("javax.swing.border", "test_javax_swing_border");
 reader.parse(new File("src/test/resources/packagedirectory.xml"));
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 String[] expectedPackages = new String[]{"package1", "package2"};
 reader.parse(new File("src/test/resources/packagedirectory.xml"));
 StringList actualPackages = SAXDirParser.getLocalPackages();
 assertArrayEquals(expectedPackages, actualPackages.toArray(new String[0]));
 }

 @Test
 public void testGetLocalBrowser() {
 String expectedLocalBrowser = "test_local_browser";
 reader.parse(new File("src/test/resources/packagedirectory.xml"));
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);
 }

 @Test
 public void testGetEditor() {
 String expectedEditor = "test_editor";
 reader.parse(new File("src/test/resources/packagedirectory.xml"));
 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);
 }

 @Test
 public void testAcceptsLineNumber() {
 boolean expectedAcceptsLineNumber = true;
 reader.parse(new File("src/test/resources/packagedirectory_acceptsLineNumber.xml"));
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() {
 String expectedLineNumberParameter = "test_line_number_parameter";
 reader.parse(new File("src/test/resources/packagedirectory_lineNumberParameter.xml"));
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test(expected = SAXException.class)
 public void testInvalidFile() throws IOException, SAXException {
 reader.parse(new File("src/test/resources/invalid_file.xml"));
 fail("Expected SAXException but it did not occur.");
 }
}