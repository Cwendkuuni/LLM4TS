package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.*;

public class ParserTest {

 @Test
 public void testParseEmptyHtml() {
 Parser parser = new Parser("", "baseUri", false);
 Document doc = parser.parse();
 assertEquals("", doc.outerHtml());
 }

 @Test
 public void testParseSimpleHtml() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "baseUri", false);
 Document doc = parser.parse();
 Element title = doc.title();
 assertEquals("Test", title.text());
 Element body = doc.body();
 Element h1 = body.child(0);
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseHtmlWithComments() {
 Parser parser = new Parser("<html><!-- comment1 --><head><title>Test</title></head><!-- comment2 --><body><h1>Hello World</h1><!-- comment3 --></body></html>", "baseUri", false);
 Document doc = parser.parse();
 Element title = doc.title();
 assertEquals("Test", title.text());
 Element body = doc.body();
 Element h1 = body.child(0);
 assertEquals("Hello World", h1.text());
 assertEquals("comment1", doc.body().child(1).nodeName());
 assertEquals("comment2", doc.head().child(1).nodeName());
 }

 @Test
 public void testParseHtmlWithScripts() {
 Parser parser = new Parser("<html><head><title>Test</title>\n<script src=\"script.js\"></script></head>\n<body>\n<script type=\"text/javascript\">function test(){\n alert('Hello World');\n}\n</script>\n<h1>Hello World</h1>\n</body></html>", "baseUri", false);
 Document doc = parser.parse();
 Element title = doc.title();
 assertEquals("Test", title.text());
 Element body = doc.body();
 Element h1 = body.child(0);
 assertEquals("Hello World", h1.text());
 assertTrue(doc.getElementsByTag("script").size() >= 2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 Parser parser = new Parser("<html><head><title>Test</title><body></html>", "baseUri", false);
 parser.parse(); // Exception should be thrown here
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseHtmlWithMissingEndTag() {
 Parser parser = new Parser("<html><head><title>Test</title><body><h1>Hello World", "baseUri", false);
 parser.parse(); // Exception should be thrown here
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><h1>Hello World</h1></body>", "baseUri", true);
 Document doc = parser.parse();
 assertEquals("", doc.head().outerHtml());
 Element body = doc.body();
 Element h1 = body.child(0);
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<body><h1>Hello World</h1><script type=\"text/javascript\">function test(){\n alert('Hello World');\n}\n</script>", "baseUri", true);
 Document doc = parser.parse();
 assertEquals("", doc.head().outerHtml());
 Element body = doc.body();
 Element h1 = body.child(0);
 assertEquals("Hello World", h1.text());
 assertTrue(doc.getElementsByTag("script").size() > 0);
 }
}