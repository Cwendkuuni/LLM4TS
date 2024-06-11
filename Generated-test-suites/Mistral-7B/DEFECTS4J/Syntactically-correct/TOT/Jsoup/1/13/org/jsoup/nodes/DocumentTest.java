package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

 // Test constructor with valid baseUri
 @Test
 public void testDocumentConstruction() {
 Document doc = new Document("http://example.com");
 Assert.assertEquals("http://example.com", doc.baseUri());
 Assert.assertTrue(doc.head() == null);
 Assert.assertTrue(doc.body() == null);
 }

 // Test createShell method with valid baseUri
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.select("html").first();
 Element head = html.getElementsByTag("head").first();
 Element body = html.getElementsByTag("body").first();
 Assert.assertNotNull(html);
 Assert.assertNotNull(head);
 Assert.assertNotNull(body);
 }

 // Test head method with document containing head element
 @Test
 public void testHeadAccess() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
 Element head = doc.head();
 Assert.assertNotNull(head);
 Assert.assertEquals("Test Title", head.text());
 }

 // Test head method with document not containing head element
 @Test(expected = IndexOutOfBoundsException.class)
 public void testHeadAccessEmpty() {
 Document doc = Jsoup.parse("<html><body></body></html>", "http://example.com");
 doc.head();
 }

 // Test body method with document containing body element
 @Test
 public void testBodyAccess() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test body text</p></body></html>", "http://example.com");
 Element body = doc.body();
 Assert.assertNotNull(body);
 Assert.assertEquals("Test body text", body.text());
 }

 // Test body method with document not containing body element
 @Test(expected = IndexOutOfBoundsException.class)
 public void testBodyAccessEmpty() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head></html>", "http://example.com");
 doc.body();
 }

 // Test title method with document containing title element
 @Test
 public void testTitleAccess() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
 String title = doc.title();
 Assert.assertEquals("Test Title", title);
 }

 // Test title method with document not containing title element
 @Test
 public void testTitleAccessEmpty() {
 Document doc = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
 String title = doc.title();
 Assert.assertEquals("", title);
 }

 // Test title method with setting title
 @Test
 public void testTitleSet() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://example.com");
 doc.title("New Title");
 Assert.assertEquals("New Title", doc.title());
 }

 // Test createElement method with valid tagName
 @Test
 public void testCreateElementValidTagName() {
 Document doc = new Document("http://example.com");
 Element button = doc.createElement("button");
 Assert.assertEquals("button", button.tagName());
 }

 // Test normalise method with empty document
 @Test
 public void testNormaliseEmpty() {
 Document doc = new Document("http://example.com");
 Assert.assertEquals(doc, doc.normalise());
 }

 // Test normalise method with document containing only root element
 @Test
 public void testNormaliseRoot() {
 Document doc = new Document("http://example.com");
 doc.setText("Test text");
 doc.appendElement("div").setText("Another test text");
 doc.normalise();
 Element div = doc.select("div").first();
 Assert.assertEquals("Test text", doc.body().text());
 Assert.assertEquals("", div.text());
 }

 // Test normalise method with document containing text nodes outside body
 @Test
 public void testNormaliseTextOutsideBody() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><text>Test text outside body</text></html>", "http://example.com");
 doc.normalise();
 Assert.assertEquals("Test text outside body", doc.select("text").first().text());
 Assert.assertEquals("", doc.body().text());
 }

 // Test normalise method with document containing text nodes inside head
 @Test
 public void testNormaliseTextInsideHead() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title><text>Test text inside head</text></head></html>", "http://example.com");
 doc.normalise();
 Assert.assertEquals("", doc.head().text());
 Assert.assertEquals("Test text inside head", doc.select("text").first().text());
 }

 // Test normalise method with document containing text nodes inside body
 @Test
 public void testNormaliseTextInsideBody() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><text>Test text inside body</text></body></html>", "http://example.com");
 doc.normalise();
 Assert.assertEquals("Test text inside body", doc.body().text());
 Assert.assertEquals("", doc.select("text").first().text());
 }

 // Test outerHtml method with document containing only root element
 @Test
 public void testOuterHtmlRoot() {
 Document doc = new Document("http://example.com");
 doc.setText("Test text");
 Assert.assertEquals("<html><head></head><body>" + doc.text() + "</body></html>", doc.outerHtml());
 }

 // Test text method with document containing only text node
 @Test
 public void testTextOnlyText() {
 Document doc = new Document("http://example.com");
 doc.setText("Test text");
 Assert.assertEquals("Test text", doc.text().toString());
 }

 // Test text method with document containing elements
 @Test
 public void testTextWithElements() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test text with elements</p></body></html>", "http://example.com");
 Assert.assertEquals("Test text with elements", doc.text().toString());
 }
}