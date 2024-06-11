import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

 @Test
 public void testParseValidHtml() {
 String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 assertEquals("Test Title", doc.title().text());
 assertNotNull(doc.body().child(0));
 assertEquals("Test Heading", doc.body().child(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Test Heading</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 assertNotNull(doc.body());
 assertEquals("Test Heading", doc.body().child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 String html = null;
 try {
 Parser.parse(html, "http://example.com");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyHtml() {
 String html = "";
 try {
 Parser.parse(html, "http://example.com");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element comment = doc.body().child(0).child(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidStartTag() {
 String html = "<html<tag name>";
 try {
 Parser.parse(html, "http://example.com");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseSelfClosedTag() {
 String html = "<html><img src=\"image.jpg\" /></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element img = doc.body().child(0).child(0);
 assertEquals("image.jpg", img.attrs().get("src"));
 }

 @Test
 public void testParseImplicitParentTag() {
 String html = "<html><body><p>This is a test<img src=\"image.jpg\"></p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element p = doc.body().child(0);
 Element img = p.child(1);
 assertEquals(4, p.childSize()); // p has 4 children: text, img, text, and null ( TextNode created to close p tag)
 }
}