package com.jstevh.viewer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Parser;
import org.xml.sax.SAXParseException;

import com.jstevh.viewer.SAXDirParser;
import com.jstevh.tools.StringList;

public class SAXDirParserTest {

 private SAXDirParser parser;

 @Test
 public void testGetWebData() {
 SAXDirParser parser = new SAXDirParser();
 assertEquals("", parser.getWebData());
 }

 @Test
 public void testStartElement_BrowserLoc() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<BrowserLoc>Mozilla/5.0</BrowserLoc>\n" +
             "</Group>";
 parser.parse(xml);
 assertEquals("Mozilla/5.0", parser.getWebData());
 }

 @Test
 public void testStartElement_Editor() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<Editor>Notepad</Editor>\n" +
             "</Group>";
 assertEquals("Notepad", parser.getWebData());
 }

 @Test
 public void testStartElement_acceptsLineNumber() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<acceptsLineNumber>Yes</acceptsLineNumber>\n" +
             "</Group>";
 assertTrue(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElement_acceptsLineNumber_No() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<acceptsLineNumber>No</acceptsLineNumber>\n" +
             "</Group>";
 assertFalse(parser.acceptsLineNumber());
 }

 @Test
 public void testStartElement_parameter() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<parameter>123</parameter>\n" +
             "</Group>";
 assertEquals("123", parser.lineNumberParameter());
 }

 @Test
 public void testStartElement_parameter_Empty() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<parameter></parameter>\n" +
             "</Group>";
 assertNull(parser.lineNumberParameter());
 }

 @Test
 public void testStartElement_Web() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<Web>https://www.example.com</Web>\n" +
             "</Group>";
 assertEquals("https://www.example.com", parser.getWebData());
 }

 @Test
 public void testStartElement_Local() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<Local>True</Local>\n" +
             "</Group>";
 assertTrue(parser.isLocal());
 }

 @Test
 public void testStartElement_Local_False() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<Local>False</Local>\n" +
             "</Group>";
 assertFalse(parser.isLocal());
 }

 @Test
 public void testStartElement_pkg() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<pkg>java.lang</pkg>\n" +
             "</Group>";
 assertEquals("java.lang", parser.getDirectory().keySet().iterator().next());
 }

 @Test
 public void testStartElement_pkg_Empty() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "</Group>";
 assertNotNull(parser.getDirectory());
 assertTrue(parser.getDirectory().isEmpty());
 }

 @Test(expected = SAXParseException.class)
 public void testStartElement_InvalidElement() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<InvalidElement>Invalid</InvalidElement>\n" +
             "</Group>";
 parser.parse(xml);
 fail("SAXParseException expected but not thrown");
 }

 @Test
 public void testParseEmptyXML() {
 parser = new SAXDirParser();
 String xml = "";
 parser.parse(xml);
 assertTrue(parser.getDirectory().isEmpty());
 }

 @Test
 public void testParseXMLWithInvalidStruct() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<Group>\n" +
             "</Group>";
 parser.parse(xml);
 assertTrue(parser.getDirectory().isEmpty());
 }

 @Test
 public void testParseXMLWithUnexpectedEndElement() {
 parser = new SAXDirParser();
 String xml = "<Group>\n" +
             "<Web>https://www.example.com</Web>\n" +
             "</Group>\n" +
             "</Group>";
 parser.parse(xml);
 assertTrue(parser.getDirectory().isEmpty());
 }
}