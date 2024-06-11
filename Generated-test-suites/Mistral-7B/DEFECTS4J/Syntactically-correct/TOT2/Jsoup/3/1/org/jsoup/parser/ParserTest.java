package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class ParserTest {
    @Test
    public void testParse() {
        // Test parsing a valid HTML string
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element htmlNode = doc.head();
        assertNotNull(htmlNode);
        assertEquals("Test", htmlNode.text());

        Element titleNode = htmlNode.child(0);
        assertEquals("title", titleNode.tagName());

        Element bodyNode = doc.body();
        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        // Test parsing a valid body fragment HTML string
        String bodyHtml = "<h1>Fragment</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        Element bodyNode = doc.body();
        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Fragment", h1Node.text());
    }

    @Test
    public void testParseInvalidHtml() {
        // Test parsing an invalid HTML string
        String html = "<html><body><h1<title>Hello World</h1>";
        try {
            Document doc = Parser.parse(html, "http://example.com");
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // Expected
        }
    }

    @Test
    public void testParseXmlDecl() {
        // Test parsing an XML declaration
        String html = "<xml version='1.0' encoding='UTF-8' standalone='yes'><title>Test</title></xml>";
        Document doc = Parser.parse(html, "http://example.com");
        Element xmlDeclaration = doc.head().child(0);
        assertNotNull(xmlDeclaration);
        assertTrue(xmlDeclaration instanceof XmlDeclaration);
    }

    @Test
    public void testParseComment() {
        // Test parsing an HTML comment
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element comment = doc.head().child(0);
        assertNotNull(comment);
        assertTrue(comment instanceof Comment);
    }

    @Test
    public void testParseEndTag() {
        // Test parsing an end tag
        String html = "<html><body><p><div></div></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element bodyNode = doc.body();
        Element pNode = bodyNode.child(0);
        Element divNode = pNode.child(0);
        assertEquals(2, divNode.parent().childSize());
    }

    @Test
    public void testParseStartTag() {
        // Test parsing a start tag with multiple attributes
        String html = "<div id='test-id' class='test-class' title='Test Title'>Test Content</div>";
        Document doc = Parser.parse(html, "http://example.com");
        Element divNode = doc.body().child(0);
        assertEquals("div", divNode.tagName());
        assertEquals("Test Content", divNode.text());
        assertEquals("test-id", divNode.attribute("id"));
        assertEquals("test-class", divNode.attribute("class"));
        assertEquals("Test Title", divNode.attribute("title"));
    }

    @Test
    public void testParseDataNode() {
        // Test parsing a data node (script tag)
        String html = "<html><head><script>console.log('Hello World');</script></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element headNode = doc.head();
        Element scriptNode = headNode.child(0);
        assertEquals("script", scriptNode.tagName());
        assertTrue(scriptNode.childNodes().size() > 0);
    }

    @Test
    public void testParseCdata() {
        // Test parsing a CDATA node
        String html = "<html><body><![CDATA[console.log('Hello World');]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element bodyNode = doc.body();
        Element cdataNode = bodyNode.child(0);
        assertEquals(1, cdataNode.childNodes().size());
        assertEquals("console.log('Hello World');", cdataNode.child(0).text());
    }
}