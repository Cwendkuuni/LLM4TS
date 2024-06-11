package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.xml.XmlDeclaration;

import org.junit.Test;

/**
 * Test class for Parser.java
 *
 * @author [Your Name]
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(String, String)}
     */
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals("Test", doc.title());
        assertEquals("http://example.com", doc.baseUri());

        Element title = doc.title();
        assertEquals("Test", title.text());
        assertEquals(1, title.childNodes().size());

        Element body = doc.body();
        assertEquals("Hello World!", body.text());
        assertEquals(1, body.childNodes().size());

        Element h1 = body.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World!", h1.text());
        assertEquals(0, h1.childNodes().size());
    }

    /**
     * Test method for {@link Parser#parseBodyFragment(String, String)}
     */
    @Test
    public void testParseBodyFragment() {
        String html = "<body><h1>Hello World!</h1></body>";
        Document doc = Parser.parseBodyFragment(html, "http://example.com");
        assertEquals("http://example.com", doc.baseUri());

        Element body = doc.body();
        assertEquals("Hello World!", body.text());
        assertEquals(1, body.childNodes().size());

        Element h1 = body.child(0);
        assertEquals("h1", h1.tagName());
        assertEquals("Hello World!", h1.text());
        assertEquals(0, h1.childNodes().size());
    }

    /**
     * Test method for {@link Parser#parseBodyFragmentRelaxed(String, String)}
     */
    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<div><script>alert('Hello World!');</script></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://example.com");
        assertEquals("http://example.com", doc.baseUri());

        Element div = doc.body().child(0);
        assertEquals("div", div.tagName());
        assertEquals(1, div.childNodes().size());

        Element script = div.child(0);
        assertEquals("script", script.tagName());
        assertEquals(0, script.childNodes().size());
    }

    /**
     * Test method for {@link Parser#parseComment()}
     */
    @Test
    public void testParseComment() {
        String html = "<html><!-- Test Comment --></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(1, doc.children().size());
        Element htmlNode = doc.child(0);
        assertEquals("html", htmlNode.tagName());
        assertEquals(1, htmlNode.childNodes().size());

        Comment comment = htmlNode.child(0);
        assertEquals("Test Comment", comment.data());
        assertEquals(0, comment.childNodes().size());
    }

    /**
     * Test method for {@link Parser#parseXmlDecl()}
     */
    @Test
    public void testParseXmlDecl() {
        String html = "<xml version='1.0' encoding='UTF-8' standalone='yes'><![CDATA[Test]]></xml>";
        Document doc = Parser.parse(html, "http://example.com");
        assertEquals(1, doc.children().size());
        Element xmlNode = doc.child(0);
        assertEquals("xml", xmlNode.tagName());
        assertEquals("http://example.com", xmlNode.baseUri());
        assertEquals(1, xmlNode.childNodes().size());

        XmlDeclaration xmlDeclaration = xmlNode.child(0);
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertEquals("yes", xmlDeclaration.isStandalone());
        assertEquals(1, xmlDeclaration.childNodes().size());

        DataNode cdataNode = xmlDeclaration.child(0);
        assertEquals("Test", cdataNode.text());
        assertEquals(0, cdataNode.childNodes().size());
    }

    /**
     * Test method for {@link Parser#parseEndTag()}
     */
    @Test
    public void testParseEndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element title = doc.title();
        Element body = doc.body();
        Element h1 = body.child(0);

        Parser parser = new Parser(doc.html(), "http://example.com", false);
        parser.parse();

        Element title2 = parser.doc.title();
        Element body2 = parser.doc.body();
        Element h12 = body2.child(0);

        assertEquals(title, title2);
        assertEquals(body, body2);
        assertEquals(h1, h12);

        parser.popStackToClose(titleTag);
        assertEquals(null, parser.doc.title());

        parser.popStackToClose(bodyTag);
        assertEquals(null, parser.doc.body());
    }

    /**
     * Test method for {@link Parser#parseStartTag()}
     */
    @Test
    public void testParseStartTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Parser parser = new Parser(doc.html(), "http://example.com", false);
        parser.parse();

        Element title = parser.last().child(0);
        Element body = parser.last();
        Element h1 = parser.last().child(0);

        Element title2 = parser.popStackToSuitableContainer(titleTag);
        assertEquals(title, title2);

        Element body2 = parser.popStackToSuitableContainer(bodyTag);
        assertEquals(body, body2);

        Element h12 = parser.popStackToSuitableContainer(h1.tag());
        assertEquals(h1, h12);

        assertNull(parser.last());

        parser.parseStartTag();
        assertEquals(parser.last().tagName(), "html");
    }

    /**
     * Test method for {@link Parser#parseAttribute()}
     */
    @Test
    public void testParseAttribute() {
        String html = "<img src='image.jpg' alt='Alternate Text' title='Title'/>";
        Document doc = Parser.parse(html, "http://example.com");

        Element img = doc.select("img").first();

        Parser parser = new Parser(doc.html(), "http://example.com", false);
        parser.parse();

        Attribute src = parser.last().attributes().get("src");
        Attribute alt = parser.last().attributes().get("alt");
        Attribute title = parser.last().attributes().get("title");

        assertEquals("image.jpg", src.getValue());
        assertEquals("Alternate Text", alt.getValue());
        assertEquals("Title", title.getValue());
    }

    /**
     * Test method for {@link Parser#parseTextNode()}
     */
    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Hello World!</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Parser parser = new Parser(doc.html(), "http://example.com", false);
        parser.parse();

        TextNode textNode = parser.last();
        assertEquals("Hello World!", textNode.text());
    }

    /**
     * Test method for {@link Parser#parseCdata()}
     */
    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[Hello World!]]></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Parser parser = new Parser(doc.html(), "http://example.com", false);
        parser.parse();

        DataNode cdataNode = parser.last();
        assertEquals("Hello World!", cdataNode.text());
    }

    /**
     * Test method for {@link Parser#addChildToParent(Element, boolean)}
     */
    @Test
    public void testAddChildToParent() {
        String html = "<html><body><div><p>Hello World!</p></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Parser parser = new Parser(doc.html(), "http://example.com", false);
        parser.parse();

        Element div = parser.last().child(0);
        Element p = parser.popStackToSuitableContainer(pTag);

        Element parent = parser.addChildToParent(p, false);
        assertSame(div, parent);

        Element child = parser.addChildToParent(new Element(textareaTag, "http://example.com"), false);
        assertSame(div, child.parent());

        assertEquals("Hello World!", p.text());
        assertEquals("", child.text());
    }

    /**
     * Test method for {@link Parser#stackHasValidParent(Tag)}
     */
    @Test
    public void testStackHasValidParent() {
        String html = "<html><body><div><p>Hello World!</p></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Parser parser = new Parser(doc.html(), "http://example.com", false);
        parser.parse();

        assertTrue(parser.stackHasValidParent(pTag));
        assertTrue(parser.stackHasValidParent(divTag));
        assertFalse(parser.stackHasValidParent(tableTag));
    }

    /**
     * Test method for {@link Parser#popStackToSuitableContainer(Tag)}
     */
    @Test
    public void testPopStackToSuitableContainer() {
        String html = "<html><body><div><div><p>Hello World!</p></div></div></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Parser parser = new Parser(doc.html(), "http://example.com", false);
        parser.parse();

        Element div1 = parser.last().child(0);
        Element div2 = parser.popStackToSuitableContainer(divTag);

        assertEquals(div1, div2);

        Element p = parser.popStackToSuitableContainer(pTag);
        assertEquals(div2, p.parent());

        assertNull(parser.last());
    }
}