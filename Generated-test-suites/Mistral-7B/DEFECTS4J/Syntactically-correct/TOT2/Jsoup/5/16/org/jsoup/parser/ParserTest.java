import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 @Test
 public void testParse() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 assertEquals("Test Page", doc.title().text());
 Element h1 = doc.select("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 assertEquals(1, doc.childNodes().size());
 Element h1 = doc.select("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<div><h1>Hello World</h1></div>"; // unknown tag "div" set as self-closing
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

 assertEquals(1, doc.childNodes().size());
 Element h1 = doc.select("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 @Test
 public void testParseEmptyHtml() {
 Document doc = Parser.parse("<html></html>", "http://example.com");
 assertEquals(2, doc.childNodes().size());
 assertEquals("", doc.title().text());
 assertNull(doc.body());
 }

 @Test
 public void testParseInvalidHtml() {
 String invalidHtml = "<html><body><script><!-- <![CDATA[-->\n<script>alert(\"Hello\");</script>\n<![CDATA[--]> --> </body></html>";
 Document doc = Parser.parse(invalidHtml, "http://example.com");

 assertEquals(3, doc.childNodes().size());
 Element script = doc.select("script").first();
 assertNotNull(script);
 }

 @Test
 public void testParseXmlDecl() {
 String xmlDeclHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(xmlDeclHtml, "http://example.com");
 Element xmlDecl = doc.select("xml-declaration").first();
 assertNotNull(xmlDecl);
 assertEquals("1.0", xmlDecl.attr("version"));
 }

 @Test
 public void testParseComment() {
 String htmlWithComment = "<html><!--This is a comment-->\n<head><title>Test Page</title></head>\n<body><h1>Hello World</h1></body>\n</html>";
 Document doc = Parser.parse(htmlWithComment, "http://example.com");
 Element comment = doc.select("comment").first();
 assertNotNull(comment);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseEndTag() {
 String htmlWithMismatchedEndTag = "<html><body><h1>Hello World</h1></body></body>";
 // Parser will throw an exception if it encounters a mismatched end tag
 // So we need to wrap it in a try-catch block
 try {
 Parser.parse(htmlWithMismatchedEndTag, "http://example.com");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseInvalidEndTag() {
 String htmlWithInvalidEndTag = "<html><body><h1>Hello World</h1></badtag>";
 // Parser will throw an exception if it encounters an invalid end tag
 // So we need to wrap it in a try-catch block
 try {
 Parser.parse(htmlWithInvalidEndTag, "http://example.com");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseInvalidStartTag() {
 String htmlWithInvalidStartTag = "<html><h1>Hello World</body>";
 // Parser will throw an exception if it encounters an invalid start tag
 // So we need to wrap it in a try-catch block
 try {
 Parser.parse(htmlWithInvalidStartTag, "http://example.com");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseInvalidAttribute() {
 String htmlWithInvalidAttribute = "<html><body><div base='href'></div></body></html>";
 // Parser will throw an exception if it encounters an invalid attribute
 // So we need to wrap it in a try-catch block
 try {
 Parser.parse(htmlWithInvalidAttribute, "http://example.com");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseUnknownTag() {
 String htmlWithUnknownTag = "<html><body><div unknown></div></body></html>";
 Document doc = Parser.parse(htmlWithUnknownTag, "http://example.com");
 Element div = doc.select("div").first();
 assertSame(Element.class, div.getClass());
 assertTrue(div.tag().isUnknownTag());
 }

 @Test
 public void testParseEmptyTag() {
 String htmlWithEmptyTag = "<html><body><img src=''></body></html>";
 Document doc = Parser.parse(htmlWithEmptyTag, "http://example.com");
 Element img = doc.select("img").first();
 assertEquals("", img.attr("src"));
 }

 @Test
 public void testParseSelfClosingTag() {
 String htmlWithSelfClosingTag = "<html><body><img src='http://example.com'/></body></html>";
 Document doc = Parser.parse(htmlWithSelfClosingTag, "http://example.com");
 Element img = doc.select("img").first();
 assertEquals("http://example.com", img.attr("src"));
 assertTrue(img.tag().isSelfClosing());
 }

 @Test
 public void testParseDataTag() {
 String htmlWithDataTag = "<html><body><script type='text/javascript' src='script.js'></script></body></html>";
 Document doc = Parser.parse(htmlWithDataTag, "http://example.com");
 Element script = doc.select("script").first();
 assertEquals("script.js", script.attr("src"));
 assertEquals("text/javascript", script.attr("type"));
 }

 @Test
 public void testParseBaseTag() {
 String htmlWithBaseTag = "<html><body><base href='http://newbase.com'/><a href='about.html'>About</a></body></html>";
 Document doc = Parser.parse(htmlWithBaseTag, "http://example.com");
 assertEquals("http://newbase.com", doc.baseUri());
 String aboutHref = doc.select("a").first().attr("href");
 assertEquals("about.html", aboutHref);
 }
}