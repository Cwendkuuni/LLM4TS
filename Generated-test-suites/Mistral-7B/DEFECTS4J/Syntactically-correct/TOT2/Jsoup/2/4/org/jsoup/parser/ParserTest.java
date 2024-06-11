package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.Parser.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test(expected = MalformedHtmlException.class)
    public void testMalformedHtml() {
        String html = "<html<body"; // missing closing tag
        String baseUri = "http://example.com";
        parse(html, baseUri);
    }

    @Test(expected = MalformedHtmlException.class)
    public void testEmptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        assertThrows(MalformedHtmlException.class, () -> parse(html, baseUri));
    }

    @Test(expected = MalformedHtmlException.class)
    public void testNullBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = null;
        assertThrows(MalformedHtmlException.class, () -> parse(html, baseUri));
    }

    @Test(expected = MalformedHtmlException.class)
    public void testEmptyBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "";
        assertThrows(MalformedHtmlException.class, () -> parse(html, baseUri));
    }

    @Test(expected = MalformedHtmlException.class)
    public void testInvalidBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "javascript:alert('XSS')";
        assertThrows(MalformedHtmlException.class, () -> parse(html, baseUri));
    }

    @Test(expected = MalformedHtmlException.class)
    public void testMalformedHtmlWithScriptTag() {
        String html = "<script>alert('XSS')</script><html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        assertThrows(MalformedHtmlException.class, () -> parse(html, baseUri));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        String baseUri = "http://example.com";
        Document doc = parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    // Add more test cases for edge cases and exception handling scenarios here
}