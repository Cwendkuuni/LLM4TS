package org.jsoup.parser.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 @Test
 public void testXmlParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlString = "<root><element attr='value'>Hello World</element></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString));

 Element rootElement = xmlDocument.getRootElement();
 Assertions.assertEquals("root", rootElement.tagName());
 Element element = rootElement.getElementsByTag("element").first();
 Assertions.assertEquals("element", element.tagName());
 Assertions.assertEquals("value", element.attr("attr"));
 Assertions.assertEquals("Hello World", element.text().getWholeText());
 }

 @Test
 public void testXmlParseWithSelfClosingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlString = "<root><element tag='selfClosing' /></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString));

 Element rootElement = xmlDocument.getRootElement();
 Element selfClosingElement = rootElement.getElementsByTag("element").first();
 Assertions.assertEquals("selfClosing", selfClosingElement.tagName());
 Assertions.assertNull(selfClosingElement.children());
 }

 @Test
 public void testXmlParseWithComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlString = "<root><!-- This is a comment --></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString));

 Element rootElement = xmlDocument.getRootElement();
 Assertions.assertEquals("root", rootElement.tagName());
 List<Node> comments = rootElement.getAllElements().get(0).childNodes();
 Assertions.assertEquals(1, comments.size());
 Assertions.assertTrue(comments.get(0) instanceof Comment);
 }

 @Test
 public void testXmlParseWithCDataSection() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlString = "<root><![CDATA[This is a CDATA section]]></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString));

 Element rootElement = xmlDocument.getRootElement();
 List<Node> cdataSections = rootElement.childNodes();
 Assertions.assertEquals(1, cdataSections.size());
 Assertions.assertTrue(cdataSections.get(0) instanceof CDataNode);
 }

 @Test
 public void testXmlParseWithDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString));

 DocumentType doctype = xmlDocument.doctype();
 Assertions.assertEquals("html", doctype.name());
 Assertions.assertEquals("about:legacy-compat", doctype.systemId());
 }

 @Test
 public void testXmlParseWithErrorInInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlString = "<root><element><!-- This is a comment -->Hello World";
 Document xmlDocument = null;

 try {
 xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString));
 } catch (Exception e) {
 // Expected
 }

 Assertions.assertNull(xmlDocument);
 }

 @Test
 public void testXmlParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlFragment = "<element attr='value'>Hello World</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, null);

 Element element = (Element) nodes.get(0);
 Assertions.assertEquals("element", element.tagName());
 Assertions.assertEquals("value", element.attr("attr"));
 Assertions.assertEquals("Hello World", element.text().getWholeText());
 }
}