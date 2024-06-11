package com.jstevh.viewer.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.StringReader;
import java.util.*;
import com.jstevh.viewer.SAXDirParser;

public class SAXDirParserTest {

    @Test
    public void testGetDirectory() {
        String xmlData = "<Web>\n" +
                "  <Local>\n" +
                "    <Names>\n" +
                "      <pkg name=\"java.lang\">/java/lang</pkg>\n" +
                "      <pkg name=\"javax.swing.border\">/javax/swing/border</pkg>\n" +
                "    </Names>\n" +
                "  </Local>\n" +
                "  <BrowserLoc>Firefox</BrowserLoc>\n" +
                "  <Editor acceptsLineNumber=\"Yes\">Editor1.jar</Editor>\n" +
                "  <Group>\n" +
                "    <Web name=\"/java/lang\">Java</Web>\n" +
                "    <Web name=\"/javax/swing/border\">JSwing Border</Web>\n" +
                "  </Group>\n" +
                "</Web>";

        SAXDirParser parser = new SAXDirParser();
        DefaultHandler handler = new HandlerWrapper(parser);
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        StringReader stringReader = new StringReader(xmlData);
        xmlReader.setContentHandler(handler);
        xmlReader.parse(stringReader);

        Map<String, String> expectedDirectory = new HashMap<>();
        expectedDirectory.put("java.lang", "/java/lang");
        expectedDirectory.put("javax.swing.border", "/javax/swing/border");

        Map<String, String> actualDirectory = parser.getDirectory();
        assertEquals("test getDirectory, expected and actual directories should be equal", expectedDirectory, actualDirectory);
    }

    @Test
    public void testGetLocalPackages() {
        String xmlData = "<Web>\n" +
                "  <Local>\n" +
                "    <Names>\n" +
                "      <pkg name=\"java.lang\">/java/lang</pkg>\n" +
                "      <pkg name=\"javax.swing.border\">/javax/swing/border</pkg>\n" +
                "    </Names>\n" +
                "  </Local>\n" +
                "  <Local>\n" +
                "    <Names>\n" +
                "      <pkg name=\"com.jstevh.viewer\">/custom</pkg>\n" +
                "    </Names>\n" +
                "  </Local>\n" +
                "</Web>";

        SAXDirParser parser = new SAXDirParser();
        DefaultHandler handler = new HandlerWrapper(parser);
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        StringReader stringReader = new StringReader(xmlData);
        xmlReader.setContentHandler(handler);
        xmlReader.parse(stringReader);

        StringList expectedLocalPackages = new StringList();
        expectedLocalPackages.add("com.jstevh.viewer");

        StringList actualLocalPackages = parser.getLocalPackages();
        assertEquals("test getLocalPackages, expected and actual local packages should be equal", expectedLocalPackages, actualLocalPackages);
    }

    @Test
    public void testGetLocalBrowser() {
        String xmlData = "<Web>\n" +
                "  <Local>\n" +
                "    <BrowserLoc>Firefox</BrowserLoc>\n" +
                "  </Local>\n" +
                "</Web>";

        SAXDirParser parser = new SAXDirParser();
        DefaultHandler handler = new HandlerWrapper(parser);
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        StringReader stringReader = new StringReader(xmlData);
        xmlReader.setContentHandler(handler);
        xmlReader.parse(stringReader);

        String expectedLocalBrowser = "Firefox";
        String actualLocalBrowser = parser.getLocalBrowser();
        assertEquals("test getLocalBrowser, expected and actual local browsers should be equal", expectedLocalBrowser, actualLocalBrowser);
    }

    @Test
    public void testGetEditor() {
        String xmlData = "<Web>\n" +
                "  <Editor acceptsLineNumber=\"Yes\">Editor1.jar</Editor>\n" +
                "</Web>";

        SAXDirParser parser = new SAXDirParser();
        DefaultHandler handler = new HandlerWrapper(parser);
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        StringReader stringReader = new StringReader(xmlData);
        xmlReader.setContentHandler(handler);
        xmlReader.parse(stringReader);

        String expectedEditor = "Editor1.jar";
        String actualEditor = parser.getEditor();
        assertEquals("test getEditor, expected and actual editors should be equal", expectedEditor, actualEditor);
    }

    @Test
    public void testAcceptsLineNumber() {
        String xmlData = "<Web>\n" +
                "  <Editor acceptsLineNumber=\"Yes\"></Editor>\n" +
                "</Web>";

        SAXDirParser parser = new SAXDirParser();
        DefaultHandler handler = new HandlerWrapper(parser);
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        StringReader stringReader = new StringReader(xmlData);
        xmlReader.setContentHandler(handler);
        xmlReader.parse(stringReader);

        boolean expectedAcceptsLineNumber = true;
        boolean actualAcceptsLineNumber = parser.acceptsLineNumber();
        assertEquals("test acceptsLineNumber, expected and actual acceptsLineNumber should be equal", expectedAcceptsLineNumber, actualAcceptsLineNumber);
    }

    @Test
    public void testLineNumberParameter() {
        String xmlData = "<Web>\n" +
                "  <Editor>\n" +
                "    <acceptsLineNumber>Yes</acceptsLineNumber>\n" +
                "    <parameter>123</parameter>\n" +
                "  </Editor>\n" +
                "</Web>";

        SAXDirParser parser = new SAXDirParser();
        DefaultHandler handler = new HandlerWrapper(parser);
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        StringReader stringReader = new StringReader(xmlData);
        xmlReader.setContentHandler(handler);
        xmlReader.parse(stringReader);

        String expectedLineNumberParameter = "123";
        String actualLineNumberParameter = parser.lineNumberParameter();
        assertEquals("test lineNumberParameter, expected and actual lineNumberParameters should be equal", expectedLineNumberParameter, actualLineNumberParameter);
    }

    private static class HandlerWrapper implements DefaultHandler {
        private DefaultHandler delegate;

        public HandlerWrapper(DefaultHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            delegate.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            delegate.endElement(uri, localName, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            delegate.characters(ch, start, length);
        }

        @Override
        public void startDocument() throws SAXException {
            delegate.startDocument();
        }

        @Override
        public void endDocument() throws SAXException {
            delegate.endDocument();
        }

        @Override
        public void warning(SAXParseException parseException) throws SAXException {
            delegate.warning(parseException);
        }

        @Override
        public void error(SAXParseException parseException) throws SAXException {
            delegate.error(parseException);
        }

        @Override
        public void fatalError(SAXParseException parseException) throws SAXException {
            delegate.fatalError(parseException);
        }
    }
}