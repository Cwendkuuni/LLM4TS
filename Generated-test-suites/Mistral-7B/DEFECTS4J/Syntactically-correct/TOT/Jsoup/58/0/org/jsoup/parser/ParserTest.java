package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

public class ParserTest {

 private Parser parser;

 @Test
 public void testParseInput() {
 parser = new Parser(new HtmlTreeBuilder());

 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Document doc = parser.parseInput(html, "https://example.com");

 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
 }

 @Test
 public void testParseInputWithErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(5);

 String html = "<html><head><script>alert('XSS')</script></head><body></body></html>";
 Document doc = null;
 try {
 doc = parser.parseInput(html, "https://example.com");
 } catch (ParseError e) {
 // expected
 }

 Assert.assertNull(doc);
 Assert.assertEquals(5, parser.getErrors().size());
 }

 @Test
 public void testParseInputWithEmptyString() {
 parser = new Parser(new HtmlTreeBuilder());

 Document doc = parser.parseInput("", "https://example.com");
 Assert.assertNull(doc);
 }

 @Test
 public void testParseInputWithNullString() {
 parser = new Parser(new HtmlTreeBuilder());

 Document doc = parser.parseInput(null, "https://example.com");
 Assert.assertNull(doc);
 }

 @Test
 public void testParseInputWithBaseUriIsNull() {
 parser = new Parser(new HtmlTreeBuilder());

 String html = "<img src='relative.html' />";
 Document doc = parser.parseInput(html, null);

 Assert.assertNotNull(doc);
 Assert.assertEquals("relative.html", doc.baseUri());
 }

 @Test
 public void testGetTreeBuilder() {
 parser = new Parser(new HtmlTreeBuilder());

 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
 }

 @Test
 public void testSetTreeBuilder() {
 parser = new Parser(new HtmlTreeBuilder());

 TreeBuilder htmlTreeBuilder = new HtmlTreeBuilder();
 Parser parserWithHtmlTreeBuilder = parser.setTreeBuilder(htmlTreeBuilder);

 Assert.assertEquals(htmlTreeBuilder, parserWithHtmlTreeBuilder.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());

 Assert.assertFalse(parser.isTrackErrors());

 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(1);

 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrorsWithZero() {
 parser = new Parser(new HtmlTreeBuilder());

 parser.setTrackErrors(0);

 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(5);

 String html = "<html><head></head><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
 Document doc = null;
 try {
 doc = parser.parseInput(html, "https://example.com");
 } catch (ParseError e) {
 // expected
 }

 List<ParseError> errors = parser.getErrors();
 Assert.assertEquals(5, errors.size());
 }

 @Test
 public void testSettings() {
 parser = new Parser(new HtmlTreeBuilder());

 ParseSettings settings = parser.settings();
 Assert.assertNotNull(settings);
 }

 @Test
 public void testSettingsWithCustomSettings() {
 parser = new Parser(new HtmlTreeBuilder());

 ParseSettings settings = new ParseSettings();
 settings.setOmitScriptAndStyleTags(true);
 Parser parserWithCustomSettings = parser.settings(settings);

 ParseSettings settingsFromParser = parserWithCustomSettings.settings();
 Assert.assertEquals(settings, settingsFromParser);
 }

 @Test
 public void testParseStaticFunctionParse() {
 Document doc = Parser.parse("
 <html><head></head><body><h1>Hello World</h1></body></html>
 ", "https://example.com");

 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
 }

 @Test
 public void testParseStaticFunctionParseFragment() {
 List<Node> nodes = Parser.parseFragment("<p>Hello World</p>", null, "https://example.com");
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertEquals("Hello World", nodes.get(0).text());
 }

 @Test
 public void testParseStaticFunctionParseXmlFragment() {
 List<Node> nodes = Parser.parseXmlFragment("<root><child>Hello World</child></root>", "https://example.com");
 Assert.assertEquals(1, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertEquals("Hello World", nodes.get(0).text());
 }

 @Test
 public void testParseStaticFunctionParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<body><p>Hello World</p></body>", "https://example.com");
 Assert.assertNotNull(doc);
 Assert.assertEquals("Hello World", doc.getElementsByTag("p").text());
 }

 @Test
 public void testParseStaticFunctionParseBodyFragmentWithEmptyString() {
 Document doc = Parser.parseBodyFragment("", "https://example.com");
 Assert.assertNotNull(doc);
 Assert.assertTrue(doc.body().isEmpty());
 }

 @Test
 public void testParseStaticFunctionParseBodyFragmentWithNullString() {
 Document doc = Parser.parseBodyFragment(null, "https://example.com");
 Assert.assertNull(doc);
 }

 @Test
 public void testUnescapeEntities() {
 String stringWithEntities = "&lt;p&gt;Hello &amp; World&lt;/p&gt;";
 String stringWithoutEntities = Parser.unescapeEntities(stringWithEntities, false);
 Assert.assertEquals("<p>Hello & World</p>", stringWithoutEntities);
 }

 @Test
 public void testUnescapeEntitiesWithAttribute() {
 String stringWithEntities = "<p class='test &class2'>&lt;strong&gt;Hello &amp; World&lt;/strong&gt;</p>";
 String stringWithoutEntities = Parser.unescapeEntities(stringWithEntities, true);
 Assert.assertEquals("<p class='test class2'><strong>Hello & World</strong></p>", stringWithoutEntities);
 }
}