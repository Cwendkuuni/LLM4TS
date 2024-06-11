package org.jsoup.nodes;

import org.junit.Assert;import org.junit.Before;import org.junit.Test;import org.jsoup.nodes.Document;import org.jsoup.nodes.Element;import org.jsoup.nodes.OutputSettings;import org.jsoup.nodes.QuirksMode;

public class DocumentTest {

 private Document document;

 @Before
 public void setup() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document document = new Document("http://example.com");
 Assert.assertEquals("http://example.com", document.baseUri());
 }

 @Test
 public void testCreateShell() {
 Document document = Document.createShell("http://example.com");
 Element html = document.head();
 Element head = html.head();
 Element body = document.body();

 Assert.assertEquals("http://example.com", document.baseUri());
 Assert.assertNotNull(html);
 Assert.assertNotNull(head);
 Assert.assertNotNull(body);
 }

 @Test
 public void testGetHeadElement() {
 document.title("Test Title");
 Element head = document.head();
 Element title = head.getElementsByTag("title").first();

 Assert.assertNotNull(title);
 Assert.assertEquals("Test Title", title.text().trim());
 }

 @Test
 public void testSetHeadElement() {
 document.title("Test Title");
 Element head = document.head();
 head.appendElement("title").text("New Title");

 Assert.assertEquals("New Title", document.title());
 }

 @Test
 public void testCreateElement() {
 Element link = document.createElement("a");
 Assert.assertEquals("a", link.nodeName());
 Assert.assertEquals("", link.baseUri());
 }

 @Test
 public void testNormalise() {
 document.title("Test Title");
 document.body().appendText("Test Body Text");
 document.text("Test Text");
 document.normalise();

 Element body = document.body();
 List<Node> bodyTextNodes = new ArrayList<>();
 for (Node node : body.childNodes) {
 if (node instanceof TextNode) {
 bodyTextNodes.add(node);
 }
 }

 Assert.assertEquals(2, bodyTextNodes.size());
 Assert.assertEquals("Test Body Text", bodyTextNodes.get(0).text().trim());
 Assert.assertEquals(" Test Text", bodyTextNodes.get(1).text().trim());
 }

 @Test
 public void testNormaliseWithQuirksMode() {
 Document documentWithQuirksMode = new Document("http://example.com") {
 @Override
 public QuirksMode quirksMode() {
 return QuirksMode.quirks;
 }
 };
 documentWithQuirksMode.title("Test Title");
 documentWithQuirksMode.body().appendText("Test Body Text");
 documentWithQuirksMode.text("Test Text");
 documentWithQuirksMode.normalise();

 Element body = documentWithQuirksMode.body();
 List<Node> bodyTextNodes = new ArrayList<>();
 for (Node node : body.childNodes) {
 if (node instanceof TextNode) {
 bodyTextNodes.add(node);
 }
 }

 Assert.assertEquals(3, bodyTextNodes.size());
 Assert.assertEquals("Test Body Text", bodyTextNodes.get(0).text().trim());
 Assert.assertEquals(" Test Text", bodyTextNodes.get(1).text().trim());
 Assert.assertEquals("", bodyTextNodes.get(2).text().trim());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document document = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBaseUri() {
 Document document = new Document("invalid://example.com");
 }
}