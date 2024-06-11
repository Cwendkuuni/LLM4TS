package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.LinkedList;

/**
 * Testing for Parser class
 */
public class ParserTest {

 @Test
 public void testParse() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Parser parser = new Parser(html, "http://test.com");
 Document doc = parser.parse();

 Element root = doc.getRootElement();
 Assert.assertEquals(htmlTag, root.tag());
 Element title = root.getElementsByTag("title").first();
 Assert.assertEquals(titleTag, title.tag());
 Element h1 = root.getElementsByTag("h1").first();
 Assert.assertEquals(h1Tag, h1.tag());
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Parser parser = new Parser(bodyHtml, "http://test.com");
 Document doc = parser.parseBodyFragment(bodyHtml, "http://test.com");

 Element root = doc; // body is the root element for body fragments
 Assert.assertEquals(bodyTag, root.tag());
 Element h1 = root.getElementsByTag("h1").first();
 Assert.assertEquals(h1Tag, h1.tag());
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><div class='my-class'><h1>Hello World</h1></div></body>";
 Parser parser = new Parser(bodyHtml, "http://test.com");
 parser.relaxed = true;
 Document doc = parser.parseBodyFragment(bodyHtml, "http://test.com");

 Element root = doc; // body is the root element for body fragments
 Element div = root.getElementsByTag("div").first();
 Element h1 = div.getElementsByTag("h1").first();
 Assert.assertEquals(h1Tag, h1.tag());
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullHtml() {
 Parser parser = new Parser(null, "http://test.com");
 // Parser should throw an exception if given null HTML
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
 Parser parser = new Parser("<html></html>", null);
 // Parser should throw an exception if given null base uri
 }

 @Test
 public void testParseWithEmptyHtml() {
 Parser parser = new Parser("", "http://test.com");
 Document doc = parser.parse();
 Element root = doc;
 Assert.assertEquals(htmlTag, root.tag());
 Assert.assertTrue(doc.body().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidTagName() {
 Parser parser = new Parser("<html><tag></html>", "http://test.com");
 // Parser should throw an exception if given an invalid tag name
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --></html>";
 Parser parser = new Parser(html, "http://test.com");
 Document doc = parser.parse();
 Comment comment = doc.getElementsByTag("comment").first();
 Assert.assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 String html = "<html xmlns='http://www.w3.org/1999/xhtml'>";
 Parser parser = new Parser(html, "http://test.com");
 Document doc = parser.parse();
 XmlDeclaration xmlDeclaration = doc.getElementsByTag("xml-declaration").first();
 Assert.assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.xmlns());
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><p>Hello World</p></body></html>";
 Parser parser = new Parser(html, "http://test.com");
 Document doc = parser.parse();
 Element root = doc;
 Element body = root.getElementsByTag("body").first();
 Element p = body.getElementsByTag("p").first();
 p.remove();
 parser.parse(); // consume remaining "</body>" and "</html>"
 Assert.assertTrue(root.getElementsByTag("p").isEmpty());
 }

 @Test
 public void testParseTextNode() {
 String html = "<html><body><p>Hello World <there</p></body></html>";
 Parser parser = new Parser(html, "http://test.com");
 Document doc = parser.parse();
 Element root = doc;
 Element p = root.getElementsByTag("p").first();
 TextNode textNode = (TextNode) p.getElementsByTag("textNode").first();
 Assert.assertEquals("Hello World <there", textNode.text());
 }

 @Test
 public void testParseCdata() {
 String html = "<html><body><![CDATA[Hello World <there]]]></body></html>";
 Parser parser = new Parser(html, "http://test.com");
 Document doc = parser.parse();
 Element root = doc;
 Element body = root.getElementsByTag("body").first();
 TextNode textNode = (TextNode) body.getElementsByTag("cdata").first();
 Assert.assertEquals("Hello World <there", textNode.text());
 }

 @Test
 public void testParseScriptTag() {
 String html = "<html><body><script type='text/javascript'>alert('Hello World')</script></body></html>";
 Parser parser = new Parser(html, "http://test.com");
 Document doc = parser.parse();
 Element root = doc;
 Element script = root.getElementsByTag("script").first();
 // Script tag should not be parsed as text node
 Assert.assertNotNull(script.text());
 Assert.assertNull(script.getElementsByTag("textNode").first());
 }

 @Test
 public void testParseBaseTag() {
 String html = "<html><head><base href='/test'></head><body></body></html>";
 Parser parser = new Parser(html, "http://test.com");
 Document doc = parser.parse();
 Element base = doc.head().getElementsByTag("base").first();
 Assert.assertEquals("/test", base.absUrl("href"));
 }

 @Test
 public void testParseUnknownTag() {
 String html = "<html><body><unknown></body></html>";
 Parser parser = new Parser(html, "http://test.com");
 Document doc = parser.parse();
 Element root = doc;
 Element unknown = root.getElementsByTag("unknown").first();
 // Unknown tag should not be parsed
 Assert.assertNull(unknown.tag());
 }
}