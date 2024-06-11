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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.
 */
public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;

 @Before
 public void setUp() {
 treeBuilder = new HtmlTreeBuilder();
 parser = new Parser(treeBuilder);
 }

 /**
 * Test parseInput method with valid HTML, default error tracking.
 */
 @Test
 public void testParseInputValidHtml() {
 String html = "<html><body><h1>Test</h1></body></html>";
 Document doc = parser.parseInput(html, "");

 assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
 }

 /**
 * Test parseInput method with valid HTML, error tracking enabled.
 */
 @Test
 public void testParseInputValidHtmlWithErrors() {
 parser.setTrackErrors(5);
 String html = "<html><body><h1>Test</h1></body></html>";
 Document doc = parser.parseInput(html, "");
 List<ParseError> errors = parser.getErrors();

 assertEquals("<html><head></head><body><h1>Test</h1></body></html>", doc.html());
 assertFalse(errors.isEmpty());
 assertEquals(5, errors.size());
 }

 /**
 * Test parseInput method with invalid HTML, default error tracking.
 */
 @Test(expected = org.jsoup.parser.ParseError.class)
 public void testParseInputInvalidHtml() {
 String html = "<html><body><h1>Test</h1><script>alert('Hello World!')</script></body></html>";
 parser.parseInput(html, "");
 }

 /**
 * Test parseInput method with invalid HTML, error tracking enabled.
 */
 @Test
 public void testParseInputInvalidHtmlWithErrors() {
 parser.setTrackErrors(1);
 String html = "<html><body><h1>Test</h1><script>alert('Hello World!')</script></body></html>";
 Document doc = null;
 try {
 doc = parser.parseInput(html, "");
 } catch (ParseError e) {
 // expected
 }
 List<ParseError> errors = parser.getErrors();

 assertNull(doc);
 assertTrue(errors.size() > 0);
 }

 /**
 * Test getErrors method when no errors occurred.
 */
 @Test
 public void testGetErrorsNoErrors() {
 List<ParseError> errors = parser.getErrors();

 assertSame(ParseErrorList.noTracking(), errors);
 }

 /**
 * Test getTreeBuilder method.
 */
 @Test
 public void testGetTreeBuilder() {
 TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

 assertEquals(expectedTreeBuilder, actualTreeBuilder);
 }

 /**
 * Test setTreeBuilder method.
 */
 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

 assertEquals(newTreeBuilder, actualTreeBuilder);
 }

 /**
 * Test isTrackErrors method.
 */
 @Test
 public void testIsTrackErrors() {
 assertFalse(parser.isTrackErrors());
 parser.setTrackErrors(5);
 assertTrue(parser.isTrackErrors());
 }

 /**
 * Test setTrackErrors method.
 */
 @Test
 public void testSetTrackErrors() {
 parser.setTrackErrors(5);
 int maxErrors = parser.getMaxErrors();

 assertEquals(5, maxErrors);
 }
}