package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Tests for Parser class
 *
 * @author John Doe
 */
public class ParserTest {

    @Test
    public void testParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello", doc.body().text());
    }

    @Test
    public void testParserBodyFragment() {
        String bodyHtml = "<body><h1>Hello</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello", doc.body().text());
        assertNull(doc.head());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        List<Comment> comments = doc.select("script,style,comment");
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("This is a comment", comments.get(0).text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:my=\"http://example.com\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
        XmlDeclaration xmlDecl = doc.xml();
        assertNotNull(xmlDecl);
        assertEquals("http://example.com", xmlDecl.xmlNsPrefixes().get("my"));
        assertEquals("en", xmlDecl.xmlLang());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><div><p>Hello</p></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element div = doc.selectFirst("#div");
        Element p = div.selectFirst("p");
        assertNotNull(div);
        assertNotNull(p);
        assertEquals("Hello", p.text());
        Element last = doc.body();
        assertEquals(div, last.previousSibling());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1 id=\"title\">Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element h1 = doc.selectFirst("#title");
        assertNotNull(h1);
        assertEquals("Hello", h1.text());
        assertEquals("h1", h1.tagName());
        assertEquals("http://example.com", h1.baseUri());

        Attributes attributes = h1.attributes();
        assertNotNull(attributes);
        assertNull(attributes.get("class"));
        assertEquals("title", attributes.get("id"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id=\"div\" class=\"class\" title=\"title\"></div>";
        Document doc = Parser.parse(html, "http://example.com");
        Element div = doc.selectFirst("#div");
        Attributes attributes = div.attributes();
        assertNotNull(attributes);
        assertEquals("div", div.tagName());
        assertEquals("class", attributes.classAttr());
        assertEquals("title", attributes.attr("title"));
        assertEquals("title", attributes.get("title"));
        assertEquals("class", attributes.get("class").toString());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body>Hello, world!</body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element body = doc.body();
        TextNode textNode = (TextNode) body.child(0);
        assertEquals("Hello, world!", textNode.text());
    }

    @Test(expected = IllegalStateException.class)
    public void testMalformedHtml() {
        String html = "<html><body<div>";
        Parser.parse(html, "http://example.com");
    }
}