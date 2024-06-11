package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

 private Document document;

 @Before
 public void setup() {
 document = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 assertNull(doc.head());
 assertNull(doc.body());
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 Element html = doc.head();
 assertEquals(Tag.HTML, html.tagName());
 assertNull(html.parent());
 assertNull(html.childNodeSize());
 html = doc.body();
 assertEquals(Tag.BODY, html.tagName());
 assertNull(html.parent());
 assertNull(html.childNodeSize());
 }

 @Test
 public void testHead() {
 Element head = document.head();
 assertEquals(Tag.HEAD, head.tagName());
 assertSame(document, head.parent());
 }

 @Test
 public void testBody() {
 Element body = document.body();
 assertEquals(Tag.BODY, body.tagName());
 assertSame(document, body.parent());
 }

 @Test
 public void testTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testCreateElement() {
 Element p = document.createElement("p");
 assertEquals(Tag.P, p.tagName());
 assertEquals("http://example.com", p.baseUri());
 assertNull(p.parent());
 }

 @Test
 public void testNormalise() {
 document.text("Test Test test");
 Element p = document.createElement("p");
 p.text("Test");
 document.body().prependChild(p);
 document.body().text(" test");
 document.normalise();
 List<Node> bodyChildren = document.body().childNodes();
 assertEquals(2, bodyChildren.size());
 assertEquals("Test", bodyChildren.get(0).text());
 assertEquals(" test", bodyChildren.get(1).text());
 }

 @Test
 public void testOutputSettings() {
 OutputSettings outputSettings = document.outputSettings();
 assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
 assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
 }

 @Test
 public void testSetOutputSettings() {
 OutputSettings outputSettings = new OutputSettings();
 outputSettings.charset("UTF-16");
 document.outputSettings(outputSettings);
 assertEquals(Charset.forName("UTF-16"), document.outputSettings().charset());
 }
}