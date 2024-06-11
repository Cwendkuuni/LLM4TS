package com.jstevh.viewer;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private HashMap<String, String> directory;
 private List<String> localPackages;
 private String localBrowser;
 private String fileEditor;
 private boolean lineNumAccepted;
 private String lineNumParam;

 @Before
 public void setUp() {
 saxDirParser = new SAXDirParser();
 directory = new HashMap<String, String>();
 localPackages = new ArrayList<String>();
 }

 @Test
 public void testStartElement_BrowserLoc() {
 saxDirParser = new SAXDirParser();
 saxDirParser.startElement(null, "BrowserLoc", "BrowserLoc", new Attributes());
 assertTrue(saxDirParser.check);
 assertEquals(0, saxDirParser.level);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testStartElement_Editor() {
 saxDirParser = new SAXDirParser();
 saxDirParser.startElement(null, "Editor", "Editor", new Attributes());
 assertTrue(saxDirParser.check);
 assertEquals(10, saxDirParser.level);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testStartElement_acceptsLineNumber() {
 saxDirParser = new SAXDirParser();
 saxDirParser.startElement(null, "acceptsLineNumber", "acceptsLineNumber", new Attributes());
 assertTrue(saxDirParser.check);
 assertEquals(11, saxDirParser.level);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testStartElement_parameter() {
 saxDirParser = new SAXDirParser();
 saxDirParser.startElement(null, "parameter", "parameter", new Attributes());
 assertTrue(saxDirParser.check);
 assertEquals(12, saxDirParser.level);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testStartElement_Group() {
 saxDirParser = new SAXDirParser();
 saxDirParser.startElement(null, "Group", "Group", new Attributes());
 assertFalse(saxDirParser.check);
 assertEquals(-1, saxDirParser.level);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testStartElement_Web() {
 saxDirParser = new SAXDirParser();
 saxDirParser.startElement(null, "Web", "Web", new Attributes());
 assertTrue(saxDirParser.check);
 assertEquals(30, saxDirParser.level);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testStartElement_Local() {
 saxDirParser = new SAXDirParser();
 saxDirParser.startElement(null, "Local", "Local", new Attributes());
 assertTrue(saxDirParser.check);
 assertEquals(31, saxDirParser.level);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testStartElement_Names() {
 saxDirParser = new SAXDirParser();
 saxDirParser.startElement(null, "Names", "Names", new Attributes());
 assertFalse(saxDirParser.check);
 assertEquals(-1, saxDirParser.level);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testStartElement_pkg() {
 saxDirParser = new SAXDirParser();
 saxDirParser.startElement(null, "pkg", "pkg", new Attributes());
 assertTrue(saxDirParser.check);
 assertEquals(4, saxDirParser.level);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testCharacters_directory_JavaLang() {
 saxDirParser = new SAXDirParser();
 saxDirParser.characters(new char[]{'j', 'a', 'v', 'a', '.','l', 'a', 'n', 'g', '.'}, 0, 9);
 saxDirParser.characters(new char[]{'/', 'p', 'a', 'c', 'k', 'a', 'g', 'e', 's', '/'}, 0, 10);
 saxDirParser.characters(new char[]{'j', 'a', 'v', 'a', '.', 'l', 'a', 'n', 'g', '.'}, 0, 9);
 saxDirParser.characters(new char[]{'.'}, 0, 1);
 assertTrue(saxDirParser.check);
 assertEquals(30, saxDirParser.level);
 assertEquals("java.lang", directory.get("java.lang"));
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testCharacters_directory_JavaxSwingBorder() {
 saxDirParser = new SAXDirParser();
 saxDirParser.characters(new char[]{'j', 's', 'e', 'r', 'v', '.', 's', 'w', 'i', 'n', 'g', '.', 'b', 'o', 'r', 'd', 'e', 'r', '/'}, 0, 21);
 saxDirParser.characters(new char[]{'j', 's', 'e', 'r', 'v', '.', 's', 'w', 'i', 'n', 'g', '.', 'b', 'o', 'r', 'd', 'e', 'r', '/'}, 0, 21);
 saxDirParser.characters(new char[]{'j', 's', 'e', 'r', 'v', '.', 's', 'w', 'i', 'n', 'g', '.'}, 0, 9);
 saxDirParser.characters(new char[]{'.'}, 0, 1);
 saxDirParser.characters(new char[]{'j', 'a', 'v', 'a', '.', 'l', 'a', 'n', 'g', '.'}, 0, 9);
 saxDirParser.characters(new char[]{'.'}, 0, 1);
 saxDirParser.characters(new char[]{'j', 'a', 'v', 'a'}, 0, 4);
 assertTrue(saxDirParser.check);
 assertEquals(30, saxDirParser.level);
 assertEquals("javax.swing.border", directory.get("javax.swing.border"));
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testCharacters_localBrowser() {
 saxDirParser = new SAXDirParser();
 saxDirParser.characters(new char[]{'M', 'o', 'z', 'i', 'l', 'l', 'a', ' ', 'L', 'o', 'c', 'a', 'l', ' ', 'H', 't', 't', 'p', ':', '/', '/'}, 0, 24);
 saxDirParser.characters(new char[]{'M', 'o', 'z', 'i', 'l', 'l', 'a'}, 0, 7);
 saxDirParser.characters(new char[]{' ', 'L', 'o', 'c', 'a', 'l', ' '}, 0, 8);
 saxDirParser.characters(new char[]{'H', 't', 't', 'p', ':', '/', '/'}, 0, 8);
 saxDirParser.characters(new char[]{'/'}, 0, 1);
 saxDirParser.characters(new char[]{'D', 'o', 'c', 'u', 'm', 'e', 'n', 't', '/'}, 0, 9);
 saxDirParser.characters(new char[]{'S', 'a', 'm', 'p', 'l', 'e'}, 0, 6);
 saxDirParser.characters(new char[]{'.'}, 0, 1);
 saxDirParser.characters(new char[]{'x', 'm', 'l'}, 0, 3);
 assertTrue(saxDirParser.check);
 assertEquals(0, saxDirParser.level);
 assertEquals("Mozilla Local Https://Documentum/SAMPLE.xml", localBrowser);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testCharacters_fileEditor() {
 saxDirParser = new SAXDirParser();
 saxDirParser.characters(new char[]{'N', 'o', 't', 'e', 'd', 'p', 'a', 'd', ' ', 'E', 'd', 'i', 't', 'o', 'r', ' '}, 0, 15);
 saxDirParser.characters(new char[]{'E', 'x', 't', 'e', ' ', 'J', 'a', 'v', 'a', ' ', 'J', 'a', 'v', 'a'}, 0, 13);
 saxDirParser.characters(new char[]{'.'}, 0, 1);
 saxDirParser.characters(new char[]{'a'}, 0, 1);
 saxDirParser.characters(new char[]{'v'}, 0, 1);
 saxDirParser.characters(new char[]{'j'}, 0, 1);
 saxDirParser.characters(new char[]{'a'}, 0, 1);
 saxDirParser.characters(new char[]{'.'}, 0, 1);
 saxDirParser.characters(new char[]{'j', 'a', 'v', 'a'}, 0, 4);
 saxDirParser.characters(new char[]{'.'}, 0, 1);
 saxDirParser.characters(new char[]{'j', 'a', 'v', 'a'}, 0, 4);
 saxDirParser.characters(new char[]{'.'}, 0, 1);
 saxDirParser.characters(new char[]{'j', 'a', 'v', 'a'}, 0, 4);
 assertTrue(saxDirParser.check);
 assertEquals(10, saxDirParser.level);
 assertEquals("Notepad Editor Java Java Java.java.java", fileEditor);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testCharacters_lineNumAccepted_Yes() {
 saxDirParser = new SAXDirParser();
 saxDirParser.characters(new char[]{'Y', 'e', 's'}, 0, 3);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 assertTrue(SAXDirParser.lineNumAccepted);
 }

 @Test
 public void testCharacters_lineNumAccepted_No() {
 saxDirParser = new SAXDirParser();
 saxDirParser.characters(new char[]{'N', 'o'}, 0, 2);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 assertFalse(SAXDirParser.lineNumAccepted);
 }

 @Test
 public void testCharacters_lineNumberParameter() {
 saxDirParser = new SAXDirParser();
 saxDirParser.characters(new char[]{'1', '2', '3'}, 0, 3);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 assertEquals("123", SAXDirParser.lineNumParam);
 }

 @Test
 public void testEndElement_Group() {
 saxDirParser = new SAXDirParser();
 saxDirParser.endElement(null, "Group", "Group");
 assertFalse(saxDirParser.check);
 assertEquals(-1, saxDirParser.level);
 saxDirParser.check = false;
 saxDirParser.level = -1;
 }

 @Test
 public void testGetDirectory() {
 directory = SAXDirParser.getDirectory();
 assertTrue(directory.containsKey("java.lang"));
 assertTrue(directory.containsKey("javax.swing.border"));
 assertEquals("java.lang", directory.get("java.lang"));
 assertEquals("javax.swing.border", directory.get("javax.swing.border"));
 }

 @Test
 public void testGetLocalPackages() {
 localPackages = SAXDirParser.getLocalPackages();
 assertNotNull(localPackages);
 }

 @Test
 public void testGetLocalBrowser() {
 assertEquals("Mozilla Local Https://Documentum/SAMPLE.xml", SAXDirParser.getLocalBrowser());
 }

 @Test
 public void testGetEditor() {
 assertEquals("Notepad Editor Java Java Java.java.java", SAXDirParser.getEditor());
 }

 @Test
 public void testAcceptsLineNumber() {
 assertTrue(SAXDirParser.acceptsLineNumber());
 }

 @Test
 public void testLineNumberParameter() {
 assertEquals("123", SAXDirParser.lineNumberParameter());
 }
}