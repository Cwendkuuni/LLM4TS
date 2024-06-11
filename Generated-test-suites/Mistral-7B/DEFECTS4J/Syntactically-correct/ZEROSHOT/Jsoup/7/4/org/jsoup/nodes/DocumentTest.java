package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc.body());
 assertNotNull(doc.head());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc.body());
 assertNotNull(doc.head());
 }

 @Test
 public void testTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 assertNotNull(doc.getElementsByTag("title").first());
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 assertNotNull(doc.getElementsByTag("title").first());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("a");
 assertNotNull(element);
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.text("Test text");
 doc.appendText(" Another test text ");
 Element p = doc.createElement("p");
 p.text("More test text");
 doc.body().prependChild(p);
 doc.normalise();
 assertEquals("Test text Another test text\nMore test text", doc.body().text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = new Document("http://example.com");
 Element html = doc.findFirstElementByTagName("html", doc);
 Element head = doc.findFirstElementByTagName("head", doc);
 Element body = doc.findFirstElementByTagName("body", doc);
 Element title = doc.findFirstElementByTagName("title", doc);
 assertEquals(html, doc.head().parentNode());
 assertEquals(head, doc.body().previousSibling());
 assertEquals(body, doc.title().nextSibling());
 assertEquals(title, doc.title());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("http://example.com");
 doc.head().appendElement("title").text("Test Title");
 doc.body().appendElement("h1").text("Test H1");
 assertEquals("<html><head><title>Test Title</title></head><body><h1>Test H1</h1></body></html>", doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = new Document("http://example.com");
 doc.body().text("Test text");
 assertEquals("Test text", doc.body().text());
 }

 @Test
 public void testNodeName() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
 }
}