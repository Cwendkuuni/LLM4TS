package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;

public class DocumentTest {

 @Test
 public void testEmptyDocumentCreation() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("http://example.com", doc.baseUri());
 assertNull(doc.title());
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertEquals("http://example.com", doc.baseUri());
 Element html = doc.head();
 assertNotNull(html);
 Element head = html.getFirstElementChild();
 assertNotNull(head);
 Element body = head.getNextElementSibling();
 assertNotNull(body);
 }

 @Test
 public void testTitleAccessors() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("http://example.com");
 Element newElement = doc.createElement("a");
 assertNotNull(newElement);
 assertEquals("a", newElement.nodeName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("http://example.com");
 doc.appendText("Testing document normalisation");
 doc.head().appendText(" Testing head normalisation");
 doc.appendElement("html").appendText(" Testing outerHTML");
 doc.normalise();
 assertEquals("Testing document normalisationTesting head normalisationTesting outerHTMLTesting document normalisation", doc.text());
 }

 @Test
 public void testGetElementsByTag() {
 Document doc = new Document("http://example.com");
 doc.appendElement("html").appendElement("head").appendElement("title").text("Test");
 Element title = doc.getElementsByTag("title").first();
 assertEquals("Test", title.text());
 }

 @Test
 public void testTextNodes() {
 Document doc = new Document("http://example.com");
 doc.text("Testing text normalisation");
 doc.head().text("Testing head text normalisation");
 doc.normalise();
 assertEquals("Testing document normalisationTesting head normalisationTesting document normalisation", doc.text());
 }

 @Test
 public void testOutputSettings() {
 Document doc = new Document("http://example.com");
 doc.outputSettings().prettyPrint(false);
 assertEquals("", doc.outerHtml());

 doc.outputSettings().prettyPrint(true);
 doc.title("Test Title");
 doc.body().appendText("Test Body");
 doc.normalise();
 assertEquals("<html><head><title>Test Title</title></head><body></body></html>", doc.outerHtml());
 }

 @Test
 public void testClone() {
 Document doc = new Document("http://example.com");
 doc.title("Test Title");
 Document clone = doc.clone();
 assertEquals("Test Title", clone.title());
 }
}