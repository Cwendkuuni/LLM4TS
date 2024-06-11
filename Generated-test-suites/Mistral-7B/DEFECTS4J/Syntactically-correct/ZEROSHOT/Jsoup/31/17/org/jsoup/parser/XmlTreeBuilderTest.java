package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.Iterator;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse("<?xml version=\"1.0\"?>\n<root>\n<element attr=\"value\">content</element>\n<another-element/>\n</root>");
 }

 @Test
 public void testInitialiseParse() {
 assertEquals("root", doc.tagName());
 assertEquals("http://www.w3.org/1998/XMLProlog", doc.baseUri().scheme() + "://" + doc.baseUri().host());
 assertNotNull(xmlTreeBuilder.stack);
 assertSame(doc, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessStartTag() {
 Element root = doc.rootElement();
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("root", element.baseUri().scheme() + "://" + element.baseUri().host());
 }

 @Test
 public void testProcessEndTag() {
 Element root = doc.rootElement();
 Element element = root.child(0);
 Element anotherElement = root.child(1);
 assertEquals(0, xmlTreeBuilder.stack.size());
 assertEquals(null, element.nextSibling());
 assertNull(anotherElement.previousSibling());
 }

 @Test
 public void testProcessComment() {
 Comment comment = doc.child(1);
 assertEquals("<!-- -->", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 TextNode content = (TextNode) doc.child(2);
 assertEquals("content", content.text());
 }

 @Test
 public void testProcessDoctype() {
 DocumentType doctype = (DocumentType) doc.child(0);
 assertEquals("xml", doctype.name());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 xmlTreeBuilder.initialiseParse("", null, null);
 xmlTreeBuilder.process(new Token(-1, ""));
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse("<root><element/><another-element><test/></another-element></root>");
 Element root = doc.rootElement();
 Element element = root.child(0);
 Element anotherElement = root.child(1);
 Element test = anotherElement.child(0);
 xmlTreeBuilder.popStackToClose(test.tag());
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(element, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testPopStackToCloseNotFound() {
 xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse("<root><element/><another-element></another-element></root>");
 Element root = doc.rootElement();
 Element element = root.child(0);
 Element anotherElement = root.child(1);
 xmlTreeBuilder.popStackToClose(new Tag("test"));
 assertEquals(2, xmlTreeBuilder.stack.size());
 }
}