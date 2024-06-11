package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.*;

public class ParserTest {

 private static final String HTML_TEST = "<html><head><title>Test</title></head>" +
         "<body><h1>Hello World</h1><p id=\"p1\">This is a <b>test</b></p>" +
         "<script src=\"script.js\"></script>" +
         "<img src=\"image.jpg\" alt=\"Image\"/><base href=\"/test/\">" +
         "</body></html>";

 @Test
 public void testParser() {
     String baseUri = "http://example.com";
     Parser parser = new Parser(HTML_TEST, baseUri, false);
     Document doc = parser.parse();

     assertEquals("html", doc.tagName());
     assertEquals(baseUri, doc.baseUri());

     Element html = doc.body();
     assertEquals("html", html.tagName());
     assertEquals(baseUri, html.baseUri());

     Element head = html.child(0);
     assertEquals("head", head.tagName());
     assertEquals(baseUri, head.baseUri());

     Element title = head.child(0);
     assertEquals("title", title.tagName());
     assertEquals("Test", title.text());

     Element h1 = html.child(1);
     assertEquals("h1", h1.tagName());
     assertEquals("Hello World", h1.text());

     Element p = html.child(2);
     assertEquals("p", p.tagName());
     assertEquals("This is a <b>test</b>", p.text());

     Element img = html.child(5);
     assertEquals("img", img.tagName());
     assertEquals("/test/image.jpg", img.absUrl("src"));

     assertEquals(3, html.childSize());

     Element script = html.child(3);
     assertNull(script.text());

     Element base = html.child(4);
     assertEquals("base", base.tagName());
     assertEquals("/test/", base.attr("href"));
 }

 @Test
 public void testParseBodyFragment() {
     String baseUri = "http://example.com";
     Parser parser = new Parser(HTML_TEST.substring(HTML_TEST.indexOf("<body>")), baseUri, true);
     Document doc = parser.parse();

     assertEquals("html", doc.tagName());
     assertEquals(baseUri, doc.baseUri());

     Element html = doc;
     assertEquals("html", html.tagName());
     assertEquals(baseUri, html.baseUri());

     assertEquals(0, html.childSize());

     Element body = html.child(0);
     assertEquals("body", body.tagName());
     assertEquals(baseUri, body.baseUri());

     assertEquals(1, body.childSize());

     Element p = body.child(0);
     assertEquals("p", p.tagName());
     assertEquals("This is a <b>test</b>", p.text());
 }

 @Test
 public void testParseInvalidHtml() {
     String baseUri = "http://example.com";
     Parser parser = new Parser("", baseUri, false);
     try {
         parser.parse();
         fail();
     } catch (IllegalArgumentException e) {
         // expected
     }
 }
}