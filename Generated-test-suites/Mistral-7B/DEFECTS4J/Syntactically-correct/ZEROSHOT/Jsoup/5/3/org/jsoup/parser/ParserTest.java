package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.*;

import static org.jsoup.parser.Parser.parse;
import static org.jsoup.parser.Parser.parseBodyFragment;
import static org.jsoup.parser.Parser.parseBodyFragmentRelaxed;
import static org.jsoup.parser.Parser.htmlTag;
import static org.jsoup.parser.Parser.headTag;
import static org.jsoup.parser.Parser.bodyTag;
import static org.jsoup.parser.Parser.titleTag;
import static org.jsoup.parser.Parser.textareaTag;

public class ParserTest {

 @Test
 public void testParse() {
 Document doc = parse("<html><head><title>Test</title></head><body><p>Hello</p></body></html>", "http://test.com");
 Element html = doc.body();
 assertTrue(html.tagName().equals(bodyTag.toString()));
 Element title = html.head();
 assertEquals("Test", title.text());
 Element p = html.getElementsByTag("p").first();
 assertEquals("Hello", p.text());
 }

 @Test
 public void testParseBodyFragment() {
 Document doc = parseBodyFragment("<p>Hello</p>", "http://test.com");
 Element p = doc.body().getElementsByTag("p").first();
 assertEquals("Hello", p.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = parseBodyFragmentRelaxed("<p>Hello</p>", "http://test.com");
 Element p = doc.body().getElementsByTag("p").first();
 assertEquals("Hello", p.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 try {
 Parser.parse(null, "http://test.com");
 } catch (IllegalArgumentException e) {
 return;
 }
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 try {
 Parser.parse("<html></html>", null);
 } catch (IllegalArgumentException e) {
 return;
 }
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseEmptyString() {
 Document doc = parse("", "http://test.com");
 assertEquals("", doc.body().text());
 }

 @Test
 public void testParseWhitespace() {
 Document doc = parse("<html><head></head><body>&nbsp;</body></html>", "http://test.com");
 assertEquals("", doc.body().text());
 }

 @Test
 public void testParseWithComments() {
 Document doc = parse("<html><!-- This is a comment --><body><p>Hello</p></body></html>", "http://test.com");
 Element p = doc.body().getElementsByTag("p").first();
 assertEquals("Hello", p.text());
 }

 @Test
 public void testParseCdata() {
 Document doc = parse("<html><![CDATA[This is some data]]><body></body></html>", "http://test.com");
 Element body = doc.body();
 assertEquals("", body.text());
 Element cdata = body.getElementsByTag("cdata").first();
 assertEquals("This is some data", cdata.text());
 }

 @Test
 public void testParseXmlDecl() {
 Document doc = parse("<html xmlns:foo=\"http://foo.com\" xml:lang=\"en\"><body></body></html>", "http://test.com");
 assertEquals("http://foo.com", doc.getElementsByAttribute("xmlns:foo").first().attr("foo"));
 assertEquals("en", doc.getElementsByTag("html").attr("xml:lang"));
 }

 @Test
 public void testParseEndTag() {
 Document doc = parse("<html><body><p>Hello</p></p></body></html>", "http://test.com");
 assertEquals(0, doc.body().getElementsByTag("p").size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseUnknownTag() {
 try {
 Document doc = parse("<html><body><<foo></foo></body></html>", "http://test.com");
 } catch (IllegalArgumentException e) {
 return;
 }
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseEmptyTag() {
 Document doc = parse("<html><body><img /></body></html>", "http://test.com");
 Element img = doc.body().getElementsByTag("img").first();
 assertTrue(img.tagName().toString().equals("img"));
 assertEquals(0, img.children().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTagWithMissingEndTag() {
 try {
 Document doc = parse("<html><body><p>Hello</p>");
 } catch (IllegalArgumentException e) {
 return;
 }
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseTagWithInvalidEndTag() {
 try {
 Document doc = parse("<html><body><p>Hello</p></div>", "http://test.com");
 } catch (IllegalArgumentException e) {
 return;
 }
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseSelfClosingTag() {
 Document doc = parse("<html><body><img src=\"test.jpg\" alt=\"Test\" /></body></html>", "http://test.com");
 Element img = doc.body().getElementsByTag("img").first();
 String src = img.attr("src");
 assertEquals("test.jpg", src);
 assertEquals(0, img.children().size());
 }

 @Test
 public void testParseDataTag() {
 Document doc = parse("<html><body><script type=\"text/javascript\">var x = 5;</script></body></html>", "http://test.com");
 Element script = doc.body().getElementsByTag("script").first();
 assertEquals("var x = 5;\n", script.data().toString());
 }

 @Test
 public void testParseScriptTagWithTextNode() {
 Document doc = parse("<html><body><script type=\"text/javascript\">var x = 5;<p>Hello</p></script></body></html>", "http://test.com");
 Element script = doc.body().getElementsByTag("script").first();
 assertEquals("var x = 5;\n", script.text());
 Element p = script.nextSibling();
 assertEquals("Hello", p.text());
 }

 @Test
 public void testParseBaseTag() {
 Document doc = parse("<html><head><base href=\"/test/\"><title>Test</title></head><body><p>Hello</p></body></html>", "http://test.com");
 assertEquals("/test/", doc.baseUri());
 Element p = doc.body().getElementsByTag("p").first();
 assertEquals("Hello", p.text());
 }

 @Test
 public void testParseBodyFragmentWithImplicitParent() {
 Document doc = parseBodyFragment("<p>Hello</p>", "http://test.com");
 Element p = doc.body().getElementsByTag("p").first();
 assertEquals("Hello", p.text());
 Element implicitHead = p.getElementsByTag("head").first();
 Element implicitHtml = implicitHead.getParent();
 assertEquals("html", implicitHtml.tagName().toString());
 }

 @Test
 public void testParseBodyFragmentWithExplicitParent() {
 Document doc = parseBodyFragment("<html><body><p>Hello</p></body></html>", "http://test.com");
 Element p = doc.body().getElementsByTag("p").first();
 assertEquals("Hello", p.text());
 Element div = new Element(htmlTag, "http://test.com");
 div.appendChild(p);
 Element parent = parseBodyFragment("<html><body><div></div></body></html>", "http://test.com");
 parent.body().appendChild(div);
 }

 @Test
 public void testParseEmptyBodyFragment() {
 Document doc = parseBodyFragment("", "http://test.com");
 assertEquals(0, doc.body().children().size());
 }

 @Test
 public void testParseBodyFragmentWithInvalidTag() {
 Document doc = parseBodyFragment("<html></html>", "http://test.com");
 try {
 Element invalid = doc.body().getElementsByTag(" invalidTag ").first();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testParseBodyFragmentWithImplicitParentAndInvalidTag() {
 Document doc = parseBodyFragment("<p>Hello</p><invalidTag></invalidTag>", "http://test.com");
 try {
 Element invalid = doc.body().getElementsByTag("invalidTag").first();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testParseBodyFragmentWithRelaxedParsing() {
 Document doc = parseBodyFragmentRelaxed("<html><body><p>Hello</p><div><invalidTag></invalidTag></div></body></html>", "http://test.com");
 Element p = doc.body().getElementsByTag("p").first();
 assertEquals("Hello", p.text());
 Element div = doc.body().getElementsByTag("div").first();
 Element invalid = div.getElementsByTag("invalidTag").first();
 assertNotNull(invalid);
 }

 @Test
 public void testParseBodyFragmentWithRelaxedParsingAndMissingEndTag() {
 Document doc = parseBodyFragmentRelaxed("<html><body><p>Hello</p><div><script", "http://test.com");
 try {
 Element script = doc.body().getElementsByTag("script").first();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }
}