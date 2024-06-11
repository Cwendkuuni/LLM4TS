package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

import java.util.LinkedList;

/**
 * Test for Parser class
 */
public class ParserTest {

 private Parser parser;

 @Before
 public void setUp() {
 parser = new Parser("<html><head><title>Test Title</title></head><body>" +
 "<div id='testDiv'><h1>Test H1</h1><p>Test Paragraph</p></div>" +
 "</body></html>", "http://test.com");
 }

 /**
 * Test parse method
 */
 @Test
 public void testParse() {
 Document doc = parser.parse();
 Element root = doc.body();
 assertEquals("div", root.tagName());
 Element div = root.child(0);
 assertEquals("h1", div.tagName());
 assertEquals("Test Title", doc.title().text());
 assertEquals("Test Paragraph", root.child(1).text());
 }

 /**
 * Test parseBodyFragment method
 */
 @Test
 public void testParseBodyFragment() {
 Document doc = Parser.parseBodyFragment("<div id='testDiv'><h1>Test H1</h1><p>Test Paragraph</p></div>", "http://test.com");
 Element root = doc.body();
 assertEquals("div", root.tagName());
 Element div = root.child(0);
 assertEquals("h1", div.tagName());
 assertEquals("Test H1", root.child(0).text());
 assertEquals("Test Paragraph", root.child(1).text());
 }

 /**
 * Test parseBodyFragmentRelaxed method
 */
 @Test
 public void testParseBodyFragmentRelaxed() {
 Document doc = Parser.parseBodyFragmentRelaxed("<div id='testDiv'><h1>Test H1</h1><p>Test Paragraph</p></div>", "http://test.com");
 Element root = doc.body();
 assertEquals("div", root.tagName());
 Element div = root.child(0);
 assertEquals("h1", div.tagName());
 assertEquals("Test H1", root.child(0).text());
 assertEquals("Test Paragraph", root.child(1).text());
 }

 /**
 * Test parseStartTag method
 */
 @Test
 public void testParseStartTag() {
 parser = new Parser("<html><head><title>Test Title</title></head><body>" +
 "<div id='testDiv'><h1>Test H1</h1><p>Test Paragraph</p></div>" +
 "</body></html>", "http://test.com");
 parser.tq.advance();
 parser.parseStartTag();
 assertEquals("html", parser.stack.getLast().tagName());
 }

 /**
 * Test parseEndTag method
 */
 @Test
 public void testParseEndTag() {
 parser = new Parser("<html><head><title>Test Title</title></head><body>" +
 "<div id='testDiv'><h1>Test H1</h1><p>Test Paragraph</p></div>" +
 "</body></html>", "http://test.com");
 parser.tq.advance();
 parser.consume("</div>");
 assertEquals("div", parser.stack.getLast().tagName());
 parser.parseEndTag();
 assertEquals(null, parser.stack.getLast());
 }

 /**
 * Test parseComment method
 */
 @Test
 public void testParseComment() {
 parser = new Parser("<html><!-- Test Comment --></html>", "http://test.com");
 parser.parseComment();
 assertTrue(parser.stack.getLast() instanceof Comment);
 }

 /**
 * Test parseXmlDecl method
 */
 @Test
 public void testParseXmlDecl() {
 parser = new Parser("<html xmlns='http://test.com'><head></head><body></body></html>", "http://test.com");
 parser.parseXmlDecl();
 assertTrue(parser.stack.getLast() instanceof XmlDeclaration);
 }

 /**
 * Test parseCdata method
 */
 @Test
 public void testParseCdata() {
 parser = new Parser("<html><![CDATA[Test Cdata]]></html>", "http://test.com");
 parser.parseCdata();
 assertTrue(parser.stack.getLast() instanceof TextNode);
 }

 /**
 * Test parseAttribute method
 */
 @Test
 public void testParseAttribute() {
 parser = new Parser("<div id='testDiv' class='testClass'><h1>Test H1</h1></div>");
 Attribute idAttr = parser.parseAttribute();
 assertEquals("id", idAttr.getKey());
 assertEquals("testDiv", idAttr.getValue());
 Attribute classAttr = parser.parseAttribute();
 assertEquals("class", classAttr.getKey());
 assertEquals("testClass", classAttr.getValue());
 }

 /**
 * Test parseTextNode method
 */
 @Test
 public void testParseTextNode() {
 parser = new Parser("<div>Test Text</div>");
 TextNode textNode = (TextNode) parser.parseTextNode();
 assertEquals("Test Text", textNode.text());
 }

 /**
 * Test parseTag method with invalid tag
 */
 @Test(expected = IllegalArgumentException.class)
 public void testParseTagWithInvalidTag() {
 parser = new Parser("<html><body><foobar></foobar></body></html>");
 parser.parseStartTag();
 }

 /**
 * Test parseTag method with unknown tag
 */
 @Test
 public void testParseTagWithUnknownTag() {
 parser = new Parser("<html><body><foobar></foobar></body></html>");
 parser.relaxed = true;
 parser.parseStartTag();
 assertEquals("foobar", parser.stack.getLast().tagName());
 }

 /**
 * Test parseTag method with self closing tag
 */
 @Test
 public void testParseTagWithSelfClosingTag() {
 parser = new Parser("<html><body><img src='test.jpg' /></body></html>");
 parser.parseStartTag();
 assertEquals("img", parser.stack.getLast().tagName());
 assertEquals(true, parser.stack.getLast().isEmpty());
 }

 /**
 * Test parseTag method with empty tag
 */
 @Test
 public void testParseTagWithEmptyTag() {
 parser = new Parser("<html><body><input type='text' /></body></html>");
 parser.parseStartTag();
 assertEquals("input", parser.stack.getLast().tagName());
 assertEquals(true, parser.stack.getLast().isEmpty());
 }

 /**
 * Test stackHasValidParent method
 */
 @Test
 public void testStackHasValidParent() {
 parser = new Parser("<html><head><body><h1>Test</h1></body></head></html>");
 assertTrue(parser.stackHasValidParent(htmlTag));
 assertTrue(parser.stackHasValidParent(headTag));
 assertTrue(parser.stackHasValidParent(bodyTag));
 assertFalse(parser.stackHasValidParent(titleTag));
 }

 /**
 * Test addChildToParent method
 */
 @Test
 public void testAddChildToParent() {
 parser = new Parser("<html><body></body></html>");
 Element body = parser.stack.getLast();
 Element div = new Element(tag("div"), baseUri);
 parser.addChildToParent(div, false);
 assertEquals(div, body.child(0));
 }

 /**
 * Test addChildToParent method with invalid parent
 */
 @Test
 public void testAddChildToParentWithInvalidParent() {
 parser = new Parser("<html><head></head></html>");
 Element head = parser.stack.getLast();
 Element div = new Element(tag("div"), baseUri);
 parser.addChildToParent(div, false);
 assertNull(div.parent());
 }

 /**
 * Test addChildToParent method with self-closing tag
 */
 @Test
 public void testAddChildToParentWithSelfClosingTag() {
 parser = new Parser("<html><body></body></html>");
 Element body = parser.stack.getLast();
 Element img = new Element(tag("img"), baseUri);
 img.setSelfClosing(true);
 parser.addChildToParent(img, false);
 assertEquals(img, body.child(0));
 }

 /**
 * Test addChildToParent method with empty tag
 */
 @Test
 public void testAddChildToParentWithEmptyTag() {
 parser = new Parser("<html><body></body></html>");
 Element body = parser.stack.getLast();
 Element input = new Element(tag("input"), baseUri);
 input.setEmptyElement(true);
 parser.addChildToParent(input, false);
 assertEquals(input, body.child(0));
 }
}