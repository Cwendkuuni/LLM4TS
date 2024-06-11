package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<html><body><h1>Hello</h1></body></html>";
 String baseUri = "http://example.com/";
 Document doc = parser.parseInput(html, baseUri);
 assertNotNull(doc);
 assertEquals("<html><body><h1>Hello</h1></body></html>", doc.html());
 }

 @Test
 public void testParseInputWithoutBaseUri() {
 String html = "<html><body><h1>Hello</h1></body></html>";
 Document doc = parser.parseInput(html);
 assertNotNull(doc);
 assertEquals("<html><body><h1>Hello</h1></body></html>", doc.html());
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser1 = new Parser(new HtmlTreeBuilder());
 Parser parser2 = parser1.setTreeBuilder(new XmlTreeBuilder());
 assert(parser1 != parser2);
 }

 @Test
 public void testIsTrackErrorsAndSetTrackErrors() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 assertTrue(parser.isTrackErrors());
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertEquals(0, errors.size());
 parser = parser.setTrackErrors(5);
 assertTrue(parser.isTrackErrors());
 errors = parser.getErrors();
 assertNotNull(errors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTrackErrorsNegativeCount() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 parser = parser.setTrackErrors(-5);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div class='test'><p>Hello</p></div>";
 Element context = Jsoup.parse("", Parser.htmlParser()).body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "");
 Element div = (Element) nodes.get(0);
 assertEquals("div", div.tagName());
 assertEquals("test", div.classes().get(0));
 assertEquals("Hello", div.child(0).text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullContext() {
 Parser.parseFragment(null, null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullFragment() {
 Parser.parseFragment("", null, "");
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div class='test'><p>Hello</p></div>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "");
 Element body = doc.body();
 Element div = (Element) body.getChild(0);
 assertEquals("div", div.tagName());
 assertEquals("test", div.classes().get(0));
 assertEquals("Hello", div.child(0).text());
 }

 @Test
 public void testUnescapeHtmlEntities() {
 String htmlEscaped = "&lt;p&gt;Hello&amp;world&rt;";
 String unescaped = Parser.unescapeHtmlEntities(htmlEscaped);
 assertEquals("<p>Helloworld</p>", unescaped);
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<script>alert('Hello')</script>";
 Parser parser = new Parser(new HtmlTreeBuilder()) {
 @Override public ParseErrorList errors() {
 return ParseErrorList.tracking(1);
 }
 };
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = doc.errors();
 assertNotNull(errors);
 assertTrue(errors.size() > 0);
 assertEquals("Unexpected char 60 found: <", errors.get(0).message());
 }

 @Test
 public void testParseInputWithMaxErrors() {
 String html = "<script>alert('Hello')</script>";
 Parser parser = new Parser(new HtmlTreeBuilder()) {
 @Override public ParseErrorList errors() {
 return ParseErrorList.tracking(2);
 }
 };
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = doc.errors();
 assertNotNull(errors);
 assertEquals(Arrays.asList(new ParseError("Unexpected char 60 found: <"), new ParseError("Unexpected char 62 found: >")), errors);
 }

 @Test
 public void testParseInputWithEmptyMaxErrors() {
 String html = "<script>alert('Hello')</script>";
 Parser parser = new Parser(new HtmlTreeBuilder()) {
 @Override public ParseErrorList errors() {
 return ParseErrorList.tracking(0);
 }
 };
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = doc.errors();
 assertNotNull(errors);
 assertTrue(errors.isEmpty());
 }
}