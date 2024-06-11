package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringList;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private static Map<String, String> expectedDirectory;
 private static StringList expectedLocalPackages;
 private static String expectedLocalBrowser;
 private static String expectedEditor;
 private static boolean expectedAcceptsLineNumber;
 private static String expectedLineNumberParameter;

 @BeforeClass
 public static void setUp() {
 expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "/java/lang/");
 expectedDirectory.put("javax.swing.border", "/javax/swing/border/");
 expectedLocalPackages = new StringList();
 expectedLocalPackages.add("com.local");
 expectedLocalPackages.add("com.jstevh.local");
 expectedLocalBrowser = "Firefox";
 expectedEditor = "Notepad";
 expectedAcceptsLineNumber = true;
 expectedLineNumberParameter = "123";
 }

 @Before
 public void init() {
 saxDirParser = new SAXDirParser();
 }

 @Test
 public void testStartElement() throws SAXException, IOException {
 File xmlFile = new File("packagedirectory.xml");
 XMLReader xmlReader = XMLReaderFactory.createXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 xmlReader.parse(xmlFile);

 // Test BrowserLoc element
 assertEquals("Firefox", saxDirParser.getWebData());

 // Test Editor element
 assertEquals("Notepad", saxDirParser.getWebData());
 // Test acceptsLineNumber element
 assertTrue(saxDirParser.acceptsLineNumber());
 // Test parameter element
 assertEquals("123", saxDirParser.lineNumberParameter());

 // Test Web element
 assertEquals("/", saxDirParser.getWebData());
 // Test Local element
 assertEquals("", saxDirParser.getWebData());

 // Test Group element
 assertNull(saxDirParser.getWebData());
 }

 @Test
 public void testGetMethods() {
 // Test getDirectory method
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(2, actualDirectory.size());
 assertTrue(actualDirectory.containsKey("java.lang"));
 assertTrue(actualDirectory.containsKey("javax.swing.border"));
 assertEquals("/java/lang/", actualDirectory.get("java.lang"));
 assertEquals("/javax/swing/border/", actualDirectory.get("javax.swing.border"));

 // Test getLocalPackages method
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(2, actualLocalPackages.size());
 assertEquals("com.local", actualLocalPackages.get(0));
 assertEquals("com.jstevh.local", actualLocalPackages.get(1));

 // Test getLocalBrowser method
 assertEquals("Firefox", SAXDirParser.getLocalBrowser());

 // Test getEditor method
 assertEquals("Notepad", SAXDirParser.getEditor());

 // Test acceptsLineNumber method
 assertTrue(SAXDirParser.acceptsLineNumber());

 // Test lineNumberParameter method
 assertEquals("123", SAXDirParser.lineNumberParameter());
 }
}