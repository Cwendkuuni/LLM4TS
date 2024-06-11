package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Element title = doc.title();
        assertEquals("Test", title.text());
        Element h1 = doc.getElementsByTag("h1").first();
        assertEquals("Hello World", h1.text());
    }

    @Test
    public void testEmptyHtml() {
        Document doc = Parser.parse("", "http://example.com");
        assertEquals(0, doc.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHtml() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World<p";
        try {
            Document doc = Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalArgumentException ex) {
            // pass
        }
    }

    @Test
    public void testParseWithEmptyHtmlAndBaseUri() {
        Document doc = Parser.parse("", "http://example.com");
        assertEquals(0, doc.childNodes().size());
    }
}