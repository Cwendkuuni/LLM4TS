package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        String html = "";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element root = doc.body();
        Assert.assertNull(root);
    }

    @Test
    public void testParseHtmlWithSimpleStructure() {
        String html = "<html><head><title>Test Parser</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element root = doc.body();
        Element title = root.getElementsByTag("title").first();
        Assert.assertEquals("Test Parser", title.text());
    }

    @Test
    public void testParseHtmlWithEmptyTag() {
        String html = "<html><head></head><body><img src='image.jpg'/></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element root = doc.body();
        Element img = root.getElementsByTag("img").first();
        Assert.assertNotNull(img);
        Assert.assertNull(img.text());
    }

    @Test
    public void testParseHtmlWithSelfClosingTag() {
        String html = "<html><head></head><body><img src='image.jpg'/></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element root = doc.body();
        Element img = root.getElementsByTag("img").first();
        Assert.assertNotNull(img);
        Assert.assertEquals("image.jpg", img.attr("src"));
    }

    @Test
    public void testParseHtmlWithAttributes() {
        String html = "<html><head></head><body><a href='http://example.com' target='_blank'>Link</a></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element root = doc.body();
        Element link = root.getElementsByTag("a").first();
        Assert.assertEquals("Link", link.text());
        Assert.assertEquals("http://example.com", link.attr("href"));
        Assert.assertEquals("_blank", link.attr("target"));
    }

    @Test
    public void testParseHtmlWithComments() {
        String html = "<html><!-- This is a comment --><head><title>Test Parser</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element root = doc.body();
        Comment comment = root.getElementsByTag("comment").first();
        Assert.assertNull(comment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithInvalidTag() {
        String html = "<html><head></head><body><invalid></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse(); // This should throw an exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithMissingTagEnd() {
        String html = "<html><head></head><body><input type='text' name='username' />";
        Parser parser = new Parser(html, "http://example.com");
        parser.parse(); // This should throw an exception
    }

    @Test
    public void testParseHtmlWithScriptTag() {
        String html = "<html><head><script src='script.js'></script></head><body></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element root = doc.body();
        Element script = root.getElementsByTag("script").first();
        Assert.assertEquals("script.js", script.attr("src"));
    }

    @Test
    public void testParseHtmlWithStyleTag() {
        String html = "<html><head><style>body { background-color: red; }</style></head><body></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element root = doc.body();
        String expectedCss = "body { background-color: red; }";
        String actualCss = doc.select("style").outerHtml();
        Assert.assertEquals(expectedCss, actualCss);
    }

    @Test
    public void testParseHtmlWithBaseTag() {
        String html = "<html><head><base href='/subfolder'></head><body><a href='page.html'>Link</a></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();
        Element link = doc.select("a").first();
        String expectedLink = "http://example.com/subfolder/page.html";
        String actualLink = link.attr("abs:href");
        Assert.assertEquals(expectedLink, actualLink);
    }
}