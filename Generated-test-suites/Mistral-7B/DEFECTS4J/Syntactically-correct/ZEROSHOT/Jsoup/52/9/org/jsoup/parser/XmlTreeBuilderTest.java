package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testInitialiseParse() {
  String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child tag=\"test\"/>\n</root>\n";
  String baseUri = "http://example.com";
  ParseErrorList errors = new ParseErrorList();

  xmlTreeBuilder.initialiseParse(input, baseUri, errors);

  assertEquals("http://example.com", xmlTreeBuilder.doc.baseUri());
  assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
  assertEquals(Document.OutputSettings.Encoding.UTF_8, xmlTreeBuilder.doc.outputSettings().encoding());
  assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
 }

 @Test
 public void testProcessStartTag() {
  String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child tag=\"test\" id=\"1\"/>\n</root>\n";
  String baseUri = "http://example.com";
  ParseErrorList errors = new ParseErrorList();

  xmlTreeBuilder.initialiseParse(input, baseUri, errors);
  xmlTreeBuilder.runParser();

  Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
  Element child = (Element) root.childNodes().get(0);

  assertEquals("child", child.nodeName());
  assertEquals("test", child.tagName());
  assertEquals("1", child.attributes().get("id"));
 }

 @Test
 public void testProcessEndTag() {
  String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child tag=\"test\"/>\n</child>\n</root>\n";
  String baseUri = "http://example.com";
  ParseErrorList errors = new ParseErrorList();

  xmlTreeBuilder.initialiseParse(input, baseUri, errors);
  xmlTreeBuilder.runParser();

  Element root = (Element) xmlTreebuilder.doc.childNodes().get(0);
  Element child = (Element) root.childNodes().get(0);

  xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));

  assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
  String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<!-- This is a comment -->\n<root>\n<child tag=\"test\"/>\n</root>\n";
  String baseUri = "http://example.com";
  ParseErrorList errors = new ParseErrorList();

  xmlTreeBuilder.initialiseParse(input, baseUri, errors);
  xmlTreeBuilder.runParser();

  Comment comment = (Comment) xmlTreeBuilder.doc.childNodes().get(0);

  assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testProcessCharacter() {
  String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child tag=\"test\">Hello World!</child>\n</root>\n";
  String baseUri = "http://example.com";
  ParseErrorList errors = new ParseErrorList();

  xmlTreeBuilder.initialiseParse(input, baseUri, errors);
  xmlTreeBuilder.runParser();

  TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(1).childNodes().get(0);

  assertEquals("Hello World!", textNode.getData());
 }

 @Test
 public void testProcessDoctype() {
  String input = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml.dtd\">\n<root>\n<child tag=\"test\"/>\n</root>\n";
  String baseUri = "http://example.com";
  ParseErrorList errors = new ParseErrorList();

  xmlTreeBuilder.initialiseParse(input, baseUri, errors);
  xmlTreeBuilder.runParser();

  DocumentType documentType = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);

  assertEquals("xml", documentType.getName());
  assertNotNull(documentType.getPublicIdentifier());
  assertNotNull(documentType.getSystemIdentifier());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
  String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child tag=\"test\">\n<!>This is not a valid token<\n</child>\n</root>\n";
  String baseUri = "http://example.com";
  ParseErrorList errors = new ParseErrorList();

  xmlTreeBuilder.initialiseParse(input, baseUri, errors);
  xmlTreeBuilder.runParser();
 }

 @Test
 public void testParseFragment() {
  String input = "<root>\n<child tag=\"test\"/>\n</root>";
  String baseUri = "http://example.com";
  ParseErrorList errors = new ParseErrorList();

  List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors);

  assertTrue(nodes.size() == 3);
  Element root = (Element) nodes.get(0);
  Element child = (Element) nodes.get(1);

  assertEquals("child", child.nodeName());
  assertEquals("test", child.tagName());
  assertEquals("root", root.nodeName());
 }
}