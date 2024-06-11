package org.jsoup.parser;

import static org.junit.Assert.*;
import org.apache.commons.lang.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element htmlNode = doc;
        assertEquals(htmlTag, htmlNode.tag());

        Element headNode = htmlNode.child(0);
        assertEquals(headTag, headNode.tag());

        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello", h1Node.text());

        assertEquals("http://test.com", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");

        Element bodyNode = doc;
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Hello", h1Node.text());

        assertEquals("http://test.com", doc.baseUri());

        // test empty document
        doc = Parser.parseBodyFragment("", "http://test.com");
        assertNotNull(doc);
        assertTrue(doc.isEmpty());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- this is a comment -->";
        Document doc = Parser.parse(html, "http://test.com");
        Element commentNode = doc.select("script[type='text/javascript']").first();
        assertNull(commentNode);
        List<Comment> comments = doc.select("comment()");
        assertEquals(1, comments.size());
        assertEquals("this is a comment", comments.get(0).data());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<xml version='1.0' encoding='UTF-8'>";
        Document doc = Parser.parse(html, "http://test.com");
        XmlDeclaration xmlDeclaration = doc.select("xml").first();
        assertNotNull(xmlDeclaration);
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertEquals("1.0", xmlDeclaration.version());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><head></head><body><div><p></p></div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element divNode = doc.select("div").first();
        assertEquals(1, divNode.childNodeSize());
        Element pNode = divNode.child(0);
        assertEquals(1, pNode.childNodeSize());
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("", textNode.text());

        Parser parser = new Parser(html, "http://test.com", false);
        parser.parseBodyFragment(html, "http://test.com");

        Element divNodeFragment = parser.parse().body();
        assertEquals(1, divNodeFragment.childNodeSize());
        Element pNodeFragment = divNodeFragment.child(0);
        assertEquals(1, pNodeFragment.childNodeSize());
        TextNode textNodeFragment = (TextNode) pNodeFragment.child(0);
        assertEquals("", textNodeFragment.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element titleNode = doc.select("title").first();
        assertEquals("Test", titleNode.text());
        assertEquals("title", titleNode.tagName());

        Element h1Node = doc.select("h1").first();
        assertEquals("Hello", h1Node.text());
        assertEquals("h1", h1Node.tagName());

        // test missing end tag
        String htmlMissingEndTag = "<html><head><title>Test</title></head><body><h1>Hello";
        try {
            Parser.parse(htmlMissingEndTag, "http://test.com");
            fail("Parsing should have failed due to missing end tag for h1 tag.");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href='http://test.com' class='class' title='Title'>Link</a>";
        Document doc = Parser.parse(html, "http://test.com");

        Element linkNode = doc.select("a").first();
        Attribute hrefAttr = linkNode.attr("href");
        assertEquals("http://test.com", hrefAttr.value());
        assertEquals("href", hrefAttr.key());

        Attribute classAttr = linkNode.attr("class");
        assertEquals("class", classAttr.key());
        assertEquals("", classAttr.value());

        Attribute titleAttr = linkNode.attr("title");
        assertEquals("Title", titleAttr.value());
        assertEquals("title", titleAttr.key());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>This is a test.</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element pNode = doc.select("p").first();
        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("This is a test.", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[This is some CDATA.]]></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element bodyNode = doc.select("body").first();
        TextNode cdataNode = (TextNode) bodyNode.child(0);
        assertEquals("This is some CDATA.", cdataNode.text());
    }

    @Test
    public void testParseEmptyElement() {
        String html = "<img src='image.png' />";
        Document doc = Parser.parse(html, "http://test.com");

        Element imgNode = doc.select("img").first();
        assertTrue(imgNode.tag().isEmpty());
        assertEquals(1, imgNode.childNodeSize());
        TextNode altTextNode = (TextNode) imgNode.child(0);
        assertEquals("", altTextNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInput() {
        String html = "This is not valid HTML";
        Parser parser = new Parser(html, "http://test.com", false);
        parser.parse();
        fail("Parsing should have failed due to invalid HTML.");
    }
}