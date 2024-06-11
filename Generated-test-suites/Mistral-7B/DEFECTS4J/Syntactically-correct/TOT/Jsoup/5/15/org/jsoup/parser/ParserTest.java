package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

 private Parser parser;

 @Before
 public void setUp() {
 parser = new Parser("", "http://localhost/", false); // initialize an instance of Parser with empty HTML and a base URI
 }

 /**
 * Test parsing a simple HTML document.
 */
 @Test
 public void testParseSimpleHtml() {
 String html = "<html><head><title>Simple HTML</title></head>" +
 "<body><h1>Welcome to JSoup</h1></body></html>";
 Document doc = Parser.parse(html, "http://localhost/");
 Element title = doc.title();
 Element h1 = doc.body().child(0);
 assertEquals("Simple HTML", title.text());
 assertEquals("Welcome to JSoup", h1.text());
 }

 /**
 * Test parsing a HTML document with an empty body fragment.
 */
 @Test
 public void testParseBodyFragmentEmpty() {
 String bodyHtml = "";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://localhost/");
 assertTrue(doc.head().isEmpty());
 assertTrue(doc.body().isEmpty());
 }

 /**
 * Test parsing a HTML document with a body fragment.
 */
 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Hello World!</p>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://localhost/");
 Element p = doc.body().child(0);
 assertEquals("Hello World!", p.text());
 }

 /**
 * Test parsing a HTML document with a body fragment and relaxed parsing.
 */
 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<p><script type=\"text/javascript\">alert('Hello!');</script>Hello World!";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://localhost/");
 Element p = doc.body().child(0);
 // The script tag is not parsed as a DataNode, but as an Element with the tag name "script".
 assertEquals(2, p.childSize());
 Element script = p.child(0);
 assertTrue(script.tagName().equals("script"));
 }

 /**
 * Test parsing a HTML document with an invalid start tag.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidStartTag() {
 String html = "<html<head><title>Simple HTML</title></html>";
 Parser.parse(html, "http://localhost/"); // This should throw an IllegalArgumentException
 }

 /**
 * Test parsing a HTML document with an invalid end tag.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidEndTag() {
 String html = "<html><head><title>Simple HTML</title></body></html>"; // The body tag should not close here
 Document doc = Parser.parse(html, "http://localhost/");
 Element body = doc.body();
 // The body element should have no children (it should not be closed yet).
 assertTrue(body.childSize() == 0);
 }
}