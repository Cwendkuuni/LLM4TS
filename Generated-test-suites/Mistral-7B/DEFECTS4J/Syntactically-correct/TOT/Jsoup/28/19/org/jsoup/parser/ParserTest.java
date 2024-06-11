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
 public void testParseInputHappyPath() {
 String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 Document doc = parser.parseInput(html, "");

 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseInputWithErrors() {
 String html = "<html><head><title>Test</title></head><body><p>Hello <script>World</script></p></body></html>";
 List<ParseError> errors = parser.parseInput(html, "").getErrors();

 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals(ParseError.TagStartError, error.getType());
 assertEquals("Expected '>' but was '<' (position:startTagStart=1:12)", error.getMessage());
 assertNull(parser.getErrors());
 }

 @Test
 public void testParseInputWithMaxErrorsReached() {
 String html = "<html><head><title>Test</title></head><body><p>Hello </p><p>World</p></body></html>";
 parser.setTrackErrors(2);
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = parser.getErrors();

 assertEquals(2, errors.size());
 assertTrue(errors.get(0).getMessage().contains("Expected '>' but was '<'"));
 assertTrue(errors.get(1).getMessage().contains("Unexpected char 0x40 (position:char=1:24)"));
 assertNull(parser.getErrors());
 }

 @Test
 public void testParseInputWithBaseUri() {
 String html = "<img src=\"img.jpg\" alt=\"Image\">";
 String baseUri = "http://example.com/";
 Document doc = parser.parseInput(html, baseUri);
 String imgSrc = doc.select("img[src]").attr("src");
 assertEquals("http://example.com/img.jpg", imgSrc);
 }

 @Test
 public void testParseInputWithCustomTreeBuilder() {
 treeBuilder = new XmlTreeBuilder();
 parser = new Parser(treeBuilder);
 String xml = "<root><child><grandChild/></child></root>";
 Document doc = parser.parseInput(xml, "");
 Element root = doc.select("root").first();
 assertEquals("child", root.child(0).tagName());
 }

 @Test
 public void testParseFragmentHappyPath() {
 String fragmentHtml = "<p>Hello World</p>";
 Element context = Jsoup.parseBodyFragment(
 "<html><body></body></html>", "").body();
 List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "");
 assertEquals(1, nodes.size());
 assertEquals("Hello World", nodes.get(0).text());
 }

 @Test
 public void testParseFragmentWithErrors() {
 String fragmentHtml = "<p>Hello <script>World</script></p>";
 Element context = Jsoup.parseBodyFragment(
 "<html><body></body></html>", "").body();
 List<ParseError> errors = Parser.getErrors();
 Parser.parseFragment(fragmentHtml, context, "");
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals(ParseError.TagStartError, error.getType());
 assertEquals("Expected '>' but was '<' (position:startTagStart=1:12)", error.getMessage());
 assertNull(Parser.getErrors());
 }

 @Test
 public void testParseBodyFragmentHappyPath() {
 String bodyHtml = "<p>Hello World</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "");
 assertEquals("Hello World", doc.body().text());
 }
}