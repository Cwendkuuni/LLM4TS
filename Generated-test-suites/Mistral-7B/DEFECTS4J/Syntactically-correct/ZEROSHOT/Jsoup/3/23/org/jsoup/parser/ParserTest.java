package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest {

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head>" +
         "<body><h1>Hello World</h1></body></html>", "http://test.com");
 Document doc = parser.parse();

 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().firstChild().text());
 assertNotNull(doc.head());
 assertNotNull(doc.title());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<div id=\"content\">Hello World</div>", "http://test.com");
 Document doc = parser.parseBodyFragment();

 assertEquals("Hello World", doc.body().firstChild().text());
 assertFalse(doc.head() instanceof Element);
 }

 @Test
 public void testParseEmptyBodyFragment() {
 Parser parser = new Parser("", "http://test.com");
 Document doc = parser.parseBodyFragment();

 assertTrue(doc.body().isEmpty());
 }

 @Test
 public void testParseWithScriptTag() {
 Parser parser = new Parser("<html><head><title>Test</title>" +
         "<script src=\"script.js\"></script></head>" +
         "<body><h1>Hello World</h1></body></html>", "http://test.com");
 Document doc = parser.parse();

 assertNotNull(doc.getElementsByTag("script").first());
 }

 @Test
 public void testParseWithStyleTag() {
 Parser parser = new Parser("<html><head><title>Test</title>" +
         "<style>body { background-color: red; }</style></head>" +
         "<body><h1>Hello World</h1></body></html>", "http://test.com");
 Document doc = parser.parse();

 assertEquals("red", doc.body().styleProperty("background-color"));
 }

 @Test
 public void testParseWithBaseTag() {
 Parser parser = new Parser("<html><head><title>Test</title>" +
         "<base href=\"/test\"/>" +
         "</head><body><h1>Hello World</h1></body></html>", "http://test.com");
 Document doc = parser.parse();

 assertEquals("/test/", doc.baseUri());
 }

 @Test
 public void testParseWithInvalidBaseTag() {
 Parser parser = new Parser("<html><head><title>Test</title>" +
         "<base target=\"_blank\"/>" +
         "</head><body><h1>Hello World</h1></body></html>", "http://test.com");
 Document doc = parser.parse();

 assertEquals("http://test.com", doc.baseUri());
 }

 @Test
 public void testParseWithCommentTag() {
 Parser parser = new Parser("<html><!-- This is a comment --><head>" +
         "<title>Test</title></head>" +
         "<body><h1>Hello World</h1></body></html>", "http://test.com");
 Document doc = parser.parse();

 assertTrue(doc.body().children().get(0) instanceof Comment);
 }

 @Test
 public void testParseWithCdataTag() {
 Parser parser = new Parser("<html><head>" +
         "<script><![CDATA[function hello() { alert('Hello World'); }]]></script>" +
         "</head><body></body></html>", "http://test.com");
 Document doc = parser.parse();

 Element script = doc.getElementsByTag("script").first();
 assertEquals("function hello() { alert('Hello World'); }", script.data());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 Parser parser = new Parser(null, "http://test.com");
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullBaseUri() {
 Parser parser = new Parser("<html></html>", null);
 }

 @Test
 public void testParseWithEmptyHtml() {
 Parser parser = new Parser("", "http://test.com");
 Document doc = parser.parse();

 assertTrue(doc.isEmpty());
 }
}