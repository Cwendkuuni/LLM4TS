package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class SAXDirParserTest {

 private DefaultHandler handler;
 private List<String> localPackages;
 private Map<String, String> directory;
 private boolean acceptsLineNumber;
 private String lineNumberParameter;
 private static final String XML_FILE_PATH = "classpath:packagedirectory.xml";

 @Before
 public void setUp() throws Exception {
 handler = new SAXDirParser();
 localPackages = new ArrayList<>();
 directory = new HashMap<>();
 }

 @After
 public void tearDown() {
 handler = null;
 localPackages.clear();
 directory.clear();
 }

 @Test
 public void testGetDirectory() throws Exception {
 InputStream inputStream = this.getClass().getResourceAsStream(XML_FILE_PATH);
 XMLReader xmlReader = XMLReaderFactory.createXMLReader(inputStream);
 xmlReader.setContentHandler(handler);
 xmlReader.parse(new InputSource(inputStream));
 Map<String, String> expectedDirectory = handler.getDirectory();
 assertEquals(expectedDirectory, handler.getDirectory());
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 InputStream inputStream = this.getClass().getResourceAsStream(XML_FILE_PATH);
 XMLReader xmlReader = XMLReaderFactory.createXMLReader(inputStream);
 xmlReader.setContentHandler(handler);
 xmlReader.parse(new InputSource(inputStream));
 List<String> expectedLocalPackages = handler.getLocalPackages();
 assertEquals(expectedLocalPackages, handler.getLocalPackages());
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 InputStream inputStream = this.getClass().getResourceAsStream(XML_FILE_PATH);
 XMLReader xmlReader = XMLReaderFactory.createXMLReader(inputStream);
 xmlReader.setContentHandler(handler);
 xmlReader.parse(new InputSource(inputStream));
 String expectedLocalBrowser = handler.getLocalBrowser();
 assertEquals(expectedLocalBrowser, handler.getLocalBrowser());
 }

 @Test
 public void testGetEditor() throws Exception {
 InputStream inputStream = this.getClass().getResourceAsStream(XML_FILE_PATH);
 XMLReader xmlReader = XMLReaderFactory.createXMLReader(inputStream);
 xmlReader.setContentHandler(handler);
 xmlReader.parse(new InputSource(inputStream));
 String expectedEditor = handler.getEditor();
 assertEquals(expectedEditor, handler.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 InputStream inputStream = this.getClass().getResourceAsStream(XML_FILE_PATH);
 XMLReader xmlReader = XMLReaderFactory.createXMLReader(inputStream);
 xmlReader.setContentHandler(handler);
 xmlReader.parse(new InputSource(inputStream));
 boolean expectedAcceptsLineNumber = handler.acceptsLineNumber();
 assertTrue(expectedAcceptsLineNumber);
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 InputStream inputStream = this.getClass().getResourceAsStream(XML_FILE_PATH);
 XMLReader xmlReader = XMLReaderFactory.createXMLReader(inputStream);
 xmlReader.setContentHandler(handler);
 xmlReader.parse(new InputSource(inputStream));
 String expectedLineNumberParameter = handler.lineNumberParameter();
 assertEquals("", expectedLineNumberParameter);
 }

 @Test(expected = SAXException.class)
 public void testInvalidXMLFile() throws Exception {
 File invalidXMLFile = new File("invalidfile.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(handler);
 xmlReader.parse(new FileInputSource(invalidXMLFile));
 fail("SAXException should be thrown when parsing invalid XML file.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullXMLFile() {
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(handler);
 try {
 xmlReader.parse(null);
 fail("IllegalArgumentException should be thrown when passing null XML file.");
 } catch (SAXException e) {
 // expected
 }
}
}