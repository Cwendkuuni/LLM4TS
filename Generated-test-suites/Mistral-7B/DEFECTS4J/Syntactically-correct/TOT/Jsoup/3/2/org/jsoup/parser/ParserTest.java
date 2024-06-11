package org.jsoup.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.util.Arrays;

public class ParserTest {

    @Test
    public void testParseSimpleHtml() throws IOException {
        String html = "<html><head><title>Test Title</title></head>" +
                "<<body><h1>Test Heading</h1><p>Test Paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        Element htmlNode = doc;
        Assertions.assertEquals("html", htmlNode.tagName());

        Element headNode = htmlNode.selectFirst("head");
        Assertions.assertNotNull(headNode);
        Assertions.assertEquals("head", headNode.tagName());

        Element titleNode = headNode.selectFirst("title");
        Assertions.assertNotNull(titleNode);
        Assertions.assertEquals("title", titleNode.tagName());
        Assertions.assertEquals("Test Title", titleNode.text());

        Element bodyNode = htmlNode.body();
        Assertions.assertNotNull(bodyNode);
        Assertions.assertEquals("body", bodyNode.tagName());

        Element h1Node = bodyNode.selectFirst("h1");
        Assertions.assertNotNull(h1Node);
        Assertions.assertEquals("h1", h1Node.tagName());
        Assertions.assertEquals("Test Heading", h1Node.text());

        Element pNode = bodyNode.selectFirst("p");
        Assertions.assertNotNull(pNode);
        Assertions.assertEquals("p", pNode.tagName());
        Assertions.assertEquals("Test Paragraph.", pNode.text());
    }

    @Test
    public void testParseEmptyHtml() throws IOException {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "https://example.com");

        Element htmlNode = doc;
        Assertions.assertEquals("html", htmlNode.tagName());

        Element headNode = htmlNode.head();
        Assertions.assertNull(headNode);

        Element bodyNode = htmlNode.body();
        Assertions.assertNotNull(bodyNode);
        Assertions.assertEquals("body", bodyNode.tagName());
        Assertions.assertTrue(bodyNode.children().isEmpty());
    }

    @Test
    public void testParseInvalidHtml() throws IOException {
        String html = "<html<title>Test Title</html>";
        Assertions.assertThrows(IllegalArgumentException.class, () -> Parser.parse(html, "https://example.com"));

        String html2 = "<html><title><script>alert('Hello')</script>";
        Assertions.assertThrows(IllegalArgumentException.class, () -> Parser.parse(html2, "https://example.com"));
    }

    @Test
    public void testParseBodyFragment() throws IOException {
        String bodyHtml = "<body><p>Test Paragraph.</p></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com");

        Element htmlNode = doc;
        Assertions.assertEquals("html", htmlNode.tagName());

        Element headNode = htmlNode.head();
        Assertions.assertNull(headNode);

        Element bodyNode = htmlNode.body();
        Assertions.assertNotNull(bodyNode);
        Assertions.assertEquals("body", bodyNode.tagName());

        Element pNode = bodyNode.selectFirst("p");
        Assertions.assertNotNull(pNode);
        Assertions.assertEquals("p", pNode.tagName());
        Assertions.assertEquals("Test Paragraph.", pNode.text());

        Assertions.assertTrue(htmlNode.children().size() == 1);
    }

    @Test
    public void testParseCdata() throws IOException {
        String html = "<html><![CDATA[Hello World]<script>alert('Hello');</script>]]><body></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        Element htmlNode = doc;
        Assertions.assertEquals("html", htmlNode.tagName());

        Element bodyNode = htmlNode.body();
        Assertions.assertNotNull(bodyNode);
        Assertions.assertEquals("body", bodyNode.tagName());
        Assertions.assertTrue(bodyNode.children().isEmpty());

        Element cdataNode = htmlNode.selectFirst("html > [CDATA[Hello World]]");
        Assertions.assertNotNull(cdataNode);
        Assertions.assertEquals("[CDATA[Hello World]", cdataNode.outerHtml());
    }

    @Test
    public void testParseComment() throws IOException {
        String html = "<html><!-- Test Comment --><body></body></html>";
        Document doc = Parser.parse(html, "https://example.com");

        Element htmlNode = doc;
        Assertions.assertEquals("html", htmlNode.tagName());

        Element bodyNode = htmlNode.body();
        Assertions.assertNotNull(bodyNode);
        Assertions.assertEquals("body", bodyNode.tagName());
        Assertions.assertTrue(bodyNode.children().isEmpty());

        Element commentNode = htmlNode.selectFirst("html > comment()");
        Assertions.assertNotNull(commentNode);
        Assertions.assertEquals("-- >\n-- Test Comment \n<!--", commentNode.outerHtml());
    }

    @Test
    public void testParseXmlDeclaration() throws IOException {
        String html = "<html xmlns:xlink=" + DQ + "http://www.w3.org/1999/xlink\" xml:lang=\"en\">";
        Document doc = Parser.parse(html, "https://example.com");

        Element htmlNode = doc;
        Assertions.assertEquals("html", htmlNode.tagName());

        XmlDeclaration xmlDecl = htmlNode.selectFirst("xml-declaration()");
        Assertions.assertNotNull(xmlDecl);

        Assertions.assertFalse(xmlDecl.isStandalone());
        Assertions.assertEquals(Arrays.asList("xmlns:xlink", "http://www.w3.org/1999/xlink", "xml:lang", "en"), xmlDecl.xmlDeclAttributes().asList());
    }
}