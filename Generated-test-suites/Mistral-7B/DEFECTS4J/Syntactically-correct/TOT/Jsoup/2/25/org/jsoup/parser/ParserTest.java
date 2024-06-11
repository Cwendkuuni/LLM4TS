import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ParserTest {

 @Test
 public void testParse() {
 String html = "<html><head><title>Test Page</title></head>" +
 "<body><h1>Hello World</h1><p>This is a test.</p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element htmlNode = doc.head();
 Assert.assertEquals(Tag.HTML, htmlNode.tag());

 Element titleNode = doc.head().title();
 Assert.assertEquals("Test Page", titleNode.text());

 Element bodyNode = doc.body();
 Element h1Node = bodyNode.getElementsByTag("h1").first();
 Assert.assertEquals("Hello World", h1Node.text());

 Element pNode = bodyNode.getElementsByTag("p").first();
 Assert.assertEquals("This is a test.", pNode.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Fragment Heading</h1><p>Fragment Text.</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 Element rootNode = doc;
 Element h1Node = rootNode.getElementsByTag("h1").first();
 Assert.assertEquals("Fragment Heading", h1Node.text());

 Element pNode = rootNode.getElementsByTag("p").first();
 Assert.assertEquals("Fragment Text.", pNode.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 String html = null;
 Parser parser = new Parser(html, "http://example.com", false);
 parser.parse(); // this will throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyHtml() {
 String html = "";
 Parser parser = new Parser(html, "http://example.com", false);
 parser.parse(); // this will throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidStartTag() {
 String html = "<h1<p>Hello World</h1>";
 Parser parser = new Parser(html, "http://example.com", false);
 parser.parse(); // this will throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingEndTag() {
 String html = "<html><head><title>Test Page</title>";
 Parser parser = new Parser(html, "http://example.com", false);
 parser.parse(); // this will throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEndTagOrder() {
 String html = "<html><head><title>Test Page</title></body></html>";
 Parser parser = new Parser(html, "http://example.com", false);
 parser.parse(); // this will throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXmlDeclaration() {
 String html = "<?xml<?xml version='1.0' encoding='UTF-8'?>";
 Parser parser = new Parser(html, "http://example.com", false);
 parser.parse(); // this will throw an exception
 }

 @Test
 public void testParseInvalidAttributeValue() {
 String html = "<input type='text' name='<script>alert(\"Hello\")' value=''>";
 Document doc = Parser.parse(html, "http://example.com");
 Element inputNode = doc.getElementsByTag("input").first();
 String value = inputNode.attr("value");
 Assert.assertEquals("", value);
 Assert.assertFalse(inputNode.text().contains("Hello"));
 }

 @Test
 public void testParseBaseHref() {
 String html = "<html><head><base href='/subdir'><title>Test Page</title></head><body><a href='page.html'>Link</a></body></html>";
 Document doc = Parser.parse(html, "http://example.com/");
 String baseUri = doc.baseUri();
 String linkHref = doc.getElementsByTag("a").first().absUrl("href");
 Assert.assertEquals("/subdir/page.html", linkHref);
 }
}