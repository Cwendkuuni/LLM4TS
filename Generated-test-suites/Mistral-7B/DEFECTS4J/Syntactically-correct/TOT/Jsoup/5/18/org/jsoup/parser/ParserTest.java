package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

public class ParserTest {

 @Test
 public void testParseEmptyHtml() {
 Parser parser = new Parser("<html></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc.body();
 Assert.assertEquals("html", html.tagName());
 Assert.assertNull(html.childNodes());
 }

 @Test
 public void testParseSimpleHtml() {
 Parser parser = new Parser("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc;
 Element head = html.head();
 Element title = head.child(0);
 Element body = html.body();
 Element h1 = body.child(0);

 Assert.assertEquals("html", html.tagName());
 Assert.assertEquals("head", head.tagName());
 Assert.assertEquals("title", title.tagName());
 Assert.assertNotNull(title.text());
 Assert.assertEquals("Test Title", title.text());
 Assert.assertEquals("body", body.tagName());
 Assert.assertEquals("h1", h1.tagName());
 Assert.assertNotNull(h1.text());
 Assert.assertEquals("Test Heading", h1.text());
 }

 @Test
 public void testParseScriptTag() {
 Parser parser = new Parser("<html><head><script type=\"text/javascript\" src=\"script.js\"></script></head><body></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc;
 Element head = html.head();
 Element script = head.child(0);

 Assert.assertEquals("html", html.tagName());
 Assert.assertEquals("head", head.tagName());
 Assert.assertEquals("script", script.tagName());
 Assert.assertNotNull(script.attr("src"));
 Assert.assertEquals("script.js", script.attr("src"));
 }

 @Test
 public void testParseDataTag() {
 Parser parser = new Parser("<html><body><textarea name=\"test\">Test</textarea></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc;
 Element body = html.body();
 Element textarea = body.child(0);
 TextNode textNode = (TextNode) textarea.child(0);

 Assert.assertEquals("html", html.tagName());
 Assert.assertEquals("body", body.tagName());
 Assert.assertEquals("textarea", textarea.tagName());
 Assert.assertEquals("Test", textNode.text());
 }

 @Test
 public void testParseCommentTag() {
 Parser parser = new Parser("<html><!-- Test Comment --></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc;
 Comment comment = (Comment) html.child(0);

 Assert.assertEquals("html", html.tagName());
 Assert.assertEquals("-->", comment.getData());
 }

 @Test
 public void testParseEmptyScriptTag() {
 Parser parser = new Parser("<html><head><script type=\"text/javascript\"></script></head><body></body></html>", "http://example.com");
 Document doc = parser.parse();
 Element html = doc;
 Element head = html.head();
 Element script = head.child(0);

 Assert.assertEquals("html", html.tagName());
 Assert.assertEquals("head", head.tagName());
 Assert.assertEquals("script", script.tagName());
 Assert.assertTrue(script.childNodes().isEmpty());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<html><body><p>Test</p></body></html>", "http://example.com");
 Document doc = Parser.parseBodyFragment(parser.getHtml(), parser.getBaseUri());
 Element html = doc;
 Element body = html.body();
 Element p = body.child(0);

 Assert.assertEquals("html", html.tagName());
 Assert.assertEquals("body", body.tagName());
 Assert.assertNotNull(p);
 Assert.assertEquals("p", p.tagName());
 Assert.assertNotNull(p.text());
 Assert.assertEquals("Test", p.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<html><div><p>Test</p></div></html>", "http://example.com");
 Document doc = Parser.parseBodyFragmentRelaxed(parser.getHtml(), parser.getBaseUri());
 Element html = doc;
 Element div = html.body();
 Element p = div.child(0);

 Assert.assertEquals("html", html.tagName());
 Assert.assertEquals("body", div.tagName());
 Assert.assertNotNull(p);
 Assert.assertEquals("p", p.tagName());
 Assert.assertNotNull(p.text());Assert.assertEquals("Test", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 Parser parser = new Parser("<html><body><script>Test</script></body><body></body>", "http://example.com");
 try {
 parser.parse();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}