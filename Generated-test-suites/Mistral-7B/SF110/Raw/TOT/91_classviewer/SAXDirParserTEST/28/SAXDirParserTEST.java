package com.jstevh.viewer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class SAXDirParserTest extends TestCase {

 private SAXDirParser parser;

 @Before
 public void setUp() throws Exception {
 parser = new SAXDirParser();
 }

 @Test
 public void testGetDirectory() throws Exception {
 InputStream input = new File("src/test/resources/packagedirectory.xml").getInputStream();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(input);

 Map<String, String> expected = new HashMap<>();
 expected.put("java.lang", "someLocation/java.lang");
 expected.put("javax.swing.border", "someLocation/javax.swing.border");

 assertEquals(expected, SAXDirParser.getDirectory());
 }

 @Test
 public void testGetDirectory_EmptyFile() throws Exception {
 InputStream input = new File("src/test/resources/empty.xml").getInputStream();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(input);

 assertTrue(SAXDirParser.getDirectory().isEmpty());
 }

 @Test
 public void testGetDirectory_MissingFile() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new InputStream[]{});

 assertTrue(SAXDirParser.getDirectory().isEmpty());
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 InputStream input = new File("src/test/resources/packagedirectory.xml").getInputStream();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(input);

 StringList expected = new StringList();
 expected.add("java.lang");
 expected.add("javax.swing.border");

 assertEquals(expected, SAXDirParser.getLocalPackages());
 }

 @Test
 public void testGetLocalPackages_EmptyFile() throws Exception {
 InputStream input = new File("src/test/resources/empty.xml").getInputStream();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(input);

 assertTrue(SAXDirParser.getLocalPackages().isEmpty());
 }

 @Test
 public void testGetLocalPackages_MissingFile() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new InputStream[]{});

 assertTrue(SAXDirParser.getLocalPackages().isEmpty());
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 InputStream input = new File("src/test/resources/packagedirectory.xml").getInputStream();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(input);

 assertEquals("someLocation/Firefox", SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testGetLocalBrowser_EmptyFile() throws Exception {
 InputStream input = new File("src/test/resources/empty.xml").getInputStream();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(input);

 assertEquals("", SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testGetLocalBrowser_MissingFile() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new InputStream[]{});

 assertEquals("", SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 InputStream input = new File("src/test/resources/packagedirectory.xml").getInputStream();SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(input);

 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testAcceptsLineNumber_EmptyFile() throws Exception {
 InputStream input = new File("src/test/resources/empty.xml").getInputStream();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(input);

 assertFalse(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testAcceptsLineNumber_MissingFile() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new InputStream[]{});

 assertFalse(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 InputStream input = new File("src/test/resources/packagedirectory.xml").getInputStream();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(input);

 assertEquals("123", SAXDirParser.lineNumberParameter());
 }

 @Test
 public void testLineNumberParameter_EmptyFile() throws Exception {
 InputStream input = new File("src/test/resources/empty.xml").getInputStream();
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(input);

 assertEquals("", SAXDirParser.lineNumberParameter());
 }

 @Test
 public void testLineNumberParameter_MissingFile() throws Exception {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 XMLReader reader = factory.newSAXParser().getXMLReader();
 reader.setContentHandler(parser);
 reader.parse(new InputStream[]{});

 assertEquals("", SAXDirParser.lineNumberParameter());
 }
}