package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;

import java.util.LinkedList;

import org.junit.Test;

/**
 * Test class for Parser.java.
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1><p>Test Paragraph.</p></body></html>";
        Document document = Parser.parse(html, "https://example.com");

        Element htmlNode = document.body();
        assertEquals("html", htmlNode.tagName());
        Element headNode = document.head();
        assertEquals("head", headNode.tagName());
        Element titleNode = headNode.child(0);
        assertEquals("title", titleNode.tagName());
        assertEquals("Test Title", titleNode.text());

        Element h1Node = htmlNode.child(0);
        assertEquals("h1", h1Node.tagName());
        assertEquals("Test Heading", h1Node.text());

        Element pNode = htmlNode.child(1);
        assertEquals("p", pNode.tagName());
        assertEquals("Test Paragraph.", pNode.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Paragraph.</p>";
        Document document = Parser.parseBodyFragment(bodyHtml, "https://example.com");

        Element htmlNode = document.body();
        assertEquals("body", htmlNode.tagName());
        Element pNode = htmlNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Test Paragraph.", pNode.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Test Paragraph.</p></div>"; // div tag is not a known tag in the given parser
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");

        Element htmlNode = document.body();
        assertEquals("body", htmlNode.tagName());
        Element divNode = htmlNode.child(0);
        assertEquals("div", divNode.tagName());
        Element pNode = divNode.child(0);
        assertEquals("p", pNode.tagName());
        assertEquals("Test Paragraph.", pNode.text());
    }
}