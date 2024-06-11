package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseValidHtml() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element htmlNode = doc.head();
 assertEquals("html", htmlNode.tagName());
 assertEquals("http://example.com", htmlNode.baseUri());

 Element titleNode = doc.head().title();
 assertEquals("Test Page", titleNode.text());

 Element h1Node = doc.body().getElementsByTag("h1").first();
 assertEquals("Hello World", h1Node.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Fragment</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");

 Element h1Node = doc.body().getElementsByTag("h1").first();
 assertEquals("Fragment", h1Node.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><div class=\"myclass\"></div>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

 Element divNode = doc.body().getElementsByClass("myclass").first();
 assertNotNull(divNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 String html = null;
 try {
 Parser.parse(html, "http://example.com");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 String html = "<html><body></body></html>";
 try {
 Parser.parse(html, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --></html>";
 Document doc = Parser.parse(html, "http://example.com");

 Element commentNode = doc.body().getElementsByTag("comment").first();
 assertEquals("This is a comment", commentNode.text());
 }

 @Test
 public void testParseXmlDeclaration() {
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body></body></html>";
 Document doc = Parser.parse(xml, "http://example.com");

 XmlDeclaration xmlDecl = doc.head().getElementsByTag("xml-declaration").first();
 assertEquals("1.0", xmlDecl.version());
 assertEquals("UTF-8", xmlDecl.encoding());
 }
}