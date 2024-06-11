package org.jsoup.parser;

import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest. core.IsInstanceOf;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

public class XmlTreeBuilderTest {

 @Test
 public void testXmlTreeBuilderDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<root><child1><grandchild1>Hello</grandchild1></child1><child2></child2></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput);

 assertEquals("root", xmlDoc.tagName());
 Element root = xmlDoc.element(0);
 List<Node> childNodes = root.childNodes();
 assertEquals("child1", childNodes.get(0).tagName());
 Element child1 = (Element) childNodes.get(0);
 List<Node> grandChildNodes = child1.childNodes();
 assertEquals("grandchild1", grandChildNodes.get(0).tagName());
 assertEquals("Hello", grandChildNodes.get(0).text());
 assertEquals("child2", childNodes.get(1).tagName());
 assertTrue(childNodes.get(1).nodeType() == Node.DOCUMENT_TYPE_NODE);
 }

 @Test
 public void testXmlTreeBuilderParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<child><grandchild>Hello</grandchild></child>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();

 assertEquals(2, parsedNodes.size());
 Element child = (Element) parsedNodes.get(0);
 assertEquals("child", child.tagName());
 List<Node> grandChildNodes = child.childNodes();
 assertEquals(1, grandChildNodes.size());
 Element grandChild = (Element) grandChildNodes.get(0);
 assertEquals("grandchild", grandChild.tagName());
 assertEquals("Hello", grandChild.text());
 }

 @Test
 public void testXmlTreeBuilderParseInvalidInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String invalidInput = "<root><child><grandchild>Hello<script>alert('XSS')</script></grandchild></child></root>";
 List<ParseError> parseErrors = xmlTreeBuilder.parse(invalidInput, null, ParseErrorList.noTracking(), ParseSettings.preserveCase).errors();

 assertEquals(1, parseErrors.size());
 ParseError parseError = parseErrors.get(0);
 assertEquals("Error at line 1: Unexpected token <script> in prolog", parseError.getMessage());
 }

 @Test
 public void testXmlTreeBuilderParseSelfClosingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<root><child1><img src=\"image.jpg\" /></child1></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput);

 assertEquals("root", xmlDoc.tagName());
 Element root = xmlDoc.element(0);
 List<Node> childNodes = root.childNodes();
 assertEquals("child1", childNodes.get(0).tagName());
 Element child1 = (Element) childNodes.get(0);
 List<Node> imgNodes = child1.childNodes();
 assertEquals("img", imgNodes.get(0).tagName());
 assertTrue(imgNodes.get(0).nodeType() == Node.ELEMENT_NODE);
 assertTrue(imgNodes.get(0).hasAttrs());
 assertEquals("image.jpg", imgNodes.get(0).attr("src"));
 }

 @Test
 public void testXmlTreeBuilderParseDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<!DOCTYPE root [ <!ELEMENT child (grandchild)> ]><root><child><grandchild>Hello</grandchild></child></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput);
 DocumentType docType = (DocumentType) xmlDoc.childNodes().get(0);
 assertEquals("root", docType.getName());
 assertEquals("root", docType.getPublicIdentifier());
 assertEquals(null, docType.getSystemIdentifier());
 }
}