package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Tests for the {@link Parser} class.
 */
public class ParserTest {

    @Test
    public void testParseSimpleHtml() throws IOException {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertTrue(htmlNode.tagName().equals("html"));
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element titleNode = doc.head().title();
        assertEquals("Test Title", titleNode.text());
        assertEquals("Test Title", titleNode.textBeforeSelf().toString());
        assertEquals("http://example.com/", titleNode.baseUri());

        Element headingNode = doc.body().get(0);
        assertEquals("h1", headingNode.tagName());
        assertEquals("Test Heading", headingNode.text());
    }

    @Test
    public void testParseEmptyHtml() throws IOException {
        String html = "<html></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertTrue(htmlNode.tagName().equals("html"));
        assertEquals(0, doc.head().childNodeSize());
        assertEquals(0, doc.body().childNodeSize());
    }

    @Test
    public void testParseSelfClosingTag() throws IOException {
        String html = "<html><img src=\"image.png\"/></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element imgNode = doc.body().get(0);
        assertTrue(imgNode.tagName().equals("img"));
        assertEquals(1, imgNode.childNodeSize());

        Element srcNode = imgNode.get(0);
        assertEquals("src", srcNode.tagName());
        assertEquals("image.png", srcNode.attr("src"));
    }

    @Test
    public void testParseEmptySelfClosingTag() throws IOException {
        String html = "<html><img/></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(0, doc.body().childNodeSize());

        Element imgNode = doc.body();
        assertTrue(imgNode.tagName().equals("img"));
        assertEquals(0, imgNode.childNodeSize());
    }

    @Test
    public void testParseDataTag() throws IOException {
        String html = "<html><script type=\"text/javascript\">alert('Hello World');</script></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element scriptNode = doc.body().get(0);
        assertEquals("script", scriptNode.tagName());
        assertEquals("text/javascript", scriptNode.attr("type"));
        assertEquals("alert('Hello World');", scriptNode.text());
    }

    @Test
    public void testParseTextareaTag() throws IOException {
        String html = "<html><textarea>Hello World</textarea></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element textareaNode = doc.body().get(0);
        assertEquals("textarea", textareaNode.tagName());
        assertEquals("Hello World", textareaNode.text());
    }

    @Test
    public void testParseBaseTag() throws IOException {
        String html = "<html><base href=\"/subdir\"/><body><a href=\"relative.html\">Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com/");

        assertEquals("http://example.com/subdir/relative.html", doc.baseUri());
        assertEquals("Link", doc.body().get(0).text());
    }

    @Test
    public void testParseComment() throws IOException {
        String html = "<html><!-- This is a comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(0, doc.body().childNodeSize());

        Comment commentNode = doc.head().child(0);
        assertEquals("This is a comment", commentNode.data());
    }

    @Test
    public void testParseCdata() throws IOException {
        String html = "<html><![CDATA[This is CDATA]]></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(0, doc.body().childNodeSize());

        DataNode cdataNode = doc.head().child(0);
        assertEquals("This is CDATA", cdataNode.text());
    }

    @Test
    public void testParseInvalidHtml() throws IOException {
        String html = "<html><body></html<script>alert('Hello World');</script>";
        try {
            Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseRelaxedBodyFragment() throws IOException {
        String html = "<script type=\"text/javascript\">alert('Hello World');</script>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(0, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element scriptNode = doc.body().get(0);
        assertEquals("script", scriptNode.tagName());
        assertEquals("text/javascript", scriptNode.attr("type"));
        assertEquals("alert('Hello World');", scriptNode.text());
    }

    @Test
    public void testParseUnknownTag() throws IOException {
        String html = "<html><unknown></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element unknownNode = doc.body().get(0);
        assertEquals("unknown", unknownNode.tagName());
        assertEquals(0, unknownNode.childNodeSize());
    }

    @Test
    public void testParseEmptyUnknownTag() throws IOException {
        String html = "<html><unknown/></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(0, doc.body().childNodeSize());

        Element unknownNode = doc.body();
        assertEquals("unknown", unknownNode.tagName());
        assertEquals(0, unknownNode.childNodeSize());
    }

    @Test
    public void testParseInvalidTagName() throws IOException {
        String html = "<html><hxx>Hello World</hxx></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseTagWithImplicitParent() throws IOException {
        String html = "<html><p><strong>Hello World</strong></p></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(1, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element pNode = doc.body().get(0);
        assertEquals("p", pNode.tagName());
        assertEquals(1, pNode.childNodeSize());

        Element strongNode = pNode.get(0);
        assertEquals("strong", strongNode.tagName());
        assertEquals("Hello World", strongNode.text());
    }

    @Test
    public void testParseTagWithImplicitParentAndRelaxedParsing() throws IOException {
        String html = "<html><p><strong>Hello World</strong></p></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(0, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element strongNode = doc.body();
        assertEquals("strong", strongNode.tagName());
        assertEquals("Hello World", strongNode.text());
    }

    @Test
    public void testParseTagWithInvalidParent() throws IOException {
        String html = "<html><div><p>Hello World</p></div></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseTagWithInvalidParentAndRelaxedParsing() throws IOException {
        String html = "<html><div><p>Hello World</p></div></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(0, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element pNode = doc.body();
        assertEquals("p", pNode.tagName());
        assertEquals("Hello World", pNode.text());
    }

    @Test
    public void testParseTagWithNoParent() throws IOException {
        String html = "<html><html/></html>";
        try {
            Parser.parse(html, "http://example.com");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseTagWithNoParentAndRelaxedParsing() throws IOException {
        String html = "<html><html/></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(0, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element htmlNode2 = doc.body().get(0);
        assertEquals("html", htmlNode2.tagName());
        assertEquals(0, htmlNode2.childNodeSize());
    }

    @Test
    public void testParseTagWithNoParentAndImplicitParent() throws IOException {
        String html = "<html><body></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");

        Element htmlNode = doc.body();
        assertEquals(0, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element bodyNode = doc.body();
        assertEquals("body", bodyNode.tagName());
        assertEquals(0, bodyNode.childNodeSize());
    }

    @Test
    public void testParseWithBaseUri() throws IOException {
        String html = "<html><base href=\"/subdir\"/><body><a href=\"relative.html\">Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com/anotherdir");

        assertEquals("http://example.com/subdir/relative.html", doc.baseUri());
        assertEquals("Link", doc.body().get(0).text());
    }

    @Test
    public void testParseWithBaseUriAndRelaxedParsing() throws IOException {
        String html = "<html><base href=\"/subdir\"/><body><a href=\"relative.html\">Link</a></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com/anotherdir");

        assertEquals("http://example.com/subdir/relative.html", doc.baseUri());
        assertEquals("Link", doc.body().get(0).text());
    }

    @Test
    public void testParseWithEmptyBaseUri() throws IOException {
        String html = "<html><base href=\"\"/><body><a href=\"relative.html\">Link</a></body></html>";
        Document doc = Parser.parse(html, "http://example.com/anotherdir");

        assertEquals("http://example.com/relative.html", doc.baseUri());
        assertEquals("Link", doc.body().get(0).text());
    }

    @Test
    public void testParseWithEmptyBaseUriAndRelaxedParsing() throws IOException {
        String html = "<html><base href=\"\"/><body><a href=\"relative.html\">Link</a></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com/anotherdir");

        assertEquals("http://example.com/relative.html", doc.baseUri());
        assertEquals("Link", doc.body().get(0).text());
    }

    @Test
    public void testParseWithEmptyBaseUriAndImplicitParent() throws IOException {
        String html = "<html><base href=\"\"/><body><p><strong>Hello World</strong></p></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com/anotherdir");

        Element htmlNode = doc.body();
        assertEquals(0, doc.head().childNodeSize());
        assertEquals(1, doc.body().childNodeSize());

        Element pNode = doc.body().get(0);
        assertEquals("p", pNode.tagName());
        assertEquals(1, pNode.childNodeSize());

        Element strongNode = pNode.get(0);
        assertEquals("strong", strongNode.tagName());
        assertEquals("Hello World", strongNode.text());
    }
}