package org.jsoup.parser;

import org.jsoup.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 @org.junit.BeforeClass
 static void setup() {
 Helper.setParseHtmlErrors(false);
 Helper.setParseXmlErrors(false);
 }

 @org.junit.Test
 public void testXmlTreeBuilderParse() {
 String xmlString = "<root><element attr=\"value\">content</element></root>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), null);
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 assertEquals(1, root.childElementCount());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute(0).name());
 assertEquals("value", element.attribute(0).value());
 assertEquals("content", element.text());
 }

 @org.junit.Test
 public void testXmlTreeBuilderParseWithBaseUri() {
 String xmlString = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><element attr=\"value\">content</element></root>";
 String baseUri = "http://example.com/";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), baseUri);
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 assertEquals(1, root.childElementCount());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute(0).name());
 assertEquals("value", element.attribute(0).value());
 assertEquals("content", element.text());
 assertEquals(baseUri, element.baseUri());
 }

 @org.junit.Test
 public void testXmlTreeBuilderParseFragment() {
 String xmlFragment = "<element attr=\"value\">content</element>";
 List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, null, null);
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute(0).name());
 assertEquals("value", element.attribute(0).value());
 assertEquals("content", element.text());
 }

 @org.junit.Test
 public void testXmlTreeBuilderParseFragmentWithContext() {
 String xmlFragment = "<element attr=\"value\">content</element>";
 Element context = new Element("root");
 List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, context, null, null);
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute(0).name());
 assertEquals("value", element.attribute(0).value());
 assertEquals("content", element.text());
 assertEquals(context, element.parent());
 }

 @org.junit.Test
 public void testXmlTreeBuilderParseDoctype() {
 String xmlString = "<!DOCTYPE root [<!ATTLIST element attr CDATA #REQUIRED>]><root><element attr=\"value\">content</element></root>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), null);
 DocumentType doctype = doc.documentType();
 assertEquals("root", doctype.nodeName());
 assertEquals("root", doctype.getName());
 assertEquals("", doctype.publicId());
 assertEquals("", doctype.systemId());
 List<Attribute> attributes = doctype.attributes();
 assertEquals(1, attributes.size());
 Attribute attribute = attributes.get(0);
 assertEquals("ATTLIST", attribute.namespace());
 assertEquals("element", attribute.localName());
 assertEquals("attr", attribute.name());
 assertEquals("CDATA", attribute.type());
 assertEquals("CDATA", attribute.value());
 assertEquals(false, attribute.specified());
 }

 @org.junit.Test
 public void testXmlTreeBuilderParseComment() {
 String xmlString = "<!-- This is a comment -->";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), null);
 Comment comment = (Comment) doc.childNodes().get(0);
 assertEquals("This is a comment", comment.data());
 }

 @org.junit.Test
 public void testXmlTreeBuilderParseCData() {
 String xmlString = "<![CDATA[This is some CDATA text]]>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), null);
 CDataNode cdata = (CDataNode) doc.childNodes().get(0);
 assertEquals("This is some CDATA text", cdata.text());
 }

 @org.junit.Test
 public void testXmlTreeBuilderParseEmpty() {
 String xmlString = "<>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), null);
 assertNull(doc.childNodes());
 }

 @org.junit.Test
 public void testXmlTreeBuilderParseInvalid() {
 String xmlString = "<root><element attr=\"value\" <badTag/> content>";
 Document doc = XojmTreeBuilder.parse(new StringReader(xmlString), null);
 assertEquals(2, doc.childElementCount());
 Element root = doc.rootElement();
 Element element = root.child(0);
 assertTrue(element.hasAttr("attr"));
 assertFalse(element.hasAttr("badTag"));
 assertEquals(1, root.childElementCount() + root.childNodes().size());
 }
}