package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTest {

 // Test constructor
 @Test
 public void testDocumentConstructor() {
 Document doc = new Document("http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 assertEquals("#document", doc.nodeName());
 }

 // Test createShell method
 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 assertEquals("#document", doc.nodeName());
 assertEquals(3, doc.numChildren());

 Element html = doc.head();
 assertEquals("head", html.nodeName());
 assertEquals(0, html.numChildren());

 Element head = doc.head();
 assertEquals("head", head.nodeName());
 assertEquals(0, head.numChildren());

 Element body = doc.body();
 assertEquals("body", body.nodeName());
 assertEquals(0, body.numChildren());
 }

 // Test head method
 @Test
 public void testHeadMethod() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 Element head = doc.head();
 assertEquals("head", head.nodeName());
 assertEquals("Test", head.title());
 }

 // Test body method
 @Test
 public void testBodyMethod() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
 Element body = doc.body();
 assertEquals("body", body.nodeName());
 assertEquals("<p>Test</p>", body.outerHtml());
 }

 // Test title method
 @Test
 public void testTitleMethod() {
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
 String title = doc.title();
 assertEquals("Test", title);
 }

 // Test title method with null title
 @Test(expected = NullPointerException.class)
 public void testTitleMethodWithNullTitle() {
 Document doc = Jsoup.parse("<html><head></head><body></body></html>");
 doc.title(null);
 }

 // Test title method with empty title
 @Test
 public void testTitleMethodWithEmptyTitle() {
 Document doc = Jsoup.parse("<html><head><title></title></head><body></body></html>");
 doc.title(" ");
 assertEquals("", doc.title());
 }

 // Test createElement method
 @Test
 public void testCreateElementMethod() {
 Document doc = new Document("http://example.com");
 Element element = doc.createElement("a");
 assertEquals("a", element.nodeName());
 assertEquals("http://example.com", element.baseUri());
 }

 // Test normalise method
 @Test
 public void testNormaliseMethod() {
 Document doc = Jsoup.parse("<html><head></head><body><p>Test</p><div>Test</div><p> Test </p></body></html>");
 doc.normalise();
 assertEquals(4, doc.numChildren());

 Element html = doc.head();
 assertEquals("head", html.nodeName());
 assertEquals(0, html.numChildren());

 Element head = doc.head();
 assertEquals("head", head.nodeName());
 assertEquals(0, head.numChildren());

 Element body = doc.body();
 assertEquals("body", body.nodeName());
 assertEquals(3, body.numChildren().intValue());

 Element text1 = body.child(0);
 assertEquals("Test", text1.text());

 Element a = doc.createElement("a");
 body.appendChild(a);
 assertEquals(4, body.numChildren().intValue());
 }
}