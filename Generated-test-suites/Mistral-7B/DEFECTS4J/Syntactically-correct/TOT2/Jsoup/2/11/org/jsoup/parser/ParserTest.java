package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1></body></html>";
        String baseUri = "http://example.com";
        parser = new Parser(html, baseUri, false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.select("h1").text());
    }

    @Test
    public void testParseWithEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertTrue(doc.body().isEmpty());
    }

    @Test
    public void testParseWithInvalidHtml() {
        String html = "<html><head><title>Test Title</title>"; // missing end tag
        try {
            Parser parser = new Parser(html, baseUri, false);
            parser.parse();
            fail("Expected ParserException");
        } catch (ParserException e) {
            // expected
        }
    }

    @Test
    public void testParseXmlDeclaration() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<body>\n";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertEquals("UTF-8", doc.xml().encoding());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->\n<html>\n<body>\n";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element comment = doc.select("meta[property='og:title']").first();
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p><span>Test</span></p>";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element span = doc.select("span").first();
        Element p = span.parent();
        assertTrue(p.getElementsByTag("span").hasSize(1));
    }

    @Test
    public void testParseStartTag() {
        String html = "<a href=\"test.html\" id=\"test-id\">Test</a>";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element a = doc.select("a#test-id").first();
        assertEquals("test.html", a.attr("abs:href"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<tag attr=\"value\">Test</tag>";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        Element tag = doc.select("tag").first();
        assertEquals("value", tag.attr("attr"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Test</p>";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        TextNode textNode = doc.select("p").text();
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[Test]]>";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        TextNode textNode = doc.select("p").first().child(0);
        assertEquals("Test", textNode.text());
    }
}