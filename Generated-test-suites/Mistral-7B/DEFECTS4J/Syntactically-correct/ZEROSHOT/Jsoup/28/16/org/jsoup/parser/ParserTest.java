package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);
 assertEquals(html, doc.html());
 }

 @Test
 public void testParseEmptyInput() {
 String html = "";
 String baseUri = "http://example.com";
 Document doc = parser.parseInput(html, baseUri);
 assertTrue(doc.body().text().isEmpty());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<script>alert('Hello')</script>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);
 Document doc = parser.parseInput(html, baseUri);
 List<ParseError> errors = parser.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Expected '<', but was '<script>'", errors.get(0).getMessage());
 }

 @Test(expected = ParseError.class)
 public void testParseInputWithErrorsMaxReached() {
 String html = "<html><body>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(0);
 parser.parseInput(html, baseUri);
 parser.parseInput(html, baseUri); // should throw an exception
 }

 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = treeBuilder;
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
 assertEquals(newTreeBuilder, actualTreeBuilder);
 }

 @Test
 public void testIsTrackErrors() {
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 parser.setTrackErrors(0);
 assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 String html = "<script>alert('Hello')</script>";
 String baseUri = "http://example.com";
 parser.setTrackErrors(1);
 List<ParseError> expectedErrors = new ArrayList<>();
 expectedErrors.add(new ParseError("Expected '<', but was '<script>'"));
 List<ParseError> actualErrors = parser.parseInput(html, baseUri).getErrors();
 assertEquals(expectedErrors, actualErrors);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Hello World</div>";
 Element context = new Element("html");
 Document doc = Document.createShell("http://example.com");
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
 Node expectedNode = new Element("div").text("Hello World");
 Node actualNode = nodes.get(0);
 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
 }
}