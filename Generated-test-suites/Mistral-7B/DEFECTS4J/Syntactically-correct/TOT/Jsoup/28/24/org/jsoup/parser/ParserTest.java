package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInputWithValidHtml() {
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 doc = parser.parseInput(html, null);

 assertEquals("Test", doc.title().text());
 assertNotNull(doc.body());
 assertNotNull(doc.body().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseInputWithValidHtmlAndBaseUri() {
 String html = "<img src=\"image.png\" alt=\"Image\">";
 String baseUri = "http://example.com/";
 doc = parser.parseInput(html, baseUri);
 assertNotNull(doc.head());
 Element img = doc.selectFirst("img");
 assertEquals("Image", img.attr("alt"));
 assertNotNull(img.attr("src"));
 assertEquals("image.png", img.attr("src"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithEmptyHtml() {
 parser.parseInput("", null);
 }

 @Test(expected = ParseError.class)
 public void testParseInputWithInvalidHtml() {
 String html = "<html<title>Test</title>";
 doc = parser.parseInput(html, null);
 assertNotNull(doc.errors());
 List<ParseError> errors = doc.errors();
 assertEquals(1, errors.size());
 assertEquals("Error parsing start tag : html", errors.get(0).getMessage());
 }

 @Test
 public void testParseInputWithValidHtmlAndTrackErrors() {
 maxErrors = 1;
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 doc = parser.parseInput(html, null);
 List<ParseError> errors = doc.errors();
 assertEquals(0, errors.size());
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 maxErrors = 1;
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p><p>Error1</p><p>Error2</p></body></html>";
 doc = parser.parseInput(html, null);
 List<ParseError> errors = doc.errors();
 assertEquals(2, errors.size());
 }

 @Test
 public void testParseInputWithValidHtmlAndUpdateTreeBuilder() {
 TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
 Parser parserWithNewTreeBuilder = new Parser(newTreeBuilder);
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 doc = parserWithNewTreeBuilder.parseInput(html, null);
 assertEquals(newTreeBuilder, parserWithNewTreeBuilder.getTreeBuilder());
 }

 @Test
 public void testParseInputWithValidHtmlAndDisableErrorTracking() {
 maxErrors = 0;
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 doc = parser.parseInput(html, null);
 assertNull(doc.errors());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Test</p>";
 doc = Parser.parseBodyFragment(bodyHtml, null);
 assertNotNull(doc.body());
 assertEquals("Test", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBodyFragmentWithNullBodyHtml() {
 Parser.parseBodyFragment(null, null);
 }

 @Test(expected = IllegalArgumentException.class) public void testParseBodyFragmentWithEmptyBodyHtml() { Parser.parseBodyFragment("", null); }

 @Test
 public void testParseBodyFragmentWithBaseUri() {
 String bodyHtml = "<img src=\"image.png\" alt=\"Image\">";
 String baseUri = "http://example.com/";
 doc = Parser.parseBodyFragment(bodyHtml, baseUri);
 assertNotNull(doc.head());
 Element img = doc.selectFirst("img");
 assertEquals("Image", img.attr("alt"));
 assertNotNull(img.attr("src"));
 assertEquals("image.png", img.attr("src"));
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<p>Test</p>";
 Element context = Jsoup.parse("<div></div>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 assertEquals(1, nodes.size());
 assertNotNull(nodes.get(0));
 assertEquals("p", nodes.get(0).nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullFragmentHtml() {
 Parser.parseFragment(null, null, null);
 }

 @Test(expected = IllegalArgumentException.class) public void testParseFragmentWithEmptyFragmentHtml() { Parser.parseFragment("", null, null); }

 @Test
 public void testParseFragmentWithContext() {
 String fragmentHtml = "<p>Test</p>";
 Element context = Jsoup.parse("<div></div>").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
 assertEquals(1, nodes.size());
 assertNotNull(nodes.get(0));
 assertEquals("p", nodes.get(0).nodeName());
 context.appendChild(nodes.get(0));
 assertEquals(1, context.childNodes().size());
 }
}