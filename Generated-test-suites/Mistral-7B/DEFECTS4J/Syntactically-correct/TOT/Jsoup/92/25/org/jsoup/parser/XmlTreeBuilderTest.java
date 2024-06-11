package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseXMLWithRootElement() {
 String xml = "<root><element>Hello World</element></root>";
 document = xmlTreeBuilder.parse(new StringReader(xml));
 Element root = document.rootElement();
 Element element = root.child(0);
 assertNotNull(root);
 assertNotNull(element);
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testParseXMLWithSelfClosingElement() {
 String xml = "<tag />";
 document = xmlTreeBuilder.parse(new StringReader(xml));
 Element tag = document.select("tag").first();
 assertNotNull(tag);
 assertEquals(1, tag.childNodes().size());
 }

 @Test
 public void testParseXMLWithEmptyElement() {
 String xml = "<tag></tag>";
 document = xmlTreeBuilder.parse(new StringReader(xml));
 Element tag = document.select("tag").first();
 assertNotNull(tag);
 assertEquals(0, tag.childNodes().size());
 }

 @Test
 public void testParseXMLWithComment() {
 String xml = "<!-- This is a comment -->";
 document = xmlTreeBuilder.parse(new StringReader(xml));
 List<Node> comments = document.select("--*");
 assertEquals(1, comments.size());
 assertEquals("This is a comment", ((Comment) comments.get(0)).text());
 }

 @Test
 public void testParseXMLWithCDATA() {
 String xml = "<![CDATA[ <script type=\"text/javascript\">\nvar x = 1;\n</script> ]]>";
 document = xmlTreeBuilder.parse(new StringReader(xml));
 List<Node> cdataNodes = document.select("script[type='text/javascript'] > *:not(*)");
 assertEquals(1, cdataNodes.size());
 assertNotNull(((CDataNode) cdataNodes.get(0)).data());
 }

 @Test
 public void testParseXMLWithDoctype() {
 String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 document = xmlTreeBuilder.parse(new StringReader(xml));
 DocumentType doctypeNode = document.doctype();
 assertNotNull(doctypeNode);
 assertEquals("html", doctypeNode.name());
 }

 @Test
 public void testParseXMLWithAttributes() {
 String xml = "<tag attr1=\"value1\" attr2='value2' />";
 document = xmlTreeBuilder.parse(new StringReader(xml));
 Element tag = document.select("tag").first();
 assertEquals("value1", tag.attr("attr1"));
 assertEquals("value2", tag.attr("attr2"));
 }

 @Test
 public void testParseXMLWithNestedElements() {
 String xml = "<root><element><subElement>Hello</subElement></element></root>";
 document = xmlTreeBuilder.parse(new StringReader(xml));
 Element root = document.rootElement();
 Element element = root.child(0);
 Element subElement = element.child(0);
 assertEquals("Hello", subElement.text());
 }

 @Test
 public void testParseXMLWithBogusXMLDeclaration() {
 String xml = "<?xml version='1.0' encoding='UTF-8'?>";
 document = xmlTreeBuilder.parse(new StringReader(xml));
 assertEquals(1, document.childNodes().size());
 assertEquals(xml, document.childNodes().get(0).toString());
 }

 @Test
 public void testParseXMLWithFragment() {
 String xmlFragment = "<root><element>Hello World</element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, new Parser(xmlTreeBuilder));
 Element root = (Element) nodes.get(0);
 Element element = (Element) nodes.get(1);
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testParseXMLWithFragmentAndContext() {
 String xmlFragment = "<root><element>Hello World</element></root>";
 Element context = Jsoup.parse("<parent><context>").select("context").first();
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, context, null, new Parser(xmlTreeBuilder));
 Element root = (Element) nodes.get(0);
 Element element = (Element) nodes.get(1);
 assertEquals("Hello World", element.text());
 assertEquals(context, root.parent());
 }
}