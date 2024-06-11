package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * Unit tests for the XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlWithBaseUri() {
        String xmlString = "<root xmlns:my=\"http://example.com/\"><my:child>Hello</my:child></root>";
        String baseUri = "http://test.com";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), baseUri);

        Element rootElement = xmlDoc.rootElement();
        assertEquals("root", rootElement.tagName());

        List<Node> children = rootElement.children();
        assertEquals(1, children.size());

        Element childElement = (Element) children.get(0);
        assertEquals("my:child", childElement.tagName());
        assertEquals("Hello", childElement.text());
    }

    @Test
    public void testParseXmlWithEmptyBaseUri() {
        String xmlString = "<root xmlns:my=\"http://example.com/\"><my:child>Hello</my:child></root>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString));

        Element rootElement = xmlDoc.rootElement();
        assertEquals("root", rootElement.tagName());

        List<Node> children = rootElement.children();
        assertEquals(1, children.size());

        Element childElement = (Element) children.get(0);
        assertEquals("my:child", childElement.tagName());
        assertEquals("Hello", childElement.text());
    }

    @Test
    public void testParseXmlWithEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(""), "");

        assertEquals(0, xmlDoc.childNodes().size());
    }

    @Test
    public void testParseXmlWithNoBaseUri() {
        String xmlString = "<root xmlns:my=\"http://example.com/\"><my:child>Hello</my:child></root>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString));

        Element rootElement = xmlDoc.rootElement();
        assertEquals("root", rootElement.tagName());

        List<Node> children = rootElement.children();
        assertEquals(1, children.size());

        Element childElement = (Element) children.get(0);
        assertEquals("my:child", childElement.tagName());
        assertEquals("Hello", childElement.text());
    }

    @Test
    public void testParseXmlWithNoNamespace() {
        String xmlString = "<root><child>Hello</child></root>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element rootElement = xmlDoc.rootElement();
        assertEquals("root", rootElement.tagName());

        List<Node> children = rootElement.children();
        assertEquals(1, children.size());

        Element childElement = (Element) children.get(0);
        assertEquals("child", childElement.tagName());
        assertEquals("Hello", childElement.text());
    }

    @Test
    public void testParseXmlWithEmptyNamespace() {
        String xmlString = "<root xmlns=\"\"><child>Hello</child></root>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element rootElement = xmlDoc.rootElement();
        assertEquals("root", rootElement.tagName());

        List<Node> children = rootElement.children();
        assertEquals(1, children.size());

        Element childElement = (Element) children.get(0);
        assertEquals("child", childElement.tagName());
        assertEquals("Hello", childElement.text());
    }

    @Test
    public void testParseXmlWithComment() {
        String xmlString = "<root><!-- This is a comment --></root>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        List<Node> children = xmlDoc.childNodes();
        assertEquals(1, children.size());

        Element rootElement = (Element) children.get(0);
        List<Node> rootComments = rootElement.childNodes();
        assertEquals(1, rootComments.size());

        Node commentNode = rootComments.get(0);
        assertTrue(commentNode instanceof Comment);
        assertEquals("This is a comment", commentNode.text());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xmlString = "<root><img src=\"image.png\" /> </root>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element rootElement = xmlDoc.rootElement();
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());

        Element imgElement = (Element) children.get(0);
        assertEquals("img", imgElement.tagName());
        assertFalse(imgElement.hasChildren());
    }

    @Test
    public void testParseXmlWithFragment() {
        String xmlFragment = "<root><child>Hello</child></root>";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element rootElement = (Element) parsedNodes.get(0);
        assertEquals("root", rootElement.tagName());

        List<Node> children = rootElement.children();
        assertEquals(1, children.size());

        Element childElement = (Element) children.get(0);
        assertEquals("child", childElement.tagName());
        assertEquals("Hello", childElement.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void testParseXmlWithNullBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("<root/>"), null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseXmlWithNullSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("<root/>"), "", null);
    }

    @Test(expected = NullPointerException.class)
    public void testParseXmlWithNullErrorList() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("<root/>"), "", null, null);
    }
}