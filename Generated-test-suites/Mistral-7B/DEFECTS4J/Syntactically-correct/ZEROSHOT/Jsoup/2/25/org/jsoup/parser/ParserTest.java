package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.*;

import org.junit.Test;

public class ParserTest {

 private static final String HTML_WITH_COMMENT = "<html><!-- This is a comment --><body></body></html>";
 private static final String HTML_WITH_XML_DECL = "<html xmlns:html=\"http://ns.adobe.com/html/1.0/\" xml:lang=\"en\">";
 private static final String HTML_WITH_EMPTY_TAG = "<html><img src=\"image.png\"/></html>";
 private static final String HTML_WITH_SELF_CLOSED_TAG = "<html><input type=\"text\"/></html>";
 private static final String HTML_WITH_INVALID_TAG = "<html><badtag></html>";
 private static final String HTML_WITH_BASE_TAG = "<html><base href=\"base.html\"/><body></body></html>";
 private static final String HTML_WITH_DATA_TAG = "<html><script type=\"text/javascript\">alert('Hello');</script></html>";
 private static final String HTML_WITH_CDATA = "<html><![CDATA[this is some raw text]]></html>";

 @Test
 public void testParser() {
 Parser parser = new Parser(HTML_WITH_COMMENT, null, false);
 Document doc = parser.parse();
 assertNotNull(doc);
 Comment comment = (Comment) doc.body().child(0);
 assertEquals("This is a comment", comment.text());

 parser = new Parser(HTML_WITH_XML_DECL, null, false);
 doc = parser.parse();
 assertNotNull(doc);
 XmlDeclaration xmlDecl = (XmlDeclaration) doc.head();
 assertTrue(xmlDecl.isStandalone());
 assertEquals("en", xmlDecl.xmlLang());

 parser = new Parser(HTML_WITH_EMPTY_TAG, null, false);
 doc = parser.parse();
 assertNotNull(doc);
 Element img = doc.body().child(0);
 assertTrue(img.tagName().equals("img"));
 assertEquals(0, img.childNodeSize());

 parser = new Parser(HTML_WITH_SELF_CLOSED_TAG, null, false);
 doc = parser.parse();
 assertNotNull(doc);
 Element input = doc.body().child(0);
 assertTrue(input.tagName().equals("input"));
 assertTrue(input.tag().isEmpty());
 assertEquals(0, input.childNodeSize());

 parser = new Parser(HTML_WITH_INVALID_TAG, null, false);
 try {
 doc = parser.parse();
 fail("Expected ParserException");
 } catch (Parser.ParserException e) {
 // Expected
 }

 parser = new Parser(HTML_WITH_BASE_TAG, null, false);
 doc = parser.parse();
 assertNotNull(doc);
 assertEquals("base.html", doc.baseUri());

 parser = new Parser(HTML_WITH_DATA_TAG, null, false);
 doc = parser.parse();
 assertNotNull(doc);
 TextNode scriptText = (TextNode) doc.body().child(0);
 assertEquals("alert('Hello');", scriptText.text());

 parser = new Parser(HTML_WITH_CDATA, null, false);
 doc = parser.parse();
 assertNotNull(doc);
 TextNode cdataText = (TextNode) doc.body().child(0);
 assertEquals("this is some raw text", cdataText.text());
 }

 @Test
 public void testParserBodyFragment() {
 Parser parser = new Parser("<html><body><h1>Hello</h1></body></html>", null, true);
 Document doc = parser.parse();
 assertNotNull(doc);
 Element h1 = doc.body().child(0);
 assertEquals("Hello", h1.text());
 }
}