package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Map;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://test.com");
 Document doc = parser.parse();

 assertEquals("Test", doc.title());
 assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", doc.html().toString());
 assertEquals("<h1>Hello World</h1>", doc.body().html().toString());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Hello World</p>", "http://test.com");
 Document doc = parser.parseBodyFragment();
 assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.html().toString());
 }

 @Test
 public void testIgnoreComments() {
 Parser parser = new Parser("<html><!-- test --><title>Test</title></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals("Test", doc.title().text());
 }

 @Test
 public void testCdata() {
 Parser parser = new Parser("<html><![CDATA[ <script type=\"text/javascript\"> alert('Hello World'); </script> ]]><title>Test</title></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals("<title>Test</title>", doc.head().html().toString());
 assertEquals("<script type=\"text/javascript\"> alert('Hello World'); </script>", doc.body().select("script").first().html().toString());
 }

 @Test
 public void testXmlDecl() {
 Parser parser = new Parser("<html xmlns=\"http://test.com/test\" xml:lang=\"en\">", "http://test.com");
 Document doc = parser.parse();
 assertEquals("http://test.com/test", doc.xmlNamespaceUri());
 assertEquals("en", doc.xmlLang());
 }

 @Test
 public void testEndTag() {
 Parser parser = new Parser("<html><body><p>Hello World</p><div></p></div></body></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals(2, doc.body().childNodeSize());
 assertEquals("<p>Hello World</p>", doc.body().get(0).html().toString());
 assertEquals("<div></div>", doc.body().get(1).html().toString());
 }

 @Test
 public void testStartTag() {
 Parser parser = new Parser("<html><body><p><input type=\"text\"></p></body></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals(1, doc.body().childNodeSize());
 Element p = doc.body().get(0);
 assertEquals(2, p.childNodeSize());
 assertEquals("input", p.get(1).tagName().toString());
 }

 @Test
 public void testAttributes() {
 Parser parser = new Parser("<html><body><a href=\"http://test.com/test\" id=\"test\" class=\"test\">Test</a></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element a = doc.body().get(0).get(0);
 Map<String, Attribute> attributes = a.attributes();
 assertEquals(3, attributes.size());
 assertEquals("http://test.com/test", a.attr("href"));
 assertEquals("test", a.attr("id"));
 assertEquals("test", a.attr("class"));
 }

 @Test
 public void testDataNode() {
 Parser parser = new Parser("<html><body><script type=\"text/javascript\">alert('Test');</script></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element script = doc.body().get(0);
 assertEquals("alert('Test');", script.html().toString());
 assertEquals("Test", ((TextNode) script.child(0)).text());
 }

 @Test
 public void testBaseTag() {
 Parser parser = new Parser("<html><body><base href=\"/test\"><a href=\"test.html\">Test</a></body></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals("/test", doc.baseUri());
 }

 @Test
 public void testNestedTags() {
 Parser parser = new Parser("<html><body><div><p>Hello World</p></div></body></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals("<div><p>Hello World</p></div>", doc.body().html().toString());
 }

 @Test
 public void testImplicitParents() {
 Parser parser = new Parser("<html><body><img src=\"test.png\"></body></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals("<html><head></head><body><img src=\"test.png\"></body></html>", doc.html().toString());
 }

 @Test
 public void testInvalidTagOrder() {
 Parser parser = new Parser("</html><body><p>Hello World</p></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals("", doc.html().toString());
 }

 @Test
 public void testUnknownChars() {
 Parser parser = new Parser("<html><body><p>Hello World test & @#%^&*()_+</p></body></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals("<html><body><p>Hello World test &amp; &amp;#36; &amp;#35; &amp;#64; &amp;#40; &amp;#41; &amp;#43; &amp;#44; &amp;#45; &amp;#46; &amp;#47;</p></body></html>", doc.html().toString());
 }
}