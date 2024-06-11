package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class DocumentTest {

 private Document doc;

 @Before
 public void setup() {
 doc = Document.createShell("");
 }

 @Test
 public void testCreateDocument() {
 Document newDoc = new Document("");
 assertEquals("#document", newDoc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document shellDoc = Document.createShell("");
 Element html = shellDoc.head();
 Element head = html.head();
 Element body = html.body();

 assertEquals("html", html.tagName());
 assertEquals("head", head.tagName());
 assertEquals("body", body.tagName());

 assertNull(shellDoc.title());
 assertNull(head.title());
 assertNull(body.text());
 }

 @Test
 public void testTitle() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testGetTitleWithNoTitle() {
 assertEquals("", doc.title());
 }

 @Test
 public void testSetTitleWithNullTitle() {
 assertThrows(NullPointerException.class, () -> doc.title(null));
 }

 @Test
 public void testCreateElement() {
 Element element = doc.createElement("div");
 assertEquals("div", element.tagName());
 }

 @Test
 public void testNormalise() {
 Element p = doc.createElement("p").text("Test");
 doc.appendElement("html").appendElement("body").appendChild(p);
 doc.normalise();
 Element body = doc.body();
 TextNode textNode = (TextNode) body.getElementsByTag("text").first();
 assertEquals("Test", textNode.text());
 }

 @Test
 public void testNormaliseWithEmptyDocument() {
 doc.normalise();
 assertFalse(doc.body().hasText());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Element head = doc.findFirstElementByTagName("head", doc);
 Element body = doc.findFirstElementByTagName("body", doc);
 Element html = doc.findFirstElementByTagName("html", doc);

 assertEquals("head", head.tagName());
 assertEquals("body", body.tagName());
 assertEquals("html", html.tagName());
 }

 @Test
 public void testFindFirstElementByTagNameWithNullNode() {
 assertNull(doc.findFirstElementByTagName("head", null));
 }

 @Test
 public void testFindFirstElementByTagNameWithInvalidTagName() {
 assertNull(doc.findFirstElementByTagName("invalidTag", doc));
 }
}