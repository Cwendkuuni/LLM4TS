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
 private TreeBuilder treeBuilder;

 @Before
 public void setup() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "");
 assertEquals("Hello World", doc.head().title().text());
 assertNotNull(doc.body());
 assertNull(doc.head());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"image\">";
 String baseUri = "http://example.com";
 Document doc = Parser.parse(html, baseUri);
 String imgSrc = doc.imageTag().first().attr("abs:src");
 assertEquals("http://example.com/image.jpg", imgSrc);
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<script>alert('Hello World')</script>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals(ParseError.SYNTAX_ERR, error.type());
 assertEquals("Unexpected char (0x27): ''\n" +
         "at line 1, column 12: <script>alert('Hello World')</script>", error.message());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithNullBaseUri() {
 parser.parseInput("", null);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithNegativeValue() {
 parser.setTrackErrors(-1);
 assertEquals(Parser.DEFAULT_MAX_ERRORS, parser.getMaxErrors());
 }

 @Test
 public void testSetTrackErrorsWithZeroValue() {
 parser.setTrackErrors(0);
 assertNotNull(parser.getErrors());
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 String html = "<script>alert('Hello World')</script>";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertEquals(1, errors.size());
 }

 @Test
 public void testStaticParseFunctions() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "");
 assertEquals("Hello World", doc.head().title().text());
 assertNotNull(doc.body());
 assertNull(doc.head());
 }
}