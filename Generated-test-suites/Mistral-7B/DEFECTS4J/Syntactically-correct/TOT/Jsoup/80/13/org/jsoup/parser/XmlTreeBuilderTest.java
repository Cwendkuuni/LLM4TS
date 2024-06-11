package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(""), "");
        assertNotNull(xmlDoc);
        assertEquals(0, xmlDoc.childNodes().size());
    }

    @Test
    public void testXmlWithRootElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root/>"), "");
        assertNotNull(xmlDoc);
        assertEquals(1, xmlDoc.childNodes().size());
        Element root = xmlDoc.child(0);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testXmlWithRootElementAndContent() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><content>Hello World</content></root>"), "");
        assertNotNull(xmlDoc);
        assertEquals(1, xmlDoc.childNodes().size());
        Element root = xmlDoc.child(0);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        Element content = root.child(0);
        assertNotNull(content);
        assertEquals("content", content.nodeName());
        assertEquals("Hello World", content.text().getWholeText());
    }

    @Test
    public void testXmlWithRootElementAndSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><img src='image.jpg'/></root>"), "");
        assertNotNull(xmlDoc);
        assertEquals(1, xmlDoc.childNodes().size());
        Element root = xmlDoc.child(0);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        Element img = root.child(0);
        assertNotNull(img);
        assertEquals("img", img.nodeName());
        assertTrue(img.hasAttr("src"));
        assertEquals("image.jpg", img.attr("src"));
        assertFalse(img.hasChildren());
    }

    @Test
    public void testXmlWithRootElementAndMixedContent() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><content>Hello World</content><img src='image.jpg'/></root>"), "");
        assertNotNull(xmlDoc);
        assertEquals(1, xmlDoc.childNodes().size());
        Element root = xmlDoc.child(0);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        assertEquals(2, root.childNodes().size());
        Element content = root.child(0);
        assertNotNull(content);
        assertEquals("content", content.nodeName());
        assertEquals("Hello World", content.text().getWholeText());
        Element img = root.child(1);
        assertNotNull(img);
        assertEquals("img", img.nodeName());
        assertTrue(img.hasAttr("src"));
        assertEquals("image.jpg", img.attr("src"));
        assertFalse(img.hasChildren());
    }

    @Test
    public void testXmlWithEmptyComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><!-- --></root>"), "");
        assertNotNull(xmlDoc);
        assertEquals(1, xmlDoc.childNodes().size());
        Element root = xmlDoc.child(0);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        assertTrue(root.childNodes().isEmpty());
    }

    @Test
    public void testXmlWithCDataSection() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><![CDATA[Hello World]]]></root>"), "");
        assertNotNull(xmlDoc);
        assertEquals(1, xmlDoc.childNodes().size());
        Element root = xmlDoc.child(0);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        TextNode textNode = (TextNode) root.child(0);
        assertNotNull(textNode);
        assertEquals("Hello World", textNode.getWholeText());
    }

    @Test(expected = IllegalStateException.class)
    public void testXmlWithMissingEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<root><content>Hello World</content>"), "");
        assertNotNull(xmlDoc);
        assertEquals(1, xmlDoc.childNodes().size());
        Element root = xmlDoc.child(0);
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        assertEquals(1, root.childNodes().size());
        Element content = root.child(0);
        assertNotNull(content);
        assertEquals("content", content.nodeName());
        assertEquals("Hello World", content.text().getWholeText());

        // This should trigger the exception
        xmlTreeBuilder.popStackToClose(new Token.EndTag("root"));
    }
}