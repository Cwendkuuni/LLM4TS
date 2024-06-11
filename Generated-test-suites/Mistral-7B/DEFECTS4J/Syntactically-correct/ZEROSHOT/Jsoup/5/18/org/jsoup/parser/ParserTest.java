package org.jsoup.parser;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.xml.XmlDeclaration;

/**
 * Test class for Parser.java
 *
 * @author John Doe
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head>"
                + "<body><h1>Test Heading</h1><p>Test Paragraph.<a href=\"http://www.google.com\">Google</a></p></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");

        // Check document elements
        Element htmlNode = doc.body();
        assertEquals(htmlTag, htmlNode.tag());
        Element headNode = doc.head();
        assertEquals(headTag, headNode.tag());
        Element titleNode = headNode.child(0);
        assertEquals(titleTag, titleNode.tag());
        Element bodyNode = doc.body();
        assertEquals(bodyTag, bodyNode.tag());

        // Check document attributes
        String baseUri = doc.baseUri();
        assertEquals("http://www.example.com", baseUri);

        // Check document text
        TextNode titleTextNode = (TextNode) titleNode.child(0);
        assertEquals("Test Title", titleTextNode.text());

        // Check document title
        String title = doc.title();
        assertEquals("Test Title", title);

        // Check document head
        Element head = doc.head();
        assertEquals(1, head.childSize());

        // Check document body
        Element body = doc.body();
        assertEquals(2, body.childSize());

        // Check h1 element
        Element h1 = body.child(0);
        assertEquals(h1Tag, h1.tag());
        assertEquals("Test Heading", h1.text());

        // Check p element
        Element p = body.child(1);
        assertEquals(pTag, p.tag());
        assertEquals(2, p.childSize());

        // Check link element
        Element link = p.child(0);
        assertEquals(aTag, link.tag());
        assertEquals("http://www.google.com", link.attr("href"));

        // Check paragraph text
        TextNode pTextNode = (TextNode) p.child(1);
        assertEquals("Test Paragraph.", pTextNode.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Paragraph.</p>";

        Document doc = Parser.parseBodyFragment(bodyHtml, "http://www.example.com");

        // Check document elements
        Element bodyNode = doc.body();
        assertEquals(bodyTag, bodyNode.tag());

        Element pNode = bodyNode.child(0);
        assertEquals(pTag, pNode.tag());

        // Check document text
        TextNode pTextNode = (TextNode) pNode.child(0);
        assertEquals("Test Paragraph.", pTextNode.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Test Paragraph.<div class=\"test\">Relaxed test</div>";

        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://www.example.com");

        // Check document elements
        Element bodyNode = doc.body();
        assertEquals(bodyTag, bodyNode.tag());

        Element pNode = bodyNode.child(0);
        assertEquals(pTag, pNode.tag());

        Element divNode = pNode.child(0);
        assertEquals(divTag, divNode.tag());

        // Check document text
        TextNode pTextNode = (TextNode) pNode.child(1);
        assertEquals("Test Paragraph.", pTextNode.text());

        TextNode divTextNode = (TextNode) divNode.child(0);
        assertEquals("Relaxed test", divTextNode.text());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --><body></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");

        // Check comment
        Comment comment = (Comment) doc.body().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String xmlDecl = "<xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\">";

        Document doc = new Document();
        doc.setBaseUri("http://www.example.com");
        doc.outputSettings().syntax("xml");
        doc.outputSettings().indentAmount(2);

        doc.setXmlDeclaration(new XmlDeclaration("1.0", "UTF-8", true));

        Element xmlRoot = doc.appendChild(new Element("xml"));
        xmlRoot.appendChild(doc.newXMLEntity("xml-decl", xmlDecl));

        String html = "<html>" + xmlDecl + "</html>";

        Document parsedDoc = Parser.parse(html, "http://www.example.com");

        // Check parsed xml declaration
        XmlDeclaration parsedXmlDecl = parsedDoc.xmlDeclaration();
        assertEquals("1.0", parsedXmlDecl.version());
        assertEquals("UTF-8", parsedXmlDecl.encoding());
        assertTrue(parsedXmlDecl.standalone());

        // Check parsed xml root
        Element parsedXmlRoot = parsedDoc.getRootElement();
        assertEquals("xml", parsedXmlRoot.tag().name());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Test<p></p></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");

        Element pNode = doc.body().child(0);
        assertEquals(pTag, pNode.tag());
        assertEquals(2, pNode.childSize());

        Element pCloseNode = pNode.child(1);
        assertEquals(pTag, pCloseNode.tag());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><p><a href=\"http://www.google.com\">Google</a></p></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");

        Element pNode = doc.body().child(0);
        assertEquals(pTag, pNode.tag());
        assertEquals(1, pNode.childSize());

        Element aNode = pNode.child(0);
        assertEquals(aTag, aNode.tag());
        assertEquals(1, aNode.childSize());

        Attribute hrefAttr = aNode.attr("href");
        assertEquals("http://www.google.com", hrefAttr.value());
    }

    @Test
    public void testParseAttribute() {
        String html = "<html><body><a href=\"http://www.google.com\" id=\"test\"></a></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");

        Element aNode = doc.body().child(0);
        assertEquals(aTag, aNode.tag());
        assertEquals(2, aNode.childSize());

        Attribute hrefAttr = aNode.attr("href");
        assertEquals("http://www.google.com", hrefAttr.value());

        Attribute idAttr = aNode.attr("id");
        assertEquals("test", idAttr.value());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>Test Paragraph.</p></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");

        Element pNode = doc.body().child(0);
        assertEquals(pTag, pNode.tag());
        assertEquals(1, pNode.childSize());

        TextNode textNode = (TextNode) pNode.child(0);
        assertEquals("Test Paragraph.", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[<h1>Test Heading</h1>]]></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");

        Element bodyNode = doc.body();
        assertEquals(bodyTag, bodyNode.tag());

        DataNode cdataNode = (DataNode) bodyNode.child(0);
        assertEquals("<h1>Test Heading</h1>", cdataNode.text());
    }

    @Test(expected = AssertionError.class)
    public void testParseInvalidHtml() {
        String html = "<html><body><h1<p"; // missing closing tag

        Document doc = Parser.parse(html, "http://www.example.com");
        fail(); // should throw an exception
    }
}