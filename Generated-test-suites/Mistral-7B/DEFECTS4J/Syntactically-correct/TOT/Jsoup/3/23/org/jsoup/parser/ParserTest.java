package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setup() {
 parser = new Parser("<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>", "http://example.com");
 doc = parser.parse();
 }

 @Test
 public void testParseWithValidHtml() {
 assertNotNull(parser);
 assertNotNull(doc);
 assertEquals("Test", doc.title());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseWithEmptyHtml() {
 parser = new Parser("<>", "http://example.com");
 doc = parser.parse();
 assertEquals("", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 parser = new Parser(null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 parser = new Parser("<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>", null);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div><p>Fragment 1</p></div>";
 Document fragmentDoc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals("Fragment 1", fragmentDoc.body().text());
 }

 @Test
 public void testParseBodyFragmentWithEmptyHtml() {
 String bodyHtml = "<>";
 Document fragmentDoc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals("", fragmentDoc.body().text());
 }

 @Test
 public void testParseBodyFragmentWithInvalidHtml() {
 String bodyHtml = "<html><head><title>Test</title></head>";
 Document fragmentDoc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertNotNull(fragmentDoc);
 assertEquals("", fragmentDoc.body().text());
 }

 @Test
 public void testParseWithInvalidHtml() {
 String invalidHtml = "<html><head<title>Test</title></head><body><div<p>Hello World<p></div></body></html>"; // missing '>' in tag names
 parser = new Parser(invalidHtml, "http://example.com");
 doc = parser.parse();
 assertEquals("Test", doc.title().text()); // title tag missing '>' should default to empty string
 assertEquals("", doc.body().text());
 }

 @Test
 public void testParseWithXmlDeclaration() {
 String xmlDeclarationHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
 parser = new Parser(xmlDeclarationHtml, "http://example.com");
 doc = parser.parse();
 Element xmlDecl = doc.getElementsByTag("xml-declaration").first();
 assertNotNull(xmlDecl);
 assertEquals("UTF-8", xmlDecl.attr("encoding"));
 }

 @Test
 public void testParseWithComment() {
 String commentHtml = "<!-- This is a comment --><html><head><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
 parser = new Parser(commentHtml, "http://example.com");
 doc = parser.parse();
 Element comment = doc.body().child(0);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseWithScriptTag() {
 String scriptHtml = "<html><head><title>Test</title><script src=\"script.js\"></script></head><body><div><p>Hello World</p></div></body></html>";
 parser = new Parser(scriptHtml, "http://example.com");
 doc = parser.parse();
 Element script = doc.getElementsByTag("script").first();
 assertNotNull(script);
 assertEquals("script.js", script.absUrl("src"));
 }

 @Test
 public void testParseWithBaseTag() {
 String baseTagHtml = "<html><head><base href=\"/new-directory/\"><title>Test</title></head><body><div><p>Hello World</p></div></body></html>";
 parser = new Parser(baseTagHtml, "http://example.com");
 doc = parser.parse();
 assertEquals("/new-directory/", doc.baseUri());
 }

 @Test
 public void testParseWithMixedAttributes() {
 String mixedAttributesHtml = "<html><head><title class=\"test\" id=\"title-id\">Test</title></head><body><div><p id=\"p-id\" class=\"p test\" href=\"/new-url/\">Hello World</p></div></body></html>";
 parser = new Parser(mixedAttributesHtml, "http://example.com");
 doc = parser.parse();
 Element title = doc.title();
 assertEquals("Test", title.text());
 assertEquals("test", title.classNames().get(0));
 assertEquals("title-id", title.id());
 Element p = doc.body().child(0);
 assertEquals("Hello World", p.text());
 assertEquals("p test", p.classNames().get(0));
 assertEquals("/new-url/", p.attr("href"));
 assertEquals("p-id", p.id());
 }

 @Test
 public void testParseWithCdata() {
 String cdataHtml = "<html><head><title>Test</title></head><body><![CDATA[This is some CDATA]]><div><p>Hello World</p></div></body></html>";
 parser = new Parser(cdataHtml, "http://example.com");
 doc = parser.parse();
 Element cdata = doc.body().child(0);
 assertEquals("This is some CDATA", cdata.text());
 }

 @Test
 public void testParseWithImplicitParents() {
 String implicitParentsHtml = "<html><head><title>Test</title></head><body><img src=\"image.jpg\"></body></html>";
 parser = new Parser(implicitParentsHtml, "http://example.com");
 doc = parser.parse();
 Element img = doc.body().child(0);
 Element imgParent = img.parent();
 assertEquals("body", imgParent.tagName());
 }
}