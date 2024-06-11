package org.jsoup.nodes;

import org.junit.Test;import static org.junit.Assert.*;import static org.jsoup.Jsoup.parse;import static org.jsoup.Jsoup.parseBodyPart;import static org.jsoup.nodes.Document.*;

/**
 * Test for Document class
 */
public class DocumentTest {

 // Test for constructor
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.tagName());
 }

 // Test for createShell method
 @Test
 public void testCreateShell() {
 Document doc = createShell("http://example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 Element html = doc.getElementsByTag("html").first();
 Element head = html.getElementsByTag("head").first();
 Element body = html.getElementsByTag("body").first();
 assertNotNull(head);
 assertNotNull(body);
 }

 // Test for head method
 @Test
 public void testHeadMethod() {
 Document doc = createShell("http://example.com");
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 }

 // Test for body method
 @Test
 public void testBodyMethod() {
 Document doc = createShell("http://example.com");
 Element body = doc.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 // Test for title method
 @Test
 public void testTitleMethod() {
 Document doc = createShell("http://example.com");
 doc.title("Test Title");
 Element title = doc.getElementsByTag("title").first();
 assertEquals("Test Title", title.text().trim());
 }

 // Test for title method with empty title
 @Test
 public void testTitleMethodEmpty() {
 Document doc = createShell("http://example.com");
 assertEquals("", doc.title());
 }

 // Test for title method with null title
 @Test(expected = NullPointerException.class)
 public void testTitleMethodNull() {
 Document doc = createShell("http://example.com");
 doc.title(null);
 }

 // Test for createElement method
 @Test
 public void testCreateElementMethod() {
 Document doc = new Document("http://example.com");
 Element p = doc.createElement("p");
 assertNotNull(p);
 assertEquals("p", p.tagName());
 }

 // Test for normalise method
 @Test
 public void testNormaliseMethod() {
 Document doc = parse("<html><head><title>Test Title</title></head><body></body></html>");
 doc.normalise();
 assertNotNull(doc.title());
 assertEquals("Test Title", doc.title().text().trim());
 }

 // Test for outerHtml method
 @Test
 public void testOuterHtmlMethod() {
 Document doc = parse("<html><head><title>Test Title</title></head><body></body></html>");
 assertEquals("<html><head><title>Test Title</title></head><body></body></html>", doc.outerHtml());
 }

 // Test for text method
 @Test
 public void testTextMethod() {
 Document doc = parse("<html><head><title>Test Title</title></head><body></body></html>");
 doc.text("Test Text");
 assertEquals("Test Text", doc.body().text().trim());
 }

 // Test for nodeName method
 @Test
 public void testNodeNameMethod() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 }
}