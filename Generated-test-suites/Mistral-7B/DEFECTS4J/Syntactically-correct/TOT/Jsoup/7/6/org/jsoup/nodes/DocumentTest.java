package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

public class DocumentTest {

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("http://example.com", doc.baseUri());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testFindElements() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 Element head = doc.head();
 Element body = doc.body();

 Element title = doc.titleElement();
 assertEquals("Test Title", title.text());
 assertEquals(head, title.parent());
 assertEquals(doc, title.ancestors().get(0));

 Element newHead = doc.createElement("head");
 newHead.appendElement("title").text("New Test Title");
 doc.head().replaceWith(newHead);
 assertEquals("New Test Title", doc.title());
 }

 @Test
 public void testNormalize() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 doc.body().appendText(" Test Body ");
 doc.text("Test Text");

 doc.normalize();
 Element bodyEl = doc.body();
 TextNode titleText = (TextNode) doc.getElementsByClass("title").first().child(0);
 TextNode bodyText = (TextNode) bodyEl.childNodes().get(0);
 TextNode textText = (TextNode) doc.getElementsByClass("text").first().child(0);

 assertEquals("Test Title", titleText.text());
 assertEquals("Test Body ", bodyText.text());
 assertEquals("Test Text", textText.text());
 }

 @Test
 public void testInvalidBaseUri() {
 Document doc = new Document(null);
 assertNull(doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTagName() {
 Document doc = new Document("http://example.com");
 doc.createElement("z");
 }

 @Test
 public void testOutputSettings() {
 OutputSettings settings = new Document().outputSettings();
 assertEquals(Entities.EscapeMode.base, settings.escapeMode());
 assertEquals(StandardCharsets.UTF_8, settings.charset());
 assertTrue(settings.prettyPrint());
 assertEquals(1, settings.indentAmount());

 Document doc = new Document("http://example.com");
 doc.outputSettings().prettyPrint(false);
 assertEquals(false, doc.outputSettings().prettyPrint());
 }
}