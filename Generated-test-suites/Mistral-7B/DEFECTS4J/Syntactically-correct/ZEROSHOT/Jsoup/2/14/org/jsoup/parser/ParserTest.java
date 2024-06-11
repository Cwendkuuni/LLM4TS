package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.LinkedList;
import java.util.List;

public class ParserTest {

    @org.junit.Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1>" +
                "<p>Test paragraph <a href=\"http://test.com\">Link</a></p></body></html>";

        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.select("h1").get(0).text());
        assertEquals("Test paragraph Link", doc.select("p a").get(0).text());
        assertEquals("http://test.com", doc.baseUri());
    }

    @org.junit.Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test paragraph <a href=\"http://test.com\">Link</a></p>";

        Parser parser = new Parser(bodyHtml, "http://test.com");
        Document doc = parser.parseBodyFragment();

        assertEquals("<p>Test paragraph <a href=\"http://test.com\">Link</a></p>", doc.body().html());
        assertEquals("http://test.com", doc.baseUri());
    }

    @org.junit.Test
    public void testParseInvalidHtml() {
        String html = "<html><!-- Test Comment -->" +
                "<head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1>" +
                "<p>Test paragraph <a href=\"http://test.com\">Link</a></p>" +
                "<!-- Test Comment -->" +
                "<script type=\"text/javascript\">function test() { alert('Hello World!'); }</script>" +
                "</body></html>";

        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        // Test comment should be parsed correctly
        Element comment = doc.select("--.*-->").get(0);
        assertEquals("Test Comment", comment.text());

        // Script tag should be parsed as a DataNode
        Element script = doc.select("script").get(0);
        assertTrue(script.childNodes().size() > 0);
        Element dataNode = (Element) script.childNodes().get(0);
        assertEquals("function test() { alert('Hello World!'); }", dataNode.text());
    }

    @org.junit.Test
    public void testParseBaseTag() {
        String html = "<html><head></head><body>" +
                "<base href=\"http://new.test.com\"/>" +
                "<a href=\"/about\">About</a></body></html>";

        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        assertEquals("http://new.test.com", doc.baseUri());
        assertEquals("http://new.test.com/about", doc.select("a").get(0).absUrl("href"));
    }

    @org.junit.Test
    public void testParseImplicitParent() {
        String html = "<div><img src=\"image.png\"></div>";

        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        Element img = doc.select("img").get(0);
        assertEquals("http://test.com/image.png", img.absUrl("src"));

        Element div = img.parent();
        assertEquals("div", div.tagName());
    }

    @org.junit.Test
    public void testParseInvalidStartTag() {
        String html = "<html><!-- Test Comment -->" +
                "<head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1>" +
                "<p>Test paragraph <a href=\"http://test.com\">Link</a></p>" +
                "<!-- Test Comment -->" +
                "<script type=\"text/javascript\">function test() { alert('Hello World!'); }</script>" +
                "<div><h1>Invalid Heading</h1>" + // this tag should be ignored
                "</body></html>";

        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        // Test comment and script tags should be parsed correctly
        Element comment = doc.select("--.*-->").get(0);
        assertEquals("Test Comment", comment.text());

        Element script = doc.select("script").get(0);
        assertTrue(script.childNodes().size() > 0);
        Element dataNode = (Element) script.childNodes().get(0);
        assertEquals("function test() { alert('Hello World!'); }", dataNode.text());

        // The invalid h1 tag should be ignored
        List<Element> h1Tags = doc.select("h1");
        assertEquals(1, h1Tags.size());
        Element validH1 = h1Tags.get(0);

        // Test that the document still has the correct structure and elements
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", validH1.text());
        assertEquals("Test paragraph Link", doc.select("p a").get(0).text());
        assertEquals("http://test.com", doc.baseUri());
    }

    @org.junit.Test
    public void testParseEmptyElement() {
        String html = "<img src=\"image.png\" />";

        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        Element img = doc.select("img").get(0);
        assertEquals("http://test.com/image.png", img.absUrl("src"));
        assertTrue(img.tag().isEmpty());
    }

    @org.junit.Test
    public void testParseEmptyBody() {
        String html = "<html><head></head><body></body></html>";

        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        assertEquals(StringUtils.EMPTY, doc.title().text());
        assertEquals(0, doc.body().childNodes().size());
    }

    @org.junit.Test
    public void testParseEmptyDocument() {
        String html = "<html></html>";

        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        assertEquals(StringUtils.EMPTY, doc.title().text());
        assertEquals(0, doc.body().childNodes().size());
        assertEquals(StringUtils.EMPTY, doc.html().trim());
    }
}