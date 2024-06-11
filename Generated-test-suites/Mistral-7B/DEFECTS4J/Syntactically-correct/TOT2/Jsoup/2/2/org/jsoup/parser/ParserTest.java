package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParse() {
        // Test case for parsing a valid HTML document
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Parser</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello, world!</p>\n" +
                "</body>\n" +
                "</html>";

        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();

        Element root = doc.body();
        Element title = root.head();
        Element p = root.getElementsByTag("p").get(0);

        assertEquals("Test Parser", title.text());
        assertEquals("Hello, world!", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        // Test case for parsing a valid HTML body fragment
        String bodyHtml = "<p>Fragment: Hello, world!</p>";

        Parser parser = new Parser(bodyHtml, "http://example.com", true);
        Document doc = parser.parse();

        Element root = doc;
        Element p = root.getElementsByTag("p").get(0);

        assertEquals("Fragment: Hello, world!", p.text());
    }

    @Test
    public void testParseInvalidHtml() {
        // Test case for parsing an invalid HTML document
        String html = "<html<";

        try {
            Parser parser = new Parser(html, "http://example.com", false);
            parser.parse();
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // Expected
        }
    }

    @Test
    public void testParseComment() {
        // Test case for parsing a comment
        String html = "<!-- This is a comment -->";

        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();

        Element comment = doc.body().getElementsByTag("comment").get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDeclaration() {
        // Test case for parsing an XML declaration
        String html = "<<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>";

        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();

        Element xmlDeclaration = doc.body().getElementsByTag("xml-declaration").get(0);
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.toString());
    }

    @Test
    public void testParseEndTag() {
        // Test case for parsing an end tag
        String html = "<p>Text<br/><br/></p>";

        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();

        Element p = doc.body().getElementsByTag("p").get(0);
        assertEquals(2, p.childNodes().size());
    }

    @Test
    public void testParseStartTag() {
        // Test case for parsing a start tag
        String html = "<p id='p1' class='pClass'><strong>Text</strong></p>";

        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();

        Element p = doc.body().getElementsByTag("p").get(0);
        assertEquals("p1", p.id());
        assertEquals("pClass", StringUtils.join(p.classes(), " "));
        assertEquals(2, p.childNodes().size());
        Element strong = p.getElementsByTag("strong").get(0);
        assertEquals("Text", strong.text());
    }

    @Test
    public void testParseAttribute() {
        // Test case for parsing an attribute
        String html = "<input type='text' name='username' value='Test'>";

        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();

        Element input = doc.body().getElementsByTag("input").get(0);
        assertEquals("text", input.tagName());
        assertEquals("username", input.attr("name"));
        assertEquals("Test", input.attr("value"));
    }

    @Test
    public void testParseTextNode() {
        // Test case for parsing a text node
        String html = "<p>Hello, world!</p>";

        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();

        Element p = doc.body().getElementsByTag("p").get(0);
        assertEquals("Hello, world!", p.text());
    }

    @Test
    public void testParseCdata() {
        // Test case for parsing a CDATA section
        String html = "<script><![CDATA[function test() { alert('Hello, world!'); }]]></script>";

        Parser parser = new Parser(html, "http://example.com", false);
        Document doc = parser.parse();

        Element script = doc.body().getElementsByTag("script").get(0);
        assertEquals("[CDATA[function test() { alert('Hello, world!'); }]]", script.outerHtml());
    }
}