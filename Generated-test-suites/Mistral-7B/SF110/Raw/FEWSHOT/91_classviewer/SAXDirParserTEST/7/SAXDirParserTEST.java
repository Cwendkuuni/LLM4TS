package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.junit.Test;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.XMLInputFactory;
import javax.xml.parsers.XMLParserConfigurationException;

public class SAXDirParserTest {

 private SAXDirParser parser;
 private File inputFile;

 @Test
 public void testSAXDirParser() throws ParserConfigurationException, SAXException, IOException, XMLParserConfigurationException {
 SAXParserFactory factory = SAXParserFactory.newInstance();
 SAXParser parser = factory.newSAXParser();
 InputSource inputSource = new InputSource(new File("packagedirectory.xml").toURL().openStream());
 parser.parse(inputSource, new SAXDirParser());

 // Test getDirectory()
 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "expectedJavaLangLocation");
 expectedDirectory.put("javax.swing.border", "expectedJSwingBorderLocation");
 HashMap<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);

 // Test getLocalPackages()
 StringList expectedLocalPackages = new StringList();
 expectedLocalPackages.add("expectedPackage1");
 expectedLocalPackages.add("expectedPackage2");
 StringList actualLocalPackages = SAXDirParser.getLocalPackages();
 assertEquals(expectedLocalPackages, actualLocalPackages);

 // Test getLocalBrowser()
 String expectedLocalBrowser = "expectedLocalBrowser";
 String actualLocalBrowser = SAXDirParser.getLocalBrowser();
 assertEquals(expectedLocalBrowser, actualLocalBrowser);

 // Test getEditor()
 String expectedEditor = "expectedEditor";
 String actualEditor = SAXDirParser.getEditor();
 assertEquals(expectedEditor, actualEditor);

 // Test acceptsLineNumber()
 boolean expectedAcceptsLineNumber = true;
 boolean actualAcceptsLineNumber = SAXDirParser.acceptsLineNumber();
 assertEquals(expectedAcceptsLineNumber, actualAcceptsLineNumber);

 // Test lineNumberParameter()
 String expectedLineNumberParameter = "expectedLineNumberParameter";
 String actualLineNumberParameter = SAXDirParser.lineNumberParameter();
 assertEquals(expectedLineNumberParameter, actualLineNumberParameter);
 }

 @Test
 public void testStartElement() throws SAXException {
 parser = new SAXDirParser();
 inputFile = new File("packagedirectory.xml");
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 SAXParserFactory parserFactory = SAXParserFactory.newInstance();
 SAXParser saxParser = parserFactory.newSAXParser();
 InputSource inputSource = inputFactory.createInputSource(inputFile);
 saxParser.parse(inputSource, parser);
 String namespace = "";
 String expectedLocal = "";
 String qname = "";
 int expectedLevel = -1;
 for (Integer level = 0; level <= 12; level++) {
 String local = "";
 String qnameTemp = "";
 switch (level) {
 case 0:
 namespace = "BrowserLoc";
 local = "Firefox";
 qname = "BrowserLoc";
 expectedLevel = 0;
 break;
 case 10:
 namespace = "";
 local = "Editor";
 qname = "Editor";
 expectedLevel = 10;
 break;
 case 11:
 namespace = "";
 local = "Yes";
 qname = "acceptsLineNumber";
 expectedLevel = 11;
 break;
 case 12:
 namespace = "";
 local = "lineNumber";
 qname = "parameter";
 expectedLevel = 12;
 break;
 default:
 local = "Group";
 qname = "Group";
 expectedLevel = -1;
 }
 parser.startElement(namespace, local, qname, null);
 assertEquals(expectedLocal, parser.local);
 assertEquals(expectedLevel, parser.level);
 }
 }

 @Test
 public void testEndElement() throws SAXException {
 parser = new SAXDirParser();
 inputFile = new File("packagedirectory.xml");
 XMLInputFactory inputFactory = XMLInputFactory.newInstance();
 SAXParserFactory parserFactory = SAXParserFactory.newInstance();
 SAXParser saxParser = parserFactory.newSAXParser();
 InputSource inputSource = inputFactory.createInputSource(inputFile);
 saxParser.parse(inputSource, parser);
 String namespace = "";
 String local = "";
 String qname = "";
 parser.startElement(namespace, local, qname, null);
 parser.endElement();
 parser.endElement();
 assertEquals(0, parser.level);
 }
}