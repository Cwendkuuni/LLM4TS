package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.namespace.QName;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

    private W3CDom w3CDom = new W3CDom();

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() throws ParserConfigurationException, TransformerException {
        String html = "<html xmlns:test=\"http://example.com/test\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Test</title></head><body><p xml:id=\"p1\">Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = w3cDom.newInstance().factory.newDocument();
        w3CDom.convert(jsoupDoc, w3cDoc);

        Assert.assertEquals("http://www.w3.org/1999/xhtml", w3cDoc.getDocumentElement().getNamespaceURI());
        Assert.assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        Element head = (Element) w3cDoc.getDocumentElement().getElementsByTagName("head").item(0);
        Assert.assertNotNull(head);
        Element title = (Element) head.getElementsByTagName("title").item(0);
        Assert.assertNotNull(title);
        Assert.assertEquals("Test", title.getTextContent());
        Element body = (Element) w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
        Assert.assertNotNull(body);
        Element p = (Element) body.getElementsByTagName("p").item(0);
        Assert.assertNotNull(p);
        Assert.assertEquals("Hello World", p.getTextContent());
        Assert.assertEquals("http://example.com/test", p.getNamespaceURI());
        Assert.assertEquals("p", p.getTagName());
        Assert.assertEquals("p1", p.getAttribute("xml:id"));
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        String html = "<html xmlns:test=\"http://example.com/test\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Test</title></head><body><p xml:id=\"p1\">Hello World</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        Document w3cDoc = w3cDom.newInstance().factory.newDocument();
        w3CDom.convert(jsoupDoc, w3cDoc);
        String w3cDocString = w3CDom.newInstance().asString(w3cDoc);

        String expected = "<html xmlns:test=\"http://example.com/test\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Test</title></head><body><p xml:id=\"p1\">Hello World</p></body></html>";
        Assert.assertEquals(expected, w3cDocString);
    }

    @Test
    public void testConvertWithNamespaces() throws ParserConfigurationException, TransformerException {
        String xml = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:test=\"http://example.com/test\"><test:child><xsi:attribute>Value</xsi:attribute></test:child></root>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(xml);
        Document w3cDoc = w3cDom.newInstance().factory.newDocument();
        w3CDom.convert(jsoupDoc, w3cDoc);

        Element root = (Element) w3cDoc.getDocumentElement();
        Element child = (Element) root.getElementsByTagNameNS("http://example.com/test", "child").item(0);
        Attributes childAttributes = child.getAttributes();
        Assert.assertEquals(1, childAttributes.getLength());
        Attribute attr = childAttributes.getNamedItem("xml:id");
        Assert.assertNull(attr);
        NamedNodeMap namespaceMap = child.getNamespaceURI().getOwnerDocument().getElementsByTagName("nil").item(0).getAttributes();
        attr = (Attribute) namespaceMap.getNamedItem("xmlns:xsi");
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getValue());
        Assert.assertEquals("xsi", attr.getPrefix());
        Attribute childAttribute = (Attribute) childAttributes.getNamedItem("attribute");
        Assert.assertEquals("Value", childAttribute.getValue());
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", childAttribute.getNamespaceURI());
        Assert.assertEquals("attribute", childAttribute.getLocalName());
    }
}

class W3CDomTestNewInstance {
    private static final W3CDom W3CDOM_INSTANCE = new W3CDom();
}