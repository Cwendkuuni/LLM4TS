package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.parser.ParserConfigurationException;

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
import java.util.Stack;

import static org.junit.Assert.*;

public class W3CDomTest {

    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private W3CDom w3cdom;

    @Before
    public void setUp() throws ParserConfigurationException {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        w3cdom = new W3CDom();
    }

    @After
    public void tearDown() {
        factory = null;
        builder = null;
        w3cdom = null;
    }

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);

        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());

        // Test head element
        Element head = (Element) w3cDoc.getDocumentElement().getElementsByTagName("head").item(0);
        assertEquals(1, head.getChildNodes().getLength());
        assertEquals("title", head.getChildNodes().item(0).getTagName());
        assertEquals("Test", head.getChildNodes().item(0).getTextContent());

        // Test body element
        Element body = (Element) w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
        assertEquals(1, body.getChildNodes().getLength());
        assertEquals("p", body.getChildNodes().item(0).getTagName());
        assertEquals("Hello World", body.getChildNodes().item(0).getTextContent());

        // Test conversion of attributes
        org.jsoup.nodes.Element p = (org.jsoup.nodes.Element) jsoupDoc.select("p").first();
        p.classes("test-class");
        p.attr("data-test", "test-data");

        Document convertedDoc = w3cdom.fromJsoup(jsoupDoc);
        Element convertedP = (Element) w3cDoc.getDocumentElement().getElementsByTagName("p").item(0);
        assertEquals("test-class", convertedP.getAttribute("class"));
        assertEquals("test-data", convertedP.getAttribute("data-test"));
    }

    @Test
    public void testConvert() throws ParserConfigurationException {
        Document w3cDoc = builder.newDocument();
        w3cdom.convert(builder.newDocument(), w3cDoc);
        assertEquals(0, w3cDoc.getDocumentElement().getChildNodes().getLength());

        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
        w3cdom.convert(jsoupDoc, w3cDoc);

        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());

        // Test head element
        Element head = (Element) w3cDoc.getDocumentElement().getElementsByTagName("head").item(0);
        assertEquals(1, head.getChildNodes().getLength());
        assertEquals("title", head.getChildNodes().item(0).getTagName());
        assertEquals("Test", head.getChildNodes().item(0).getTextContent());

        // Test body element
        Element body = (Element) w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
        assertEquals(1, body.getChildNodes().getLength());
        assertEquals("p", body.getChildNodes().item(0).getTagName());
        assertEquals("Hello World", body.getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDoc = builder.newDocument();
        w3cdom.convert(builder.newDocument(), w3cDoc);
        org.jsoup.nodes.Document jsoupDoc = w3cdom.fromJsoup(w3cDoc);

        String jsoupString = w3cdom.asString(w3cdom.fromJsoup(jsoupDoc));
        String w3cString = builder.newDocumentBuilder().newDocument().getDocumentElement().appendChild(w3cdom.fromJsoup(jsoupDoc)).getDocumentURI();

        assertEquals(jsoupString, w3cString);
    }

    @Test
    public void testNamespaceAware() throws ParserConfigurationException {
        Document xmlDoc = builder.newDocument();
        xmlDoc.setXmlStandalone(true);
        xmlDoc.setDocumentElementNamespaceURI("http://example.com");
        xmlDoc.getDocumentElement().appendChild(xmlDoc.createProcessingInstruction("xml", "version='1.0' encoding='UTF-8'"));
        xmlDoc.getDocumentElement().appendChild(xmlDoc.createProcessingInstruction("xml-ns", "prefix='example'"));
        xmlDoc.getDocumentElement().appendChild(xmlDoc.createElementNS("prefix", "root"));
        xmlDoc.getDocumentElement().getElementsByTagNameNS("prefix", "root").item(0).appendChild(xmlDoc.createTextNode("Hello World"));

        Document jsoupDoc = org.jsoup.Jsoup.parseBodyFragment(xmlDoc.getDocumentURI());
        Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);

        DocumentTraversal traversal = (DocumentTraversal) w3cDoc;
        NodeFilter elementFilter = new NodeFilter() {
            public boolean acceptNode(Node node) {
                return (node.getNodeType() == Node.ELEMENT_NODE);
            }
        };
        NodeIterator nodeIterator = traversal.getElementsByTagNameNS("prefix", "root");
        Element testElement = (Element) nodeIterator.nextNode();

        assertEquals("root", testElement.getTagName());
        assertEquals("http://example.com", testElement.getNamespaceURI());
        assertEquals("prefix", testElement.getPrefix());
    }
}