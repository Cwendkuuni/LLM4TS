package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("html", doc.tagName());
        assertTrue(doc.body().isEmpty());
        assertTrue(doc.head().isEmpty());
    }

    @Test
    public void testParseSimpleHtml() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("html", doc.tagName());
        assertEquals("h1", doc.body().tagName());
        assertEquals("Hello, World!", doc.body().text().toString());
    }

    @Test
    public void testParseHtmlWithComments() {
        String html = "<html><!-- This is a comment --><body><h1>Hello, World!</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("html", doc.tagName());
        assertEquals("h1", doc.body().tagName());
        assertEquals("Hello, World!", doc.body().text().toString());
        assertTrue(doc.body().child(0) instanceof Comment);
    }

    @Test
    public void testParseHtmlWithScriptTag() {
        String html = "<html><body><script>alert('Hello, World!')</script></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("html", doc.tagName());
        assertFalse(doc.body().text().toString().contains("Hello, World!"));
        assertTrue(doc.body().child(0) instanceof DataNode);
    }

    @Test
    public void testParseHtmlWithCdataTag() {
        String html = "<html><body><![CDATA[alert('Hello, World!')]]></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("html", doc.tagName());
        assertFalse(doc.body().text().toString().contains("alert('Hello, World!'"));
        assertTrue(doc.body().child(0) instanceof DataNode);
    }

    @Test
    public void testParseHtmlWithAttribute() {
        String html = "<html><body><h1 id='title'>Hello, World!</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("html", doc.tagName());
        assertEquals("h1", doc.body().tagName());
        assertEquals("Hello, World!", doc.body().child(0).text().toString());
        Element h1 = doc.body().child(0);
        assertEquals("title", h1.attribute("id").getValue());
    }

    @Test
    public void testParseHtmlWithSelfClosingTag() {
        String html = "<html><body><img src='image.png' alt='An image' /></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("html", doc.tagName());
        assertEquals("img", doc.body().child(0).tagName());
        assertTrue(doc.body().child(0).hasAttrs());
        assertTrue(doc.body().child(0).isPropertiesSet());
        assertEquals("image.png", doc.body().child(0).attribute("src").getValue());
        assertEquals("An image", doc.body().child(0).attribute("alt").getValue());
    }

    @Test
    public void testParseHtmlWithBaseTag() {
        String html = "<html><head><base href='/foo/bar'></head><body><a href='baz'>Go to baz</a></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        assertEquals("html", doc.tagName());
        assertEquals("/foo/bar", doc.baseUri());
        assertEquals("a", doc.body().child(0).tagName());
        assertEquals("Go to baz", doc.body().child(0).text().toString());
        assertEquals("http://example.com/foo/bar/baz", doc.body().child(0).absUrl("href"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtmlWithEmptyBaseUri() {
        String html = "<html><body></body></html>";
        Parser parser = new Parser(html, "");
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtmlWithNullHtml() {
        Parser parser = new Parser(null, "http://example.com");
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtmlWithNullBaseUri() {
        String html = "<html><body></body></html>";
        Parser parser = new Parser(html, null);
        parser.parse();
    }
}