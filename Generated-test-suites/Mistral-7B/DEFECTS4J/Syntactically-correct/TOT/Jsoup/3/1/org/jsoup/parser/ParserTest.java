package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseBasicHtml() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlTag = doc.elementSibling(0);
        Element headTag = htmlTag.child(0);
        Element titleTag = headTag.child(0);
        Element bodyTag = htmlTag.child(1);
        Element h1Tag = bodyTag.child(0);
        Element pTag = bodyTag.child(1);

        Assert.assertEquals("html", htmlTag.tagName());
        Assert.assertEquals("head", headTag.tagName());
        Assert.assertEquals("title", titleTag.tagName());
        Assert.assertEquals("body", bodyTag.tagName());
        Assert.assertEquals("h1", h1Tag.tagName());
        Assert.assertEquals("p", pTag.tagName());
        Assert.assertEquals("Test Title", titleTag.text());
        Assert.assertEquals("Test Paragraph.", pTag.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Paragraph.</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyTag = doc.body();
        Element pTag = bodyTag.child(0);

        Assert.assertEquals(1, doc.childNodeSize());
        Assert.assertEquals("p", pTag.tagName());
        Assert.assertEquals("Test Paragraph.", pTag.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "http://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        Parser.parse("", "http://example.com");
    }

    @Test
    public void testParseHtmlWithScriptTag() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph.</p><script type=\"text/javascript\">alert('Hello World!');</script></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlTag = doc.elementSibling(0);
        Element headTag = htmlTag.child(0);
        Element titleTag = headTag.child(0);
        Element bodyTag = htmlTag.child(1);
        Element h1Tag = bodyTag.child(0);
        Element pTag = bodyTag.child(1);
        Element scriptTag = bodyTag.child(2);

        Assert.assertEquals("html", htmlTag.tagName());
        Assert.assertEquals("head", headTag.tagName());
        Assert.assertEquals("title", titleTag.tagName());
        Assert.assertEquals("body", bodyTag.tagName());
        Assert.assertEquals("h1", h1Tag.tagName());
        Assert.assertEquals("p", pTag.tagName());
        Assert.assertEquals("script", scriptTag.tagName());

        // Verify script tag is not executed
        doc.executeScript("window.alert('Test')");
    }

    @Test
    public void testParseHtmlWithBaseTag() {
        String html = "<html><head><base href=\"/test\"><title>Test Title</title></head><body><a href=\"about.html\">About</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlTag = doc.elementSibling(0);
        Element headTag = htmlTag.child(0);
        Element baseTag = headTag.child(0);
        Element linkTag = baseTag.child(0);
        Element bodyTag = htmlTag.child(1);
        Element aTag = bodyTag.child(0);

        Assert.assertEquals("html", htmlTag.tagName());
        Assert.assertEquals("head", headTag.tagName());
        Assert.assertEquals("base", baseTag.tagName());
        Assert.assertEquals("a", aTag.tagName());
        Assert.assertEquals("/test", baseTag.absUrl("href"));
        Assert.assertEquals("about.html", aTag.absUrl("href"));
    }

    @Test
    public void testParseHtmlWithImplicitParent() {
        String html = "<img src=\"image.jpg\" alt=\"Test\"><div><p>Test Paragraph.</p></div>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlTag = doc.elementSibling(0);
        Element bodyTag = htmlTag.child(1);
        Element imgTag = bodyTag.child(0);
        Element divTag = bodyTag.child(1);
        Element pTag = divTag.child(0);

        Assert.assertEquals("html", htmlTag.tagName());
        Assert.assertEquals("body", bodyTag.tagName());
        Assert.assertEquals("img", imgTag.tagName());
        Assert.assertEquals("div", divTag.tagName());
        Assert.assertEquals("p", pTag.tagName());

        // img should have been wrapped in an implicit p tag
        Assert.assertEquals(1, imgTag.childNodeSize());
        Assert.assertEquals("p", imgTag.parent().tagName());
        Assert.assertEquals("Test Paragraph.", imgTag.parent().text());
        Assert.assertEquals("Test Paragraph.", pTag.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithInvalidTag() {
        String html = "<html><head><title>Test Title</title></head><body><foo></foo></body></html>";
        Parser.parse(html, "http://example.com");
    }

    @Test
    public void testParseHtmlWithDataTags() {
        String html = "<html><head><title>Test Title</title></head><body><script type=\"text/javascript\">alert('Hello World!');</script><textarea>Test Paragraph.</textarea></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlTag = doc.elementSibling(0);
        Element headTag = htmlTag.child(0);
        Element titleTag = headTag.child(0);
        Element bodyTag = htmlTag.child(1);
        Element scriptTag = bodyTag.child(0);
        Element textareaTag = bodyTag.child(1);

        Assert.assertEquals("html", htmlTag.tagName());
        Assert.assertEquals("head", headTag.tagName());
        Assert.assertEquals("title", titleTag.tagName());
        Assert.assertEquals("body", bodyTag.tagName());
        Assert.assertEquals("script", scriptTag.tagName());
        Assert.assertEquals("textarea", textareaTag.tagName());
        Assert.assertTrue(scriptTag.data().isEmpty());
        Assert.assertEquals("Test Paragraph.", textareaTag.text());
    }

    @Test
    public void testParseHtmlWithEmptyElements() {
        String html = "<html><head><title>Test Title</title></head><body><img src=\"image.jpg\"/><input type=\"submit\"/></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlTag = doc.elementSibling(0);
        Element headTag = htmlTag.child(0);
        Element titleTag = headTag.child(0);
        Element bodyTag = htmlTag.child(1);
        Element imgTag = bodyTag.child(0);
        Element inputTag = bodyTag.child(1);

        Assert.assertEquals("html", htmlTag.tagName());
        Assert.assertEquals("head", headTag.tagName());
        Assert.assertEquals("title", titleTag.tagName());
        Assert.assertEquals("body", bodyTag.tagName());
        Assert.assertEquals("img", imgTag.tagName());
        Assert.assertEquals("input", inputTag.tagName());
        Assert.assertTrue(imgTag.text().isEmpty());
        Assert.assertTrue(inputTag.text().isEmpty());
    }

    @Test
    public void testParseHtmlWithNestedElements() {
        String html = "<html><head><title>Test Title</title></head><body><p>Nested <a href=\"test.html\">Link</a>.</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlTag = doc.elementSibling(0);
        Element headTag = htmlTag.child(0);
        Element titleTag = headTag.child(0);
        Element bodyTag = htmlTag.child(1);
        Element pTag = bodyTag.child(0);
        Element aTag = pTag.child(0);

        Assert.assertEquals("html", htmlTag.tagName());
        Assert.assertEquals("head", headTag.tagName());
        Assert.assertEquals("title", titleTag.tagName());
        Assert.assertEquals("body", bodyTag.tagName());
        Assert.assertEquals("p", pTag.tagName());
        Assert.assertEquals("a", aTag.tagName());
        Assert.assertEquals("Nested", aTag.text());
        Assert.assertEquals("Link", aTag.absUrl("href"));
    }

    @Test
    public void testParseHtmlWithAttributes() {
        String html = "<html><head><title>Test Title</title></head><body><a href=\"test.html\" id=\"linkId\" class=\"linkClass\">Test Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlTag = doc.elementSibling(0);
        Element headTag = htmlTag.child(0);
        Element titleTag = headTag.child(0);
        Element bodyTag = htmlTag.child(1);
        Element aTag = bodyTag.child(0);

        Assert.assertEquals("html", htmlTag.tagName());
        Assert.assertEquals("head", headTag.tagName());
        Assert.assertEquals("title", titleTag.tagName());
        Assert.assertEquals("body", bodyTag.tagName());
        Assert.assertEquals("a", aTag.tagName());
        Assert.assertEquals("test.html", aTag.absUrl("href"));
        Assert.assertEquals("linkId", aTag.attribute("id").getValue());
        Assert.assertEquals("linkClass", aTag.attribute("class").getValue());
    }
}