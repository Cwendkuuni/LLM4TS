package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;

public class ParserTest {

    @org.junit.Test
    public void testParseHtmlWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());

        Element headNode = htmlNode.getFirstChild();
        assertEquals("head", headNode.tagName());

        Element titleNode = headNode.getFirstChild();
        assertEquals("title", titleNode.tagName());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.getNextSibling();
        assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.getFirstChild();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @org.junit.Test
    public void testParseBodyFragmentWithBaseUri() throws IOException {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.getFirstChild();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        try {
            Parser.parse(html, "http://example.com");
        } catch (IllegalArgumentException e) {
            assertEquals("html is markup: null", e.getMessage());
            throw e;
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        String html = "";
        try {
            Parser.parse(html, "http://example.com");
        } catch (IllegalArgumentException e) {
            assertEquals("html is markup: ", e.getMessage());
            throw e;
        }
    }

    @org.junit.Test
    public void testParseHtmlWithEmptyBody() {
        String html = "<html><head><title>Test</title></head></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element bodyNode = doc.body();
        assertEquals(0, bodyNode.childNodeSize());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testParseHtmlWithInvalidEndTag() {
        String html = "<html><head><title>Test</title></head></titles>";
        try {
            Document doc = Parser.parse(html, "http://example.com");
        } catch (IllegalStateException e) {
            assertEquals("Expected END_TAG but was START_TAG. expecting: tags[titles], found: tags[title]", e.getMessage());
            throw e;
        }
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testParseHtmlWithEmptyStartTag() {
        String html = "<html>";
        try {
            Document doc = Parser.parse(html, "http://example.com");
        } catch (IllegalStateException e) {
            assertEquals("Expected START_TAG but was EOF. expected: tags[html]", e.getMessage());
            throw e;
        }
    }

    @org.junit.Test
    public void testParseHtmlWithSelfClosingStartTag() {
        String html = "<html><head selfclosed='true'><title>Test</title></head></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());

        Element headNode = htmlNode.getFirstChild();
        assertEquals("head", headNode.tagName());
        assertTrue(headNode.hasAttr("selfclosed"));
        assertEquals("true", headNode.attr("selfclosed"));
    }

    @org.junit.Test
    public void testParseHtmlWithAttributes() {
        String html = "<html id='home' class='default'><head title='Title'><body><h1 class='header' id='h1'>Hello World</h1></body></head></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
        assertEquals("home", htmlNode.attr("id"));
        assertEquals("default", htmlNode.attr("class"));

        Element headNode = htmlNode.getFirstChild();
        assertEquals("head", headNode.tagName());
        assertEquals("Title", headNode.attr("title"));

        Element bodyNode = htmlNode.getNextSibling();
        assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.getFirstChild();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
        assertEquals("header", h1Node.attr("class"));
        assertEquals("h1", h1Node.attr("id"));
    }

    @org.junit.Test
    public void testParseHtmlWithComments() {
        String html = "<html><!-- This is a comment --><head><title>Test</title></head><body><!-- Another comment --><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(2, htmlNode.childNodeSize());

        Element comment1 = htmlNode.getFirstChild();
        assertTrue(comment1 instanceof Comment);
        assertEquals("This is a comment", comment1.text());

        Element comment2 = htmlNode.getChild(1);
        assertTrue(comment2 instanceof Comment);
        assertEquals("Another comment", comment2.text());

        Element h1Node = htmlNode.getChild(2);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @org.junit.Test
    public void testParseHtmlWithCDATA() {
        String html = "<html><![CDATA[ <h1>Hello World</h1> ]]><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(1, htmlNode.childNodeSize());

        Element h1Node = htmlNode.getFirstChild();
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello World", h1Node.text());
    }

    @org.junit.Test
    public void testParseHtmlWithXmlDeclaration() {
        String html = "<xml version='1.0' encoding='UTF-8'><html><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element xmlDeclaration = doc.head();
        assertEquals("xml", xmlDeclaration.tagName());
        assertEquals(" version='1.0' encoding='UTF-8'", xmlDeclaration.attr("version"));

        Element htmlNode = doc.body();
        assertEquals("html", htmlNode.tagName());
    }

    @org.junit.Test
    public void testParseHtmlWithScriptTag() {
        String html = "<html><head><script type='text/javascript' src='script.js'></script></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element scriptNode = doc.head();
        assertEquals("script", scriptNode.tagName());
        assertEquals("text/javascript", scriptNode.attr("type"));
        assertTrue(StringUtils.isNotBlank(scriptNode.attr("src")));
    }

    @org.junit.Test
    public void testParseHtmlWithBaseTag() {
        String html = "<html><head><base href='/test.html'></base><title>Test</title></head><body></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("/test.html", doc.baseUri());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithUnknownTag() {
        String html = "<html><head><unknown></head><body></body></html>";
        try {
            Document doc = Parser.parse(html, "http://example.com");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown tag name: 'unknown'", e.getMessage());
            throw e;
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithWhitespaceInTagName() {
        String html = "<html><head><title>\t Test</title></head><body></body></html>";
        try {
            Document doc = Parser.parse(html, "http://example.com");
        } catch (IllegalArgumentException e) {
            assertEquals("Tag name contains invalid character: '\t' at character 1", e.getMessage());
            throw e;
        }
    }
}