package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {
    @Test
    public void testParseSimpleHtml() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc.rootElement();
        Assert.assertEquals("html", root.tagName());
        Assert.assertNotNull(root.head());
        Assert.assertNotNull(root.title());
        Assert.assertEquals("Test Title", root.title().text());
        Assert.assertEquals("body", root.body().tagName());
        Assert.assertEquals("h1", root.body().tag(0).tagName());
        Assert.assertEquals("Test Heading", root.body().tag(0).text());
    }

    @Test
    public void testEmptyHtml() {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc.rootElement();
        Assert.assertEquals("html", root.tagName());
        Assert.assertNull(root.head());
        Assert.assertEquals("body", root.body().tagName());
        Assert.assertTrue(root.body().children().isEmpty());
    }

    @Test
    public void testParseEmptyBodyFragment() {
        String bodyHtml = "";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element root = doc.rootElement();
        Assert.assertEquals("html", root.tagName());
        Assert.assertNotNull(root.body());
        Assert.assertTrue(root.body().children().isEmpty());
    }

    @Test
    public void testParseHtmlWithScriptTag() {
        String html = "<html><head></head><body><script>alert('Hello World')</script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element root = doc.rootElement();
        Element script = root.getElementsByTag("script").get(0);
        Assert.assertEquals("script", script.tagName());
        // Cannot test the script's functionality directly in JUnit
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html></html>";
        Parser.parse(html, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyBaseUri() {
        String html = "<html></html>";
        Parser.parse(html, "");
    }
}