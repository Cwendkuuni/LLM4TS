package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseErrorList;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, null);
 assertNotNull(doc);
 assertSame(html, doc.html());
 assertNull(parser.getErrors().size());
 }

 @Test
 public void testParseInputWithTrackingErrors() {
 String html = "<html><body><h1><script>alert('Hello World')</script></h1></body></html>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, null);
 assertNotNull(doc);
 assertSame(html, doc.html());
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertFalse(errors.isEmpty());
 assertEquals(1, errors.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, null);
 }

 @Test
 public void testParseInputWithNullBaseUri() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 parser.parseInput(html, null);
 }

 @Test
 public void testParseInputWithTrackingErrorsAndMaxReached() {
 String html = "<html><body><h1><script>alert('Hello World')</script></h1></body></html>";
 parser.setTrackErrors(1);
 for (int i = 0; i < 10; i++) {
 parser.parseInput(html, null);
 }
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertEquals(10, errors.size());
 }

 @Test
 public void testParseInputWithTrackingErrorsAndNoErrors() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, null);
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertEquals(0, errors.size());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src='test.html'>";
 String baseUri = "http://www.example.com/";
 Document doc = parser.parseInput(html, baseUri);
 String imgSrc = doc.select("img").attr("src");
 assertEquals("test.html", imgSrc);
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser1 = new Parser(new TreeBuilder());
 Parser parser2 = new Parser(new XmlTreeBuilder());
 assertNotSame(parser1.getTreeBuilder(), parser2.getTreeBuilder());
 parser1.setTreeBuilder(parser2.getTreeBuilder());
 assertSame(parser2.getTreeBuilder(), parser1.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 Parser parser = new Parser(new TreeBuilder());
 assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithMaxZero() {
 Parser parser = new Parser(new TreeBuilder());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithMaxGreaterThanZero() {
 Parser parser = new Parser(new TreeBuilder());
 parser.setTrackErrors(5);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 Parser parser = new Parser(new TreeBuilder());
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertFalse(errors.isEmpty());
 }

 @Test
 public void testGetErrorsEmpty() {
 Parser parser = new Parser(new TreeBuilder());
 parser.setTrackErrors(0);
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertEquals(0, errors.size());
 }

 @Test
 public void testStaticParse() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, null);
 assertNotNull(doc);
 assertSame(html, doc.html());
 assertNull(parser.getErrors().size());
 }

 @Test
 public void testStaticParseWithTrackingErrors() {
 String html = "<html><body><h1><script>alert('Hello World')</script></h1></body></html>";
 Parser parser = new Parser(new TreeBuilder());
 parser.setTrackErrors(1);
 Document doc = Parser.parse(html, null);
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertFalse(errors.isEmpty());
 assertEquals(1, errors.size());
 }

 @Test
 public void testStaticParseFragment() {
 String html = "<div id='content'>Hello World</div>";
 Element div = Jsoup.parseBodyFragment(html).selectFirst("#content");
 assertNotNull(div);
 assertEquals("Hello World", div.text());
 }

 @Test
 public void testStaticParseBodyFragment() {
 String html = "<html><body><div id='content'>Hello World</div></body></html>";
 Document doc = Parser.parseBodyFragment(html, null);
 Element div = doc.selectFirst("#content");
 assertNotNull(div);
 assertEquals("Hello World", div.text());
 }

 @Test
 public void testStaticParseBodyFragmentWithBaseUri() {
 String html = "<img src='test.html'>";
 String baseUri = "http://www.example.com/";
 Document doc = Parser.parseBodyFragment(html, baseUri);
 String imgSrc = doc.select("img").attr("src");
 assertEquals("test.html", imgSrc);
 }
}