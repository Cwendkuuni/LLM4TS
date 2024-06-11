package com.jstevh.viewer;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXParseException;

public class SAXDirParserTest {

 private SAXDirParser saxDirParser;
 private XMLReader xmlReader;

 @Test
 public void testSAXDirParser() throws Exception {
 saxDirParser = new SAXDirParser();
 xmlReader = new org.apache.xml.utils.wrapper.SAXParserFactoryImpl().newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 File file = new File("packagedirectory.xml");
 xmlReader.parse(file);

 Map<String, String> expectedDirectory = new HashMap<>();
 expectedDirectory.put("java.lang", "java/lang");
 expectedDirectory.put("javax.swing.border", " javax/swing/border");
 String expectedLocalBrowser = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0";
 String expectedEditor = "notepad";
 boolean expectedLineNumAccepted = true;
 String expectedLineNumParam = "56";
 Map<String, String> actualDirectory = SAXDirParser.getDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 assertEquals(expectedLocalBrowser, SAXDirParser.getLocalBrowser());
 assertEquals(expectedEditor, SAXDirParser.getEditor());
 assertTrue(SAXDirParser.acceptsLineNumber());
 assertEquals(expectedLineNumParam, SAXDirParser.lineNumberParameter());
 }

 @Test(expected = SAXParseException.class)
 public void testSAXParseException() throws Exception {
 saxDirParser = new SAXDirParser();
 xmlReader = new org.apache.xml.utils.wrapper.SAXParserFactoryImpl().newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 File file = new File("invalidfile.xml");
 xmlReader.parse(file);
 fail("SAXParseException expected");
 }

 @Test
 public void testStartElement() throws Exception {
 saxDirParser = new SAXDirParser();
 xmlReader = new org.apache.xml.utils.wrapper.SAXParserFactoryImpl().newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 String xmlString = "<Web>\n<Group>\n<Local>\n<acceptsLineNumber>Yes</acceptsLineNumber>\n<parameter>56</parameter>\n</Local>\n<Names>\n<pkg>java.lang</pkg>\n<pkg>javax.swing.border</pkg>\n</Names>\n</Group>\n<BrowserLoc>Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0</BrowserLoc>\n<Editor>notepad</Editor>\n</Web>";
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 xmlReader.parse(inputSource);
 assertEquals("BrowserLoc", saxDirParser.getLastElementName());
 assertEquals("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0", saxDirParser.getWebData());
 assertEquals(2, saxDirParser.getLevel());
 assertEquals(false, saxDirParser.isCheck());
 assertEquals(false, saxDirParser.isLocal());
 }

 @Test
 public void testCharacters() throws Exception {
 saxDirParser = new SAXDirParser();
 xmlReader = new org.apache.xml.utils.wrapper.SAXParserFactoryImpl().newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 String xmlString = "<Web>\n<Group>\n<Local>\n<acceptsLineNumber>Yes</acceptsLineNumber>\n<parameter>56</parameter>\n</Local>\n<Names>\n<pkg>java.lang</pkg>\n<pkg>javax.swing.border</pkg>\n</Names>\n<BrowserLoc>Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0</BrowserLoc>\n<Editor>notepad</Editor>\n</Web>";
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 xmlReader.parse(inputSource);
 assertEquals("Yes", saxDirParser.getLastAcceptedValue());
 assertEquals("56", saxDirParser.getLastParameter());
 assertEquals("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0", saxDirParser.getLastBrowser());
 assertEquals("notepad", saxDirParser.getLastEditor());
 }

 @Test
 public void testEndElement() throws Exception {
 saxDirParser = new SAXDirParser();
 xmlReader = new org.apache.xml.utils.wrapper.SAXParserFactoryImpl().newSAXParser().getXMLReader();
 xmlReader.setContentHandler(saxDirParser);
 String xmlString = "<Web>\n<Group>\n<Local>\n<acceptsLineNumber>Yes</acceptsLineNumber>\n<parameter>56</parameter>\n</Local>\n<Names>\n<pkg>java.lang</pkg>\n<pkg>javax.swing.border</pkg>\n</Names>\n<BrowserLoc>Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0</BrowserLoc>\n<Editor>notepad</Editor>\n</Web>";
 StringReader stringReader = new StringReader(xmlString);
 InputSource inputSource = new InputSource(stringReader);
 xmlReader.parse(inputSource);
 assertEquals("Web", saxDirParser.getLastElementName());
 }
}

class StringReader extends Reader {
 private String buffer;
 private int bufferPosition;
 private int bufferLength;

 public StringReader(String buffer) {
 this.buffer = buffer;
 this.bufferLength = buffer.length();
 }

 @Override
 public int read(char[] buffer, int offset, int length) throws IOException {
 if (bufferPosition >= bufferLength) {
 return -1;
 }
 int numCharsToRead = Math.min(length, bufferLength - bufferPosition);
 System.arraycopy(buffer, offset, buffer, offset, numCharsToRead);
 bufferPosition += numCharsToRead;
 return numCharsToRead;
 }

 @Override
 public void close() throws IOException {
 // Empty
 }

 @Override
 public boolean markSupported(boolean readAhead) {
 return false;
 }

 @Override
 public int mark(int readAheadLimit) {
 return 0;
 }

 @Override
 public boolean markSupported(int readAheadLimit) {
 return false;
 }

 @Override
 public synchronized void reset() throws IOException {
 bufferPosition = 0;
 }

 @Override
 public synchronized long position() throws IOException {
 return bufferPosition;
 }

 @Override
 public synchronized int read(CharBuffer target) throws IOException {
 int length = target.remaining();
 if (bufferPosition >= bufferLength) {
 return -1;
 }
 int numCharsToRead = Math.min(length, bufferLength - bufferPosition);
 char[] targetCharArray = target.array();
 System.arraycopy(buffer, (int) bufferPosition, targetCharArray, 0, numCharsToRead);
 bufferPosition += numCharsToRead;
 target.position(numCharsToRead);
 return numCharsToRead;
 }

 @Override
 public synchronized int read(CharBuffer target, int srcOffset, int targetOffset, int length) throws IOException {
 int numCharsToRead = Math.min(length, bufferLength - bufferPosition);
 char[] targetCharArray = target.array();
 System.arraycopy(buffer, (int) bufferPosition, targetCharArray, targetOffset, numCharsToRead);
 bufferPosition += numCharsToRead;
 target.position(numCharsToRead + srcOffset);
 return numCharsToRead;
 }

 @Override
 public synchronized int read(CharBuffer target, int offset, int length) throws IOException {
 int numCharsToRead = Math.min(length, bufferLength - bufferPosition);
 char[] targetCharArray = target.array();
 System.arraycopy(buffer, (int) bufferPosition, targetCharArray, offset, numCharsToRead);
 bufferPosition += numCharsToRead;
 target.position(numCharsToRead);
 return numCharsToRead;
 }

 @Override
 public synchronized long positionLength() throws IOException {
 return bufferLength;
 }

 @Override
 public synchronized boolean isOpen() {
 return true;
 }

 @Override
 public synchronized void flush() throws IOException {
 // Empty
 }

 @Override
 public synchronized String toString() {
 return buffer.substring(0, bufferPosition);
 }
}