package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(""), "");
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><child>Hello World</child></root>"), "");
        Element root = doc.child(0);
        Element child = root.child(0);
        TextNode text = (TextNode) child.child(0);
        assertEquals("Hello World", text.text());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><img src='image.jpg'/></root>"), "");
        Element root = doc.child(0);
        assertEquals(1, root.childNodeSize());
        Element img = root.child(0);
        assertEquals("img", img.tagName());
        assertEquals(0, img.childNodeSize());
    }

    @Test
    public void testParseXmlWithComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><!-- This is a comment --></root>"), "");
        Element root = doc.child(0);
        assertEquals(0, root.childNodeSize());
        Element comment = (Element) doc.childNodes().get(0);
        assertEquals("--", comment.tagName());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlWithCDataSection() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><![CDATA[This is a CDataSection]]]></root>"), "");
        Element root = doc.child(0);
        Element cdata = (CDataNode) root.child(0);
        assertEquals("This is a CDataSection", cdata.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<invalid>"), "");
        // This test case should throw an IllegalArgumentException since "invalid" is not a valid start tag
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><child><badTag/></child></root>"), "");
        // This test case should throw an IllegalArgumentException since "badTag" is not a valid end tag
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithEmptyTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><tag/></root>"), "");
        Element root = doc.child(0);
        Element tag = root.child(0);
        assertEquals("tag", tag.tagName());
        assertEquals(0, tag.childNodeSize());
        // This test case should throw an IllegalArgumentException since the tag is empty
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<html>"), "");
        // This test case should throw an IllegalArgumentException since the input is not valid XML
    }
}