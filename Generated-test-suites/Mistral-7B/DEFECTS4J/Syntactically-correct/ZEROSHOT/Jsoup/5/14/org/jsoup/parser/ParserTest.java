package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 @Test
 public void testParser() {
 Parser parser = new Parser("", "", false); // empty constructor test
 assertNull(parser.doc);
 assertNotNull(parser.stack);
 assertNotNull(parser.tq);

 Parser parser1 = new Parser("<html><head></head><body></body></html>", "", false);
 Document doc1 = parser1.parse();
 assertEquals("html", doc1.tagName());
 Element html1 = doc1;
 assertEquals("head", html1.childNodes().get(0).tagName());
 assertEquals("body", html1.childNodes().get(1).tagName());
 assertFalse(doc1.body().hasText());

 Parser parser2 = new Parser("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body>", "", false);
 Document doc2 = parser2.parse();
 assertEquals("html", doc2.tagName());
 Element html2 = doc2;
 assertEquals("head", html2.childNodes().get(0).tagName());
 Element title2 = html2.childNodes().get(0).childNodes().get(0);
 assertEquals("Test Title", title2.text());
 assertEquals("body", html2.childNodes().get(1).tagName());
 Element h12 = html2.childNodes().get(1).childNodes().get(0);
 assertEquals("h1", h12.tagName());
 assertEquals("Test Heading", h12.text());

 Parser parser3 = new Parser("<html><head><title>Test Title</title></head><body><p>Test Paragraph<br/></p></body></html>", "", false);
 Document doc3 = parser3.parse();
 Element html3 = doc3;
 assertEquals("html", html3.tagName());
 Element head3 = html3.childNodes().get(0);
 Element title3 = head3.childNodes().get(0);
 assertEquals("Test Title", title3.text());
 Element body3 = html3.childNodes().get(1);
 Element p3 = body3.childNodes().get(0);
 assertEquals("p", p3.tagName());
 assertTrue(p3.text().contains("Test Paragraph"));
 assertTrue(p3.text().contains("\n"));
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<p>Test Paragraph</p>", "", true);
 Document doc = parser.parse();
 assertEquals("", doc.head().text());
 assertTrue(doc.body().hasText());
 assertEquals("p", doc.body().childNodes().get(0).tagName());
 assertEquals("Test Paragraph", doc.body().childNodes().get(0).text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<div><p>Test Paragraph</div>", "", true);
 Document doc = parser.parse();
 assertEquals("", doc.head().text());
 assertTrue(doc.body().hasText());
 Element p = doc.body().childNodes().get(0);
 assertEquals("p", p.tagName());
 assertEquals("Test Paragraph", p.text());
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidHtml() {
 Parser parser = new Parser("</html>", "", false);
 parser.parse(); // should throw AssertionError
 }
}