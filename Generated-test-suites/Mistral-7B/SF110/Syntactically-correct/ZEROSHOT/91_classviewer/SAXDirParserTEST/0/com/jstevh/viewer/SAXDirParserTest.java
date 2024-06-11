package com.jstevh.viewer;

import static org.junit.Assert.*;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.ArrayList;

public class SAXDirParserTest {

 private static final String XML_DATA = "<Web>\n" +
                                        "  <Local>\n" +
                                        "    <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                                        "    <parameter>23</parameter>\n" +
                                        "  </Local>\n" +
                                        "  <Group>\n" +
                                        "    <Names>\n" +
                                        "      <pkg>java.lang</pkg>\n" +
                                        "      <pkg>javax.swing.border</pkg>\n" +
                                        "    </Names>\n" +
                                        "  </Group>\n" +
                                        "  <Web>\n" +
                                        "    <Local>\n" +
                                        "      <Editor>Eclipse</Editor>\n" +
                                        "    </Local>\n" +
                                        "    <Local>\n" +
                                        "      <acceptsLineNumber>No</acceptsLineNumber>\n" +
                                        "    </Local>\n" +
                                        "    <Names>\n" +
                                        "      <pkg>java.util</pkg>\n" +
                                        "      <pkg>java.awt</pkg>\n" +
                                        "    </Names>\n" +
                                        "  </Web>\n" +
                                        "  <Local>\n" +
                                        "    <Editor>IntelliJ</Editor>\n" +
                                        "  </Local>\n" +
                                        "</Web>";

 @SuppressWarnings("unchecked")
 @org.junit.Test
 public void testSAXDirParser() throws Exception {
 SAXDirParser parser = new SAXDirParser();
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(parser);
 StringReader reader = new StringReader(XML_DATA);
 xmlReader.parse(reader);

 // Test getDirectory()
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "testDir1/java.lang");
 expectedDirectory.put("javax.swing.border", "testDir1/javax.swing.border");
 expectedDirectory.put("java.util", "testDir2/java.util");
 expectedDirectory.put("java.awt", "testDir2/java.awt");
 Map<String, String> actualDirectory = parser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);

 // Test getLocalPackages()
 StringList expectedLocalPackages = new ArrayList<>();
 expectedLocalPackages.add("java.lang");
 expectedLocalPackages.add("javax.swing.border");
 StringList actualLocalPackages = parser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);

 // Test getLocalBrowser()
 String expectedLocalBrowser = "Eclipse";
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
 String expectedLineNumberParameter = "23";
 String actualLineNumberParameter = parser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 static class StringList extends ArrayList<String> {
 @Override
 public String toString() {
 StringBuilder sb = new StringBuilder();
 for (String s : this) {
 if (sb.length() > 0) {
 sb.append(", ");
 }
 sb.append(s);
 }
 return sb.toString();
 }
 }
}