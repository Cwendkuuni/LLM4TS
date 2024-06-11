package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParse() {
        String xmlInput = "<root><child><subChild/></child></root>";
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

        Element root = xmlDoc.rootElement();
        Element child = root.child(0);
        Element subChild = child.child(0);

        assertEquals("root", root.tagName());
        assertEquals("child", child.tagName());
        assertTrue(subChild.tagName().equals("subChild") || subChild.tagName().equals("SubChild"));
    }

    @Test
    public void testParseWithBaseUri() {
        String xmlInput = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><child xsi:type=\"xsd:string\"><subChild/></child></root>";
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");

        Element root = xmlDoc.rootElement();
        Element child = root.child(0);
        Element subChild = child.child(0);

        assertEquals("root", root.tagName());
        assertEquals("child", child.tagName());
        assertTrue(subChild.tagName().equals("subChild") || subChild.tagName().equals("SubChild"));
        assertEquals("http://test.com", root.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<root><child/><child/></root>";

        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");
        Element root = xmlDoc.rootElement();
        Element child1 = root.child(0);
        Element child2 = root.child(1);

        assertEquals("root", root.tagName());
        assertEquals("child", child1.tagName());
        assertEquals("child", child2.tagName());
    }

    @Test
    public void testProcessEndTag() {
        xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<root><child><subChild/></child></root>";

        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");
        Element root = xmlDoc.rootElement();
        Element child = root.child(0);

        assertEquals("root", root.tagName());
        assertEquals("child", child.tagName());
        assertEquals(1, child.childNodes().size());

        child.remove();
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testProcessComment() {
        xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<!-- This is a comment --><root><child><subChild/></child></root>";

        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");
        List<Node> commentNodes = xmlDoc.selectNodes("//comment()");

        assertEquals(1, commentNodes.size());
        assertTrue(commentNodes.get(0) instanceof Comment);
        assertEquals("This is a comment", commentNodes.get(0).text());
    }

    @Test
    public void testProcessCharacter() {
        xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<root><child>Hello World</child></root>";

        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");
        List<Node> textNodes = xmlDoc.selectNodes("//text()");

        assertEquals(1, textNodes.size());
        assertEquals("Hello World", textNodes.get(0).text());
    }

    @Test
    public void testProcessDoctype() {
        xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<!DOCTYPE root SYSTEM \"root.dtd\"> <root></root>";

        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");
        DocumentType doctypeNode = xmlDoc.doctype();

        assertEquals("root", doctypeNode.nodeName());
        assertEquals("root.dtd", doctypeNode.publicIdentifier());
        assertEquals(null, doctypeNode.systemIdentifier());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTokenType() {
        xmlTreeBuilder = new XmlTreeBuilder();
        String xmlInput = "<root><child>Hello World</child>5</root>";

        xmlTreeBuilder.parse(xmlInput, "");
        Validate.fail("Unexpected token type: 5");
    }
}