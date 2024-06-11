package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private Tokenizer tokeniser;
 private XmlTreeBuilder builder;

 @Before
 public void setUp() {
 tokeniser = new Tokenizer();
 builder = new XmlTreeBuilder();
 }

 @Test
 public void testProcessStartTag() {
 // given
 String xmlString = "<root><child element='value'/></root>";

 // when
 Document doc = Jsoup.parse(xmlString, Parser.xmlParser());
 List<Node> children = doc.childNodes();
 Element root = (Element) children.get(0);

 // then
 assertEquals("root", root.nodeName());
 assertEquals(1, root.childNodes().size());
 Element child = (Element) root.childNodes().get(0);
 assertEquals("child", child.nodeName());
 assertEquals("value", child.attr("element"));
 }

 @Test
 public void testProcessEndTag() {
 // given
 String xmlString = "<root><child/></root>";

 // when
 Document doc = Jsoup.parse(xmlString, Parser.xmlParser());
 List<Node> children = doc.childNodes();
 Element root = (Element) children.get(0);
 Element child = (Element) children.get(1);
 tokeniser = new Tokenizer(xmlString);
 builder.initialiseParse(xmlString, "", new ParseErrorList());
 builder.runParser();

 // then
 assertEquals(2, stack.size());
 assertEquals("root", stack.get(stack.size() - 1).nodeName());
 assertEquals("child", stack.get(stack.size() - 2).nodeName());
 assertEquals(0, stack.size() - 2); // pop the root element
 }

 @Test
 public void testProcessComment() {
 // given
 String xmlString = "<!-- This is a comment -->";

 // when
 Document doc = Jsoup.parse(xmlString, Parser.xmlParser());
 List<Node> children = doc.childNodes();

 // then
 assertNotNull(children.get(0));
 assertEquals(Node.COMMENT_NODE, children.get(0).nodeType());
 }

 @Test
 public void testProcessCharacter() {
 // given
 String xmlString = "<root>&#123;</root>";

 // when
 Document doc = Jsoup.parse(xmlString, Parser.xmlParser());
 List<Node> children = doc.childNodes();

 // then
 assertEquals(1, children.size());
 assertEquals(Node.TEXT_NODE, children.get(0).nodeType());
 assertEquals("&#123;", children.get(0).nodeValue());
 }

 @Test
 public void testProcessDoctype() {
 // given
 String xmlString = "<!DOCTYPE root [ <!ATTLIST child element CDATA #REQUIRED > ]>";

 // when
 Document doc = Jsoup.parse(xmlString, Parser.xmlParser());
 List<Node> children = doc.childNodes();

 // then
 assertEquals(1, children.size());
 assertEquals(Node.DOCUMENT_TYPE_NODE, children.get(0).nodeType());
 DocumentType documentType = (DocumentType) children.get(0);
 assertEquals("root", documentType.nodeName());
 }

 @Test
 public void testProcessUnknownTag() {
 // given
 String xmlString = "<customTag attribute='value'/>";

 // when
 Document doc = Jsoup.parse(xmlString, Parser.xmlParser());
 List<Node> children = doc.childNodes();

 // then
 assertEquals(1, children.size());
 assertEquals(Node.ELEMENT_NODE, children.get(0).nodeType());
 Element customTag = (Element) children.get(0);
 assertEquals("customTag", customTag.nodeName());
 assertNotNull(customTag.attr("attribute"));
 }

 @Test
 public void testProcessSelfClosingTag() {
 // given
 String xmlString = "<customTag attribute='value'/>";
 // when
 Document doc = Jsoup.parse(xmlString, Parser.xmlParser());
 List<Node> children = doc.childNodes(); // then
 assertEquals(1, children.size());
 assertEquals(Node.ELEMENT_NODE, children.get(0).nodeType());
 Element customTag = (Element) children.get(0);
 assertEquals("customTag", customTag.nodeName());
 assertNotNull(customTag.attr("attribute"));
 assertFalse(customTag.hasChildren());
 }
}