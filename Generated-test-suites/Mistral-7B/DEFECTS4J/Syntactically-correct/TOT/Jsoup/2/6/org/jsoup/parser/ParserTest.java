import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParse() throws Exception {
 String html = "<html><head></head><body><h1>Hello</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.tagName());
 Element head = doc.head();
 Element body = doc.body();
 Element h1 = body.getElementsByTag("h1").first();

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, head.childNodeSize());
 assertEquals(1, body.childNodeSize());
 assertEquals(1, h1.childNodeSize());
 assertEquals("Hello", h1.text());
 }

 @Test
 public void testParseBodyFragment() throws Exception {
 String bodyHtml = "<body><h1>Hello</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(bodyTag, doc.tagName());
 Element bodyFragment = doc.body();
 Element h1 = bodyFragment.getElementsByTag("h1").first();

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, bodyFragment.childNodeSize());
 assertEquals(1, h1.childNodeSize());
 assertEquals("Hello", h1.text());
 }

 @Test
 public void testParseEmptyHtml() throws Exception {
 String html = "<html></html>";
 Document doc = Parser.parse(html, "http://example.com");

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.childNodeSize());
 assertNotNull(doc.head());
 assertEquals(0, doc.head().childNodeSize());
 assertNotNull(doc.body());
 assertEquals(0, doc.body().childNodeSize());
 }

 @Test
 public void testParseInvalidHtml() throws Exception {
 String html = "<html><h1<script>alert('XSS')</script>";
 Document doc = Parser.parse(html, "http://example.com");

 assertEquals("http://example.com", doc.baseUri());
 assertEquals(htmlTag, doc.tagName());
 assertFalse(doc.body().hasText()); // body should be empty due to script tag
 }

 @Test
 public void testParseComment() throws Exception {
 String html = "<html><!-- this is a comment --><body></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, doc.body().childNodeSize());
 Element comment = doc.body().getElementsByTag(Comment.class.getName()).first();
 assertEquals("this is a comment", comment.text());
 }

 @Test
 public void testParseCdata() throws Exception {
 String html = "<html><![CDATA[this is cdata]]><body></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 assertEquals(1, doc.childNodeSize());
 assertEquals(1, doc.body().childNodeSize());
 Element cdata = doc.body().getElementsByTag(TextNode.class.getName()).get(0);
 assertEquals("this is cdata", cdata.text());
 }

 @Test
 public void testParseBaseTag() throws Exception {
 String html = "<html><head><base href=\"/new-dir/\"/><body><a href=\"foo.html\">Foo</a></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 assertEquals("/new-dir/foo.html", doc.absUrl("foo.html"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyString() {
 Parser.parse("", "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidBaseUri() {
 Parser.parse("<html></html>", null);
 }
}