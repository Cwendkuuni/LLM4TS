package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

 @Test
 public void testParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(new StringReader("<xml><root><child/><child/></root></xml>"), "http://test.com");

 Element root = document.rootElement();
 assertEquals("root", root.tagName());
 assertNotNull(root.child(0));
 assertNotNull(root.child(1));
 }

 @Test
 public void testParseWithFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(new StringReader("<child/>"), null, new Parser(xmlTreeBuilder));
 Element child = document.rootElement();
 Element parent = new Element("parent");
 parent.appendChild(child);

 List<Node> fragment = xmlTreeBuilder.parseFragment("<child/>", parent, null, new Parser(xmlTreeBuilder));
 assertFalse(fragment.isEmpty());
 assertEquals(1, fragment.size());
 Element resultChild = (Element) fragment.get(0);
 assertEquals("child", resultChild.tagName());
 assertEquals(parent, resultChild.parent());
 }

 @Test
 public void testParseDocumentType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root><child/></root>"), "http://test.com");
 DocumentType doctypeNode = document.doctype();
 assertNotNull(doctypeNode);
 assertNull(doctypeNode.publicIdentifier());
 assertNull(doctypeNode.systemIdentifier());
 }

 @Test
 public void testParseDocumentTypeWithPublicIdentifier() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" public \"-//W3C//DTD XHTML 1.0 Strict//EN\" system \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"?>\r\n<root><child/></root>"), "http://test.com");
 DocumentType doctypeNode = document.doctype();
 assertNotNull(doctypeNode);
 assertNotNull(doctypeNode.publicIdentifier());
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctypeNode.getPublicIdentifier());
 assertNull(doctypeNode.systemIdentifier());
 }

 @Test
 public void testParseDocumentTypeWithSystemIdentifier() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>><root><child/></root>"), "http://test.com");
 DocumentType doctypeNode = document.doctype();
 assertNull(doctypeNode);
 }

 @Test
 public void testParseComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(new StringReader("<!-- This is a comment --><root><child/></root>"), "http://test.com");
 List<Node> comments = document.childNodes();
 assertFalse(comments.isEmpty());
 assertEquals(1, comments.size());
 Comment comment = (Comment) comments.get(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test(expected = AssertionError.class)
 public void testParseUnexpectedToken() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(new StringReader("<html><body><script>alert('Hello')</script></body></html>"), "http://test.com");
 }
}