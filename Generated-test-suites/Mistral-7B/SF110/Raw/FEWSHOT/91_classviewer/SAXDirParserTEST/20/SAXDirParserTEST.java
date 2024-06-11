package com.jstevh.viewer.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private List<String> packages;

 @Test
 public void testSAXDirParser() throws Exception {
 parser = new SAXDirParser();
 }

 @Test
 public void testGetWebData() {
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "BrowserLoc", "BrowserLoc", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(0, parser.getLevel());
 }

 @Test
 public void testStartElement_Editor() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "Editor", "Editor", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(10, parser.getLevel());
 }

 @Test
 public void testStartElement_acceptsLineNumber() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "acceptsLineNumber", "acceptsLineNumber", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(11, parser.getLevel());
 }

 @Test
 public void testStartElement_parameter() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "parameter", "parameter", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(12, parser.getLevel());
 }

 @Test
 public void testStartElement_Group() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "Group", "Group", new AttributesImpl());
 assertFalse(parser.isCheck());
 }

 @Test
 public void testStartElement_Web() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "Web", "Web", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(30, parser.getLevel());
 }

 @Test
 public void testStartElement_Local() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "Local", "Local", new AttributesImpl());
 assertTrue(parser.isCheck());
 assertEquals(31, parser.getLevel());
 }

 @Test
 public void testStartElement_Names() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "Names", "Names", new AttributesImpl());
 assertTrue(parser.isCheck());
 }

 @Test
 public void testStartElement_pkg() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "pkg", "pkg", new AttributesImpl());
 assertEquals(4, parser.getLevel());
 }

 @Test
 public void testCharacters_java_lang() throws Exception {
 parser = new SAXDirParser();
 parser.characters(new char[]{'j', 'a', 'v', 'a', '.', 'l', 'a', 'n', 'g', '.'}, 0, 9);
 parser.check = true;
 parser.level = 4;
 assertEquals("java.lang", parser.location);
 }

 @Test
 public void testCharacters_javax_swing_border() throws Exception {
 parser = new SAXDirParser();
 parser.characters(new char[]{'j', 'e', 'r', 's', '.', 's', 'w', 'i', 'n', 'g', '.', 'b', 'o', 'r', 'd', 'e', 'r'}, 0, 19);
 parser.check = true;
 parser.level = 4;
 assertEquals("javax.swing.border", parser.location);
 }

 @Test
 public void testCharacters_localBrowser() throws Exception {
 parser = new SAXDirParser();
 parser.characters(new char[]{'M', 'o', 'z', 'i', 'l', 'l', 'a', ' ', 'F', 'i', 'r', 'e', 'f', 'o', 'x'}, 0, 13);
 parser.check = true;
 parser.level = 0;
 assertEquals("Firefox", parser.localBrowser);
 }

 @Test
 public void testCharacters_editor() throws Exception {
 parser = new SAXDirParser();
 parser.characters(new char[]{'E', 'm', 'e', 'd', 'i', 't', 'o', 'r'}, 0, 7);
 parser.check = true;
 parser.level = 10;
 assertEquals("Editor", parser.file_editor);
 }

 @Test
 public void testCharacters_acceptLineNumber_Yes() throws Exception {
 parser = new SAXDirParser();
 parser.characters(new char[]{'Y', 'e', 's'}, 0, 3);
 parser.check = true;
 parser.level = 11;
 assertTrue(parser.lineNumAccepted);
 }

 @Test
 public void testCharacters_acceptLineNumber_No() throws Exception {
 parser = new SAXDirParser();
 parser.characters(new char[]{'N', 'o'}, 0, 2);
 parser.check = true;
 parser.level = 11;
 assertFalse(parser.lineNumAccepted);
 }

 @Test
 public void testCharacters_lineNumberParameter() throws Exception {
 parser = new SAXDirParser();
 parser.characters(new char[]{'4', '2'}, 0, 2);
 parser.check = true;
 parser.level = 12;
 assertEquals("42", parser.lineNumParam);
 }

 @Test
 public void testEndElement_Group() throws Exception {
 parser = new SAXDirParser();
 parser.startElement("", "Group", "Group", new AttributesImpl());
 parser.endElement("", "Group", "Group");
 assertFalse(parser.isCheck());
 }

 @Test
 public void testGetDirectory() throws Exception {
 parser = new SAXDirParser();
 parser.parseDirectory("testdata/packagedirectory.xml");
 HashMap<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "test/java/lang");
 expectedDirectory.put("javax.swing.border", "test/javax/swing/border");
 assertEquals(expectedDirectory, SAXDirParser.getDirectory());
 }

 @Test
 public void testGetLocalPackages() throws Exception {
 parser = new SAXDirParser();
 parser.parseDirectory("testdata/packagedirectory.xml");
 List<String> expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("com.test");
 expectedLocalPackages.add("org.test");
 assertNotNull(SAXDirParser.getLocalPackages());
 assertEquals(expectedLocalPackages, SAXDirParser.getLocalPackages());
 }

 @Test
 public void testGetLocalBrowser() throws Exception {
 parser = new SAXDirParser();
 parser.parseDirectory("testdata/packagedirectory.xml");
 assertEquals("Firefox", SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() throws Exception {
 parser = new SAXDirParser();
 parser.parseDirectory("testdata/packagedirectory.xml");
 assertEquals("Editor", SAXDirParser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() throws Exception {
 parser = new SAXDirParser();
 parser.parseDirectory("testdata/packagedirectory.xml");
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() throws Exception {
 parser = new SAXDirParser();
 parser.parseDirectory("testdata/packagedirectory.xml");
 assertEquals("42", SAXDirParser.lineNumberParameter());
 }

 @Test
 public void testParseDirectory() throws Exception {
 parser = new SAXDirParser();
 parser.parseDirectory("testdata/packagedirectory.xml");
 }

}