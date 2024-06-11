package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testParseXmlWithValidInput() {
        String xmlInput = "<root><child>Hello World</child></root>";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlInput), null);
        Assert.assertNotNull(document);
        Assert.assertEquals("<root><child>Hello World</child></root>", document.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithNullInput() {
        new XmlTreeBuilder().parse(null, null);
    }

    @Test
    public void testParseXmlWithEmptyInput() {
        Document document = new XmlTreeBuilder().parse(new StringReader(""), null);
        Assert.assertNull(document);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseXmlWithInvalidInput() {
        String invalidXmlInput = "<html><body><h1>Invalid XML</h1></body></html>";
        new XmlTreeBuilder().parse(new StringReader(invalidXmlInput), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithNullBaseUri() {
        new XmlTreeBuilder().parse(new StringReader("<root></root>"), null);
    }

    @Test
    public void testParseXmlWithDoctypeDeclaration() {
        String xmlInput = "<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED > ]><root><child>Hello World</child></root>";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlInput), null);
        Assert.assertNotNull(document);
        Assert.assertEquals("<root><child attr=''>Hello World</child></root>", document.html());
    }

    @Test
    public void testParseXmlWithXmlDeclaration() {
        String xmlInput = "<?xml version='1.0' encoding='UTF-8'?><root><child>Hello World</child></root>";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlInput), null);
        Assert.assertNotNull(document);
        Assert.assertEquals("<root><child>Hello World</child></root>", document.html());
    }

    @Test
    public void testParseXmlWithExternalEntity() {
        String xmlInput = "<!DOCTYPE root SYSTEM 'external.dtd'><root><child>Hello World</child></root>";
        Document document = new XmlTreeBuilder().parse(new StringReader(xmlInput), null);
        Assert.assertNotNull(document);
        Assert.assertEquals("<root><child>Hello World</child></root>", document.html());
    }

    @Test(expected = SAXException.class)
    public void testParseXmlWithError() {
        String xmlInput = "<root><child><child>";
        new XmlTreeBuilder().parse(new StringReader(xmlInput), null);
    }
}