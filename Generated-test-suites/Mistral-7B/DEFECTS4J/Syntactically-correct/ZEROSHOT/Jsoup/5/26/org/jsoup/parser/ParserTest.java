package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.LinkedList;

import org.junit.Test;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        Element headNode = htmlNode.previousSibling();
        Element titleNode = headNode.child(0);

        assertEquals("Test", titleNode.text());
        assertEquals("Hello World", htmlNode.child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals("Hello World", htmlNode.child(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals("Hello World", htmlNode.child(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Parser.parse("<html></html>", null);
    }

    @Test
    public void testParseEmptyHtml() {
        Document doc = Parser.parse("<html></html>", "http://example.com");
        assertEquals(0, doc.body().childSize());
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<img src=\"image.jpg\" />";
        Document doc = Parser.parse(html, "http://example.com");

        Element imgNode = doc.body().child(0);
        Element imgParentNode = imgNode.parent();
        assertEquals("img", imgParentNode.tag().name());
    }

    @Test
    public void testParseDataTag() {
        String html = "<p><textarea>Test Data</textarea></p>";
        Document doc = Parser.parse(html, "http://example.com");

        Element textareaNode = doc.body().child(0).child(0);
        assertEquals("Test Data", textareaNode.text());
    }

    @Test
    public void testParseDataTagWithAttribute() {
        String html = "<script type=\"text/javascript\" src=\"script.js\"></script>";
        Document doc = Parser.parse(html, "http://example.com");

        Element scriptNode = doc.body().child(0);
        assertEquals("script", scriptNode.tag().name());
        assertEquals("text/javascript", scriptNode.attr("type"));
    }

    @Test
    public void testParseEmptyScriptTag() {
        String html = "<script></script>";
        Document doc = Parser.parse(html, "http://example.com");

        Element scriptNode = doc.body().child(0);
        assertEquals(0, scriptNode.childSize());
    }

    @Test
    public void testParseComment() {
        String html = "<!DOCTYPE html>\n<!-- This is a comment -->\n<html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element commentNode = doc.head();
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        Document doc = Parser.parse(html, "http://example.com");

        Element cdataNode = doc.body();
        assertEquals("This is CDATA", cdataNode.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Document doc = Parser.parse(html, "http://example.com");

        Element xmlDeclNode = doc.head();
        assertEquals("about:legacy-compat", xmlDeclNode.attr("systemId"));
    }

    @Test
    public void testParseBaseTag() {
        String html = "<base href=\"/test/\">";
        Document doc = Parser.parse(html, "http://example.com");

        assertEquals("/test/", doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentWithBaseTag() {
        String html = "<base href=\"/test/\">\n<body><p>Test</p></body>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");

        assertEquals("/test/", doc.baseUri());
        assertEquals("Test", doc.body().child(0).text());
    }
}