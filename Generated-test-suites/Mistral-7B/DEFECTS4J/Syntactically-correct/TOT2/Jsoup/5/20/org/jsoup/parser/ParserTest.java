import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 // Test case for parse method
 @Test
 public void testParse() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Parser parser = new Parser(html, "http://example.com");
 Document doc = parser.parse();

 Element htmlNode = doc.head();
 Assert.assertEquals(htmlTag, htmlNode.tag());
 Element headNode = htmlNode.child(0);
 Assert.assertEquals(headTag, headNode.tag());
 Element titleNode = headNode.child(0);
 Assert.assertEquals(titleTag, titleNode.tag());
 Assert.assertEquals("Test", titleNode.text());

 Element bodyNode = doc.body();
 Element h1Node = bodyNode.child(0);
 Assert.assertEquals(bodyTag, bodyNode.tag());
 Assert.assertEquals(h1Tag, h1Node.tag());
 Assert.assertEquals("Hello World", h1Node.text());
 }

 // Test case for parseBodyFragment method
 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Hello World</h1>";
 Parser parser = new Parser(bodyHtml, "http://example.com");
 Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");

 Element bodyNode = doc.body();
 Element h1Node = bodyNode.child(0);
 Assert.assertEquals(bodyTag, bodyNode.tag());
 Assert.assertEquals(h1Tag, h1Node.tag());
 Assert.assertEquals("Hello World", h1Node.text());
 }

 // Test case for parseBodyFragmentRelaxed method
 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<h1>Hello World</h1>";
 Parser parser = new Parser(bodyHtml, "http://example.com");
 parser.relaxed = true;
 Document doc = parser.parseBodyFragment(bodyHtml, "http://example.com");

 Element bodyNode = doc.body();
 Element h1Node = bodyNode.child(0);
 Assert.assertEquals(bodyTag, bodyNode.tag());
 Assert.assertEquals(h1Tag, h1Node.tag());
 Assert.assertEquals("Hello World", h1Node.text());
 }

 // Test case for parse method with invalid HTML
 @Test(expected = RuntimeException.class)
 public void testParseInvalidHtml() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World<script>alert('Hello')</script></body></html>";
 Parser parser = new Parser(html, "http://example.com");
 parser.parse();
 }

 // Test case for parse method with missing end tag
 @Test(expected = RuntimeException.class)
 public void testParseMissingEndTag() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World<script";
 Parser parser = new Parser(html, "http://example.com");
 parser.parse();
 }

 // Test case for parse method with self closing tag
 @Test
 public void testParseSelfClosingTag() {
 String html = "<html><head><title>Test</title></head><body><img src=\"image.jpg\" /></body></html>";
 Parser parser = new Parser(html, "http://example.com");
 Document doc = parser.parse();

 Element imgNode = doc.body().child(0);
 Assert.assertEquals(imgTag, imgNode.tag());
 Assert.assertTrue(imgNode.isSelfClosing());
 }

 // Test case for parse method with base tag
 @Test
 public void testParseBaseTag() {
 String html = "<html><head><base href=\"/test/\"><title>Test</title></head><body><link rel=\"stylesheet\" href=\"styles.css\"></body></html>";
 Parser parser = new Parser(html, "http://example.com");
 Document doc = parser.parse();

 Element baseNode = doc.head().child(0);
 Assert.assertEquals(baseTag, baseNode.tag());
 Assert.assertEquals("/test/", baseNode.absUrl(".").substring(0, 10));
 }
}