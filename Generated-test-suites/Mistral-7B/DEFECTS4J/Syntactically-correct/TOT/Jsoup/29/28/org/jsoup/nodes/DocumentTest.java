package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("html", html.tagName());
 Element head = html.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 Element body = html.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 @Test
 public void testTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 Element titleEl = doc.head().getElementsByTagName("title").first();
 assertNotNull(titleEl);
 assertEquals("Test Title", titleEl.text());
 }

 @Test
 public void testTitleSetTwice() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title 1");
 doc.title("Test Title 2");
 Element titleEl = doc.head().getElementsByTagName("title").first();
 assertNotNull(titleEl);
 assertEquals("Test Title 2", titleEl.text());
 }

 @Test
 public void testTitleEmpty() {
 Document doc = new Document("http://example.com");
 doc.title("");
 Element titleEl = doc.head().getElementsByTagName("title").first();
 assertNotNull(titleEl);
 assertEquals("", titleEl.text());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.appendText("Test text 1");
 doc.appendText(" Test text 2 ");
 doc.appendElement(doc.createElement("p")).text("Test text 3");
 doc.normalise();
 Element body = doc.body();
 TextNode text1 = (TextNode) body.getElementsByTag("text").first().get(0);
 TextNode text2 = (TextNode) body.getElementsByTag("text").first().get(1);
 Element p = body.getElementsByTag("p").first();
 TextNode pText = (TextNode) p.getElementsByTag("text").first();
 assertEquals("Test text 1", text1.text());
 assertEquals(" Test text 2 ", text2.text());
 assertEquals("Test text 3", pText.text());
 }

 @Test
 public void testNormaliseEmpty() {
 Document doc = new Document("http://example.com");
 doc.normalise();
 assertFalse(doc.body().hasText());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testTitleNull() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }
}