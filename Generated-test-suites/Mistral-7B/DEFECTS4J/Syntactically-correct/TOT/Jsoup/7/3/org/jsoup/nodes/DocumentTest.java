package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

/**
 * Tests for Document class
 */
public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("baseUri");
 assertTrue(doc instanceof Document);
 assertEquals("#root", doc.tagName());
 assertEquals("baseUri", doc.baseUri());
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertTrue(doc instanceof Document);
 assertEquals("#root", doc.tagName());
 assertEquals("baseUri", doc.baseUri());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testHeadAndBodyAccessors() {
 Document doc = Document.createShell("baseUri");
 Element head = doc.head();
 Element body = doc.body();

 assertEquals(head, doc.head());
 assertEquals(body, doc.body());
 }

 @Test
 public void testTitleAccessors() {
 Document doc = Document.createShell("baseUri");
 doc.title("Test Title");

 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testTitleSetterNull() {
 Document doc = Document.createShell("baseUri");

 try {
 doc.title(null);
 fail("Should not allow null title");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("baseUri");
 Element p = doc.createElement("p");

 assertEquals("p", p.tagName());
 assertEquals(doc.baseUri(), p.baseUri());
 }

 @Test
 public void testNormalise() {
 Document doc = Document.createShell("baseUri");
 doc.head().appendElement("foo").appendText(" bar");
 doc.body().appendElement("baz").appendText(" qux ");
 doc.normalise();

 Element head = doc.head();
 assertEquals(1, head.childNodeSize());
 assertEquals("foo", head.child(0).tagName());

 Element body = doc.body();
 assertEquals(1, body.childNodeSize());
 assertEquals("baz", body.child(0).tagName());

 TextNode textNode = (TextNode) head.child(0).child(0);
 assertEquals(" bar", textNode.text());

 TextNode bodyTextNode = (TextNode) body.child(0);
 assertEquals(" qux ", bodyTextNode.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.appendElement("html").appendElement("body").appendElement("div").appendElement("p");

 assertEquals(html, doc.findFirstElementByTagName("html", doc));
 assertEquals(html, doc.findFirstElementByTagName("body", html));
 assertEquals(html, doc.findFirstElementByTagName("div", html));
 assertEquals(html, doc.findFirstElementByTagName("p", doc));
 assertEquals(null, doc.findFirstElementByTagName("table", doc));
 }

 @Test
 public void testOuterHtmlAndText() {
 Document doc = Document.createShell("baseUri");
 Element p = doc.createElement("p").text("Test");
 doc.body().appendChild(p);

 assertEquals("<p>Test</p>", p.outerHtml());
 assertEquals("Test", p.text());
 }
}