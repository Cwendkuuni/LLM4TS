package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        this.parser = new Parser(html, "http://test.com", false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().child(0).text());
    }

    @Test
    public void testParseComment() {
        parser.tq.addFirst("<!-- This is a comment -->");
        parser.parse();
        Element comment = parser.last().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        parser.tq.addFirst("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        parser.parse();
        XmlDeclaration xmlDecl = (XmlDeclaration) parser.last().child(0);
        assertEquals("UTF-8", xmlDecl.encoding());
        assertEquals("1.0", xmlDecl.version());
    }

    @Test
    public void testParseEndTag() {
        parser.tq.addFirst("</h1>");
        parser.parse();
        Element h1 = parser.last().child(0);
        assertTrue(h1.tag().isEmpty());
    }

    @Test
    public void testParseStartTag() {
        parser.tq.addFirst("<h1 class=\"test\">Hello World</h1>");
        parser.parse();
        Element h1 = parser.last().child(0);
        assertEquals(Tag.H1, h1.tag());
        assertEquals("test", h1.classes().get(0));
        assertEquals("Hello World", h1.child(0).text());
    }

    @Test
    public void testParseAttribute() {
        parser.tq.addFirst("<img src=\"image.jpg\" alt=\"Image\">");
        parser.parse();
        Element img = parser.last().child(0);
        Attribute src = img.attr("src");
        assertEquals("image.jpg", src.value());
        Attribute alt = img.attr("alt");
        assertEquals("Image", alt.value());
    }

    @Test
    public void testParseTextNode() {
        parser.tq.addFirst("Hello World");
        parser.parse();
        TextNode textNode = (TextNode) parser.last().child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        parser.tq.addFirst("<![CDATA[Hello World]]>");
        parser.parse();
        TextNode textNode = (TextNode) parser.last().child(0);
        assertEquals("Hello World", textNode.text());
    }
}