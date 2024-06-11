package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class W3CDomTest {

 @Test
 public void testFromJsoup() throws ParserConfigurationException {
 Document jsoupDoc = DocumentHelper.parseText("<html><head></head><body><p>Hello World</p></body></html>");
 W3CDom w3cdom = new W3CDom();
 Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
 assertNotNull(w3cDoc);
 }

 @Test
 public void testConvert() throws ParserConfigurationException {
 Document jsoupDoc = DocumentHelper.parseText("<html><head></head><body><p xmlns:test=\"http://test.com\" xmlns:html=\"http://www.w3.org/1999/xhtml\"><test:test>Hello World</test:test></p></body></html>");
 W3CDom w3cdom = new W3CDom();
 Document w3cDoc = w3cdom.factory.newDocument();
 w3cdom.convert(jsoupDoc, w3cDoc);
 assertNotNull(w3cDoc);

 Element html = w3cDoc.getDocumentElement();
 assertEquals("html", html.getTagName());
 Element head = html.getElementsByTagName("head").item(0);
 assertEquals(0, head.getChildNodes().getLength());
 Element body = html.getElementsByTagName("body").item(0);
 Element test = body.getElementsByTagNameNS("http://test.com", "test").item(0);
 assertEquals("test", test.getTagName());
 assertEquals("Hello World", test.getTextContent());
 }

 @Test
 public void testConvertWithAttributes() throws ParserConfigurationException {
 Document jsoupDoc = DocumentHelper.parseText("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xmlns:test=\"http://test.com\" class=\"myclass\"><head><title>Test Title</title></head><body><p id=\"testID\" class=\"testclass\">Hello World</p></body></html>");
 W3CDom w3cdom = new W3CDom();
 Document w3cDoc = w3cdom.factory.newDocument();
 w3cdom.convert(jsoupDoc, w3cDoc);
 assertNotNull(w3cDoc);

 Element html = w3cDoc.getDocumentElement();
 assertEquals("html", html.getTagName());
 Element title = html.getElementsByTagName("title").item(0);
 assertEquals("Test Title", title.getTextContent());
 Element body = html.getElementsByTagName("body").item(0);
 Element p = body.getElementsByTagName("p").item(0);
 assertEquals("p", p.getTagName());
 assertEquals("myclass", p.getAttribute("class"));
 assertEquals("testID", p.getAttribute("id"));
 assertEquals("Hello World", p.getTextContent());
 }

 @Test
 public void testConvertWithNamespaces() throws ParserConfigurationException {
 Document jsoupDoc = DocumentHelper.parseText("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xmlns:test=\"http://test.com\" xmlns:x=\"http://www.w3.org/2001/XMLSchema\"><head><title>Test Title</title></head><body><test:test xmlns:test=\"http://test.com\"><x:text>Hello World</x:text></test:test></body></html>");
 W3CDom w3cdom = new W3CDom();
 Document w3cDoc = w3cdom.factory.newDocument();
 w3cdom.convert(jsoupDoc, w3cDoc);
 assertNotNull(w3cDoc);

 Element html = w3cDoc.getDocumentElement();
 assertEquals("html", html.getTagName());
 Element title = html.getElementsByTagName("title").item(0);
 assertEquals("Test Title", title.getTextContent());
 Element body = html.getElementsByTagName("body").item(0);
 Element test = body.getElementsByTagNameNS("http://test.com", "test").item(0);
 assertEquals("test", test.getTagName());
 Element text = test.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "text").item(0);
 assertEquals("text", text.getTagName());
 assertEquals("Hello World", text.getTextContent());
 }

 @Test
 public void testAsString() throws ParserConfigurationException, TransformerException {
 Document jsoupDoc = DocumentHelper.parseText("<html><head></head><body><p>Hello World</p></body></html>");
 W3CDom w3cdom = new W3CDom();
 Document w3cDoc = w3cdom.fromJsoup(jsoupDoc);
 String result = w3cdom.asString(w3cDoc);
 assertEquals("<html><head></head><body><p>Hello World</p></body></html>", result);
 }
}