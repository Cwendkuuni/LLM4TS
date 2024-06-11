package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {

 @Test
 public void testParseEmpty() {
 Parser parser = new Parser("", "", false);
 Document doc = parser.parse();
 assertEquals("", doc.body().outerHtml());
 }

 @Test
 public void testParseSimpleHtml() {
 String html = "<html><body><p>Hello World!</p></body></html>";
 Parser parser = new Parser(html, "", false);
 Document doc = parser.parse();
 assertEquals("<html><head></head><body><p>Hello World!</p></body></html>", doc.outerHtml());
 Element body = doc.body();
 assertTrue(body.childNodes().size() == 1);
 Element p = (Element) body.childNodes().get(0);
 assertEquals("Hello World!", p.text());
 }

 @Test
 public void testParseComments() {
 String html = "<html><body><!-- this is a comment --><p>Hello World!</p></body></html>";
 Parser parser = new Parser(html, "", false);
 Document doc = parser.parse();
 Element body = doc.body();
 Element comment = (Comment) body.childNodes().get(0);
 assertEquals(" this is a comment ", comment.text());
 }

 @Test
 public void testParseCdata() {
 String html = "<html><body><![CDATA[ <script type=\"text/javascript\">alert(\"Hello World!\");</script> ]]></body></html>";
 Parser parser = new Parser(html, "", false);
 Document doc = parser.parse();
 Element script = (Element) doc.body().childNodes().get(0);
 assertTrue(script.childNodes().size() == 1);
 DataNode data = (DataNode) script.childNodes().get(0);
 assertEquals("<script type=\"text/javascript\">alert(\"Hello World!\");</script>", data.outerHtml());
 }

 @Test
 public void testParseXmlDecl() {
 String html = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" version=\"1.0\">";
 Parser parser = new Parser(html, "", false);
 Document doc = parser.parse();
 XmlDeclaration decl = (XmlDeclaration) doc.childNodes().get(0);
 assertEquals("xmlns:html=\"http://www.w3.org/1999/xhtml\" version=\"1.0\"", decl.toString());
 }

 @Test
 public void testParseAttributes() {
 String html = "<a href=\"http://www.google.com\" id=\"link1\" title=\"Google\">Google</a>";
 Parser parser = new Parser(html, "", false);
 Document doc = parser.parse();
 Element link = (Element) doc.body().childNodes().get(0);
 List<Attribute> attrs = link.attributes();
 assertEquals(3, attrs.size());
 Attribute href = attrs.get(0);
 assertEquals("href", href.getKey());
 assertEquals("http://www.google.com", href.getValue());
 Attribute id = attrs.get(1);
 assertEquals("id", id.getKey());
 assertEquals("link1", id.getValue());
 Attribute title = attrs.get(2);
 assertEquals("title", title.getKey());
 assertEquals("Google", title.getValue());
 }

 @Test
 public void testParseEmptyTag() {
 String html = "<img src=\"image.jpg\" />";
 Parser parser = new Parser(html, "", false);
 Document doc = parser.parse();
 Element img = (Element) doc.body().childNodes().get(0);
 assertTrue(img.childNodes().isEmpty());
 }

 @Test
 public void testParseScriptTag() {
 String html = "<script type=\"text/javascript\">\"use strict\";alert(\"Hello World!\");</script>";
 Parser parser = new Parser(html, "", false);
 Document doc = parser.parse();
 Element script = (Element) doc.body().childNodes().get(0);
 assertTrue(script.childNodes().isEmpty());
 }

 @Test
 public void testParseTextareaTag() {
 String html = "<textarea>Hello World!</textarea>";
 Parser parser = new Parser(html, "", false);
 Document doc = parser.parse();
 Element textarea = (Element) doc.body().childNodes().get(0);
 TextNode text = (TextNode) textarea.childNodes().get(0);
 assertEquals("Hello World!", text.text());
 }

 @Test
 public void testParseBaseTag() {
 String html = "<base href=\"/test\">";
 Parser parser = new Parser(html, "", false);
 Document doc = parser.parse();
 assertEquals("/test", doc.baseUri());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Hello World!</p>";
 Parser parser = new Parser(bodyHtml, "", true);
 Document doc = parser.parse();
 assertEquals("<html><head></head><body>" + bodyHtml + "</body></html>", doc.outerHtml());
 Element p = (Element) doc.body().childNodes().get(0);
 assertEquals("Hello World!", p.text());
 }

 @Test
 public void testParseInvalidHtml() {
 String html = "</p><html>";
 try {
 Parser parser = new Parser(html, "", false);
 parser.parse();
 fail("Expected ParserException");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testParseInvalidHtml2() {
 String html = "</body></html><p>";
 try {
 Parser parser = new Parser(html, "", false);
 parser.parse();
 fail("Expected ParserException");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testParseInvalidHtml3() {
 String html = "<html><body><script>alert('Hello World!');</script>Hello World!";
 try {
 Parser parser = new Parser(html, "", false);
 parser.parse();
 fail("Expected ParserException");
 } catch (Exception e) {
 // expected
 }
 }
}