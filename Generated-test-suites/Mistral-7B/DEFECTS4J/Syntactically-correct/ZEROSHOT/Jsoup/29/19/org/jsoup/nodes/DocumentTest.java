package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.List;

public class DocumentTest {

 @org.junit.Test
 public void testCreateDocument() {
 Document document = new Document("http://example.com");
 assertNotNull(document);
 assertEquals("#root", document.tagName());
 }

 @org.junit.Test
 public void testCreateShellDocument() {
 Document document = Document.createShell("http://example.com");
 assertNotNull(document);
 assertEquals("#root", document.tagName());

 Element html = document.head();
 assertNotNull(html);
 assertEquals("html", html.tagName());

 Element head = document.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());

 Element body = document.body();
 assertNotNull(body);
 assertEquals("body", body.tagName());
 }

 @org.junit.Test
 public void testAccessHeadElement() {
 Document document = Document.createShell("http://example.com");
 Element head = document.head();
 assertNotNull(head);
 }

 @org.junit.Test
 public void testAccessBodyElement() {
 Document document = Document.createShell("http://example.com");
 Element body = document.body();
 assertNotNull(body);
 }

 @org.junit.Test
 public void testGetTitle() {
 Document document = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>");
 String title = document.title();
 assertEquals("Test Title", title);
 }

 @org.junit.Test
 public void testSetTitle() {
 Document document = Jsoup.parse("<html><head><title></title></head><body></body></html>");
 document.title("Test Title");
 Element titleEl = document.getElementsByTag("title").first();
 assertEquals("Test Title", titleEl.text());
 }

 @org.junit.Test
 public void testCreateElement() {
 Document document = Jsoup.parse("<html><body></body></html>");
 Element newElement = document.createElement("a");
 assertNotNull(newElement);
 assertEquals("a", newElement.tagName());
 }

 @org.junit.Test
 public void testNormaliseDocument() {
 Document document = Jsoup.parse("<html><head></head><body><p>Text 1</p><p>Text 2</p><p>Text 3</p></body></html>");
 document.normalise();
 Element body = document.body();
 TextNode textNode1 = (TextNode) body.getElementsByTag("p").get(0).getElementsByTag("text").first();
 TextNode textNode2 = (TextNode) body.getElementsByTag("p").get(1).getElementsByTag("text").first();
 TextNode textNode3 = (TextNode) body.getElementsByTag("p").get(2).getElementsByTag("text").first();
 assertEquals("Text 1", textNode1.text());
 assertEquals("Text 2", textNode2.text());
 assertEquals("Text 3", textNode3.text());
 }

 @org.junit.Test
 public void testNormaliseStructure() {
 Document document = Jsoup.parse("<html><head><title></title></head><body><p>Text 1</p></body><body><p>Text 2</p></body></html>");
 document.normalise();
 Element body = document.body();
 TextNode textNode1 = (TextNode) body.getElementsByTag("p").get(0).getElementsByTag("text").first();
 TextNode textNode2 = (TextNode) body.getElementsByTag("p").get(0).getElementsByTag("text").last();
 assertEquals("Text 1", textNode1.text());
 assertEquals("Text 2", textNode2.text());
 }

 @org.junit.Test
 public void testFindFirstElementByTagName() {
 Document document = Jsoup.parse("<html><head></head><body><p>Text 1</p><p>Text 2</p><p>Text 3</p></body></html>");
 Element head = document.findFirstElementByTagName("head", document);
 assertNull(head);

 Element body = document.findFirstElementByTagName("body", document);
 assertEquals("body", body.tagName());
 }

 @org.junit.Test
 public void testOuterHtml() {
 Document document = Jsoup.parse("<html><body><p>Text 1</p></body></html>");
 String outerHtml = document.outerHtml();
 assertEquals("<html><body><p>Text 1</p></body></html>", outerHtml);
 }

 @org.junit.Test
 public void testTextMethod() {
 Document document = Jsoup.parse("<html><body></body></html>");
 document.text("Test Text");
 assertEquals("Test Text", document.getElementsByTag("body").first().text());
 }
}