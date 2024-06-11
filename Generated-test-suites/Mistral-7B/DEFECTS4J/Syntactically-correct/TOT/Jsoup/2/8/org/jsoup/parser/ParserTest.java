import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseEmptyHtml() {
 Parser parser = new Parser("<html></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc.body();
 assertEquals("html", html.tagName());
 assertNull(html.childNodes().get(0));
 }

 @Test
 public void testParseNormalHtml() {
 Parser parser = new Parser("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc.body();
 Element head = html.child(0);
 Element title = head.child(0);
 assertEquals("html", html.tagName());
 assertEquals("head", head.tagName());
 assertEquals("title", title.tagName());
 assertEquals("Test Title", title.text());
 }

 @Test
 public void testParseComments() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc.body();
 assertEquals("html", html.tagName());
 assertTrue(html.childNodes().isEmpty());
 }

 @Test
 public void testParseScriptTag() {
 Parser parser = new Parser("<html><script src=\"script.js\"></script></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc.body();
 Element script = html.child(0);
 assertEquals("html", html.tagName());
 assertEquals("script", script.tagName());
 assertNull(script.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidStartTag() {
 Parser parser = new Parser("<html<body></html>", "http://example.com");
 try {
 parser.parse();
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEndTag() {
 Parser parser = new Parser("<html><body></body></body></html>", "http://example.com");
 try {
 parser.parse();
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXmlDecl() {
 Parser parser = new Parser("<html<? xml:version=\"1.0\"?>", "http://example.com");
 try {
 parser.parse();
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidAttribute() {
 Parser parser = new Parser("<html <key=\"value\" />", "http://example.com");
 try {
 parser.parse();
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidCdata() {
 Parser parser = new Parser("<html><![CDATA[<script>alert('XSS')</script>]]>", "http://example.com");
 try {
 parser.parse();
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}