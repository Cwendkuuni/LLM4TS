package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.jstevh.viewer.*;
import com.jstevh.tools.*;
import java.util.*;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private XMLReader xmlReader;

 @Before
 public void setUp() {
 saxDirParser = new SAXDirParser();
 xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 }

 @Test
 public void testGetWebData() {
 SAXDirParser saxDirParser = new SAXDirParser();
 assertEquals("", saxDirParser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() {
 xmlReader.parse("src/test/resources/SAXTestCases/browserLoc.xml");
 assertTrue(saxDirParser.isCheck());
 assertEquals(0, saxDirParser.getLevel());
 assertNotNull(saxDirParser.getLocalBrowser());
 }

 @Test
 public void testStartElement_Editor() {
 xmlReader.parse("src/test/resources/SAXTestCases/editor.xml");
 assertTrue(saxDirParser.isCheck());
 assertEquals(10, saxDirParser.getLevel());
 assertNotNull(saxDirParser.getFile_editor());
 }

 @Test
 public void testStartElement_acceptsLineNumber() {
 xmlReader.parse("src/test/resources/SAXTestCases/acceptsLineNumber.xml");
 assertTrue(saxDirParser.isCheck());
 assertEquals(11, saxDirParser.getLevel());
 assertTrue(saxDirParser.isLineNumAccepted());
 }

 @Test
 public void testStartElement_parameter() {
 xmlReader.parse("src/test/resources/SAXTestCases/parameter.xml");
 assertTrue(saxDirParser.isCheck());
 assertEquals(12, saxDirParser.getLevel());
 assertEquals("value", saxDirParser.getLineNumParam());
 }

 @Test
 public void testStartElement_Group() {
 xmlReader.parse("src/test/resources/SAXTestCases/group.xml");
 assertFalse(saxDirParser.isCheck());
 }

 @Test
 public void testStartElement_Web() {
 xmlReader.parse("src/test/resources/SAXTestCases/web.xml");
 assertTrue(saxDirParser.isCheck());
 assertEquals(30, saxDirParser.getLevel());
 assertNotNull(saxDirParser.getLocation());
 }

 @Test
 public void testStartElement_Local() {
 xmlReader.parse("src/test/resources/SAXTestCases/local.xml");
 assertTrue(saxDirParser.isCheck());
 assertEquals(31, saxDirParser.getLevel());
 assertFalse(saxDirParser.isLocal());
 }

 @Test
 public void testStartElement_Names() {
 xmlReader.parse("src/test/resources/SAXTestCases/names.xml");
 assertTrue(saxDirParser.isCheck());
 }

 @Test
 public void testStartElement_pkg() {
 xmlReader.parse("src/test/resources/SAXTestCases/pkg.xml");
 assertTrue(saxDirParser.isCheck());
 assertEquals(4, saxDirParser.getLevel());
 assertEquals("com.jstevh.viewer", saxDirParser.getDirectory().get("com.jstevh.viewer"));
 assertTrue(saxDirParser.isLocal());
 assertEquals(1, saxDirParser.getLocalPackages().size());
 assertEquals("com.jstevh.viewer", saxDirParser.getLocalPackages().get(0));
 }

 @Test(expected = SAXException.class)
 public void testStartElement_InvalidTag() {
 xmlReader.parse("src/test/resources/SAXTestCases/invalidTag.xml");
 fail("SAXException should be thrown");
 }

 @Test
 public void testGetDirectory() {
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "java.lang");
 expectedDirectory.put("javax.swing.border", "javax.swing.border");
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testGetLocalPackages() {
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("com.jstevh.viewer");
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 assertEquals("", SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() {
 assertEquals("", SAXDirParser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() {
 assertEquals(false, SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 assertEquals("", SAXDirParser.lineNumberParameter());
 }

 @Test
 public void testMain() {
 try {
 SAXDirParser.main(new String[0]);
 fail("SAXParserException should be thrown in main method");
 } catch (Exception e) {
 // expected
 }
}

 @After
 public void tearDown() {
 saxDirParser = null;
 xmlReader = null;
 }
}