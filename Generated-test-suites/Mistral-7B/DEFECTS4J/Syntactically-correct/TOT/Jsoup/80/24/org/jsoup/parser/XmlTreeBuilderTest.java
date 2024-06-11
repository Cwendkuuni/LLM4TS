package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testParseXmlDocument() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><element attribute='value'>Content</element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");

 assertEquals("root", xmlDoc.tagName());
 Element rootElement = xmlDoc.element("root");
 Element element = rootElement.element("element");
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attribute"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseEmptyXmlDocument() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(""), "http://example.com");
 assertEquals("", xmlDoc.body().text());
 }

 @Test
 public void testParseXmlDocumentWithInvalidTagName() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><% invalidTag %><element>Content</element></root>";
 ParseErrorList errors = new ParseErrorList();
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com", errors, ParseSettings.defaultSettings());
 assertEquals(1, errors.size());
 assertEquals("Element <% invalidTag > is invalid: not well-formed (invalid token)", errors.toString());
 }

 @Test
 public void testParseXmlDocumentWithEmptyElement() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><element></element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 Element emptyElement = xmlDoc.element("root").element("element");
 assertEquals("", emptyElement.text());
 }

 @Test
 public void testParseXmlDocumentWithSelfClosingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><img src='image.jpg' /> <element>Content</element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 Element imageElement = xmlDoc.element("root").element("img");
 assertNull(imageElement.childNodes());
 }

 @Test
 public void testParseXmlDocumentWithMixedContent() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><element><subElement>Content 1</subElement><subElement2>Content 2</subElement2></element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 List<Node> childNodes = xmlDoc.childNodes();
 assertEquals(1, childNodes.size());
 Element rootElement = (Element) childNodes.get(0);
 List<Node> elementChildNodes = rootElement.childNodes();
 assertEquals(1, elementChildNodes.size());
 Element element = (Element) elementChildNodes.get(0);
 List<Node> subElementChildNodes = element.childNodes();
 assertEquals(2, subElementChildNodes.size());
 assertEquals("Content 1", subElementChildNodes.get(0).text());
 assertEquals("Content 2", subElementChildNodes.get(1).text());
 }
}