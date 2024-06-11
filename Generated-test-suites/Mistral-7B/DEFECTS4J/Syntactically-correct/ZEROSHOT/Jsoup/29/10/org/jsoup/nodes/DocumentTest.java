package org.jsoup.nodes.test;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("baseUri");
 assertEquals("#document", doc.nodeName());
 assertNull(doc.head());
 assertNull(doc.body());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("baseUri");
 assertEquals("#document", doc.nodeName());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test
 public void testGetHeadAndBody() {
 Document doc = Document.createShell("baseUri");
 Element head = doc.head();
 Element body = doc.body();

 assertEquals("head", head.nodeName());
 assertEquals("body", body.nodeName());
 }

 @Test
 public void testGetTitle() {
 Document doc = Document.createShell("baseUri");
 doc.title("Test Title");
 String title = doc.title();

 assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = Document.createShell("baseUri");
 doc.title("");
 doc.title("Test Title");
 String title = doc.title();

 assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = Document.createShell("baseUri");
 Element element = doc.createElement("div");

 assertEquals("div", element.nodeName());
 assertEquals("baseUri", element.baseUri());
 }

 @Test
 public void testNormalise() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.createElement("html");
 Element body = doc.createElement("body");
 Element title = doc.createElement("title");
 title.text("Test Title");
 html.appendChild(title);
 html.appendChild(body);
 doc.appendChild(html);
 doc.text("Test Text");
 doc.normalise();

 // title should be inside body
 List<Node> bodyChildren = body.childNodes();
 int titleIndex = -1;
 for (int i = 0; i < bodyChildren.size(); i++) {
 Node node = bodyChildren.get(i);
 if (node instanceof Element && node.nodeName().equals("title")) {
 titleIndex = i;
 break;
 }
 }
 assertEquals(0, titleIndex);
 }

 @Test
 public void testNormaliseTextNodes() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.createElement("html");
 Element body = doc.createElement("body");
 Element title = doc.createElement("title");
 title.text("Test Title");
 html.appendChild(title);
 html.appendChild(body);
 doc.appendChild(html);
 doc.text("Test Text1 Test Text2 Test Text3");
 doc.normalise();

 String bodyText = body.text();
 assertEquals("Test Text1 Test Text2 Test Text3", bodyText);
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.createElement("html");
 Element head = doc.createElement("head");
 Element body = doc.createElement("body");
 doc.appendChild(html);
 html.appendChild(head);
 html.appendChild(body);
 doc.head().appendElement("title").text("Test Title");
 doc.body().appendElement("p").text("Test Paragraph");

 Element title = doc.findFirstElementByTagName("title", this);
 assertEquals("title", title.nodeName());
 assertEquals("Test Title", title.text());

 Element p = doc.findFirstElementByTagName("p", this);
 assertEquals("p", p.nodeName());
 assertEquals("Test Paragraph", p.text());
 }

 @Test
 public void testOuterHtml() {
 Document doc = Document.createShell("baseUri");
 Element html = doc.createElement("html");
 Element head = doc.createElement("head");
 Element body = doc.createElement("body");
 doc.appendChild(html);
 html.appendChild(head);
 html.appendChild(body);
 doc.head().appendElement("title").text("Test Title");
 doc.body().appendElement("p").text("Test Paragraph");
 String htmlString = doc.outerHtml();
 assertTrue(htmlString.contains("<html>"));
 assertTrue(htmlString.contains("<head>"));
 assertTrue(htmlString.contains("<title>Test Title</title>"));
 assertTrue(htmlString.contains("<body>"));
 assertTrue(htmlString.contains("<p>Test Paragraph</p>"));
 }

 @Test
 public void testText() {
 Document doc = Document.createShell("baseUri");
 doc.text("Test Text");
 String text = doc.text();
 assertEquals("Test Text", text);
 }

 @Test
 public void testClone() {
 Document doc = Document.createShell("baseUri");
 Document clone = doc.clone();
 assertEquals(doc, clone);
 }

 @Test
 public void testOutputSettings() {
 Document doc = Document.createShell("baseUri");
 OutputSettings settings = new OutputSettings();
 doc.outputSettings(settings);
 OutputSettings clonedSettings = doc.outputSettings();
 assertEquals(settings, clonedSettings);
 }

 @Test
 public void testQuirksMode() {
 Document doc = Document.createShell("baseUri");
 doc.quirksMode(QuirksMode.quirks);
 QuirksMode quirksMode = doc.quirksMode();
 assertEquals(QuirksMode.quirks, quirksMode);
 }
}