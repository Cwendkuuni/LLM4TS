package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.*;
import org.junit.Assert.*;

public class XmlTreeBuilderTest {
    XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParse() {
        String xmlInput = "<xml><tag attr='value' /></xml>";
        Document parsedXml = xmlTreeBuilder.parse(xmlInput, null);
        assertNotNull(parsedXml);
        Element rootElement = parsedXml.rootElement();
        assertEquals("xml", rootElement.tagName());
        assertEquals("tag", rootElement.child(0).tagName());
        assertEquals("value", rootElement.child(0).attribute("attr"));
    }

    @Test
    public void testInitialiseParse() {
        String xmlInput = "<xml><tag attr='value' /></xml>";
        Document parsedXml = xmlTreeBuilder.parse(xmlInput, null);
        xmlTreeBuilder.initialiseParse(xmlInput, null, null, null);
        assertEquals(parsedXml, xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testProcessStartTag() {
        String xmlInput = "<tag attr='value' />";
        Document parsedXml = xmlTreeBuilder.parse(xmlInput, null);
        Token startTag = new Token.StartTag("tag", null, null);
        xmlTreeBuilder.process(startTag);
        Element element = parsedXml.elements().get(0);
        assertEquals("tag", element.tagName());
        assertEquals("value", element.attribute("attr"));
    }

    @Test
    public void testProcessEndTag() {
        String xmlInput = "<tag><subtag /></tag>";
        Document parsedXml = xmlTreeBuilder.parse(xmlInput, null);
        Token endTag = new Token.EndTag("tag");
        xmlTreeBuilder.process(endTag);
        assertEquals(parsedXml.child(0), xmlTreeBuilder.stack.pop());
    }

    @Test
    public void testProcessComment() {
        String xmlInput = "<!-- This is a comment -->";
        Document parsedXml = xmlTreeBuilder.parse(xmlInput, null);
        Token comment = new Token.Comment("<!-- This is a comment -->");
        xmlTreeBuilder.process(comment);
        Comment commentNode = (Comment) parsedXml.child(parsedXml.childNodes().size());
        assertEquals("This is a comment", commentNode.data());
    }

    @Test
    public void testProcessCharacter() {
        String xmlInput = "<tag>Hello World</tag>";
        Document parsedXml = xmlTreeBuilder.parse(xmlInput, null);
        Token character = new Token.Character('H');
        xmlTreeBuilder.process(character);
        TextNode textNode = (TextNode) parsedXml.child(0).child(0);
        assertEquals("Hello World", textNode.text());
        assertEquals("H", textNode.childNodes().get(0).text());
    }

    @Test
    public void testProcessDoctype() {
        String xmlInput = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Document parsedXml = xmlTreeBuilder.parse(xmlInput, null);
        Token doctype = new Token.Doctype("html", null, null, null);
        xmlTreeBuilder.process(doctype);
        DocumentType documentType = (DocumentType) parsedXml.child(0);
        assertEquals("html", documentType.tagName());
        assertNull(documentType.publicIdentifier());
        assertNull(documentType.systemIdentifier());
    }

    @Test
    public void testInsertNode() {
        Document parsedXml = xmlTreeBuilder.parse("<xml/>", null);
        Element rootElement = parsedXml.rootElement();

        Element element = new Element("tag", null, null);
        xmlTreeBuilder.insertNode(element);
        assertEquals(rootElement, xmlTreeBuilder.stack.peek());
        assertEquals(1, rootElement.childNodes().size());
        assertEquals("tag", rootElement.child(0).tagName());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        Token invalidToken = new Token(123);
        xmlTreeBuilder.process(invalidToken);
    }
}