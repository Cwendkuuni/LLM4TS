package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element htmlNode = doc.rootElement();
        assertEquals(htmlTag, htmlNode.tag());

        Element headNode = htmlNode.child(0);
        assertEquals(headTag, headNode.tag());

        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode.tag());
        assertEquals("Test", titleNode.text());

        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element bodyNode = doc.rootElement();
        assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        assertEquals(h1Tag, h1Node.tag());
        assertEquals("Hello World", h1Node.text());
    }

    @Test
    public void testParseXmlDecl() {
        String xml = "<!DOCTYPE html SYSTEM 'about:legacy-compat'>";
        String html = "<html>" + xml + "</html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element xmlDeclarationNode = doc.rootElement().child(0);
        assertEquals(XmlDeclaration.class, xmlDeclarationNode.getClass());
        assertEquals("about:legacy-compat", xmlDeclarationNode.attr("systemId"));
    }

    @Test
    public void testParseComment() {
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "  <!-- This is a comment -->" +
                "  <head><title>Test</title></head>" +
                "  <body>" +
                "    <h1>Hello World</h1>" +
                "  </body>" +
                "</html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element commentNode = doc.rootElement().child(1);
        assertEquals(Comment.class, commentNode.getClass());
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World<br><!-- --></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.rootElement().child(0).child(0);
        assertEquals(pTag, pNode.tag());
        assertEquals(2, pNode.childNodeSize());

        Element firstChildNode = pNode.child(0);
        assertEquals("Hello World", firstChildNode.text());

        Element secondChildNode = pNode.child(1);
        assertEquals(TextNode.class, secondChildNode.getClass());
        assertEquals("", secondChildNode.text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p><a href='http://example.com'>Link</a></p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.rootElement().child(0).child(0);
        assertEquals(pTag, pNode.tag());
        assertEquals(1, pNode.childNodeSize());

        Element aNode = pNode.child(0);
        assertEquals(aTag, aNode.tag());
        assertEquals("Link", aNode.text());
        assertEquals("http://example.com", aNode.absUrl("href"));
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src='image.jpg' alt='Image' title='Title'>";
        Document doc = Parser.parse(html, "http://example.com");

        Element imgNode = doc.rootElement().child(0);
        assertEquals(imgTag, imgNode.tag());

        List<Attribute> attributes = imgNode.attributes();
        assertEquals(3, attributes.size());

        Attribute srcAttribute = imgNode.attr("src");
        assertEquals("image.jpg", srcAttribute.getValue());

        Attribute altAttribute = imgNode.attr("alt");
        assertEquals("Image", altAttribute.getValue());

        Attribute titleAttribute = imgNode.attr("title");
        assertEquals("Title", titleAttribute.getValue());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>Hello World</p>";
        Document doc = Parser.parse(html, "http://example.com");

        Element pNode = doc.rootElement().child(0);
        assertEquals(pTag, pNode.tag());
        assertEquals(1, pNode.childNodeSize());

        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<script><![CDATA[console.log('Hello World');)]]></script>";
        Document doc = Parser.parse(html, "http://example.com");

        Element scriptNode = doc.rootElement().child(0);
        assertEquals(scriptTag, scriptNode.tag());

        TextNode cdataNode = (TextNode) scriptNode.child(0);
        assertEquals("console.log('Hello World');", cdataNode.text());
    }

    @Test(expected = AssertionError.class)
    public void testParseInvalidHtml() {
        String html = "<html><body><p>Hello World<br>"; // missing closing tag
        Document doc = Parser.parse(html, "http://example.com");
        fail("Should have thrown an AssertionError");
    }
}