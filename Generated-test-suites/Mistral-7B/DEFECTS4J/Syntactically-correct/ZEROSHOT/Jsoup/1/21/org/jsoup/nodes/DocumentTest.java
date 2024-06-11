package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("baseUri");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertNotNull(doc);
 Element html = doc.select("html").first();
 assertNotNull(html);
 Element head = html.getElementsByTag("head").first();
 assertNotNull(head);
 Element body = html.getElementsByTag("body").first();
 assertNotNull(body);
 }

 @Test
 public void testHeadElement() {
 Document doc = new Document("baseUri");
 Element head = doc.head();
 assertNull(head);
 head = doc.head();
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 assertEquals("Test Title", head.text().trim());
 }

 @Test
 public void testBodyElement() {
 Document doc = new Document("baseUri");
 Element body = doc.body();
 assertNull(body);
 body = doc.body();
 doc.title("Test Title");
 doc.text("Test Body");
 assertEquals("Test Body", body.text().trim());
 }

 @Test
 public void testTitleSetter() {
 Document doc = new Document("baseUri");
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test(expected = NullPointerException.class)
 public void testTitleNull() {
 Document doc = new Document("baseUri");
 doc.title(null);
 }

 @Test
 public void testCreateElement() {
 Document doc = new Document("baseUri");
 Element element = doc.createElement("a");
 assertNotNull(element);
 assertEquals("a", element.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = new Document("baseUri");
 doc.appendText("Test One ");
 doc.appendText("Test Two ");
 doc.appendElement("Test Three").text("Test Three Text");
 Element body = doc.body();
 body.appendText("Test Four ");
 body.appendText("Test Five ");

 doc.normalise();

 List<Node> bodyChildren = body.childNodes();
 List<String> expectedChildren = new ArrayList<>();
 expectedChildren.add("Test One ");
 expectedChildren.add("Test Two ");
 expectedChildren.add("Test Three Text");
 expectedChildren.add("Test Four ");
 expectedChildren.add("Test Five ");

 for (Node node : bodyChildren) {
 if (node instanceof TextNode) {
 TextNode textNode = (TextNode) node;
 assertEquals(expectedChildren.remove(0), textNode.text().trim());
 }
 }

 assertTrue(expectedChildren.isEmpty());
 }

 @Test
 public void testOuterHtml() {
 Document doc = new Document("baseUri");
 doc.appendElement("html").text("Test HTML");
 doc.appendElement("head").text("Test Head");
 doc.appendElement("body").text("Test Body");
 assertEquals("<html>Test HTML<head>Test Head</head><body>Test Body</body>", doc.outerHtml());
 }

 @Test
 public void testTextSetter() {
 Document doc = new Document("baseUri");
 doc.appendText("Test One ");
 doc.appendText("Test Two ");
 doc.appendElement("Test Three").text("Test Three Text");
 Element body = doc.body();
 body.appendText("Test Four ");
 body.appendText("Test Five ");

 doc.text("Test Document");
 assertEquals("Test Document", doc.outerHtml());
 }

}