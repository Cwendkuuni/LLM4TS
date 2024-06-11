package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 assertNotNull(html);
 assertEquals("html", html.nodeName());
 Element head = html.head();
 assertNotNull(head);
 assertEquals("head", head.nodeName());
 Element body = html.body();
 assertNotNull(body);
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testDocumentTitle() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testDocumentTitleNull() {
 Document doc = new Document("http://example.com");
 assertNull(doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("div");
 assertNotNull(element);
 assertEquals("div", element.nodeName());
 }

 @Test
 public void testDocumentNormalise() {
 Document doc = new Document("http://example.com");
 doc.text("Test text");
 Element div = doc.createElement("div");
 div.text("Another test text");
 doc.appendChild(div);
 doc.normalise();
 Element body = doc.body();
 TextNode textNode = (TextNode) body.childNodes().get(0);
 assertEquals("Test textAnother test text", textNode.text());
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTitleNullInput() {
 Document doc = new Document("http://example.com");
 doc.title(null);
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 OutputSettings outputSettings = doc.outputSettings();
 assertNotNull(outputSettings);
 assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
 }
}