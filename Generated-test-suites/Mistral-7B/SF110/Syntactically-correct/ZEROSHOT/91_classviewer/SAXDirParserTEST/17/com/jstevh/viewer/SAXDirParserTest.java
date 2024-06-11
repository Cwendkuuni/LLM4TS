package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;
import org.junit.Test;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class SAXDirParserTest {

 private static final String XML_DATA = "<Web>\n" +
                                          "  <Local>\n" +
                                          "    <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                                          "    <parameter>123</parameter>\n" +
                                          "  </Local>\n" +
                                          "  <Group name=\"Java\">\n" +
                                          "    <Names>\n" +
                                          "      <pkg>java.lang</pkg>\n" +
                                          "      <pkg>java.util</pkg>\n" +
                                          "    </Names>\n" +
                                          "    <Names>\n" +
                                          "      <pkg>javax.swing.border</pkg>\n" +
                                          "    </Names>\n" +
                                          "  </Group>\n" +
                                          "  <BrowserLoc>Chrome</BrowserLoc>\n" +
                                          "  <Editor>Eclipse</Editor>\n" +
                                          "  <Local>\n" +
                                          "    <acceptsLineNumber>No</acceptsLineNumber>\n" +
                                          "  </Local>\n" +
                                          "  <Web>\n" +
                                          "    <Local>\n" +
                                          "      <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                                          "      <parameter>456</parameter>\n" +
                                          "    </Local>\n" +
                                          "    <Names>\n" +
                                          "      <pkg>com.jstevh.viewer</pkg>\n" +
                                          "    </Names>\n" +
                                          "  </Web>\n" +
                                          "</Web>";

 @Test
 public void testSAXDirParser() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 StringReader stringReader = new StringReader(XML_DATA);
 xmlReader.setContentHandler(parser);
 xmlReader.parse(stringReader);

 // Test getDirectory()
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "test directory, test gives java.lang");
 expectedDirectory.put("javax.swing.border", "test directory, test gives javax.swing.border");
 expectedDirectory.put("com.jstevh.viewer", "test directory, test gives com.jstevh.viewer");
 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);

 // Test getLocalPackages()
 ArrayList<String> expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("java.lang");
 expectedLocalPackages.add("javax.swing.border");
 expectedLocalPackages.add("com.jstevh.viewer");
 ArrayList<String> actualLocalPackages = (ArrayList<String>) parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);

 // Test getLocalBrowser()
 String expectedLocalBrowser = "Chrome";
 String actualLocalBrowser = parser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);

 // Test getEditor()
 String expectedEditor = "Eclipse";
 String actualEditor = parser.getEditor();
 assertEquals(expectedEditor, actualEditor);

 // Test acceptsLineNumber()
 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);

 // Test lineNumberParameter()
 String expectedLineNumberParameter = "123";
 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);

 // Test acceptsLineNumber() with false
 parser = new SAXDirParser();
 xmlReader = XMLReaderFactory.createXMLReader();
 stringReader = new StringReader(XML_DATA);
 xmlReader.setContentHandler(parser);
 xmlReader.parse(stringReader);
 expectedAcceptsLineNumber = false;
 actualAcceptsLineNumber = parser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);

 // Test lineNumberParameter() with empty string
 parser = new SAXDirParser();
 xmlReader = XMLReaderFactory.createXMLReader();
 stringReader = new StringReader(XML_DATA.replaceAll("parameter>[0-9]+</parameter>", "parameter></parameter>"));
 xmlReader.setContentHandler(parser);
 xmlReader.parse(stringReader);
 expectedLineNumberParameter = "";
 actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }
}