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
    public void testParseValidHtml() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1></body></html>";

        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        Element htmlNode = doc.head();
        assertEquals("html", htmlNode.tagName());

        Element titleNode = doc.head().title();
        assertEquals("Test Title", titleNode.text());

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());

        Element headingNode = bodyNode.child(0);
        assertEquals("h1", headingNode.tagName());
        assertEquals("Test Heading", headingNode.text());
    }

    @Test
    public void testParseValidBodyFragment() {
        String html = "<h1>Test Heading</h1>";

        Parser parser = Parser.parseBodyFragment(html, "http://test.com");
        Document doc = parser.parse();

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());

        Element headingNode = bodyNode.child(0);
        assertEquals("h1", headingNode.tagName());
        assertEquals("Test Heading", headingNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        String html = null;
        Parser parser = new Parser(html, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1></body></html>";
        String baseUri = null;
        Parser parser = new Parser(html, baseUri, false);
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        Comment comment = doc.body().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        Element cdataNode = doc.body().child(0);
        assertEquals("CDATA", cdataNode.tagName());
        assertEquals("This is CDATA", cdataNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<xml version='1.0' encoding='UTF-8' standalone='yes'/>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        XmlDeclaration xmlDeclaration = doc.body().child(0);
        assertNotNull(xmlDeclaration);
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertEquals("yes", xmlDeclaration.standalone());
    }

    @Test
    public void testParseEndTag() {
        String html = "<div><p>This is a paragraph</p></div>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        Element divNode = doc.body().child(0);
        Element pNode = divNode.child(0);

        parser.parseEndTag();
        assertEquals(1, doc.body().numChildren());
    }

    @Test
    public void testParseStartTag() {
        String html = "<p id='test'>This is a paragraph</p>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        Element pNode = doc.body().child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("test", pNode.attr("id"));
        assertEquals("This is a paragraph", pNode.text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src='image.png' alt='Image' title='Title' class='class'/>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        Element imgNode = doc.body().child(0);
        assertEquals("img", imgNode.tagName());

        assertEquals("image.png", imgNode.attr("src"));
        assertEquals("Image", imgNode.attr("alt"));
        assertEquals("Title", imgNode.attr("title"));
        assertEquals("class", imgNode.attr("class"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a paragraph</p>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        Element pNode = doc.body().child(0);
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("This is a paragraph", textNode.text());
    }
}