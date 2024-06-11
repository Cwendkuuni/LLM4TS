package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;

import java.util.*;

/**
 * Unit tests for the Parser class.
 */
public class ParserTest {

    @org.junit.Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><h1>Welcome</h1><p>This is a test.</p></body>" +
                "</html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.head();
        assertEquals("html", htmlNode.tagName());
        assertEquals("http://example.com", htmlNode.baseUri());

        Element titleNode = htmlNode.child(0);
        assertEquals("head", titleNode.tagName());

        Element titleEl = titleNode.child(0);
        assertEquals("title", titleEl.tagName());
        assertEquals("Test Page", titleEl.text());

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Welcome", h1Node.text());

        Element pNode = bodyNode.child(1);
        assertEquals("p", pNode.tagName());
        assertEquals("This is a test.", pNode.text());

        // Test baseURI handling
        Document doc2 = Parser.parse("<base href=\"/foo/\">" +
                "<html><head></head>" +
                "<body><a href=\"/bar/baz.html\"></a></body>" +
                "</html>", "http://example.com/qux/");

        assertEquals("/foo/baz.html", doc2.selectFirst("a[href]").attr("abs:href"));

        // Test fragment parsing
        Document doc3 = Parser.parse("<html><body id=\"foo\"></body></html>", "http://example.com/");
        Element bodyNode3 = doc3.body();
        assertEquals("#foo", bodyNode3.id());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        Parser.parse("<html></html>", null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        Parser.parse("", "http://example.com");
    }

    @org.junit.Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        assertEquals("html", doc.tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
        assertEquals("p", doc.body().child(0).tagName());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @org.junit.Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment -->" +
                "<body></body>" +
                "</html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element commentNode = doc.body().child(0);
        assertEquals("comment", commentNode.tagName());
        assertEquals("This is a comment", commentNode.text());
    }

    @org.junit.Test
    public void testParseCdata() {
        String html = "<html><![CDATA[This is some CDATA]]>" +
                "<body></body>" +
                "</html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element cdataNode = doc.body().child(0);
        assertEquals("text", cdataNode.tagName());
        assertEquals("This is some CDATA", cdataNode.text());
    }

    @org.junit.Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:lang=\"en\">" +
                "<body></body>" +
                "</html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element xmlDeclNode = doc.head();
        assertEquals("xml-decl", xmlDeclNode.tagName());
        assertEquals("http://example.com", xmlDeclNode.baseUri());
        assertEquals("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:lang=\"en\"", xmlDeclNode.attributes().toString());
    }

    @org.junit.Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello</p></body></html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.body().child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Hello", pNode.text());
    }

    @org.junit.Test
    public void testParseStartTag() {
        String html = "<html><body><p><b>Hello</b></p></body></html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.body().child(0);
        assertEquals("p", pNode.tagName());

        Element bNode = pNode.child(0);
        assertEquals("b", bNode.tagName());
        assertEquals("Hello", bNode.text());
    }

    @org.junit.Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World</p></body></html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.body().child(0);
        assertEquals("p", pNode.tagName());

        Element textNode = pNode.child(0);
        assertEquals("text", textNode.tagName());
        assertEquals("Hello World", textNode.text());
    }

    @org.junit.Test
    public void testParseAttributes() {
        String html = "<p id='foo' class='bar baz' title='Hello'></p>";

        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.body().child(0);
        Map<String,String> attributes = pNode.attributes();
        assertEquals(3, attributes.size());
        assertEquals("foo", attributes.get("id"));
        assertEquals("bar baz", attributes.get("class"));
        assertEquals("Hello", attributes.get("title"));
    }

    @org.junit.Test
    public void testParseSelfClosingTag() {
        String html = "<html><body><img src='foo.jpg' /></body></html>";

        Document doc = Parser.parse(html, "http://example.com");

        Element imgNode = doc.body().child(0);
        assertEquals("img", imgNode.tagName());
        assertTrue(imgNode.hasAttrs());
        assertEquals("http://example.com/foo.jpg", imgNode.attr("abs:src"));
    }

    @org.junit.Test
    public void testParseFragmentWithNoClosingTag() {
        String html = "<p>Hello <input type='text'> World";

        Document doc = Parser.parseBodyFragment(html, "http://example.com");

        Element pNode = doc.body().child(0);
        assertEquals("p", pNode.tagName());

        Element textNode = pNode.child(0);
        assertEquals("text", textNode.tagName());
        assertEquals("Hello ", textNode.text());

        Element inputNode = pNode.child(1);
        assertEquals("input", inputNode.tagName());
    }

    @org.junit.Test
    public void testParseFragmentWithImplicitParents() {
        String html = "<div><input type='text' /></div><p>Hello World</p>";

        Document doc = Parser.parseBodyFragment(html, "http://example.com");

        Element divNode = doc.body().child(0);
        assertEquals("div", divNode.tagName());

        Element inputNode = divNode.child(0);
        assertEquals("input", inputNode.tagName());

        Element pNode = doc.body().child(1);
        assertEquals("p", pNode.tagName());

        Element textNode = pNode.child(0);
        assertEquals("text", textNode.tagName());
        assertEquals("Hello World", textNode.text());

        // check that input node is a child of divNode
        assertEquals(divNode, inputNode.parent());
    }

    @org.junit.Test
    public void testParseFragmentWithInvalidParents() {
        String html = "<html><body><div><p>Hello World</p></div></body></html>";

        try {
            Document doc = Parser.parseBodyFragment(html, "http://example.com");
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseFragmentWithInvalidParents2() {
        String html = "<html><body><p><div></div></p></body></html>";

        try {
            Document doc = Parser.parseBodyFragment(html, "http://example.com");
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStartTag() {
        String html = "<html><body><p<input type='text' /></body></html>";

        Parser.parseBodyFragment(html, "http://example.com");
        fail();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStartTag2() {
        String html = "<html><body><p><input type='text' ></body></html>";

        Parser.parseBodyFragment(html, "http://example.com");
        fail();
    }
}