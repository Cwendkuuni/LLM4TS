package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenCollector;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 private String xmlString = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://www.example.com/\">" +
         "<element x=\"value\">Hello World</element>" +
         "<comment>This is a comment</comment>" +
         "<root></root>" +
         "</test>";

 @Test
 public void testXmlTreeBuilder() {
 Document xmlDoc = Jsoup.parse(xmlString, Parser.xmlParser());
 assertNotNull(xmlDoc);
 assertEquals("test", xmlDoc.tagName());

 List<Node> nodes = xmlDoc.childNodes();
 assertEquals(4, nodes.size());

 Node rootNode = nodes.get(3);
 assertEquals("root", rootNode.tagName());
 assertNull(rootNode.attributes());

 List<Node> rootChildren = rootNode.childNodes();
 assertEquals(1, rootChildren.size());

 Node elementNode = rootChildren.get(0);
 assertEquals("element", elementNode.tagName());

 List<Node> elementAttributes = elementNode.attributes();
 assertEquals(1, elementAttributes.size());
 assertEquals("value", elementAttributes.get(0).getKey());
 assertEquals("Hello World", elementAttributes.get(0).getValue());

 List<Node> elementChildren = elementNode.childNodes();
 assertEquals(1, elementChildren.size());

 Node textNode = elementChildren.get(0);
 assertEquals(TextNode.class, textNode.getClass());
 assertEquals("Hello World", textNode.text());

 Element commentElement = (Element) nodes.get(1);
 assertEquals("comment", commentElement.tagName());
 assertEquals("This is a comment", commentElement.text());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidXml() {
 Document xmlDoc = Jsoup.parse("<xml/><tag", Parser.xmlParser());
 fail("Should throw AssertionError when parsing invalid XML");
 }

 @Test
 public void testXmlTreeBuilderWithErrors() {
 Document xmlDoc = Jsoup.parse("<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://www.example.com/\"><element x=\"value\">Hello World</element>" +
         "<comment>This is a comment</comment>" +
         "<root></root></test><badTag>", Parser.xmlParser());
 List<Node> nodes = xmlDoc.childNodes();
 assertEquals(5, nodes.size());

 List<Node> rootChildren = xmlDoc.childNodes().get(3).childNodes();
 assertEquals(3, rootChildren.size());

 List<Node> errorNodes = xmlDoc.outputSettings().parseErrorList().getAll();
 assertEquals(1, errorNodes.size());
 assertEquals("unexpected '<' at line 7 column 1", errorNodes.get(0).getMessage());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:x=\"http://www.example.com/\">" +
         "<element x=\"value\">Hello World</element>" +
         "</test>";
 List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, null, null);
 assertEquals(3, nodes.size());

 Element rootElement = (Element) nodes.get(0);
 assertEquals("test", rootElement.tagName());

 List<Node> rootChildren = rootElement.childNodes();
 assertEquals(1, rootChildren.size());

 Node elementNode = rootChildren.get(0);
 assertEquals("element", elementNode.tagName());

 List<Node> elementChildren = elementNode.childNodes();
 assertEquals(0, elementChildren.size());
 }
}