package org.jsoup.parser; // or the package where XmlTreeBuilder is located

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        String xmlInput = "<xml><tag attr=\"value\"/></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://example.com");

        Assert.assertEquals("xml", xmlDoc.documentType().name());
        Element root = xmlDoc.rootElement();
        Assert.assertEquals("tag", root.tagName());
        Assert.assertEquals("value", root.attribute("attr"));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<tag attr=\"value\"/>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", null, XmlTreeBuilder.ParseSettings.preserveCase);

        Element tag = (Element) nodes.get(0);
        Assert.assertEquals("tag", tag.tagName());
        Assert.assertEquals("value", tag.attribute("attr"));
    }

    @Test
    public void testSelfClosingTag() {
        String xmlInput = "<tag/>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://example.com");

        Element tag = xmlDoc.rootElement();
        Assert.assertEquals("tag", tag.tagName());
        Assert.assertTrue(tag.hasAttrs()); // self-closing tag still has attributes
        Assert.assertFalse(tag.hasText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("invalid input", "http://example.com");
    }

    // Add test cases for other methods and edge cases as necessary
}