package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.TreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private ParseErrorList errors;
 private Document document;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 errors = ParseErrorList.noTracking();
 }

 @Test
 public void testParser(){
 // test constructor
 Parser parser1 = new Parser(treeBuilder);
 // test parseInput method
 String html = "<html><body><h1>Test</h1></body></html>";
 document = parser.parseInput(html, "http://www.example.com");
 // test getTreeBuilder method
 TreeBuilder treeBuilderActual = parser.getTreeBuilder();
 assertSame(treeBuilder, treeBuilderActual);
 // test isTrackErrors method
 assertFalse(parser.isTrackErrors());
 // test setTrackErrors method
 parser.setTrackErrors(1);
 assertTrue(parser.isTrackErrors());
 errors = parser.getErrors();
 assertNotNull(errors);
 // test parseInput method with tracking errors
 errors = ParseErrorList.tracking(1);
 document = parser.parseInput(html, "http://www.example.com");
 errors = parser.getErrors();
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Error message", error.getMessage());
 // test setTreeBuilder method
 TreeBuilder treeBuilderNew = new XmlTreeBuilder();
 parser.setTreeBuilder(treeBuilderNew);
 TreeBuilder treeBuilderActualNew = parser.getTreeBuilder();
 assertSame(treeBuilderNew, treeBuilderActualNew);
 // test parseInput method with new treeBuilder
 document = parser.parseInput(html, "http://www.example.com");
 treeBuilderActual = parser.getTreeBuilder();
 assertSame(treeBuilderNew, treeBuilderActual);
 // test static parse method
 Document documentStatic = Parser.parse(html, "http://www.example.com");
 assertNotNull(documentStatic);
 // test static parseFragment method
 Element element = new Element("div");
 document = Document.createShell("http://www.example.com");
 document.body().appendChild(element);
 String fragmentHtml = "<p>Fragment</p>";
 List<Node> nodeList = Parser.parseFragment(fragmentHtml, element, "http://www.example.com");
 Node node = nodeList.get(0);
 assertNotNull(node);
 Element p = (Element) node;
 assertEquals("p", p.tagName());
 // test static parseXmlFragment method
 String fragmentXml = "<p xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3.org/1999/xhtml xhtml1-strict.dtd\"><p/></p>";
 List<Node> nodeListXml = Parser.parseXmlFragment(fragmentXml, "http://www.example.com");
 assertNotNull(nodeListXml);
 // test static parseBodyFragment method
 String bodyHtml = "<h1>Header</h1><body><p>Body</p></body>";
 document = Parser.parseBodyFragment(bodyHtml, "http://www.example.com");
 assertEquals(2, document.numChildren());
 Element h1 = (Element) document.child(0);
 assertEquals("h1", h1.tagName());
 Element body = (Element) document.child(1);
 assertEquals("body", body.tagName());
 // test static unescapeEntities method
 String string = "&lt;p&gt;Test&amp;amp;&#38;amp;&#38;gt;";
 String unescapedString = Parser.unescapeEntities(string, false);
 assertEquals("<p>Test&amp; &gt;</p>", unescapedString);
 // test static parseBodyFragmentRelaxed method (deprecated)
 document = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://www.example.com");
 assertNotNull(document);
 }

 private boolean booleanAssert(boolean condition, String message) {
 if (!condition) {
 fail(message);
 }
 return condition;
 }

 private void assertFalse(boolean condition) {
 booleanAssert(!condition, "Expected false, but was: " + condition);
 }

 private void assertTrue(boolean condition) {
 booleanAssert(condition, "Expected true, but was: " + condition);
 }
}