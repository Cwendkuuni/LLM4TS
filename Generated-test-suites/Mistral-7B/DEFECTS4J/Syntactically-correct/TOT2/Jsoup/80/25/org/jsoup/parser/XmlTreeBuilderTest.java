package org.example;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.Test;
import java.io.*;

public class XmlTreeBuilderTest {
    @Test
    public void testParse() {
        String xmlString = "<root><child>Hello World</child></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        Element root = xmlDoc.getRootElement();
        Element child = root.getChild(0);
        assertEquals("Hello World", child.text());
    }

    @Test
    public void testParseEmptyInput() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(""), "http://example.com");
        assertEquals(0, xmlDoc.childNodes().size());
    }

    @Test(expected = ParseError.class)
    public void testParseMissingEndTag() {
        String xmlString = "<root><child>Hello World";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidBaseUri() {
        String xmlString = "<root><child>Hello World</child></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), null);
    }

    @Test(expected = IOException.class)
    public void testParseIOException() throws IOException {
        StringReader reader = new StringReader("");
        reader.close();
        Document xmlDoc = XmlTreeBuilder.parse(reader, "http://example.com");
    }

    @Test
    public void testParseFragmentEmptyInput() {
        List<Node> nodes = XmlTreeBuilder.parseFragment("", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
        assertEquals(0, nodes.size());
    }

    @Test(expected = ParseError.class)
    public void testParseFragmentMissingTag() {
        String xmlFragment = "<root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }

    @Test(expected = IOException.class)
    public void testParseFragmentIOException() throws IOException {
        String xmlFragment = "<root><child>Hello World</child></root>";
        StringReader reader = new StringReader(xmlFragment);
        reader.close();
        List<Node> nodes = XmlTreeBuilder.parseFragment(reader, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }
}