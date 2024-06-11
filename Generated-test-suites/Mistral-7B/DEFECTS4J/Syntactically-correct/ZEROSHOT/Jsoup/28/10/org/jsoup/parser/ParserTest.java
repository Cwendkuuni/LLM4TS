package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;
import org.jsoup.nodes.TreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private Document document;

 @Before
 public void setUp() {
 treeBuilder = Mockito.spy(new HtmlTreeBuilder());
 parser = new Parser(treeBuilder);
 document = Mockito.spy(Document.createShell(""));
 }

 @Test
 public void testParseInput() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.tracking(1);

 Mockito.when(treeBuilder.parse(html, baseUri, errors)).thenReturn(document);

 Document result = parser.parseInput(html, baseUri);

 assertEquals(document, result);
 assertEquals(1, errors.size());
 assertFalse(errors.isEmpty());
 assertEquals(html, errors.get(0).getInput());
 assertEquals(ParseError.ErrorType.START_TAG_MISMATCH, errors.get(0).getType());
 assertEquals("html", errors.get(0).getLine());
 assertEquals(1, errors.get(0).getColumn());
 }

 @Test
 public void testParseInput_NoErrors() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";

 Document result = parser.parseInput(html, baseUri);

 assertEquals(document, result);
 assertNull(parser.getErrors());
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = Mockito.spy(new XmlTreeBuilder());

 parser.setTreeBuilder(newTreeBuilder);

 assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());

 parser.setTrackErrors(1);

 assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 ParseError error = new ParseError("Error message", ParseError.ErrorType.START_TAG_MISMATCH, 1, 1);
 List<ParseError> errors = new ArrayList<>();
 errors.add(error);

 Mockito.when(parser.getErrors()).thenReturn(errors);

 assertEquals(errors, parser.getErrors());
 }

 @Test
 public void testGetErrors_NoErrors() {
 assertNull(parser.getErrors());
 }

 @Test
 public void testParse_Default() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";

 Document result = Parser.parse(html, baseUri);

 assertEquals(document, result);
 }

 @Test
 public void testParseFragment() {
 String fragmentHtml = "<div>Hello World</div>";
 Element context = Mockito.spy(Document.createRootElement("html"));
 String baseUri = "http://example.com";
 List<Node> nodeList = new ArrayList<>();
 nodeList.add(Mockito.spy(Document.createTextNode("Hello World")));
 Node node = nodeList.get(0);

 Mockito.when(treeBuilder.parseFragment(fragmentHtml, context, baseUri, ParseErrorList.noTracking())).thenReturn(nodeList);

 List<Node> result = Parser.parseFragment(fragmentHtml, context, baseUri);

 assertEquals(nodeList, result);
 assertEquals(node, result.get(0));
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<div>Hello World</div>";
 String baseUri = "http://example.com";

 Document result = Parser.parseBodyFragment(bodyHtml, baseUri);

 assertEquals(document, result);
 assertEquals(1, document.body().childNodeSize());
 assertEquals(bodyHtml, document.body().child(0).text());
 }

 @Test
 public void testParseBodyFragmentRelaxed_Deprecated() {
 String bodyHtml = "<div>Hello World</div>";
 String baseUri = "http://example.com";

 Document result = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

 assertEquals(document, result);
 }
}