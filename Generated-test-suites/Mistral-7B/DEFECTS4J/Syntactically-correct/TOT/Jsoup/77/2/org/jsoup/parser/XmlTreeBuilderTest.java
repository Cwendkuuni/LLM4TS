package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(""), "baseUri");

        Element root = xmlDoc.rootElement();
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><element>Hello World</element></root>"), "baseUri");

        Element root = xmlDoc.rootElement();
        Element element = root.child(0);
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><img src=\"image.png\" /> </root>"), "baseUri");

        Element root = xmlDoc.rootElement();
        Element img = root.getElementsByTag("img").first();
        assertNotNull(img);
        assertEquals(0, img.childNodes().size());
    }

    @Test
    public void testParseComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><!-- Comment --></root>"), "baseUri");

        Element root = xmlDoc.rootElement();
        Comment comment = root.getElementsByTag("comment").first();
        assertNotNull(comment);
        assertEquals("Comment", comment.text());
    }

    @Test
    public void testParseDocumentType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<!DOCTYPE root [ <!ATTLIST element att CDATA #REQUIRED > ]> <root></root>"), "baseUri");

        DocumentType doctypeNode = xmlDoc.doctype();
        assertEquals("root", doctypeNode.nodeName());
        assertEquals("[ <!ATTLIST element att CDATA #REQUIRED > ]", doctypeNode.getSystemIdentifier());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader(""), "baseUri");
        Validate.fail("Should throw an IllegalArgumentException with the message 'Unexpected character '<' at line 1 column 1 path ''\n'");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputWithComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("<!--"), "baseUri");
        Validate.fail("Should throw an IllegalArgumentException with the message 'Unexpected character '<' at line 1 column 1 path ''\n'");
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<root><element>Hello World</element></root>", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Node root = fragmentNodes.get(0);
        Element element = (Element) root.childNodes().get(0);
        assertEquals("Hello World", element.text());
    }
}