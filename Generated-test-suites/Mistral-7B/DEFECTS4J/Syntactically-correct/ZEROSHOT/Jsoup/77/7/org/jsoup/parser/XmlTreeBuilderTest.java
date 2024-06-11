package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testParse() {
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader("<xml><root><child><grandChild/><anotherChild/></child></root></xml>"), "http://test.com");
 Element root = xmlDoc.select("root").first();
 assertEquals("root", root.tagName());

 List<Node> children = root.childNodes();
 assertEquals(1, children.size());
 Element child = (Element) children.get(0);
 assertEquals("child", child.tagName());

 List<Node> grandChildren = child.childNodes();
 assertEquals(2, grandChildren.size());
 Element grandChild1 = (Element) grandChildren.get(0);
 assertTrue(grandChild1.tagName().equals("grandChild") || grandChild1.tagName().equals("GrandChild")); // tag name is case insensitive

 Element grandChild2 = (Element) grandChildren.get(1);
 assertTrue(grandChild2.tagName().equals("anotherChild") || grandChild2.tagName().equals("AnotherChild")); // tag name is case insensitive
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child><grandChild/><anotherChild/></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());

 List<Node> children = root.childNodes();
 assertEquals(1, children.size());
 Element child = (Element) children.get(0);
 assertEquals("child", child.tagName());

 List<Node> grandChildren = child.childNodes();
 assertEquals(2, grandChildren.size());
 Element grandChild1 = (Element) grandChildren.get(0);
 assertTrue(grandChild1.tagName().equals("grandChild") || grandChild1.tagName().equals("GrandChild")); // tag name is case insensitive

 Element grandChild2 = (Element) grandChildren.get(1);
 assertTrue(grandChild2.tagName().equals("anotherChild") || grandChild2.tagName().equals("AnotherChild")); // tag name is case insensitive
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 xmlTreeBuilder.parse(new StringReader(""), "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithBaseUri() {
 xmlTreeBuilder.parse(new StringReader(""), "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 xmlTreeBuilder.parse(null, "http://test.com");
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("root", null, null, null, null, null, 1, 1);
 Element root = xmlTreeBuilder.insert(startTagToken);
 assertEquals("root", root.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("root", null, null, null, null, null, 1, 1);
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("<!-- This is a comment -->");
 xmlTreeBuilder.insert(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.currentElement().childNodes().get(0);
 assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNodes().get(0);
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null, null, null, null, 1, 1);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
 assertEquals("html", documentType.tagName());
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.publicId());
 assertEquals(null, documentType.systemId());
 }
}