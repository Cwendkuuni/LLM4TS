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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<html><head><title>Test</title></head>" +
 "<body><h1>Hello World</h1></body></html>";
 String baseUri = "https://example.com";
 Document doc = parser.parseInput(html, baseUri);

 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertSame(baseUri, doc.baseUri());
 }

 @Test(expected = NullPointerException.class)
 public void testParseInputWithNullHtml() {
 parser.parseInput(null, "https://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInputWithEmptyHtml() {
 parser.parseInput("", "https://example.com");
 }

 @Test
 public void testParseInputWithTrackErrors() {
 treeBuilder = new HtmlTreeBuilder(true);
 parser = new Parser(treeBuilder);
 String html = "<html><title>Test</title></html>"; // missing body tag
 Document doc = parser.parseInput(html, "https://example.com");

 List<ParseError> errors = parser.getErrors();
 assertNotNull(errors);
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Error parsing start tag : body", error.getMessage());
 }

 @Test
 public void testParseInputWithNoTrackErrors() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 String html = "<html><title>Test</title></html>"; // missing body tag
 Document doc = parser.parseInput(html, "https://example.com");

 List<ParseError> errors = parser.getErrors();
 assertNull(errors);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 assertSame(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }
}