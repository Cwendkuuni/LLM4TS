package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

    @Test
    public void testParseBasic() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element htmlNode = doc;
        assertEquals(Tag.HTML, htmlNode.tag());
        assertEquals("http://example.com", htmlNode.baseUri());

        Element headNode = htmlNode.child(0);
        assertEquals(Tag.HEAD, headNode.tag());

        Element titleNode = headNode.child(0);
        assertEquals(Tag.TITLE, titleNode.tag());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals(Tag.BODY, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(Tag.H1, h1Node.tag());
        assertEquals("Hello World", h1Node.text());

        doc.clear(); // clean up for next test
    }

    @Test
    public void testParseFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc;
        assertEquals(Tag.BODY, bodyNode.tag());
        assertEquals("http://example.com", bodyNode.baseUri());

        Element divNode = bodyNode.child(0);
        assertEquals(Tag.DIV, divNode.tag());

        Element pNode = divNode.child(0);
        assertEquals(Tag.P, pNode.tag());
        assertEquals("Test", pNode.text());

        doc.clear(); // clean up for next test
    }

    @Test
    public void testParseFragmentRelaxed() {
        String bodyHtml = "<div><p>Test</p></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        Element bodyNode = doc;
        assertEquals(Tag.BODY, bodyNode.tag());
        assertEquals("http://example.com", bodyNode.baseUri());

        Element divNode = bodyNode.child(0);
        assertEquals(Tag.DIV, divNode.tag());

        Element pNode = divNode.child(0);
        assertEquals(Tag.P, pNode.tag());
        assertEquals("Test", pNode.text());

        // test relax parsing without div tag
        String bodyHtml2 = "<p>Test</p>";
        Document doc2 = Parser.parseBodyFragmentRelaxed(bodyHtml2, "http://example.com");
        Element pNode2 = doc2.body().child(0);
        assertEquals(Tag.P, pNode2.tag());
        assertEquals("Test", pNode2.text());

        doc.clear(); // clean up for next test
        doc2.clear(); // clean up for next test
    }

    @Test
    public void testParseError() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World<script>alert('Hello')</script></body></html>";
        Parser parser = new Parser(html, "http://example.com");

        try {
            parser.parse();
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // expected
        }

        doc.clear(); // clean up for next test
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";
        Parser parser = new Parser(html, "http://example.com");
        Document doc = parser.parse();

        Element htmlNode = doc;
        assertEquals(Tag.HTML, htmlNode.tag());

        Element bodyNode = htmlNode.child(1);
        assertEquals(Tag.BODY, bodyNode.tag());

        Element commentNode = htmlNode.child(0);
        assertNotNull(commentNode);
        assertEquals(Tag.COMMENT, commentNode.tag());
        assertEquals("This is a comment", commentNode.text());

        doc.clear(); // clean up for next test
    }
}