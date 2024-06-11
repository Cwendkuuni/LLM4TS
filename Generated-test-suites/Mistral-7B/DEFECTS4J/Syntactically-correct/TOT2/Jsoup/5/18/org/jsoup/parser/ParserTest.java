import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

 @Test
 public void testParseEmptyHTML() {
 Parser parser = new Parser("");
 Document doc = parser.parse();
 assertEquals("", doc.body().text());
 assertEquals(0, doc.numberOfElements());
 }

 @Test
 public void testParseValidHTML() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element title = doc.title();
 Element h1 = doc.body().getElementsByTag("h1").first();
 assertEquals("Test", title.text());
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals("", doc.head().text());
 Element h1 = doc.body().getElementsByTag("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><p foo='bar'><h1>Hello World</h1></p></body>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 Element h1 = doc.body().getElementsByTag("h1").first();
 assertEquals("Hello World", h1.text());
 Element p = doc.body().getElementsByTag("p").first();
 assertNotNull(p.attr("foo"));
 assertEquals("bar", p.attr("foo"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHTML() {
 Parser parser = new Parser(null, "http://example.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyBaseUri() {
 Parser parser = new Parser("<html><head></head><body></body></html>", "", false);
 }

 @Test
 public void testParseInvalidStartTag() {
 Parser parser = new Parser("<h1<foo bar='baz'>Hello World</foo>");
 Document doc = parser.parse();
 assertNull(doc.head());
 }

 @Test
 public void testParseInvalidEndTag() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></foot>");
 Document doc = parser.parse();
 assertNull(doc.head());
 }

 @Test
 public void testParseInvalidXMLDecl() {
 Parser parser = new Parser("<xml version='1.0'<title>Test</title>");
 Document doc = parser.parse();
 assertNull(doc.head());
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>");
 Document doc = parser.parse();
 assertNotNull(doc.body().child(0));
 assertEquals("", doc.body().child(0).text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<html><![CDATA[This is CDATA]]></html>");
 Document doc = parser.parse();
 assertEquals("This is CDATA", doc.body().text());
 }

 @Test
 public void testParseBaseTag() {
 String html = "<html><head><base href='/test'><title>Test</title></head><body></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals("/test", doc.baseUri());
 }

 @Test
 public void testParseScriptTag() {
 String html = "<html><head><script type='text/javascript'>alert('Hello World')</script><title>Test</title></head><body></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals("", doc.body().text());
 }

 @Test
 public void testParseUnknownTag() {
 Parser parser = new Parser("<html><body><unknown></body></html>");
 Document doc = parser.parse();
 assertNull(doc.head());
 }
}