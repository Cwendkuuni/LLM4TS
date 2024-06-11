package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.namespace.QName;
import org.xml.namespace.NSConstants;
import org.xml.namespace.QNameFactory;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document jsoupDocument;

    @Before
    public void setUp() throws ParserConfigurationException {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        jsoupDocument = org.jsoup.Jsoup.parse(html);
    }

    @Test
    public void testFromJsoup() {
        W3CDom w3CDom = new W3CDom();
        Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
        assertNotNull(w3cDocument);
        assertEquals(jsoupDocument.childNodes().getLength(), w3cDocument.getChildNodes().getLength());
    }

    @Test
    public void testConvert() {
        W3CDom w3CDom = new W3CDom();
        Document w3cDocument = Document.newDocument();
        w3CDom.convert(jsoupDocument, w3cDocument);
        assertNotNull(w3cDocument);
        assertEquals(jsoupDocument.childNodes().getLength(), w3cDocument.getChildNodes().getLength());

        Element root = w3cDocument.getDocumentElement();
        assertEquals("html", root.getTagName());
        assertNotNull(root.getElementsByTagName("head").getItem(0));
        assertNotNull(root.getElementsByTagName("body").getItem(0));
        assertEquals("p", root.getElementsByTagName("p").getItem(0).getTagName());
        assertEquals("Hello World", w3cDocument.getElementsByTagName("p").getItem(0).getTextContent());
    }

    @Test
    public void testAsString() {
        W3CDom w3CDom = new W3CDom();
        Document w3cDocument = Document.newDocument();
        w3CDom.convert(jsoupDocument, w3cDocument);
        String result = w3CDom.asString(w3cDocument);
        String expected = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        assertEquals(expected, result);
    }

    @Test
    public void testNamespaces() {
        W3CDom w3CDom = new W3CDom();
        Document w3cDocument = Document.newDocument();
        Element root = w3cDocument.createElementNS("http://www.w3.org/1999/xhtml", "html");
        root.setAttributeNS("xmlns:xhtml", "http://www.w3.org/1999/xhtml");
        w3cDocument.appendChild(root);
        Element body = root.createElementNS("xhtml", "body");
        root.appendChild(body);
        Element p = body.createElementNS("xhtml", "p");
        p.setTextContent("Hello World");
        body.appendChild(p);

        w3CDom.convert(jsoupDocument, w3cDocument);
        NodeVisitor nodeVisitor = w3CDom.new W3CBuilder(w3cDocument);
        nodeVisitor.head(jsoupDocument, 0);

        assertEquals("html", w3cDocument.getDocumentElement().getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", w3cDocument.getDocumentElement().getNamespaceURI());

        Element html = (Element) nodeVisitor.peek();
        assertEquals("html", html.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", html.getNamespaceURI());
        assertEquals("http://www.w3.org/1999/xhtml", html.getPrefix());

        Element bodyElement = (Element) nodeVisitor.peek();
        assertEquals("body", bodyElement.getTagName());
        assertEquals("xhtml", bodyElement.getNamespaceURI());
        assertEquals("xhtml", bodyElement.getPrefix());

        Element pElement = (Element) nodeVisitor.peek();
        assertEquals("p", pElement.getTagName());
        assertEquals("xhtml", pElement.getNamespaceURI());
        assertEquals("xhtml", pElement.getPrefix());
        assertEquals("Hello World", pElement.getTextContent());
    }
}