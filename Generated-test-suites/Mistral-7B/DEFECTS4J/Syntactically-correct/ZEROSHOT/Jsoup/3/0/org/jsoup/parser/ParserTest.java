package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("", "", false); // empty html, null baseUri
 Document doc = parser.parse();
 assertEquals(htmlTag, doc.tagName());
 assertTrue(doc.children().isEmpty());

 // test with valid html
 String validHtml = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 parser = new Parser(validHtml, "", false);
 doc = parser.parse();
 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.numChildren());
 assertEquals("Test Page", doc.title().text());
 Element body = doc.body();
 assertEquals("Hello World", body.children().get(0).text());

 // test with baseUri
 String baseUri = "https://example.com/";
 parser = new Parser("<html><head></head><body><img src='image.png'></body></html>", baseUri, false);
 Document docWithBaseUri = parser.parse();
 Element img = docWithBaseUri.select("img").get(0);
 assertEquals("image.png", img.absUrl("src"));
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Hello World</p>", "", true);
 Document doc = parser.parse();
 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.numChildren());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseWithRelaxedParsing() {
 Parser parser = new Parser("<div><script>window.onload = function() { alert('Hello World'); }</script></div>", "", true);
 Document doc = parser.parse();
 assertEquals(htmlTag, doc.tagName());
 assertEquals(1, doc.numChildren());
 assertTrue(doc.body().children().get(0).tagName().isScriptElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 Parser parser = new Parser(null, "", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 Parser parser = new Parser("", null, false);
 }
}