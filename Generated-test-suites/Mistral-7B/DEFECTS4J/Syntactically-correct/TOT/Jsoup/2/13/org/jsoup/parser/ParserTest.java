import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test for Parser class
 */
public class ParserTest {

 @Test
 public void testParse() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";

 // Test parsing a full HTML document
 Document doc = Parser.parse(html, baseUri);

 // Check root element
 Element root = doc.getRootElement();
 Assert.assertEquals("html", root.tagName());

 // Check title element
 Element title = root.getElementsByTag("title").get(0);
 Assert.assertEquals("Test", title.text());

 // Check text node
 Element h1 = root.getElementsByTag("h1").get(0);
 TextNode textNode = (TextNode) h1.getElementsByTag("textnode").get(0);
 Assert.assertEquals("Hello World", textNode.text());

 // Check comment
 Element comment = root.getElementsByTag("comment").get(0);
 Assert.assertEquals("", comment.text()); // Empty comment should have empty text
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 String baseUri = "http://example.com";

 // Test parsing a body fragment
 Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

 // Check root element
 Element root = doc; // body fragment has no head, so root is body
 Assert.assertEquals("body", root.tagName());

 // Check title element (should not be present)
 Assert.assertNull(root.getElementsByTag("title").get(0));

 // Check text node
 Element h1 = root.getElementsByTag("h1").get(0);
 TextNode textNode = (TextNode) h1.getElementsByTag("textnode").get(0);
 Assert.assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseSpecialChars() {
 String html = "<html><head><title>&amp;quot; Test &amp;lsquo; &amp;rsquo;</title></head><body>&lt;p&gt;&lt;a href=&quot;http://example.com&quot;&gt;Link&lt;/a&gt;</p></body></html>";
 String baseUri = "http://example.com";

 // Test parsing HTML with special characters
 Document doc = Parser.parse(html, baseUri);

 // Check title element
 Element title = doc.getRootElement().getElementsByTag("title").get(0);
 Assert.assertEquals("&quot; Test &lsquo; &rsquo;", title.text());

 // Check link element
 Element link = doc.getRootElement().getElementsByTag("a").get(0);
 Assert.assertEquals("http://example.com", link.absUrl("href"));
 }

 @Test
 public void testParseErrors() {
 String html = "<html><head><title>Test</title></head><body><script>document.write('Hello World')</script></body></html>";
 String baseUri = "http://example.com";

 // Test parsing HTML with script tag
 Document doc = Parser.parse(html, baseUri);

 // Check root element
 Element root = doc.getRootElement();
 Assert.assertEquals("html", root.tagName());

 // Check script tag (should be skipped)
 Element script = root.getElementsByTag("script").get(0);
 Assert.assertEquals(0, script.childElementCount());
 }

 @Test
 public void testParseInvalidHtml() { // Intentional missing '>' in 'html' tag
 String html = "<html<head><title>Test</title></head><body><h1>Hello World</h1></body>";
 String baseUri = "http://example.com";

 // Test parsing invalid HTML
 try {
 Document doc = Parser.parse(html, baseUri);
 Assert.fail(); // Parse should fail
 } catch (IllegalArgumentException e) {
 // Expect IllegalArgumentException
 }
 }

 @Test
 public void testParseEmptyHtml() { // Empty HTML document
 String html = "";
 String baseUri = "http://example.com";

 // Test parsing empty HTML document
 Document doc = Parser.parse(html, baseUri);

 // Check root element
 Element root = doc; // Empty document has no root element
 Assert.assertNull(root);
 }
}