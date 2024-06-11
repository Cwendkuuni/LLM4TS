package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

 @Test
 public void testParseEmptyHtml() {
 Document doc = Parser.parse("<html></html>", "http://example.com");
 Element body = doc.body();
 Assert.assertEquals(Tag.HTML, body.tag());
 Assert.assertTrue(body.children().isEmpty());
 }

 @Test
 public void testParseSimpleHtml() {
 Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
 Element body = doc.body();
 Element h1 = body.child(0);
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseHtmlWithScriptAndStyle() {
 Document doc = Parser.parse("<html><head><script src=\"script.js\"></script><style></style></head><body><h1>Hello World</h1></body></html>", "http://example.com");
 Element head = doc.head();
 Element script = head.child(0);
 Element style = head.child(1);
 Assert.assertEquals("script.js", script.attr("src"));
 Assert.assertTrue(style.children().isEmpty());
 }

 @Test
 public void testParseHtmlWithBaseTag() {
 Document doc = Parser.parse("<html><base href=\"/subdir\"><body><h1>Hello World</h1></body></html>", "http://example.com");
 Assert.assertEquals("/subdir/", doc.baseUri());
 }

 @Test
 public void testParseHtmlWithTitleTag() {
 Document doc = Parser.parse("<html><head><title>Hello World</title></head><body></body></html>", "http://example.com");
 Assert.assertEquals("Hello World", doc.title().text());
 }

 @Test
 public void testParseHtmlWithTextareaTag() {
 Document doc = Parser.parse("<html><body><textarea>Hello World</textarea></body></html>", "http://example.com");
 Element textarea = doc.body().child(0);
 Assert.assertEquals("Hello World", textarea.childNodes().text());
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parse("<div>Hello World</div>", "http://example.com");
 Element body = doc;
 body.setTag(bodyTag);
 doc = Parser.parseBodyFragment("<body>" + body.html(), "http://example.com");
 Assert.assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = Parser.parse("<div>Hello World</div>", "http://example.com");
 Element body = doc;
 body.setTag(bodyTag);
 doc = Parser.parseBodyFragmentRelaxed("<body>" + body.html(), "http://example.com");
 Assert.assertEquals("Hello World", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 Parser.parse("<html><body><h1>Hello<body<world</h1></body></html>", "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidBodyFragment() {
 Parser.parseBodyFragment("<html><body><h1>Hello<body<world</h1></body></html>", "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidBodyFragmentRelaxed() {
 Parser.parseBodyFragmentRelaxed("<html><body><h1>Hello<body<world</h1></body></html>", "http://example.com");
 }

}