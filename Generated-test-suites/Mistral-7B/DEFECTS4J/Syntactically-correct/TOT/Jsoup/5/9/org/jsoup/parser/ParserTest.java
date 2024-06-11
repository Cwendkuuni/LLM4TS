package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1><p>This is a <strong>test</strong>.</p></body></html>";
 parser = new Parser(html, "http://test.com");
 doc = parser.parse();
 }

 @Test
 public void testParseHtml() {
 // Test parsing an entire HTML document
 Element root = doc.body();
 assertEquals("Hello World", root.getElementsByTag("h1").text());
 assertEquals("This is a test.", root.getElementsByTag("p").text());
 }

 @Test
 public void testParseBodyFragment() {
 // Test parsing a body fragment
 String bodyHtml = "<p>This is a <strong>test</strong>.</p>";
 Document docFragment = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 Element rootFragment = docFragment.body();
 assertEquals("This is a test.", rootFragment.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 // Test parsing a body fragment with relaxed parsing
 String bodyHtmlRelaxed = "<p><script>alert('Hello');</script>This is a <strong>test</strong>.</p>";
 Document docFragmentRelaxed = Parser.parseBodyFragmentRelaxed(bodyHtmlRelaxed, "http://test.com");
 Element rootFragmentRelaxed = docFragmentRelaxed.body();
 assertEquals("This is a test.", rootFragmentRelaxed.getElementsByTag("p").text());
 // Check that the script tag was not parsed as an error, but just ignored
 assertNotNull(rootFragmentRelaxed.getElementsByTag("script"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 String nullHtml = null;
 try {
 Parser.parse(nullHtml, "http://test.com");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 String emptyHtml = "";
 try {
 Parser.parse(emptyHtml, "http://test.com");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseComment() {
 // Test parsing comments
 String htmlWithComment = "<html><!-- This is a comment --></html>";
 Document docWithComment = Parser.parse(htmlWithComment, "http://test.com");
 assertEquals("This is a comment", docWithComment.body().getElementsByTag("comment").text());
 }

 @Test
 public void testParseEndTag() {
 // Test parsing end tags
 String htmlWithEndTag = "<p><a><b></b></a></p>";
 Document docWithEndTag = Parser.parse(htmlWithEndTag, "http://test.com");
 Element rootWithEndTag = docWithEndTag.body();
 assertEquals(2, rootWithEndTag.getElementsByTag("p").size());
 assertEquals(1, rootWithEndTag.getElementsByTag("a").size());
 assertEquals(1, rootWithEndTag.getElementsByTag("b").size());
 }

 @Test
 public void testParseStartTag() {
 // Test parsing start tags
 String htmlWithStartTag = "<p><a href='http://test.com'><b>Hello</b></a></p>";
 Document docWithStartTag = Parser.parse(htmlWithStartTag, "http://test.com");
 Element rootWithStartTag = docWithStartTag.body();
 assertEquals(1, rootWithStartTag.getElementsByTag("p").size());
 assertEquals(1, rootWithStartTag.getElementsByTag("a").size());
 assertEquals(1, rootWithStartTag.getElementsByTag("b").size());
 assertEquals("http://test.com", rootWithStartTag.getElementsByTag("a").get(0).attr("href"));
 assertEquals("Hello", rootWithStartTag.getElementsByTag("b").text());
 }

 @Test
 public void testParseAttribute() {
 // Test parsing attributes
 String htmlWithAttribute = "<p class='test' id='test'><a href='test.com'><b>Hello</b></a></p>";
 Document docWithAttribute = Parser.parse(htmlWithAttribute, "http://test.com");
 Element rootWithAttribute = docWithAttribute.body();
 assertEquals("test", rootWithAttribute.classNames());
 assertEquals("test", rootWithAttribute.tag().attrName("class"));
 assertEquals("test.com", rootWithAttribute.getElementsByTag("a").get(0).attr("href"));
 }

 @Test
 public void testParseTextNode() {
 // Test parsing text nodes
 String htmlWithTextNode = "<p>This is a <textarea>test</textarea> text node.</p>";
 Document docWithTextNode = Parser.parse(htmlWithTextNode, "http://test.com");
 Element rootWithTextNode = docWithTextNode.body();
 assertEquals("This is a " + TextNode.class.getName() + " test text node.", rootWithTextNode.text());
 }

 @Test
 public void testParseCdata() {
 // Test parsing CDATA sections
 String htmlWithCdata = "<p><![CDATA[This is a CDATA section]]></p>";
 Document docWithCdata = Parser.parse(htmlWithCdata, "http://test.com");
 Element rootWithCdata = docWithCdata.body();
 assertEquals("This is a CDATA section", rootWithCdata.getElementsByTag("textNode").text());
 }

 @Test
 public void testParseXmlDecl() {
 // Test parsing XML declarations
 String htmlWithXmlDecl = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body><p>Hello World</p></body></html>";
 Document docWithXmlDecl = Parser.parse(htmlWithXmlDecl, "http://test.com");
 assertTrue(docWithXmlDecl.isXmlDeclarationPresent());
 }
}