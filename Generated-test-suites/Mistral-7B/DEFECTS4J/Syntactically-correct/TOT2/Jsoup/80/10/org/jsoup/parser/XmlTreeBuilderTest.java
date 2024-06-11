package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.NodeList;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;

public class XmlTreeBuilderTest {

    public static final String XML_ROOT_TAG = "root";

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReader() throws IOException {
        String xmlInput = "<root><child>Hello</child></root>";
        Reader input = new StringReader(xmlInput);
        Document doc = XmlTreeBuilder.parse(input, "baseUri");

        assertEquals(XML_ROOT_TAG, doc.tagName());
        assertEquals("Hello", doc.select("child[0]").text());
    }

    @Test
    public void testParseString() throws IOException {
        String xmlInput = "<root><child>Hello</child></root>";
        Document doc = XmlTreeBuilder.parse(xmlInput, "baseUri");

        assertEquals(XML_ROOT_TAG, doc.tagName());
        assertEquals("Hello", doc.select("child[0]").text());
    }

    @Test
    public void testParseEmptyInput() {
        Document doc = XmlTreeBuilder.parse(new StringReader(""), "baseUri");
        assertEquals(Document.class, doc.getClass());
        assertTrue(doc.childNodes().isEmpty());
    }

    @Test(expected = XMLParseException.class)
    public void testParseMalformedXML() {
        String xmlInput = "<root><child>Hello<world";
        Reader input = new StringReader(xmlInput);
        XmlTreeBuilder.parse(input, "baseUri");
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullInput() {
        XmlTreeBuilder.parse(null, "baseUri");
    }

    @Test
    public void testParseNullBaseUri() {
        String xmlInput = "<root><child>Hello</child></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xmlInput), null);

        assertEquals("baseUri is null", doc.baseUri());
    }

    @Test(expected = IOException.class)
    public void testParseException() throws IOException {
        String xmlInput = "<root><child>Hello</child></root>";
        Reader input = new StringReader(xmlInput);
        File file = new File("test.xml");
        try {
            XmlTreeBuilder.parse(input, "baseUri", new PrintWriter(file), ParseSettings.preserveCase);
        } catch (IOException e) {
            // Expected
        } finally {
            input.close();
            file.deleteOnExit();
        }
    }

    // Add test cases for other methods here
}