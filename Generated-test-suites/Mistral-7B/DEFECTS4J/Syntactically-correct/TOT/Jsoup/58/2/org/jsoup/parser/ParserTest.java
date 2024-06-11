package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

 private Parser htmlParser;
 private Parser xmlParser;

 @Before
 public void setup() {
 htmlParser = Parser.htmlParser();
 xmlParser = Parser.xmlParser();
 }

 @Test
 public void testParseHtmlDocument() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = htmlParser.parseInput(html, null);
 assertNotNull(doc);
 assertEquals("Hello World", doc.head().title().text());
 }

 @Test
 public void testParseHtmlDocumentWithBaseUri() {
 String baseUri = "http://example.com";
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 Document doc = htmlParser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals("image.jpg", doc.select("img[src]").first().attr("src"));
 }

 @Test
 public void testParseHtmlFragment() {
 String html = "<h1>Hello World</h1>";
 Element context = Jsoup.parse("<div></div>").body();
 List<Node> nodes = Parser.parseFragment(html, context, null);
 Element h1 = (Element) nodes.get(0);
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseXmlDocument() {
 String xml = "<book><title>XML Test</title></book>";
 Document doc = xmlParser.parseInput(xml, null);
 assertNotNull(doc);
 assertEquals("XML Test", doc.select("title").first().text());
 }

 @Test
 public void testParseXmlFragment() {
 String xml = "<book><title>XML Test</title></book>";
 Document doc = Jsoup.parse(xml);
 Element root = doc.rootElement();
 List<Node> nodes = Parser.parseFragment(xml, root, null);
 Element title = (Element) nodes.get(0);
 assertEquals("XML Test", title.text());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(1);
 String html = "<script>";
 List<ParseError> errors = parser.parseInput(html, null).errors();
 assertNotNull(errors);
 assertEquals(1, errors.size());
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(2);
 String html = "<script><script>";
 List<ParseError> errors = parser.parseInput(html, null).errors();
 assertNotNull(errors);
 assertEquals(2, errors.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNegativeMaxErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(-1);
 String html = "<script>";
 parser.parseInput(html, null);
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<h1>Hello World</h1>";
 Document doc = Parser.parseBodyFragment(html, null);
 assertNotNull(doc);
 assertEquals("Hello World", doc.head().title().text()); // will be null since parseBodyFragment doesn't create a head
 }

 @Test
 public void testParseInputWithCustomSettings() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser.settings(new ParseSettings().tagNameStart("html", "testHtml"));
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, null);
 assertEquals("testHtml", doc.tagName());
 }

 @Test
 public void testUnescapeEntities() {
 String escaped = "&amp;lt;Hello &amp;gt;World&amp;quot;";
 String unescaped = Parser.unescapeEntities(escaped, false);
 assertEquals("<Hello World>", unescaped);
 }

 @Test
 public void testUnescapeEntitiesWithAttributes() {
 String escaped = "<div class=\"&amp;lt;class&amp;gt;name\"></div>";
 String unescaped = Parser.unescapeEntities(escaped, true);
 assertEquals("<div class=\"<class>name\"></div>", unescaped);
 }
}