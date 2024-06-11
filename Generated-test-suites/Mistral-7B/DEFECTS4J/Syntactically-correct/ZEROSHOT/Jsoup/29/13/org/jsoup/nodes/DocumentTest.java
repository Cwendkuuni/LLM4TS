package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

 @org.junit.Before
 public void setUp() {
 // Initialize any mock objects or pre-conditions here if needed
 }

 @org.junit.After
 public void tearDown() {
 // Perform any post-test cleanup here, if needed
 }

 @org.junit.Test
 public void testDocumentCreation() {
 Document doc = new Document("baseUri");
 assertTrue(doc instanceof Document);
 assertEquals("#document", doc.nodeName());
 }

 @org.junit.Test
 public void testCreateShell() {
 Document shell = Document.createShell("baseUri");
 assertTrue(shell instanceof Document);
 assertTrue(shell.head() != null);
 assertTrue(shell.body() != null);
 }

 @org.junit.Test
 public void testHeadAccess() {
 Document doc = Document.createShell("baseUri");
 Element head = doc.head();
 assertEquals(doc, head.parent());
 }

 @org.junit.Test
 public void testBodyAccess() {
 Document doc = Document.createShell("baseUri");
 Element body = doc.body();
 assertEquals(doc, body.parent());
 }

 @org.junit.Test
 public void testTitleAccess() {
 Document doc = Document.createShell("baseUri");
 String title = "test title";
 doc.title(title);
 assertEquals(title, doc.title());
 }

 @org.junit.Test
 public void testTitleSet() {
 Document doc = Document.createShell("baseUri");
 doc.title("test title");
 Element titleEl = doc.getElementsByTag("title").first();
 assertEquals("test title", titleEl.text().trim());
 }

 @org.junit.Test
 public void testCreateElement() {
 Document doc = Document.createShell("baseUri");
 Element element = doc.createElement("div");
 assertTrue(element instanceof Element);
 }

 @org.junit.Test
 public void testNormalise() {
 Document doc = Document.createShell("baseUri");
 doc.appendText("test text before normalise");
 Element div = doc.createElement("div");
 div.appendText("test text inside div");
 doc.body().appendChild(div);
 doc.normalise();
 List<Node> textNodes = new ArrayList<>();
 for (Node node : doc.body().childNodes()) {
 if (node instanceof TextNode) {
 textNodes.add(node);
 }
 }
 assertEquals(3, textNodes.size());
 assertEquals("test text before normalise", textNodes.get(0).nodeValue().trim());
 assertEquals("test text inside div", textNodes.get(1).nodeValue().trim());
 assertEquals("", textNodes.get(2).nodeValue().trim());
 }

 @org.junit.Test
 public void testFindFirstElementByTagName() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.findFirstElementByTagName("html", doc);
 assertEquals(doc, html.parent());
 }

 @org.junit.Test
 public void testOuterHtml() {
 Document doc = Document.createShell("baseUri");
 doc.appendElement("html").appendElement("head").appendElement("body");
 Element titleEl = doc.appendElement("title").appendText("test title");
 assertEquals("<html><head></head><body></body></html>", doc.outerHtml());
 assertEquals("test title", doc.title());
 }

 @org.junit.Test
 public void testText() {
 Document doc = Document.createShell("baseUri");
 doc.body().appendText("test text");
 assertEquals("test text", doc.text());
 }

 @org.junit.Test
 public void testClone() {
 Document doc = Document.createShell("baseUri");
 Document clone = doc.clone();
 assertEquals(doc, clone);
 }
}