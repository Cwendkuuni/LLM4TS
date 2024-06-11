package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(htmlTag, htmlNode.tag());

        Element headNode = doc.head();
        assertEquals(headTag, headNode.tag());

        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());

        Element h1Node = htmlNode.child(0);
        assertEquals(bodyTag, htmlNode.tag());
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals(htmlTag, doc.tag());
        Element bodyNode = doc.body();
        assertEquals(bodyTag, bodyNode.tag());
        Element h1Node = bodyNode.child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><h1<script>alert('Hello')</script>";
        try {
            Parser.parse(html, "http://example.com");
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // Expected
        }
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element commentNode = doc.body().child(0);
        assertEquals(Comment.class, commentNode.getClass());
        assertEquals(" This is a comment ", commentNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><script><![CDATA[function test() { alert('Hello'); }]]></script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element scriptNode = doc.body().child(0);
        assertEquals(DataNode.class, scriptNode.child(0).getClass());
        assertEquals("function test() { alert('Hello'); }", scriptNode.child(0).text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:my=\"http://example.com/my\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "http://example.com");

        XmlDeclaration xmlDecl = doc.head().child(0);
        assertEquals("xmlns:my=\"http://example.com/my\" xml:lang=\"en\"", xmlDecl.data());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element h1Node = doc.body().child(0);
        h1Node.remove();

        assertEquals(2, doc.body().childSize());
        assertFalse(h1Node.parent().hasChild(h1Node));
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1 id=\"test\" class=\"test\" title=\"Test Title\"><a href=\"http://example.com\">Link</a></h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element h1Node = doc.body().child(0);
        Element titleNode = h1Node.attr("title");
        assertEquals("Test Title", titleNode.text());

        Element aNode = h1Node.child(0);
        assertEquals("Link", aNode.text());
        assertEquals("http://example.com", aNode.attr("href"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<input type=\"text\" name=\"username\" value=\"John Doe\">";
        Document doc = Parser.parse(html, "http://example.com");

        Element inputNode = doc.body().child(0);
        assertEquals("John Doe", inputNode.attr("value"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        TextNode textNode = doc.body().child(0).child(0);
        assertEquals("Hello World", textNode.text());
    }
}