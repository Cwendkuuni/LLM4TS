package org.jsoup.parser; // make sure to import necessary classes at the beginning

import static org.junit.Assert.assertEquals;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        String xmlString = "<root><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xmlString, ""); // empty baseUri
        Element root = document.getRootElement();
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        assertEquals("child", root.child(0).tagName());
    }

    @Test
    public void testParseWithBaseUri() {
        String xmlString = "<base><child xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"test.xsd test.xsd\"><subchild></subchild></child></base>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xmlString, "http://www.test.com");
        Element base = document.getRootElement();
        assertEquals("base", base.tagName());
        Element child = base.child(0);
        assertEquals("child", child.tagName());
        assertEquals("http://www.test.com/subchild", child.child(0).attributes().get("xsi:schemaLocation").toString());
    }

    @Test
    public void testProcessTokens() {
        String xmlString = "<root><child><subchild><grandchild></grandchild></subchild></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xmlString, "");
        Element root = document.getRootElement();
        Element child = root.child(0);
        Element subchild = child.child(0);
        Element grandchild = subchild.child(0);

        assertEquals("root", root.tagName());
        assertEquals("child", child.tagName());
        assertEquals("subchild", subchild.tagName());
        assertEquals("grandchild", grandchild.tagName());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidInput() {
        String xmlString = "<root>"; // missing closing tag
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(xmlString, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBaseUri() {
        String xmlString = "<root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<script src=\"javascript:alert('XSS')\">", "javascript:alert('XSS')");
    }
}