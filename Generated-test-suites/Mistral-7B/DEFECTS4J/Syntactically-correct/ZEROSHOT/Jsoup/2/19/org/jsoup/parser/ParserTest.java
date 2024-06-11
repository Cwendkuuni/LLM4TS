package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test Page</title></head>" +
                "<body><h1>Hello World</h1></body></html>";
        parser = new Parser(html, "", false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertThat(doc, is(notNullValue()));
        assertThat(doc.title(), is("Test Page"));
        assertThat(doc.head(), is(notNullValue()));
        assertThat(doc.body(), is(notNullValue()));
        assertThat(doc.title().text(), is("Test Page"));
        assertThat(doc.body().text(), is("Hello World"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Hello World</h1></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "");
        assertThat(doc, is(notNullValue()));
        assertThat(doc.head(), is(nullValue()));
        assertThat(doc.body(), is(notNullValue()));
        assertThat(doc.body().text(), is("Hello World"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser parser = new Parser(null, "", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        Parser parser = new Parser("", "", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html<title>Test Page</title></html>";
        Parser parser = new Parser(html, "", false);
    }

    @Test
    public void testParseDocumentStructure() {
        Document doc = parser.parse();
        List<Element> elements = new LinkedList<>(Arrays.asList(doc.body().childNodes()));

        assertThat(elements.size(), is(1));
        assertThat(elements.get(0).tagName(), is("h1"));
        assertThat(elements.get(0).text(), is("Hello World"));
    }

    @Test
    public void testParseAttributes() {
        String html = "<div id='test' class='class1 class2' title='Test Title'></div>";
        Parser parser = new Parser(html, "", false);
        Document doc = parser.parse();
        Element element = doc.body().childNodes().get(0);

        List<Attribute> attributes = new LinkedList<>(element.attributes().asList());
        assertThat(attributes.size(), is(4));
        assertThat(attributes.get(0).key(), is("id"));
        assertThat(attributes.get(0).value(), is("test"));
        assertThat(attributes.get(1).key(), is("class"));
        assertThat(attributes.get(1).value(), is("class1 class2"));
        assertThat(attributes.get(2).key(), is("title"));
        assertThat(attributes.get(2).value(), is("Test Title"));
    }

    @Test
    public void testParseTextNode() {
        String html = "<div>Hello World</div>";
        Parser parser = new Parser(html, "", false);
        Document doc = parser.parse();
        Element div = doc.body().childNodes().get(0);

        List<Node> childNodes = new LinkedList<>(div.childNodes());
        assertThat(childNodes.size(), is(1));
        assertThat(childNodes.get(0), is(instanceOf(TextNode.class)));
        TextNode textNode = (TextNode) childNodes.get(0);
        assertThat(textNode.text(), is("Hello World"));
    }

    @Test
    public void testParseCdata() {
        String html = "<div><![CDATA[Hello World]]></div>";
        Parser parser = new Parser(html, "", false);
        Document doc = parser.parse();
        Element div = doc.body().childNodes().get(0);

        List<Node> childNodes = new LinkedList<>(div.childNodes());
        assertThat(childNodes.size(), is(1));
        assertThat(childNodes.get(0), is(instanceOf(TextNode.class)));
        TextNode textNode = (TextNode) childNodes.get(0);
        assertThat(textNode.text(), is("Hello World"));
    }

    @Test
    public void testParseComment() {
        String html = "<!--Test Comment-->";
        Parser parser = new Parser(html, "", false);
        Document doc = parser.parse();

        Element comment = (Element) doc.childNodes().get(0);
        assertThat(comment.tagName(), is("comment"));
        assertThat(comment.text(), is("Test Comment"));
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<<?xml version='1.0' encoding='UTF-8'?>" +
                "<html><head></head><body></body></html>";
        Parser parser = new Parser(html, "", false);
        Document doc = parser.parse();

        Element xmlDecl = (Element) doc.childNodes().get(0);
        assertThat(xmlDecl.tagName(), is("xml-declaration"));
        XmlDeclaration xml = (XmlDeclaration) xmlDecl.childNodes().get(0);
        assertThat(xml.version(), is("1.0"));
        assertThat(xml.encoding(), is("UTF-8"));
    }
}